/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author allan
 */
public class Servidor {

    private static int PORTATCP = 12345;
    private static int PORTAUDP = 3456;
    static Handler han = Handler.getInstance();

    public static void main(String[] args) throws IOException {
        ouvirTCP();
        ouvirUDP();
    }

    public static void ouvirTCP() throws IOException {
        boolean erro = true;
        while (erro) {
            try {
                SSLServerSocket server;
                SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
                server = (SSLServerSocket) factory.createServerSocket(PORTATCP);
                
                erro = false;
                new Thread() {
                    @Override
                    public void run() {
                        while (true) {
                            System.out.println("Esperando cliente TCP na porta = " + PORTATCP + "......");
                            
                            try {
                                SSLSocket cliente = (SSLSocket) server.accept();
                                System.out.println("cliente conectado");
                                ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());
                                ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
                                ThreadTCP thread = new ThreadTCP(cliente, in, out, han);
                                Thread t = new Thread(thread);
                                t.start();
                            } catch (IOException ex) {
                                
                            }
                        }
                    }
                }.start();
            } catch (BindException be) {
                PORTATCP++;
            }
        }
        
        //Server thread = new Server(servidor);
        //Thread t = new Thread(thread);
        //t.start
        
        
    }

    public static void ouvirUDP() throws IOException {

        InetAddress grupo = InetAddress.getByName("225.4.5.6");
        MulticastSocket multiReceiver = new MulticastSocket(PORTAUDP);
        multiReceiver.joinGroup(grupo);
        MulticastSocket multiSender = new MulticastSocket();
        
        new Thread() {
            @Override
            public void run() {
                //while (true) {
                    
                    System.out.println("Esperando cliente UDP......");
                    
                    //System.out.println("Datagrama UDP [" + numConn + "] recebido...");
                    ThreadUDP thr = new ThreadUDP(multiSender, multiReceiver, han, grupo, PORTAUDP);
                    Thread t = new Thread(thr);
                    t.start();
                //}
            }
        }.start();

    }

}

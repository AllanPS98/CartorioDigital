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
import java.net.InetAddress;
import java.net.MulticastSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author allan
 */
public class Servidor {
    private static final String nomepathWindowsKey = "ssl\\servidorCartorio.key";
    private static final String nomepathWindowsTrust = "ssl\\servidorCartorioTrustStore.key";
    private static final String nomepathLinuxKey = "ssl//servidorCartorio.key";
    private static final String nomepathLinuxTrust = "ssl//servidorCartorioTrustStore.key";
    private static int PORTATCP = 12345;
    private static int PORTAUDP = 3456;
    static Handler han = Handler.getInstance();

    public static void main(String[] args) throws IOException {
        //System.setProperty("javax.net.debug", "all");
        System.setProperty("javax.net.ssl.keyStore", nomepathWindowsKey);
        System.setProperty("javax.net.ssl.keyStorePassword", "allanpereira11");
        System.setProperty("javax.net.ssl.trustStore", nomepathWindowsTrust);
        System.setProperty("javax.net.ssl.trustStorePassword", "allanpereira11");
        ouvirTCP();
        ouvirUDPCidadao();
        ouvirUDPDocumento();
        ouvirUDPTransferencia();
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
    }

    public static void ouvirUDPCidadao() throws IOException {
        new Thread() {
            @Override
            public void run() {
                System.out.println("Esperando Datagram Cidadão......");
                MulticastReceiverCidadao receiver = new MulticastReceiverCidadao(han);
                Thread t = new Thread(receiver);
                t.start();
            }
        }.start();
    }

    public static void ouvirUDPDocumento() throws IOException {
        new Thread() {
            @Override
            public void run() {
                System.out.println("Esperando Datagram Documento......");
                MulticastReceiverDocumento receiver = new MulticastReceiverDocumento(han);
                Thread t = new Thread(receiver);
                t.start();
            }
        }.start();
    }
    
    public static void ouvirUDPTransferencia(){
        new Thread() {
            @Override
            public void run() {
                System.out.println("Esperando Datagram Transferência......");
                MulticastReceiverTransferencia receiver = new MulticastReceiverTransferencia(han);
                Thread t = new Thread(receiver);
                t.start();
            }
        }.start();
    }

}

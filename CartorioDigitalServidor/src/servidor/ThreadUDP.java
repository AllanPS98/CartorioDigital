/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;


import cliente.Cidadao;
import cliente.Documento;
import cliente.Transferencia;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class ThreadUDP implements Runnable {

    
    private DatagramPacket receive;
    private final MulticastSocket multiSender;
    private final MulticastSocket multiReceiver;
    private DatagramPacket send;
    private static Handler han;
    private InetAddress grupo;
    private int porta;
    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];

    ThreadUDP(MulticastSocket multiSender, MulticastSocket multiReceiver, Handler han, InetAddress grupo, int porta ) {
        this.multiReceiver = multiReceiver;
        this.multiSender = multiSender;
        ThreadUDP.han = han;
        this.grupo = grupo;
        this.porta = porta;
        
    }
    

    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);
        String msg1 = scan.nextLine();
        output(msg1);
        String msg;
        while(true){
            try {
                msg = (String) input();
                System.out.println("Mensagem = "+ msg);
            } catch (IOException ex) {
                Logger.getLogger(ThreadUDP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

    }
    
    public Object input() throws IOException{
        receive = new DatagramPacket(receiveData, receiveData.length);
        multiReceiver.receive(receive);
        return new String(receiveData);
    }
    
    public void output(String msg){
        try {
            send = new DatagramPacket(msg.getBytes(), msg.length(), grupo, porta);
            multiSender.send(send);
        } catch (IOException ex) {
            Logger.getLogger(ThreadUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * metódo responsável por sincronizar dados de cadastro de usuários com todos os servidores via multicast
     * @param cidadao 
     */
    public void sincronizarCadastroUsuario(Cidadao cidadao){
        output("1");
        output(cidadao.toString());
    }
    /**
     * método responsável por sincronizar dados de cadastro de documentos com todos os servidores via multicast
     * @param doc
     * @param cid 
     */
    public void sincronizarCadastroDocumento(Documento doc, Cidadao cid){
        output("2");
        output(cid.toString());
        output(doc.toString());
    }   
    /**
     * método responsável por sincronizar dados de cadastro de transferencia de documentos com todos os 
     * servidores via multicast
     * @param transf 
     */
    public void sincronizarTransferenciaDocumento(Transferencia transf){
        output("3");
        output(transf.toString());
    }
    


}

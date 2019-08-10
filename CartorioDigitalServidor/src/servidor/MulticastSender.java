/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 *
 * @author Allan Pereira da Silva
 */
public class MulticastSender {

    public MulticastSender() {
    
    }
    
    
    /**
     * Manda uma mensagem multicast para o grupo de servidores cidadão
     * @param msg
     * @throws UnknownHostException
     * @throws IOException 
     */
    public void outputCidadao(String msg) throws UnknownHostException, IOException{
        InetAddress grupo = InetAddress.getByName("225.4.5.6");
        MulticastSocket multi = new MulticastSocket();
        DatagramPacket pkt = new DatagramPacket(msg.getBytes(), msg.length(), grupo, 3456);
        multi.send(pkt);
        multi.close();
    }
    /**
     * Manda uma mensagem multicast para o grupo de servidores documento
     * @param msg
     * @throws UnknownHostException
     * @throws IOException 
     */
    public void outputDocumento(String msg) throws UnknownHostException, IOException{
        InetAddress grupo = InetAddress.getByName("225.4.5.7");
        MulticastSocket multi = new MulticastSocket();
        DatagramPacket pkt = new DatagramPacket(msg.getBytes(), msg.length(), grupo, 3457);
        multi.send(pkt);
        multi.close();
    }
    /**
     * Manda uma mensagem multicast para o grupo de servidores transferência
     * @param msg
     * @throws UnknownHostException
     * @throws IOException 
     */
    public void outputTransferencia(String msg) throws UnknownHostException, IOException{
        InetAddress grupo = InetAddress.getByName("225.4.5.8");
        MulticastSocket multi = new MulticastSocket();
        DatagramPacket pkt = new DatagramPacket(msg.getBytes(), msg.length(), grupo, 3458);
        multi.send(pkt);
        multi.close();
    }
    
}

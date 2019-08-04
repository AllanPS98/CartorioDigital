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
 * @author User
 */
public class MulticastSender {

    public MulticastSender() {
    
    }
    
    
    
    public void output(String msg) throws UnknownHostException, IOException{
        InetAddress grupo = InetAddress.getByName("225.4.5.6");
        MulticastSocket multi = new MulticastSocket();
        DatagramPacket pkt = new DatagramPacket(msg.getBytes(), msg.length(), grupo, 3456);
        multi.send(pkt);
        multi.close();
    }
    
}

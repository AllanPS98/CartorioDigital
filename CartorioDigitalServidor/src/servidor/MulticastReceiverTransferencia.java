/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import cliente.Documento;
import cliente.Transferencia;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class MulticastReceiverTransferencia implements Runnable{
    
    private static Handler han;

    public MulticastReceiverTransferencia(Handler han) {
        MulticastReceiverTransferencia.han = han;
    }
    
    @Override
    public void run() {
        InetAddress grupo;
        MulticastSocket multi;
        try {
            grupo = InetAddress.getByName("225.4.5.8");
            multi = new MulticastSocket(3458);
            multi.joinGroup(grupo);
            byte[] buff = new byte[1000];
            DatagramPacket pkt = new DatagramPacket(buff, buff.length);
            while (true) {
                multi.receive(pkt);
                String transf = new String(buff);
                System.out.println(transf);
                Transferencia transfer = transformaTransferenciaEmObjeto(transf);
                String resultado = han.cadastrarTransferencia(transfer);
                System.out.println(resultado);
                System.out.println("Transferência sincronizada");
            }
        } catch (IOException ex) {
            Logger.getLogger(MulticastReceiverCidadao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Transferencia transformaTransferenciaEmObjeto(String transf){
        String[] particionada;
        particionada = transf.split("#");
        System.out.println(Arrays.toString(particionada));
        
        String[] particionada2;
        particionada2 = particionada[0].split(";");
        float valor = Float.parseFloat(particionada[2]);
        Documento doc = transformaDocumentoEmObjeto(particionada[1], valor);
        Transferencia transfer = new Transferencia(particionada2[0], particionada2[1], doc, 
                valor, particionada[3]);
        return transfer;
    }
    
    
    private Documento transformaDocumentoEmObjeto(String doc, float valor){
        System.out.println(doc);
        String[] particionada = doc.split(";");
        String id = particionada[0];
        String proprietario = particionada[1];
        String cpf_prop = particionada[2];
        String texto = particionada[3];
        Documento documento = new Documento(id, proprietario, cpf_prop, texto, valor);
        return documento;
    }
}

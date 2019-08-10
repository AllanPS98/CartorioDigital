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
 * @author Allan Pereira da Silva
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
            grupo = InetAddress.getByName("225.4.5.8"); // definindo o IP do grupo
            multi = new MulticastSocket(3458); // e a porta
            multi.joinGroup(grupo);// entra no grupo
            byte[] buff = new byte[1000];// crio um vetor de 1000 bytes
            DatagramPacket pkt = new DatagramPacket(buff, buff.length);// crio o pacote a ser recebido
            while (true) {
                multi.receive(pkt); // recebo o pacote
                String transf = new String(buff); // transformo os bytes recebidos em string
                System.out.println(transf);
                Transferencia transfer = transformaTransferenciaEmObjeto(transf); // transformo a string em objeto
                String resultado = han.cadastrarTransferencia(transfer);
                System.out.println(resultado);
                System.out.println("Transferência sincronizada");
            }
        } catch (IOException ex) {
            Logger.getLogger(MulticastReceiverCidadao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Transforma uma string em um objeto Transferência
     * @param transf
     * @return 
     */
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
    
    /**
     * Recebe a string de um documento e a transforma em um objeto Documento
     * @param doc
     * @return 
     */
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

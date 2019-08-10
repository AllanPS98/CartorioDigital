/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import cliente.Cidadao;
import cliente.Documento;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class MulticastReceiverDocumento implements Runnable{
    
    private static Handler han;

    MulticastReceiverDocumento(Handler han) {
        MulticastReceiverDocumento.han = han;
    }
    
    @Override
    public void run() {
        InetAddress grupo;
        MulticastSocket multi;
        try {
            grupo = InetAddress.getByName("225.4.5.7");
            multi = new MulticastSocket(3457);
            multi.joinGroup(grupo);
            byte[] buff = new byte[1000];
            DatagramPacket pkt = new DatagramPacket(buff, buff.length);
            while (true) {
                multi.receive(pkt);
                String doc = new String(buff);
                Documento docx = transformaDocumentoEmObjeto(doc);
                docx.setTexto(han.decodificarTexto(docx.getTexto()));
                boolean podeCadastrar = true;
                for(int i = 0; i < Handler.usuarios.size(); i++){
                    if(docx.getCpf_proprietario().equals(Handler.usuarios.get(i).getCpf())){
                        for(int j = 0; j < Handler.usuarios.get(i).getDocumentos().size(); j++){
                            if(docx.getId().equals(Handler.usuarios.get(i).getDocumentos().get(j).getId())){
                                podeCadastrar = false;
                            }
                        }
                        if(podeCadastrar){
                            han.cadastrarDocumento(docx);
                            System.out.println("Documento do cidadão "+docx.getProprietario()+"de ID = "
                                    +docx.getId()+" sincronizado.");
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MulticastReceiverCidadao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Documento transformaDocumentoEmObjeto(String doc){
        String[] particionada = doc.split(";");
        String id = particionada[0];
        String proprietario = particionada[1];
        String cpf_prop = particionada[2];
        String texto = particionada[3];
        Documento documento = new Documento(id, proprietario, cpf_prop, texto);
        return documento;
    }
}

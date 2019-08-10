/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import cliente.Documento;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Allan Pereira da Silva
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
            grupo = InetAddress.getByName("225.4.5.7"); // definindo o IP do grupo
            multi = new MulticastSocket(3457); // e a porta
            multi.joinGroup(grupo); // entra no grupo
            byte[] buff = new byte[1000]; // crio um vetor de 1000 bytes
            DatagramPacket pkt = new DatagramPacket(buff, buff.length); // crio o pacote a ser recebido
            while (true) {
                multi.receive(pkt); // recebo o pacote
                String doc = new String(buff); // transformo os bytes recebidos em string
                Documento docx = transformaDocumentoEmObjeto(doc); // transformo a string em objeto
                docx.setTexto(han.decodificarTexto(docx.getTexto())); //decodifico o texto do documento
                // cadastro (ou não) o documento
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
    /**
     * Recebe a string de um documento e a transforma em um objeto Documento
     * @param doc
     * @return 
     */
    private Documento transformaDocumentoEmObjeto(String doc){
        String[] particionada = doc.split(";");
        String id = particionada[0];
        String proprietario = particionada[1];
        String cpf_prop = particionada[2];
        String texto = particionada[3];
        float valor = Float.parseFloat(particionada[4]);
        Documento documento = new Documento(id, proprietario, cpf_prop, texto, valor);
        return documento;
    }
}

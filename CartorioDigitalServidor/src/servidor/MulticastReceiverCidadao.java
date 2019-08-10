/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import cliente.Cidadao;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Allan Pereira da Silva
 */
public class MulticastReceiverCidadao implements Runnable {

    private static Handler han;

    public MulticastReceiverCidadao(Handler han) {
        MulticastReceiverCidadao.han = han;
    }

    @Override
    public void run() {
        InetAddress grupo;
        MulticastSocket multi;
        try {
            grupo = InetAddress.getByName("225.4.5.6"); // definindo o IP do grupo
            multi = new MulticastSocket(3456); // e a porta
            multi.joinGroup(grupo); // entra no grupo
            byte[] buff = new byte[1000]; // crio um vetor de 1000 bytes
            DatagramPacket pkt = new DatagramPacket(buff, buff.length); // crio o pacote a ser recebido
            while (true) {
                multi.receive(pkt); // recebo o pacote
                String cidadao = new String(buff); // transformo os bytes recebidos em string
                Cidadao cid = transformaCidadaoEmObjeto(cidadao); // transformo a string em objeto
                //ThreadTCP.carregarDados();
                // cadastro o cidadão (ou não)
                boolean naoPodeCadastrar = false;
                for (int i = 0; i < Handler.usuarios.size(); i++) {
                    System.out.println(Handler.usuarios.get(i).toString());
                    if (cid.getCpf().equals(Handler.usuarios.get(i).getCpf())) {
                        naoPodeCadastrar = true;
                    }
                }
                if (!naoPodeCadastrar) {
                    han.cadastrarUsuario(cid);
                    System.out.println("Cidadao " + cid.getCpf() + "/" + cid.getNome() + "/" + cid.getSenha()+" sincronizado");
                }
                
            }
        } catch (IOException | NoSuchAlgorithmException ex) {
            Logger.getLogger(MulticastReceiverCidadao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /**
     * Recebe uma string e transforma em um objeto Cidadão
     * @param cidadao
     * @return 
     */
    public Cidadao transformaCidadaoEmObjeto(String cidadao) {
        String[] particionada;
        particionada = cidadao.split(";");
        String nome = particionada[0];
        String cpf = particionada[1];
        String senha = particionada[2];
        Cidadao cid = new Cidadao(nome, cpf, senha);
        return cid;
    }

}

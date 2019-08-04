/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import cliente.Cidadao;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
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
            grupo = InetAddress.getByName("225.4.5.6");
            multi = new MulticastSocket(3456);
            multi.joinGroup(grupo);
            byte[] buff = new byte[1000];
            DatagramPacket pkt = new DatagramPacket(buff, buff.length);
            while (true) {
                multi.receive(pkt);
                String cidadao = new String(buff);
                Cidadao cid = transformaCidadaoEmObjeto(cidadao);
                //ThreadTCP.carregarDados();
                boolean naoPodeCadastrar = false;
                for (int i = 0; i < Handler.usuarios.size(); i++) {
                    System.out.println(Handler.usuarios.get(i).toString());
                    if (cid.getCpf().equals(Handler.usuarios.get(i).getCpf())) {
                        naoPodeCadastrar = true;
                    }
                }
                if (!naoPodeCadastrar) {
                    han.cadastrarUsuario(cid);
                    System.out.println("Cidadao " + cid.getCpf() + "/" + cid.getNome() + " sincronizado");
                }
                
            }
        } catch (IOException | NoSuchAlgorithmException ex) {
            Logger.getLogger(MulticastReceiverCidadao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

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

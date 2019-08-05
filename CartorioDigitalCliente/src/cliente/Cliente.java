/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class Cliente {
    ObjectOutputStream out;
    ObjectInputStream in;
    Cidadao usuario;
    SSLSocket cliente;
    SSLSocketFactory factory;
    

    public void cliente(String ip, int porta) throws IOException {
        
        factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        cliente = (SSLSocket) factory.createSocket(ip, porta);
        //cliente.startHandshake();
        out = new ObjectOutputStream(cliente.getOutputStream());
        in = new ObjectInputStream(cliente.getInputStream());
        System.out.println("CLIENTE CONECTADO");

    }
    
    
    public void sair() {
        try {
            System.out.println("entrou no sair");
            output(Protocolo.SAIR);
            //out.close();
            //in.close();
            //cliente.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("CLIENTE DESCONECTADO !!!!!");
    }
    
    private Object desserializarMensagem(byte[] data) {
        ByteArrayInputStream mensagem = new ByteArrayInputStream(data);

        try {
            ObjectInput leitor = new ObjectInputStream(mensagem);
            return (Object) leitor.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block

        }

        return null;
    }

    public byte[] serializarMensagens(Object mensagem) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try {
            ObjectOutput saida = new ObjectOutputStream(b);
            saida.writeObject(mensagem);
            saida.flush();
            return b.toByteArray();
        } catch (IOException e) {
            System.err.println("Erro no envio/recebimento dos dados");
        }
        return null;

    }
    
    public void output(Object msg) throws IOException {
        out.flush();
        out.write(serializarMensagens(msg));
        out.reset();
    }

    public Object input() throws IOException, ClassNotFoundException {
        try {
            InputStream tcpInput = new ObjectInputStream(in);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream obj = new ObjectOutputStream(bytes);

            try {
                obj.writeObject(((ObjectInputStream) tcpInput).readObject());
                bytes.toByteArray();
                return desserializarMensagem(bytes.toByteArray());
            } catch (ClassNotFoundException e) {

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block

        }
        return null;
    }
    
    
    
    public String login(String cpf, String senha){
        String resultado = "";
        try {
            output(Protocolo.LOGIN);
            output(cpf);
            output(senha);
            resultado = (String) input();
            if(resultado.equals("Login efetuado com sucesso")){
                
            }else{
                JOptionPane.showMessageDialog(null, resultado);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro de IO!");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Erro de Class!");
        }
        return resultado;
        
    }
    
    public String cadastrarSe(String nome, String cpf, String senha, String confirmaSenha) throws IOException, ClassNotFoundException{
        output(Protocolo.CADASTRAR_USUARIO);
        output(nome);
        output(cpf);
        output(senha);
        output(confirmaSenha);
        String resultado = (String) input();
        return resultado;
    }

    public String cadastrarDocumento(String loginAux, String text, String latitude, String longitude) {
        String resultado = "";
        try {
            output(Protocolo.CADASTRAR_DOCUMENTO);
            output(latitude+"/"+longitude);
            output(loginAux);
            output(text);
            resultado = (String) input();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro de IO!");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Erro de Class!");
        }
        
        return resultado;
    }
}

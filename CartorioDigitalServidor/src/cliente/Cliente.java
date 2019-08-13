/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author Allan Pereira da Silva
 */
public class Cliente {
    ObjectOutputStream out;
    ObjectInputStream in;
    Cidadao usuario;
    SSLSocket cliente;
    SSLSocketFactory factory;
    public static List<Documento> docs;
    public static List<Transferencia> transfs;
    
    /**
     * Criação do cliente no padrão SSL/TLS de comunicação
     * @param ip
     * @param porta
     * @throws IOException 
     */
    public void cliente(String ip, int porta) throws IOException {
        factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        cliente = (SSLSocket) factory.createSocket(ip, porta);
        //cliente.startHandshake();
        out = new ObjectOutputStream(cliente.getOutputStream());
        in = new ObjectInputStream(cliente.getInputStream());
        System.out.println("CLIENTE CONECTADO");
    }
    
    /**
     * Método que invoca o protocolo sair, para dizer ao servidor que será desconectado
     */
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
    
    /**
     * Método recebe um array de bytes e transforma em objeto
     * @param data
     * @return 
     */
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
    /**
     * Método recebe um objeto e o transforma em um array de bytes
     * @param mensagem
     * @return 
     */
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
    
    /**
     * Método responsável por enviar outputs ao servidor
     * @param msg
     * @throws IOException 
     */
    public void output(Object msg) throws IOException {
        out.flush();
        out.write(serializarMensagens(msg));
        out.reset();
    }
    /**
     * Método responsável por receber mensagens do servidor
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
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
    
    
    /**
     * Método que recebe os parâmetros necessários para fazer um login de um usuário e faz o pedido ao servidor
     * @param cpf
     * @param senha
     * @return 
     */
    public String login(String cpf, String senha){
        String resultado = "";
        try {
            output(Protocolo.LOGIN);
            output(cpf);
            output(senha);
            resultado = (String) input();
            if(resultado.equals("Login efetuado com sucesso.")){
                
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
    /**
     * Método responsável por pedir ao servidor para cadastrar um usuário
     * @param nome
     * @param cpf
     * @param senha
     * @param confirmaSenha
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public String cadastrarSe(String nome, String cpf, String senha, String confirmaSenha) throws IOException, ClassNotFoundException{
        output(Protocolo.CADASTRAR_USUARIO);
        output(nome);
        output(cpf);
        output(senha);
        output(confirmaSenha);
        String resultado = (String) input();
        return resultado;
    }
    /**
     * Método responsável por pedir ao servidor para fazer o cadastro de um documento
     * @param loginAux
     * @param text
     * @param latitude
     * @param longitude
     * @param valor
     * @return 
     */
    public String cadastrarDocumento(String loginAux, String text, String latitude, String longitude, String valor) {
        String resultado = "";
        try {
            output(Protocolo.CADASTRAR_DOCUMENTO);
            output(latitude+"/"+longitude);
            output(loginAux);
            output(text);
            float valorDoc = Float.parseFloat(valor);
            output(valorDoc);
            resultado = (String) input();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro de IO!");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Erro de Class!");
        }
        
        return resultado;
    }
    /**
     * Método responsável por solicitar ao servidor uma lista de documentos do usuário logado
     * @param cpf
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void carregarListaDocumentos(String cpf) throws IOException, ClassNotFoundException{
        output(Protocolo.CARREGAR_LISTA_DOCUMENTOS);
        output(cpf);
        docs = (List<Documento>) input();
    }
    /**
     * Método que requisita que o servidor decodifique uma mensagem
     * @param texto
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public String decodificarTextoDoc(String texto) throws IOException, ClassNotFoundException{
        output(Protocolo.DECODIFICAR_DOC);
        output(texto);
        String decod = (String) input();
        return decod;
    }
    /**
     * Método que pede para o servidor fazer uma transferência de documento
     * @param cpfVend
     * @param cpfComp
     * @param doc
     * @param valor
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public String enviarTransferencia(String cpfVend, String cpfComp, Documento doc, float valor) throws IOException, ClassNotFoundException{
        output(Protocolo.TRANSFERIR_DOCUMENTO);
        output(cpfVend+";"+cpfComp);
        output(doc);
        output(valor);
        String resultado = (String) input();
        return resultado;
    }
    /**
     * Método que solicita ao servidor uma lista de transferências
     * @param cpf
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void carregarListaTransf(String cpf) throws IOException, ClassNotFoundException{
        output(Protocolo.CARREGAR_LISTA_TRANSF);
        output(cpf);
        transfs = (List<Transferencia>) input();
    }
    /**
     * Método responsável por enviar um sinal de "recusar transferência" ao servidor
     * @param transf
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public String recusarTransferencia(Transferencia transf) throws IOException, ClassNotFoundException{
        output(Protocolo.RECUSAR_TRANSF);
        output(transf);
        String resultado = (String) input();
        return resultado;
    }
    
    public LinkedList<Cidadao> sincronizar() throws IOException, ClassNotFoundException{
        output(Protocolo.SINCRONIZAR);
        return (LinkedList<Cidadao>) input();
    }
}

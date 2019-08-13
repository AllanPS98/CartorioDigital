/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import cliente.Cidadao;
import cliente.Documento;
import cliente.Transferencia;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Allan Pereira da Silva
 */
public class Handler {

    public static Handler han;
    public static List<Cidadao> usuarios = new LinkedList<Cidadao>();
    private static final String PATH = "dados\\usuarios";

    private Handler() {

    }
    /**
     * Método que cadastra um usuário
     * @param nome
     * @param cpf
     * @param senha
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException 
     */
    public void cadastrarUsuario(String nome, String cpf, String senha) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Cidadao cid = new Cidadao(nome, cpf, criptografarSenha(senha));
        usuarios.add(cid);
        // verifico se a lista de documentos dele/dela é vazia e então crio uma
        if (usuarios.get(usuarios.size() - 1).getDocumentos() == null) {
            usuarios.get(usuarios.size() - 1).criarListaDocsVazia();
        }
        // verifico se a lista de transferências dele/dela é vazia e então crio uma
        if (usuarios.get(usuarios.size() - 1).getTransferencias() == null) {
            usuarios.get(usuarios.size() - 1).criarListaTransfVazia();
        }
        try {
            escreverArquivoSerial(PATH, usuarios);

        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Método responsável por cadastrar um cidadão recebendo um objeto cidadão
     * @param cid
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException 
     */
    public void cadastrarUsuario(Cidadao cid) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        usuarios.add(cid);
        if (usuarios.get(usuarios.size() - 1).getDocumentos() == null) {
            usuarios.get(usuarios.size() - 1).criarListaDocsVazia();
        }
        if (usuarios.get(usuarios.size() - 1).getTransferencias() == null) {
            usuarios.get(usuarios.size() - 1).criarListaTransfVazia();
        }
        try {
            escreverArquivoSerial(PATH, usuarios);

        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Método responsável por cadastrar um documento
     * @param doc
     * @return 
     */
    public String cadastrarDocumento(Documento doc) {
        //codifico o campo do texto do documento antes de salvá-lo
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date datax = new Date();
        String data = sdf.format(datax);
        doc.setData(data);
        String docCodificado = codificarTexto(doc.getTexto());
        doc.setTexto(docCodificado);
        for (int i = 0; i < usuarios.size(); i++) {
            if (doc.getCpf_proprietario().equals(usuarios.get(i).getCpf())) { // se existe algum usuário com o mesmo cpf
                usuarios.get(i).getDocumentos().add(doc);                     // adicione o documento nesse usuário
                try {
                    escreverArquivoSerial(PATH, usuarios);
                    return "Documento cadastrado com sucesso";
                } catch (IOException ex) {
                    Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return "Erro ao cadastrar documento";
    }
    /**
     * Método que cadastra uma transferência e manda o documento para o usuário que será (ou não) o comprador
     * @param transfer
     * @return
     * @throws IOException 
     */
    public String cadastrarTransferencia(Transferencia transfer) throws IOException {
        String resultado = null;
        boolean podeTransferir = true;
        boolean existeUsuario = false;
        for (int i = 0; i < usuarios.size(); i++) {
            if (transfer.getCpf_comprador().equals(usuarios.get(i).getCpf())) {
                existeUsuario = true;
                for (int j = 0; j < usuarios.get(i).getTransferencias().size(); j++) {
                    if (transfer.getDocumento().getId().equals(usuarios.get(i).getTransferencias().get(j).getDocumento().getId())) {
                        podeTransferir = false;
                        resultado = "Transferência ilegal.";
                    }
                }
            }
        }
        if (podeTransferir && existeUsuario) {
            for (int i = 0; i < usuarios.size(); i++) {
                if (transfer.getCpf_comprador().equals(usuarios.get(i).getCpf())) {
                    usuarios.get(i).getTransferencias().add(transfer);
                    break;
                }
            }
            resultado = "Transferência parcialmente feita\nAgora o comprador precisa confirmar.";
            for (int i = 0; i < usuarios.size(); i++) {
                if (transfer.getCpf_vendedor().equals(usuarios.get(i).getCpf())) {
                    for (int j = 0; j < usuarios.get(i).getDocumentos().size(); j++) {
                        if (usuarios.get(i).getDocumentos().get(j).getId().equals(transfer.getDocumento().getId())) {
                            usuarios.get(i).getDocumentos().remove(j);
                        }
                    }
                }
            }
        }
        if(!existeUsuario){
            resultado = "Não existe usuário com este CPF";
        }
        escreverArquivoSerial(PATH, usuarios);
        return resultado;
    }
    /**
     * Método que recusa um transferência, ou seja, envia de volta para o usuário vendedor o documento
     * @param transf
     * @return
     * @throws IOException 
     */
    public String recusarTransferencia(Transferencia transf) throws IOException {
        Documento docRejeitado;
        Transferencia transfRejeitada = new Transferencia();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCpf().equals(transf.getCpf_comprador())) {
                for (int j = 0; j < usuarios.get(i).getTransferencias().size(); j++) {
                    if (usuarios.get(i).getTransferencias().get(j).getDocumento().getId().equals(transf.getDocumento().getId())) {
                        transfRejeitada = usuarios.get(i).getTransferencias().remove(j);
                        
                    }
                }
            }
        }
        docRejeitado = transfRejeitada.getDocumento();
        for (int i = 0; i < usuarios.size(); i++) {
            if (transf.getCpf_vendedor().equals(usuarios.get(i).getCpf())) {
                usuarios.get(i).getDocumentos().add(docRejeitado);
            }
        }
        escreverArquivoSerial(PATH, usuarios);
        return "Transferência rejeitada.";
    }
    /**
     * Singleton 
     * @return 
     */
    public static Handler getInstance() {
        if (han == null) {
            han = new Handler();
        }
        return han;
    }
    /**
     * Método que armazena os dados em um arquivo
     * @param nome
     * @param obj
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void escreverArquivoSerial(String nome, Object obj) throws FileNotFoundException, IOException {
        //Classe responsavel por inserir os objetos
        try (FileOutputStream arquivo = new FileOutputStream(nome)) {
            //Grava o objeto cliente no arquivo
            try ( //Classe responsavel por inserir os objetos
                    ObjectOutputStream objGravar = new ObjectOutputStream(arquivo)) {
                //Grava o objeto cliente no arquivo
                objGravar.writeObject(obj);
                objGravar.flush();
            }
            arquivo.flush();
        }
    }
    /**
     * Método que ler os dados do arquivo
     * @param nome
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void lerArquivoSerial(String nome) throws FileNotFoundException, IOException, ClassNotFoundException {
        Object obj;
        try {
            // Classe responsavel por recuperar os objetos do arquivo
            try (FileInputStream arquivo = new FileInputStream(nome); // Classe responsavel por recuperar os objetos do arquivo
                    ObjectInputStream leitura = new ObjectInputStream(arquivo)) {
                obj = leitura.readObject();
            }
            if (nome.equals(PATH)) {
                usuarios = (List<Cidadao>) obj;
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Ainda não existe nenhum arquivo com esse caminho => " + nome);
        }

    }
    /**
     * Método que criptografa uma senha usando SHA-256
     * @param senha
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException 
     */
    public String criptografarSenha(String senha) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
        byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));

        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02X", 0xFF & b));
        }
        String senhaCrip = hexString.toString();
        return senhaCrip;
    }
    /**
     * Método que verifica se a criptografia de uma senha é igual a senha criptografada que está armazenada no arquivo
     * @param senhaA
     * @param senhaB
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException 
     */
    public boolean verificarSenha(String senhaA, String senhaB) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return criptografarSenha(senhaA).equals(senhaB);
    }
    /**
     * Método que usa o Base64 do apache commons codec pra codificar um texto
     * @param texto
     * @return 
     */
    public String codificarTexto(String texto) {
        return Base64.encodeBase64String(texto.getBytes());
    }
    /**
     * Método que usa o Base64 do apache commons codec pra decodificar um texto
     * @param texto
     * @return 
     */
    public String decodificarTexto(String texto) {
        byte[] decoded = Base64.decodeBase64(texto.getBytes());
        return new String(decoded);
    }
    
    public static void sincronizar(LinkedList<Cidadao> lista){
        for(int i = 0; i < lista.size(); i++){
            sincronizarDocumentos(lista.get(i).getDocumentos(), lista.get(i));
            sincronizarTransferencias(lista.get(i).getTransferencias(), lista.get(i));
        }
        sincronizarCidadaos(lista);
    }
    
    private static void sincronizarDocumentos(LinkedList<Documento> docs, Cidadao cid){
        for(int i = 0; i < usuarios.size(); i++){
            if(cid.getCpf().equals(usuarios.get(i).getCpf())){
                usuarios.get(i).setDocumentos((LinkedList<Documento>) docs);
            }
        }
    }
    
    private static void sincronizarTransferencias(LinkedList<Transferencia> transfs, Cidadao cid){
        for(int i = 0; i < usuarios.size(); i++){
            if(cid.getCpf().equals(usuarios.get(i).getCpf())){
                usuarios.get(i).setTransferencias((LinkedList<Transferencia>) transfs);
            }
        }
    }
    
    private static void sincronizarCidadaos(LinkedList<Cidadao> cidadaos){
        boolean temIgual;
        for(int i = 0; i < cidadaos.size(); i++){
            temIgual = false;
            for(int j = 0; j < usuarios.size(); j++){
                if(cidadaos.get(i).equals(usuarios.get(j))){
                   temIgual = true; 
                }
            }
            if(!temIgual){
                usuarios.add(cidadaos.get(i));
            }
        }
    }
}

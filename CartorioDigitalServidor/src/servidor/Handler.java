/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import cliente.Cidadao;
import cliente.Documento;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Handler {

    public static Handler han;
    public static List<Cidadao> usuarios = new LinkedList<Cidadao>();
    
    private Handler() {

    }

    public void cadastrarUsuario(String nome, String cpf, String senha) {
        
        Cidadao cid = new Cidadao(nome, cpf, senha);
        usuarios.add(cid);
        if(usuarios.get(usuarios.size()-1).getDocumentos()==null){
            usuarios.get(usuarios.size()-1).criarListaDocsVazia();
        }
        
        try {
            escreverArquivoSerial("dados\\usuarios", usuarios);
            
        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String cadastrarDocumento(Documento doc){
        for(int i = 0; i < usuarios.size(); i++){
            if(doc.getCpf_proprietario().equals(usuarios.get(i).getCpf())){
                usuarios.get(i).getDocumentos().add(doc);
                try {
                    escreverArquivoSerial("dados\\usuarios", usuarios);
                    return "Documento cadastrado com sucesso";
                } catch (IOException ex) {
                    Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        return "Erro ao cadastrar documento";
    }

    public static Handler getInstance() {
        if (han == null) {
            han = new Handler();
        }
        return han;
    }

    public void escreverArquivoSerial(String nome, Object obj) throws FileNotFoundException, IOException {
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

    public void lerArquivoSerial(String nome) throws FileNotFoundException, IOException, ClassNotFoundException {
        Object obj;
        try {
            // Classe responsavel por recuperar os objetos do arquivo
            try (FileInputStream arquivo = new FileInputStream(nome); // Classe responsavel por recuperar os objetos do arquivo
                    ObjectInputStream leitura = new ObjectInputStream(arquivo)) {
                obj = leitura.readObject();
            }
            if (nome.equals("dados\\usuarios")) {
                usuarios = (List<Cidadao>) obj;
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Ainda não existe nenhum arquivo com esse caminho => " + nome);
        }

    }
    
}
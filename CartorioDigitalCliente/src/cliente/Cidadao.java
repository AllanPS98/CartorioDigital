/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author User
 */
public class Cidadao implements Serializable{
    String nome;
    String cpf;
    String senha;
    LinkedList<Documento> documentos;
    LinkedList<Transferencia> transferencias;

    public Cidadao(String nome, String cpf, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        
        this.senha = senha;
    }

    Cidadao() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LinkedList<Documento> getDocumentos() {
        return documentos;
    }

    public LinkedList<Transferencia> getTransferencias() {
        return transferencias;
    }
    
    
    
    @Override
    public String toString() {
        return nome+";"+cpf+";"+senha+";";
    }
}

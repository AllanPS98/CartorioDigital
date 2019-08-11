/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.Serializable;

/**
 *
 * @author Allan Pereira da Silva
 */
public class Documento implements Serializable{
    String id;
    String proprietario;
    String cpf_proprietario;
    String texto;
    String data;
    float valorDoc;

    public Documento(String id, String proprietario, String cpf_proprietario, String texto, float valorDoc) {
        this.id = id;
        this.proprietario = proprietario;
        this.cpf_proprietario = cpf_proprietario;
        this.texto = texto;
        this.valorDoc = valorDoc;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    public String getCpf_proprietario() {
        return cpf_proprietario;
    }

    public void setCpf_proprietario(String cpf_proprietario) {
        this.cpf_proprietario = cpf_proprietario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public float getValorDoc() {
        return valorDoc;
    }

    public void setValorDoc(float valorDoc) {
        this.valorDoc = valorDoc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
    
    
    @Override
    public String toString() {
        return id + ";" + proprietario + ";" + cpf_proprietario + ";" + texto + ";" + data + ";" + valorDoc + ";";
    }
    
    
}

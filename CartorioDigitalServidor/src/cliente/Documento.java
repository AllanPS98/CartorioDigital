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
import java.util.Date;

/**
 *
 * @author User
 */
public class Documento implements Serializable{
    String id;
    String proprietario;
    String cpf_proprietario;
    String texto;
    Date data;

    public Documento(String id, String proprietario, String cpf_proprietario, String texto) {
        this.id = id;
        this.proprietario = proprietario;
        this.cpf_proprietario = cpf_proprietario;
        this.texto = texto;
    }

    public Documento() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public String toString() {
        return id + ";" + proprietario + ";" + cpf_proprietario + ";" + texto + ";" + data + ";";
    }
    
    
}

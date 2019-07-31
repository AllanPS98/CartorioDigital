/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

/**
 *
 * @author User
 */
public class Cartorio {
    String identificacao;
    String proprietario;
    String texto;

    public Cartorio(String identificacao, String proprietario, String texto) {
        this.identificacao = identificacao;
        this.proprietario = proprietario;
        this.texto = texto;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return "Cartorio{" + "identificacao=" + identificacao + ", proprietario=" + proprietario + ", texto=" + texto + '}';
    }
    
    
}

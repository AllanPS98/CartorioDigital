/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class Transferencia implements Serializable{
    String cpf_vendedor;
    String cpf_comprador;
    Documento documento;
    String data;
    float valorVenda;

    public Transferencia(String cpf_vendedor, String cpf_comprador, Documento documento, float valorVenda, String data ) {
        this.cpf_vendedor = cpf_vendedor;
        this.cpf_comprador = cpf_comprador;
        this.documento = documento;
        this.data = data;
        this.valorVenda = valorVenda;
    }

    public Transferencia() {
    }

    public String getCpf_vendedor() {
        return cpf_vendedor;
    }

    public void setCpf_vendedor(String cpf_vendedor) {
        this.cpf_vendedor = cpf_vendedor;
    }

    public String getCpf_comprador() {
        return cpf_comprador;
    }

    public void setCpf_comprador(String cpf_comprador) {
        this.cpf_comprador = cpf_comprador;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public float getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(float valorVenda) {
        this.valorVenda = valorVenda;
    }
    
    
    

    @Override
    public String toString() {
        return cpf_vendedor + ";" + cpf_comprador + ";"+ valorVenda+";" + data + "#" + documento.toString();
    }

    
    
    
    
}

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
public class Transferencia implements Serializable{
    Cidadao vendedor;
    Cidadao comprador;
    Documento documento;
    Date data;

    public Transferencia(Cidadao vendedor, Cidadao comprador, Documento documento, Date data) {
        this.vendedor = vendedor;
        this.comprador = comprador;
        this.documento = documento;
        this.data = data;
    }

    public Cidadao getVendedor() {
        return vendedor;
    }

    public void setVendedor(Cidadao vendedor) {
        this.vendedor = vendedor;
    }

    public Cidadao getComprador() {
        return comprador;
    }

    public void setComprador(Cidadao comprador) {
        this.comprador = comprador;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    
    
}

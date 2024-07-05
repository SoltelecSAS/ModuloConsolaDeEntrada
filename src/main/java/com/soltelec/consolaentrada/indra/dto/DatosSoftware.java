/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.indra.dto;

/**
 *
 * @author usurio
 */
public class DatosSoftware {
    private String nombreAplicacion = "";

    public String getNombreAplicacion() {
        return nombreAplicacion;
    }

    public void setNombreAplicacion(String NombreAplicacion) {
        this.nombreAplicacion = NombreAplicacion;
    }
    
    @Override
    public String toString (){
        return nombreAplicacion;
    }
}

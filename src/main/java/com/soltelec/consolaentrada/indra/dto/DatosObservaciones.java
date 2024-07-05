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
public class DatosObservaciones {

    private String observaciones = "";

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return observaciones;
    }

}

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
public class DatosFurAsociados {

    private String numeroFur = "";
    private String fechaHoraFur = "";

    /**
     * @return the numeroFur
     */
    public String getNumeroFur() {
        return numeroFur;
    }

    /**
     * @param numeroFur the numeroFur to set
     */
    public void setNumeroFur(String numeroFur) {
        this.numeroFur = numeroFur;
    }

    /**
     * @return the fechaHoraFur
     */
    public String getFechaHoraFur() {
        return fechaHoraFur;
    }

    /**
     * @param fechaHoraFur the fechaHoraFur to set
     */
    public void setFechaHoraFur(String fechaHoraFur) {
        this.fechaHoraFur = fechaHoraFur;
    }

    @Override
    public String toString() {
        if ((numeroFur + "" + fechaHoraFur).equals("")) {
            return "";
        } else {
            return numeroFur + ";" + fechaHoraFur;
        }
    }

}

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
public class DatosFirma {

    private String FirmaDirectorTecnico = "";
    private String noIdentificacionDirectorTec = "";

    public String getFirmaDirectorTecnico() {
        return FirmaDirectorTecnico;
    }

    public void setFirmaDirectorTecnico(String FirmaDirectorTecnico) {
        this.FirmaDirectorTecnico = FirmaDirectorTecnico;
    }

    public String getNoIdentificacionDirectorTec() {
        return noIdentificacionDirectorTec;
    }

    public void setNoIdentificacionDirectorTec(String noIdentificacionDirectorTec) {
        this.noIdentificacionDirectorTec = noIdentificacionDirectorTec;
    }

    @Override
    public String toString() {
        return FirmaDirectorTecnico + ";"
                + noIdentificacionDirectorTec;
    }

}

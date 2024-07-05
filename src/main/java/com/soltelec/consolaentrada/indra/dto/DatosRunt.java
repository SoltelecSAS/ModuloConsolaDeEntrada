/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.indra.dto;

/**
 *
 * @author GerenciaDesarrollo
 */
public class DatosRunt {

//    private String noCertificado = "";
//    private String consecutivoRunt = "";
    private String noFur = "";
    private Integer idRunt = 0;
//    private String idRunt = "";

    @Override
    public String toString() {
        return getNoFur() + ";"
                + getIdRunt();
    }
//    public String toString() {
//        return getNoCertificado() + ";"
//                + getConsecutivoRunt() + ";"
//                + getNoFur() + ";"
//                + getIdRunt();
//    }
    
      public String toString1() {
        return "DatosRunt{" + "noFur=" + getNoFur() + ", idRunt=" + getIdRunt() + '}';
    }

//    public String toString1() {
//        return "DatosRunt{" + "noCertificado=" + getNoCertificado() + ", consecutivoRunt=" + getConsecutivoRunt() + ", noFur=" + getNoFur() + ", idRunt=" + getIdRunt() + '}';
//    }

    /**
     * @return the noCertificado
     */
//    public String getNoCertificado() {
//        return noCertificado;
//    }

    /**
     * @param noCertificado the noCertificado to set
     */
//    public void setNoCertificado(String noCertificado) {
//        this.noCertificado = noCertificado;
//    }

    /**
     * @return the consecutivoRunt
     */
//    public String getConsecutivoRunt() {
//        return consecutivoRunt;
//    }

    /**
     * @param consecutivoRunt the consecutivoRunt to set
     */
//    public void setConsecutivoRunt(String consecutivoRunt) {
//        this.consecutivoRunt = consecutivoRunt;
//    }

    /**
     * @return the noFur
     */
    public String getNoFur() {
        return noFur;
    }

    /**
     * @param noFur the noFur to set
     */
    public void setNoFur(String noFur) {
        this.noFur = noFur;
    }

    public Integer getIdRunt() {
        return idRunt;
    }

    public void setIdRunt(Integer idRunt) {
        this.idRunt = idRunt;
    }

    /**
     * @return the idRunt
     */
//    public String getIdRunt() {
//        return idRunt;
//    }

    /**
     * @param idRunt the idRunt to set
     */
    
    
   
    
}

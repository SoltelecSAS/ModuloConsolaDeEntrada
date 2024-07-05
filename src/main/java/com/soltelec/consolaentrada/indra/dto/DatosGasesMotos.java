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
public class DatosGasesMotos extends DatosUsuarioPrueba {

    private String tempRalenti = "";
    private String rpmRalenti = "";
    private String hcRalenti = "";
    private String coRalenti = "";
    private String co2RAlenti = "";
    private String o2Ralenti = "";
    private String temperaturaAmbiente = "";
    private String humedadRelativa = "";

    /**
     * @return the tempRalenti
     */
    public String getTempRalenti() {
        return tempRalenti;
    }

    /**
     * @param tempRalenti the tempRalenti to set
     */
    public void setTempRalenti(String tempRalenti) {
        this.tempRalenti = tempRalenti;
    }

    /**
     * @return the rpmRalenti
     */
    public String getRpmRalenti() {
        return rpmRalenti;
    }

    /**
     * @param rpmRalenti the rpmRalenti to set
     */
    public void setRpmRalenti(String rpmRalenti) {
        this.rpmRalenti = rpmRalenti;
    }

    /**
     * @return the hcRalenti
     */
    public String getHcRalenti() {
        return hcRalenti;
    }

    /**
     * @param hcRalenti the hcRalenti to set
     */
    public void setHcRalenti(String hcRalenti) {
        this.hcRalenti = hcRalenti;
    }

    /**
     * @return the coRalenti
     */
    public String getCoRalenti() {
        return coRalenti;
    }

    /**
     * @param coRalenti the coRalenti to set
     */
    public void setCoRalenti(String coRalenti) {
        this.coRalenti = coRalenti;
    }

    /**
     * @return the co2RAlenti
     */
    public String getCo2RAlenti() {
        return co2RAlenti;
    }

    /**
     * @param co2RAlenti the co2RAlenti to set
     */
    public void setCo2RAlenti(String co2RAlenti) {
        this.co2RAlenti = co2RAlenti;
    }

    /**
     * @return the o2Ralenti
     */
    public String getO2Ralenti() {
        return o2Ralenti;
    }

    /**
     * @param o2Ralenti the o2Ralenti to set
     */
    public void setO2Ralenti(String o2Ralenti) {
        this.o2Ralenti = o2Ralenti;
    }

    public String getTemperaturaAmbiente() {
        return temperaturaAmbiente;
    }

    public void setTemperaturaAmbiente(String temperaturaAmbiente) {
        this.temperaturaAmbiente = temperaturaAmbiente;
    }

    public String getHumedadRelativa() {
        return humedadRelativa;
    }

    public void setHumedadRelativa(String humedadRelativa) {
        this.humedadRelativa = humedadRelativa;
    }

    @Override
    public String toString() {
        return operarioGases + ";"
                + noIdentificacionOpGases + ";"
                + tempRalenti + ";"
                + rpmRalenti + ";"
                + hcRalenti + ";"
                + coRalenti + ";"
                + co2RAlenti + ";"
                + o2Ralenti + ";"
                + temperaturaAmbiente + ";"
                + humedadRelativa;
    }

    public String toString1() {
        return "DatosGasesMotos{" + "operarioGases=" + operarioGases + "noIdentificacionOpGases=" + noIdentificacionOpGases + "tempRalenti=" + tempRalenti + ", rpmRalenti=" + rpmRalenti + ", hcRalenti=" + hcRalenti + ", coRalenti=" + coRalenti + ", co2RAlenti=" + co2RAlenti + ", o2Ralenti=" + o2Ralenti + ", temperaturaAmbiente=" + temperaturaAmbiente + ", humedadRelativa=" + humedadRelativa + '}';
    }
}

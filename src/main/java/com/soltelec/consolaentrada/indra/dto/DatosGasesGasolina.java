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
public class DatosGasesGasolina extends DatosUsuarioPrueba {

    private String rpmRalenti = "";
    private String hcRalenti = "";
    private String coRalenti = "";
    private String co2RAlenti = "";
    private String o2Ralenti = "";
    private String rmpCrucero = "";
    private String hcCrucero = "";
    private String coCrucero = "";
    private String co2Crucero = "";
    private String o2Crucero = "";
    private boolean dilucion = false;
    private String catalizador = "";
    private String temperaturaPrueba = "";
    private String temperaturaAmbiente = "";
    private String humedadRelativa = "";

    
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


    /**
     * @return the rmpCrucero
     */
    public String getRmpCrucero() {
        return rmpCrucero;
    }

    /**
     * @param rmpCrucero the rmpCrucero to set
     */
    public void setRmpCrucero(String rmpCrucero) {
        this.rmpCrucero = rmpCrucero;
    }

    /**
     * @return the hcCrucero
     */
    public String getHcCrucero() {
        return hcCrucero;
    }

    /**
     * @param hcCrucero the hcCrucero to set
     */
    public void setHcCrucero(String hcCrucero) {
        this.hcCrucero = hcCrucero;
    }

    /**
     * @return the coCrucero
     */
    public String getCoCrucero() {
        return coCrucero;
    }

    /**
     * @param coCrucero the coCrucero to set
     */
    public void setCoCrucero(String coCrucero) {
        this.coCrucero = coCrucero;
    }

    /**
     * @return the co2Crucero
     */
    public String getCo2Crucero() {
        return co2Crucero;
    }

    /**
     * @param co2Crucero the co2Crucero to set
     */
    public void setCo2Crucero(String co2Crucero) {
        this.co2Crucero = co2Crucero;
    }

    /**
     * @return the o2Crucero
     */
    public String getO2Crucero() {
        return o2Crucero;
    }

    /**
     * @param o2Crucero the o2Crucero to set
     */
    public void setO2Crucero(String o2Crucero) {
        this.o2Crucero = o2Crucero;
    }

    /**
     * @return the dilucion
     */
    public boolean getDilucion() {
        return dilucion;
    }

    /**
     * @param dilucion the dilucion to set
     */
    public void setDilucion(boolean dilucion) {
        this.dilucion = dilucion;
    }

    public String getCatalizador() {
        return catalizador;
    }

    public void setCatalizador(String catalizador) {
        this.catalizador = catalizador;
    }

    public String getTemperaturaPrueba() {
        return temperaturaPrueba;
    }

    public void setTemperaturaPrueba(String temperaturaPrueba) {
        this.temperaturaPrueba = temperaturaPrueba;
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
        return operarioGases
                + ";"
                + noIdentificacionOpGases
                + ";"
                + rpmRalenti
                + ";"
                + hcRalenti
                + ";"
                + coRalenti
                + ";"
                + co2RAlenti
                + ";"
                + o2Ralenti
                + ";"
                + rmpCrucero
                + ";"
                + hcCrucero
                + ";"
                + coCrucero
                + ";"
                + co2Crucero
                + ";"
                + o2Crucero
                + ";"
                + dilucion
                + ";"
                + catalizador
                + ";"
                + temperaturaPrueba
                + ";"
                + temperaturaAmbiente
                + ";"
                + humedadRelativa;
    }

}

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
public class PreviaGasesVehiculosDisel {

    private boolean fugasTuboEscape;
    private boolean fugasSilenciador;
    private boolean tapaCombustible;
    private boolean tapaAceite;
    private boolean sistemaMuestreo;
    private boolean salidasAdicionales;
    private boolean filtroAire;
    private boolean sistemaRefrigeracion;
    private boolean revolucionesFueraRango;

    /**
     * @return the fugasTuboEscape
     */
    public boolean isFugasTuboEscape() {
        return fugasTuboEscape;
    }

    /**
     * @param fugasTuboEscape the fugasTuboEscape to set
     */
    public void setFugasTuboEscape(boolean fugasTuboEscape) {
        this.fugasTuboEscape = fugasTuboEscape;
    }

    /**
     * @return the fugasSilenciador
     */
    public boolean isFugasSilenciador() {
        return fugasSilenciador;
    }

    /**
     * @param fugasSilenciador the fugasSilenciador to set
     */
    public void setFugasSilenciador(boolean fugasSilenciador) {
        this.fugasSilenciador = fugasSilenciador;
    }

    /**
     * @return the tapaCombustible
     */
    public boolean isTapaCombustible() {
        return tapaCombustible;
    }

    /**
     * @param tapaCombustible the tapaCombustible to set
     */
    public void setTapaCombustible(boolean tapaCombustible) {
        this.tapaCombustible = tapaCombustible;
    }

    /**
     * @return the tapaAceite
     */
    public boolean isTapaAceite() {
        return tapaAceite;
    }

    /**
     * @param tapaAceite the tapaAceite to set
     */
    public void setTapaAceite(boolean tapaAceite) {
        this.tapaAceite = tapaAceite;
    }

    /**
     * @return the sistemaMuestreo
     */
    public boolean isSistemaMuestreo() {
        return sistemaMuestreo;
    }

    /**
     * @param sistemaMuestreo the sistemaMuestreo to set
     */
    public void setSistemaMuestreo(boolean sistemaMuestreo) {
        this.sistemaMuestreo = sistemaMuestreo;
    }

    /**
     * @return the salidasAdicionales
     */
    public boolean isSalidasAdicionales() {
        return salidasAdicionales;
    }

    /**
     * @param salidasAdicionales the salidasAdicionales to set
     */
    public void setSalidasAdicionales(boolean salidasAdicionales) {
        this.salidasAdicionales = salidasAdicionales;
    }

    /**
     * @return the filtroAire
     */
    public boolean isFiltroAire() {
        return filtroAire;
    }

    /**
     * @param filtroAire the filtroAire to set
     */
    public void setFiltroAire(boolean filtroAire) {
        this.filtroAire = filtroAire;
    }

    /**
     * @return the sistemaRefrigeracion
     */
    public boolean isSistemaRefrigeracion() {
        return sistemaRefrigeracion;
    }

    /**
     * @param sistemaRefrigeracion the sistemaRefrigeracion to set
     */
    public void setSistemaRefrigeracion(boolean sistemaRefrigeracion) {
        this.sistemaRefrigeracion = sistemaRefrigeracion;
    }

    /**
     * @return the revolucionesFueraRango
     */
    public boolean isRevolucionesFueraRango() {
        return revolucionesFueraRango;
    }

    /**
     * @param revolucionesFueraRango the revolucionesFueraRango to set
     */
    public void setRevolucionesFueraRango(boolean revolucionesFueraRango) {
        this.revolucionesFueraRango = revolucionesFueraRango;
    }

    @Override
    public String toString() {
        return isFugasTuboEscape()
                + ";" + isFugasSilenciador()
                + ";" + isTapaCombustible()
                + ";" + isTapaAceite()
                + ";" + isSistemaMuestreo()
                + ";" + isSalidasAdicionales()
                + ";" + isFiltroAire()
                + ";" + isSistemaRefrigeracion()
                + ";" + isRevolucionesFueraRango();
    }
}

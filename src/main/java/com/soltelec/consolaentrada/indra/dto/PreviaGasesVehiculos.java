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
public class PreviaGasesVehiculos {

    private boolean fugasTuboEscape;
    private boolean fugasSilenciador;
    private boolean tapaCombustible;
    private boolean tapaAceite;
    private boolean salidasAdicionales;
    private boolean presenciaHumos;
    private boolean revolucionesFueraRango;
    private boolean fallaSistemaRefrigeracion;

    public boolean isFugasTuboEscape() {
        return fugasTuboEscape;
    }

    public void setFugasTuboEscape(boolean fugasTuboEscape) {
        this.fugasTuboEscape = fugasTuboEscape;
    }

    public boolean isFugasSilenciador() {
        return fugasSilenciador;
    }

    public void setFugasSilenciador(boolean fugasSilenciador) {
        this.fugasSilenciador = fugasSilenciador;
    }

    public boolean isTapaCombustible() {
        return tapaCombustible;
    }

    public void setTapaCombustible(boolean tapaCombustible) {
        this.tapaCombustible = tapaCombustible;
    }

    public boolean isTapaAceite() {
        return tapaAceite;
    }

    public void setTapaAceite(boolean tapaAceite) {
        this.tapaAceite = tapaAceite;
    }

    public boolean isSalidasAdicionales() {
        return salidasAdicionales;
    }

    public void setSalidasAdicionales(boolean salidasAdicionales) {
        this.salidasAdicionales = salidasAdicionales;
    }

    public boolean isPresenciaHumos() {
        return presenciaHumos;
    }

    public void setPresenciaHumos(boolean presenciaHumos) {
        this.presenciaHumos = presenciaHumos;
    }

    public boolean isRevolucionesFueraRango() {
        return revolucionesFueraRango;
    }

    public void setRevolucionesFueraRango(boolean revolucionesFueraRango) {
        this.revolucionesFueraRango = revolucionesFueraRango;
    }

    public boolean isFallaSistemaRefrigeracion() {
        return fallaSistemaRefrigeracion;
    }

    public void setFallaSistemaRefrigeracion(boolean fallaSistemaRefrigeracion) {
        this.fallaSistemaRefrigeracion = fallaSistemaRefrigeracion;
    }

    @Override
    public String toString() {
        return fugasTuboEscape
                + ";" + fugasSilenciador
                + ";" + tapaCombustible
                + ";" + tapaAceite
                + ";" + salidasAdicionales
                + ";" + presenciaHumos
                + ";" + revolucionesFueraRango
                + ";" + fallaSistemaRefrigeracion;
    }

    public String toString1() {
        return "PreviaGasesVehiculos{" + "fugasTuboEscape=" + fugasTuboEscape + ", fugasSilenciador=" + fugasSilenciador + ", tapaCombustible=" + tapaCombustible + ", tapaAceite=" + tapaAceite + ", salidasAdicionales=" + salidasAdicionales + ", presenciaHumos=" + presenciaHumos + ", revolucionesFueraRango=" + revolucionesFueraRango + ", fallaSistemaRefrigeracion=" + fallaSistemaRefrigeracion + '}';
    }
}

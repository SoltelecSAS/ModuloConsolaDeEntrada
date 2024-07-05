/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.indra.dto;

import com.soltelec.consolaentrada.utilities.UtilPropiedades;

/**
 *
 * @author GerenciaDesarrollo
 */
public class DatosFur {

    private String codigoProveedor = "";
    private String propietario = "";
    private String vehiculo = "";
    private String datosFotos = "";
    private String resultadoGases = "";
    private String inspeccionPrevia = "";
    private String resultadoLuces = "";
    private String resultadoRuidos = "";
    private String resultadoFas = "";
    private String resultadoVisual = "";
    private String resultadoTaximero = "";
    private String observaciones = "";
    private String runt = "";
    private String llantas = "";
    private String equipos = "";
    private String software = "";
    private String firma = "";
    private String furAsociado = "";

    /**
     * @return the propietario
     */
    public String getPropietario() {
        return propietario;
    }

    /**
     * @param propietario the propietario to set
     */
    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    /**
     * @return the vehiculo
     */
    public String getVehiculo() {
        return vehiculo;
    }

    /**
     * @param vehiculo the vehiculo to set
     */
    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    /**
     * @return the resultadoGases
     */
    public String getResultadoGases() {
        return resultadoGases;
    }

    /**
     * @param resultadoGases the resultadoGases to set
     */
    public void setResultadoGases(String resultadoGases) {
        this.resultadoGases = resultadoGases;
    }

    /**
     * @return the inspaccionPrevia
     */
    public String getInspeccionPrevia() {
        return inspeccionPrevia;
    }

    /**
     * @param inspaccionPrevia the inspaccionPrevia to set
     */
    public void setInspeccionPrevia(String inspaccionPrevia) {
        this.inspeccionPrevia = inspaccionPrevia;
    }

    /**
     * @return the resultadoLuces
     */
    public String getResultadoLuces() {
        return resultadoLuces;
    }

    /**
     * @param resultadoLuces the resultadoLuces to set
     */
    public void setResultadoLuces(String resultadoLuces) {
        this.resultadoLuces = resultadoLuces;
    }

    /**
     * @return the resultadoRuidos
     */
    public String getResultadoRuidos() {
        return resultadoRuidos;
    }

    /**
     * @param resultadoRuidos the resultadoRuidos to set
     */
    public void setResultadoRuidos(String resultadoRuidos) {
        this.resultadoRuidos = resultadoRuidos;
    }

    /**
     * @return the resultadoFas
     */
    public String getResultadoFas() {
        return resultadoFas;
    }

    /**
     * @param resultadoFas the resultadoFas to set
     */
    public void setResultadoFas(String resultadoFas) {
        this.resultadoFas = resultadoFas;
    }

    /**
     * @return the resultadoVisual
     */
    public String getResultadoVisual() {
        return resultadoVisual;
    }

    /**
     * @param resultadoVisual the resultadoVisual to set
     */
    public void setResultadoVisual(String resultadoVisual) {
        this.resultadoVisual = resultadoVisual;
    }

    /**
     * @return the resultadoTaximero
     */
    public String getResultadoTaximero() {
        return resultadoTaximero;
    }

    /**
     * @param resultadoTaximero the resultadoTaximero to set
     */
    public void setResultadoTaximero(String resultadoTaximero) {
        this.resultadoTaximero = resultadoTaximero;
    }

    /**
     * @return the codigoProveedor
     */
    public String getCodigoProveedor() {
        return codigoProveedor;
    }

    /**
     * @param codigoProveedor the codigoProveedor to set
     */
    public void setCodigoProveedor(String codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    /**
     * @return the datosFotos
     */
    public String getDatosFotos() {
        return datosFotos;
    }

    /**
     * @param datosFotos the datosFotos to set
     */
    public void setDatosFotos(String datosFotos) {
        this.datosFotos = datosFotos;
    }

    /**
     * @return the observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * @param observaciones the observaciones to set
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getRunt() {
        return runt;
    }
//    

    public void setRunt(String runt) {
        this.runt = runt;
    }

    @Override

    public String toString() {
        return codigoProveedor + "|"
                + propietario + "|"
                + vehiculo + "|"
                + datosFotos + "|"
                + resultadoGases + "|"
                + inspeccionPrevia + "|"
                + resultadoLuces + "|"
                + resultadoFas + "|"
                + resultadoVisual + "|"
                + resultadoTaximero + "|"
                + observaciones + "|"
                + runt + "|"
                + llantas + "|"
                + equipos + "|"
                + software + "|"
                + firma + "|"
                + furAsociado;
    }

    public String toString1() {
        return "DatosFur{" + "codigoProveedor=" + codigoProveedor + ", propietario=" + propietario + ", vehiculo=" + vehiculo + ", datosFotos=" + datosFotos + ", resultadoGases=" + resultadoGases + ", inspeccionPrevia=" + inspeccionPrevia + ", resultadoLuces=" + resultadoLuces + ", resultadoRuidos=" + resultadoRuidos + ", resultadoFas=" + resultadoFas + ", resultadoVisual=" + resultadoVisual + ", resultadoTaximero=" + resultadoTaximero + ", observaciones=" + observaciones + ", runt=" + runt + '}';
    }

    /**
     * @return the llantas
     */
    public String getLlantas() {
        return llantas;
    }

    /**
     * @param llantas the llantas to set
     */
    public void setLlantas(String llantas) {
        this.llantas = llantas;
    }

    /**
     * @return the equipos
     */
    public String getEquipos() {
        return equipos;
    }

    /**
     * @param equipos the equipos to set
     */
    public void setEquipos(String equipos) {
        this.equipos = equipos;
    }

    /**
     * @return the software
     */
    public String getSoftware() {
        return software;
    }

    /**
     * @param software the software to set
     */
    public void setSoftware(String software) {
        this.software = software;
    }

    /**
     * @return the firma
     */
    public String getFirma() {
        return firma;
    }

    /**
     * @param firma the firma to set
     */
    public void setFirma(String firma) {
        this.firma = firma;
    }

    /**
     * @return the furAsociado
     */
    public String getFurAsociado() {
        return furAsociado;
    }

    /**
     * @param furAsociado the furAsociado to set
     */
    public void setFurAsociado(String furAsociado) {
        this.furAsociado = furAsociado;
    }
}

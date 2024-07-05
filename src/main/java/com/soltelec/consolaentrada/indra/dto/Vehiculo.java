/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.indra.dto;


/**
 *
 * @author gerenciaDesarrollo
 */
public class Vehiculo {

    private String operario = "";
    private String noIdentificaiconOperario = "";
    private String placa = "";
    private String pais = "";
    private String servicio = "";
    private String clase = "";
    private String marca = "";
    private String linea = "";
    private Integer modelo;
    private String noLicenciaDeTransito = "";
    private String fechaMatricula = "";
    private String color = "";
    private Integer combustible;
    private String vinchasis = "";
    private String noMotor = "";
    private Integer tipoMotor;
    private Integer cilindraje;
    private String kilometraje;
    private Integer numSillas;
    private boolean vidriosPolarizados;
    private boolean blindaje;
    private boolean reinspeccion;
    private String fechaPrueba;
    private Integer edoRevision;
    private Integer potencia;
    private Integer tipoCarroceria;
    private String fechaVencimientoSoat;
    private String conversionGNV;
    private String fechaVencimientoConversionGNV;

    public String getOperario() {
        return operario;
    }

    public void setOperario(String operario) {
        this.operario = operario;
    }

    public String getNoIdentificaiconOperario() {
        return noIdentificaiconOperario;
    }

    public void setNoIdentificaiconOperario(String noIdentificaiconOperario) {
        this.noIdentificaiconOperario = noIdentificaiconOperario;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public Integer getModelo() {
        return modelo;
    }

    public void setModelo(Integer modelo) {
        this.modelo = modelo;
    }

    public String getNoLicenciaDeTransito() {
        return noLicenciaDeTransito;
    }

    public void setNoLicenciaDeTransito(String noLicenciaDeTransito) {
        this.noLicenciaDeTransito = noLicenciaDeTransito;
    }

    public String getFechaMatricula() {
        return fechaMatricula;
    }

    public void setFechaMatricula(String fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getCombustible() {
        return combustible;
    }

    public void setCombustible(Integer combustible) {
        this.combustible = combustible;
    }

    public String getVinchasis() {
        return vinchasis;
    }

    public void setVinchasis(String vinchasis) {
        this.vinchasis = vinchasis;
    }

    public String getNoMotor() {
        return noMotor;
    }

    public void setNoMotor(String noMotor) {
        this.noMotor = noMotor;
    }

    public Integer getTipoMotor() {
        return tipoMotor;
    }

    public void setTipoMotor(Integer tipoMotor) {
        this.tipoMotor = tipoMotor;
    }

    public Integer getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(Integer cilindraje) {
        this.cilindraje = cilindraje;
    }

    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }

    public Integer getNumSillas() {
        return numSillas;
    }

    public void setNumSillas(Integer numSillas) {
        this.numSillas = numSillas;
    }

    public boolean isVidriosPolarizados() {
        return vidriosPolarizados;
    }

    public void setVidriosPolarizados(boolean vidriosPolarizados) {
        this.vidriosPolarizados = vidriosPolarizados;
    }

    public boolean isBlindaje() {
        return blindaje;
    }

    public void setBlindaje(boolean blindaje) {
        this.blindaje = blindaje;
    }

    public String getFechaPrueba() {
        return fechaPrueba;
    }

    public void setFechaPrueba(String fechaPrueba) {
        this.fechaPrueba = fechaPrueba;
    }

    public Integer getEdoRevision() {
        return edoRevision;
    }

    public void setEdoRevision(Integer edoRevision) {
        this.edoRevision = edoRevision;
    }

    public Integer getPotencia() {
        return potencia;
    }

    public void setPotencia(Integer potencia) {
        this.potencia = potencia;
    }

    public Integer getTipoCarroceria() {
        return tipoCarroceria;
    }

    public void setTipoCarroceria(Integer tipoCarroceria) {
        this.tipoCarroceria = tipoCarroceria;
    }

    public String getFechaVencimientoSoat() {
        return fechaVencimientoSoat;
    }

    public void setFechaVencimientoSoat(String fechaVencimientoSoat) {
        this.fechaVencimientoSoat = fechaVencimientoSoat;
    }

    public String getConversionGNV() {
        return conversionGNV;
    }

    public void setConversionGNV(String conversionGNV) {
        this.conversionGNV = conversionGNV;
    }

    public String getFechaVencimientoConversionGNV() {
        return fechaVencimientoConversionGNV;
    }

    public void setFechaVencimientoConversionGNV(String fechaVencimientoConversionGNV) {
        this.fechaVencimientoConversionGNV = fechaVencimientoConversionGNV;
    }

    public String toStringIndra() {
        return operario + ";"
                + noIdentificaiconOperario + ";"
                + placa + ";"
                + pais + ";"
                + servicio + ";"
                + clase + ";"
                + marca + ";"
                + linea + ";"
                + modelo + ";"
                + noLicenciaDeTransito + ";"
                + fechaMatricula + ";"
                + color + ";"
                + combustible + ";"
                + vinchasis + ";"
                + noMotor + ";"
                + tipoMotor + ";"
                + cilindraje + ";"
                + kilometraje + ";"
                + numSillas + ";"
                + blindaje + ";"
                + reinspeccion + ";"
                + fechaPrueba + ";"
                + edoRevision + ";"
                + potencia + ";"
                + tipoCarroceria + ";"
                + fechaVencimientoSoat + ";"
                + conversionGNV + ";"
                + fechaVencimientoConversionGNV;
    }

    /**
     * @return the reinspeccion
     */
    public boolean isReinspeccion() {
        return reinspeccion;
    }

    /**
     * @param reinspeccion the reinspeccion to set
     */
    public void setReinspeccion(boolean reinspeccion) {
        this.reinspeccion = reinspeccion;
    }

}

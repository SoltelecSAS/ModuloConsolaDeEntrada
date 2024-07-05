/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.entities;

import java.util.Date;

/**
 *
 * @author User
 */
public class ParametrosBusqueda {
    
    private Date fechaInicio;
    private Date fechaFin;
    private String estado;
    private int tipo;
    private int clase;
    private int servicio;
    private int linea;
    private String placa;
    private int marca;

    public void setCmbModelo(String cmbModelo) {
        this.cmbModelo = cmbModelo;
    }

    public void setCmbcilindraje(String cmbcilindraje) {
        this.cmbcilindraje = cmbcilindraje;
    }
    private int modelo;
    private int cilindraje;
    private String numeroMotor;
    private String vin;
    private long idPropietario;

    public String getCmbModelo() {
        return cmbModelo;
    }

    public String getCmbcilindraje() {
        return cmbcilindraje;
    }
    private String apellido;
    private String nombre;
    private String usuarioRegistro;
    private String numeroLicencia;
    private int numeroEjes;
    private String cmbModelo;
    private String cmbcilindraje;

    /**
     * @return the tipo
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the clase
     */
    public int getClase() {
        return clase;
    }

    /**
     * @param clase the clase to set
     */
    public void setClase(int clase) {
        this.clase = clase;
    }

    /**
     * @return the servicio
     */
    public int getServicio() {
        return servicio;
    }

    /**
     * @param servicio the servicio to set
     */
    public void setServicio(int servicio) {
        this.servicio = servicio;
    }

    /**
     * @return the linea
     */
    public int getLinea() {
        return linea;
    }

    /**
     * @param linea the linea to set
     */
    public void setLinea(int linea) {
        this.linea = linea;
    }

    /**
     * @return the placa
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * @param placa the placa to set
     */
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /**
     * @return the marca
     */
    public int getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(int marca) {
        this.marca = marca;
    }

    /**
     * @return the modelo
     */
    public Integer getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(int modelo) {
        this.modelo = modelo;
    }

    /**
     * @return the cilindraje
     */
    public int getCilindraje() {
        return cilindraje;
    }

    /**
     * @param cilindraje the cilindraje to set
     */
    public void setCilindraje(int cilindraje) {
        this.cilindraje = cilindraje;
    }

    /**
     * @return the numeroMotor
     */
    public String getNumeroMotor() {
        return numeroMotor;
    }

    /**
     * @param numeroMotor the numeroMotor to set
     */
    public void setNumeroMotor(String numeroMotor) {
        this.numeroMotor = numeroMotor;
    }

    /**
     * @return the vin
     */
    public String getVin() {
        return vin;
    }

    /**
     * @param vin the vin to set
     */
    public void setVin(String vin) {
        this.vin = vin;
    }

    /**
     * @return the idPropietario
     */
    public long getIdPropietario() {
        return idPropietario;
    }

    /**
     * @param idPropietario the idPropietario to set
     */
    public void setIdPropietario(long idPropietario) {
        this.idPropietario = idPropietario;
    }

    /**
     * @return the apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @param apellido the apellido to set
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the usuarioRegistro
     */
    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    /**
     * @param usuarioRegistro the usuarioRegistro to set
     */
    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    /**
     * @return the numeroLicencia
     */
    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    /**
     * @param numeroLicencia the numeroLicencia to set
     */
    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    /**
     * @return the numeroEjes
     */
    public int getNumeroEjes() {
        return numeroEjes;
    }

    /**
     * @param numeroEjes the numeroEjes to set
     */
    public void setNumeroEjes(int numeroEjes) {
        this.numeroEjes = numeroEjes;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the fechaInicio
     */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the fechaFin
     */
    public Date getFechaFin() {
        return fechaFin;
    }

    /**
     * @param fechaFin the fechaFin to set
     */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

}

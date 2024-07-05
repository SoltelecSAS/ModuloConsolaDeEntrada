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
public class Propietario {

    private String nombreRazon = "";
    private String tipoDocumentoIdentidad = "";
    private String noIdentidad = "";
    private String direccion = "";
    private String telefono = "";
    private String ciudad = "";
    private String departamento = "";
    private String correo = "";

    public String getNombreRazon() {
        return nombreRazon;
    }

    public void setNombreRazon(String nombreRazon) {
        this.nombreRazon = nombreRazon;
    }

    public String getTipoDocumentoIdentidad() {
        return tipoDocumentoIdentidad;
    }

    public void setTipoDocumentoIdentidad(String tipoDocumentoIdentidad) {
        this.tipoDocumentoIdentidad = tipoDocumentoIdentidad;
    }

    public String getNoIdentidad() {
        return noIdentidad;
    }

    public void setNoIdentidad(String noIdentidad) {
        this.noIdentidad = noIdentidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return nombreRazon + ";"
                + tipoDocumentoIdentidad + ";"
                + noIdentidad + ";"
                + direccion + ";"
                + telefono + ";"
                + ciudad + ";"
                + departamento + ";"
                + correo;
    }

    public String toString1() {
        return "Propietario{" + "nombreRazon=" + nombreRazon + ", tipoDocumentoIdentidad=" + tipoDocumentoIdentidad + ", noIdentidad=" + noIdentidad +
                ", direccion=" + direccion + ", telefono=" + telefono + ", ciudad=" + ciudad + ", departamento=" + departamento + ", correo=" + correo + '}';
    }
}

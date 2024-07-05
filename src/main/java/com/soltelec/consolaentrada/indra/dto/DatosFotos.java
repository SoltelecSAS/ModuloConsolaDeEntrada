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
public class DatosFotos {

    private String placa="";
    private String fotoDelantera="";
    private String fotoTrasera="";

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
     * @return the fotoDelantera
     */
    public String getFotoDelantera() {
        return fotoDelantera;
    }

    /**
     * @param fotoDelantera the fotoDelantera to set
     */
    public void setFotoDelantera(String fotoDelantera) {
        this.fotoDelantera = fotoDelantera;
    }

    /**
     * @return the fotoTrasera
     */
    public String getFotoTrasera() {
        return fotoTrasera;
    }

    /**
     * @param fotoTrasera the fotoTrasera to set
     */
    public void setFotoTrasera(String fotoTrasera) {
        this.fotoTrasera = fotoTrasera;
    }

    @Override
    public String toString() {
        return placa + ";" + fotoDelantera + ";" + fotoTrasera;
    }

    
    public String toString1() {
        return "DatosFotos{" + "placa=" + placa + ", fotoDelantera=" + fotoDelantera + ", fotoTrasera=" + fotoTrasera + '}';
    }
    
    public String toString2() {
        return fotoDelantera + ";" + fotoTrasera;
    }
    
    
}

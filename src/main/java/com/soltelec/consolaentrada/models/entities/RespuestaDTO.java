/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.entities;

/**
 *
 * @author usurio
 */
public class RespuestaDTO {

    private String codigoRespuesta;
    private String mensajeRespuesta;

    public RespuestaDTO(String codigoRespuesta, String mensajeRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
        this.mensajeRespuesta = mensajeRespuesta;
    }

    /**
     * @return the codigoRespuesta
     */
    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    /**
     * @param codigoRespuesta the codigoRespuesta to set
     */
    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    /**
     * @return the mensajeRespuesta
     */
    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    /**
     * @param mensajeRespuesta the mensajeRespuesta to set
     */
    public void setMensajeRespuesta(String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }

}

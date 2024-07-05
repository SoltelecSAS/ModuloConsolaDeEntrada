/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.entities;

import java.io.Serializable;

/**
 * Encargada de almacenar las respuestas ante cualquier petición
 *
 * @author GerenciaDesarrollo
 */
public class ResponseDTO implements Serializable {

    private String codigoRespuesta;
    private String mensajeRespuesta;

    public ResponseDTO(String codigoRespuesta, String mensajeRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
        this.mensajeRespuesta = mensajeRespuesta;
    }

    public ResponseDTO() {
        
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

    @Override
    public String toString() {
        return "RespuestaDTO{" + "codigoRespuesta=" + codigoRespuesta + ", mensajeRespuesta=" + mensajeRespuesta + '}';
    }

}

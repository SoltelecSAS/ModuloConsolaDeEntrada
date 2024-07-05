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
public class DatosRuido {

    private String valor = "";
    private String operarioSonometria = "";
    private String noIdentificacionOpSonometria = "";

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * @return the operarioSonometria
     */
    public String getOperarioSonometria() {
        return operarioSonometria;
    }

    /**
     * @param operarioSonometria the operarioSonometria to set
     */
    public void setOperarioSonometria(String operarioSonometria) {
        this.operarioSonometria = operarioSonometria;
    }

    /**
     * @return the noIdentificacionOpSonometria
     */
    public String getNoIdentificacionOpSonometria() {
        return noIdentificacionOpSonometria;
    }

    /**
     * @param noIdentificacionOpSonometria the noIdentificacionOpSonometria to
     * set
     */
    public void setNoIdentificacionOpSonometria(String noIdentificacionOpSonometria) {
        this.noIdentificacionOpSonometria = noIdentificacionOpSonometria;
    }

    @Override
    public String toString() {
        return operarioSonometria + ";"
                + noIdentificacionOpSonometria + ";"
                + valor;
    }

    public String toString1() {
        return "DatosRuido{" + "valor=" + valor + ", operarioSonometria=" + operarioSonometria + ", noIdentificacionOpSonometria=" + noIdentificacionOpSonometria + '}';
    }

}

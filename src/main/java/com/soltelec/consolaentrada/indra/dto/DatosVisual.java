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
public class DatosVisual {

    private String operarioVisual = "";
    private String noIdentificacionOpVisual = "";
    private String codigoRechazo = "";
//    private String visualTexTA = "";
//    private String sumaA = "0";
//    private String visualTexTB = "";
//    private String sumaB = "0";
//    private String visualTexTAnexo = "";
//    private String sumAnexo = "0";

    /**
     * @return the visualTexTA
     */
//    public String getVisualTexTA() {
//        return visualTexTA;
//    }
//
//    /**
//     * @param visualTexTA the visualTexTA to set
//     */
//    public void setVisualTexTA(String visualTexTA) {
//        this.visualTexTA = visualTexTA;
//    }
//
//    /**
//     * @return the sumaA
//     */
//    public String getSumaA() {
//        return sumaA;
//    }
//
//    /**
//     * @param sumaA the sumaA to set
//     */
//    public void setSumaA(String sumaA) {
//        this.sumaA = sumaA;
//    }
//
//    /**
//     * @return the visualTexTB
//     */
//    public String getVisualTexTB() {
//        return visualTexTB;
//    }
//
//    /**
//     * @param visualTexTB the visualTexTB to set
//     */
//    public void setVisualTexTB(String visualTexTB) {
//        this.visualTexTB = visualTexTB;
//    }
//
//    /**
//     * @return the sumaB
//     */
//    public String getSumaB() {
//        return sumaB;
//    }
//
//    /**
//     * @param sumaB the sumaB to set
//     */
//    public void setSumaB(String sumaB) {
//        this.sumaB = sumaB;
//    }
//
//    /**
//     * @return the visualTexTAnexo
//     */
//    public String getVisualTexTAnexo() {
//        return visualTexTAnexo;
//    }
//
//    /**
//     * @param visualTexTAnexo the visualTexTAnexo to set
//     */
//    public void setVisualTexTAnexo(String visualTexTAnexo) {
//        this.visualTexTAnexo = visualTexTAnexo;
//    }
//
//    /**
//     * @return the sumAnexo
//     */
//    public String getSumAnexo() {
//        return sumAnexo;
//    }
//
//    /**
//     * @param sumAnexo the sumAnexo to set
//     */
//    public void setSumAnexo(String sumAnexo) {
//        this.sumAnexo = sumAnexo;
//    }
    /**
     * @return the operarioVisual
     */
    public String getOperarioVisual() {
        return operarioVisual;
    }

    /**
     * @param operarioVisual the operarioVisual to set
     */
    public void setOperarioVisual(String operarioVisual) {
        this.operarioVisual = operarioVisual;
    }

    /**
     * @return the noIdentificacionOpVisual
     */
    public String getNoIdentificacionOpVisual() {
        return noIdentificacionOpVisual;
    }

    /**
     * @param noIdentificacionOpVisual the noIdentificacionOpVisual to set
     */
    public void setNoIdentificacionOpVisual(String noIdentificacionOpVisual) {
        this.noIdentificacionOpVisual = noIdentificacionOpVisual;
    }

    public String getCodigoRechazo() {
        return codigoRechazo;
    }

    public void setCodigoRechazo(String codigoRechazo) {
        this.codigoRechazo = codigoRechazo;
    }

    @Override
    public String toString() {
        return operarioVisual + ";"
                + noIdentificacionOpVisual + ";"
                + codigoRechazo;
    }
}

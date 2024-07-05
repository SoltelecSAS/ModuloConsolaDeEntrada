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
public class DatosTaximetro {

    private String operarioTaximetro="";
    private String noIdentificacionOpTaximetro="";
    private Boolean aplicaTaximetro=false;
    private Boolean taximetroVisible = false;
    private Boolean tieneTaximetro = false;
    private String  Referencia_llanta = "";
    private String Error_Tiempo  = "";
    private String  Error_Distancia = "";

    public String getReferencia_llanta() {
        return Referencia_llanta;
    }

    public void setReferenciallanta(String Referencia_llanta) {
        this.Referencia_llanta = Referencia_llanta;
    }

    public String getErrorTiempo() {
        return Error_Tiempo;
    }

    public void setErrorTiempo(String Error_Tiempo) {
        this.Error_Tiempo = Error_Tiempo;
    }

    public String getErrorDistancia() {
        return Error_Distancia;
    }

    public void setErrorDistancia(String Error_Distancia) {
        this.Error_Distancia = Error_Distancia;
    }
       
    /**
     * @return the operarioTaximetro
     */
    public String getOperarioTaximetro() {
        return operarioTaximetro;
    }

    /**
     * @param operarioTaximetro the operarioTaximetro to set
     */
    public void setOperarioTaximetro(String operarioTaximetro) {
        this.operarioTaximetro = operarioTaximetro;
    }

    /**
     * @return the noIdentificacionOpTaximetro
     */
    public String getNoIdentificacionOpTaximetro() {
        return noIdentificacionOpTaximetro;
    }

    /**
     * @param noIdentificacionOpTaximetro the noIdentificacionOpTaximetro to set
     */
    public void setNoIdentificacionOpTaximetro(String noIdentificacionOpTaximetro) {
        this.noIdentificacionOpTaximetro = noIdentificacionOpTaximetro;
    }

    public Boolean getAplicaTaximetro() {
        return aplicaTaximetro;
    }

    public void setAplicaTaximetro(Boolean aplicaTaximetro) {
        this.aplicaTaximetro = aplicaTaximetro;
    }

    public Boolean getTaximetroVisible() {
        return taximetroVisible;
    }

    public void setTaximetroVisible(Boolean taximetroVisible) {
        this.taximetroVisible = taximetroVisible;
    }

    public Boolean getTieneTaximetro() {
        return tieneTaximetro;
    }

    public void setTieneTaximetro(Boolean tieneTaximetro) {
        this.tieneTaximetro = tieneTaximetro;
    }

    /**
     * @return the referenciaLlanta
     */
   


    

    @Override
    public String toString() {     
        String tramaTaximetro =
            operarioTaximetro + ";"
            + noIdentificacionOpTaximetro + ";"
            + aplicaTaximetro + ";"
            + tieneTaximetro + ";"
            + taximetroVisible + ";"
            + Referencia_llanta + ";"
            + Error_Distancia + ";"
            + Error_Tiempo ;

        System.out.println("---------------------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------------------");
        System.out.println  ("-TRAMA TAXIMETRO:' "+tramaTaximetro+" ':TRAMA TAXIMETRO--");
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------------------");
        
        return tramaTaximetro;
    }

}



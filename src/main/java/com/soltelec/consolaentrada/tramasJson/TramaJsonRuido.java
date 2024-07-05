/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.tramasJson;

/**
 *
 * @author SOLTELEC
 */
public class TramaJsonRuido 
{
    String ruidoEscape="";
    String tablaAfectada="";
    String idRegistro="";

    public void setRuidoEscape(String ruidoEscape)
    {
        if (ruidoEscape!=null)
        {
            this.ruidoEscape = ruidoEscape;
        }
    }

    public void setTablaAfectada(String tablaAfectada) 
    {
        if (tablaAfectada!=null)
        {
           this.tablaAfectada = tablaAfectada;
        } 
    }

    public void setIdRegistro(String idRegistro) 
    {
        if (idRegistro!=null)
        {
           this.idRegistro = idRegistro;
        }
    }

    public String getRuidoEscape() 
    {
        return ruidoEscape;
    }

    public String getTablaAfectada() 
    {
        return tablaAfectada;
    }

    public String getIdRegistro() 
    {
        return idRegistro;
    }

    @Override
    public String toString() 
    {
        return "{\"ruidoEscape\":\"" + ruidoEscape + "\","
                + "\"tablaAfectada\":\"" + tablaAfectada + "\","
                + "\"idRegistro\":\"" + idRegistro + "\"}";
    }
    
}


package com.soltelec.consolaentrada.tramasJson;

/**
 *
 * @author SOLTELEC
 */
public class TramaJsonTaximetro
{
    private String refComericalLLanta="";
    private String errorDistancia="";
    private String errorTiempo="";
    private String tablaAfectada="";
    private String idRegistro="";

    public void setRefComericalLLanta(String refComericalLLanta) 
    {
        if (refComericalLLanta!=null) 
        {
            this.refComericalLLanta = refComericalLLanta;
        }
        
    }

    public void setErrorDistancia(String errorDistancia) 
    {
        if (errorDistancia!=null) 
        {
           this.errorDistancia = errorDistancia; 
        } 
    }

    public void setErrorTiempo(String errorTiempo)
    {
        if (errorTiempo != null) 
        {
            this.errorTiempo = errorTiempo;
        }

    }

    public void setTablaAfectada(String tablaAfectada)
    {
        if (tablaAfectada != null) {
            this.tablaAfectada = tablaAfectada;
        }

    }

    public void setIdRegistro(String idRegistro) 
    {
        if (idRegistro != null)
        {
            this.idRegistro = idRegistro;
        }
        
    }

    public String getRefComericalLLanta() {
        return refComericalLLanta;
    }

    public String getErrorDistancia() {
        return errorDistancia;
    }

    public String getErrorTiempo() {
        return errorTiempo;
    }

    public String getTablaAfectada() {
        return tablaAfectada;
    }

    public String getIdRegistro() {
        return idRegistro;
    }

    @Override
    public String toString()
    {
        return "{\"refComericalLLanta\":\"" + refComericalLLanta + "\","
                + "\"errorDistancia\":\"" + errorDistancia + "\","
                + "\"errorTiempo\":\"" + errorTiempo + "\","
                + "\"tablaAfectada\":\"" + tablaAfectada + "\","
                + "\"idRegistro\":\"" + idRegistro + "\"}";
    
    }

   
}

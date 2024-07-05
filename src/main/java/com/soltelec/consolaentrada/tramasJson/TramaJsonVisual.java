
package com.soltelec.consolaentrada.tramasJson;

/**
 *
 * @author SOLTELEC
 */
public class TramaJsonVisual
{
    private String Observaciones="";
    private String CodigoRechazo="";
    private String tablaAfectada="";
    private String idRegistro="";

    public void setObservaciones(String Observaciones) 
    {
        if (Observaciones!=null)
        {
            this.Observaciones = Observaciones;
        }
    }

    public void setCodigoRechazo(String CodigoRechazo)
    {
        if (CodigoRechazo!=null)
        {
            this.CodigoRechazo = this.CodigoRechazo + CodigoRechazo;
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

    public String getObservaciones() {
        return Observaciones;
    }

    public String getCodigoRechazo() {
        return CodigoRechazo;
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
        return "{\"Observaciones\":\"" + Observaciones + "\","
                + "\"CodigoRechazo\":\"" + CodigoRechazo + "\","
                + "\"tablaAfectada\":\"" + tablaAfectada + "\","
                + "\"idRegistro\":\"" + idRegistro + "\"}";
    }

}

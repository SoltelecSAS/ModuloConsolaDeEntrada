
package com.soltelec.consolaentrada.tramasJson;

/**
 *
 * @author SOLTELEC
 */
public class TramaJsonSuspension 
{
    private String suspIzqEje1="";
    private String suspIzqEje2="";
    private String susPDerEje1="";
    private String susPDerEje2="";
    private String tablaAfectada="";
    private String idRegistro="";

    public void setSuspIzqEje1(String suspIzqEje1) 
    {
        if (suspIzqEje1!=null)
        {
            this.suspIzqEje1 = suspIzqEje1;
        } 
    }

    public void setSuspIzqEje2(String suspIzqEje2) 
    {
        if(suspIzqEje2!=null)
        {
             this.suspIzqEje2 = suspIzqEje2;
        }  
    }

    public void setSusPDerEje1(String susPDerEje1)
    {
        if(susPDerEje1!=null)
        {
            this.susPDerEje1 = susPDerEje1;
        }   
    }

    public void setSusPDerEje2(String susPDerEje2)
    {
        if(susPDerEje2!=null)
        {
            this.susPDerEje2 = susPDerEje2;
        }  
    }

    public void setTablaAfectada(String tablaAfectada) 
    {
        if(tablaAfectada!=null)
        {
           this.tablaAfectada = tablaAfectada;
        }
    }

    public void setIdRegistro(String idRegistro) 
    {
        if(idRegistro!=null)
        {
           this.idRegistro = idRegistro;
        }
    }

    public String getSuspIzqEje1() {
        return suspIzqEje1;
    }

    public String getSuspIzqEje2() {
        return suspIzqEje2;
    }

    public String getSusPDerEje1() {
        return susPDerEje1;
    }

    public String getSusPDerEje2() {
        return susPDerEje2;
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
        return "{\"suspIzqEje1\":\"" + suspIzqEje1 + "\","
                + "\"suspIzqEje2\":\"" + suspIzqEje2 + "\","
                + "\"susPDerEje1\":\"" + susPDerEje1 + "\","
                + "\"susPDerEje2\":\"" + susPDerEje2 + "\","
                + "\"tablaAfectada\":\"" + tablaAfectada + "\","
                + "\"idRegistro\":\"" + idRegistro + "\"}";
    }

}

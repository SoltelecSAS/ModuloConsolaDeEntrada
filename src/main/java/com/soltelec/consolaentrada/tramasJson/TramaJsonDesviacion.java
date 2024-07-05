package com.soltelec.consolaentrada.tramasJson;

/**
 *
 * @author SOLTELEC
 */
public class TramaJsonDesviacion 
{
    private String desviacionLateraleje1="";
    private String desviacionLateraleje2="";
    private String desviacionLateraleje3="";
    private String desviacionLateraleje4="";
    private String desviacionLateraleje5="";
    private String desviacionLateraleje6="";
    private String tablaAfectada="";
    private String idRegistro="";

    public void setDesviacionLateraleje1(String desviacionLateraleje1) 
    {
        if (desviacionLateraleje1!=null)
        {
            this.desviacionLateraleje1 = desviacionLateraleje1;
        }      
    }

    public void setDesviacionLateraleje2(String desviacionLateraleje2)
    {
        if (desviacionLateraleje2 != null)
        {
            this.desviacionLateraleje2 = desviacionLateraleje2;
        }
    }

    public void setDesviacionLateraleje3(String desviacionLateraleje3)
    {
        if (desviacionLateraleje3!=null)
        {
            this.desviacionLateraleje3 = desviacionLateraleje3;
        } 
    }

    public void setDesviacionLateraleje4(String desviacionLateraleje4)
    {
        if (desviacionLateraleje4!=null)
        {
            this.desviacionLateraleje4 = desviacionLateraleje4;
        } 
    }

    public void setDesviacionLateraleje5(String desviacionLateraleje5) 
    {
        if (desviacionLateraleje5!=null) 
        {
            this.desviacionLateraleje5 = desviacionLateraleje5;
        }
    }

    public void setDesviacionLateraleje6(String desviacionLateraleje6) 
    {
        if (desviacionLateraleje6!=null) 
        {
            this.desviacionLateraleje6 = desviacionLateraleje6;
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

    public String getDesviacionLateraleje1() {
        return desviacionLateraleje1;
    }

    public String getDesviacionLateraleje2() {
        return desviacionLateraleje2;
    }

    public String getDesviacionLateraleje3() {
        return desviacionLateraleje3;
    }

    public String getDesviacionLateraleje4() {
        return desviacionLateraleje4;
    }

    public String getDesviacionLateraleje5() {
        return desviacionLateraleje5;
    }

    public String getDesviacionLateraleje6() {
        return desviacionLateraleje6;
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
        return "{\"desviacionLateraleje1\":\""+desviacionLateraleje1+"\","
                +"\"desviacionLateraleje2\":\""+desviacionLateraleje2+"\","
                +"\"desviacionLateraleje3\":\""+desviacionLateraleje3+"\","
                +"\"desviacionLateraleje4\":\""+desviacionLateraleje4+"\","
                +"\"desviacionLateraleje5\":\""+desviacionLateraleje5+"\","
                +"\"desviacionLateraleje6\":\""+desviacionLateraleje6+"\","
                +"\"tablaAfectada\":\""+tablaAfectada+"\","
                +"\"idRegistro\":\""+idRegistro+"\"}";  
    }
}

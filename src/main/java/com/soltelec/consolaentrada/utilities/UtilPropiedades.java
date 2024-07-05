package com.soltelec.consolaentrada.utilities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//import com.soltelec.estandar.SartComunicadorException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @author GerenciaDesarrollo
 */
public class UtilPropiedades {

    /**
     * Metodo para cargar una propiedad de un archivo properties
     *
     * @param nombrePropiedad
     * @param nombreArchivo
     * @return el valor de la propiedad
     */
    
    
    public static String cargarPropiedad(String nombrePropiedad) {
        return "";
    }

    /**
     * Metodo encargado de dar formato a las fechas
     *
     * @param date Fecha a convertir
     * @param formato Formato en el que se desea convertir. Ejemplo: (ddMMyyyy
     * HH:mm)
     * @return Fecha convertida
     */
    public static String convertiFechas(Date date, String formato) {
        return new SimpleDateFormat(formato).format(date);
    }

    public static String cargarEquipos(String nombrePropiedad) {
        return "";
     }
     public static String decimalFormat(Float args) {
        DecimalFormat df = new DecimalFormat("####.####");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(sym);
        System.out.print("-----------estoy en decimal Format float---------");
        System.out.print(df.format(args));
        return df.format(args);
    }
     public static String decimalFormat(Double args) {
        DecimalFormat df = new DecimalFormat("####.####");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(sym);
        System.out.print("-----------estoy en decimal Format double---------");
        System.out.print(df.format(args));
        return df.format(args);
    }
}
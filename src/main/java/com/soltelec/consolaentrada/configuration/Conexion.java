/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.configuration;

import com.soltelec.appencript.Utilidades;
import com.soltelec.consolaentrada.utilities.Serializador;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 *
 * @author Gerencia TIC
 */
public class Conexion implements Serializable {

    public static final String ARCHIVO = "Conexion.stlc";  
  
    private static Conexion secondInstance;
    private String driver;      
    protected static String baseDatos;
    protected static String ipServidor;
    protected static String usuario;
    protected static String puerto;
    protected static String contrasena;
    private static Conexion instance;
    private static char patron='s';   
    private Conexion() {
        baseDatos = "db_cda";
        ipServidor = "localhost";
        puerto = "3306";
        usuario = "root";
        contrasena = "admin";
        driver = "com.mysql.jdbc.Driver";
    }

    public static Conexion getInstance() {      
        loadAsObject();
        return instance;
    }
    
    public static void loadAsObject() {        
        FileReader fw;        
        String CARPETA = "./configuracion/"; //ruta donde se desea guardar o consultar el archivo
        String EXTENSION = ".soltelec"; //extension con la cual se desea guardar el objeto.
        String ARCHIVO = "Conexion"; //Nombre del Objeto.
        try {
            fw = new FileReader(CARPETA + ARCHIVO + EXTENSION);
            BufferedReader br = new BufferedReader(fw);                     
            String tmpStr="";
            int posCut;
            try{
                 tmpStr = Utilidades.deCifrar(br.readLine());
            } catch (Throwable e) {            }         
            posCut = tmpStr.indexOf("&");
            Conexion.baseDatos = tmpStr.substring(posCut +1, tmpStr.length());
            System.out.println("db:" + Conexion.baseDatos);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            System.out.println("fecha: "+ dtf.format(LocalDateTime.now()));
            tmpStr = br.readLine();
            posCut = tmpStr.indexOf(":");
            Conexion.ipServidor =tmpStr.substring(posCut+1, tmpStr.length());
            System.out.println("ser:" + Conexion.ipServidor);
            tmpStr = Utilidades.deCifrar(br.readLine());
            posCut = tmpStr.indexOf("&");
            Conexion.usuario = tmpStr.substring(posCut+1, tmpStr.length());
            System.out.println("us: " + Conexion.usuario);
            tmpStr = Utilidades.deCifrar(br.readLine());
            posCut = tmpStr.indexOf("&");
            Conexion.puerto =tmpStr.substring(posCut+1, tmpStr.length());
            System.out.println("p" + Conexion.puerto+ "p");
            tmpStr = Utilidades.deCifrar(br.readLine());
            posCut = tmpStr.indexOf("&");
            Conexion.contrasena =tmpStr.substring(posCut +1, tmpStr.length());   
           System.out.println("Æ0H‹t$8HƒÄ _ÃL‹ÆH‹>"+ Conexion.contrasena+">ß   Ã‹Êé"  );
             
        } catch (IOException ex) {
            Logger.getLogger(Serializador.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
     
    public static String getBaseDatos() {
        return baseDatos;
    }

    public void setBaseDatos(String baseDatos) {
        this.baseDatos = baseDatos;
    }

    public static String getIpServidor() {
        return ipServidor;
    }

    public void setIpServidor(String ipServidor) {
        this.ipServidor = ipServidor;
    }

    public static String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public static String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public static String getUrl() {
        return "jdbc:mysql://" + ipServidor + ":" + puerto + "/" + baseDatos + "?zeroDateTimeBehavior=convertToNull";        
//        return "jdbc:mysql://" + ipServidor + "/" + baseDatos + "?zeroDateTimeBehavior=convertToNull";
    }

    /**
     * @return the driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * @param driver the driver to set
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * @return the puerto
     */
    public String getPuerto() {
        return puerto;
    }

    /**
     * @param puerto the puerto to set
     */
    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.configuration;




import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import com.soltelec.appencript.Utilidades;
import com.soltelec.consolaentrada.utilities.CMensajes;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Gerencia TIC
 */
public class Conexion implements Serializable {

    public static final String ARCHIVO = "Conexion.stlc"; 
    private String driver = "com.mysql.jdbc.Driver";      
    protected static String baseDatos;
    protected static String ipServidor;
    protected static String usuario;
    protected static String puerto;
    protected static String contrasena;
    private static Conexion instance;

    private static final String USER_AGENT = "Mozilla/5.0";

    public static Conexion getInstance() {     
       setConexionFromFile();
       return instance;
    }

    private static final String CARPETA = "./configuracion/";
    private static final String EXTENSION = ".soltelec";
    private static final String NOMBRE_ARCHIVO = "Conexion";

    public static void setConexionFromFile() {
        try {

            FileReader fileReader = new FileReader(CARPETA + NOMBRE_ARCHIVO + EXTENSION);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String linea;
            int numLinea = 0;
            List<String> datos = new ArrayList<>();
            String response = "";

            while ((linea = bufferedReader.readLine()) != null) {
                
                numLinea +=1;
                String dato = "";
                
                if(numLinea !=2 && numLinea !=4){
                    linea = Utilidades.deCifrar(linea);
                    int indice = linea.indexOf(":");
                    dato = linea.substring(indice +1, linea.length());
                }else{
                    int indice = linea.indexOf(":");
                    dato = linea.substring(indice +1, linea.length());
                }
                
                datos.add(dato);
            }

            String consulta = "SELECT NIT FROM cda WHERE id_cda = 1";
            String url = "jdbc:mysql://" + datos.get(1) + ":" + datos.get(3) + "/" + datos.get(0) + "?zeroDateTimeBehavior=convertToNull";
            

            boolean license = false;
            try (Connection conexion = DriverManager.getConnection(url, datos.get(2), datos.get(4));
                PreparedStatement consultaDagma = conexion.prepareStatement(consulta)) {

                //rc representa el resultado de la consulta
                try (ResultSet rc = consultaDagma.executeQuery()) {
                    while (rc.next()) {
                        String nit = rc.getString("NIT");
                        String urlPeticion = "http://api.soltelec.com:8087/api/public/"+nit;
                        response = sendGet(urlPeticion);

                        if (response.equalsIgnoreCase("true")) license = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!license){
                bufferedReader.close();
                CMensajes.mensajeError("Su licencia ha expirado, contactese con Soltelec");
                throw new RuntimeException("respuesta del servidor licencia: \n"+ response);
            } 

            baseDatos = datos.get(0);
            ipServidor = datos.get(1);
            usuario = datos.get(2);
            puerto = datos.get(3);
            contrasena = datos.get(4);

            bufferedReader.close();
        } catch (IOException ex) {
            CMensajes.mensajeError("No se pudo leer el archivo de conexion "+ CARPETA + NOMBRE_ARCHIVO + EXTENSION);
            System.out.println("No se pudo leer el archivo de conexion de la base de datos");
            ex.printStackTrace();
        }
    }

    // Método para enviar una solicitud GET
    private static String sendGet(String url) {
        HttpURLConnection con = null;
        try {
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // código 200 OK
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) { // código 404
                return "true";
            }else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) { // código 500
                return "No encontro el nit en la base de datos de licencia: 500 Internal Server Error";
            }else {
                return "Unexpected Response Code: " + responseCode;
            }
        } catch (java.net.UnknownHostException | java.net.ConnectException | java.net.SocketTimeoutException e) {
            return "true";
        }   catch (Exception e) {
            return "Exception: " + e.getMessage();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
    
    public static String getBaseDatos() {
        return baseDatos;
    }

    public static void setBaseDatos(String baseDatos) {
        Conexion.baseDatos = baseDatos;
    }

    public static String getIpServidor() {
        return ipServidor;
    }

    public static void setIpServidor(String ipServidor) {
        Conexion.ipServidor = ipServidor;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        Conexion.usuario = usuario;
    }

    public static String getContrasena() {
        return contrasena;
    }

    public static void setContrasena(String contrasena) {
        Conexion.contrasena = contrasena;
    }

    public static String getUrl() {
        return "jdbc:mysql://" + ipServidor + ":" + puerto + "/" + baseDatos + "?zeroDateTimeBehavior=convertToNull";
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
    public static String getPuerto() 
    {
        return puerto;
    }

    /**
     * @param puerto the puerto to set
     */
    public void setPuerto(String puerto) {
        Conexion.puerto = puerto;
    }
}

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

import com.soltelec.consolaentrada.utilities.CMensajes;
import com.soltelec.consolaentrada.utilities.CifraDesifra;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Gerencia TIC
 */
public class Conexion implements Serializable {

    public static final String ARCHIVO = "Conexion.stlc"; 
    private String driver = "com.mysql.cj.jdbc.Driver";   
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
    private static boolean licencia = false;

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
                    linea = CifraDesifra.deCifrar(linea);
                    int indice = linea.indexOf(":");
                    dato = linea.substring(indice +1, linea.length());
                }else{
                    int indice = linea.indexOf(":");
                    dato = linea.substring(indice +1, linea.length());
                }
                
                datos.add(dato);
            }

            String consulta = "SELECT NIT FROM cda WHERE id_cda = 1";
            String url = "jdbc:mysql://" + datos.get(1) + ":" + datos.get(3) + "/" + datos.get(0) + "?zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false";
            System.out.println("URL: "+url);

            String user = datos.get(2);
            String password = datos.get(4);
            
            if (!licencia) {
                try (Connection conexion = DriverManager.getConnection(url, user, password)) {
            
                    // Ejecutar el comando para desactivar ONLY_FULL_GROUP_BY
                    try (Statement statement = conexion.createStatement()) {
                        String sql = "SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''))";
                        statement.execute(sql);
                        System.out.println("Modo ONLY_FULL_GROUP_BY desactivado correctamente.");
                    }
            
                    // Preparar y ejecutar la consulta SQL después de modificar el modo SQL
                    try (PreparedStatement consultaDagma = conexion.prepareStatement(consulta)) {
            
                        String nit = "";
                        // Ejecutar la consulta
                        try (ResultSet rc = consultaDagma.executeQuery()) {
                            while (rc.next()) {
                                nit = rc.getString("NIT");
                                String urlPeticion = "http://api.soltelec.com:8087/api/public/" + nit;
                                response = sendGet(urlPeticion);
            
                                if (response.equalsIgnoreCase("true")) licencia = true;
                            }
                        }
            
                        // Si no se encuentra el NIT
                        if (nit.equals("")) {
                            CMensajes.mensajeError("La columna 'id_cda' de la tabla 'cda' de la base de datos debe ser 1 \no el cda no cuenta con NIT registrado en esa misma tabla\n Contactese con Soltelec.\n");
                            throw new RuntimeException("Error porque no logro encontrar los datos de la tabla cda para id_cda = 1 o el campo NIT de esa misma tabla esta vacio\n");
                        }
                        
                    }
            
                } catch (Exception e) {
                    CMensajes.mensajeError(
                        "Hubo un error al tratar de conectarse a la base de datos, contactese con Soltelec.\n" +
                        "Revise por favor el archivo " + CARPETA + NOMBRE_ARCHIVO + EXTENSION + "\n" +
                        "Si este mismo error se repite en todos los computadores del CDA revise el servidor"
                    );
                    e.printStackTrace();
                    throw new RuntimeException("Error al tratar de conectarse con el base de datos: \n" + e.getMessage());
                }
            }

            if (!licencia){
                bufferedReader.close();
                CMensajes.mensajeError(
                    "Su licencia ha expirado o no tiene conexion a internet, contactese con Soltelec\n"
                );
                String mensajeRespuesta = response.equalsIgnoreCase("false") ? "Licencia expirada" : response;
                System.out.println("Respuesta del VPS ante la expiracion de licencia: "+mensajeRespuesta);
                throw new RuntimeException("respuesta del VPS: \n"+ mensajeRespuesta);
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
                return "404 Not Found";
            }else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) { // código 500
                return "El CDA no se encuentra inscrito en la base de datos del VPS";
            }else {
                return "Unexpected Response Code: " + responseCode;
            }
        } catch (java.net.UnknownHostException e) {
            return "Unknown Host Exception: " + e.getMessage();
        } catch (java.net.ConnectException e) {
            return "true";
        } catch (java.net.SocketTimeoutException e) {
            return "Socket Timeout Exception: " + e.getMessage();
        } catch (Exception e) {
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

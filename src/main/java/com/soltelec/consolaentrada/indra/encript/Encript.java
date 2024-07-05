/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.indra.encript;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *
 * @author GerenciaDesarrollo
 */
public class Encript {

    private static final String USER_AGENT = "Chrome/77.0.3865.120";

    public static String encript(String cadena, String urlServicio) {
        try {
          
            String cadenaEncode = URLEncoder.encode(cadena);            
            URL obj = new URL(urlServicio + cadenaEncode);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            System.out.println("HTTP CONECT ");
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            System.out.println(" send HTTP  ");
            int responseCode = con.getResponseCode();    
                   System.out.println(" responseCode "+responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
                  System.out.println(" in "+in.toString());
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }           
            //http://192.168.0.200/sicov.asmx
            //http://192.168.0.120:8080/wsEncripcion/cliente.php?cadena=
            in.close();
            return response.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar los datos por favor reiniciar el servicio de Mysql y iniciar xampp" + e.getMessage());
        }
    }
}

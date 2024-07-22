/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.soltelec.consolaentrada.configuration.Conexion;

/**
 *
 * @author GerenciaDesarrollo
 */
public class UtilConexion {
    
    public static Connection obtenerConexion() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Conexion conex = Conexion.getInstance();
        //Crear el objeto de <span class="IL_AD" id="IL_AD12">conexion</span> a la base de datos
        Connection conexion = DriverManager.getConnection(Conexion.getUrl(),Conexion.getUsuario(),Conexion.getContrasena());
        return conexion;        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.utilities;

import com.soltelec.consolaentrada.configuration.Conexion;
import com.soltelec.consolaentrada.models.entities.ClaseVehiculo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GerenciaDesarrollo
 */
public class Serializador {
    
    private static final String CARPETA = "./configuracion/"; 
    
    public static void saveAsFile(Serializable object, String nombreArchivo) throws IOException {
        File file = new File(CARPETA);
        
        if (!file.exists()) {
            file.mkdir();
        }
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CARPETA + nombreArchivo))) {
            out.writeObject(object);
            System.out.println("Se Guardo el archivo con nombre: " + nombreArchivo);
        }        
    }    
}

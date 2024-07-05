/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author SOLTELEC
 */
public class GenerarArchivoTramaCI2
{
    
    public static void setFur(String trama) 
    {
        try {

            File file = new File("C:\\opt\\TramaCruda.txt");
            if (file.exists()) 
            {
                file.delete();
            }
            try 
            {
                file.createNewFile();
            } catch (IOException ex) 
            {
                System.out.println("Error creando el archivo");
                ex.printStackTrace(System.err);
            }
            PrintWriter output = null;
            try {
                //Generar el archivo txt de la entrada, la entrada corregida y la salida          
                output = new PrintWriter(file);
                output.println("-.** Trama Enviada .-** ");
                output.println(" ");
                output.println("-.- " + trama);
            } catch (FileNotFoundException ex) 
            {
                ex.printStackTrace(System.err);
            } catch (Exception exc) {
                exc.printStackTrace();
            } finally {
                output.close();
            }

        } catch (Exception e) 
        {

        }
    }
}

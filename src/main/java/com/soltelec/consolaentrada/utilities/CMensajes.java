/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.utilities;

import javax.swing.JOptionPane;

/**
 *
 * @author Soltelec Ltda
 */
public class CMensajes {
    
    public static void mensajeError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void mensajeAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    
    public static void mensajeCorrecto(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Exito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void mostrarExcepcion(Exception excepcion) {
        JOptionPane.showMessageDialog(null, "Error: " + excepcion.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        excepcion.printStackTrace(System.err);
    }
    
    public static boolean mensajePregunta(String mensaje) {
        boolean estado = true;
        int i = JOptionPane.showOptionDialog(null, mensaje, "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
        
        if (i != JOptionPane.YES_OPTION) {
            estado = false;
        }
        
        return estado;
    }
}

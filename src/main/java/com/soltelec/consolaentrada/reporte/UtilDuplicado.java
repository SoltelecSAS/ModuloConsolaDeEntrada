/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.reporte;

import javax.swing.*;

/**
 *
 * @author User
 */
public class UtilDuplicado {
    
    public static String preguntarMotivoAnulacion() {
          String strSeleccionMotivo = null;
          Object objSeleccion = JOptionPane.showInputDialog(
                    null,
                    "Motivo de la anulacion",
                    "Duplicado",
                    JOptionPane.QUESTION_MESSAGE,
                    null,  // null para icono defecto
                    new Object[] {"Perdida o deterioro del documento","Error de ingreso o impresion"}, 
                    "Perdida o deterioro del documento");
                    String strSeleccion = (String)objSeleccion;
                    if(strSeleccion.equals("Perdida o deterioro del documento") ){
                                Object objSeleccionMotivo = JOptionPane.showInputDialog(
                                null,
                                "Motivo de la anulacion",
                                "Duplicado",
                                JOptionPane.QUESTION_MESSAGE,
                                null,  // null para icono defecto
                                new Object[] {"Deterioro del documento","Robo","Extravio"},
                                "Perdida o deterioro del documento");
                                strSeleccionMotivo = (String)objSeleccionMotivo;
                    }else{//si el motivo no es perdida o deterioro del documento
                                Object objSeleccionMotivo = JOptionPane.showInputDialog(
                                null,
                                "Motivo de la anulacion",
                                "Duplicado",
                                JOptionPane.QUESTION_MESSAGE,
                                null,  // null para icono defecto
                                new Object[] {"Error de digitacion","Fallo de impresion","Formato defectuoso","Defectos de papel"}, 
                                "Perdida o deterioro del documento");
                                strSeleccionMotivo = (String)objSeleccionMotivo;
                    }// fin else
                    return strSeleccionMotivo;
    }
    
    
     public static int preguntarConsecutivo(){
        
         int consecutivo = -1;
        boolean numeroInvalido = false;
            
            do {
                String strConsecutivo = JOptionPane.showInputDialog("Digite Consecutivo PREIMPRESO");
                try {
                    consecutivo = Integer.parseInt(strConsecutivo);
                    numeroInvalido = false;
                } catch (NumberFormatException ne) {
                    numeroInvalido = true;
                }
            } while (numeroInvalido);
            
            return consecutivo;
        
        
    }  
    
}

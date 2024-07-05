/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.reporte;

import com.soltelec.consolaentrada.utilities.UtilConexion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *Clase que inserta los datos del certificado en la base de datos.
 * esta clase es la que se invoca cuando se da click en 
 * el boton imprimr en el JRViewer
 * @author Usuario
 */
public class ListenerPrimerCertificado implements ActionListener{

    private long idHojaPrueba,consecutivoRUNTCertificado;
    private java.sql.Timestamp fechaExpedicion,fechaVencimiento;
    private Consultas consultas;
    private ConsultasCertificados consultasCertificados;

    public ListenerPrimerCertificado(long idHojaPrueba, Timestamp fechaExpedicion, Timestamp fechaVencimiento,long consecutivoRUNTCertificados) {
        this.idHojaPrueba = idHojaPrueba;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaVencimiento = fechaVencimiento;
        this.consecutivoRUNTCertificado = consecutivoRUNTCertificados;
        consultas = new Consultas();
        consultasCertificados = new ConsultasCertificados();
    }
    
    
    /**
     *Metodo que registra el certificado en la base de datos
     * al dar click en el boton imprimir en el jasperViewer
     * 
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {        
        Connection cn = null;
        try{
           boolean procesoTerminado = false;
           cn = UtilConexion.obtenerConexion();           
           long consecutivoPreimpreso = consultas.obtenerSiguienteConsecutivoPreimpreso(cn);
           consecutivoPreimpreso++;
           long consecutivoIngresado = -1;
           int opcion = JOptionPane.showOptionDialog(null,"El siguiente consecutivo de PREIMPRESO es correcto? " +consecutivoPreimpreso,"Seleccione una opcion",
           JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null,null,null);
            if(opcion == JOptionPane.NO_OPTION){
                
            
                String strConsecutivo = JOptionPane.showInputDialog("Digite Consecutivo Preimpreso");
                try{
                    
                    consecutivoPreimpreso = Integer.parseInt(strConsecutivo); 
                    consecutivoIngresado = consecutivoPreimpreso;
                    
                }catch(NumberFormatException ne){ 
                    
                    JOptionPane.showMessageDialog(null,"Error ha digitado un consecutivo invalido");                    
                    return;//no deja continuar
                }
            
            }//end de que desea cambiar el consecutivo del preimpreso
            else if (opcion==JOptionPane.YES_OPTION){
                
                consecutivoIngresado = consecutivoPreimpreso; 
                
            }
            if(consecutivoIngresado == -1){
                JOptionPane.showMessageDialog(null, "Ingrese consecutivo de preimpreso o confirme que es correcto");
                System.exit(-1);
            }
       
        
            cn.setAutoCommit(false);//Iniciar una transaccion
            consultasCertificados.registrarNuevoCertificado((int)consecutivoPreimpreso, idHojaPrueba, fechaExpedicion, fechaVencimiento, consecutivoRUNTCertificado, cn);
            consultas.cerrarRevision(consecutivoRUNTCertificado, idHojaPrueba, true, cn);           
            cn.commit();
            cn.setAutoCommit(true);
            procesoTerminado = true;
            if(!procesoTerminado){
                JOptionPane.showMessageDialog(null, "Error");
                System.exit(1);
            }       
        } catch(SQLException exc){
            
            try {
                cn.rollback();
                JOptionPane.showMessageDialog(null,"Error insetando los datos del certificado");
                exc.printStackTrace();
            } catch (SQLException ex) {
               ex.printStackTrace();
            }
            
        } catch (Exception ex) {
            
            JOptionPane.showMessageDialog(null, "Posiblemetne no se puede obtener la conexion a la base de datos " + ex.getMessage());
            ex.printStackTrace();
            
        }
        
    }
    
}

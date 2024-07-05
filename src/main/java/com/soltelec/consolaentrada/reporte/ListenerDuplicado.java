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

/**
 *Clase que realiza el proceso de insertar la información del duplicado
 * en la tabla certificados
 * @author Usuario
 */
public class ListenerDuplicado implements ActionListener{
   
    long idHojaPrueba,idCertificado;
    java.sql.Timestamp fechaExpedicion,fechaVencimiento;
    ConsultasCertificados consultasCertificados;
    Consultas consultas;

    public ListenerDuplicado( long idHojaPrueba, long idCertificado,java.sql.Timestamp fechaExpedicion,java.sql.Timestamp fechaVencimiento) {

        this.idHojaPrueba = idHojaPrueba;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaVencimiento = fechaVencimiento;
        this.idCertificado = idCertificado;
        consultasCertificados = new ConsultasCertificados();
        consultas = new Consultas();
        
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        registrarAnulacion_y_CertificadoNuevo();
       
        
        
    }

    public void registrarAnulacion_y_CertificadoNuevo()  {
        Connection cn = null;
        String motivoAnulacion = UtilDuplicado.preguntarMotivoAnulacion();
        int consecutivoPreimpreso = UtilDuplicado.preguntarConsecutivo();
        try{
            
            if(motivoAnulacion != null && !motivoAnulacion.isEmpty() && consecutivoPreimpreso > 0 ){
                
                cn = UtilConexion.obtenerConexion();
                cn.setAutoCommit(false);
                consultasCertificados.anularCertificado(idCertificado, motivoAnulacion, cn);
                long consecutivoRUNTCertificadoAnterior = consultas.consultarCertificadoRUNTAnteriorCertificado(idCertificado, cn);
                consultasCertificados.registrarNuevoCertificado(consecutivoPreimpreso, idHojaPrueba, fechaExpedicion, fechaVencimiento, consecutivoRUNTCertificadoAnterior, cn);
                consultas.cerrarRevision(consecutivoRUNTCertificadoAnterior,idHojaPrueba, true, cn);//se supone que la revision ya debe hacer cerrado
                cn.commit();
                
            } else {
                JOptionPane.showMessageDialog(null, "Ha ingresado mal  el motivo de anulacion o el consecutivo preimpreso \n"
                        + " el programa no dejara imprimir certificado");
                System.exit(1);
            }
        }catch(SQLException exc){
            
            JOptionPane.showMessageDialog( null,"Error durante anulacion del certificado" + exc.getMessage() );
            exc.printStackTrace();
           
        } catch (Exception ex) {
            
            JOptionPane.showMessageDialog( null,"Error :" + ex.getMessage() );
            ex.printStackTrace();
            
        }finally{
            
            if(cn!=null){
                
                try {
                    cn.close();
                } catch (Exception e) {
                }
                
                
            }
            
            
        }     
       
        
        
    }

    
    
   
    
}

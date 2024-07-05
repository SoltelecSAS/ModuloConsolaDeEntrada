/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.reporte;

import com.soltelec.consolaentrada.models.entities.ServicioEspecial;
import com.soltelec.consolaentrada.models.entities.Cda;
import com.soltelec.consolaentrada.models.entities.Certificado;
import com.soltelec.consolaentrada.models.entities.Servicio;
import com.soltelec.consolaentrada.models.entities.TipoVehiculo;
import com.soltelec.consolaentrada.models.entities.Vehiculo;

import java.sql.*;
import java.util.Date;

/**
 * Clase con las operaciones SQL para 
 * los certificados
 * 
 * @author GERENCIADESARROLLO
 */
public class ConsultasCertificados {
    
    
    public void anularCertificado(long idCertificado ,String motivoAnulacion,Connection cn) throws SQLException{
        
         String strAnulacion = "UPDATE certificados SET ANULED = 'Y',ANULEDCOMMENT = ?,ANULEDATE = NOW() WHERE CERTIFICATE = ?";
         PreparedStatement psAnulacion = cn.prepareStatement(strAnulacion);
         psAnulacion.setString(1,motivoAnulacion);
         psAnulacion.setLong(2,idCertificado);
         int anulados = psAnulacion.executeUpdate();
         System.out.println("Numero de registros anulados: " + anulados);  
        
    }
    
    
    public void registrarNuevoCertificado(int consecutivoPreimpreso, long idHojaPrueba,Date fechaExpedicion,Date fechaVencimiento, long consecutivoRunt,Connection cn) throws SQLException{
        
         String strInsertarCertificado = "INSERT INTO certificados(CONSECUTIVE,ANULED,PRINTED,TESTSHEET,PRINTDATE,CERTTYPE,EXPDATE,consecutivo_runt)" + "VALUES (?,'N','Y',?,?,'A',?,?)";
            PreparedStatement psConsecutivo = cn.prepareStatement(strInsertarCertificado);
            
            psConsecutivo.setInt(1, consecutivoPreimpreso);
            psConsecutivo.setLong(2, idHojaPrueba);
            psConsecutivo.setTimestamp( 3, new java.sql.Timestamp( fechaExpedicion.getTime() ) );
            psConsecutivo.setTimestamp(4, new java.sql.Timestamp( fechaVencimiento.getTime() ) );
            psConsecutivo.setLong(5,consecutivoRunt);
            psConsecutivo.executeUpdate(); 
        
    }
    
    
    /**
     * Consultar un certificado usando la hoja de prueba
     * @param idHojaPrueba
     * @param cn
     * @return 
     * @throws java.sql.SQLException 
     */
    public Certificado obtenerCertificadoPorHojaPrueba(int idHojaPrueba, Connection cn) throws SQLException{
        
        Certificado certificado = null;
        String verNoImpreso = "SELECT CERTIFICATE,CONSECUTIVE,consecutivo_runt,EXPDATE,PRINTDATE FROM certificados where TESTSHEET = ? AND ANULED = 'N' AND PRINTED='Y'";//no anulado y no impreso entonces imprimirlo
        PreparedStatement psNoImpreso = cn.prepareStatement(verNoImpreso);
        psNoImpreso.setLong(1, idHojaPrueba);
        ResultSet rsNoImpreso = psNoImpreso.executeQuery();

        if(rsNoImpreso.first()){
            int idCertificado = rsNoImpreso.getInt(1);
            int consecutivo = rsNoImpreso.getInt(2);
            String consecutivoRUNT = String.valueOf(rsNoImpreso.getLong(3));
            Timestamp timestampFechaVencimiento = rsNoImpreso.getTimestamp("EXPDATE");
            Timestamp timestamp1FechaExpedicion = rsNoImpreso.getTimestamp("PRINTDATE");                
            certificado = new Certificado();
            certificado.setId(idCertificado);
            certificado.setConsecutivo(consecutivo);
            certificado.setConsecutivoRunt(consecutivoRUNT);
            certificado.setFechaImpresion(new Date(timestamp1FechaExpedicion.getTime()));
            certificado.setFechaExpedicion(new Date(timestampFechaVencimiento.getTime()));
        }
        
        return certificado;
    }
    
    
    public Cda obtenerInfoCDA(Connection cn) throws SQLException{
        
        Cda infoCda = null;
        String str = "SELECT * FROM cda WHERE id_cda = 1";
        PreparedStatement consultaInfoCDA = cn.prepareStatement(str);
        ResultSet rsInfoCDA = consultaInfoCDA.executeQuery();
        if (rsInfoCDA.next()){
            
            infoCda = new Cda();
            infoCda.setNombre( rsInfoCDA.getString("nombre") );
            infoCda.setNit( rsInfoCDA.getString("NIT"));
            infoCda.setCertificado(rsInfoCDA.getString("Certificado_conformidad"));
            infoCda.setNombreResponsable(rsInfoCDA.getString("nom_resp_certificados"));
            
        }
        return infoCda; 
    }
    
    
    public Vehiculo obtenerInfoVehiculo(long idHojaPrueba, Connection cn) throws SQLException{
        
        Vehiculo vehiculo = null;
        
        String strFechaMatricula = "Select v.Fecha_registro,v.SERVICE,v.CARTYPE,v.SPSERVICE from vehiculos as v inner join hoja_pruebas as hp "
        +"on v.CAR = hp.Vehiculo_for and hp.TESTSHEET = ?";
        PreparedStatement psFechaMatricula = cn.prepareStatement(strFechaMatricula);
        psFechaMatricula.setLong(1,idHojaPrueba);
        ResultSet rsFechaMatricula = psFechaMatricula.executeQuery();
        
        if(rsFechaMatricula.next()){
        
            vehiculo = new Vehiculo();
            vehiculo.setFechaRegistro(rsFechaMatricula.getDate(1));
            vehiculo.setServicios( new Servicio( rsFechaMatricula.getInt(2) ) );
            vehiculo.setTipoVehiculo( new TipoVehiculo( rsFechaMatricula.getInt(3)) );
            vehiculo.setServicioEspecial(new ServicioEspecial(rsFechaMatricula.getInt(4)));
            
        }
        
        return vehiculo;
    }
    
    /**
     * Consulta la fecha de expedicion de un certificado. Retorna null en caso
     * de que no exista
     * @param idCertificadoAnterior el id del certificado del que se le busca la fecha de expedicion
     * @param cn
     * @return La fecha de expedicion del certificado, null en caso de que no se encuentre
     * @throws SQLException 
     */
    public Date obtenerFechaExpedicionCertificado(long idCertificadoAnterior,Connection cn) throws SQLException{
       
        Date fechaExpedicion = null;
        
        String strFecha = "SELECT PRINTDATE FROM certificados WHERE CERTIFICATE = ?"; 
        PreparedStatement consultaFecha = cn.prepareStatement(strFecha);
        consultaFecha.setLong(1,idCertificadoAnterior);
        ResultSet rsFecha = consultaFecha.executeQuery();
        if(rsFecha.next()){
            java.sql.Timestamp timestampFechaExpedicion = rsFecha.getTimestamp(1);
            fechaExpedicion = new Date(timestampFechaExpedicion.getTime());
        }
        return fechaExpedicion;
        
    }
    
    
    /**
     * Consulta la fecha de vencimiento de un certificado dada la id del Certificado
     * 
     * @param idCertificadoAnterior id del certificado del que se va a consultar la fecha de vencimiento
     * @param cn
     * @return la fecha de vencimiento o null en caso de no encontrarla
     * @throws SQLException 
     */
    public Date obtenerFechaVencimientoCertificado(long idCertificadoAnterior,Connection cn)throws SQLException{
        
        
        Date fechaVencimiento = null;
        
        String strConsultaFechaVenc = "SELECT EXPDATE FROM certificados WHERE CERTIFICATE=?";
        PreparedStatement ps = cn.prepareStatement(strConsultaFechaVenc);
        ps.setLong(1,idCertificadoAnterior);
        ResultSet rsFechaVenc = ps.executeQuery();
        java.sql.Timestamp timeStampFechaVenc = new Timestamp(1);
        if( rsFechaVenc.next() ){//retorna null para los certificados anteriores.
            
            timeStampFechaVenc = rsFechaVenc.getTimestamp("EXPDATE");
            fechaVencimiento = new Date(timeStampFechaVenc.getTime());
                       
        }
        
        return fechaVencimiento;
        
    }
    
    
}

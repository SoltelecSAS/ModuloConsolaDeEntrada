/**
 * SicovSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.soltelec.consolaentrada.sicov.indra;

public interface SicovSoap_PortType extends java.rmi.Remote {
    public java.lang.String setConexion(java.lang.String cadena) throws java.rmi.RemoteException;
    public com.soltelec.consolaentrada.sicov.indra.EventosRespuesta setEventsRTMEC(java.lang.String cadena) throws java.rmi.RemoteException;
    public com.soltelec.consolaentrada.sicov.indra.EventosRespuesta enviarEventosSicov(java.lang.String cadena) throws java.rmi.RemoteException;
    public com.soltelec.consolaentrada.sicov.indra.FURRespuesta setFUR(java.lang.String cadena) throws java.rmi.RemoteException;
    public com.soltelec.consolaentrada.sicov.indra.FURRespuesta enviarFurSicov(java.lang.String cadena) throws java.rmi.RemoteException;
    public com.soltelec.consolaentrada.sicov.indra.VehiculoRespuesta getInfoVehiculo(java.lang.String placa, java.lang.String extranjero) throws java.rmi.RemoteException;
    public com.soltelec.consolaentrada.sicov.indra.FURRespuesta enviarRuntSicov(java.lang.String nombreEmpleado, java.lang.String numeroIdentificacion, java.lang.String placa, java.lang.String extranjero, java.lang.String consecutivoRUNT, java.lang.String idRunt, java.lang.String direccionIpEquipo) throws java.rmi.RemoteException;
}

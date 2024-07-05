/**
 * DatosSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.soltelec.consolaentrada.sicov.ci2;

public interface DatosSoap extends java.rmi.Remote {
    public java.lang.String echo(java.lang.String dato) throws java.rmi.RemoteException;
    public Resultado ingresar_fur(Formulario fur) throws java.rmi.RemoteException;
    public Resultado ingresar_fur_v2(Formulario_v2 fur) throws java.rmi.RemoteException;

    /**
     * ingresar_fur_v3 se utiliza para implementar Resolución 0762
     * de 2022 Art.34
     * 
     * Vigencia a partir del 8 de agosto de 2023
     */
    public Resultado ingresar_fur_v3(Formulario_v3 fur) throws java.rmi.RemoteException;
    public VehiculoRUNT consultaRuntSicov(ConsultaJohn dato) throws java.rmi.RemoteException;
    public VehiculoSICOV consultaRUNT(Consulta dato) throws java.rmi.RemoteException;
    public Resultado utilizar_pin(Pin PIN) throws java.rmi.RemoteException;
    public Resultado consulta_pin(Pin PIN) throws java.rmi.RemoteException;
    public Resultado consulta_pin_placa(Pin PIN) throws java.rmi.RemoteException;
    public java.lang.String generico(java.lang.String array_parametros) throws java.rmi.RemoteException;
    public Traer_fotoResponseTraer_fotoResult traer_foto(Pin PIN) throws java.rmi.RemoteException;
}

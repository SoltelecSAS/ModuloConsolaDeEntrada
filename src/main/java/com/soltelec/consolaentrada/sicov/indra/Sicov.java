/**
 * Sicov.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.soltelec.consolaentrada.sicov.indra;

public interface Sicov extends javax.xml.rpc.Service {
    public java.lang.String getSicovSoap12Address();

    public com.soltelec.consolaentrada.sicov.indra.SicovSoap_PortType getSicovSoap12() throws javax.xml.rpc.ServiceException;

    public com.soltelec.consolaentrada.sicov.indra.SicovSoap_PortType getSicovSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getSicovSoapAddress();

    public com.soltelec.consolaentrada.sicov.indra.SicovSoap_PortType getSicovSoap() throws javax.xml.rpc.ServiceException;

    public com.soltelec.consolaentrada.sicov.indra.SicovSoap_PortType getSicovSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

/**
 * DatosLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.soltelec.consolaentrada.sicov.ci2;

public class DatosLocator extends org.apache.axis.client.Service implements Datos {

public DatosLocator(String urlSicov) {
        this.datosSoap_address = urlSicov;
        this.datosSoap12_address = urlSicov;
    }


    public DatosLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DatosLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for datosSoap
    private java.lang.String datosSoap_address = "http://192.168.248.90/ci2_cda_ws/sincrofur.asmx";

    public java.lang.String getdatosSoapAddress() {
        return datosSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String datosSoapWSDDServiceName = "datosSoap";

    public java.lang.String getdatosSoapWSDDServiceName() {
        return datosSoapWSDDServiceName;
    }

    public void setdatosSoapWSDDServiceName(java.lang.String name) {
        datosSoapWSDDServiceName = name;
    }

    public DatosSoap getdatosSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(datosSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getdatosSoap(endpoint);
    }

    public DatosSoap getdatosSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            DatosSoapStub _stub = new DatosSoapStub(portAddress, this);
            _stub.setPortName(getdatosSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setdatosSoapEndpointAddress(java.lang.String address) {
        datosSoap_address = address;
    }


    // Use to get a proxy class for datosSoap12
    private java.lang.String datosSoap12_address = "http://192.168.248.90/ci2_cda_ws/sincrofur.asmx";

    public java.lang.String getdatosSoap12Address() {
        return datosSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String datosSoap12WSDDServiceName = "datosSoap12";

    public java.lang.String getdatosSoap12WSDDServiceName() {
        return datosSoap12WSDDServiceName;
    }

    public void setdatosSoap12WSDDServiceName(java.lang.String name) {
        datosSoap12WSDDServiceName = name;
    }

    public DatosSoap getdatosSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(datosSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getdatosSoap12(endpoint);
    }

    public DatosSoap getdatosSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            DatosSoap12Stub _stub = new DatosSoap12Stub(portAddress, this);
            _stub.setPortName(getdatosSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setdatosSoap12EndpointAddress(java.lang.String address) {
        datosSoap12_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (DatosSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                DatosSoapStub _stub = new DatosSoapStub(new java.net.URL(datosSoap_address), this);
                _stub.setPortName(getdatosSoapWSDDServiceName());
                return _stub;
            }
            if (DatosSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                DatosSoap12Stub _stub = new DatosSoap12Stub(new java.net.URL(datosSoap12_address), this);
                _stub.setPortName(getdatosSoap12WSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("datosSoap".equals(inputPortName)) {
            return getdatosSoap();
        }
        else if ("datosSoap12".equals(inputPortName)) {
            return getdatosSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "datos");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "datosSoap"));
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "datosSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("datosSoap".equals(portName)) {
            setdatosSoapEndpointAddress(address);
        }
        else 
if ("datosSoap12".equals(portName)) {
            setdatosSoap12EndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

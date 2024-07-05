/**
 * VehiculoRespuesta.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.soltelec.consolaentrada.sicov.indra;

public class VehiculoRespuesta  implements java.io.Serializable {
    private int codRespuesta;

    private java.lang.String msjRespuesta;

    private com.soltelec.consolaentrada.sicov.indra.GetInfoVehiculoResult vehRespuesta;

    public VehiculoRespuesta() {
    }

    public VehiculoRespuesta(
           int codRespuesta,
           java.lang.String msjRespuesta,
           com.soltelec.consolaentrada.sicov.indra.GetInfoVehiculoResult vehRespuesta) {
           this.codRespuesta = codRespuesta;
           this.msjRespuesta = msjRespuesta;
           this.vehRespuesta = vehRespuesta;
    }


    /**
     * Gets the codRespuesta value for this VehiculoRespuesta.
     * 
     * @return codRespuesta
     */
    public int getCodRespuesta() {
        return codRespuesta;
    }


    /**
     * Sets the codRespuesta value for this VehiculoRespuesta.
     * 
     * @param codRespuesta
     */
    public void setCodRespuesta(int codRespuesta) {
        this.codRespuesta = codRespuesta;
    }


    /**
     * Gets the msjRespuesta value for this VehiculoRespuesta.
     * 
     * @return msjRespuesta
     */
    public java.lang.String getMsjRespuesta() {
        return msjRespuesta;
    }


    /**
     * Sets the msjRespuesta value for this VehiculoRespuesta.
     * 
     * @param msjRespuesta
     */
    public void setMsjRespuesta(java.lang.String msjRespuesta) {
        this.msjRespuesta = msjRespuesta;
    }


    /**
     * Gets the vehRespuesta value for this VehiculoRespuesta.
     * 
     * @return vehRespuesta
     */
    public com.soltelec.consolaentrada.sicov.indra.GetInfoVehiculoResult getVehRespuesta() {
        return vehRespuesta;
    }


    /**
     * Sets the vehRespuesta value for this VehiculoRespuesta.
     * 
     * @param vehRespuesta
     */
    public void setVehRespuesta(com.soltelec.consolaentrada.sicov.indra.GetInfoVehiculoResult vehRespuesta) {
        this.vehRespuesta = vehRespuesta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VehiculoRespuesta)) return false;
        VehiculoRespuesta other = (VehiculoRespuesta) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.codRespuesta == other.getCodRespuesta() &&
            ((this.msjRespuesta==null && other.getMsjRespuesta()==null) || 
             (this.msjRespuesta!=null &&
              this.msjRespuesta.equals(other.getMsjRespuesta()))) &&
            ((this.vehRespuesta==null && other.getVehRespuesta()==null) || 
             (this.vehRespuesta!=null &&
              this.vehRespuesta.equals(other.getVehRespuesta())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getCodRespuesta();
        if (getMsjRespuesta() != null) {
            _hashCode += getMsjRespuesta().hashCode();
        }
        if (getVehRespuesta() != null) {
            _hashCode += getVehRespuesta().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VehiculoRespuesta.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://190.25.205.154:809?/sicov.asmx", "VehiculoRespuesta"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codRespuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://190.25.205.154:809?/sicov.asmx", "codRespuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("msjRespuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://190.25.205.154:809?/sicov.asmx", "msjRespuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vehRespuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://190.25.205.154:809?/sicov.asmx", "vehRespuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://190.25.205.154:809?/sicov.asmx", "getInfoVehiculoResult"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

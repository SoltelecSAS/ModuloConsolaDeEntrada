/**
 * EventosRespuesta.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.soltelec.consolaentrada.sicov.indra;

public class EventosRespuesta  implements java.io.Serializable {
    private java.lang.String furData;

    private int codRespuesta;

    private java.lang.String msjRespuesta;

    public EventosRespuesta() {
    }

    public EventosRespuesta(
           java.lang.String furData,
           int codRespuesta,
           java.lang.String msjRespuesta) {
           this.furData = furData;
           this.codRespuesta = codRespuesta;
           this.msjRespuesta = msjRespuesta;
    }


    /**
     * Gets the furData value for this EventosRespuesta.
     * 
     * @return furData
     */
    public java.lang.String getFurData() {
        return furData;
    }


    /**
     * Sets the furData value for this EventosRespuesta.
     * 
     * @param furData
     */
    public void setFurData(java.lang.String furData) {
        this.furData = furData;
    }


    /**
     * Gets the codRespuesta value for this EventosRespuesta.
     * 
     * @return codRespuesta
     */
    public int getCodRespuesta() {
        return codRespuesta;
    }


    /**
     * Sets the codRespuesta value for this EventosRespuesta.
     * 
     * @param codRespuesta
     */
    public void setCodRespuesta(int codRespuesta) {
        this.codRespuesta = codRespuesta;
    }


    /**
     * Gets the msjRespuesta value for this EventosRespuesta.
     * 
     * @return msjRespuesta
     */
    public java.lang.String getMsjRespuesta() {
        return msjRespuesta;
    }


    /**
     * Sets the msjRespuesta value for this EventosRespuesta.
     * 
     * @param msjRespuesta
     */
    public void setMsjRespuesta(java.lang.String msjRespuesta) {
        this.msjRespuesta = msjRespuesta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EventosRespuesta)) return false;
        EventosRespuesta other = (EventosRespuesta) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.furData==null && other.getFurData()==null) || 
             (this.furData!=null &&
              this.furData.equals(other.getFurData()))) &&
            this.codRespuesta == other.getCodRespuesta() &&
            ((this.msjRespuesta==null && other.getMsjRespuesta()==null) || 
             (this.msjRespuesta!=null &&
              this.msjRespuesta.equals(other.getMsjRespuesta())));
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
        if (getFurData() != null) {
            _hashCode += getFurData().hashCode();
        }
        _hashCode += getCodRespuesta();
        if (getMsjRespuesta() != null) {
            _hashCode += getMsjRespuesta().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EventosRespuesta.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://190.25.205.154:809?/sicov.asmx", "EventosRespuesta"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("furData");
        elemField.setXmlName(new javax.xml.namespace.QName("http://190.25.205.154:809?/sicov.asmx", "furData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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

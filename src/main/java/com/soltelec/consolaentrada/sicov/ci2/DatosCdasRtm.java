/**
 * DatosCdasRtm.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.soltelec.consolaentrada.sicov.ci2;

public class DatosCdasRtm  implements java.io.Serializable {
    private java.lang.String idCda;

    private java.lang.String cda;

    public DatosCdasRtm() {
    }

    public DatosCdasRtm(
           java.lang.String idCda,
           java.lang.String cda) {
           this.idCda = idCda;
           this.cda = cda;
    }


    /**
     * Gets the idCda value for this DatosCdasRtm.
     * 
     * @return idCda
     */
    public java.lang.String getIdCda() {
        return idCda;
    }


    /**
     * Sets the idCda value for this DatosCdasRtm.
     * 
     * @param idCda
     */
    public void setIdCda(java.lang.String idCda) {
        this.idCda = idCda;
    }


    /**
     * Gets the cda value for this DatosCdasRtm.
     * 
     * @return cda
     */
    public java.lang.String getCda() {
        return cda;
    }


    /**
     * Sets the cda value for this DatosCdasRtm.
     * 
     * @param cda
     */
    public void setCda(java.lang.String cda) {
        this.cda = cda;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosCdasRtm)) return false;
        DatosCdasRtm other = (DatosCdasRtm) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idCda==null && other.getIdCda()==null) || 
             (this.idCda!=null &&
              this.idCda.equals(other.getIdCda()))) &&
            ((this.cda==null && other.getCda()==null) || 
             (this.cda!=null &&
              this.cda.equals(other.getCda())));
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
        if (getIdCda() != null) {
            _hashCode += getIdCda().hashCode();
        }
        if (getCda() != null) {
            _hashCode += getCda().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosCdasRtm.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "DatosCdasRtm"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCda");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "idCda"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cda");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "cda"));
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

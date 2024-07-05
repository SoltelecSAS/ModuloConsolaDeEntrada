/**
 * Consulta.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.soltelec.consolaentrada.sicov.ci2;

public class Consulta  implements java.io.Serializable {
    private java.lang.String usuario;

    private java.lang.String clave;

    private java.lang.String placa;

    public Consulta() {
    }

    public Consulta(
           java.lang.String usuario,
           java.lang.String clave,
           java.lang.String placa) {
           this.usuario = usuario;
           this.clave = clave;
           this.placa = placa;
    }


    /**
     * Gets the usuario value for this Consulta.
     * 
     * @return usuario
     */
    public java.lang.String getUsuario() {
        return usuario;
    }


    /**
     * Sets the usuario value for this Consulta.
     * 
     * @param usuario
     */
    public void setUsuario(java.lang.String usuario) {
        this.usuario = usuario;
    }


    /**
     * Gets the clave value for this Consulta.
     * 
     * @return clave
     */
    public java.lang.String getClave() {
        return clave;
    }


    /**
     * Sets the clave value for this Consulta.
     * 
     * @param clave
     */
    public void setClave(java.lang.String clave) {
        this.clave = clave;
    }


    /**
     * Gets the placa value for this Consulta.
     * 
     * @return placa
     */
    public java.lang.String getPlaca() {
        return placa;
    }


    /**
     * Sets the placa value for this Consulta.
     * 
     * @param placa
     */
    public void setPlaca(java.lang.String placa) {
        this.placa = placa;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Consulta)) return false;
        Consulta other = (Consulta) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.usuario==null && other.getUsuario()==null) || 
             (this.usuario!=null &&
              this.usuario.equals(other.getUsuario()))) &&
            ((this.clave==null && other.getClave()==null) || 
             (this.clave!=null &&
              this.clave.equals(other.getClave()))) &&
            ((this.placa==null && other.getPlaca()==null) || 
             (this.placa!=null &&
              this.placa.equals(other.getPlaca())));
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
        if (getUsuario() != null) {
            _hashCode += getUsuario().hashCode();
        }
        if (getClave() != null) {
            _hashCode += getClave().hashCode();
        }
        if (getPlaca() != null) {
            _hashCode += getPlaca().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Consulta.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "Consulta"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usuario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Usuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clave");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Clave"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("placa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Placa"));
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

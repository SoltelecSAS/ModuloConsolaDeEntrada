/**
 * Pin.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.soltelec.consolaentrada.sicov.ci2;

public class Pin  implements java.io.Serializable {
    private java.lang.String usuario;

    private java.lang.String clave;

    private java.lang.String p_pin;

    private java.lang.String p_tipo_rtm;

    private java.lang.String p_placa;

    public Pin() {
    }

    public Pin(
           java.lang.String usuario,
           java.lang.String clave,
           java.lang.String p_pin,
           java.lang.String p_tipo_rtm,
           java.lang.String p_placa) {
           this.usuario = usuario;
           this.clave = clave;
           this.p_pin = p_pin;
           this.p_tipo_rtm = p_tipo_rtm;
           this.p_placa = p_placa;
    }

    

    @Override
    public String toString() {
        return "Pin [usuario=" + usuario + ", clave=" + clave + ", p_pin=" + p_pin + ", p_tipo_rtm=" + p_tipo_rtm
                + ", p_placa=" + p_placa + ", __equalsCalc=" + __equalsCalc + ", __hashCodeCalc=" + __hashCodeCalc
                + "]";
    }

    /**
     * Gets the usuario value for this Pin.
     * 
     * @return usuario
     */
    public java.lang.String getUsuario() {
        return usuario;
    }


    /**
     * Sets the usuario value for this Pin.
     * 
     * @param usuario
     */
    public void setUsuario(java.lang.String usuario) {
        this.usuario = usuario;
    }


    /**
     * Gets the clave value for this Pin.
     * 
     * @return clave
     */
    public java.lang.String getClave() {
        return clave;
    }


    /**
     * Sets the clave value for this Pin.
     * 
     * @param clave
     */
    public void setClave(java.lang.String clave) {
        this.clave = clave;
    }


    /**
     * Gets the p_pin value for this Pin.
     * 
     * @return p_pin
     */
    public java.lang.String getP_pin() {
        return p_pin;
    }


    /**
     * Sets the p_pin value for this Pin.
     * 
     * @param p_pin
     */
    public void setP_pin(java.lang.String p_pin) {
        this.p_pin = p_pin;
    }


    /**
     * Gets the p_tipo_rtm value for this Pin.
     * 
     * @return p_tipo_rtm
     */
    public java.lang.String getP_tipo_rtm() {
        return p_tipo_rtm;
    }


    /**
     * Sets the p_tipo_rtm value for this Pin.
     * 
     * @param p_tipo_rtm
     */
    public void setP_tipo_rtm(java.lang.String p_tipo_rtm) {
        this.p_tipo_rtm = p_tipo_rtm;
    }


    /**
     * Gets the p_placa value for this Pin.
     * 
     * @return p_placa
     */
    public java.lang.String getP_placa() {
        return p_placa;
    }


    /**
     * Sets the p_placa value for this Pin.
     * 
     * @param p_placa
     */
    public void setP_placa(java.lang.String p_placa) {
        this.p_placa = p_placa;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Pin)) return false;
        Pin other = (Pin) obj;
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
            ((this.p_pin==null && other.getP_pin()==null) || 
             (this.p_pin!=null &&
              this.p_pin.equals(other.getP_pin()))) &&
            ((this.p_tipo_rtm==null && other.getP_tipo_rtm()==null) || 
             (this.p_tipo_rtm!=null &&
              this.p_tipo_rtm.equals(other.getP_tipo_rtm()))) &&
            ((this.p_placa==null && other.getP_placa()==null) || 
             (this.p_placa!=null &&
              this.p_placa.equals(other.getP_placa())));
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
        if (getP_pin() != null) {
            _hashCode += getP_pin().hashCode();
        }
        if (getP_tipo_rtm() != null) {
            _hashCode += getP_tipo_rtm().hashCode();
        }
        if (getP_placa() != null) {
            _hashCode += getP_placa().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Pin.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "pin"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usuario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "usuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clave");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "clave"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_pin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "p_pin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_tipo_rtm");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "p_tipo_rtm"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_placa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "p_placa"));
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

/**
 * Resultado.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.soltelec.consolaentrada.sicov.ci2;

public class Resultado  implements java.io.Serializable {
    private java.lang.String fechaTransaccion;

    private java.lang.String horaTransaccion;

    private java.lang.String codigoRespuesta;

    private java.lang.String mensajeRespuesta;

    private java.lang.String confirmacion;

    public Resultado() {
    }

    public Resultado(
           java.lang.String fechaTransaccion,
           java.lang.String horaTransaccion,
           java.lang.String codigoRespuesta,
           java.lang.String mensajeRespuesta,
           java.lang.String confirmacion) {
           this.fechaTransaccion = fechaTransaccion;
           this.horaTransaccion = horaTransaccion;
           this.codigoRespuesta = codigoRespuesta;
           this.mensajeRespuesta = mensajeRespuesta;
           this.confirmacion = confirmacion;
    }


    /**
     * Gets the fechaTransaccion value for this Resultado.
     * 
     * @return fechaTransaccion
     */
    public java.lang.String getFechaTransaccion() {
        return fechaTransaccion;
    }


    /**
     * Sets the fechaTransaccion value for this Resultado.
     * 
     * @param fechaTransaccion
     */
    public void setFechaTransaccion(java.lang.String fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }


    /**
     * Gets the horaTransaccion value for this Resultado.
     * 
     * @return horaTransaccion
     */
    public java.lang.String getHoraTransaccion() {
        return horaTransaccion;
    }


    /**
     * Sets the horaTransaccion value for this Resultado.
     * 
     * @param horaTransaccion
     */
    public void setHoraTransaccion(java.lang.String horaTransaccion) {
        this.horaTransaccion = horaTransaccion;
    }


    /**
     * Gets the codigoRespuesta value for this Resultado.
     * 
     * @return codigoRespuesta
     */
    public java.lang.String getCodigoRespuesta() {
        return codigoRespuesta;
    }


    /**
     * Sets the codigoRespuesta value for this Resultado.
     * 
     * @param codigoRespuesta
     */
    public void setCodigoRespuesta(java.lang.String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }


    /**
     * Gets the mensajeRespuesta value for this Resultado.
     * 
     * @return mensajeRespuesta
     */
    public java.lang.String getMensajeRespuesta() {
        return mensajeRespuesta;
    }


    /**
     * Sets the mensajeRespuesta value for this Resultado.
     * 
     * @param mensajeRespuesta
     */
    public void setMensajeRespuesta(java.lang.String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }


    /**
     * Gets the confirmacion value for this Resultado.
     * 
     * @return confirmacion
     */
    public java.lang.String getConfirmacion() {
        return confirmacion;
    }


    /**
     * Sets the confirmacion value for this Resultado.
     * 
     * @param confirmacion
     */
    public void setConfirmacion(java.lang.String confirmacion) {
        this.confirmacion = confirmacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Resultado)) return false;
        Resultado other = (Resultado) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fechaTransaccion==null && other.getFechaTransaccion()==null) || 
             (this.fechaTransaccion!=null &&
              this.fechaTransaccion.equals(other.getFechaTransaccion()))) &&
            ((this.horaTransaccion==null && other.getHoraTransaccion()==null) || 
             (this.horaTransaccion!=null &&
              this.horaTransaccion.equals(other.getHoraTransaccion()))) &&
            ((this.codigoRespuesta==null && other.getCodigoRespuesta()==null) || 
             (this.codigoRespuesta!=null &&
              this.codigoRespuesta.equals(other.getCodigoRespuesta()))) &&
            ((this.mensajeRespuesta==null && other.getMensajeRespuesta()==null) || 
             (this.mensajeRespuesta!=null &&
              this.mensajeRespuesta.equals(other.getMensajeRespuesta()))) &&
            ((this.confirmacion==null && other.getConfirmacion()==null) || 
             (this.confirmacion!=null &&
              this.confirmacion.equals(other.getConfirmacion())));
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
        if (getFechaTransaccion() != null) {
            _hashCode += getFechaTransaccion().hashCode();
        }
        if (getHoraTransaccion() != null) {
            _hashCode += getHoraTransaccion().hashCode();
        }
        if (getCodigoRespuesta() != null) {
            _hashCode += getCodigoRespuesta().hashCode();
        }
        if (getMensajeRespuesta() != null) {
            _hashCode += getMensajeRespuesta().hashCode();
        }
        if (getConfirmacion() != null) {
            _hashCode += getConfirmacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Resultado.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "resultado"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaTransaccion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "FechaTransaccion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("horaTransaccion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "HoraTransaccion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoRespuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "CodigoRespuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mensajeRespuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "MensajeRespuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("confirmacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Confirmacion"));
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

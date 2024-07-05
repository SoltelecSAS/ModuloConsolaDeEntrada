
import java.math.RoundingMode;
import java.text.DecimalFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author usurio
 */
public class SoatNacionales implements java.io.Serializable {

    private java.lang.String noPoliza;

    private java.lang.String fechaExpedicion;

    private java.lang.String fechaVigencia;

    private java.lang.String fechaVencimiento;

    private java.lang.String entidadExpideSoat;

    private java.lang.String estado;

    public SoatNacionales() {
    }

    public SoatNacionales(
            java.lang.String noPoliza,
            java.lang.String fechaExpedicion,
            java.lang.String fechaVigencia,
            java.lang.String fechaVencimiento,
            java.lang.String entidadExpideSoat,
            java.lang.String estado) {
        this.noPoliza = noPoliza;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaVigencia = fechaVigencia;
        this.fechaVencimiento = fechaVencimiento;
        this.entidadExpideSoat = entidadExpideSoat;
        this.estado = estado;
    }

    /**
     * Gets the noPoliza value for this SoatNacionales.
     *
     * @return noPoliza
     */
    public java.lang.String getNoPoliza() {
        return noPoliza;
    }

    /**
     * Sets the noPoliza value for this SoatNacionales.
     *
     * @param noPoliza
     */
    public void setNoPoliza(java.lang.String noPoliza) {
        this.noPoliza = noPoliza;
    }

    /**
     * Gets the fechaExpedicion value for this SoatNacionales.
     *
     * @return fechaExpedicion
     */
    public java.lang.String getFechaExpedicion() {
        return fechaExpedicion;
    }

    /**
     * Sets the fechaExpedicion value for this SoatNacionales.
     *
     * @param fechaExpedicion
     */
    public void setFechaExpedicion(java.lang.String fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    /**
     * Gets the fechaVigencia value for this SoatNacionales.
     *
     * @return fechaVigencia
     */
    public java.lang.String getFechaVigencia() {
        return fechaVigencia;
    }

    /**
     * Sets the fechaVigencia value for this SoatNacionales.
     *
     * @param fechaVigencia
     */
    public void setFechaVigencia(java.lang.String fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    /**
     * Gets the fechaVencimiento value for this SoatNacionales.
     *
     * @return fechaVencimiento
     */
    public java.lang.String getFechaVencimiento() {

        return fechaVencimiento;
    }

    /**
     * Sets the fechaVencimiento value for this SoatNacionales.
     *
     * @param fechaVencimiento
     */
    public void setFechaVencimiento(java.lang.String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * Gets the entidadExpideSoat value for this SoatNacionales.
     *
     * @return entidadExpideSoat
     */
    public java.lang.String getEntidadExpideSoat() {
        return entidadExpideSoat;
    }

    /**
     * Sets the entidadExpideSoat value for this SoatNacionales.
     *
     * @param entidadExpideSoat
     */
    public void setEntidadExpideSoat(java.lang.String entidadExpideSoat) {
        this.entidadExpideSoat = entidadExpideSoat;
    }

    /**
     * Gets the estado value for this SoatNacionales.
     *
     * @return estado
     */
    public java.lang.String getEstado() {
        return estado;
    }

    /**
     * Sets the estado value for this SoatNacionales.
     *
     * @param estado
     */
    public void setEstado(java.lang.String estado) {
        this.estado = estado;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SoatNacionales)) {
            return false;
        }
        SoatNacionales other = (SoatNacionales) obj;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true
                && ((this.noPoliza == null && other.getNoPoliza() == null)
                || (this.noPoliza != null
                && this.noPoliza.equals(other.getNoPoliza())))
                && ((this.fechaExpedicion == null && other.getFechaExpedicion() == null)
                || (this.fechaExpedicion != null
                && this.fechaExpedicion.equals(other.getFechaExpedicion())))
                && ((this.fechaVigencia == null && other.getFechaVigencia() == null)
                || (this.fechaVigencia != null
                && this.fechaVigencia.equals(other.getFechaVigencia())))
                && ((this.fechaVencimiento == null && other.getFechaVencimiento() == null)
                || (this.fechaVencimiento != null
                && this.fechaVencimiento.equals(other.getFechaVencimiento())))
                && ((this.entidadExpideSoat == null && other.getEntidadExpideSoat() == null)
                || (this.entidadExpideSoat != null
                && this.entidadExpideSoat.equals(other.getEntidadExpideSoat())))
                && ((this.estado == null && other.getEstado() == null)
                || (this.estado != null
                && this.estado.equals(other.getEstado())));
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
        if (getNoPoliza() != null) {
            _hashCode += getNoPoliza().hashCode();
        }
        if (getFechaExpedicion() != null) {
            _hashCode += getFechaExpedicion().hashCode();
        }
        if (getFechaVigencia() != null) {
            _hashCode += getFechaVigencia().hashCode();
        }
        if (getFechaVencimiento() != null) {
            _hashCode += getFechaVencimiento().hashCode();
        }
        if (getEntidadExpideSoat() != null) {
            _hashCode += getEntidadExpideSoat().hashCode();
        }
        if (getEstado() != null) {
            _hashCode += getEstado().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc
            = new org.apache.axis.description.TypeDesc(SoatNacionales.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "SoatNacionales"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noPoliza");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "noPoliza"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaExpedicion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "fechaExpedicion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaVigencia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "fechaVigencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaVencimiento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "fechaVencimiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidadExpideSoat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "entidadExpideSoat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "estado"));
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
        return new org.apache.axis.encoding.ser.BeanSerializer(
                _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
            java.lang.String mechType,
            java.lang.Class _javaType,
            javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(
                _javaType, _xmlType, typeDesc);
    }


}

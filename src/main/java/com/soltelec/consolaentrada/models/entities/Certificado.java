/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author GerenciaDesarrollo
 */
@Entity
@Table(name = "certificados")
public class Certificado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "CERTIFICATE")
    private Integer id;
    @Column(name = "CONSECUTIVE")
    private long consecutivo;
    @Basic(optional = false)
    @Column(name = "CERTTYPE")
    private String tipo;
    @Column(name = "ANULED")
    private String anulado;
    @Column(name = "ANULEDCOMMENT")
    private String comentario;
    @Column(name = "PRINTED")
    private String impreso;
    @OneToOne()
    @JoinColumn(name ="TESTSHEET")
    private HojaPruebas hojaPruebas;
    @Basic(optional = false)
    @Column(name = "PRINTDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaImpresion;
    @Basic(optional = false)
    @Column(name = "ANULEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;
    @Column(name="EXPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpedicion;
    @Column(name = "consecutivo_runt")
    private String consecutivoRunt;
    
    public Certificado() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer certificate) {
        this.id = certificate;
    }

    public long getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(long consecutivo) {
        this.consecutivo = consecutivo;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String certtype) {
        this.tipo = certtype;
    }

    public String getAnulado() {
        return anulado;
    }

    public void setAnulado(String anuled) {
        this.anulado = anuled;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String anuledcomment) {
        this.comentario = anuledcomment;
    }

    public String getImpreso() {
        return impreso;
    }

    public void setImpreso(String printed) {
        this.impreso = printed;
    }

    public HojaPruebas getHojaPruebas() {
        return hojaPruebas;
    }

    public void setHojaPruebas(HojaPruebas hojaPrueba) {
        this.hojaPruebas = hojaPrueba;
    }

   
    public Date getFechaImpresion() {
        return fechaImpresion;
    }

    public void setFechaImpresion(Date printdate) {
        this.fechaImpresion = printdate;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date anuledate) {
        this.fechaAnulacion = anuledate;
    }

    public String getConsecutivoRunt() {
        return consecutivoRunt;
    }

    public void setConsecutivoRunt(String consecutivoRunt) {
        this.consecutivoRunt = consecutivoRunt;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Certificado)) {
            return false;
        }
        Certificado other = (Certificado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.soltelec.persistencia.Certificado[certificate=" + id + "]";
    }

    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Date expDate) {
        this.fechaExpedicion = expDate;
    }
    
    

}

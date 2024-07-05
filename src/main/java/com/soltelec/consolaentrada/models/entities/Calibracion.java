/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author GerenciaDesarrollo
 */
@Entity
@Table(name = "calibraciones")
@XmlRootElement
public class Calibracion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CALIBRATION")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "CURDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "VALOR1")
    private String valor1;
    @Column(name = "VALOR2")
    private String valor2;
    @Column(name = "VALOR3")
    private String valor3;
    @Column(name = "VALOR4")
    private String valor4;
    @Column(name = "VALOR5")
    private String valor5;
    @Column(name = "VALOR6")
    private String valor6;
    @Basic(optional = false)
    @Column(name = "aprobada")
    private boolean aprobada;
    @JoinColumn(name = "id_tipo_calibracion", referencedColumnName = "id_tipo_calibracion")
    @ManyToOne
    private TipoCalibracion tipoCalibracion;
    @JoinColumn(name = "TESTTYPE", referencedColumnName = "TESTTYPE")
    @ManyToOne(optional = false)
    private TipoPrueba tipoPrueba;
    @JoinColumn(name = "GEUSER", referencedColumnName = "GEUSER")
    @ManyToOne(optional = false)
    private Usuario usuario;
    @JoinColumn(name = "id_equipo", referencedColumnName = "id_equipo")
    @ManyToOne(optional = false)
    private Equipo equipo;

    public Calibracion() {
    }

    public Calibracion(Integer calibration) {
        this.id = calibration;
    }

    public Calibracion(Integer id, Date fecha, boolean aprobada) {
        this.id = id;
        this.fecha = fecha;
        this.aprobada = aprobada;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer idCalibracion) {
        this.id = idCalibracion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date curdate) {
        this.fecha = curdate;
    }

    public String getValor1() {
        return valor1;
    }

    public void setValor1(String valor1) {
        this.valor1 = valor1;
    }

    public String getValor2() {
        return valor2;
    }

    public void setValor2(String valor2) {
        this.valor2 = valor2;
    }

    public String getValor3() {
        return valor3;
    }

    public void setValor3(String valor3) {
        this.valor3 = valor3;
    }

    public String getValor4() {
        return valor4;
    }

    public void setValor4(String valor4) {
        this.valor4 = valor4;
    }

    public String getValor5() {
        return valor5;
    }

    public void setValor5(String valor5) {
        this.valor5 = valor5;
    }

    public String getValor6() {
        return valor6;
    }

    public void setValor6(String valor6) {
        this.valor6 = valor6;
    }

    public boolean getAprobada() {
        return aprobada;
    }

    public void setAprobada(boolean aprobada) {
        this.aprobada = aprobada;
    }

    public TipoCalibracion getTipoCalibracion() {
        return tipoCalibracion;
    }

    public void setTipoCalibracion(TipoCalibracion idTipoCalibracion) {
        this.tipoCalibracion = idTipoCalibracion;
    }

    public TipoPrueba getTipoPrueba() {
        return tipoPrueba;
    }

    public void setTipoPrueba(TipoPrueba tipoPrueba) {
        this.tipoPrueba = tipoPrueba;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
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
        if (!(object instanceof Calibracion)) {
            return false;
        }
        Calibracion other = (Calibracion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.models.entities.Calibracion[ calibration=" + id + " ]";
    }
    
}

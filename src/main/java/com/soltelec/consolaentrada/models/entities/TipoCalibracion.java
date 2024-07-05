/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Dany
 */
@Entity
@Table(name = "tipo_calibracion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoCalibracion.findAll", query = "SELECT t FROM TipoCalibracion t"),
    @NamedQuery(name = "TipoCalibracion.findByIdTipoCalibracion", query = "SELECT t FROM TipoCalibracion t WHERE t.id = :idTipoCalibracion"),
    @NamedQuery(name = "TipoCalibracion.findByTipoCalibracion", query = "SELECT t FROM TipoCalibracion t WHERE t.nombre = :tipoCalibracion")})
public class TipoCalibracion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_calibracion")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tipo_calibracion")
    private String nombre;
    @OneToMany(mappedBy = "tipoCalibracion")
    private List<Calibracion> calibracionesList;

    public TipoCalibracion() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer idTipoCalibracion) {
        this.id = idTipoCalibracion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String tipoCalibracion) {
        this.nombre = tipoCalibracion;
    }

    @XmlTransient
    public List<Calibracion> getCalibracionesList() {
        return calibracionesList;
    }

    public void setCalibracionesList(List<Calibracion> calibracionesList) {
        this.calibracionesList = calibracionesList;
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
        if (!(object instanceof TipoCalibracion)) {
            return false;
        }
        TipoCalibracion other = (TipoCalibracion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.models.entities.TipoCalibracion[ idTipoCalibracion=" + id + " ]";
    }
    
}

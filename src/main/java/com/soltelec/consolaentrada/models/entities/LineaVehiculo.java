/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author GerenciaDesarrollo
 */
@Entity
@Table(name = "lineas_vehiculos")
@NamedQueries({
    @NamedQuery(name = "LineaVehiculo.findAll", query = "SELECT l FROM LineaVehiculo l"),
    @NamedQuery(name = "LineaVehiculo.findByCarline", query = "SELECT l FROM LineaVehiculo l WHERE l.id = :carline"),
    @NamedQuery(name = "LineaVehiculo.findByCrlcod", query = "SELECT l FROM LineaVehiculo l WHERE l.codigo = :crlcod"),
    @NamedQuery(name = "LineaVehiculo.findByCrlname", query = "SELECT l FROM LineaVehiculo l WHERE l.nombre = :crlname")})
public class LineaVehiculo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "CARLINE")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "CRLCOD")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "CRLNAME")
    private String nombre;
    @JoinColumn(name = "CARMARK", referencedColumnName = "CARMARK")
    @ManyToOne(optional = false)
    private Marca marca;

    public LineaVehiculo() {
    }

    public LineaVehiculo(Integer id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public LineaVehiculo(Integer id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer carline) {
        this.id = carline;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String crlcod) {
        this.codigo = crlcod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String crlname) {
        this.nombre = crlname;
    }

    
    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marcas) {
        this.marca = marcas;
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
        if (!(object instanceof LineaVehiculo)) {
            return false;
        }
        LineaVehiculo other = (LineaVehiculo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

}

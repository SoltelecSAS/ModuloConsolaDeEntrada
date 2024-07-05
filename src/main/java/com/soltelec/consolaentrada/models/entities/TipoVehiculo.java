/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
@Entity
@Table(name = "tipo_vehiculo")
@NamedQueries({
    @NamedQuery(name = "TipoVehiculo.findAll", query = "SELECT t FROM TipoVehiculo t"),
    @NamedQuery(name = "TipoVehiculo.findByCartype", query = "SELECT t FROM TipoVehiculo t WHERE t.id = :cartype"),
    @NamedQuery(name = "TipoVehiculo.findByNombre", query = "SELECT t FROM TipoVehiculo t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoVehiculo.findByDescripcion", query = "SELECT t FROM TipoVehiculo t WHERE t.descripcion = :descripcion")})
public class TipoVehiculo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "CARTYPE")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Nombre")
    private String nombre;
    @Column(name = "Descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoVehiculo", fetch= FetchType.LAZY)
    private List<Permisibles> permisiblesList;

    public TipoVehiculo() {
    }

    public TipoVehiculo(Integer id) {
        this.id = id;
    }

    public TipoVehiculo(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer cartype) {
        this.id = cartype;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Permisibles> getPermisiblesList() {
        return permisiblesList;
    }

    public void setPermisiblesList(List<Permisibles> permisiblesList) {
        this.permisiblesList = permisiblesList;
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
        if (!(object instanceof TipoVehiculo)) {
            return false;
        }
        TipoVehiculo other = (TipoVehiculo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return nombre;
    }

}

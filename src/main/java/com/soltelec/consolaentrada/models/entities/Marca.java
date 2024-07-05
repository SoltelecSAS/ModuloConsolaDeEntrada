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
@Table(name = "marcas")
@NamedQueries({
    @NamedQuery(name = "Marca.findAll", query = "SELECT m FROM Marca m"),
    @NamedQuery(name = "Marca.findByCarmark", query = "SELECT m FROM Marca m WHERE m.id = :id"),
    @NamedQuery(name = "Marca.findByNombremarca", query = "SELECT m FROM Marca m WHERE m.nombre = :nombre")})
public class Marca implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "CARMARK")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Nombre_marca")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marca",fetch= FetchType.LAZY)
    @OrderBy("nombre")
    private List<LineaVehiculo> lineasVehiculosList;

    public Marca() {
    }

    public Marca(Integer id) {
        this.id = id;
    }

    public Marca(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer carmark) {
        this.id = carmark;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombremarca) {
        this.nombre = nombremarca;
    }

  
    public List<LineaVehiculo> getLineasVehiculosList() {
        return lineasVehiculosList;
    }

    public void setLineasVehiculosList(List<LineaVehiculo> lineasVehiculosList) {
        this.lineasVehiculosList = lineasVehiculosList;
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
        if (!(object instanceof Marca)) {
            return false;
        }
        Marca other = (Marca) object;
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "servicios")
@NamedQueries({
    @NamedQuery(name = "Servicio.findAll", query = "SELECT s FROM Servicio s"),
    @NamedQuery(name = "Servicio.findByService", query = "SELECT s FROM Servicio s WHERE s.id = :service"),
    @NamedQuery(name = "Servicio.findByNombreservicio", query = "SELECT s FROM Servicio s WHERE s.nombre = :nombreservicio")})
public class Servicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "SERVICE")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Nombre_servicio")
    private String nombre;
    @Column(name = "servicio_super")
    private Integer servicioSuper;
   

    public Servicio() {
    }

    public Servicio(Integer id) {
        this.id = id;
    }

    public Servicio(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer service) {
        this.id = service;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombreservicio) {
        this.nombre = nombreservicio;
    }

    public Integer getServicioSuper() {
        return servicioSuper;
    }

    public void setServicioSuper(Integer servicioSuper) {
        this.servicioSuper = servicioSuper;
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
        if (!(object instanceof Servicio)) {
            return false;
        }
        Servicio other = (Servicio) object;
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

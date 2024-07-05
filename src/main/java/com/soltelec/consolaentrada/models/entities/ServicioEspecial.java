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
@Table(name = "spservices")
@NamedQueries({
    @NamedQuery(name = "ServicioEspecial.findAll", query = "SELECT s FROM ServicioEspecial s"),
    @NamedQuery(name = "ServicioEspecial.findBySpservice", query = "SELECT s FROM ServicioEspecial s WHERE s.id = :id"),
    @NamedQuery(name = "ServicioEspecial.findBySpservname", query = "SELECT s FROM ServicioEspecial s WHERE s.nombre = :nombre")})
public class ServicioEspecial implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "SPSERVICE")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "SPSERVNAME")
    private String nombre;

    public ServicioEspecial() {
    }

    public ServicioEspecial(Integer id) {
        this.id = id;
    }

    public ServicioEspecial(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer spservice) {
        this.id = spservice;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String spservname) {
        this.nombre = spservname;
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
        if (!(object instanceof ServicioEspecial)) {
            return false;
        }
        ServicioEspecial other = (ServicioEspecial) object;
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

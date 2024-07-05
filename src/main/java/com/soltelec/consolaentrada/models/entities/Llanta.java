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
@Table(name = "llantas")
@NamedQueries({
    @NamedQuery(name = "Llanta.findAll", query = "SELECT l FROM Llanta l"),
    @NamedQuery(name = "Llanta.findByWheel", query = "SELECT l FROM Llanta l WHERE l.id = :wheel"),
    @NamedQuery(name = "Llanta.findByNombrellanta", query = "SELECT l FROM Llanta l WHERE l.nombre = :nombrellanta"),
    @NamedQuery(name = "Llanta.findByRadiollanta", query = "SELECT l FROM Llanta l WHERE l.radio = :radiollanta")})
public class Llanta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "WHEEL")
    private Integer id;
    @Column(name = "Nombre_llanta")
    private String nombre;
    @Column(name = "Radio_llanta")
    private Integer radio;
    

    public Llanta() {
    }

    public Llanta(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer wheel) {
        this.id = wheel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombrellanta) {
        this.nombre = nombrellanta;
    }

    public Integer getRadio() {
        return radio;
    }

    public void setRadio(Integer radiollanta) {
        this.radio = radiollanta;
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
        if (!(object instanceof Llanta)) {
            return false;
        }
        Llanta other = (Llanta) object;
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

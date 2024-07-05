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
@Table(name = "tipo_carroceria")
@NamedQueries({
    @NamedQuery(name = "Tipocarroceria.findAll", query = "SELECT tc FROM Tipocarroceria tc"),
    @NamedQuery(name = "Tipocarroceria.findByTipocarroceria", query = "SELECT tc FROM Tipocarroceria tc WHERE tc.id = :id"),
    @NamedQuery(name = "Tipocarroceria.findBynombretipocarroceria", query = "SELECT tc FROM Tipocarroceria tc WHERE tc.nombre = :nombre")})
public class Tipocarroceria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre_carroceria")
    private String nombre;
    @Column(name = "TIPO")
    private Integer tipo;
    
    public Tipocarroceria() {
    }

    public Tipocarroceria(Integer id) {
        this.id = id;
    }

    public Tipocarroceria(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer tipocarroceria) {
        this.id = tipocarroceria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombrecarroceria) {
        this.nombre = nombrecarroceria;
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
        if (!(object instanceof Tipocarroceria)) {
            return false;
        }
        Tipocarroceria other = (Tipocarroceria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

    /**
     * @return the tipo
     */
    public Integer getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

}
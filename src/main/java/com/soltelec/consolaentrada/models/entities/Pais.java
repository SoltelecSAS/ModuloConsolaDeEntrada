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
@Table(name = "pais")
@NamedQueries({
    @NamedQuery(name = "Pais.findAll", query = "SELECT p FROM Pais p"),
    @NamedQuery(name = "Pais.findByCodigo", query = "SELECT p FROM Pais p WHERE p.id = :codigo"),
    @NamedQuery(name = "Pais.findByNombrePais", query = "SELECT p FROM Pais p WHERE p.nombre = :nombrePais")})
public class Pais implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "codigo")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Nombre_Pais")
    private String nombre;

    public Pais() {
    }

    public Pais(Integer id) {
        this.id = id;
    }

    public Pais(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer codigo) {
        this.id = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombrePais) {
        this.nombre = nombrePais;
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
        if (!(object instanceof Pais)) {
            return false;
        }
        Pais other = (Pais) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombre();
    }
    
}

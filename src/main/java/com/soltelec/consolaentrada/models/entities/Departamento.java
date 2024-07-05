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
 * @author Usuario
 */
@Entity
@Table(name = "departamentos")
@NamedQueries({
    @NamedQuery(name = "Departamento.findAll", query = "SELECT d FROM Departamento d"),
    @NamedQuery(name = "Departamento.findByIdDepartamento", query = "SELECT d FROM Departamento d WHERE d.id = :idDepartamento"),
    @NamedQuery(name = "Departamento.findByNombreDepartamento", query = "SELECT d FROM Departamento d WHERE d.nombre = :nombreDepartamento")})
public class Departamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_departamento")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre_departamento")
    private String nombre;

    @OneToMany(mappedBy="departamento",fetch= FetchType.LAZY)
    @OrderBy("nombre")
    private List<Ciudad> ciudades;
    public Departamento() {
    }

    public Departamento(Integer id) {
        this.id = id;
    }

    public Departamento(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer idDepartamento) {
        this.id = idDepartamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombreDepartamento) {
        this.nombre = nombreDepartamento;
    }

    public List<Ciudad> getCiudades() {
        return ciudades;
    }

    public void setCiudades(List<Ciudad> ciudades) {
        this.ciudades = ciudades;
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
        if (!(object instanceof Departamento)) {
            return false;
        }
        Departamento other = (Departamento) object;
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

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
@Table(name="ciudades")
public class Ciudad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="CITY")
    private Long id;
    @Column(name = "Nombre_ciudad")
    private String nombre;
    @Column(name = "Ciudad_principal")
    private String ciudadPrincipal;
    @Column(name = "codigo")
    private String codigo;
    @ManyToOne
    @JoinColumn(name = "STATE")
    private Departamento departamento;

    public Long getId() {
        return id;
    }

    public String getCiudadPrincipal() {
        return ciudadPrincipal;
    }

    public void setCiudadPrincipal(String ciudadPrincipal) {
        this.ciudadPrincipal = ciudadPrincipal;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombreCiudad) {
        this.nombre = nombreCiudad;
    }

    

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Ciudad)) {
            return false;
        }
        Ciudad other = (Ciudad) object;
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

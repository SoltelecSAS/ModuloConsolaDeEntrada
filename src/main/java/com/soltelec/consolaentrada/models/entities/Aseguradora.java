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
@Table(name = "aseguradoras")
public class Aseguradora implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "INSURING")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Codigo_aseguradora")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "Nombre_aseguradora")
    private String nombre;
    @OneToMany(mappedBy = "aseguradora",cascade = CascadeType.MERGE)
    private List<HojaPruebas> lstHojaPrueba;
   
    public Aseguradora() {
    }

    public Aseguradora(Integer id) {
        this.id = id;
    }

    public Aseguradora(Integer id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer insuring) {
        this.id = insuring;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigoaseguradora) {
        this.codigo = codigoaseguradora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombreaseguradora) {
        this.nombre = nombreaseguradora;
    }

    public List<HojaPruebas> getLstHojaPrueba() {
        return lstHojaPrueba;
    }

    public void setLstHojaPrueba(List<HojaPruebas> lstHojaPrueba) {
        this.lstHojaPrueba = lstHojaPrueba;
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
        if (!(object instanceof Aseguradora)) {
            return false;
        }
        Aseguradora other = (Aseguradora) object;
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

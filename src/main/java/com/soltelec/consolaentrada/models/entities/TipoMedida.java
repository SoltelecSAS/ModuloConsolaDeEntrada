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
@Table(name = "tipos_medida")
@NamedQueries({
    @NamedQuery(name = "TipoMedida.findAll", query = "SELECT t FROM TipoMedida t"),
    @NamedQuery(name = "TipoMedida.findByMeasuretype", query = "SELECT t FROM TipoMedida t WHERE t.id = :measuretype"),
    @NamedQuery(name = "TipoMedida.findByTesttype", query = "SELECT t FROM TipoMedida t WHERE t.tipoPrueba = :testtype"),
    @NamedQuery(name = "TipoMedida.findByNombremedida", query = "SELECT t FROM TipoMedida t WHERE t.nombre = :nombremedida"),
    @NamedQuery(name = "TipoMedida.findByDescripcionmedida", query = "SELECT t FROM TipoMedida t WHERE t.descripcion = :descripcionmedida"),
    @NamedQuery(name = "TipoMedida.findByUnidad", query = "SELECT t FROM TipoMedida t WHERE t.unidad = :unidad")})
public class TipoMedida implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "MEASURETYPE")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "TESTTYPE")
    private int tipoPrueba;
    @Basic(optional = false)
    @Column(name = "Nombre_medida")
    private String nombre;
    @Column(name = "Descripcion_medida")
    private String descripcion;
    @Column(name = "Unidad")
    private String unidad;
    @OneToMany(mappedBy = "tiposmedida")
    private List<Permisibles> permisiblesList;

    public TipoMedida() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer measuretype) {
        this.id = measuretype;
    }

    public int getTipoPrueba() {
        return tipoPrueba;
    }

    public void setTipoPrueba(int testtype) {
        this.tipoPrueba = testtype;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombremedida) {
        this.nombre = nombremedida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcionmedida) {
        this.descripcion = descripcionmedida;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
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
        if (!(object instanceof TipoMedida)) {
            return false;
        }
        TipoMedida other = (TipoMedida) object;
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

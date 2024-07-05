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
@Table(name = "tipos_gasolina")
@NamedQueries({
    @NamedQuery(name = "TipoGasolina.findAll", query = "SELECT t FROM TipoGasolina t"),
    @NamedQuery(name = "TipoGasolina.findByFueltype", query = "SELECT t FROM TipoGasolina t WHERE t.id = :fueltype"),
    @NamedQuery(name = "TipoGasolina.findByNombregasolina", query = "SELECT t FROM TipoGasolina t WHERE t.nombre = :nombregasolina")})
public class TipoGasolina implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "FUELTYPE")
    private Integer id;
    @Column(name = "Nombre_gasolina")
    private String nombre;
   
    public TipoGasolina() {
    }

    public TipoGasolina(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer fueltype) {
        this.id = fueltype;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombregasolina) {
        this.nombre = nombregasolina;
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
        if (!(object instanceof TipoGasolina)) {
            return false;
        }
        TipoGasolina other = (TipoGasolina) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return nombre;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author GerenciaDesarrollo
 */
@Entity
@Table(name = "defxprueba")
@NamedQueries({
    @NamedQuery(name = "Defxprueba.findAll", query = "SELECT d FROM Defxprueba d")})
public class Defxprueba implements Serializable {


    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DefxpruebaPK defxpruebaPK;
    @Column(name = "Tipo_Defecto")
    private String tipoDefecto;
    @JoinColumn(name = "id_defecto", referencedColumnName = "CARDEFAULT", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Defectos defectos;
    @JoinColumn(name = "id_prueba", referencedColumnName = "Id_Pruebas", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Prueba pruebas;
    @Column(name = "tercer_estado")
    private String tercerEstado;

    public Defxprueba() {
    }

    public Defxprueba(DefxpruebaPK defxpruebaPK) {
        this.defxpruebaPK = defxpruebaPK;
    }

    public Defxprueba(int idDefecto, int idPrueba) {
        this.defxpruebaPK = new DefxpruebaPK(idDefecto, idPrueba);
    }

    public DefxpruebaPK getDefxpruebaPK() {
        return defxpruebaPK;
    }

    public void setDefxpruebaPK(DefxpruebaPK defxpruebaPK) {
        this.defxpruebaPK = defxpruebaPK;
    }

    public String getTipoDefecto() {
        return tipoDefecto;
    }

    public void setTipoDefecto(String tipoDefecto) {
        this.tipoDefecto = tipoDefecto;
    }

    public Defectos getDefectos() {
        return defectos;
    }

    public void setDefectos(Defectos defectos) {
        this.defectos = defectos;
    }

    public Prueba getPruebas() {
        return pruebas;
    }

    public void setPruebas(Prueba pruebas) {
        this.pruebas = pruebas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (defxpruebaPK != null ? defxpruebaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Defxprueba)) {
            return false;
        }
        Defxprueba other = (Defxprueba) object;
        if ((this.defxpruebaPK == null && other.defxpruebaPK != null) || (this.defxpruebaPK != null && !this.defxpruebaPK.equals(other.defxpruebaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.sart.core.ejb.domain.Defxprueba[ defxpruebaPK=" + defxpruebaPK + " ]";
    }

    /**
     * @return the tercerEstado
     */
    public String getTercerEstado() {
        return tercerEstado;
    }

    /**
     * @param tercerEstado the tercerEstado to set
     */
    public void setTercerEstado(String tercerEstado) {
        this.tercerEstado = tercerEstado;
    }

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.entities;

import java.io.Serializable;
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
@Table(name = "grupos_sub_grupos")
@NamedQueries({
    @NamedQuery(name = "GruposSubGrupos.findAll", query = "SELECT g FROM GruposSubGrupos g")})
public class GruposSubGrupos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GruposSubGruposPK gruposSubGruposPK;
    @JoinColumn(name = "DEFGROUP", referencedColumnName = "DEFGROUP", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Grupo grupos;
    @JoinColumn(name = "SCDEFGROUPSUB", referencedColumnName = "SCDEFGROUPSUB", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private SubGrupo subGrupos;
    @JoinColumn(name = "CARTYPE", referencedColumnName = "CARTYPE", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TipoVehiculo tipoVehiculo;

    public GruposSubGrupos() {
    }

    public GruposSubGrupos(GruposSubGruposPK gruposSubGruposPK) {
        this.gruposSubGruposPK = gruposSubGruposPK;
    }

    public GruposSubGrupos(int defgroup, int scdefgroupsub, int cartype) {
        this.gruposSubGruposPK = new GruposSubGruposPK(defgroup, scdefgroupsub, cartype);
    }

    public GruposSubGruposPK getGruposSubGruposPK() {
        return gruposSubGruposPK;
    }

    public void setGruposSubGruposPK(GruposSubGruposPK gruposSubGruposPK) {
        this.gruposSubGruposPK = gruposSubGruposPK;
    }

    public Grupo getGrupos() {
        return grupos;
    }

    public void setGrupos(Grupo grupos) {
        this.grupos = grupos;
    }

    public SubGrupo getSubGrupos() {
        return subGrupos;
    }

    public void setSubGrupos(SubGrupo subGrupos) {
        this.subGrupos = subGrupos;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gruposSubGruposPK != null ? gruposSubGruposPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GruposSubGrupos)) {
            return false;
        }
        GruposSubGrupos other = (GruposSubGrupos) object;
        if ((this.gruposSubGruposPK == null && other.gruposSubGruposPK != null) || (this.gruposSubGruposPK != null && !this.gruposSubGruposPK.equals(other.gruposSubGruposPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.sart.core.ejb.domain.GruposSubGrupos[ gruposSubGruposPK=" + gruposSubGruposPK + " ]";
    }
    
}

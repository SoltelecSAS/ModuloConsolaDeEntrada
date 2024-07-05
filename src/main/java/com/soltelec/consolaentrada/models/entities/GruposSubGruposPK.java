/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Dany
 */
@Embeddable
public class GruposSubGruposPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "DEFGROUP")
    private int defgroup;
    @Basic(optional = false)
    @Column(name = "SCDEFGROUPSUB")
    private int scdefgroupsub;
    @Basic(optional = false)
    @Column(name = "CARTYPE")
    private int cartype;

    public GruposSubGruposPK() {
    }

    public GruposSubGruposPK(int defgroup, int scdefgroupsub, int cartype) {
        this.defgroup = defgroup;
        this.scdefgroupsub = scdefgroupsub;
        this.cartype = cartype;
    }

    public int getDefgroup() {
        return defgroup;
    }

    public void setDefgroup(int defgroup) {
        this.defgroup = defgroup;
    }

    public int getScdefgroupsub() {
        return scdefgroupsub;
    }

    public void setScdefgroupsub(int scdefgroupsub) {
        this.scdefgroupsub = scdefgroupsub;
    }

    public int getCartype() {
        return cartype;
    }

    public void setCartype(int cartype) {
        this.cartype = cartype;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) defgroup;
        hash += (int) scdefgroupsub;
        hash += (int) cartype;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GruposSubGruposPK)) {
            return false;
        }
        GruposSubGruposPK other = (GruposSubGruposPK) object;
        if (this.defgroup != other.defgroup) {
            return false;
        }
        if (this.scdefgroupsub != other.scdefgroupsub) {
            return false;
        }
        if (this.cartype != other.cartype) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.sart.core.ejb.domain.GruposSubGruposPK[ defgroup=" + defgroup + ", scdefgroupsub=" + scdefgroupsub + ", cartype=" + cartype + " ]";
    }
    
}

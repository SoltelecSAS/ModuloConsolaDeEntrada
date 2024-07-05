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
 * @author GerenciaDesarrollo
 */
@Embeddable
public class DefxplacaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_vehiculo_for")
    private int idVehiculoFor;
    @Basic(optional = false)
    @Column(name = "id_defecto")
    private int idDefecto;
    @Basic(optional = false)
    @Column(name = "Id_hojaprueba_for")
    private int idhojapruebafor;

    public DefxplacaPK() {
    }

    public DefxplacaPK(int idVehiculoFor, int idDefecto, int idhojapruebafor) {
        this.idVehiculoFor = idVehiculoFor;
        this.idDefecto = idDefecto;
        this.idhojapruebafor = idhojapruebafor;
    }

    public int getIdVehiculoFor() {
        return idVehiculoFor;
    }

    public void setIdVehiculoFor(int idVehiculoFor) {
        this.idVehiculoFor = idVehiculoFor;
    }

    public int getIdDefecto() {
        return idDefecto;
    }

    public void setIdDefecto(int idDefecto) {
        this.idDefecto = idDefecto;
    }

    public int getIdhojapruebafor() {
        return idhojapruebafor;
    }

    public void setIdhojapruebafor(int idhojapruebafor) {
        this.idhojapruebafor = idhojapruebafor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idVehiculoFor;
        hash += (int) idDefecto;
        hash += (int) idhojapruebafor;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DefxplacaPK)) {
            return false;
        }
        DefxplacaPK other = (DefxplacaPK) object;
        if (this.idVehiculoFor != other.idVehiculoFor) {
            return false;
        }
        if (this.idDefecto != other.idDefecto) {
            return false;
        }
        if (this.idhojapruebafor != other.idhojapruebafor) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.sart.core.ejb.domain.DefxplacaPK[ idVehiculoFor=" + idVehiculoFor + ", idDefecto=" + idDefecto + ", idhojapruebafor=" + idhojapruebafor + " ]";
    }
    
}

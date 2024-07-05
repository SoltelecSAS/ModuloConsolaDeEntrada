/*
 * To change this template, choose Tools | Templates
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
public class DefxpruebaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_defecto")
    private int idDefecto;
    @Basic(optional = false)
    @Column(name = "id_prueba")
    private int idPrueba;
    

    public DefxpruebaPK() {
    }

    
    public DefxpruebaPK(int idDefecto, int idPrueba) {
        this.idDefecto = idDefecto;
        this.idPrueba = idPrueba;         
    }

    public int getIdDefecto() {
        return idDefecto;
    }

    public void setIdDefecto(int idDefecto) {
        this.idDefecto = idDefecto;
    }

    public int getIdPrueba() {
        return idPrueba;
    }

    public void setIdPrueba(int idPrueba) {
        this.idPrueba = idPrueba;
    }   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idDefecto;
        hash += (int) idPrueba;       
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DefxpruebaPK)) {
            return false;
        }
        DefxpruebaPK other = (DefxpruebaPK) object;
        if (this.idDefecto != other.idDefecto) {
            return false;
        }
        if (this.idPrueba != other.idPrueba) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.model.DefxpruebaPK[idDefecto=" + idDefecto + ", idPrueba=" + idPrueba + "]";
    }

}

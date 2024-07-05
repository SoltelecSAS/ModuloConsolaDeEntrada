/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 *
 * @author Usuario
 */
@Embeddable
public class DefectoxmedidaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "CARDEFAULT")
    private int cardefault;
    @Basic(optional = false)
    @Column(name = "MEASURE")
    private int measure;

    public DefectoxmedidaPK() {
    }

    public DefectoxmedidaPK(int cardefault, int measure) {
        this.cardefault = cardefault;
        this.measure = measure;
    }

    public int getCardefault() {
        return cardefault;
    }

    public void setCardefault(int cardefault) {
        this.cardefault = cardefault;
    }

    public int getMeasure() {
        return measure;
    }

    public void setMeasure(int measure) {
        this.measure = measure;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cardefault;
        hash += (int) measure;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DefectoxmedidaPK)) {
            return false;
        }
        DefectoxmedidaPK other = (DefectoxmedidaPK) object;
        if (this.cardefault != other.cardefault) {
            return false;
        }
        if (this.measure != other.measure) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.soltelec.persistencia.DefectoxmedidaPK[cardefault=" + cardefault + ", measure=" + measure + "]";
    }

}

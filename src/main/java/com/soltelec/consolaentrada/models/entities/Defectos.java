/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @GerenciaDesarrollo
 */
@Entity
@Table(name = "defectos")
@NamedQueries({
    @NamedQuery(name = "Defectos.findAll", query = "SELECT d FROM Defectos d")})
public class Defectos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CARDEFAULT")
    private Integer cardefault;
    @Column(name = "Nombre_problema")
    private String nombreproblema;
    @Basic(optional = false)
    @Column(name = "Tipo_defecto")
    private String tipodefecto;
   @Basic(optional = false)
    @Column(name = "codigo_Resolucion")
    private String codigoResolucion;
    
    
    @Basic(optional = false)
    @Column(name = "Codigo_Super")
    private String codigoSuperintendencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cardefault")
    private List<Permisibles> permisiblesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "defectos")
    private List<Defxplaca> defxplacaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "defectos")
    private List<Defxprueba> defxpruebaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "defectos")
    private List<Defectoxmedida> defectoxmedidaList;
     @JoinColumn(name = "DEFGROUPSSUB", referencedColumnName = "SCDEFGROUPSUB")
    @ManyToOne(optional = false)
    private SubGrupo subGrupo;

    public Defectos() {
    }

    public Defectos(Integer cardefault) {
        this.cardefault = cardefault;
    }

    public Defectos(Integer cardefault, String tipodefecto) {
        this.cardefault = cardefault;
        this.tipodefecto = tipodefecto;
    }

    public Integer getCardefault() {
        return cardefault;
    }

    public void setCardefault(Integer cardefault) {
        this.cardefault = cardefault;
    }

    public String getNombreproblema() {
        return nombreproblema;
    }

    public void setNombreproblema(String nombreproblema) {
        this.nombreproblema = nombreproblema;
    }

    public String getTipodefecto() {
        return tipodefecto;
    }

    public void setTipodefecto(String tipodefecto) {
        this.tipodefecto = tipodefecto;
    }

  
    public SubGrupo getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(SubGrupo subGrupo) {
        this.subGrupo = subGrupo;
    }

    
    public String getCodigoSuperintendencia() {
        return codigoSuperintendencia;
    }

    public void setCodigoSuperintendencia(String codigoSuperintendencia) {
        this.codigoSuperintendencia = codigoSuperintendencia;
    }

    public List<Permisibles> getPermisiblesList() {
        return permisiblesList;
    }

    public void setPermisiblesList(List<Permisibles> permisiblesList) {
        this.permisiblesList = permisiblesList;
    }

    public List<Defxplaca> getDefxplacaList() {
        return defxplacaList;
    }

    public void setDefxplacaList(List<Defxplaca> defxplacaList) {
        this.defxplacaList = defxplacaList;
    }

    public List<Defxprueba> getDefxpruebaList() {
        return defxpruebaList;
    }

    public void setDefxpruebaList(List<Defxprueba> defxpruebaList) {
        this.defxpruebaList = defxpruebaList;
    }

    public List<Defectoxmedida> getDefectoxmedidaList() {
        return defectoxmedidaList;
    }

    public void setDefectoxmedidaList(List<Defectoxmedida> defectoxmedidaList) {
        this.defectoxmedidaList = defectoxmedidaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cardefault != null ? cardefault.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Defectos)) {
            return false;
        }
        Defectos other = (Defectos) object;
        if ((this.cardefault == null && other.cardefault != null) || (this.cardefault != null && !this.cardefault.equals(other.cardefault))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.sart.core.ejb.domain.Defectos[ cardefault=" + cardefault + " ]";
    }

    /**
     * @return the codigoResolucion
     */
    public String getCodigoResolucion() {
        return codigoResolucion;
    }

    /**
     * @param codigoResolucion the codigoResolucion to set
     */
    public void setCodigoResolucion(String codigoResolucion) {
        this.codigoResolucion = codigoResolucion;
    }

}

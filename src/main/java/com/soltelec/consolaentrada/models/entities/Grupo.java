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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author GerenciaDesarrollo
 */
@Entity
@Table(name = "grupos")
@NamedQueries({
    @NamedQuery(name = "Grupos.findAll", query = "SELECT g FROM Grupo g")})
public class Grupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DEFGROUP")
    private Integer defgroup;
    @Basic(optional = false)
    @Column(name = "Nombre_grupo")
    private String nombregrupo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupos")
    private List<GruposSubGrupos> gruposSubGruposList;
    @JoinTable(name = "ttpxdgp", joinColumns = {
        @JoinColumn(name = "DEFGROUP", referencedColumnName = "DEFGROUP")}, inverseJoinColumns = {
        @JoinColumn(name = "TESTTYPE", referencedColumnName = "TESTTYPE")})
    @ManyToMany
    private List<TipoPrueba> tipoPruebaList;
    public Grupo() {
    }

    public Grupo(Integer defgroup) {
        this.defgroup = defgroup;
    }

    public Grupo(Integer defgroup, String nombregrupo) {
        this.defgroup = defgroup;
        this.nombregrupo = nombregrupo;
    }

    public Integer getDefgroup() {
        return defgroup;
    }

    public void setDefgroup(Integer defgroup) {
        this.defgroup = defgroup;
    }

    public String getNombregrupo() {
        return nombregrupo;
    }

    public void setNombregrupo(String nombregrupo) {
        this.nombregrupo = nombregrupo;
    }

    public List<GruposSubGrupos> getGruposSubGruposList() {
        return gruposSubGruposList;
    }

    public void setGruposSubGruposList(List<GruposSubGrupos> gruposSubGruposList) {
        this.gruposSubGruposList = gruposSubGruposList;
    }

    public List<TipoPrueba> getTipoPruebaList() {
        return tipoPruebaList;
    }

    public void setTipoPruebaList(List<TipoPrueba> tipoPruebaList) {
        this.tipoPruebaList = tipoPruebaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (defgroup != null ? defgroup.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.defgroup == null && other.defgroup != null) || (this.defgroup != null && !this.defgroup.equals(other.defgroup))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.sart.core.ejb.domain.Grupos[ defgroup=" + defgroup + " ]";
    }
    
}

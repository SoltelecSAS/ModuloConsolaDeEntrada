/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Dany
 */
@Entity
@Table(name = "entities")
@NamedQueries({
    @NamedQuery(name = "Entities.findAll", query = "SELECT e FROM Entities e")})
public class Entities implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ENTITY")
    private Integer entity;
    @Basic(optional = false)
    @Column(name = "ENTNAME")
    private String entname;
    @Column(name = "ENTDES")
    private String entdes;
    @Basic(optional = false)
    @Column(name = "CATEGORY")
    private String category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
    private List<Permissions> permissionsList;

    public Entities() {
    }

    public Entities(Integer entity) {
        this.entity = entity;
    }

    public Entities(Integer entity, String entname, String category) {
        this.entity = entity;
        this.entname = entname;
        this.category = category;
    }

    public Integer getEntity() {
        return entity;
    }

    public void setEntity(Integer entity) {
        this.entity = entity;
    }

    public String getEntname() {
        return entname;
    }

    public void setEntname(String entname) {
        this.entname = entname;
    }

    public String getEntdes() {
        return entdes;
    }

    public void setEntdes(String entdes) {
        this.entdes = entdes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Permissions> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(List<Permissions> permissionsList) {
        this.permissionsList = permissionsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (entity != null ? entity.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entities)) {
            return false;
        }
        Entities other = (Entities) object;
        if ((this.entity == null && other.entity != null) || (this.entity != null && !this.entity.equals(other.entity))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.sart.core.ejb.domain.Entities[ entity=" + entity + " ]";
    }
    
}

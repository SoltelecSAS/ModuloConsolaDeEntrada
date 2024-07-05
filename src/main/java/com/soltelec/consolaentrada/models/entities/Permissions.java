/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "permissions")
@NamedQueries({
    @NamedQuery(name = "Permissions.findAll", query = "SELECT p FROM Permissions p")})
public class Permissions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "PERMISSION")
    private Integer permission;
    @Column(name = "READACCESS")
    private String readaccess;
    @Column(name = "UPDATEACCESS")
    private String updateaccess;
    @Column(name = "CREATEACCESS")
    private String createaccess;
    @Column(name = "EXECUTEACCESS")
    private String executeaccess;
    @Column(name = "DELETEACCESS")
    private String deleteaccess;
    @Column(name = "WRITEACCESS")
    private String writeaccess;
    @JoinColumn(name = "ENTITY", referencedColumnName = "ENTITY")
    @ManyToOne(optional = false)
    private Entities entity;
    @JoinColumn(name = "GEUSER", referencedColumnName = "GEUSER")
    @ManyToOne(optional = false)
    private Usuario geuser;

    public Permissions() {
    }

    public Permissions(Integer permission) {
        this.permission = permission;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public String getReadaccess() {
        return readaccess;
    }

    public void setReadaccess(String readaccess) {
        this.readaccess = readaccess;
    }

    public String getUpdateaccess() {
        return updateaccess;
    }

    public void setUpdateaccess(String updateaccess) {
        this.updateaccess = updateaccess;
    }

    public String getCreateaccess() {
        return createaccess;
    }

    public void setCreateaccess(String createaccess) {
        this.createaccess = createaccess;
    }

    public String getExecuteaccess() {
        return executeaccess;
    }

    public void setExecuteaccess(String executeaccess) {
        this.executeaccess = executeaccess;
    }

    public String getDeleteaccess() {
        return deleteaccess;
    }

    public void setDeleteaccess(String deleteaccess) {
        this.deleteaccess = deleteaccess;
    }

    public String getWriteaccess() {
        return writeaccess;
    }

    public void setWriteaccess(String writeaccess) {
        this.writeaccess = writeaccess;
    }

    public Entities getEntity() {
        return entity;
    }

    public void setEntity(Entities entity) {
        this.entity = entity;
    }

    public Usuario getGeuser() {
        return geuser;
    }

    public void setGeuser(Usuario geuser) {
        this.geuser = geuser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permission != null ? permission.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permissions)) {
            return false;
        }
        Permissions other = (Permissions) object;
        if ((this.permission == null && other.permission != null) || (this.permission != null && !this.permission.equals(other.permission))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.sart.core.ejb.domain.Permissions[ permission=" + permission + " ]";
    }
    
}

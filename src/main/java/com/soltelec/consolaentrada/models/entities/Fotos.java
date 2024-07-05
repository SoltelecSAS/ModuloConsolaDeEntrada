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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author GerenciaDesarrollo
 */
@Entity
@Table(name = "fotos")
@NamedQueries({
    @NamedQuery(name = "Fotos.findAll", query = "SELECT f FROM Fotos f")})
public class Fotos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_fotos")
    private Integer idfotos;
    @Lob
    @Column(name = "Foto1")
    private byte[] foto1;
    @Lob
    @Column(name = "Foto2")
    private byte[] foto2;
    @Column(name = "numeroRevision")
    private Integer numeroRevision;
    @JoinColumn(name = "id_hoja_pruebas_for", referencedColumnName = "TESTSHEET")
    @ManyToOne(optional = false)
    private HojaPruebas idHojaPruebasFor;

    public Fotos() {
    }

    public Fotos(Integer idfotos) {
        this.idfotos = idfotos;
    }

    public Integer getIdfotos() {
        return idfotos;
    }

    public void setIdfotos(Integer idfotos) {
        this.idfotos = idfotos;
    }

    public byte[] getFoto1() {
        return foto1;
    }

    public void setFoto1(byte[] foto1) {
        this.foto1 = foto1;
    }

    public byte[] getFoto2() {
        return foto2;
    }

    public void setFoto2(byte[] foto2) {
        this.foto2 = foto2;
    }

    public Integer getNumeroRevision() {
        return numeroRevision;
    }

    public void setNumeroRevision(Integer numeroRevision) {
        this.numeroRevision = numeroRevision;
    }

    public HojaPruebas getIdHojaPruebasFor() {
        return idHojaPruebasFor;
    }

    public void setIdHojaPruebasFor(HojaPruebas idHojaPruebasFor) {
        this.idHojaPruebasFor = idHojaPruebasFor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idfotos != null ? idfotos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fotos)) {
            return false;
        }
        Fotos other = (Fotos) object;
        if ((this.idfotos == null && other.idfotos != null) || (this.idfotos != null && !this.idfotos.equals(other.idfotos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.sart.core.ejb.domain.Fotos[ idfotos=" + idfotos + " ]";
    }
    
}

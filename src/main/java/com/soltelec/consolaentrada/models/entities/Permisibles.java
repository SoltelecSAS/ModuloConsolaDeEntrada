/*
 * To change this template, choose Tools | Templates
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author GerenciaDesarrollo
 */
@Entity
@Table(name = "permisibles")
@NamedQueries({
    @NamedQuery(name = "Permisibles.findAll", query = "SELECT p FROM Permisibles p")})
public class Permisibles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_permisible")
    private Integer idpermisible;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Valor_minimo")
    private Float valorminimo;
    @Column(name = "Valor_maximo")
    private Float valormaximo;
    @Column(name = "NOTIN")
    private String notin;
    @Column(name = "Condicional_minimo")
    private Float condicionalminimo;
    @Column(name = "Condicional_maximo")
    private Float condicionalmaximo;
    @Column(name = "Descripcion_condicion")
    private String descripcioncondicion;
    @JoinColumn(name = "Tipos_medida_for", referencedColumnName = "MEASURETYPE")
    @ManyToOne         
    private TipoMedida tiposmedida;
    @JoinColumn(name = "CARDEFAULT", referencedColumnName = "CARDEFAULT")
    @ManyToOne(optional = false)
    private Defectos cardefault;    
    @JoinColumn(name = "CARTYPE", referencedColumnName = "CARTYPE")
    @ManyToOne(optional = false)
    private TipoVehiculo tipoVehiculo;

    public Permisibles() {
    }

    public Permisibles(Integer idpermisible) {
        this.idpermisible = idpermisible;
    }

    public Integer getIdpermisible() {
        return idpermisible;
    }

    public void setIdpermisible(Integer idpermisible) {
        this.idpermisible = idpermisible;
    }

    public Float getValorminimo() {
        return valorminimo;
    }

    public void setValorminimo(Float valorminimo) {
        this.valorminimo = valorminimo;
    }

    public Float getValormaximo() {
        return valormaximo;
    }

    public void setValormaximo(Float valormaximo) {
        this.valormaximo = valormaximo;
    }

    public String getNotin() {
        return notin;
    }

    public void setNotin(String notin) {
        this.notin = notin;
    }

    public Float getCondicionalminimo() {
        return condicionalminimo;
    }

    public void setCondicionalminimo(Float condicionalminimo) {
        this.condicionalminimo = condicionalminimo;
    }

    public Float getCondicionalmaximo() {
        return condicionalmaximo;
    }

    public void setCondicionalmaximo(Float condicionalmaximo) {
        this.condicionalmaximo = condicionalmaximo;
    }

    public String getDescripcioncondicion() {
        return descripcioncondicion;
    }

    public void setDescripcioncondicion(String descripcioncondicion) {
        this.descripcioncondicion = descripcioncondicion;
    }

    public TipoMedida getTiposmedidafor() {
        return tiposmedida;
    }

    public void setTiposmedidafor(TipoMedida tiposmedidafor) {
        this.tiposmedida = tiposmedidafor;
    }

    public Defectos getCardefault() {
        return cardefault;
    }

    public void setCardefault(Defectos cardefault) {
        this.cardefault = cardefault;
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
        hash += (idpermisible != null ? idpermisible.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permisibles)) {
            return false;
        }
        Permisibles other = (Permisibles) object;
        if ((this.idpermisible == null && other.idpermisible != null) || (this.idpermisible != null && !this.idpermisible.equals(other.idpermisible))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.soltelec.sart.core.ejb.domain.Permisibles[ idpermisible=" + idpermisible + " ]";
    }
    
}

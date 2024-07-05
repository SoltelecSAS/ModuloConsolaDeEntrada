/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
@Entity
@Table(name = "tipo_prueba")
@NamedQueries({
    @NamedQuery(name = "TipoPrueba.findAll", query = "SELECT t FROM TipoPrueba t"),
    @NamedQuery(name = "TipoPrueba.findByTesttype", query = "SELECT t FROM TipoPrueba t WHERE t.id = :testtype"),
    @NamedQuery(name = "TipoPrueba.findByNombretipoprueba", query = "SELECT t FROM TipoPrueba t WHERE t.nombre = :nombretipoprueba"),
    @NamedQuery(name = "TipoPrueba.findByDescripciontipoprueba", query = "SELECT t FROM TipoPrueba t WHERE t.descripcion = :descripciontipoprueba")})
public class TipoPrueba implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPrueba")
    private List<Calibracion> calibracionesList;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "TESTTYPE")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Nombre_tipo_prueba")
    private String nombre;    
    @Basic(optional = false)
    @Column(name = "tipo_prueba_sicov")
    private String nombreSicov;  
    @Basic(optional = false)
    @Column(name = "Cod_evento_sicov")
    private Integer CodEventoSicov;  
    @Basic(optional = false)
    @Column(name = "Descripcion_tipo_prueba")
    private String descripcion;
    @ManyToMany(mappedBy = "tipoPruebaList")
    private List<Grupo> gruposList;
    
    public TipoPrueba() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer idTipoPrueba) {
        this.id = idTipoPrueba;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String NombreTipoPrueba) {
        this.nombre = NombreTipoPrueba;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripciontipoprueba) {
        this.descripcion = descripciontipoprueba;
    }

    public Integer getCodEventoSicov() {
        return CodEventoSicov;
    }

    public void setCodEventoSicov(Integer CodEventoSicov) {
        this.CodEventoSicov = CodEventoSicov;
    }

    public List<Grupo> getGruposList() {
        return gruposList;
    }

    public void setGruposList(List<Grupo> gruposList) {
        this.gruposList = gruposList;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPrueba)) {
            return false;
        }
        TipoPrueba other = (TipoPrueba) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.soltelec.persistencia.TipoPrueba[testtype=" + id + "]";
    }

    @XmlTransient
    public List<Calibracion> getCalibracionesList() {
        return calibracionesList;
    }

    public void setCalibracionesList(List<Calibracion> calibracionesList) {
        this.calibracionesList = calibracionesList;
    }

    /**
     * @return the nombreSicov
     */
    public String getNombreSicov() {
        return nombreSicov;
    }

    /**
     * @param nombreSicov the nombreSicov to set
     */
    public void setNombreSicov(String nombreSicov) {
        this.nombreSicov = nombreSicov;
    }

}

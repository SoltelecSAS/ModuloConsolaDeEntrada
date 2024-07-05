/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SOLTELEC
 */
@Entity
@Table(name = "eventos_sicov")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventosSicov.findAll", query = "SELECT e FROM EventosSicov e"),
    @NamedQuery(name = "EventosSicov.findById", query = "SELECT e FROM EventosSicov e WHERE e.id = :id"),
    @NamedQuery(name = "EventosSicov.findByCodigoWs", query = "SELECT e FROM EventosSicov e WHERE e.codigoWs = :codigoWs"),
    @NamedQuery(name = "EventosSicov.findByNombreEvento", query = "SELECT e FROM EventosSicov e WHERE e.nombreEvento = :nombreEvento"),
    @NamedQuery(name = "EventosSicov.findByEstado", query = "SELECT e FROM EventosSicov e WHERE e.estado = :estado"),
    @NamedQuery(name = "EventosSicov.findByFecha", query = "SELECT e FROM EventosSicov e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "EventosSicov.findByPlaca", query = "SELECT e FROM EventosSicov e WHERE e.placa = :placa"),
    @NamedQuery(name = "EventosSicov.findByIdPrueba", query = "SELECT e FROM EventosSicov e WHERE e.idPrueba = :idPrueba")})
public class EventosSicov implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Column(name = "mensaje_ws")
    private String mensajeWs;
    @Column(name = "codigo_ws")
    private String codigoWs;
    @Column(name = "nombre_evento")
    private String nombreEvento;
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "placa")
    private String placa;
    @Column(name = "id_prueba")
    private Integer idPrueba;
    @JoinColumn(name = "id_hoja_prueba", referencedColumnName = "TESTSHEET")
    @ManyToOne(optional = false)
    private HojaPruebas idHojaPrueba;

    public EventosSicov() {
    }

    public EventosSicov(Integer id) {
        this.id = id;
    }

    public EventosSicov(Integer id, Date fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMensajeWs() {
        return mensajeWs;
    }

    public void setMensajeWs(String mensajeWs) {
        this.mensajeWs = mensajeWs;
    }

    public String getCodigoWs() {
        return codigoWs;
    }

    public void setCodigoWs(String codigoWs) {
        this.codigoWs = codigoWs;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Integer getIdPrueba() {
        return idPrueba;
    } 

    public void setIdPrueba(Integer idPrueba) {
        this.idPrueba = idPrueba;
    }

    public HojaPruebas getIdHojaPrueba() {
        return idHojaPrueba;
    }

    public void setIdHojaPrueba(HojaPruebas idHojaPrueba) {
        this.idHojaPrueba = idHojaPrueba;
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
        if (!(object instanceof EventosSicov)) {
            return false;
        }
        EventosSicov other = (EventosSicov) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EventosSicov[ id=" +id +"; mensaje= " + mensajeWs + "; codigo= "+ codigoWs + "; nombreEvento="+ nombreEvento + "; estado=" + estado + "; fecha="+ fecha +"; placa= " + placa + "; idPrueba= "+ idPrueba;
    }
    
}

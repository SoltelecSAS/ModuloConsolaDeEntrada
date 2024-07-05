/*
 * To change this template, choose Tools | Templates
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author GerenciaDesarrollo
 */
@Entity
@Table(name = "auditoria_sicov")
@NamedQueries({
    @NamedQuery(name = "AuditoriaSicov.findAll", query = "SELECT a FROM AuditoriaSicov a")})
public class AuditoriaSicov implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_AUDITORIA_SICOV")
    private Integer idAuditoriaSICOV;
    @Basic(optional = false)
    @Column(name = "ID_REVISION")
    private Integer idRevision;
    @Basic(optional = false)
    @Column(name = "SERIAL_EQUIPO_MEDICION")
    private String serialEquipoMedicion;
    @Basic(optional = false)
    @Column(name = "IP_EQUIPO_MEDICION")
    private String ipEquipoMedicion;
    @Basic(optional = false)
    @Column(name = "FECHA_REGISTRO_BD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistroBD;
    @Basic(optional = false)
    @Column(name = "FECHA_EVENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEvento;  
    @Basic(optional = false)
    @Column(name = "TIPO_OPERACION")
    private Integer tipoOperacion;
    @Basic(optional = false)
    @Column(name = "TIPO_EVENTO")
    private Integer tipoEvento;   
    @Basic(optional = false)
    @Column(name = "CODIGO_PROVEEDOR")
    private String codigoProveedor;    
    @Basic(optional = false)
    @Column(name = "ID_RUNT_CDA")
    private Integer idRUNTCDA;    
    @Basic(optional = false)
    @Column(name = "TRAMA")
    private String TRAMA;    
    @Basic(optional = false)
    @Column(name = "USUARIO")
    private String usuario;    
    @Basic(optional = false)
    @Column(name = "IDENTIFICACION_USUARIO")
    private String IdentificacionUsuario;    
    @Basic(optional = false)
    @Column(name = "OBSERVACION")
    private String Observacion;
    @Column(name = "PLACA")
    private String placa;
       
    public AuditoriaSicov() {
    }

    public AuditoriaSicov(Integer idAuditoriaSICOV) {
        this.idAuditoriaSICOV = idAuditoriaSICOV;
    }    

    public Integer getIdAuditoriaSICOV() {
        return idAuditoriaSICOV;
    }
     public void setIdAuditoriaSICOV(Integer idAudi) {
      this.idAuditoriaSICOV=idAudi ;
    }

    public void setIdRevision(Integer idRevision) {
        this.idRevision = idRevision;
    }

    public Integer getIdRevision() {
        return idRevision;
    }

    public void setRevision(Integer idRevision) {
        this.idRevision = idRevision;
    }

    public String getSerialEquipoMedicion() {
        return serialEquipoMedicion;
    }

    public void setSerialEquipoMedicion(String serialEquipoMedicion) {
        this.serialEquipoMedicion = serialEquipoMedicion;
    }

    public String getIpEquipoMedicion() {
        return ipEquipoMedicion;
    }

    public void setIpEquipoMedicion(String ipEquipoMedicion) {
        this.ipEquipoMedicion = ipEquipoMedicion;
    }

    public Date getFechaRegistroBD() {
        return fechaRegistroBD;
    }

    public void setFechaRegistroBD(Date fechaRegistroBD) {
        this.fechaRegistroBD = fechaRegistroBD;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public Integer getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(Integer tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public Integer getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(Integer tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(String codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public Integer getIdRUNTCDA() {
        return idRUNTCDA;
    }

    public void setIdRUNTCDA(Integer idRUNTCDA) {
        this.idRUNTCDA = idRUNTCDA;
    }

    public String getTRAMA() {
        return TRAMA;
    }

    public void setTRAMA(String TRAMA) {
        this.TRAMA = TRAMA;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIdentificacionUsuario() {
        return IdentificacionUsuario;
    }

    public void setIdentificacionUsuario(String IdentificacionUsuario) {
        this.IdentificacionUsuario = IdentificacionUsuario;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String Observacion) {
        this.Observacion = Observacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAuditoriaSICOV != null ? idAuditoriaSICOV.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuditoriaSicov)) {
            return false;
        }
        AuditoriaSicov other = (AuditoriaSicov) object;
        return (this.idAuditoriaSICOV != null || other.idAuditoriaSICOV == null) && (this.idAuditoriaSICOV == null || this.idAuditoriaSICOV.equals(other.idAuditoriaSICOV));
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    
    @Override
    public String toString() {
        return "com.soltelec.model.AuditoriaSicov[idAuditoriaSICOV=" + idAuditoriaSICOV + "]";
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.entities;

import org.eclipse.persistence.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Gerencia TIC
 */
@Entity
@Table(name = "hoja_pruebas")
@Cache(alwaysRefresh = true)
@NamedQueries({
    @NamedQuery(name = "HojaPruebas.findByFechas", query = "SELECT h FROM HojaPruebas h WHERE h.fechaIngreso BETWEEN :fechaInicial and :fechaFinal"),
    @NamedQuery(name = "HojaPruebas.findByAll", query = "SELECT h FROM HojaPruebas h")})
public class HojaPruebas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TESTSHEET")
    private Integer id;
    @Column(name = "estado")
    private String estado;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Vehiculo_for")
    private Vehiculo vehiculo;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Propietario_for")
    private Propietario propietario;
    @Column(name = "Usuario_for")
    private Integer usuario;
    @Column(name = "Hoja_activa_activeflag")
    private String activa;
    @Basic(optional = false)// se mapea la propiedad como no optional a nivel de objeto java           
    @Column(name = "ubicacion_municipio")
    private String ubicacionMunicipio;
    @Column(name = "Finalizada")
    private String finalizada;
    @Column(name = "Impreso")
    private String impreso;
    @Basic(optional = false)
    @Column(name = "Fecha_ingreso_vehiculo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "Anulado")
    private String anulado;
    @Column(name = "pin")
    private String pin;
    @Column(name = "Aprobado")
    private String aprobado;
    @Column(name = "forma_med_temp")
    private char formaMedTemperatura;
    @Basic(optional = false)
    @Column(name = "Fecha_expiracion_revision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpiracion;
    @Column(name = "nro_soat")
    private String nroIdentificacionSoat;
    @Column(name = "fecha_exp_soat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpSoat;
    @Column(name = "fecha_venc_soat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencSoat;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Conductor")
    private Propietario conductor;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_aseguradora")
    private Aseguradora aseguradora;
    @Column(name = "Consecutivo_resolucion")
    private String consecutivo;
    @Column(name = "Cerrada")
    private String cerrada;
    @Basic(optional = false)
    @Column(name = "Fecha_expedicion_certificados")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpedicion;
    @Lob
    @Column(name = "Comentarios_cda")
    private String comentario;
    @Column(name = "id_fotos_for")
    private int nroPruebasRegistradas;
    @OneToMany(mappedBy = "hojaPruebas", cascade = CascadeType.ALL)
    private List<Prueba> listPruebas;
    @Column(name = "Numero_intentos")
    private int intentos;
    @Column(name = "con_preventiva")
    private int con_preventiva;
    @Column(name = "con_hoja_prueba")
    private int con_hoja_prueba;
    @Column(name = "numero_solicitud")
    private String numeroSolicitud;
    @Column(name = "consecutivo_runt")
    private String consecutivoRunt;
    @Column(name = "preventiva")
    private String preventiva;
    @Column(name = "estado_sicov")
    private String estadoSICOV;
    @OneToMany(mappedBy = "hojaPruebas", fetch = FetchType.LAZY)
    private List<Reinspeccion> reinspeccionList;
    @JoinColumn(name = "usuario_resp")
    @ManyToOne
    private Usuario responsable;
    @OneToMany(mappedBy = "hojaPruebas")
    private List<Certificado> certificados;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idHojaPruebasFor", fetch = FetchType.LAZY)
    private List<Fotos> fotosList;
    @Column(name = "kilometraje_rtm")
    private String kilometraje;
    @Column(name = "fecha_venc_gnv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimientoGnv;

    public HojaPruebas() {
        super();
        this.finalizada = "N";
        this.activa = "Y";
        this.anulado = "N";
        this.aprobado = "N";
        this.preventiva = "N";
        this.fechaIngreso = new Date();
        this.nroPruebasRegistradas = 0;
        this.intentos = 1;
    }

    public HojaPruebas(Integer testsheet) {
        this.id = testsheet;
    }

    public HojaPruebas(Integer testsheet, String finalizada, Date fechaingresovehiculo, Date fechaExpiracion, int conductor, Date fechaExpedicion, int try1) {
        this.id = testsheet;
        this.finalizada = finalizada;
        this.fechaIngreso = fechaingresovehiculo;
        this.fechaExpiracion = fechaExpiracion;
        this.fechaExpedicion = fechaExpedicion;
    }
    
    public HojaPruebas( String finalizada, Date fechaingresovehiculo, Date fechaExpiracion, int conductor, Date fechaExpedicion, int try1) {
        
        this.finalizada = finalizada;
        this.fechaIngreso = fechaingresovehiculo;
        this.fechaExpiracion = fechaExpiracion;
        this.fechaExpedicion = fechaExpedicion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer idHojaPruebas) {
        this.id = idHojaPruebas;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public String getEstadoSICOV() {
        return estadoSICOV;
    }

    public void setEstadoSICOV(String estadoSICOV) {
        this.estadoSICOV = estadoSICOV;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public String getUbicacionMunicipio() {
        return ubicacionMunicipio;
    }

    public void setUbicacionMunicipio(String ubicacionMunicipio) {
        this.ubicacionMunicipio = ubicacionMunicipio;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public String getActiva() {
        return activa;
    }

    public int getCon_preventiva() {
        return con_preventiva;
    }

    public int getCon_hoja_prueba() {
        return con_hoja_prueba;
    }

    public void setActiva(String activeflag) {
        this.activa = activeflag;
    }

    public char getFormaMedTemperatura() {
        return formaMedTemperatura;
    }

    public String getNroIdentificacionSoat() {
        return nroIdentificacionSoat;
    }

    public void setNroIdentificacionSoat(String nroIdentificacionSoat) {
        this.nroIdentificacionSoat = nroIdentificacionSoat;
    }

    public List<Fotos> getFotosList() {
        return fotosList;
    }

    public void setFotosList(List<Fotos> fotosList) {
        this.fotosList = fotosList;
    }

    public Date getFechaExpSoat() {
        return fechaExpSoat;
    }

    public void setFechaExpSoat(Date fechaExpSoat) {
        this.fechaExpSoat = fechaExpSoat;
    }

    public Date getFechaVencSoat() {
        return fechaVencSoat;
    }

    public void setFechaVencSoat(Date fechaVencSoat) {
        this.fechaVencSoat = fechaVencSoat;
    }

    public void setFormaMedTemperatura(char formaMedTemperatura) {
        this.formaMedTemperatura = formaMedTemperatura;
    }

    public String getFinalizada() {
        return finalizada;
    }

    public void setCon_preventiva(int con_preventiva) {
        this.con_preventiva = con_preventiva;
    }

    public void setCon_hoja_prueba(int con_hoja_prueba) {
        this.con_hoja_prueba = con_hoja_prueba;
    }

    public void setFinalizada(String finalizada) {
        this.finalizada = finalizada;
    }

    public String getImpreso() {
        return impreso;
    }

    public void setImpreso(String impreso) {
        this.impreso = impreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngresoVehiculo) {
        this.fechaIngreso = fechaIngresoVehiculo;
    }

    public Aseguradora getAseguradora() {
        return aseguradora;
    }

    public void setAseguradora(Aseguradora aseguradora) {
        this.aseguradora = aseguradora;
    }

    public String getAnulado() {
        return anulado;
    }

    public void setAnulado(String anulado) {
        this.anulado = anulado;
    }

    public String getAprobado() {
        return aprobado;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaexpiracionrevision) {
        this.fechaExpiracion = fechaexpiracionrevision;
    }

    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String consecutivoresolucion) {
        this.consecutivo = consecutivoresolucion;
    }

    public String getCerrada() {
        return cerrada;
    }

    public void setCerrada(String closed) {
        this.cerrada = closed;
    }

    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Date fechaexpedicioncertificados) {
        this.fechaExpedicion = fechaexpedicioncertificados;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentarioscda) {
        this.comentario = comentarioscda;
    }

    public int getNroPruebasRegistradas() {
        return nroPruebasRegistradas;
    }

    public void setNroPruebasRegistradas(int nroPruebas) {
        this.nroPruebasRegistradas = nroPruebas;
    }

    public List<Prueba> getListPruebas() {
        return listPruebas;
    }

    public void setListPruebas(List<Prueba> listPruebas) {
        this.listPruebas = listPruebas;
    }

    public Propietario getConductor() {
        return conductor;
    }

    public void setConductor(Propietario conductor) {
        this.conductor = conductor;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int numeroIntentos) {
        this.intentos = numeroIntentos;
    }

    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public String getConsecutivoRunt() {
        return consecutivoRunt;
    }

    public void setConsecutivoRunt(String consecutivoRunt) {
        this.consecutivoRunt = consecutivoRunt;
    }

    public List<Reinspeccion> getReinspeccionList() {
        return reinspeccionList;
    }

    public void setReinspeccionList(List<Reinspeccion> listaReinspecciones) {
        this.reinspeccionList = listaReinspecciones;
    }

    public String getPreventiva() {
        return preventiva;
    }

    public void setPreventiva(String preventiva) {
        this.preventiva = preventiva;
    }

    public List<Certificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<Certificado> certificados) {
        this.certificados = certificados;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario usuarioResp) {
        this.responsable = usuarioResp;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HojaPruebas)) {
            return false;
        }
        HojaPruebas other = (HojaPruebas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.soltelec.persistencia.HojaPruebas[testsheet=" + id + "]";
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the pin
     */
    public String getPin() {
        return pin;
    }

    /**
     * @param pin the pin to set
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * @return the kilometraje
     */
    public String getKilometraje()
    {
        return kilometraje;
    }

    /**
     * @param kilometraje the kilometraje to set
     */
    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }

  
    
    
    

    /**
     * @return the fechaVencimientoGnv
     */
    public Date getFechaVencimientoGnv() {
        return fechaVencimientoGnv;
    }

    /**
     * @param fechaVencimientoGnv the fechaVencimientoGnv to set
     */
    public void setFechaVencimientoGnv(Date fechaVencimientoGnv) {
        this.fechaVencimientoGnv = fechaVencimientoGnv;
    }
    
    public void fechaLimiteReinspeccion (Date fechaIngresoVehiculo){
        
    }

    

    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
@Entity
@Table(name = "pruebas")
public class Prueba implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Pruebas")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Fecha_prueba")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
     @Column(name = "Fecha_final")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinal;
    @ManyToOne()
    @JoinColumn(name = "Tipo_prueba_for")
    private TipoPrueba tipoPrueba;   
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "hoja_pruebas_for")
    private HojaPruebas hojaPruebas;
    @Column(name = "Finalizada")
    private String finalizada;
    @Column(name = "Aprobada")
    private String aprobado;
    @Column(name = "Abortada")
    private String abortado;
    @Column(name = "Autorizada")
    private String autorizada;
    @Basic(optional = false)
    @Column(name = "Fecha_aborto")   
    private String fechaAborto;
    @Column(name = "Pista")
    private Short pista;
    @Column(name = "Comentario_aborto")
    private String comentario;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "serialEquipo")
    private String serialEquipo;
    @JoinColumn(name = "id_tipo_aborto", referencedColumnName = "id")
    @ManyToOne
    private TipoAborto idTipoAborto;
    @JoinColumn(name = "usuario_for", referencedColumnName = "GEUSER")
    @ManyToOne
    private Usuario usuarioFor;
     @OneToMany(cascade = CascadeType.ALL, mappedBy = "pruebas")
    private List<Defxprueba> defxpruebaList;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "prueba", fetch = FetchType.LAZY)
    private List<Medida> medidaList;

    public Prueba() {
        this.abortado = "N";
        this.aprobado = "N";
        this.finalizada = "N";
        this.autorizada = "N";
        this.usuarioFor= new Usuario();
    }

    public Prueba(Integer id) {
        this.id = id;
    }

    public Prueba(Integer id, Date fecha, int tipopruebafor, int vehiculofor, int propietariofor, Usuario usuario, HojaPruebas hojaPruebas, String fechaAborto) {
        this.id = id;
        this.fecha = fecha;
        this.usuarioFor = usuario;
        this.hojaPruebas = hojaPruebas;
        this.fechaAborto = fechaAborto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer idPrueba) {
        this.id = idPrueba;
    }

    public Date getFecha() {
        return fecha;
    }

    public TipoAborto getIdTipoAborto() {
        return idTipoAborto;
    }

    public void setIdTipoAborto(TipoAborto idTipoAborto) {
        this.idTipoAborto = idTipoAborto;
    }

    public Usuario getUsuarioFor() {
        return usuarioFor;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public void setUsuarioFor(Usuario usuarioFor) {
        this.usuarioFor = usuarioFor;
    }

    public void setFecha(Date fechaprueba) {
        this.fecha = fechaprueba;
    }

    public HojaPruebas getHojaPruebas() {
        return hojaPruebas;
    }

    public void setHojaPruebas(HojaPruebas hojaPruebaFor) {
        this.hojaPruebas = hojaPruebaFor;
    }

    

    public List<Defxprueba> getDefxpruebaList() {
        return defxpruebaList;
    }

    public void setDefxpruebaList(List<Defxprueba> defxpruebaList) {
        this.defxpruebaList = defxpruebaList;
    }

    public TipoPrueba getTipoPrueba() {
        return tipoPrueba;
    }

    public void setTipoPrueba(TipoPrueba tipoPrueba) {
        this.tipoPrueba = tipoPrueba;
    }

    public String getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(String finalizada) {
        this.finalizada = finalizada;
    }

    public String getAprobado() {
        return aprobado;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getAbortado() {
        return abortado;
    }

    public void setAbortado(String abortado) {
        this.abortado = abortado;
    }

    public String getFechaAborto() {
        return fechaAborto;
    }

    public void setFechaAborto(String fechaaborto) {
        this.fechaAborto = fechaaborto;
    }

    public Short getPista() {
        return pista;
    }

    public void setPista(Short pista) {
        this.pista = pista;
    }

    public String getAutorizada() {
        return autorizada;
    }

    public void setAutorizada(String autorizada) {
        this.autorizada = autorizada;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentariodeaborto) {
        this.comentario = comentariodeaborto;
    }

    public List<Medida> getMedidaList() {
        return medidaList;
    }

    public void setMedidaList(List<Medida> medidasList) {
        this.medidaList = medidasList;
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
        if (!(object instanceof Prueba)) {
            return false;
        }
        Prueba other = (Prueba) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "org.soltelec.persistencia.Prueba[idPrueba=" + id + "]";
    }

    /**
     * @return the serialEquipo
     */
    public String getSerialEquipo() {
        return serialEquipo;
    }

    /**
     * @param serialEquipo the serialEquipo to set
     */
    public void setSerialEquipo(String serialEquipo) {
        this.serialEquipo = serialEquipo;
    }

}

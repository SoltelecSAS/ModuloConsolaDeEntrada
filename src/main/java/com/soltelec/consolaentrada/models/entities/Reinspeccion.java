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
@Table(name="reinspecciones")
@NamedQueries({ @NamedQuery(name="Reinspeccion.findAll", query="SELECT r FROM Reinspeccion r"),
                @NamedQuery(name="Reinspeccion.findByHojaPrueba",query = "SELECT r FROM Reinspeccion r WHERE r.hojaPruebas =:hojaPruebas" ),
                @NamedQuery(name = "Reinspeccion.findByFechaSiguiente", query = "SELECT r FROM Reinspeccion r WHERE r.fechaSiguiente between :fechainicial and :fechafinal")
})

public class Reinspeccion implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reinspeccion")
    private int id;
    
    @Basic(optional = false)
    @Column(name="aprobada")
    private String aprobada;
    
    @Basic(optional = false)
    @Column(name="consecutivo_runt")
    private String consecutivoRunt;
    
    
    @Basic(optional = false)
    @Column(name="fecha_anterior")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnterior;
    
    
    @Basic(optional = false)
    @Column(name="intento")
    private int intento;
    
    @Column(name="comentarios")
    private String comentario;
    
    @ManyToOne
    @JoinColumn(name="hoja_pruebas")
    private HojaPruebas hojaPruebas;
    
    @Basic(optional = false)
    @Column(name="fecha_siguiente")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSiguiente;
    
    @OneToMany(fetch= FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name="reinspxprueba",
            joinColumns=@JoinColumn(name="id_reinspeccion"),
            inverseJoinColumns=@JoinColumn(name="id_prueba_for"))
    private List<Prueba> pruebaList;
    

    public int getId() {
        return id;
    }

    public void setId(int idReinspeccion) {
        this.id = idReinspeccion;
    }

    public String getAprobada() {
        return aprobada;
    }

    public void setAprobada(String aprobada) {
        this.aprobada = aprobada;
    }

    public String getConsecutivoRunt() {
        return consecutivoRunt;
    }

    public void setConsecutivoRunt(String consecutivoRunt) {
        this.consecutivoRunt = consecutivoRunt;
    }

    public Date getFechaAnterior() {
        return fechaAnterior;
    }

    public void setFechaAnterior(Date fechaAnterior) {
        this.fechaAnterior = fechaAnterior;
    }

    public int getIntento() {
        return intento;
    }

    public void setIntento(int numeroIntento) {
        this.intento = numeroIntento;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentarios) {
        this.comentario = comentarios;
    }

    public HojaPruebas getHojaPruebas() {
        return hojaPruebas;
    }

    public void setHojaPruebas(HojaPruebas hojaPruebas) {
        this.hojaPruebas = hojaPruebas;
    }

    public Date getFechaSiguiente() {
        return fechaSiguiente;
    }

    public void setFechaSiguiente(Date fechaSiguiente) {
        this.fechaSiguiente = fechaSiguiente;
    }

    public List<Prueba> getPruebaList() {
        return pruebaList;
    }

    public void setPruebaList(List<Prueba> listaPruebas) {
        this.pruebaList = listaPruebas;
    }    
    
}

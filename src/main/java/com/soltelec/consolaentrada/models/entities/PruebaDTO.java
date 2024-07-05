/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author Usuario
 */
@Entity
public class PruebaDTO implements Serializable, Comparator<PruebaDTO> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Pruebas")
    private Integer id;
    @Column(name = "Tipo_prueba_for")
    private Integer tipoPrueba;
    @Column(name = "Finalizada")
    private String finalizada;
    @Column(name = "Aprobada")
    private String aprobado;
    @Column(name = "Abortado")
    private String abortado;
    @Column(name = "Comentario_aborto")
    private String comentarioAbortado;
    @Column(name = "observaciones")
    private String observaciones;

    public PruebaDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer idPrueba) {
        this.id = idPrueba;
    }

    public Integer getTipoPrueba() {
        return tipoPrueba;
    }

    public void setTipoPrueba(Integer tipoPrueba) {
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

    @Override
    public int compare(PruebaDTO o1, PruebaDTO o2) {
        return o1.getTipoPrueba().compareTo(o2.getTipoPrueba());
    }

    /**
     * @return the abortado
     */
    public String getAbortado() {
        return abortado;
    }

    /**
     * @param abortado the abortado to set
     */
    public void setAbortado(String abortado) {
        this.abortado = abortado;
    }

    /**
     * @return the comentarioAbortado
     */
    public String getComentarioAbortado() {
        return comentarioAbortado;
    }

    /**
     * @param comentarioAbortado the comentarioAbortado to set
     */
    public void setComentarioAbortado(String comentarioAbortado) {
        this.comentarioAbortado = comentarioAbortado;
    }
}

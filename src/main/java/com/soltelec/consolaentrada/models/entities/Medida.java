/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "medidas")
@NamedQueries({
    @NamedQuery(name = "Medida.findAll", query = "SELECT m FROM Medida m"),
    @NamedQuery(name = "Medida.findByMeasure", query = "SELECT m FROM Medida m WHERE m.id = :id"),
    @NamedQuery(name = "Medida.findByValormedida", query = "SELECT m FROM Medida m WHERE m.valor = :valormedida"),
    @NamedQuery(name = "Medida.findByCondicion", query = "SELECT m FROM Medida m WHERE m.condicion = :condicion")})
public class Medida implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Valor_medida")
    private Float valor;
    @Column(name = "Simult")
    private String simult;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEASURE")
    private Integer id;
    @Column(name = "Condicion")
    private String condicion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medidas")
    private List<Defectoxmedida> defectoxmedidaList;
    @JoinColumn(name = "MEASURETYPE", referencedColumnName = "MEASURETYPE")
    @ManyToOne(optional = false)
    private TipoMedida tipoMedida;
    @JoinColumn(name = "TEST", referencedColumnName = "Id_Pruebas")
    @ManyToOne(optional = false)
    private Prueba prueba;

    
    public Medida() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer measure) {
        this.id = measure;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public String getCondicion() {
        if (condicion != null)
        return " " + condicion;
        else 
            return " ";
    }
    
    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public List<Defectoxmedida> getDefectoxmedidaList() {
        return defectoxmedidaList;
    }

    public void setDefectoxmedidaList(List<Defectoxmedida> defectoxmedidaList) {
        this.defectoxmedidaList = defectoxmedidaList;
    }

    public TipoMedida getTipoMedida() {
        return tipoMedida;
    }

    public void setTipoMedida(TipoMedida tiposMedida) {
        this.tipoMedida = tiposMedida;
    }

    public Prueba getPrueba() {
        return prueba;
    }

    public void setPrueba(Prueba pruebas) {
        this.prueba = pruebas;
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
        if (!(object instanceof Medida)) {
            return false;
        }
        Medida other = (Medida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String asterisco = this.getCondicion() != null? "*" : "";        
        return this.getValor()+ ' ' + asterisco;
    }

    public Float getValormedida() {
        return valor;
    }

    public void setValormedida(Float valor) {
        this.valor = valor;
    }

    public String getSimult() {
        return simult;
    }

    public void setSimult(String simult) {
        this.simult = simult;
    }

}

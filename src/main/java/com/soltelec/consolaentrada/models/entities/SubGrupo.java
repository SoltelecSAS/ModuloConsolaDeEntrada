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
@Table(name = "sub_grupos")
@NamedQueries({
    @NamedQuery(name = "SubGrupo.findAll", query = "SELECT s FROM SubGrupo s"),
    @NamedQuery(name = "SubGrupo.findByScdefgroupsub", query = "SELECT s FROM SubGrupo s WHERE s.id = :scdefgroupsub"),
    @NamedQuery(name = "SubGrupo.findByNombresubgrupo", query = "SELECT s FROM SubGrupo s WHERE s.nombre = :nombresubgrupo")})
public class SubGrupo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCDEFGROUPSUB")
    private Integer id;
    @Column(name = "Nombre_subgrupo")
    private String nombre;
    @JoinColumn(name = "TESTTYPE", referencedColumnName = "DEFGROUP")
    @ManyToOne
    private Grupo grupo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subGrupo")
    private List<Defectos> defectosList;

    public SubGrupo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer scdefgroupsub) {
        this.id = scdefgroupsub;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombresubgrupo) {
        this.nombre = nombresubgrupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupos) {
        this.grupo = grupos;
    }

    public List<Defectos> getDefectosList() {
        return defectosList;
    }

    public void setDefectosList(List<Defectos> defectosList) {
        this.defectosList = defectosList;
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
        if (!(object instanceof SubGrupo)) {
            return false;
        }
        SubGrupo other = (SubGrupo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "org.soltelec.persistencia.SubGrupo[scdefgroupsub=" + id + "]";
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author GerenciaDesarrollo
 */
@Entity
@Table(name = "sequence")
@NamedQueries({
    @NamedQuery(name = "sequence.findAll", query = "SELECT a FROM SEQUENCE a")})
public class SEQUENCE implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id    
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SEQ_NAME")
    private String SEQNAME;
    @Basic(optional = false)
    @Column(name = "SEQ_COUNT")
    private Integer SEQCOUNT;
    @Version
    @Column(name = "version")
    private Integer version;
    
    
    public SEQUENCE() {
    }

    public SEQUENCE(String SEQNAME) {
        this.SEQNAME = SEQNAME;
    }    

    public Integer getSEQCOUNT() {
        return SEQCOUNT;
    }

    public void setSEQCOUNT(Integer SEQCOUNT) {
        this.SEQCOUNT = SEQCOUNT;
    }

    public String getSEQNAME() {
        return SEQNAME;
    }

    public void setSEQNAME(String SEQNAME) {
        this.SEQNAME = SEQNAME;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (SEQNAME != null ? SEQNAME.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SEQUENCE)) {
            return false;
        }
        SEQUENCE other = (SEQUENCE) object;
        return (this.SEQNAME != null || other.SEQNAME == null) && (this.SEQNAME == null || this.SEQNAME.equals(other.SEQNAME));
    }

    @Override
    public String toString() {
        return "com.soltelec.model.SEQNAME[SEQNAME=" + SEQNAME + "]";
    }

}

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
@NamedQueries ({
    @NamedQuery(name = "Propietarios.findByIdentificacion", query = "SELECT p FROM Propietario p WHERE p.id = :identificacion")
})
@Table(name = "propietarios")
public class Propietario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "CAROWNER")
    private Long id;
    @Column(name = "Tipo_identificacion")
    @Enumerated(EnumType.STRING)
    private TipoIdentificacion tipoIdentificacion;
    @Column(name = "Apellidos")
    private String apellidos;
    @Basic(optional = false)
    @Column(name = "Nombres")
    private String nombres;
    @Basic(optional = false)
    @Column(name = "GEUSER")
    private int usuario;
    @Basic(optional = false)
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Basic(optional = false)
    @Column(name = "Numero_telefono")
    private String telefono;
    @Column(name = "celular")
    private String celular;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "Direccion")
    private String direccion;
    @Column(name = "Numero_licencia")
    private String licencia;  
     @Column(name = "Tipo_licencia")
    private String tipolicencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "propietario",fetch= FetchType.LAZY)
    private List<Vehiculo> vehiculoList;
    @OneToMany(mappedBy = "propietario",fetch= FetchType.LAZY)
    private List<HojaPruebas> listHojaPruebas;

    @ManyToOne
    @JoinColumn(name = "CITY")
    private Ciudad ciudad;

    public Propietario() {
        usuario = 1;
        fechaRegistro = new Date();
    }

    public Propietario(Long carowner) {
        this.id = carowner;
    }

    public Propietario(Long carowner, String varcharBorrar, String nombres, int geuser, Date fechaRegistro, String numerotelefono, String direccion) {
        this.id = carowner;
        
        this.nombres = nombres;
        this.usuario = geuser;
        this.fechaRegistro = fechaRegistro;
        this.telefono = numerotelefono;
        this.direccion = direccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long identificacion) {
        this.id = identificacion;
    }

    

    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public String getTipolicencia() {
        return tipolicencia;
    }

    public void setTipolicencia(String tipolicencia) {
        this.tipolicencia = tipolicencia;
    }  

    public List<Vehiculo> getVehiculoList() {
        return vehiculoList;
    }

    public void setVehiculoList(List<Vehiculo> vehiculosList) {
        this.vehiculoList = vehiculosList;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public List<HojaPruebas> getListHojaPruebas() {
        return listHojaPruebas;
    }

    public void setListHojaPruebas(List<HojaPruebas> listHojaPruebas) {
        this.listHojaPruebas = listHojaPruebas;
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
        if (!(object instanceof Propietario)) {
            return false;
        }
        Propietario other = (Propietario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.soltelec.persistencia.Propietarios[carowner=" + id + "]";
    }

}

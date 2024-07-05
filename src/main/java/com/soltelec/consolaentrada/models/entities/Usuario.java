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
@Table(name = "usuarios")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByNickusuario", query = "SELECT u FROM Usuario u WHERE u.nick = :nickusuario"),
    @NamedQuery(name = "Usuario.findByGeuser", query = "SELECT u FROM Usuario u WHERE u.usuario = :geuser"),
    @NamedQuery(name = "Usuario.findByNombreusuario", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombreusuario"),
    @NamedQuery(name = "Usuario.findByEsAdministrador", query = "SELECT u FROM Usuario u WHERE u.administrador = :esAdministrador"),
    @NamedQuery(name = "Usuario.findByContrasenia", query = "SELECT u FROM Usuario u WHERE u.contrasena = :contrasenia"),
    @NamedQuery(name = "Usuario.findByFechavalidacion", query = "SELECT u FROM Usuario u WHERE u.fechaValidacion = :fechavalidacion")})
public class Usuario implements Serializable {
    @Basic(optional = false)
    @Column(name = "cedula")
    private String cedula;
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "Nick_usuario")
    private String nick;
    @Id
    @Column(name = "GEUSER")
    private Integer usuario;
    @Basic(optional = false)
    @Column(name = "Nombre_usuario")
    private String nombre;
    @Column(name = "es_administrador")
    private String administrador;
    @Column(name = "Contrasenia")
    private String contrasena;
    @Basic(optional = false)
    @Column(name = "Fecha_validacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacion; 
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "geuser")
    private List<Permissions> permissionsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responsable")
    private List<HojaPruebas> hojaPruebasList;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "usuarioFor")
    private List<Prueba> pruebasList;

    public Usuario() {
    }

    public Usuario(Integer geuser) {
        this.usuario = geuser;
    }

    public Usuario(Integer geuser, String nick, String nombreusuario, Date fechavalidacion) {
        this.usuario = geuser;
        this.nick = nick;
        this.nombre = nombreusuario;
        this.fechaValidacion = fechavalidacion;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nickusuario) {
        this.nick = nickusuario;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombreUsuario) {
        this.nombre = nombreUsuario;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String esAdministrador) {
        this.administrador = esAdministrador;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Date getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public List<Permissions> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(List<Permissions> permissionsList) {
        this.permissionsList = permissionsList;
    }

    public List<HojaPruebas> getHojaPruebasList() {
        return hojaPruebasList;
    }

    public void setHojaPruebasList(List<HojaPruebas> hojaPruebasList) {
        this.hojaPruebasList = hojaPruebasList;
    }

    public List<Prueba> getPruebasList() {
        return pruebasList;
    }

    public void setPruebasList(List<Prueba> pruebasList) {
        this.pruebasList = pruebasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuario != null ? usuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        return !((this.usuario == null && other.usuario != null) || (this.usuario != null && !this.usuario.equals(other.usuario)));
    }

    @Override
    public String toString() {
        return nombre;
    }
}

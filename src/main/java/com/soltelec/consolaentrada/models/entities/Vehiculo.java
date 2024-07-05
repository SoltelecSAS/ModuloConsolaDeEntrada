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
 * @author GerenciaDesarrollo
 */
@Entity
@Cache(alwaysRefresh = true)
@Table(name = "vehiculos")
@NamedQueries({
    @NamedQuery(name = "Vehiculo.findAll", query = "SELECT v FROM Vehiculo v"),
    @NamedQuery(name = "Vehiculo.findByCar", query = "SELECT v FROM Vehiculo v WHERE v.id = :idVehiculo"),
    @NamedQuery(name = "Vehiculo.findByCarplate", query = "SELECT v FROM Vehiculo v WHERE v.placa = :placa"),
    @NamedQuery(name = "Vehiculo.findByModelo", query = "SELECT v FROM Vehiculo v WHERE v.modelo = :modelo"),
    @NamedQuery(name = "Vehiculo.findByCinlindraje", query = "SELECT v FROM Vehiculo v WHERE v.cilindraje = :cinlindraje"),
    @NamedQuery(name = "Vehiculo.findByNumerolicencia", query = "SELECT v FROM Vehiculo v WHERE v.licencia = :numerolicencia"),
    @NamedQuery(name = "Vehiculo.findByNumeroejes", query = "SELECT v FROM Vehiculo v WHERE v.ejes = :numeroejes"),
    @NamedQuery(name = "Vehiculo.findByIndate", query = "SELECT v FROM Vehiculo v WHERE v.fecha = :indate"),
    @NamedQuery(name = "Vehiculo.findByNumeroexostos", query = "SELECT v FROM Vehiculo v WHERE v.exostos = :numeroexostos"),
    @NamedQuery(name = "Vehiculo.findByDiametro", query = "SELECT v FROM Vehiculo v WHERE v.diametro = :diametro"),
    @NamedQuery(name = "Vehiculo.findByTiemposmotor", query = "SELECT v FROM Vehiculo v WHERE v.tiemposMotor = :tiemposmotor"),
    @NamedQuery(name = "Vehiculo.findByVelocidad", query = "SELECT v FROM Vehiculo v WHERE v.velocidad = :velocidad"),
    @NamedQuery(name = "Vehiculo.findByNumeroSOAT", query = "SELECT v FROM Vehiculo v WHERE v.numeroSOAT = :numeroSOAT"),
    @NamedQuery(name = "Vehiculo.findByFechasoat", query = "SELECT v FROM Vehiculo v WHERE v.fechaSOAT = :fechasoat"),
    @NamedQuery(name = "Vehiculo.findByFechaexpsoat", query = "SELECT v FROM Vehiculo v WHERE v.fechaExpedicionSOAT = :fechaexpsoat"),
    @NamedQuery(name = "Vehiculo.findByNacionalidad", query = "SELECT v FROM Vehiculo v WHERE v.nacionalidad = :nacionalidad"),
    @NamedQuery(name = "Vehiculo.findBySpservice", query = "SELECT v FROM Vehiculo v WHERE v.servicioEspecial = :spservice"),
    @NamedQuery(name = "Vehiculo.findByNumeromotor", query = "SELECT v FROM Vehiculo v WHERE v.motor = :numeromotor"),
    @NamedQuery(name = "Vehiculo.findByVin", query = "SELECT v FROM Vehiculo v WHERE v.vin = :vin"),
    @NamedQuery(name = "Vehiculo.findByFecharegistro", query = "SELECT v FROM Vehiculo v WHERE v.fechaRegistro = :fecharegistro")})

public class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAR")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "CARPLATE")
    private String placa;
    @Basic(optional = false)
    @Column(name = "Modelo")
    private int modelo;
    @Basic(optional = false)
    @Column(name = "diseño")
    @Enumerated(EnumType.STRING)
    private Diseno diseno;
    @Column(name = "Cinlindraje")
    private Integer cilindraje;
    @Column(name = "Numero_licencia")
    private String licencia;
    @Column(name = "Numero_ejes")
    private Integer ejes;
    @Basic(optional = false)
    @Column(name = "INDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "Numero_exostos")
    private Integer exostos;
    @Column(name = "Diametro")
    private Integer diametro;
    @Column(name = "Tiempos_motor")
    private Integer tiemposMotor;
    @Column(name = "peso_bruto")
    private Integer pesoBruto;
    @Column(name = "Velocidad")
    private Integer velocidad;
    @Column(name = "Numero_SOAT")
    private String numeroSOAT;
    @Basic(optional = false)
    @Column(name = "Fecha_soat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSOAT;
    @Basic(optional = false)
    @Column(name = "Fecha_exp_soat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpedicionSOAT;
    @Column(name = "Nacionalidad")
    private String nacionalidad;
    @Column(name = "Catalizador")
    private String catalizador;
    @JoinColumn(name = "SPSERVICE", referencedColumnName = "SPSERVICE")
    @ManyToOne(optional = false)
    private ServicioEspecial servicioEspecial;
    @Column(name = "Numero_motor")
    private String motor;
    @Column(name = "VIN")
    private String vin;
    @Basic(optional = false)
    @Column(name = "Fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    /***Modificado esEnsenaza por Diego Garzon***/
    @Column(name = "esEnsenaza")
    //@Column(name = "es_ensenaza")
    private Integer esEnsenaza;
    @JoinColumn(name = "WHEEL", referencedColumnName = "WHEEL")
    @ManyToOne(optional = false)
    private Llanta llantas;
    @JoinColumn(name = "SERVICE", referencedColumnName = "SERVICE")
    @ManyToOne(optional = false)
    private Servicio servicios;
    @JoinColumn(name = "INSURING", referencedColumnName = "INSURING")
    @ManyToOne(optional = false)
    private Aseguradora aseguradora;
    @Column(name = "GEUSER")
    private Integer usuario;
    @JoinColumn(name = "FUELTYPE", referencedColumnName = "FUELTYPE")
    @ManyToOne(optional = false)
    private TipoGasolina tipoGasolina;
    @JoinColumn(name = "Color", referencedColumnName = "COLOR")
    @ManyToOne(optional = false)
    private Color color;
    @JoinColumn(name = "CLASS", referencedColumnName = "CLASS")
    @ManyToOne(optional = false)
    private ClaseVehiculo claseVehiculo;
    @JoinColumn(name = "CARTYPE", referencedColumnName = "CARTYPE")
    @ManyToOne(optional = false)
    private TipoVehiculo tipoVehiculo;
    @JoinColumn(name = "CAROWNER", referencedColumnName = "CAROWNER")
    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private Propietario propietario;
    @JoinColumn(name = "CARMARK", referencedColumnName = "CARMARK")
    @ManyToOne
    private Marca marca;
    @JoinColumn(name = "CARLINE", referencedColumnName = "CARLINE")
    @ManyToOne(optional = false)
    private LineaVehiculo lineaVehiculo;
    @OneToMany(mappedBy = "vehiculo")
    private List<HojaPruebas> hojaPruebasList;
    @JoinColumn(name = "pais", referencedColumnName = "codigo")
    @ManyToOne
    private Pais pais;
    @Column(name = "kilometraje")
    @Basic(optional = true)
    private Integer kilometraje;
    @Column(name = "numero_sillas")
    private Integer sillas;
    @Column(name = "vidrios_polarizados")
    private String vidriosPolarizados;
    @Column(name = "blindaje")
    private String blindaje;
    @Column(name = "numero_chasis")
    private String chasis;
    @Column(name = "codigo_interno")
    private String codigoInterno;
    //Ajustes sicov v2
    @Column(name = "potencia")
    private Integer potencia;
    @JoinColumn(name = "carroceria", referencedColumnName = "id")
    @ManyToOne
    private Tipocarroceria tipoCarroceria;
    @Column(name = "conversion_gnv")
    private String esConversionGnv;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_vencimiento_gnv")
    private Date fechaVencimientoGnv;
    public Vehiculo() {
        usuario = 1;
    }

    public Vehiculo(Integer car) {
        this.id = car;
        usuario = 1;

    }

    public Vehiculo(Integer car, String carplate, int modelo, Date fecha, Date fechaSOAT, Date fechaExpedicionSOAT, Date fechaRegistro) {
        this.id = car;
        this.placa = carplate;
        this.modelo = modelo;
        this.fecha = fecha;
        this.fechaSOAT = fechaSOAT;
        this.fechaExpedicionSOAT = fechaExpedicionSOAT;
        //this.spservice = spservice;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer idVehiculo) {
        this.id = idVehiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public Integer getEsEnsenaza() {
        return esEnsenaza;
    }

    public void setEsEnsenaza(Integer esEnsenaza) {
        this.esEnsenaza = esEnsenaza;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getModelo() {
        return modelo;
    }

    public void setModelo(int modelo) {
        this.modelo = modelo;
    }

    public Diseno getDiseno() {
        return diseno;
    }

    public void setDiseno(Diseno diseno) {
        this.diseno = diseno;
    }

    public Integer getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(Integer cinlindraje) {
        this.cilindraje = cinlindraje;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String numerolicencia) {
        this.licencia = numerolicencia;
    }

    public Integer getEjes() {
        return ejes;
    }

    public void setEjes(Integer numeroejes) {
        this.ejes = numeroejes;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date indate) {
        this.fecha = indate;
    }

    public Integer getExostos() {
        return exostos;
    }

    public void setExostos(Integer numeroexostos) {
        this.exostos = numeroexostos;
    }

    public Integer getDiametro() {
        return diametro;
    }

    public void setDiametro(Integer diametro) {
        this.diametro = diametro;
    }

    public Integer getTiemposMotor() {
        return tiemposMotor;
    }

    public void setTiemposMotor(Integer tiemposmotor) {
        this.tiemposMotor = tiemposmotor;
    }

    public Integer getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(Integer velocidad) {
        this.velocidad = velocidad;
    }

    public String getNumeroSOAT() {
        return numeroSOAT;
    }

    public void setNumeroSOAT(String numeroSOAT) {
        this.numeroSOAT = numeroSOAT;
    }

    public Date getFechaSOAT() {
        return fechaSOAT;
    }

    public void setFechaSOAT(Date fechasoat) {
        this.fechaSOAT = fechasoat;
    }

    public Date getFechaExpedicionSOAT() {
        return fechaExpedicionSOAT;
    }

    public void setFechaExpedicionSOAT(Date fechaexpsoat) {
        this.fechaExpedicionSOAT = fechaexpsoat;
    }

    public Integer getPesoBruto() {
        return pesoBruto;
    }

    public void setPesoBruto(Integer pesoBruto) {
        this.pesoBruto = pesoBruto;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public ServicioEspecial getServicioEspecial() {
        return servicioEspecial;
    }

    public void setServicioEspecial(ServicioEspecial spservice) {
        this.servicioEspecial = spservice;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String numeromotor) {
        this.motor = numeromotor;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fecharegistro) {
        this.fechaRegistro = fecharegistro;
    }

    public Llanta getLlantas() {
        return llantas;
    }

    public void setLlantas(Llanta llantas) {
        this.llantas = llantas;
    }

    public Servicio getServicios() {
        return servicios;
    }

    public void setServicios(Servicio servicios) {
        this.servicios = servicios;
    }

    public Aseguradora getAseguradora() {
        return aseguradora;
    }

    public void setAseguradora(Aseguradora aseguradoras) {
        this.aseguradora = aseguradoras;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer idUsuario) {
        this.usuario = idUsuario;
    }

//    public Usuarios getUsuarios() {
//        return usuarios;
//    }
//
//    public void setUsuarios(Usuarios usuarios) {
//        this.usuarios = usuarios;
//    }
    public TipoGasolina getTipoGasolina() {
        return tipoGasolina;
    }

    public void setTipoGasolina(TipoGasolina tiposGasolina) {
        this.tipoGasolina = tiposGasolina;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color colores) {
        this.color = colores;
    }

    public ClaseVehiculo getClaseVehiculo() {
        return claseVehiculo;
    }

    public void setClaseVehiculo(ClaseVehiculo clasesVehiculo) {
        this.claseVehiculo = clasesVehiculo;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marcas) {
        this.marca = marcas;
    }

    public LineaVehiculo getLineaVehiculo() {
        return lineaVehiculo;
    }

    public void setLineaVehiculo(LineaVehiculo lineasVehiculo) {
        this.lineaVehiculo = lineasVehiculo;
    }

    public List<HojaPruebas> getHojaPruebasList() {
        return hojaPruebasList;
    }

    public void setHojaPruebasList(List<HojaPruebas> hojaPruebasList) {
        this.hojaPruebasList = hojaPruebasList;
    }

    public String getCatalizador() {
        return catalizador;
    }

    public void setCatalizador(String catalizador) {
        this.catalizador = catalizador;
    }
    
    

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getBlindaje() {
        return blindaje;
    }

    public void setBlindaje(String blindaje) {
        this.blindaje = blindaje;
    }

    public Integer getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(Integer kilometraje) {
        this.kilometraje = kilometraje;
    }

    public Integer getSillas() {
        return sillas;
    }

    public void setSillas(Integer numeroSillas) {
        this.sillas = numeroSillas;
    }

    public String getVidriosPolarizados() {
        return vidriosPolarizados;
    }

    public void setVidriosPolarizados(String vidriosPolarizados) {
        this.vidriosPolarizados = vidriosPolarizados;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String numeroChasis) {
        this.chasis = numeroChasis;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
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
        if (!(object instanceof Vehiculo)) {
            return false;
        }
        Vehiculo other = (Vehiculo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.soltelec.persistencia.Vehiculo[car=" + id + "]";
    }

    /**
     * @return the potencia
     */
    public Integer getPotencia() {
        return potencia;
    }

    /**
     * @param potencia the potencia to set
     */
    public void setPotencia(Integer potencia) {
        this.potencia = potencia;
    }

    /**
     * @return the tipoCarroceria
     */
    public Tipocarroceria getTipoCarroceria() {
        return tipoCarroceria;
    }

    /**
     * @param tipoCarroceria the tipoCarroceria to set
     */
    public void setTipoCarroceria(Tipocarroceria tipoCarroceria) {
        this.tipoCarroceria = tipoCarroceria;
    }

    /**
     * @return the esConversionGnv
     */
    public String getEsConversionGnv() {
        return esConversionGnv;
    }

    /**
     * @param esConversionGnv the esConversionGnv to set
     */
    public void setEsConversionGnv(String esConversionGnv) {
        this.esConversionGnv = esConversionGnv;
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


}

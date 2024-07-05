package com.soltelec.consolaentrada.views;

import com.soltelec.consolaentrada.models.entities.LineaVehiculo;
import com.soltelec.consolaentrada.models.entities.TipoIdentificacion;
import com.soltelec.consolaentrada.models.entities.Marca;
import com.soltelec.consolaentrada.models.entities.Departamento;
import com.soltelec.consolaentrada.models.entities.TipoGasolina;
import com.soltelec.consolaentrada.models.entities.TipoLicencia;
import com.soltelec.consolaentrada.models.entities.Diseno;
import com.soltelec.consolaentrada.models.entities.Propietario;
import com.soltelec.consolaentrada.models.entities.ClaseVehiculo;
import com.soltelec.consolaentrada.models.entities.Prueba;
import com.soltelec.consolaentrada.models.entities.Vehiculo;
import com.soltelec.consolaentrada.models.entities.Aseguradora;
import com.soltelec.consolaentrada.models.entities.ServicioEspecial;
import com.soltelec.consolaentrada.models.entities.Usuario;
import com.soltelec.consolaentrada.models.entities.Reinspeccion;
import com.soltelec.consolaentrada.models.entities.Llanta;
import com.soltelec.consolaentrada.models.entities.Pais;
import com.soltelec.consolaentrada.models.entities.Servicio;
import com.soltelec.consolaentrada.models.entities.TipoVehiculo;
import com.soltelec.consolaentrada.models.entities.Ciudad;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.controllers.PaisJpaController;
import com.soltelec.consolaentrada.models.controllers.ReinspeccionJpaController;
import com.soltelec.consolaentrada.models.controllers.TipoGasolinaJpaController;
import com.soltelec.consolaentrada.models.controllers.VehiculoJpaController;
import com.soltelec.consolaentrada.models.controllers.PropietarioJpaController;
import com.soltelec.consolaentrada.models.controllers.UsuarioJpaController;
import com.soltelec.consolaentrada.models.controllers.TipoVehiculoJpaController;
import com.soltelec.consolaentrada.models.controllers.CdaJpaController;
import com.soltelec.consolaentrada.models.controllers.PruebaJpaController;
import com.soltelec.consolaentrada.models.controllers.TipoPruebaJpaController;
import com.soltelec.consolaentrada.custom.ModeloTablaHojasVer;
import com.soltelec.consolaentrada.custom.ModeloTablaPrueba;
import com.soltelec.consolaentrada.custom.MyOwnComboBoxModel;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.HojaPruebasJpaController;
import com.soltelec.consolaentrada.models.entities.Cda;
import com.soltelec.consolaentrada.models.entities.Color;
import com.soltelec.consolaentrada.models.statics.LoggedUser;
import com.soltelec.consolaentrada.utilities.Mensajes;
import com.soltelec.consolaentrada.utilities.Validaciones;
import com.soltelec.consolaentrada.models.controllers.AseguradoraJpaController;
import com.soltelec.consolaentrada.models.controllers.CiudadJpaController;
import com.soltelec.consolaentrada.models.entities.AuditoriaSicov;
import com.soltelec.consolaentrada.models.entities.RespuestaDTO;
import com.soltelec.consolaentrada.models.entities.Tipocarroceria;
import com.soltelec.consolaentrada.sicov.ci2.ClienteCi2;
import com.soltelec.consolaentrada.sicov.ci2.Pin;
import com.soltelec.consolaentrada.utilities.UtilConexion;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author Gerencia TIC
 */
public class PanelRevisiones extends javax.swing.JPanel {

    private String placaActual;
    private Vehiculo vehiculo;
    private HojaPruebas hojaPruebasActual;
    private Reinspeccion reinspeccion;
    private final List<Prueba> pruebasTemp;
    private Propietario propietarioActual;
    private Propietario conductorActual;
    private Reinspeccion reinspeccionActual;
//    private String valida_foto;
    private boolean valida_foto;

    private final VehiculoJpaController vehiculoJpa;
    private final HojaPruebasJpaController hojaPruebasJPA;
    private final PruebaJpaController pruebasJPA;
    private final PropietarioJpaController propietarioJPA;

    //Generar un modelo en el cual se pueda actualizar la informacion cuando se
    //ejecute algun evento especifico
    private MyOwnComboBoxModel modeloComboDinamico;
    private MyOwnComboBoxModel modeloCiudadC;
    private MyOwnComboBoxModel modeloCiudadP;

    private ModeloTablaHojasVer modeloHojaPruebas;
    private ModeloTablaPrueba modeloPruebas;
    private int selectedRow;
    private BufferedReader config;

    private String ctxPing = "0";
    private Cda ctxCda = null;
    public Boolean pulsoTrans = false;
    public boolean profLabFren = false;
    private int idHojaPrueba = 0;
    private String kilometrajeTemporal = "";
    private boolean kilometrajeAnulada = false;

    /**
     * Creates new form PanelRevisiones
     */
    public PanelRevisiones() {
        vehiculoJpa = new VehiculoJpaController();
        pruebasJPA = new PruebaJpaController();
        hojaPruebasJPA = new HojaPruebasJpaController();
        propietarioJPA = new PropietarioJpaController();
        this.ctxCda = new CdaJpaController().find(1);
        initComponents();
        defaultValues();
        formatearCampos();
        pruebasTemp = new ArrayList<>();
        pnlRevisiones.remove(pnlRegistroPropietario);
        pnlRevisiones.remove(pnlRegistroPruebas);
        txtPlaca.requestFocus();
        /*mostrar1 = true;
         txtCodInterno.setVisible(mostrar1);
         lblCodInterno.setVisible(mostrar1);*/
//        txtKilometraje.setText("NO FUNCIONAL");
//      txtKilometraje.setEnabled(true);
//        chckKilometrajeFuncional.setSelected(true);
    }

    /**
     * Metodo para llenar los campos del formulario de vehiculo con la variable
     * de la clase.
     */
    private void datosVehiculo() {
        sprModelo.setValue(vehiculo.getModelo());
        cmbTipo.setSelectedItem(vehiculo.getTipoVehiculo());
        cmbMarca.setSelectedItem(vehiculo.getMarca());
        cmbClase.setSelectedItem(vehiculo.getClaseVehiculo());
        cmbLinea.addItem((Object) vehiculo.getLineaVehiculo());
        cmbLinea.setSelectedItem(vehiculo.getLineaVehiculo());
        cmbServicio.setSelectedItem(vehiculo.getServicios());
        cmbServEspeciales.setSelectedItem(vehiculo.getServicioEspecial());
        txtCilindraje.setText(String.valueOf(vehiculo.getCilindraje()));
        txtPesoBruto.setText(String.valueOf(vehiculo.getPesoBruto()));
        txtPotencia.setText(String.valueOf(vehiculo.getPotencia()));
        sprTiemposMotor.setValue(vehiculo.getTiemposMotor());
        sprExostos.setValue(vehiculo.getExostos());
        sprEjes.setValue(vehiculo.getEjes());
        cmbNacionalidad.setSelectedItem(vehiculo.getNacionalidad());
        txtNumMotor.setText(vehiculo.getMotor());
        txtNumLicencia.setText(vehiculo.getLicencia());
        /// txtCarroceria.setText((vehiculo.getTipoCarroceria().getNombre() != null) ? vehiculo.getTipoCarroceria().getNombre() : "");
        txtNumSerie.setText(vehiculo.getVin());
        txtKilometraje.setText(String.valueOf(vehiculo.getKilometraje()));
//        txtKilometraje.setText(vehiculo.getKilometraje() == null ? "0" : String.valueOf(vehiculo.getKilometraje())); // Se esta tomando desde la hoja de prueba
//        sprSillas.setValue(vehiculo.getSillas());
        sprSillas.setValue(vehiculo.getSillas() == null ? 0 : vehiculo.getSillas());
        txtChasis.setText(vehiculo.getChasis());
        txtCodInterno.setText(vehiculo.getCodigoInterno());//Codigo interno (Uso para fur preventivo)
        cmbTipoLlanta.setSelectedItem(vehiculo.getLlantas());
        cmbCombustible.setSelectedItem(vehiculo.getTipoGasolina());
        cmbColor.addItem((Object) vehiculo.getColor());
        cmbColor.setSelectedItem(vehiculo.getColor());
        cmbCarroceria.addItem((Object) vehiculo.getTipoCarroceria());
        cmbCarroceria.setSelectedItem(vehiculo.getTipoCarroceria());
        cmbAseguradora.setSelectedItem(vehiculo.getAseguradora());
        cmbPais.setSelectedItem(vehiculo.getPais());
        txtNumSoat.setText(vehiculo.getNumeroSOAT());
        dprVencSoat.setDate(vehiculo.getFechaSOAT());
        dprExpSoat.setDate(vehiculo.getFechaExpedicionSOAT());
        dprFechaMatricula.setDate(vehiculo.getFechaRegistro());

        if (vehiculo.getDiseno() == Diseno.Scooter) {
            chbScooter.setSelected(true);
        } else {
            chbScooter.setSelected(false);
        }

        if (vehiculo.getEsEnsenaza() == 1) {
            apliEnsezas.setSelected(true);
        } else {
            apliEnsezas.setSelected(false);
        }

        if (vehiculo.getBlindaje().equalsIgnoreCase("Y")) {
            rdbBlindajeSi.setSelected(true);
        } else {
            rdbBlindajeNo.setSelected(true);
        }

        if (vehiculo.getVidriosPolarizados().equalsIgnoreCase("Y")) {
            rdbPolarizadosSi.setSelected(true);
        } else {
            rdbPolarizadosNo.setSelected(true);
        }

//        if (vehiculo.getCatalizador().equalsIgnoreCase("Y")) {
//      catalizadorsi.setSelected(true);
//        } else {
//            catalizadorno.setSelected(true);
//        }
        VencGnv.setDate(vehiculo.getFechaVencimientoGnv());
        if (vehiculo.getEsConversionGnv() != null) {
            switch (vehiculo.getEsConversionGnv()) {
                case "Y":
                    congnvsi.setSelected(true);
                    break;
                case "N":
                    congnvno.setSelected(true);
                    break;
                default:
                    congnvnoaplica.setSelected(true);
                    break;
            }
        } else {
            congnvnoaplica.setSelected(true);
        }
    }

    /**
     *
     * @return
     */
    private String cargarKilometraje(int idHojaPrueba) {
        try {
            Connection cn = UtilConexion.obtenerConexion();
            int idprueba = HojaPruebasJpaController.obtenerIdPruebaVisual(idHojaPrueba, cn);
            return HojaPruebasJpaController.consultarMedida(idprueba, cn);
        } catch (Exception e) {
            System.out.println("Error en el metod:cargarKilometraje()" + e);
        }
        return null;
    }

    /**
     * Metodo con el cual se llena el formulario de Propietario si el
     * propietario es null se vacian los campos
     *
     * @param propietario - Entidad de la cual se obtiene la informacion para
     * llenar
     * @param validar - Si es true se ejecuta el metodo sameDriver()
     */
    private void datosPropietario(Propietario propietario, Boolean validar) {
        propietarioActual = propietario;

        if (propietario != null) {
            cmbTipoIdentificacionP.setSelectedItem(propietario.getTipoIdentificacion());
            txtIdentificacionP.setText(propietario.getId().toString());
            txtNombresP.setText(propietario.getNombres());
            txtApellidosP.setText(propietario.getApellidos());
            txtDireccionP.setText(propietario.getDireccion());
            txtTelefonoP.setText(propietario.getTelefono());
            txtCelularP.setText(propietario.getCelular());
            txtEmailP.setText(propietario.getEmail());
            cmbDepartamentoP.setSelectedItem(propietario.getCiudad().getDepartamento());
            cmbCiudadP.setSelectedItem(propietario.getCiudad());
            cmbTipoLicenciaP.setSelectedItem(propietario.getTipolicencia());
            txtLicenciaP.setText(propietario.getLicencia());
        } else {
            int tipoIdentificacion = cmbTipoIdentificacionP.getSelectedIndex();
            String identificacion = txtIdentificacionP.getText();
            Validaciones.limpiarCampos(pnlPropietario);
            cmbTipoIdentificacionP.setSelectedIndex(tipoIdentificacion);
            txtIdentificacionP.setText(identificacion);
        }

        if (validar) {
            sameDriver(propietario, conductorActual);
        }
    }

    private void datosPropietario(Propietario propietario) {
        datosPropietario(propietario, Boolean.TRUE);
    }

    /**
     * Metodo con el cual se llena el formulario de Conductor si el conductor es
     * null se vacian los campos
     *
     * @param conductor - Entidad de la cual se obtiene la informacion para
     * llenar
     * @param validar - Si es true se ejecuta el metodo sameDriver()
     */
    private void datosConductor(Propietario conductor, Boolean validar) {

        conductorActual = conductor;

        if (conductor != null) {
            cmbTipoIdentificacionC.setSelectedItem(conductor.getTipoIdentificacion());
            txtIdentificacionC.setText(conductor.getId().toString());
            txtNombresC.setText(conductor.getNombres());
            txtApellidosC.setText(conductor.getApellidos());
            txtDireccionC.setText(conductor.getDireccion());
            txtTelefonoC.setText(conductor.getTelefono());
            txtCelularC.setText(conductor.getCelular());
            txtEmailC.setText(conductor.getEmail());
            cmbDepartamentoC.setSelectedItem(conductor.getCiudad().getDepartamento());
            cmbCiudadC.setSelectedItem(conductor.getCiudad());

            cmbTipoLicenciaC.setSelectedItem(TipoLicencia.valueOf(conductor.getTipolicencia()));
            txtLicenciaC.setText(conductor.getLicencia());
        } else {
            int tipoIdentificacion = cmbTipoIdentificacionC.getSelectedIndex();
            String identificacion = txtIdentificacionC.getText();
            Validaciones.limpiarCampos(pnlConductor);
            cmbTipoIdentificacionC.setSelectedIndex(tipoIdentificacion);
            txtIdentificacionC.setText(identificacion);
        }

        if (validar) {
            sameDriver(propietarioActual, conductor);
        }
    }

    private void datosConductor(Propietario conductor) {
        datosConductor(conductor, Boolean.TRUE);
    }

    /**
     * Metodo para verificar si el propietario es el mismo que el conductor.
     *
     * @param propietario
     * @param conductor
     */
    private void sameDriver(Propietario propietario, Propietario conductor) {
        if (propietario != null && conductor != null && propietario.getId().compareTo(conductor.getId()) == 0) {
            chbConductorPropietario.setSelected(true);
        } else {
            chbConductorPropietario.setSelected(false);
        }
    }

    /**
     * Metodo para llenar el Objecto propietarioActual con los datos que se
     * encuentran en el formulario.
     */
    private void fillPropietario() {
        if (propietarioActual == null) {
            propietarioActual = new Propietario();
            propietarioActual.setId(Long.parseLong(txtIdentificacionP.getText()));
        }
        propietarioActual.setTipoIdentificacion((TipoIdentificacion) cmbTipoIdentificacionP.getSelectedItem());
        propietarioActual.setNombres(txtNombresP.getText());
        propietarioActual.setApellidos(txtApellidosP.getText());
        propietarioActual.setDireccion(txtDireccionP.getText());
        propietarioActual.setTelefono(txtTelefonoP.getText());
        propietarioActual.setCelular(txtCelularP.getText());
        propietarioActual.setEmail(txtEmailP.getText());
        propietarioActual.setCiudad((Ciudad) cmbCiudadP.getSelectedItem());
        TipoLicencia tpLic = (TipoLicencia) cmbTipoLicenciaP.getSelectedItem();
        propietarioActual.setTipolicencia(tpLic.name());
        propietarioActual.setLicencia(txtLicenciaP.getText());
    }

    /**
     * Metodo para llenar el Objecto conductorActual con los datos que se
     * encuentran en el formulario.
     */
    private void fillConductor() {
        if (conductorActual == null) {
            conductorActual = new Propietario();
            conductorActual.setId(Long.parseLong(txtIdentificacionC.getText().trim()));
        }
        conductorActual.setTipoIdentificacion((TipoIdentificacion) cmbTipoIdentificacionC.getSelectedItem());
        conductorActual.setNombres(txtNombresC.getText());
        conductorActual.setApellidos(txtApellidosC.getText());
        conductorActual.setDireccion(txtDireccionC.getText());
        conductorActual.setTelefono(txtTelefonoC.getText());
        conductorActual.setCelular(txtCelularC.getText());
        conductorActual.setEmail(txtEmailC.getText());
        conductorActual.setCiudad((Ciudad) cmbCiudadC.getSelectedItem());
        TipoLicencia tpLic = (TipoLicencia) cmbTipoLicenciaC.getSelectedItem();
        conductorActual.setTipolicencia(tpLic.name());
        conductorActual.setLicencia(txtLicenciaC.getText());
        this.hojaPruebasActual.setConductor(conductorActual);
    }

    /**
     * Metodo para llenar el Objeto vehiculo con los datos que se encuentran en
     * el formulario.
     */
    private void fillVehiculo() {
        if (vehiculo == null) {
            vehiculo = new Vehiculo();
            vehiculo.setPlaca(txtPlaca.getText());
        }
        AseguradoraJpaController asegContro = new AseguradoraJpaController();
        Aseguradora aseg = (Aseguradora) cmbAseguradora.getSelectedItem();
        aseg = asegContro.find(aseg.getId());
        vehiculo.setAseguradora(aseg);

        vehiculo.setFechaSOAT(dprVencSoat.getDate());
        vehiculo.setFechaExpedicionSOAT(dprExpSoat.getDate());
        vehiculo.setNumeroSOAT(txtNumSoat.getText());

        hojaPruebasActual.setAseguradora(aseg);
        hojaPruebasActual.setFechaExpSoat(dprExpSoat.getDate());
        hojaPruebasActual.setFechaVencSoat(dprVencSoat.getDate());
        hojaPruebasActual.setNroIdentificacionSoat(txtNumSoat.getText());
        vehiculo.setCilindraje(Integer.parseInt(txtCilindraje.getText()));
        vehiculo.setClaseVehiculo((ClaseVehiculo) cmbClase.getSelectedItem());
        vehiculo.setColor((Color) cmbColor.getSelectedItem());
        vehiculo.setTipoCarroceria((Tipocarroceria) cmbCarroceria.getSelectedItem());
//        vehiculo.setDiametro(430); ///???   
        vehiculo.setPesoBruto(Integer.parseInt(txtPesoBruto.getText()));
        vehiculo.setPotencia(Integer.parseInt(txtPotencia.getText()));
        vehiculo.setFechaRegistro(dprFechaMatricula.getDate());
        vehiculo.setLineaVehiculo((LineaVehiculo) cmbLinea.getSelectedItem());
        vehiculo.setLlantas((Llanta) cmbTipoLlanta.getSelectedItem());
        vehiculo.setMarca((Marca) cmbMarca.getSelectedItem());
        vehiculo.setModelo((Integer) sprModelo.getValue());
        vehiculo.setNacionalidad((String) cmbNacionalidad.getSelectedItem());
        vehiculo.setEjes((Integer) sprEjes.getValue());
        vehiculo.setExostos((Integer) sprExostos.getValue());
        vehiculo.setMotor(txtNumMotor.getText());
        vehiculo.setLicencia(txtNumLicencia.getText());
        if (this.txtChasis.getText().isEmpty()) {
            vehiculo.setVin(txtChasis.getText());
        } else {
            vehiculo.setVin(txtNumSerie.getText());
        }
        vehiculo.setServicios((Servicio) cmbServicio.getSelectedItem());
        vehiculo.setServicioEspecial((ServicioEspecial) cmbServEspeciales.getSelectedItem());
        vehiculo.setTiemposMotor((Integer) sprTiemposMotor.getValue());
        vehiculo.setTipoVehiculo((TipoVehiculo) cmbTipo.getSelectedItem());
        vehiculo.setTipoGasolina((TipoGasolina) cmbCombustible.getSelectedItem());
        vehiculo.setPais((Pais) cmbPais.getSelectedItem());
        vehiculo.setCatalizador("N");
        if (txtKilometraje.getText().equalsIgnoreCase("NO FUNCIONAL") || txtKilometraje.getText().equalsIgnoreCase("")) {
//          hojaPruebasActual.setKilometraje("0");
            vehiculo.setKilometraje(0);
        } else {
            vehiculo.setKilometraje(Integer.parseInt(txtKilometraje.getText()));
//            hojaPruebasActual.setKilometraje(txtKilometraje.getText());
        }
        vehiculo.setSillas((Integer) sprSillas.getValue());
        vehiculo.setBlindaje((rdbBlindajeSi.isSelected() ? "Y" : "N"));
        vehiculo.setVidriosPolarizados((rdbPolarizadosSi.isSelected() ? "Y" : "N"));
        //vehiculo.setCatalizador((catalizadorsi.isSelected() ? "Y" : "N"));
        vehiculo.setChasis(txtChasis.getText());
        vehiculo.setCodigoInterno(txtCodInterno.getText());//Obtener Codigo interno      
        vehiculo.setUsuario(LoggedUser.getIdUsuario());

        if (cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Moto")) {
            if (chbScooter.isSelected()) {
                vehiculo.setDiseno(Diseno.Scooter);
            } else {
                vehiculo.setDiseno(Diseno.Convencional);
            }
        } else {
            vehiculo.setDiseno(Diseno.None);
        }
        if (apliEnsezas.isSelected()) {
            vehiculo.setEsEnsenaza(1);
        } else {
            vehiculo.setEsEnsenaza(0);
        }
        if (congnvsi.isSelected()) {
            vehiculo.setEsConversionGnv("Y");
        }
        if (congnvno.isSelected()) {
            vehiculo.setEsConversionGnv("N");
        }
        if (congnvnoaplica.isSelected()) {
            vehiculo.setEsConversionGnv("NA");
        }
        vehiculo.setTipoCarroceria((Tipocarroceria) cmbCarroceria.getSelectedItem());
        hojaPruebasActual.setVehiculo(vehiculo);
    }

    private void datosHojaPruebas(List<HojaPruebas> hojasPruebas) {
        modeloHojaPruebas = new ModeloTablaHojasVer(hojasPruebas);
        tblHojaPruebas.setModel(modeloHojaPruebas);
    }

    private void datosPruebas(List<Prueba> pruebas) {
        modeloPruebas = new ModeloTablaPrueba(pruebas);
        tblPruebas.setModel(modeloPruebas);

    }

    private void guardarReinspeccion() throws Exception {

        if (reinspeccionActual != null && !pruebasTemp.isEmpty()) {
            System.out.println("ENTRO A REINSPE ACTUAL");
            ReinspeccionJpaController reinspeccionesJPA = new ReinspeccionJpaController();
            reinspeccionActual.setHojaPruebas(hojaPruebasActual);
            System.out.println("Hoja de pruebas que estoy evaluando");
            reinspeccionActual.setFechaSiguiente(Calendar.getInstance().getTime());
            reinspeccionActual.setAprobada("N");
            reinspeccionActual.setFechaAnterior(hojaPruebasActual.getFechaIngreso());
            reinspeccionActual.setIntento(1);
            List<Prueba> pruebasReinspeccion = new ArrayList<>();
            int nroTest = 0;
            for (Prueba prueba : hojaPruebasActual.getListPruebas()) {
                if (prueba.getFinalizada().equals("N")) {
                    pruebasReinspeccion.add(prueba);
                }
            }
            reinspeccionActual.setPruebaList(pruebasReinspeccion);
            reinspeccionesJPA.create(reinspeccionActual);
            System.out.println("cree la reinspeccion");
            if (!pruebasTemp.isEmpty()) {
                hojaPruebasActual.setUsuario(LoggedUser.getIdUsuario());

                if (reinspeccionActual != null) {
                    hojaPruebasActual.setIntentos(hojaPruebasActual.getIntentos() + 1);
                    System.out.println("sumo un numero de intento");
                }
            }
            // new ReinspByPruebaController().createByList(pruebasReinspeccion, reinspeccionActual.getId());
            for (Prueba pruebas : pruebasTemp) {
                System.out.println("ID Prueba: " + pruebas.getId());
            }
            System.out.println("hoja de pruebas que edito: " + hojaPruebasActual.getId());
            hojaPruebasJPA.edit(hojaPruebasActual);
            this.pulsoTrans = false;
        }
        defaultValues();
        txtPlaca.requestFocus();
        pnlRevisiones.remove(pnlRegistroPropietario);
        pnlRevisiones.remove(pnlRegistroPruebas);

    }

    private void guardarDatos() throws Exception {
        //Validar que todos los campos se encuentren, con la informacion correspondiente
        if (!validar()) {
            return;
        }
        //Guardar Propietario y Conductor
        //Se comprueba que el propietario exista en la base de datos
        if (ctxCda.getClaseCda().equalsIgnoreCase("B")) {
            Integer pesoBruto = Integer.parseInt(txtPesoBruto.getText());
            if (pesoBruto >= 3500) {
                Mensajes.mensajeCorrecto("Disculpe La Categoria de este CDA es B, por lo tanto no puede Ingresar Vehiculo Mayores 3500 Kg.");
                return;
            }
        }
        fillPropietario();
        //Se comprueba si el propietario y el conductor son el mismo de no ser asi se guarda o actualiza el conductor
        if (chbConductorPropietario.isSelected() == true) {
            conductorActual = propietarioActual;
        } else {
            fillConductor();
        }
        //Guardar Vehiculo        
        fillVehiculo();
        //Guardar Hoja de Prueba y sus Respectivas Prueba    
        Usuario usuarioResp = new UsuarioJpaController().findNombre(this.cmbDtResp.getSelectedItem().toString());
        Cda ctxCDA = new CdaJpaController().find(1);
        PropietarioJpaController proJpa = new PropietarioJpaController();
        hojaPruebasActual.setConductor(conductorActual);
        hojaPruebasActual.setPropietario(propietarioActual);
        hojaPruebasActual.setVehiculo(vehiculo);
        hojaPruebasActual.setResponsable(usuarioResp);
        hojaPruebasActual.setUbicacionMunicipio(ctxCDA.getCiudad());
        hojaPruebasActual.setKilometraje("0");
        if (VencGnv.getDate() != null) {
            hojaPruebasActual.setFechaVencimientoGnv(VencGnv.getDate());
        }
        String msgUser = "";
        if (hojaPruebasActual.getId() == null) {
            System.out.println("HOJA DE PRUEBA ACTUAL NULL");
            if (ctxPing.length() > 2) {
                hojaPruebasActual.setPin(ctxPing);
            }
            hojaPruebasActual.setAnulado("N");
            hojaPruebasJPA.create(hojaPruebasActual);
            this.pulsoTrans = false;
            if (hojaPruebasActual.getPreventiva().equalsIgnoreCase("N")) {
                msgUser = "Se ha REGISTRADO con exito esta Revision TecnoMecanica ..! ";
            } else {
                msgUser = "Se ha REGISTRADO con exito esta Revision Preventiva ..! ";
            }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        } else {
            System.out.println("hoja de prueba actual no null");
            System.out.println("hoja de pruebas que edito: " + hojaPruebasActual.getId());
            hojaPruebasActual.setResponsable(usuarioResp);
            System.out.println("usuario que envio: " + usuarioResp);
            System.out.println("");
            hojaPruebasJPA.edit(hojaPruebasActual);
            this.pulsoTrans = false;
            if (hojaPruebasActual.getPreventiva().equalsIgnoreCase("N")) {
                msgUser = "Se ha ACTUALIZADO con exito esta Revision TecnoMecanica ..! ";
            } else {
                msgUser = "Se ha ACTUALIZADO  con exito esta Revision Preventiva ..! "; 
            }
        }
        //Registrar reinspeccion y Prueba x Reinspeccion
        //pruebasTemp.clear();
        Mensajes.mensajeCorrecto(msgUser);
        btnHojaPruebas.setEnabled(true);
        txtPlaca.setText("");
        placaActual = "";
        defaultValues();
        txtPlaca.requestFocus();
        pnlRevisiones.remove(pnlRegistroPropietario);
        pnlRevisiones.remove(pnlRegistroPruebas);
    }

    public final void defaultValues() {
        //Vehiculo
        txtPesoBruto.setText("");
        txtPotencia.setText("");
        txtCodInterno.setText("");
        txtCilindraje.setText("");
        txtKilometraje.setText("");
        txtNumLicencia.setText("");
        txtNumMotor.setText("");
        txtNumSerie.setText("");
        txtNumSoat.setText("");
        txtColor.setText("");
        cmbColor.removeAllItems();
        txtCarroceria.setText("");
        cmbCarroceria.removeAllItems();
        cmbAseguradora.setSelectedIndex(1);
        txtChasis.setText("");
        txtPlaca.setText("");
//        cmbAseguradora.removeItemAt(0);
        cmbClase.setSelectedIndex(0);

        cmbCombustible.setSelectedItem(new TipoGasolinaJpaController().find(1));
        cmbMarca.setSelectedIndex(0);
        cmbTipoLlanta.setSelectedIndex(0);
        cmbPais.setSelectedIndex(18);
        cmbServEspeciales.setSelectedIndex(0);
        cmbServicio.setSelectedIndex(2);
        cmbTipo.setSelectedIndex(0);
        cmbNacionalidad.setSelectedIndex(0);

        dprExpSoat.setDate(new Date());
        //dprFechaMatricula.setDate(new Date());
        dprVencSoat.setDate(new Date());
        dprFechaMatricula.setDate(null);
        sprEjes.setValue(2);
        sprExostos.setValue(1);
        sprSillas.setValue(1);
        sprTiemposMotor.setValue(4);
        sprModelo.setValue(2000);

        rdbBlindajeNo.setSelected(true);
        rdbPolarizadosNo.setSelected(true);
        congnvno.setSelected(true);
        //catalizadorno.setSelected(true);

        chbScooter.setEnabled(false);
        chbScooter.setSelected(false);
        apliEnsezas.setSelected(false);

        sprEjes.setEnabled(false);
        sprEjes.setValue(2);

        //Conductor y Propietario
        Validaciones.limpiarCampos(pnlConductor);
        Validaciones.limpiarCampos(pnlPropietario);
        chbConductorPropietario.setSelected(true);
        pnlConductor.setVisible(false);
        conductorActual = null;
        propietarioActual = null;

        //Hoja de Prueba y Prueba
        tblHojaPruebas.setModel(new ModeloTablaHojasVer());
        tblPruebas.setModel(new ModeloTablaPrueba());
        btnHojaPruebas.setText("Nueva");
        btnCancelarHojaPruebas.setEnabled(false);
        reinspeccionActual = null;

        for (Component boton : pnlPruebas.getComponents()) {
            if (boton instanceof JButton) {
                boton.setEnabled(false);
            }
        }
        congnvsi.setSelected(false);
        congnvno.setSelected(false);
        congnvnoaplica.setSelected(false);
    }

    /**
     * Metodo para formatear los campos del formulario
     */
    private void formatearCampos() {
        //Campos que solo admiten numeros

        //Vehiculo
        Validaciones.validarCamposNumericos(txtCilindraje);
        Validaciones.validarCamposNumericos(txtKilometraje);

        //Conductor y Propietario
        Validaciones.validarCamposNumericos(txtTelefonoC);
        Validaciones.validarCamposNumericos(txtTelefonoP);
        Validaciones.validarCamposNumericos(txtIdentificacionC);
        Validaciones.validarCamposNumericos(txtIdentificacionP);

        //Campos Mayusculas
        //Vehiculo
        Validaciones.pasarAMayusculas(pnlIzquierdo);
        Validaciones.pasarAMayusculas(pnlDerecho);

        //Conductor y Propietario
        Validaciones.pasarAMayusculas(pnlConductor);
        Validaciones.pasarAMayusculas(pnlPropietario);
        txtEmailC.removeKeyListener(txtEmailC.getKeyListeners()[0]);
        txtEmailP.removeKeyListener(txtEmailP.getKeyListeners()[0]);
    }

    /**
     * Metodo para validar (Llenar los campos que asi lo requieran) los
     * formularios de todos los datos, segun las especificaciones del metodo
     * validarCampos de la clase Validaciones
     *
     * @return - false cuando hay campos vacios que requieren informacion - true
     * cuando todos los campos que requieren informacion estan completos
     */
    private boolean validar() {
        Boolean estado = true;
        if (!Validaciones.validarCampos(pnlIzquierdo)) {
            estado = false;
        }

        if (!Validaciones.validarCampos(pnlDerecho)) {
            estado = false;
        }
        if (dprFechaMatricula.getDate() == null) {
            Mensajes.mensajeAdvertencia("Disculpe, no se ha registrado la fecha de la Matricula del Vehiculo");
            return false;
        }

        if (hojaPruebasActual == null || modeloHojaPruebas.getListaHojaPruebas().isEmpty()) {
            Mensajes.mensajeAdvertencia("Debe crear una Hoja de Pruebas!");
            estado = false;
        }

        if (pnlConductor.isVisible()) {
            if (!Validaciones.validarCampos(pnlConductor)) {
                estado = false;
            }
        }

        if (!Validaciones.validarCampos(pnlPropietario)) {
            estado = false;
        }

        if (!txtKilometraje.getText().equalsIgnoreCase("NO FUNCIONAL")) {
            if (!kilometrajeAnulada) {
                if (txtKilometraje.getText().equalsIgnoreCase(kilometrajeTemporal)) {
                    estado = false;
                    Mensajes.mensajeAdvertencia("Debe Actualizar Kilometraje o validar funcionamineto del odometro");
                }
            } else {
                kilometrajeAnulada = false;
            }
        }
        return estado;
    }

    /**
     * Metodo para bloquear los campos dependiendo del tipo de vehiculo
     */
    private void blockFields() {
        //Invalidamos todos los campos que haci lo requieran, ademas se resetean sus valores
        chbScooter.setEnabled(false);
        chbScooter.setSelected(false);
        sprEjes.setEnabled(false);

        //Se realiza la validacion para saber que campos se pueden habilitar
        if (cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Pesado")) {
            sprEjes.setEnabled(true);
        } else {
            sprEjes.setValue(2);
        }

        if (cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Moto")) {
            chbScooter.setEnabled(true);
            if (vehiculo != null && (vehiculo.getDiseno() == Diseno.Scooter)) {
                chbScooter.setSelected(true);
            }
        }
    }

    /**
     * metodo para cambiar la clase del vehiculo dependiendo del tipo de
     * vehiculo seleccionado
     */
    private void claseByTipoVehiculo() {
        if (cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Moto")) {
            cmbClase.setSelectedIndex(9);
        } else if (cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Liviano")) {
            cmbClase.setSelectedIndex(0);
        } else if (cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Pesado")) {
            cmbClase.setSelectedIndex(3);
        }
    }

    /**
     * Metodo para bloquear los botones de pruebas, si la hoja esta finalizada,
     * o hay pruebas que no se puedan agregar por alguna razon.
     */
    private void verificarHojaPruebas() {

        //Si la prueba esta finalizada no se habilitara ningun boton para la
        //modificacion de pruebas
        if (hojaPruebasActual.getFinalizada().equals("Y") && hojaPruebasActual.getAprobado().equalsIgnoreCase("Y")) {
            for (Component boton : pnlPruebas.getComponents()) {
                if (boton instanceof JButton) {
                    boton.setEnabled(false);
                }
            }
            return;
        }

        for (Component boton : pnlPruebas.getComponents()) {
            if (boton instanceof JButton) {
                boton.setEnabled(true);
            }
        }

        /*if (hojaPruebasActual.getPreventiva().equals("Y")) {
         mostrar1 = true;
         txtCodInterno.setVisible(mostrar1);
         lblCodInterno.setVisible(mostrar1);
         } else {
         mostrar1 = false;
         txtCodInterno.setVisible(mostrar1);
         lblCodInterno.setVisible(mostrar1);
         }*/
        btnReinspeccion.setEnabled(false);
        btnSugerir.setEnabled(true);
        //Cuando se crea una hoja de pruebas nueva se dejan habilitados los botones
        //excepto el boton de Quitar pruebas, y se termina el metodo.
        if (hojaPruebasActual.getId() == null) {
            return;
        }

        List<Prueba> pruebas = pruebasJPA.findUltimasPruebasByHoja(hojaPruebasActual.getId());

        //Se valida que tenga todas las pruebas minimas segun el tipo de vehiculo.
        /*Predicate<Integer> pruebasBasicas = i -> i == 1 || i == 2 || i == 3 || i == 5 || i == 7 || i == 8;
         List<Integer> idTiposPruebas = pruebas.stream().mapToInt(p -> p.getTipoPrueba().getId()).distinct().boxed().collect(Collectors.toList());
        
         Boolean tienePruebasBasicas = idTiposPruebas.stream().filter(pruebasBasicas).count() == 6;
        
         int idTipoVehiculo = vehiculo.getTipoVehiculo().getId();
        
         if (tienePruebasBasicas && (idTipoVehiculo == 1 || idTipoVehiculo == 2 || idTipoVehiculo == 3)) {
         tienePruebasBasicas = idTiposPruebas.stream().anyMatch(p -> p == 4);
            
         if (tienePruebasBasicas && (idTipoVehiculo == 1 || idTipoVehiculo == 2)) {
         tienePruebasBasicas = idTiposPruebas.stream().anyMatch(p -> p == 6);
         }
         }*/
        if (!pruebas.isEmpty() && hojaPruebasActual.getIntentos() <= 2 /*&& tienePruebasBasicas*/) {
            btnReinspeccion.setEnabled(true);
        }
        //validacion si el vehiculo  se le debe habilitar pruebas de gases
        //vehiculo.getTipoGasolina().getId() == 2 || vehiculo.getTipoGasolina().getId() == 9 || 
        if (vehiculo.getTipoGasolina().getId() == 6 || vehiculo.getTipoGasolina().getId() == 5) {
            btnGases.setEnabled(false);
        }
        //Se verifica que pruebas se pueden agregar y se habilita el boton correspondiente para cada prueba
        for (Prueba prueba : pruebas) {

            if (prueba.getFinalizada().equals("N")) {
                btnReinspeccion.setEnabled(false);
            }

            if (prueba.getFinalizada().equalsIgnoreCase("Y") || prueba.getFinalizada().equalsIgnoreCase("N")) {
                switch (prueba.getTipoPrueba().getId()) {
                    case 1:
                        btnVisual.setEnabled(false);
                        break;
                    case 2:
                        btnLuces.setEnabled(false);
                        break;
                    case 3:
                        btnFoto.setEnabled(false);
                        break;
                    case 4:
                        btnDesviacion.setEnabled(false);
                        break;
                    case 5:
                        btnFrenos.setEnabled(false);
                        break;
                    case 6:
                        btnSuspension.setEnabled(false);
                        break;
                    case 7:
                        btnRuido.setEnabled(false);
                        break;
                    case 8:
                        btnGases.setEnabled(false);
                        break;
                }
            }
        }
    }

    /**
     * Metodo para crear y agregar una prueba a la lista de pruebas a guardar y
     * a la lista de la hoja de pruebas actual
     *
     * @param idTipoPrueba
     */
    private void addPrueba(int idTipoPrueba, boolean changeNroRevision) {
        try {
            try {
                String resourceName = "configuracion.properties"; // could also be a constant
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                Properties properties = new Properties();
                try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
                    properties.load(resourceStream);
                }

//            config = loader.getResourceAsStream(resourceName);
//            config = new BufferedReader(new FileReader(new File("configuracion.txt")));
//            String line;
//            while (!config.readLine().startsWith("[Reinspeccion]")) {
//            }
//            if ((line = config.readLine()).startsWith("valida_foto:"));
//            valida_foto = (line.substring(line.indexOf(" ") + 1, line.length()));
                valida_foto = Boolean.parseBoolean(properties.getProperty("valida_foto"));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println("no se pudo abrir" + ex);
            }
            String aprovado = "";
            Prueba prueba = new Prueba();
            prueba.setTipoPrueba(new TipoPruebaJpaController().find(idTipoPrueba));
            prueba.setHojaPruebas(hojaPruebasActual);//aca
            Calendar calendar = Calendar.getInstance();
            prueba.setFecha(calendar.getTime());
            prueba.setFechaFinal(calendar.getTime());

            prueba.getUsuarioFor().setUsuario(LoggedUser.getIdUsuario());
            if (valida_foto) {
                if (idTipoPrueba == 3) {
                    aprovado = "Y";
                    prueba.setAprobado(aprovado);
                    prueba.setFinalizada(aprovado);
                }
            }
            btnReinspeccion.setEnabled(false);
            pruebasTemp.add(prueba);
            hojaPruebasActual.getListPruebas().add(prueba);
            if (changeNroRevision == true) {

            }

            modeloPruebas.setListPruebas(hojaPruebasActual.getListPruebas());
            if (hojaPruebasActual.getReinspeccionList() != null) {
                if (hojaPruebasActual.getReinspeccionList().size() > 0) {
                    try {
                        Reinspeccion reins = hojaPruebasActual.getReinspeccionList().iterator().next();
                        reins.getPruebaList().add(prueba);
                        ReinspeccionJpaController reinspeccionesJPA = new ReinspeccionJpaController();
                        reinspeccionesJPA.edit(reins);
                    } catch (Exception ex) {
                    }
                } else {
                    PruebaJpaController pru = new PruebaJpaController();
                    pru.create(prueba);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PanelRevisiones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void tblPruebaValueChanged(ListSelectionEvent event) {
        if (tblPruebas.getSelectedRow() == -1) {
            return;
        }

        Prueba prueba = modeloPruebas.getListPruebas().get(tblPruebas.getSelectedRow());
        if (prueba.getTipoPrueba().getId() == 3 || (!LoggedUser.isAdministrador() && prueba.getId() != null)) {

        } else {

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupBlindaje = new javax.swing.ButtonGroup();
        groupPolarizados = new javax.swing.ButtonGroup();
        grupocatalizador = new javax.swing.ButtonGroup();
        ConversionGNV = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        pnlRevisiones = new javax.swing.JTabbedPane();
        pnlVehiculo = new javax.swing.JPanel();
        pnlIzquierdo = new javax.swing.JPanel();
        sprExostos = new javax.swing.JSpinner();
        sprEjes = new javax.swing.JSpinner();
        com.soltelec.consolaentrada.models.controllers.ClaseVehiculoJpaController clasesJPA = new com.soltelec.consolaentrada.models.controllers.ClaseVehiculoJpaController();
        cmbClase = new javax.swing.JComboBox(clasesJPA.findAll().toArray())
        ;
        com.soltelec.consolaentrada.models.controllers.ServicioJpaController servicioJPA = new com.soltelec.consolaentrada.models.controllers.ServicioJpaController();
        cmbServicio = new javax.swing.JComboBox(servicioJPA.findAll().toArray());
        com.soltelec.consolaentrada.models.controllers.ServicioEspecialJpaController spServiceJPA = new com.soltelec.consolaentrada.models.controllers.ServicioEspecialJpaController();
        cmbServEspeciales = new javax.swing.JComboBox(spServiceJPA.findAll().toArray());
        txtCilindraje = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtPesoBruto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        PaisJpaController paisJPA = new PaisJpaController();
        cmbPais = new javax.swing.JComboBox(paisJPA.findAll().toArray());
        txtPlaca = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        sprTiemposMotor = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TipoVehiculoJpaController tpvJPA = new TipoVehiculoJpaController();
        cmbTipo = new javax.swing.JComboBox(tpvJPA.findAllAutorizados().toArray());
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        com.soltelec.consolaentrada.models.controllers.MarcaJpaController marcasJPA = new com.soltelec.consolaentrada.models.controllers.MarcaJpaController();
        cmbMarca = new javax.swing.JComboBox(marcasJPA.findAll().toArray())
        ;
        cmbLinea = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        sprModelo = new javax.swing.JSpinner();
        lblCodInterno1 = new javax.swing.JLabel();
        chbScooter = new javax.swing.JCheckBox();
        apliEnsezas = new javax.swing.JCheckBox();
        jLabel54 = new javax.swing.JLabel();
        txtLinea = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        txtChasis = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        txtPotencia = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        cmbCarroceria = new javax.swing.JComboBox();
        jLabel60 = new javax.swing.JLabel();
        txtCarroceria = new javax.swing.JTextField();
        chckKilometrajeFuncional = new javax.swing.JCheckBox();
        txtKilometraje = new javax.swing.JTextField();
        pnlDerecho = new javax.swing.JPanel();
        sprSillas = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        rdbBlindajeSi = new javax.swing.JRadioButton();
        rdbBlindajeNo = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        rdbPolarizadosSi = new javax.swing.JRadioButton();
        rdbPolarizadosNo = new javax.swing.JRadioButton();
        jLabel17 = new javax.swing.JLabel();
        cmbNacionalidad = new javax.swing.JComboBox();
        com.soltelec.consolaentrada.models.controllers.LlantaJpaController llantaJPA = new com.soltelec.consolaentrada.models.controllers.LlantaJpaController();
        cmbTipoLlanta = new javax.swing.JComboBox(llantaJPA.findAll().toArray())
        ;
        jLabel16 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        dprVencSoat = new JXDatePicker(new Date(),new Locale("es"));
        dprVencSoat.setFormats(new String[]{"dd/MM/yyyy"});
        txtNumSoat = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        com.soltelec.consolaentrada.models.controllers.AseguradoraJpaController aseguradoraJPA = new com.soltelec.consolaentrada.models.controllers.AseguradoraJpaController();
        cmbAseguradora = new javax.swing.JComboBox(aseguradoraJPA.findAll().toArray());
        jLabel28 = new javax.swing.JLabel();
        dprFechaMatricula = new org.jdesktop.swingx.JXDatePicker();
        dprFechaMatricula.setFormats(new String[]{"dd/MM/yyyy"});
        jLabel27 = new javax.swing.JLabel();
        dprExpSoat = new JXDatePicker(new Date(),new Locale("es"));
        dprExpSoat.setFormats(new String[]{"dd/MM/yyyy"});
        jLabel26 = new javax.swing.JLabel();
        txtNumSerie = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtNumMotor = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtNumLicencia = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        com.soltelec.consolaentrada.models.controllers.TipoGasolinaJpaController tipoGasolinaJPA = new com.soltelec.consolaentrada.models.controllers.TipoGasolinaJpaController();
        cmbCombustible = new javax.swing.JComboBox(tipoGasolinaJPA.findAll().toArray());
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtColor = new javax.swing.JTextField();
        cmbColor = new javax.swing.JComboBox();
        jLabel53 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        VencGnv = new org.jdesktop.swingx.JXDatePicker();
        dprFechaMatricula.setFormats(new String[]{"dd/MM/yyyy"});
        jPanel1 = new javax.swing.JPanel();
        congnvsi = new javax.swing.JRadioButton();
        congnvno = new javax.swing.JRadioButton();
        congnvnoaplica = new javax.swing.JRadioButton();
        lblCodInterno = new javax.swing.JLabel();
        txtCodInterno = new javax.swing.JTextField();
        pnlRegistroPruebas = new javax.swing.JPanel();
        pnlHojaPruebas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHojaPruebas = new javax.swing.JTable();
        btnCancelarHojaPruebas = new javax.swing.JButton();
        btnHojaPruebas = new javax.swing.JButton();
        pnlPruebas = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPruebas = new javax.swing.JTable();
        btnDesviacion = new javax.swing.JButton();
        btnFrenos = new javax.swing.JButton();
        btnSuspension = new javax.swing.JButton();
        btnVisual = new javax.swing.JButton();
        btnTaximetro = new javax.swing.JButton();
        btnFoto = new javax.swing.JButton();
        btnLuces = new javax.swing.JButton();
        btnGases = new javax.swing.JButton();
        btnRuido = new javax.swing.JButton();
        btnQuitar = new javax.swing.JButton();
        btnSugerir = new javax.swing.JButton();
        btnDerogar = new javax.swing.JButton();
        UsuarioJpaController uJpa = new UsuarioJpaController();
        cmbDtResp = new javax.swing.JComboBox(uJpa.findDtCda().toArray());
        lblCodInterno2 = new javax.swing.JLabel();
        btnReinspeccion = new javax.swing.JButton();
        btnAnular1 = new javax.swing.JButton();
        btnEditarPlaca = new javax.swing.JButton();
        btnHabilitar = new javax.swing.JButton();
        btnAnular = new javax.swing.JButton();
        pnlRegistroPropietario = new javax.swing.JPanel();
        pnlConductor = new javax.swing.JPanel();
        txtNombresC = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        cmbTipoIdentificacionC = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtApellidosC = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtIdentificacionC = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtTelefonoC = new javax.swing.JTextField();
        txtDireccionC = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtEmailC = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        com.soltelec.consolaentrada.models.controllers.DepartamentoJpaController departamentosJPA = new com.soltelec.consolaentrada.models.controllers.DepartamentoJpaController();
        cmbDepartamentoC = new javax.swing.JComboBox(departamentosJPA.findAll().toArray());
        jLabel37 = new javax.swing.JLabel();
        modeloCiudadC = new MyOwnComboBoxModel(((com.soltelec.consolaentrada.models.entities.Departamento)cmbDepartamentoC.getSelectedItem()).getCiudades());
        cmbCiudadC = new javax.swing.JComboBox(modeloCiudadC);
        jLabel38 = new javax.swing.JLabel();
        cmbTipoLicenciaC = new javax.swing.JComboBox();
        jLabel39 = new javax.swing.JLabel();
        txtLicenciaC = new javax.swing.JTextField();
        txtCelularC = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        pnlPropietario = new javax.swing.JPanel();
        txtNombresP = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        cmbTipoIdentificacionP = new javax.swing.JComboBox();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        txtApellidosP = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtIdentificacionP = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        txtTelefonoP = new javax.swing.JTextField();
        txtDireccionP = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        txtEmailP = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        cmbDepartamentoP = new javax.swing.JComboBox(departamentosJPA.findAll().toArray());
        jLabel48 = new javax.swing.JLabel();
        modeloCiudadP = new MyOwnComboBoxModel(((com.soltelec.consolaentrada.models.entities.Departamento)cmbDepartamentoC.getSelectedItem()).getCiudades());
        cmbCiudadP = new javax.swing.JComboBox(modeloCiudadP);
        jLabel49 = new javax.swing.JLabel();
        cmbTipoLicenciaP = new javax.swing.JComboBox();
        jLabel50 = new javax.swing.JLabel();
        txtLicenciaP = new javax.swing.JTextField();
        chbConductorPropietario = new javax.swing.JCheckBox();
        jLabel51 = new javax.swing.JLabel();
        txtCelularP = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        pnlRevisiones.setForeground(new java.awt.Color(0, 102, 255));
        pnlRevisiones.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        pnlVehiculo.setPreferredSize(new java.awt.Dimension(800, 600));

        pnlIzquierdo.setPreferredSize(new java.awt.Dimension(450, 528));

        sprExostos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sprExostos.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));
        sprExostos.setPreferredSize(new java.awt.Dimension(260, 23));

        sprEjes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sprEjes.setModel(new javax.swing.SpinnerNumberModel(2, 2, 5, 1));
        sprEjes.setPreferredSize(new java.awt.Dimension(260, 23));

        cmbClase.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbClase.setPreferredSize(new java.awt.Dimension(260, 23));
        cmbClase.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbClaseItemStateChanged(evt);
            }
        });
        cmbClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbClaseActionPerformed(evt);
            }
        });

        cmbServicio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbServicio.setPreferredSize(new java.awt.Dimension(260, 23));

        cmbServEspeciales.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbServEspeciales.setPreferredSize(new java.awt.Dimension(260, 23));

        txtCilindraje.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCilindraje.setName("Cilindraje"); // NOI18N
        txtCilindraje.setPreferredSize(new java.awt.Dimension(260, 23));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Pais");

        txtPesoBruto.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPesoBruto.setName("Peso Bruto"); // NOI18N
        txtPesoBruto.setPreferredSize(new java.awt.Dimension(260, 23));
        txtPesoBruto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesoBrutoActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Marca");

        cmbPais.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbPais.setPreferredSize(new java.awt.Dimension(260, 23));

        txtPlaca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPlaca.setName("Placa"); // NOI18N
        txtPlaca.setPreferredSize(new java.awt.Dimension(260, 23));
        txtPlaca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPlacaFocusLost(evt);
            }
        });
        txtPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlacaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Placa");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Tipo");

        sprTiemposMotor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sprTiemposMotor.setModel(new javax.swing.SpinnerNumberModel(2, 2, 4, 2));
        sprTiemposMotor.setPreferredSize(new java.awt.Dimension(260, 23));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Escoja Linea");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Modelo");

        cmbTipo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoItemStateChanged(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setText("Kilometraje:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setText("Peso Bruto (Kg):");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Num. Exostos");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("Ejes");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Cilindraje");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Tiempos de Motor");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Servicio");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Serv. Especiales");

        cmbMarca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbMarca.setPreferredSize(new java.awt.Dimension(260, 23));
        cmbMarca.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMarcaItemStateChanged(evt);
            }
        });
        cmbMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMarcaActionPerformed(evt);
            }
        });

        cmbLinea.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbLinea.setPreferredSize(new java.awt.Dimension(260, 23));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Clase");

        sprModelo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sprModelo.setModel(new javax.swing.SpinnerNumberModel(2000, 1930, 2050, 1));
        sprModelo.setEditor(new javax.swing.JSpinner.NumberEditor(sprModelo, "####"));
        sprModelo.setPreferredSize(new java.awt.Dimension(260, 23));

        lblCodInterno1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCodInterno1.setText("Aplica Enseñanzas");

        chbScooter.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        chbScooter.setText("Scooter");
        chbScooter.setEnabled(false);

        jLabel54.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel54.setText("Busq. Linea");

        txtLinea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLineaActionPerformed(evt);
            }
        });
        txtLinea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLineaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLineaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtLineaKeyTyped(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel55.setText("Numero Chasis");

        txtChasis.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtChasis.setName("Nmero Chasis"); // NOI18N
        txtChasis.setPreferredSize(new java.awt.Dimension(260, 23));

        jLabel58.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel58.setText("Potencia:");

        txtPotencia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPotencia.setName("Peso Bruto"); // NOI18N
        txtPotencia.setPreferredSize(new java.awt.Dimension(260, 23));

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel59.setText("Tipo de carroceria:");

        cmbCarroceria.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbCarroceria.setPreferredSize(new java.awt.Dimension(260, 23));
        cmbCarroceria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCarroceriaActionPerformed(evt);
            }
        });
        cmbCarroceria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cmbCarroceriaKeyTyped(evt);
            }
        });

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel60.setText("Busq. carroceria:");

        txtCarroceria.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCarroceria.setPreferredSize(new java.awt.Dimension(260, 23));
        txtCarroceria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCarroceriaActionPerformed(evt);
            }
        });
        txtCarroceria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCarroceriaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCarroceriaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCarroceriaKeyTyped(evt);
            }
        });

        chckKilometrajeFuncional.setText("Funcional?");
        chckKilometrajeFuncional.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chckKilometrajeFuncionalStateChanged(evt);
            }
        });
        chckKilometrajeFuncional.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chckKilometrajeFuncionalActionPerformed(evt);
            }
        });

        txtKilometraje.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtKilometraje.setName("Peso Bruto"); // NOI18N
        txtKilometraje.setPreferredSize(new java.awt.Dimension(260, 23));

        javax.swing.GroupLayout pnlIzquierdoLayout = new javax.swing.GroupLayout(pnlIzquierdo);
        pnlIzquierdo.setLayout(pnlIzquierdoLayout);
        pnlIzquierdoLayout.setHorizontalGroup(
            pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIzquierdoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlIzquierdoLayout.createSequentialGroup()
                        .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlIzquierdoLayout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblCodInterno1)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel9)))
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel55, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sprModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlIzquierdoLayout.createSequentialGroup()
                                .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(chbScooter, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbLinea, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbServEspeciales, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCilindraje, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlIzquierdoLayout.createSequentialGroup()
                                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(sprEjes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                    .addComponent(sprTiemposMotor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(sprExostos, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlIzquierdoLayout.createSequentialGroup()
                                    .addComponent(txtPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel58)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtPotencia, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addComponent(txtChasis, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbPais, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtLinea, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(apliEnsezas)
                            .addComponent(txtCarroceria, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlIzquierdoLayout.createSequentialGroup()
                        .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCarroceria, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIzquierdoLayout.createSequentialGroup()
                                .addComponent(chckKilometrajeFuncional)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtKilometraje, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)))))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        pnlIzquierdoLayout.setVerticalGroup(
            pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIzquierdoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(sprModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chbScooter))
                .addGap(9, 9, 9)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCodInterno1)
                    .addComponent(apliEnsezas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLinea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbLinea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbServEspeciales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCilindraje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sprTiemposMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(sprExostos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(sprEjes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(cmbPais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(txtChasis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58)
                    .addComponent(txtPotencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCarroceria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCarroceria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(chckKilometrajeFuncional)
                    .addComponent(txtKilometraje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        chbScooter.getAccessibleContext().setAccessibleName("chbScooter");

        pnlDerecho.setPreferredSize(new java.awt.Dimension(450, 528));

        sprSillas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sprSillas.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));
        sprSillas.setPreferredSize(new java.awt.Dimension(260, 23));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Blindaje", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(163, 48));

        groupBlindaje.add(rdbBlindajeSi);
        rdbBlindajeSi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rdbBlindajeSi.setText("SI");

        groupBlindaje.add(rdbBlindajeNo);
        rdbBlindajeNo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rdbBlindajeNo.setText("NO");
        rdbBlindajeNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbBlindajeNoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(rdbBlindajeSi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addComponent(rdbBlindajeNo)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbBlindajeSi)
                    .addComponent(rdbBlindajeNo)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Vidrios Polarizados", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        groupPolarizados.add(rdbPolarizadosSi);
        rdbPolarizadosSi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rdbPolarizadosSi.setText("SI");
        rdbPolarizadosSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbPolarizadosSiActionPerformed(evt);
            }
        });

        groupPolarizados.add(rdbPolarizadosNo);
        rdbPolarizadosNo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rdbPolarizadosNo.setText("NO");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdbPolarizadosSi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(rdbPolarizadosNo)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbPolarizadosSi)
                    .addComponent(rdbPolarizadosNo)))
        );

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setText("Tipo de Llanta");

        cmbNacionalidad.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbNacionalidad.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "N", "E" }));
        cmbNacionalidad.setPreferredSize(new java.awt.Dimension(260, 23));

        cmbTipoLlanta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbTipoLlanta.setPreferredSize(new java.awt.Dimension(260, 23));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Nacionalidad");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel25.setText("Fecha Exp SOAT");

        dprVencSoat.setOpaque(true);
        dprVencSoat.setPreferredSize(new java.awt.Dimension(260, 23));

        txtNumSoat.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNumSoat.setName("Num de SOAT"); // NOI18N
        txtNumSoat.setPreferredSize(new java.awt.Dimension(260, 23));

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setText("Num de SOAT");

        cmbAseguradora.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbAseguradora.setPreferredSize(new java.awt.Dimension(260, 23));

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel28.setText("Número de pasajeros (sin incluir el conductor)");

        dprFechaMatricula.setOpaque(true);
        dprFechaMatricula.setPreferredSize(new java.awt.Dimension(260, 23));

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel27.setText("Fecha Matricula");

        dprExpSoat.setOpaque(true);
        dprExpSoat.setPreferredSize(new java.awt.Dimension(260, 23));
        dprExpSoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dprExpSoatActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel26.setText("Fecha Venc SOAT");

        txtNumSerie.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNumSerie.setName("Num de Serie"); // NOI18N
        txtNumSerie.setPreferredSize(new java.awt.Dimension(260, 23));
        txtNumSerie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumSerieActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("Num de Serie");

        txtNumMotor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNumMotor.setName("Num del Motor"); // NOI18N
        txtNumMotor.setPreferredSize(new java.awt.Dimension(260, 23));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("Num del Motor");

        txtNumLicencia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNumLicencia.setName("Num de Licencia"); // NOI18N
        txtNumLicencia.setPreferredSize(new java.awt.Dimension(260, 23));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setText("Num de Licencia");

        cmbCombustible.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbCombustible.setPreferredSize(new java.awt.Dimension(260, 23));

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Busq. Color");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel23.setText("Aseguradora");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel21.setText("Combustible");

        txtColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColorActionPerformed(evt);
            }
        });
        txtColor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtColorKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtColorKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtColorKeyTyped(evt);
            }
        });

        cmbColor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbColor.setPreferredSize(new java.awt.Dimension(260, 23));
        cmbColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbColorActionPerformed(evt);
            }
        });
        cmbColor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cmbColorKeyTyped(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel53.setText("Escoja Color ");

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel57.setText("Fecha Vencimiento Gnv");

        VencGnv.setOpaque(true);
        VencGnv.setPreferredSize(new java.awt.Dimension(260, 23));
        VencGnv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VencGnvActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Conversión GNV", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel1.setPreferredSize(new java.awt.Dimension(163, 48));

        ConversionGNV.add(congnvsi);
        congnvsi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        congnvsi.setText("SI");
        congnvsi.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                congnvsiStateChanged(evt);
            }
        });

        ConversionGNV.add(congnvno);
        congnvno.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        congnvno.setText("NO");

        ConversionGNV.add(congnvnoaplica);
        congnvnoaplica.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        congnvnoaplica.setText("NA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(congnvsi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(congnvno)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(congnvnoaplica)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(congnvsi)
                    .addComponent(congnvno)
                    .addComponent(congnvnoaplica))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        lblCodInterno.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCodInterno.setText("Cdg Interno Vehiculo");

        txtCodInterno.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCodInterno.setName(""); // NOI18N

        javax.swing.GroupLayout pnlDerechoLayout = new javax.swing.GroupLayout(pnlDerecho);
        pnlDerecho.setLayout(pnlDerechoLayout);
        pnlDerechoLayout.setHorizontalGroup(
            pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDerechoLayout.createSequentialGroup()
                .addContainerGap(127, Short.MAX_VALUE)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDerechoLayout.createSequentialGroup()
                        .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel57)
                            .addComponent(jLabel23)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26)
                            .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel24)
                            .addComponent(lblCodInterno)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21))
                        .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlDerechoLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cmbCombustible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtNumSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNumLicencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNumMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlDerechoLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cmbAseguradora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtNumSoat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dprFechaMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnlDerechoLayout.createSequentialGroup()
                                            .addGap(1, 1, 1)
                                            .addComponent(cmbTipoLlanta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(VencGnv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(dprExpSoat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(dprVencSoat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(txtCodInterno, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDerechoLayout.createSequentialGroup()
                        .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlDerechoLayout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(18, 18, 18)
                                .addComponent(cmbNacionalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDerechoLayout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sprSillas, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addGap(22, 22, 22))
        );
        pnlDerechoLayout.setVerticalGroup(
            pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDerechoLayout.createSequentialGroup()
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDerechoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(pnlDerechoLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(cmbNacionalidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(sprSillas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(cmbTipoLlanta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtNumMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtNumLicencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtNumSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCombustible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtColor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(cmbAseguradora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtNumSoat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(dprExpSoat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(dprVencSoat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(dprFechaMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(VencGnv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addGroup(pnlDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodInterno)
                    .addComponent(txtCodInterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        javax.swing.GroupLayout pnlVehiculoLayout = new javax.swing.GroupLayout(pnlVehiculo);
        pnlVehiculo.setLayout(pnlVehiculoLayout);
        pnlVehiculoLayout.setHorizontalGroup(
            pnlVehiculoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVehiculoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIzquierdo, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(pnlDerecho, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlVehiculoLayout.setVerticalGroup(
            pnlVehiculoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVehiculoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIzquierdo, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE))
            .addGroup(pnlVehiculoLayout.createSequentialGroup()
                .addComponent(pnlDerecho, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 25, Short.MAX_VALUE))
        );

        pnlRevisiones.addTab("Vehiculo", pnlVehiculo);

        pnlHojaPruebas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hojas de Pruebas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        pnlHojaPruebas.setPreferredSize(new java.awt.Dimension(800, 126));

        tblHojaPruebas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero", "Fecha", "Estado", "Preventiva"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHojaPruebas.setDragEnabled(true);
        tblHojaPruebas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblHojaPruebas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHojaPruebasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHojaPruebas);

        btnCancelarHojaPruebas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCancelarHojaPruebas.setText("Cancelar");
        btnCancelarHojaPruebas.setPreferredSize(new java.awt.Dimension(104, 23));
        btnCancelarHojaPruebas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarHojaPruebasActionPerformed(evt);
            }
        });

        btnHojaPruebas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHojaPruebas.setText("Nueva");
        btnHojaPruebas.setPreferredSize(new java.awt.Dimension(104, 23));
        btnHojaPruebas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHojaPruebasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHojaPruebasLayout = new javax.swing.GroupLayout(pnlHojaPruebas);
        pnlHojaPruebas.setLayout(pnlHojaPruebasLayout);
        pnlHojaPruebasLayout.setHorizontalGroup(
            pnlHojaPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHojaPruebasLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlHojaPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHojaPruebas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelarHojaPruebas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        pnlHojaPruebasLayout.setVerticalGroup(
            pnlHojaPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(pnlHojaPruebasLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(btnHojaPruebas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCancelarHojaPruebas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 52, Short.MAX_VALUE))
        );

        pnlPruebas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pruebas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        pnlPruebas.setPreferredSize(new java.awt.Dimension(800, 126));

        tblPruebas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Prueba", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPruebas.setDragEnabled(true);
        tblPruebas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPruebas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                tblPruebaValueChanged(event);
            }
        });
        jScrollPane3.setViewportView(tblPruebas);

        btnDesviacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDesviacion.setText("Desviacion");
        btnDesviacion.setPreferredSize(new java.awt.Dimension(104, 23));
        btnDesviacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesviacionActionPerformed(evt);
            }
        });

        btnFrenos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnFrenos.setText("Frenos");
        btnFrenos.setPreferredSize(new java.awt.Dimension(104, 23));
        btnFrenos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrenosActionPerformed(evt);
            }
        });

        btnSuspension.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSuspension.setText("Suspension");
        btnSuspension.setPreferredSize(new java.awt.Dimension(104, 23));
        btnSuspension.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuspensionActionPerformed(evt);
            }
        });

        btnVisual.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnVisual.setText("I. Visual");
        btnVisual.setPreferredSize(new java.awt.Dimension(104, 23));
        btnVisual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVisualActionPerformed(evt);
            }
        });

        btnTaximetro.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnTaximetro.setText("Taximetro");
        btnTaximetro.setPreferredSize(new java.awt.Dimension(104, 23));
        btnTaximetro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaximetroActionPerformed(evt);
            }
        });

        btnFoto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnFoto.setText("Foto");
        btnFoto.setPreferredSize(new java.awt.Dimension(104, 23));
        btnFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoActionPerformed(evt);
            }
        });

        btnLuces.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnLuces.setText("Luces");
        btnLuces.setPreferredSize(new java.awt.Dimension(104, 23));
        btnLuces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLucesActionPerformed(evt);
            }
        });

        btnGases.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGases.setText("Gases");
        btnGases.setPreferredSize(new java.awt.Dimension(104, 23));
        btnGases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGasesActionPerformed(evt);
            }
        });

        btnRuido.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnRuido.setText("Ruido");
        btnRuido.setPreferredSize(new java.awt.Dimension(104, 23));
        btnRuido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRuidoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPruebasLayout = new javax.swing.GroupLayout(pnlPruebas);
        pnlPruebas.setLayout(pnlPruebasLayout);
        pnlPruebasLayout.setHorizontalGroup(
            pnlPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPruebasLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 821, Short.MAX_VALUE)
                .addGroup(pnlPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(pnlPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(pnlPruebasLayout.createSequentialGroup()
                                                    .addGap(31, 31, 31)
                                                    .addComponent(btnTaximetro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPruebasLayout.createSequentialGroup()
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(btnGases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPruebasLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnFrenos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPruebasLayout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btnSuspension, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPruebasLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnDesviacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPruebasLayout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnRuido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPruebasLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPruebasLayout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnLuces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPruebasLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnVisual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlPruebasLayout.setVerticalGroup(
            pnlPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPruebasLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
            .addGroup(pnlPruebasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVisual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLuces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRuido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDesviacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSuspension, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnFrenos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTaximetro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        btnQuitar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnQuitar.setForeground(new java.awt.Color(255, 51, 51));
        btnQuitar.setText("REPETIR");
        btnQuitar.setToolTipText("");
        btnQuitar.setPreferredSize(new java.awt.Dimension(104, 23));
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });

        btnSugerir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSugerir.setForeground(new java.awt.Color(0, 102, 255));
        btnSugerir.setText("SUGERIR");
        btnSugerir.setPreferredSize(new java.awt.Dimension(104, 23));
        btnSugerir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSugerirActionPerformed(evt);
            }
        });

        btnDerogar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDerogar.setForeground(new java.awt.Color(255, 51, 51));
        btnDerogar.setText("QUITAR");
        btnDerogar.setToolTipText("");
        btnDerogar.setPreferredSize(new java.awt.Dimension(104, 23));
        btnDerogar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDerogarActionPerformed(evt);
            }
        });

        lblCodInterno2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCodInterno2.setText("Director Tecnico (Quien Firma)");

        btnReinspeccion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnReinspeccion.setText("REINSPECCION");
        btnReinspeccion.setName(""); // NOI18N
        btnReinspeccion.setPreferredSize(new java.awt.Dimension(104, 23));
        btnReinspeccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReinspeccionActionPerformed(evt);
            }
        });

        btnAnular1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAnular1.setText("Cmb. Edo. SICOV");
        btnAnular1.setName(""); // NOI18N
        btnAnular1.setPreferredSize(new java.awt.Dimension(104, 23));
        btnAnular1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnular1ActionPerformed(evt);
            }
        });

        btnEditarPlaca.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEditarPlaca.setForeground(new java.awt.Color(204, 0, 0));
        btnEditarPlaca.setText("Otras Funciones");
        btnEditarPlaca.setName(""); // NOI18N
        btnEditarPlaca.setPreferredSize(new java.awt.Dimension(104, 23));
        btnEditarPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarPlacaActionPerformed(evt);
            }
        });

        btnHabilitar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHabilitar.setForeground(new java.awt.Color(204, 0, 0));
        btnHabilitar.setText("Habilitar Pruebas");
        btnHabilitar.setName(""); // NOI18N
        btnHabilitar.setPreferredSize(new java.awt.Dimension(104, 23));
        btnHabilitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHabilitarActionPerformed(evt);
            }
        });

        btnAnular.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAnular.setForeground(new java.awt.Color(204, 0, 0));
        btnAnular.setText("ANULAR RTM");
        btnAnular.setName(""); // NOI18N
        btnAnular.setPreferredSize(new java.awt.Dimension(104, 23));
        btnAnular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnularActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRegistroPruebasLayout = new javax.swing.GroupLayout(pnlRegistroPruebas);
        pnlRegistroPruebas.setLayout(pnlRegistroPruebasLayout);
        pnlRegistroPruebasLayout.setHorizontalGroup(
            pnlRegistroPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegistroPruebasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlRegistroPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRegistroPruebasLayout.createSequentialGroup()
                        .addGroup(pnlRegistroPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlPruebas, javax.swing.GroupLayout.PREFERRED_SIZE, 972, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlRegistroPruebasLayout.createSequentialGroup()
                                .addComponent(pnlHojaPruebas, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlRegistroPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCodInterno2, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbDtResp, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlRegistroPruebasLayout.createSequentialGroup()
                        .addComponent(btnSugerir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDerogar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnReinspeccion, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAnular1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAnular, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditarPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnHabilitar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))))
        );
        pnlRegistroPruebasLayout.setVerticalGroup(
            pnlRegistroPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegistroPruebasLayout.createSequentialGroup()
                .addGroup(pnlRegistroPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRegistroPruebasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlHojaPruebas, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRegistroPruebasLayout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(lblCodInterno2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbDtResp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(pnlRegistroPruebasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSugerir, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDerogar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReinspeccion, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnular1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditarPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHabilitar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAnular, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlPruebas, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlRevisiones.addTab("Pruebas", pnlRegistroPruebas);

        pnlConductor.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Conductor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        txtNombresC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNombresC.setName("Nombres"); // NOI18N
        txtNombresC.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel29.setText("Tipo Identificacion");

        cmbTipoIdentificacionC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbTipoIdentificacionC.setModel(new DefaultComboBoxModel<>(TipoIdentificacion.values()));
        cmbTipoIdentificacionC.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel30.setText("Nombres");

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel31.setText("Apellidos");

        txtApellidosC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtApellidosC.setName("Apellidos"); // NOI18N
        txtApellidosC.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel32.setText("Identificacion");

        txtIdentificacionC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtIdentificacionC.setName("Identificacion"); // NOI18N
        txtIdentificacionC.setPreferredSize(new java.awt.Dimension(198, 23));
        txtIdentificacionC.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdentificacionCFocusLost(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel33.setText("Direccion");

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel34.setText("Telefono");

        txtTelefonoC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTelefonoC.setName("Telefono"); // NOI18N
        txtTelefonoC.setPreferredSize(new java.awt.Dimension(198, 23));

        txtDireccionC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtDireccionC.setName("Direccion"); // NOI18N
        txtDireccionC.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel35.setText("E-mail");

        txtEmailC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtEmailC.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel36.setText("Departamento");

        cmbDepartamentoC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbDepartamentoC.setPreferredSize(new java.awt.Dimension(198, 23));
        cmbDepartamentoC.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDepartamentoCItemStateChanged(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel37.setText("Ciudad");

        cmbCiudadC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbCiudadC.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel38.setText("Tipo Licencia");

        cmbTipoLicenciaC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbTipoLicenciaC.setModel(new DefaultComboBoxModel<>(TipoLicencia.values()));
        cmbTipoLicenciaC.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel39.setText("Licencia");

        txtLicenciaC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtLicenciaC.setName("Licencia"); // NOI18N
        txtLicenciaC.setPreferredSize(new java.awt.Dimension(198, 23));

        txtCelularC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCelularC.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel52.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel52.setText("Celular");

        javax.swing.GroupLayout pnlConductorLayout = new javax.swing.GroupLayout(pnlConductor);
        pnlConductor.setLayout(pnlConductorLayout);
        pnlConductorLayout.setHorizontalGroup(
            pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConductorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addGroup(pnlConductorLayout.createSequentialGroup()
                        .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30)
                            .addComponent(jLabel33)
                            .addComponent(jLabel38))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombresC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbTipoIdentificacionC, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDireccionC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbDepartamentoC, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbTipoLicenciaC, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlConductorLayout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addGap(79, 79, 79)
                        .addComponent(txtCelularC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlConductorLayout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtEmailC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlConductorLayout.createSequentialGroup()
                        .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(jLabel32)
                            .addComponent(jLabel31)
                            .addComponent(jLabel39)
                            .addComponent(jLabel37))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCiudadC, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtApellidosC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefonoC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdentificacionC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLicenciaC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlConductorLayout.setVerticalGroup(
            pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConductorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlConductorLayout.createSequentialGroup()
                        .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(txtIdentificacionC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(txtApellidosC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(txtTelefonoC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52)
                            .addComponent(txtCelularC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlConductorLayout.createSequentialGroup()
                        .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(cmbTipoIdentificacionC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombresC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDireccionC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel33))
                    .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtEmailC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel35)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlConductorLayout.createSequentialGroup()
                        .addComponent(cmbCiudadC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(txtLicenciaC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlConductorLayout.createSequentialGroup()
                        .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(cmbDepartamentoC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlConductorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38)
                            .addComponent(cmbTipoLicenciaC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlPropietario.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Propietario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        txtNombresP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNombresP.setName("Nombres"); // NOI18N
        txtNombresP.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel40.setText("Tipo Identificacion");

        cmbTipoIdentificacionP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbTipoIdentificacionP.setModel(new DefaultComboBoxModel<>(TipoIdentificacion.values()));
        cmbTipoIdentificacionP.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel41.setText("Nombres");

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel42.setText("Apellidos");

        txtApellidosP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtApellidosP.setName("Apellidos"); // NOI18N
        txtApellidosP.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel43.setText("Identificacion");

        txtIdentificacionP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtIdentificacionP.setName("Identificacion"); // NOI18N
        txtIdentificacionP.setPreferredSize(new java.awt.Dimension(198, 23));
        txtIdentificacionP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdentificacionPFocusLost(evt);
            }
        });
        txtIdentificacionP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdentificacionPActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel44.setText("Direccion");

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel45.setText("Telefono");

        txtTelefonoP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTelefonoP.setName("Telefono"); // NOI18N
        txtTelefonoP.setPreferredSize(new java.awt.Dimension(198, 23));

        txtDireccionP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtDireccionP.setName("Direccion"); // NOI18N
        txtDireccionP.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel46.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel46.setText("E-mail");

        txtEmailP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtEmailP.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel47.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel47.setText("Departamento");

        cmbDepartamentoP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbDepartamentoP.setPreferredSize(new java.awt.Dimension(198, 23));
        cmbDepartamentoP.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDepartamentoPItemStateChanged(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel48.setText("Ciudad");

        cmbCiudadP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbCiudadP.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel49.setText("Tipo Licencia");

        cmbTipoLicenciaP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbTipoLicenciaP.setModel(new DefaultComboBoxModel<>(TipoLicencia.values()));
        cmbTipoLicenciaP.setPreferredSize(new java.awt.Dimension(198, 23));

        jLabel50.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel50.setText("Licencia");

        txtLicenciaP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtLicenciaP.setName("Licencia"); // NOI18N
        txtLicenciaP.setPreferredSize(new java.awt.Dimension(198, 23));

        chbConductorPropietario.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        chbConductorPropietario.setText("Conductor y Propietario son la misma persona");
        chbConductorPropietario.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbConductorPropietarioItemStateChanged(evt);
            }
        });
        chbConductorPropietario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbConductorPropietarioActionPerformed(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel51.setText("Celular");

        txtCelularP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCelularP.setPreferredSize(new java.awt.Dimension(198, 23));

        javax.swing.GroupLayout pnlPropietarioLayout = new javax.swing.GroupLayout(pnlPropietario);
        pnlPropietario.setLayout(pnlPropietarioLayout);
        pnlPropietarioLayout.setHorizontalGroup(
            pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPropietarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPropietarioLayout.createSequentialGroup()
                        .addComponent(chbConductorPropietario)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlPropietarioLayout.createSequentialGroup()
                        .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlPropietarioLayout.createSequentialGroup()
                                .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel40)
                                    .addComponent(jLabel41)
                                    .addComponent(jLabel49))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlPropietarioLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtNombresP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbTipoIdentificacionP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(cmbTipoLicenciaP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlPropietarioLayout.createSequentialGroup()
                                .addComponent(jLabel44)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtDireccionP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPropietarioLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel51)
                                .addGap(79, 79, 79)
                                .addComponent(txtCelularP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPropietarioLayout.createSequentialGroup()
                                .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlPropietarioLayout.createSequentialGroup()
                                        .addGap(119, 119, 119)
                                        .addComponent(cmbDepartamentoP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel47))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlPropietarioLayout.createSequentialGroup()
                                .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel45)
                                    .addComponent(jLabel43)
                                    .addComponent(jLabel42)
                                    .addComponent(jLabel50)
                                    .addComponent(jLabel48))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtApellidosP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTelefonoP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIdentificacionP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbCiudadP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtLicenciaP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnlPropietarioLayout.createSequentialGroup()
                                .addComponent(jLabel46)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtEmailP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        pnlPropietarioLayout.setVerticalGroup(
            pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPropietarioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPropietarioLayout.createSequentialGroup()
                        .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40)
                            .addComponent(cmbTipoIdentificacionP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombresP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41))
                        .addGap(45, 45, 45)
                        .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmailP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46)
                            .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtDireccionP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel44))))
                    .addGroup(pnlPropietarioLayout.createSequentialGroup()
                        .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43)
                            .addComponent(txtIdentificacionP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(txtApellidosP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(txtTelefonoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCelularP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel51))
                        .addGap(45, 45, 45)
                        .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel48)
                            .addComponent(cmbCiudadP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47)
                            .addComponent(cmbDepartamentoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel50)
                            .addComponent(txtLicenciaP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel49)
                            .addComponent(cmbTipoLicenciaP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(chbConductorPropietario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlRegistroPropietarioLayout = new javax.swing.GroupLayout(pnlRegistroPropietario);
        pnlRegistroPropietario.setLayout(pnlRegistroPropietarioLayout);
        pnlRegistroPropietarioLayout.setHorizontalGroup(
            pnlRegistroPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRegistroPropietarioLayout.createSequentialGroup()
                .addContainerGap(183, Short.MAX_VALUE)
                .addGroup(pnlRegistroPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlPropietario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlConductor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(182, Short.MAX_VALUE))
        );
        pnlRegistroPropietarioLayout.setVerticalGroup(
            pnlRegistroPropietarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRegistroPropietarioLayout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addComponent(pnlPropietario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlConductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        pnlRevisiones.addTab("Propietario - Conductor", pnlRegistroPropietario);

        btnGuardar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlRevisiones, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(388, 388, 388)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlRevisiones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(23, 23, 23))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbDepartamentoCItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDepartamentoCItemStateChanged
        modeloCiudadC.setBackList(((Departamento) cmbDepartamentoC.getSelectedItem()).getCiudades());
    }//GEN-LAST:event_cmbDepartamentoCItemStateChanged

    private void cmbDepartamentoPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDepartamentoPItemStateChanged
        modeloCiudadP.setBackList(((Departamento) cmbDepartamentoP.getSelectedItem()).getCiudades());
    }//GEN-LAST:event_cmbDepartamentoPItemStateChanged

    private void chbConductorPropietarioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbConductorPropietarioItemStateChanged
        if (chbConductorPropietario.isSelected()) {
            pnlConductor.setVisible(false);
            conductorActual = propietarioActual;
        } else {
            pnlConductor.setVisible(true);
            Validaciones.limpiarCampos(pnlConductor);

            //Se valida por si se cambia el propietario, y asi poder mantener
            //el conductor pero si no se cambia el propietario el conductor sera null
            if (conductorActual == propietarioActual) {
                conductorActual = null;
            }
        }
        //Se carga el conductor pero no se valida ya que dentro de la validacion
        //se genera de nuevo el evento del CheckBox
        datosConductor(conductorActual, Boolean.FALSE);
    }//GEN-LAST:event_chbConductorPropietarioItemStateChanged

    private void btnHojaPruebasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHojaPruebasActionPerformed

        if (btnHojaPruebas.getText().equals("Nueva")) {
            hojaPruebasActual = null;
            btnHojaPruebas.setText("Guardar");
            // chbPreventiva.setEnabled(true);
            btnCancelarHojaPruebas.setEnabled(true);
            chbConductorPropietario.setSelected(true);
        } else {
            try {
                int lastPos = 0;
                if (modeloHojaPruebas != null) {
                    if (modeloHojaPruebas.getListaHojaPruebas() != null) {
                        lastPos = modeloHojaPruebas.getListaHojaPruebas().size();
                    }
                }
                int seleccion = JOptionPane.showOptionDialog(null, "¿Este Registro es aplicado a una Revision TecnoMecanica ?",
                        "Seleccion de Tipo Revision", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (lastPos > 0) {
                    HojaPruebas ctxHojaPrueba = modeloHojaPruebas.getListaHojaPruebas().get(lastPos - 1);
                    System.out.println("NUMERO DE LA HOJA DE PRUEBA:" + ctxHojaPrueba.getCon_hoja_prueba());
                    System.out.println("estado de la prueba finalziadas? :" + ctxHojaPrueba.getFinalizada());
                    hojaPruebasActual = ctxHojaPrueba;
                    if (ctxHojaPrueba.getFinalizada().equalsIgnoreCase("N")) {
                        HojaPruebasJpaController controller = new HojaPruebasJpaController();
                        String estado = controller.verificacionCierreHoja(ctxHojaPrueba);
                        String[] est = estado.split(";");
                        ctxHojaPrueba.setFinalizada(est[0]);
                        ctxHojaPrueba.setAprobado(est[1]);
                        controller.edit(ctxHojaPrueba);
                    }
                    if (seleccion == JOptionPane.YES_OPTION && !ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("NO_APLICA")) {
                        if (ctxHojaPrueba.getFinalizada().equalsIgnoreCase("N") && ctxHojaPrueba.getPreventiva().equalsIgnoreCase("N")) {
                            DateFormat dtfInicial = new SimpleDateFormat("dd/MM/yyyy");
                            JOptionPane.showMessageDialog(null, "Que Pena; No puedo Abrir otra Hoja de Prueba hasta tanto no se finalice la Ultima con fecha: " + dtfInicial.format(ctxHojaPrueba.getFechaIngreso()));
                            return;
                        }
                        if (ctxHojaPrueba.getFinalizada().equalsIgnoreCase("Y") && ctxHojaPrueba.getAprobado().equalsIgnoreCase("N") && ctxHojaPrueba.getPreventiva().equalsIgnoreCase("N")) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(ctxHojaPrueba.getFechaIngreso());
                            calendar.add(Calendar.DAY_OF_YEAR, 15);
                            java.util.Date fecha = new Date();
                            if (calendar.getTime().after(fecha)) {
                                if (!ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("SINCRONIZADO") || !ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("NO_APLICA")) {

                                    JOptionPane.showMessageDialog(null, "Que Pena; No puedo Abrir otra Hoja de Prueba hasta tanto no se FINALIZE el Proceso de Reporte en el SICOV");
                                    return;
                                }
                                if (ctxHojaPrueba.getIntentos() == 1) {
                                    JOptionPane.showMessageDialog(null, "Que Pena; No puedo Abrir otra Hoja de Prueba hasta tanto NO se Reporte la REINSPECCION respectiva de esta Prueba ..! ");
                                    return;
                                }
                            }
                        }
                    } else {

                        if (ctxHojaPrueba.getFinalizada().equalsIgnoreCase("N") && ctxHojaPrueba.getPreventiva().equalsIgnoreCase("N")) {
                            DateFormat dtfInicial = new SimpleDateFormat("dd/MM/yyyy");
                            JOptionPane.showMessageDialog(null, "Que Pena; No puedo Abrir otra Hoja de Prueba hasta tanto no se finalice la Ultima con fecha: " + dtfInicial.format(ctxHojaPrueba.getFechaIngreso()));
                            return;
                        }
                        if (ctxHojaPrueba.getFinalizada().equalsIgnoreCase("Y") && ctxHojaPrueba.getAprobado().equalsIgnoreCase("N") && ctxHojaPrueba.getPreventiva().equalsIgnoreCase("N")) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(ctxHojaPrueba.getFechaIngreso());
                            calendar.add(Calendar.DAY_OF_YEAR, 15);
                            java.util.Date fecha = new Date();
                            if (calendar.getTime().after(fecha)) {
                                if (!ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("NO_APLICA")) {
                                    JOptionPane.showMessageDialog(null, "Que Pena; No puedo Abrir otra Hoja de Prueba hasta tanto no se FINALIZE el Proceso de Reporte en el SICOV");
                                    return;
                                }
                                if (ctxHojaPrueba.getIntentos() == 1) {
                                    JOptionPane.showMessageDialog(null, "Que Pena; No puedo Abrir otra Hoja de Prueba hasta tanto NO se Reporte la REINSPECCION respectiva de esta Prueba ..! ");
                                    return;
                                }
                            }
                        }

                    }
                }
                btnHojaPruebas.setText("Nueva");
                btnHojaPruebas.setEnabled(false);
                btnCancelarHojaPruebas.setEnabled(false);
                hojaPruebasActual = new HojaPruebas();
                hojaPruebasActual.setUsuario(LoggedUser.getIdUsuario());
                hojaPruebasActual.setFormaMedTemperatura('I');
                HojaPruebasJpaController controller = new HojaPruebasJpaController();
                if (seleccion == JOptionPane.YES_OPTION) {
                    if (!this.ctxCda.getProveedorSicov().equals("NO_APLICA")) {
                        if (this.ctxCda.getProveedorSicov().equals("CI2")) {
                            try {
                                Pin pin = new Pin();
                                CdaJpaController cdaControler = new CdaJpaController();
                                Cda ctxCDA = cdaControler.find(1);
                                pin.setClave(ctxCDA.getPasswordSicov());
                                pin.setP_placa(txtPlaca.getText().trim());
                                pin.setUsuario(ctxCDA.getUsuarioSicov());

                                ClienteCi2 clienteCi2 = new ClienteCi2(ctxCDA.getUrlServicioSicov());
                                RespuestaDTO respuestaDTO = clienteCi2.consultarPinPlaca(pin);
                                System.out.print("\n" + respuestaDTO + "\n");
                                if (respuestaDTO == null) {
                                    Mensajes.mensajeAdvertencia("Disculpe, No he Tenido COMUNICACION con el Servidor CI2 en este momento ..! \n Intente dentro de un Minuto si el problema persiste COMUNIQUESE con la Mesa de Ayuda");
                                    Mensajes.mensajeAdvertencia("Por Favor, se RECOMIENDA NO continuar con el REGISTRO DEL VEHICULO, PARA NO GENERAR INCONSISTENCIAS ..!");
                                    btnHojaPruebas.setEnabled(true);
                                    btnCancelarHojaPruebas.setEnabled(true);
                                    return;
                                }
                                if (respuestaDTO.getCodigoRespuesta().equalsIgnoreCase("2006")) {
                                    String temp = respuestaDTO.getMensajeRespuesta().split("@")[1];
                                    respuestaDTO.setMensajeRespuesta(temp.replace("|TD", ""));
                                    hojaPruebasActual.setPin(respuestaDTO.getMensajeRespuesta());
                                    ctxPing = respuestaDTO.getMensajeRespuesta();
                                    hojaPruebasActual.setEstadoSICOV("Iniciado");

                                } else {
                                    System.out.println("codigo respuesta" + respuestaDTO.getCodigoRespuesta());
                                    System.out.println("Falla en RespuestaDTO !2006" + respuestaDTO.getMensajeRespuesta());
                                    Mensajes.mensajeAdvertencia("Disculpe, No he podido ASOCIAR el Pin debido a :" + respuestaDTO.getMensajeRespuesta());
                                    btnHojaPruebas.setEnabled(true);
                                    btnCancelarHojaPruebas.setEnabled(true);
                                    return;
                                }
                                pin.setP_pin(respuestaDTO.getMensajeRespuesta());
                                pin.setP_tipo_rtm("1");
                                respuestaDTO = clienteCi2.utilizarPin(pin);
                                if (!respuestaDTO.getCodigoRespuesta().equals("0000")) { //ok
                                    Mensajes.mensajeAdvertencia("Disculpe, No he podido iniciar el Pin debido a :" + respuestaDTO.getMensajeRespuesta());
                                    btnHojaPruebas.setEnabled(true);
                                    btnCancelarHojaPruebas.setEnabled(true);
                                    return;
                                } else {
                                    Mensajes.mensajeCorrecto("Pin Iniciado con EXITO ..¡ ");
                                }
                            } catch (Throwable ne) {
                                btnHojaPruebas.setEnabled(true);
                                btnCancelarHojaPruebas.setEnabled(true);
                            }
                        }//Validacion de Proveedor para la busqueda del Pin
                        if (this.ctxCda.getProveedorSicov().equals("INDRA")) {
                            hojaPruebasActual.setEstadoSICOV("Iniciado");
                        }
                    } else {
                        hojaPruebasActual.setEstadoSICOV("NO_APLICA");
                    }
                    hojaPruebasActual.setPreventiva("N");

                } else {
                    hojaPruebasActual.setPreventiva("Y");
                    hojaPruebasActual.setEstadoSICOV("NO_APLICA");
                    if (controller.BuscarMaximo_preventiva() == null) {
                        hojaPruebasActual.setCon_preventiva(1);
                    } else {
                        hojaPruebasActual.setCon_preventiva((controller.BuscarMaximo_preventiva()) + 1);
                    }
                }// fIN DE  DE TIPO DE REVISION SI ES TECNOMECANICA O PREVENTIVA
                HojaPruebasJpaController hojaPruebasJPA = new HojaPruebasJpaController();
                // hojaPruebasJPA.create(hojaPruebasActual);
                hojaPruebasActual.setListPruebas(new ArrayList<Prueba>());
                if (modeloHojaPruebas == null) {
                    datosHojaPruebas(new ArrayList<HojaPruebas>());
                }
                modeloHojaPruebas.getListaHojaPruebas().add(hojaPruebasActual);
                modeloHojaPruebas.fireTableDataChanged();
                //chbPreventiva.setSelected(false);
                selectedRow = modeloHojaPruebas.getRowCount() - 1;
                try {
                    tblHojaPruebas.setRowSelectionInterval(selectedRow, selectedRow);
                } catch (Exception x) {

                }
                if (modeloPruebas == null) {
                    datosPruebas(new ArrayList<Prueba>());
                }
                modeloPruebas.setListPruebas(hojaPruebasActual.getListPruebas());
                verificarHojaPruebas();
                btnSugerir.setEnabled(true);
                btnSugerir.doClick();
            } catch (Exception ex) {
                Logger.getLogger(PanelRevisiones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnHojaPruebasActionPerformed

    private void btnCancelarHojaPruebasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarHojaPruebasActionPerformed
        btnHojaPruebas.setText("Nueva");
        btnCancelarHojaPruebas.setEnabled(false);
    }//GEN-LAST:event_btnCancelarHojaPruebasActionPerformed

    private void btnSugerirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSugerirActionPerformed
        if (hojaPruebasActual.getId() != null && this.btnReinspeccion.isEnabled() == true) {
            List<Prueba> pruebas = pruebasJPA.findUltimasPruebasByHoja(hojaPruebasActual.getId());
            for (Prueba prueba : pruebas) {
                if (prueba.getFinalizada().equals("Y") && prueba.getAprobado().equals("N") && prueba.getAbortado().equals("N")) {
                    switch (prueba.getTipoPrueba().getId()) {
                        case 2:
                            btnLuces.setEnabled(true);
                            break;
                        case 4:
                            btnDesviacion.setEnabled(true);
                            break;
                        case 5:
                            btnFrenos.setEnabled(true);
                            break;
                        case 6:
                            btnSuspension.setEnabled(true);
                            break;
                        case 7:
                            btnRuido.setEnabled(true);
                            break;
                        case 8:
                            btnGases.setEnabled(true);
                            break;
                        case 9:
                            btnTaximetro.setEnabled(true);
                            break;
                    }
                }
            }
        }
        if (btnVisual.isEnabled()) {
            try {
                btnVisual.doClick();
            } catch (Throwable e) {

            }
        }

        if (btnLuces.isEnabled()) {
            btnLuces.doClick();
        }

        if (btnFoto.isEnabled()) {
            btnFoto.doClick();
        }

        if (btnRuido.isEnabled()) {
            btnRuido.doClick();
        }
//cmbCombustible.getSelectedItem().toString().equalsIgnoreCase("GAS NATURAL VEHICULAR") || cmbCombustible.getSelectedItem().toString().equalsIgnoreCase("GLP") || 
        if (cmbCombustible.getSelectedItem().toString().equalsIgnoreCase("HIDROGENO") || cmbCombustible.getSelectedItem().toString().equalsIgnoreCase("ELECTRICO")) {
            btnGases.setEnabled(false);
        } else {
            if (btnGases.isEnabled()) {
                btnGases.doClick();
            }
        }

        if (btnFrenos.isEnabled()) {
            btnFrenos.doClick();
        }

        if (cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Liviano") || cmbTipo.getSelectedItem().toString().equalsIgnoreCase("4x4") || cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Taxis_AplTaximetro") || cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Taxis")) {
            if (btnDesviacion.isEnabled()) {
                btnDesviacion.doClick();
            }

            if (btnSuspension.isEnabled()) {
                btnSuspension.doClick();
            }
        } else if (cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Pesado")) {
            if (btnDesviacion.isEnabled()) {
                btnDesviacion.doClick();
            }
        }
        if (cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Taxis_AplTaximetro")) {
            if (this.btnTaximetro.isEnabled()) {
                btnTaximetro.doClick();
            }
        }

        btnSugerir.setEnabled(false);

    }//GEN-LAST:event_btnSugerirActionPerformed

    private void btnDesviacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesviacionActionPerformed
        if (cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Taxis_AplTaximetro") || cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Liviano") || cmbTipo.getSelectedItem().toString().equalsIgnoreCase("4x4") || cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Taxis") || cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Pesado")) {
            if (hojaPruebasActual.getId() == null) {
                addPrueba(4, true);
                btnDesviacion.setEnabled(false);
                return;
            }
            List<Prueba> pruebas = pruebasJPA.findUltimasPruebasByHoja(hojaPruebasActual.getId());
            boolean encontre = false;
            for (Prueba prueba : pruebas) {
                if (prueba.getTipoPrueba().getId() == 4) {
                    encontre = true;
                }
            }
            if (encontre == false) {
                addPrueba(4, true);
                btnDesviacion.setEnabled(false);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Disculpe; la Pueba de Desviacion no esta AUTORIZA para este tipo de vehiculo  .. ¡");
        }
    }//GEN-LAST:event_btnDesviacionActionPerformed

    private void btnFrenosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrenosActionPerformed
        if (hojaPruebasActual.getId() == null) {
            addPrueba(5, true);
            btnFrenos.setEnabled(false);
            return;
        }
        List<Prueba> pruebas = pruebasJPA.findUltimasPruebasByHoja(hojaPruebasActual.getId());
        boolean encontre = false;
        for (Prueba prueba : pruebas) {
            if (prueba.getTipoPrueba().getId() == 5) {
                encontre = true;
            }
        }
        if (encontre == false) {
            addPrueba(5, true);
            btnFrenos.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(null, "Disculpe; la PRUEBA de FRENOS ya se encuentra ASIGNADA a esta Hoja de Prueba ..!");
        }
    }//GEN-LAST:event_btnFrenosActionPerformed

    private void btnSuspensionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuspensionActionPerformed
        if (cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Taxis_AplTaximetro") || cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Liviano") || cmbTipo.getSelectedItem().toString().equalsIgnoreCase("4x4") || cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Taxis")) {
            if (hojaPruebasActual.getId() == null) {
                addPrueba(6, true);
                btnSuspension.setEnabled(false);
                return;
            }
            List<Prueba> pruebas = pruebasJPA.findUltimasPruebasByHoja(hojaPruebasActual.getId());
            boolean encontre = false;
            for (Prueba prueba : pruebas) {
                if (prueba.getTipoPrueba().getId() == 6) {
                    encontre = true;
                }
            }
            if (encontre == false) {
                addPrueba(6, true);
                btnSuspension.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Disculpe; la PRUEBA de SUSPENSION ya se encuentra ASIGNADA a esta Hoja de Prueba ..!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Disculpe; la Pueba de SUSPENSION no esta AUTORIZA para este tipo de vehiculo  .. ¡");
            return;
        }


    }//GEN-LAST:event_btnSuspensionActionPerformed

    private void btnGasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGasesActionPerformed
        if (hojaPruebasActual.getId() == null) {
            addPrueba(8, true);
            btnGases.setEnabled(false);
            return;
        }
        List<Prueba> pruebas = pruebasJPA.findUltimasPruebasByHoja(hojaPruebasActual.getId());
        boolean encontre = false;
        for (Prueba prueba : pruebas) {
            if (prueba.getTipoPrueba().getId() == 8) {
                encontre = true;
            }
        }
        if (encontre == false) {
            addPrueba(8, true);
            btnGases.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(null, "Disculpe; la PRUEBA de GASES ya se encuentra ASIGNADA a esta Hoja de Prueba ..!");
        }
    }//GEN-LAST:event_btnGasesActionPerformed

    private void btnVisualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVisualActionPerformed
        addPrueba(1, true);
        btnVisual.setEnabled(false);
    }//GEN-LAST:event_btnVisualActionPerformed

    private void btnLucesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLucesActionPerformed

        if (hojaPruebasActual.getId() == null) {
            addPrueba(2, true);
            btnLuces.setEnabled(false);
        } else {
            List<Prueba> pruebas = pruebasJPA.findUltimasPruebasByHoja(hojaPruebasActual.getId());
            boolean encontre = false;
            for (Prueba prueba : pruebas) {
                if (prueba.getTipoPrueba().getId() == 2) {
                    encontre = true;
                }
            }
            if (encontre == false) {
                addPrueba(2, true);
                btnLuces.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Disculpe; la PRUEBA de LUCES ya se encuentra ASIGNADA a esta Hoja de Prueba ..!");
            }
        }
    }//GEN-LAST:event_btnLucesActionPerformed

    private void btnRuidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRuidoActionPerformed
        addPrueba(7, true);
        btnRuido.setEnabled(false);
    }//GEN-LAST:event_btnRuidoActionPerformed

    private void btnFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoActionPerformed
        addPrueba(3, true);
        btnFoto.setEnabled(false);
    }//GEN-LAST:event_btnFotoActionPerformed

    private void btnTaximetroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaximetroActionPerformed
        if (cmbTipo.getSelectedItem().toString().equalsIgnoreCase("Taxis_AplTaximetro")) {
            addPrueba(9, true);
            btnTaximetro.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(null, "Disculpe;  para Aplicar a la PRUEBA de TAXIMETRO el tipo de Vehiculo debe de ser \"Aplica_Taximetro\" \n si ese el caso seleccione la opcion ..¡");
        }
    }//GEN-LAST:event_btnTaximetroActionPerformed

    @SuppressWarnings("empty-statement")
    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        int row = tblPruebas.getSelectedRow();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hojaPruebasActual.getFechaIngreso());
        calendar.add(Calendar.DAY_OF_YEAR, 15);
        java.util.Date fecha = new Date();
        if (row >= 0) {
            System.out.println("fecha de la hoja de prueba: " + hojaPruebasActual.getFechaIngreso());
            System.out.println("numero de intentos: " + hojaPruebasActual.getIntentos());
            System.out.println("finalzada? :" + hojaPruebasActual.getFinalizada());
            if ((!calendar.getTime().before(fecha)) && hojaPruebasActual.getFinalizada().equalsIgnoreCase("Y") && hojaPruebasActual.getIntentos() < 2) {
                int choice;
                String[] opciones = {"Repetir prueba", "Crear reinspeccion", "cancelar"};
                choice = JOptionPane.showOptionDialog(this, "Aun no han pasado los 15 dias desde la ultima revision del vehiculo, ¿esta seguro de que no desea realizar una reinspeccion en vez de repetir la prueba?", "confirmacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
                if (choice == 1) {
                    btnReinspeccionActionPerformed(evt);
                    return;
                } else if (choice == 2) {
                    return;
                }
            }
            Prueba prueba = modeloPruebas.getListPruebas().get(tblPruebas.getSelectedRow());
            int cntClv = 1;
            EntityManager em;
            if (prueba.getHojaPruebas().getPreventiva().equalsIgnoreCase("Y")) {
                JOptionPane.showMessageDialog(null, "Disculpe; Solo las pruebas pertencientes a una RTM podran ser repetidas  ");
                return;
            }
            if (prueba.getTipoPrueba().getId() == 3) {
                JOptionPane.showMessageDialog(null, "Disculpe; las Pruebas de Fotos no se pueden ANULAR  ");
                return;
            }
            if (hojaPruebasActual.getEstadoSICOV().equalsIgnoreCase("SINCRONIZADO")) {
                JOptionPane.showMessageDialog(null, "Disculpe, NO SE PUEDE  REPETIR pruebas que ya esten REPORTADAS al SICOV ..! ");
                return;
            }
            if (hojaPruebasActual.getEstadoSICOV().equalsIgnoreCase("Env1FUR")) {
                JOptionPane.showMessageDialog(null, "Disculpe, NO SE PUEDE  REPETIR pruebas que ya esten REPORTADAS al SICOV ..! ");
                return;
            }
            while (true) {
                FrmPassword frmP = new FrmPassword(null, JDialog.ModalityType.MODELESS);
                frmP.setModal(true);
                frmP.setLocationRelativeTo(null);
                frmP.setVisible(true);
                UsuarioJpaController usrCon = new UsuarioJpaController();
                em = usrCon.getEntityManager();
                Boolean paso = UsuarioJpaController.findUsuariosByAdministrador(FrmPassword.parametro, em);
                if (paso == true) {
                    FrmPassword.desistir = false;
                    break;
                } else {
                    if (cntClv == 2) {
                        FrmPassword.desistir = true;
                        FrmPassword.msgApp = "";
                        break;
                    }
                    FrmPassword.msgApp = "La Clave Digitada es Incorrecta";
                    cntClv++;
                }
            }
            FrmPassword.msgApp = "";
            if (FrmPassword.desistir == false) {
                String nombre = UsuarioJpaController.findUsuariosByAdmNombre(FrmPassword.parametro, em);
                if (prueba.getFinalizada().equalsIgnoreCase("Y")) {
                    if (!prueba.getAbortado().equalsIgnoreCase("A")) {
                        Boolean encontre = pruebasJPA.findExisTestAnulada(prueba.getHojaPruebas().getId(), prueba.getTipoPrueba().getId());
                        if (encontre == true) {
                            JOptionPane.showMessageDialog(null, "QUE PENA: Ya no puede REPETIR mas esta Prueba ");
                            return;
                        }
                        if (prueba.getHojaPruebas().getReinspeccionList().size() > 0) {
                            boolean encontrado = false;
                            Reinspeccion r = prueba.getHojaPruebas().getReinspeccionList().iterator().next();
                            List<Prueba> lstpruebas = r.getPruebaList();
                            for (Prueba pr : lstpruebas) {
                                if (pr.getId() == prueba.getId()) {
                                    encontrado = true;
                                }
                            }
                            if (encontrado == false) {
                                JOptionPane.showMessageDialog(null, "Que pena; Usted NO Puede PRETENDER REPETIR una Prueba que pertenece a la Inspección Inicial ");
                                return;
                            }
                        }
                        try {
                            prueba.setAbortado("A");
                            prueba.setFinalizada("A");
                            String obs;
                            if (prueba.getTipoPrueba().getId() == 1) {//-------------------------------------------------------------------------------------------------
                                int pos = prueba.getObservaciones().indexOf("obs");
                                obs = prueba.getObservaciones().substring(pos + 3, prueba.getObservaciones().length());
                            } else {
                                obs = prueba.getObservaciones();
                            }
                            FrmComentario frm = new FrmComentario(null, JDialog.ModalityType.MODELESS, obs);
                            frm.setModal(true);
                            frm.setLocationRelativeTo(null);
                            frm.setVisible(true);
                            prueba.setObservaciones(FrmComentario.obsNulidad);
                            String eve = "";
                            Query q = em.createQuery("SELECT a FROM AuditoriaSicov a WHERE a.idRevision= :idHp AND a.tipoEvento= :idEvento ");
                            q.setParameter("idHp", prueba.getHojaPruebas().getId());
                            q.setParameter("idEvento", prueba.getTipoPrueba().getCodEventoSicov());
                            List<AuditoriaSicov> lstEv = q.getResultList();
                            int posTrama = 0;
                            System.out.println("-------------------" + hojaPruebasActual.getComentario());
                            if (hojaPruebasActual.getComentario() != null) {
                                hojaPruebasActual.setComentario("Prueba de ".concat(prueba.getTipoPrueba().getNombre()).concat(" ANULADA POR ").concat(nombre).concat(" debido a: ").concat(FrmComentario.obsNulidad).concat(";").concat(hojaPruebasActual.getComentario()));
                            } else {
                                hojaPruebasActual.setComentario("Prueba de ".concat(prueba.getTipoPrueba().getNombre()).concat(" ANULADA POR ").concat(nombre).concat(" debido a: ").concat(FrmComentario.obsNulidad).concat(";"));
                            }
//                            if (hojaPruebasActual.getComentario() == null) {
//                                hojaPruebasActual.setComentario("Prueba de ".concat(prueba.getTipoPrueba().getNombre()).concat(" ANULADA POR ").concat(nombre).concat(" debido a: ").concat(FrmComentario.obsNulidad).concat(";"));
//                            } else {
//                                hojaPruebasActual.setComentario("Prueba de ".concat(prueba.getTipoPrueba().getNombre()).concat(" ANULADA POR ").concat(nombre).concat(" debido a: ").concat(FrmComentario.obsNulidad).concat(";").concat(hojaPruebasActual.getComentario()));
//                            }
                            for (AuditoriaSicov auScv : lstEv) {
                                posTrama = auScv.getTRAMA().indexOf("idRegistro");
                                eve = auScv.getTRAMA().substring(posTrama + 13, auScv.getTRAMA().length() - 2);
                                if (prueba.getId() == Integer.parseInt(eve)) {
                                    em.getTransaction().begin();
                                    auScv.setObservacion("Prueba Anulada por: ".concat(FrmComentario.obsNulidad));
                                    em.merge(auScv);
                                    em.getTransaction().commit();
                                }
                            }
                            new PruebaJpaController().edit(prueba);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        //int e = 0;
                        List<Prueba> lstPruebas = pruebasJPA.findUltimasPruebasByHoja(hojaPruebasActual.getId());
                        datosPruebas(lstPruebas);
                        tblPruebas.getColumnModel().getColumn(1).setPreferredWidth(43);
                        tblPruebas.getColumnModel().getColumn(2).setPreferredWidth(65);
                        tblPruebas.getColumnModel().getColumn(3).setPreferredWidth(330);

                        switch (prueba.getTipoPrueba().getId()) {
                            case 1:
                                addPrueba(1, false);
                                break;
                            case 2:
                                addPrueba(2, false);
                                break;
                            case 4:
                                addPrueba(4, false);
                                break;
                            case 5:
                                addPrueba(5, false);
                                break;
                            case 6:
                                addPrueba(6, false);
                                break;
                            case 7:
                                addPrueba(7, false);
                                break;
                            case 8:
                                addPrueba(8, false);
                                break;
                            case 9:
                                addPrueba(9, false);
                                break;
                        }
                        if (vehiculo.getTipoGasolina().getId() == 2 || vehiculo.getTipoGasolina().getId() == 9 || vehiculo.getTipoGasolina().getId() == 6 || vehiculo.getTipoGasolina().getId() == 5) {
                            btnGases.setEnabled(false);
                        }
                        //} OJO  EL DIA 19 DE NOV DEL 2022 SE SACO EL SWITCH DEL CATCH
                        this.pulsoTrans = true;
                        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                        parentFrame.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosing(WindowEvent e) {
                                if (pulsoTrans == true) {
                                    btnGuardar.doClick();
                                    ViewManager.getInstance().showPrincipal();
                                }
                            }
                        });
                    } else {
                        if (prueba.getAbortado().equalsIgnoreCase("A")) {
                            JOptionPane.showMessageDialog(null, "Disculpe; No se Puede Repetir un Prueba si su CONDICION ya es ANULADA ");
                        } else if (prueba.getAbortado().equalsIgnoreCase("Y")) {
                            tblPruebas.getColumnModel().getColumn(1).setPreferredWidth(43);
                            tblPruebas.getColumnModel().getColumn(2).setPreferredWidth(65);
                            tblPruebas.getColumnModel().getColumn(3).setPreferredWidth(330);
                            switch (prueba.getTipoPrueba().getId()) {
                                case 1:
                                    addPrueba(1, false);
                                    break;
                                case 2:
                                    addPrueba(2, false);
                                    break;
                                case 4:
                                    addPrueba(4, false);
                                    break;
                                case 5:
                                    addPrueba(5, false);
                                    break;
                                case 6:
                                    addPrueba(6, false);
                                    break;
                                case 7:
                                    addPrueba(7, false);
                                    break;
                                case 8:
                                    addPrueba(8, false);
                                    break;
                                case 9:
                                    addPrueba(9, false);
                                    break;
                            }
                            if (vehiculo.getTipoGasolina().getId() == 2 || vehiculo.getTipoGasolina().getId() == 9 || vehiculo.getTipoGasolina().getId() == 6 || vehiculo.getTipoGasolina().getId() == 5) {
                                btnGases.setEnabled(false);
                            }
                            this.pulsoTrans = true;
                            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                            parentFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e) {
                                    if (pulsoTrans == true) {
                                        btnGuardar.doClick();
                                        ViewManager.getInstance().showPrincipal();
                                    }
                                }
                            });
                        } else {
                            JOptionPane.showMessageDialog(null, "Disculpe; No se Puede Repetir un Prueba si su CONDICION es PENDIENTE ");
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Disculpe; No ha Seleccionado ninguna Prueba para Repetir ");
            }
    }//GEN-LAST:event_btnQuitarActionPerformed

    }
    private void txtIdentificacionPFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdentificacionPFocusLost
        String identificacion = txtIdentificacionP.getText();

        if (!identificacion.equals("")) {
            Propietario propietario = propietarioJPA.find(Long.parseLong(identificacion));
            datosPropietario(propietario);
        } else {
            txtIdentificacionP.requestFocus();
        }
    }//GEN-LAST:event_txtIdentificacionPFocusLost

    private void txtIdentificacionCFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdentificacionCFocusLost
        String identificacion = txtIdentificacionC.getText();

        if (!identificacion.equals("")) {
            Propietario conductor = propietarioJPA.find(Long.parseLong(identificacion));

            datosConductor(conductor);
        } else {
            txtIdentificacionC.requestFocus();
        }
    }//GEN-LAST:event_txtIdentificacionCFocusLost

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            guardarDatos();
        } catch (Exception ex) {
            Mensajes.mostrarExcepcion(ex);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        /*        if (this.pulsoTrans == true) {
            this.btnGuardar.doClick();
        }*/
        //  Mensajes.mensajeCorrecto("operacion cancelada");
        defaultValues();
        txtPlaca.setText("");
        placaActual = "";
        pnlRevisiones.remove(pnlRegistroPropietario);
        pnlRevisiones.remove(pnlRegistroPruebas);
        ViewManager.getInstance().showPrincipal();
    }//GEN-LAST:event_btnCancelarActionPerformed


    private void btnReinspeccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReinspeccionActionPerformed
        /*  if (hojaPruebasActual.getPreventiva().equalsIgnoreCase("Y")) {
         Mensajes.mensajeAdvertencia("Disculpe, solo las Reinspecciones Aplican a una Revision TecnoMecanica");
         return;
         }*/
        String msgUser = "";
        int Res = JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea crear la reinspeccion para el vehiculo: " + hojaPruebasActual.getVehiculo().getPlaca() + " ?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (Res == JOptionPane.NO_OPTION) {
            return;
        }

        if (!txtKilometraje.getText().equalsIgnoreCase("NO FUNCIONAL")) {
            if (txtKilometraje.getText().equalsIgnoreCase(kilometrajeTemporal)) {
                Mensajes.mensajeAdvertencia("Debe Actualizar Kilometraje o validar funcionamineto del odometro");
                return;
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hojaPruebasActual.getFechaIngreso());
        calendar.add(Calendar.DAY_OF_YEAR, 15);
        java.util.Date fecha = new Date();
        if (calendar.getTime().before(fecha)) {
            JOptionPane.showMessageDialog(null, "Disculpe;  Ha caducado el plazo de 15 dias para la Ejecucion Prueba de Reinspeccion ..¡");
            return;
        }

        if (this.ctxCda.getProveedorSicov().equals("CI2") && !hojaPruebasActual.getPreventiva().equalsIgnoreCase("Y")) {
            try {
                Pin pin = new Pin();
                pin.setClave(this.ctxCda.getPasswordSicov());
                pin.setP_placa(txtPlaca.getText());
                pin.setUsuario(this.ctxCda.getUsuarioSicov());
                ClienteCi2 clienteCi2 = new ClienteCi2(this.ctxCda.getUrlServicioSicov());
                pin.setP_pin(hojaPruebasActual.getPin());
                pin.setP_tipo_rtm("2");
                RespuestaDTO respuestaDTO = clienteCi2.utilizarPin(pin);
                if (respuestaDTO == null) {
                    Mensajes.mensajeAdvertencia("Disculpe, No he tenido COMUNICACION con el Servidor CI2 en este momento ..! \n Intente dentro de un Minuto si el problema persiste COMUNIQUESE con la Mesa de Ayuda");
                    Mensajes.mensajeAdvertencia("Por Favor, se RECOMIENDA NO continuar con el REGISTRO DEL VEHICULO, PARA NO GENERAR INCONSISTENCIAS ..!");
                    return;
                }
                if (respuestaDTO.getCodigoRespuesta().equals("0000")) { //ok
                    Mensajes.mensajeCorrecto(" Inicializado el PIN para Reinspeccion con EXITO ..¡ ");
                } else {
                    Mensajes.mensajeAdvertencia("Disculpe, No he podido iniciar el Pin para Reinspeccion, debido a :" + respuestaDTO.getMensajeRespuesta());
                    //return;
                }
            } catch (Throwable ne) {
            }
        }//Validacion de Proveedor para la busqueda del Pin
        if (hojaPruebasActual.getReinspeccionList().size() == 0) {
            if (!hojaPruebasActual.getEstadoSICOV().equalsIgnoreCase("SINCRONIZADO") && hojaPruebasActual.getPreventiva().equalsIgnoreCase("N")) {
                JOptionPane.showMessageDialog(null, "Disculpe; No se Puede implementar una reinspeccion si la hoja de prueba no se ha CERRADO su proceso con el SICOV ");
                return;
            }
            List<Prueba> pruebas = pruebasJPA.findUltimasPruebasByHoja(hojaPruebasActual.getId());
            for (Prueba p : pruebas) {
                if (p.getAbortado().equalsIgnoreCase("A") || p.getAbortado().equalsIgnoreCase("Y")) {
                    JOptionPane.showMessageDialog(null, "Disculpe; No se Puede implementar una reinspeccion si hay Pruebas en Estado ANULADA o ABORTADA ");
                    return;
                }
            }
            hojaPruebasActual.setFinalizada("N");
            hojaPruebasActual.setEstado("PENDIENTE");
            this.btnQuitar.setEnabled(false);
            this.btnDerogar.setEnabled(false);
            if (this.ctxCda.getProveedorSicov().equals("NO_APLICA")) {
                hojaPruebasActual.setEstadoSICOV("NO_APLICA");
            } else {
                hojaPruebasActual.setEstadoSICOV("INICIADO");
            }
            btnFotoActionPerformed(evt);
            btnVisualActionPerformed(evt);
            reinspeccionActual = new Reinspeccion();
            for (Component boton : pnlPruebas.getComponents()) {
                if (boton instanceof JButton) {
                    boton.setEnabled(false);
                }
            }
            btnSugerir.setEnabled(true);
            int idPruVis = 0;
            for (Prueba prueba : pruebas) {
                if (prueba.getTipoPrueba().getId() == 1 && prueba.getFinalizada().equals("Y")) {
                    idPruVis = pruebas.get(0).getId();
                    break;
                }
            }
            // int nroTest = 0;
            int nroPrueba = 0;

            for (Prueba prueba : pruebas) {
                if (prueba.getTipoPrueba().getId() == 5 && prueba.getAprobado().equals("Y")) {
                    boolean existe = pruebasJPA.findDefecXProfLabrado(idPruVis);
                    if (existe == true) {
                        int opcion = JOptionPane.showOptionDialog(null, " ¿Desea Implementar de Nuevo la Prueba de Frenos?",
                                "Existencia Defecto Labrado", JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "NO");
                        if (opcion == JOptionPane.YES_OPTION) {
                            addPrueba(5, true);
                            profLabFren = true;
                        }
                    }
                }

                if (prueba.getFinalizada().equals("Y") && prueba.getAprobado().equals("N") && prueba.getAbortado().equals("N")) {
                    switch (prueba.getTipoPrueba().getId()) {
                        case 2:
                            btnLuces.setEnabled(false);
                            addPrueba(2, true);
                            nroPrueba++;
                            break;
                        case 4:
                            btnDesviacion.setEnabled(false);
                            addPrueba(4, true);
                            nroPrueba++;
                            break;
                        case 5:
                            btnFrenos.setEnabled(false);
                            addPrueba(5, true);
                            nroPrueba++;
                            break;
                        case 6:
                            btnSuspension.setEnabled(false);
                            addPrueba(6, true);
                            nroPrueba++;
                            break;
                        case 7:
                            btnRuido.setEnabled(false);
                            addPrueba(7, true);
                            nroPrueba++;
                            break;
                        case 8:
                            btnGases.setEnabled(false);
                            addPrueba(8, true);
                            nroPrueba++;
                            break;
                        case 9:
                            btnTaximetro.setEnabled(false);
                            addPrueba(9, true);
                            nroPrueba++;
                            break;
                    }
                }
            }
            this.pulsoTrans = true;
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            try {
                guardarReinspeccion();
                msgUser = "Se ha creado la reinspeccion para el vehiculo con placas :" + hojaPruebasActual.getVehiculo().getPlaca() + " correctamente  ";
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear la reinspeccion", "ERROR", JOptionPane.ERROR_MESSAGE);
                System.out.println("error al crear la reinspeccion: " + ex.getMessage());
                System.out.println(ex);
            }
            parentFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (pulsoTrans == true) {
                        btnGuardar.doClick();
                        ViewManager.getInstance().showPrincipal();
                    }
                }
            });

            btnSugerir.doClick();
        } else {
            JOptionPane.showMessageDialog(null, "Disculpe; No se Puede IMPLEMENTAR una Reinspeccion si LA Hoja de Prueba Ya POSEE UNA REINSPECCION ");
        }
//validacion de que no exita mas de una reinspeccion
        Mensajes.mensajeCorrecto(msgUser);
        pnlRevisiones.remove(pnlRegistroPropietario);
        pnlRevisiones.remove(pnlRegistroPruebas);
    }//GEN-LAST:event_btnReinspeccionActionPerformed

    private void tblHojaPruebasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHojaPruebasMouseClicked
        if (!pruebasTemp.isEmpty() || (hojaPruebasActual != null && hojaPruebasActual.getId() == null && tblHojaPruebas.getSelectedRow() != tblHojaPruebas.getRowCount() - 1)) {
            if (!Mensajes.mensajePregunta("Disculpe; Hay datos sin guardar. ¿Desea continuar?")) {
                tblHojaPruebas.setRowSelectionInterval(selectedRow, selectedRow);
                return;
            }

            //Se verifica que la hoja de pruebas no se encuentra registrada en la base de datos
            if (hojaPruebasActual.getId() == null) {
                modeloHojaPruebas.getListaHojaPruebas().remove(hojaPruebasActual);
                modeloHojaPruebas.fireTableDataChanged();
                btnHojaPruebas.setEnabled(true);
            } else {
                for (int i = 0; i < hojaPruebasActual.getListPruebas().size(); i++) {
                    if (hojaPruebasActual.getListPruebas().get(i).getId() == null) {
                        hojaPruebasActual.getListPruebas().remove(i);
                    }
                }
            }

            pruebasTemp.clear();
            modeloPruebas.setListPruebas(hojaPruebasActual.getListPruebas());
            hojaPruebasActual = null;
        }

        selectedRow = tblHojaPruebas.getSelectedRow();

        if (selectedRow != -1) {
            hojaPruebasActual = modeloHojaPruebas.getListaHojaPruebas().get(selectedRow);

            datosPruebas(hojaPruebasActual.getListPruebas());
            datosConductor(hojaPruebasActual.getConductor());
            verificarHojaPruebas();
        }
    }//GEN-LAST:event_tblHojaPruebasMouseClicked

    private void dprExpSoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dprExpSoatActionPerformed
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dprExpSoat.getDate());
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DATE, -1);
        dprVencSoat.setDate(calendar.getTime());
    }//GEN-LAST:event_dprExpSoatActionPerformed

    private void txtNumSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumSerieActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumSerieActionPerformed

    private void cmbColorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbColorKeyTyped

    }//GEN-LAST:event_cmbColorKeyTyped

    private void cmbColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbColorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbColorActionPerformed

    private void txtColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtColorActionPerformed

    private void txtColorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtColorKeyPressed

    }//GEN-LAST:event_txtColorKeyPressed

    private void txtLineaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLineaKeyPressed

    }//GEN-LAST:event_txtLineaKeyPressed

    private void txtLineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLineaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLineaActionPerformed

    private void cmbMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMarcaActionPerformed

    private void cmbMarcaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMarcaItemStateChanged
        cmbLinea.removeAllItems();
        txtLinea.setText("");
        txtLinea.setFocusable(true);

        //  modeloComboDinamico.setBackList(((Marca) cmbMarca.getSelectedItem()).getLineasVehiculosList());
    }//GEN-LAST:event_cmbMarcaItemStateChanged

    private void cmbTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoItemStateChanged
        blockFields();
        claseByTipoVehiculo();
    }//GEN-LAST:event_cmbTipoItemStateChanged

    private void txtPlacaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaFocusLost
        if (txtPlaca.getText().length() > 4) {
            if (pnlRevisiones.getComponentCount() == 1) {
                pnlRevisiones.addTab("Propietario - Conductor", pnlRegistroPropietario);
                pnlRevisiones.addTab("Pruebas", pnlRegistroPruebas);
                this.btnQuitar.setEnabled(true);
                this.btnDerogar.setEnabled(true);
            }
            //La validacion sirve para que no se ejecute la consulta cuando la
            //placa se consulto y se asigno la informacion obtenida

            if (txtPlaca.getText().equals(placaActual)) {
                return;
                //} else if (hojaPruebasActual != null && !modeloHojaPruebas.getListaHojaPruebas().isEmpty()) {
            } else if (hojaPruebasActual != null && !modeloHojaPruebas.getListaHojaPruebas().isEmpty()) {
                txtPlaca.setText(txtPlaca.getText().toUpperCase());
                modeloHojaPruebas.getListaHojaPruebas().remove(hojaPruebasActual);
                modeloHojaPruebas.fireTableDataChanged();
                pruebasTemp.clear();
                hojaPruebasActual = null;
                btnHojaPruebas.setEnabled(true);
            }

            vehiculo = vehiculoJpa.findVehiculosByPlaca(txtPlaca.getText());
            placaActual = txtPlaca.getText();
            //Se verifica que exista el vehiculo
            if (vehiculo != null) {
                datosVehiculo();
                datosPropietario(vehiculo.getPropietario());
                //Se verifia que tiene hojas de prueba asignadas para cargar la informacion
                if (!vehiculo.getHojaPruebasList().isEmpty()) {
                    datosHojaPruebas(vehiculo.getHojaPruebasList());
                    hojaPruebasActual = modeloHojaPruebas.getListaHojaPruebas().get(modeloHojaPruebas.getListaHojaPruebas().size() - 1);
                    idHojaPrueba = hojaPruebasActual.getId();
                    String kilometrajeCargado = vehiculo.getKilometraje().toString();
//                    String kilometrajeCargado=cargarKilometraje(idHojaPrueba);
//                    kilometrajeTemporal=kilometrajeCargado;
//                    if(kilometrajeCargado == null){
//                        if(vehiculo.getKilometraje() != null){
//                            kilometrajeCargado = vehiculo.getKilometraje().toString();
//                            kilometrajeTemporal = "";
//                        }else{
//                            kilometrajeCargado = "0";
//                        }
//                    }

                    if (kilometrajeCargado.equalsIgnoreCase("0")) {
                        txtKilometraje.setText(kilometrajeCargado);
                        chckKilometrajeFuncional.setSelected(false);
                    } else {
                        txtKilometraje.setText(kilometrajeCargado);
                        chckKilometrajeFuncional.setSelected(true);
                    }
                    verificarHojaPruebas();
                    datosConductor(hojaPruebasActual.getConductor());
                    tblHojaPruebas.setRowSelectionInterval(tblHojaPruebas.getRowCount() - 1, tblHojaPruebas.getRowCount() - 1);
                    datosPruebas(hojaPruebasActual.getListPruebas());
                    datosVehiculoHojaPrueba(hojaPruebasActual);
                    tblPruebas.getColumnModel().getColumn(1).setPreferredWidth(43);
                    tblPruebas.getColumnModel().getColumn(2).setPreferredWidth(65);
                    tblPruebas.getColumnModel().getColumn(3).setPreferredWidth(330);
                    Usuario usuarioResp = new UsuarioJpaController().find(hojaPruebasActual.getResponsable().getUsuario());
                    int t = this.cmbDtResp.getItemCount();
                    for (int i = 0; i < t; i++) {
                        this.cmbDtResp.setSelectedIndex(i);
                        if (this.cmbDtResp.getSelectedItem().toString().equalsIgnoreCase(usuarioResp.getNombre())) {
                            break;
                        }
                    }
                }
            } else {
                HojaPruebasJpaController hojaPruebasJPA = new HojaPruebasJpaController();
                Integer maxHP = hojaPruebasJPA.BuscarMaximoTestSheet();
                HojaPruebas hp = hojaPruebasJPA.find(maxHP);
                Usuario usuarioResp = new UsuarioJpaController().find(hp.getResponsable().getUsuario());
                int t = this.cmbDtResp.getItemCount();
                for (int i = 0; i < t; i++) {
                    this.cmbDtResp.setSelectedIndex(i);
                    if (this.cmbDtResp.getSelectedItem().toString().equalsIgnoreCase(usuarioResp.getNombre())) {
                        break;
                    }
                }
                defaultValues();
                datosHojaPruebas(new ArrayList<HojaPruebas>());
                datosPruebas(new ArrayList<Prueba>());
                txtPlaca.setText(placaActual);
            }

        } else {
            if (txtPlaca.getText().length() > 0) {
                Mensajes.mensajeAdvertencia("Disculpe, La placa debe tener mas de 4 caracteres");
            }
            txtPlaca.requestFocus();
            /*mostrar1 = false;
             txtCodInterno.setVisible(mostrar1);
             lblCodInterno.setVisible(mostrar1);*/
        }
        blockFields();
    }//GEN-LAST:event_txtPlacaFocusLost

    private void cmbClaseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbClaseItemStateChanged

    }//GEN-LAST:event_cmbClaseItemStateChanged

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void txtLineaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLineaKeyTyped

    }//GEN-LAST:event_txtLineaKeyTyped

    private void txtColorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtColorKeyTyped

    }//GEN-LAST:event_txtColorKeyTyped

    private void txtLineaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLineaKeyReleased
        com.soltelec.consolaentrada.models.controllers.LineaVehiculoJpaController lnVehJPA = new com.soltelec.consolaentrada.models.controllers.LineaVehiculoJpaController();
        for (int i = 0; i < cmbLinea.getItemCount(); i++) {
            cmbLinea.removeAllItems();
        }
        Marca marca = (Marca) cmbMarca.getSelectedItem();
        List<LineaVehiculo> lstLnVh = lnVehJPA.findLineaTXT(txtLinea.getText(), marca.getId());
        for (LineaVehiculo co : lstLnVh) {
            cmbLinea.addItem((Object) co);
        }
    }//GEN-LAST:event_txtLineaKeyReleased

    private void txtColorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtColorKeyReleased
        if (txtColor.getText().length() >= 3 && txtColor.getText().length() <= 9) {
            com.soltelec.consolaentrada.models.controllers.ColorJpaController colorJPA = new com.soltelec.consolaentrada.models.controllers.ColorJpaController();
            cmbColor.removeAllItems();
            List<Color> lstColor = colorJPA.findColoresxTXT(txtColor.getText());
            for (Color co : lstColor) {
                cmbColor.addItem((Object) co);
            }
        }
        if (txtColor.getText().length() < 3) {
            cmbColor.removeAllItems();
        }
    }//GEN-LAST:event_txtColorKeyReleased


    private void btnAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnularActionPerformed

        kilometrajeAnulada = true;
        if (hojaPruebasActual.getPreventiva().equalsIgnoreCase("Y")) {
            JOptionPane.showMessageDialog(null, "Disculpe, Solo se puede Anular Hoja de Pruebas  que apliquen a Revision Tecnomecanica ..! ");
            return;
        }
        if (hojaPruebasActual.getEstadoSICOV().equalsIgnoreCase("SINCRONIZADO")) {
            JOptionPane.showMessageDialog(null, "Disculpe, NO SE PUEDE  Anular una Hoja de Prueba SINCRONIZADA ..! ");
            return;
        }
        EntityManager em;
        int cntClv = 1;
        String nombre;
        while (true) {
            FrmPassword frmP = new FrmPassword(null, JDialog.ModalityType.MODELESS);
            frmP.setModal(true);
            frmP.setLocationRelativeTo(null);
            frmP.setVisible(true);
            UsuarioJpaController usrCon = new UsuarioJpaController();
            em = usrCon.getEntityManager();
            nombre = UsuarioJpaController.findUsuariosByAdmNombre(FrmPassword.parametro, em);
            if (nombre.equalsIgnoreCase("0")) {
                if (cntClv == 2) {
                    FrmPassword.desistir = true;
                    FrmPassword.msgApp = "";
                    break;
                }
                FrmPassword.msgApp = "La Clave Digitada es Incorrecta";
                cntClv++;
            } else {
                FrmPassword.desistir = false;
                break;
            }
        }
        if (nombre.equalsIgnoreCase("0")) {
            return;
        }
        if (FrmPassword.desistir == true) {
            return;
        }
        int seleccion = JOptionPane.showOptionDialog(null, "".concat(nombre).concat("; Esta a punto de ANULAR esta HojaPrueba  ¿Esta Seguro que Desea continuar ? "),
                "Confirmacion de Operacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (seleccion == JOptionPane.NO_OPTION) {
            FrmPassword.desistir = false;
            return;
        }
        seleccion = JOptionPane.showOptionDialog(null, "".concat("RECUERDE que Continuar con esta OPERACION no Tiene Vuelta Atras ¿Desea Continuar? "),
                "Confirmacion de Operacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (seleccion == JOptionPane.NO_OPTION) {
            FrmPassword.desistir = false;
            return;
        }
        FrmComentario frm = new FrmComentario(null, JDialog.ModalityType.MODELESS, "");
        frm.setModal(true);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        if (hojaPruebasActual.getReinspeccionList().size() == 0) {
            try {
                hojaPruebasActual.setFinalizada("Y");
                hojaPruebasActual.setEstadoSICOV("NO_APLICA");
                hojaPruebasActual.setEstado("ANULADO");
                hojaPruebasActual.setAnulado("Y");
                if (hojaPruebasActual.getComentario() != null) {
                    hojaPruebasActual.setComentario("Hoja Prueba Anulada POR ".concat(nombre).concat(" debido a: ").concat(FrmComentario.obsNulidad).concat(";").concat(hojaPruebasActual.getComentario()));
                } else {
                    hojaPruebasActual.setComentario("Hoja Prueba Anulada POR ".concat(nombre).concat(" debido a: ").concat(FrmComentario.obsNulidad).concat(";"));
                }
                hojaPruebasJPA.edit(hojaPruebasActual);
                List<Prueba> lstPruebas = hojaPruebasActual.getListPruebas();
                for (Prueba prueba : lstPruebas) {
                    prueba.setAbortado("A");
                    prueba.setFinalizada("A");
                    prueba.setAutorizada("N");
                    prueba.setObservaciones(FrmComentario.obsNulidad);
                    pruebasJPA.edit(prueba);
                    Query q = em.createQuery("SELECT a FROM AuditoriaSicov a WHERE a.idRevision= :idHp AND a.tipoEvento= :idEvento ");
                    q.setParameter("idHp", prueba.getHojaPruebas().getId());
                    q.setParameter("idEvento", prueba.getTipoPrueba().getCodEventoSicov());
                    List<AuditoriaSicov> lstEv = q.getResultList();
                    int posTrama = 0;
                    String idPrueba;
                    for (AuditoriaSicov auScv : lstEv) {
                        posTrama = auScv.getTRAMA().indexOf("idRegistro");
                        idPrueba = auScv.getTRAMA().substring(posTrama + 13, auScv.getTRAMA().length() - 2);
                        if (prueba.getId() == Integer.parseInt(idPrueba)) {
                            em.getTransaction().begin();
                            auScv.setObservacion("Hoja de Prueba Anulada ".concat(FrmComentario.obsNulidad));
                            em.merge(auScv);
                            em.getTransaction().commit();
                        }
                    }
                }
            } catch (Exception ex) {
            }
        } else {
            try {
                Reinspeccion reinpeccion = hojaPruebasActual.getReinspeccionList().iterator().next();
                List<Prueba> lstPruebas = reinpeccion.getPruebaList();
                hojaPruebasActual.setEstadoSICOV("SINCRONIZADO");
                hojaPruebasActual.setFinalizada("Y");
                hojaPruebasActual.setIntentos(1);
                if (hojaPruebasActual.getComentario() != null) {
                    hojaPruebasActual.setComentario("Reinspeccion Anulada POR ".concat(nombre).concat(" debido a ").concat(FrmComentario.obsNulidad).concat(";").concat(hojaPruebasActual.getComentario()));
                } else {
                    hojaPruebasActual.setComentario("Reinspeccion Anulada POR ".concat(nombre).concat(" debido a ").concat(FrmComentario.obsNulidad).concat(";"));
                }
                hojaPruebasJPA.edit(hojaPruebasActual);
                for (Prueba prueba : lstPruebas) {
                    pruebasJPA.destroy(prueba.getId());
                    Query q = em.createQuery("SELECT a FROM AuditoriaSicov a WHERE a.idRevision= :idHp AND a.tipoEvento= :idEvento ");
                    q.setParameter("idHp", prueba.getHojaPruebas().getId());
                    q.setParameter("idEvento", prueba.getTipoPrueba().getCodEventoSicov());
                    List<AuditoriaSicov> lstEv = q.getResultList();
                    int posTrama = 0;
                    String idPrueba;
                    for (AuditoriaSicov auScv : lstEv) {
                        posTrama = auScv.getTRAMA().indexOf("idRegistro");
                        idPrueba = auScv.getTRAMA().substring(posTrama + 13, auScv.getTRAMA().length() - 2);
                        if (prueba.getId() == Integer.parseInt(idPrueba)) {
                            em.getTransaction().begin();
                            auScv.setObservacion("Hoja de Prueba Anulada ".concat(FrmComentario.obsNulidad));
                            em.merge(auScv);
                            em.getTransaction().commit();
                        }
                    }
                }
                em.getTransaction().begin();
                reinpeccion = em.find(Reinspeccion.class, reinpeccion.getId());
                em.remove(reinpeccion);
                em.getTransaction().commit();
            } catch (Exception ex) {
            }
        }
        JOptionPane.showMessageDialog(null, "Se Ha ANULADO la presente Hoja de prueba de una manera Exitosa ..! ");
    }//GEN-LAST:event_btnAnularActionPerformed

    private void btnAnular1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnular1ActionPerformed
        try {
            if (hojaPruebasActual.getPreventiva().equalsIgnoreCase("Y")) {
                JOptionPane.showMessageDialog(null, "Disculpe, Solo se puede CAMBIAR el Edo. SICOV a una Hoja que apliquen a Revision Tecnomecanica ..! ");
                return;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(hojaPruebasActual.getFechaIngreso());
            calendar.add(Calendar.DAY_OF_YEAR, 15);
            java.util.Date fecha = new Date();
            if (calendar.getTime().before(fecha)) {
                JOptionPane.showMessageDialog(null, "Disculpe;  Ha caducado el plazo de 15 dias para el CAMBIO edo. SICOV para esta Revision Tecnomecanica");
                return;
            }
            EntityManager em;
            int cntClv = 1;
            String nombre;
            while (true) {
                FrmPassword frmP = new FrmPassword(null, JDialog.ModalityType.MODELESS);
                frmP.setModal(true);
                frmP.setLocationRelativeTo(null);
                frmP.setVisible(true);
                UsuarioJpaController usrCon = new UsuarioJpaController();
                em = usrCon.getEntityManager();
                nombre = UsuarioJpaController.findUsuariosByAdmNombre(FrmPassword.parametro, em);
                if (nombre.equalsIgnoreCase("0")) {
                    if (cntClv == 2) {
                        FrmPassword.desistir = true;
                        FrmPassword.msgApp = "";
                        break;
                    }
                    FrmPassword.msgApp = "La Clave Digitada es Incorrecta";
                    cntClv++;
                } else {
                    FrmPassword.desistir = false;
                    break;
                }
            }
            if (nombre.equalsIgnoreCase("0")) {
                FrmPassword.desistir = false;
                return;
            }
            int seleccion = JOptionPane.showOptionDialog(null, "".concat(nombre).concat("; Esta a punto de CAMBIAR el Edo. SICOV a INICIADO ¿Esta seguro que desea continuar ? "),
                    "Confirmacion de Operacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (seleccion == JOptionPane.NO_OPTION) {
                FrmPassword.desistir = false;
                return;
            }
            seleccion = JOptionPane.showOptionDialog(null, "".concat("RECUERDE que Continuar con esta OPERACION no Tiene Vuelta Atras ¿Desea Continuar? "),
                    "Confirmacion de Operacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (seleccion == JOptionPane.NO_OPTION) {
                FrmPassword.desistir = false;
                return;
            }
            HojaPruebasJpaController hojaPruebasJPA = new HojaPruebasJpaController();
            hojaPruebasActual.setEstadoSICOV("INICIADO");
            if (hojaPruebasActual.getComentario() != null) {
                hojaPruebasActual.setComentario("CAMBIO ESTADO SICOV POR ".concat(nombre).concat(" a ").concat(FrmFunciones.edoSicov).concat(";").concat(hojaPruebasActual.getComentario()));
            } else {
                hojaPruebasActual.setComentario("CAMBIO ESTADO SICOV POR ".concat(nombre).concat(" a ").concat(FrmFunciones.edoSicov).concat(";"));
            }
            hojaPruebasActual.setFinalizada("N");
            hojaPruebasJPA.edit(hojaPruebasActual);
            JOptionPane.showMessageDialog(null, "He Cambiado El Edo. SICOV a INICIADO para esta Revision Tecnomecanica ..! ");
        } catch (Exception ex) {
        }

    }//GEN-LAST:event_btnAnular1ActionPerformed

    private void btnEditarPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarPlacaActionPerformed
        EntityManager em;
        int cntClv = 1;
        String nombre;
        while (true) {
            FrmPassword frmP = new FrmPassword(null, JDialog.ModalityType.MODELESS);
            frmP.setModal(true);
            frmP.setLocationRelativeTo(null);
            frmP.setVisible(true);
            UsuarioJpaController usrCon = new UsuarioJpaController();
            em = usrCon.getEntityManager();
            nombre = UsuarioJpaController.findUsuariosByAdmNombre(FrmPassword.parametro, em);
            if (nombre.equalsIgnoreCase("0")) {
                if (cntClv == 2) {
                    FrmPassword.desistir = true;
                    FrmPassword.msgApp = "";
                    break;
                }
                FrmPassword.msgApp = "La Clave Digitada es Incorrecta";
                cntClv++;
            } else {
                FrmPassword.desistir = false;
                break;
            }
        }
        if (FrmPassword.desistir == false) {
            FrmFunciones frmP = new FrmFunciones(null, JDialog.ModalityType.MODELESS, hojaPruebasActual, nombre, ctxCda.getProveedorSicov());
            frmP.setModal(true);
            frmP.setLocationRelativeTo(null);
            frmP.setVisible(true);
        }
    }//GEN-LAST:event_btnEditarPlacaActionPerformed

    private void btnHabilitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHabilitarActionPerformed
        if (hojaPruebasActual.getId() != null) {
            btnLuces.setEnabled(true);
            btnDesviacion.setEnabled(true);
            btnFrenos.setEnabled(true);
            btnSuspension.setEnabled(true);
            btnRuido.setEnabled(true);
            btnGases.setEnabled(true);
            btnTaximetro.setEnabled(true);
        }
    }//GEN-LAST:event_btnHabilitarActionPerformed

    private void chbConductorPropietarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbConductorPropietarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chbConductorPropietarioActionPerformed

    private void VencGnvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VencGnvActionPerformed
        Calendar calendar = new GregorianCalendar();
        //calendar.setTime(dprExpSoat.getDate());
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DATE, -1);
        VencGnv.setDate(calendar.getTime());

        /*   Calendar calendar = new GregorianCalendar();
        calendar.setTime(dprExpSoat.getDate());
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DATE, -1);
        dprVencSoat.setDate(calendar.getTime());*/
    }//GEN-LAST:event_VencGnvActionPerformed

    private void rdbBlindajeNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbBlindajeNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdbBlindajeNoActionPerformed

    private void rdbPolarizadosSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbPolarizadosSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdbPolarizadosSiActionPerformed

    private void cmbClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClaseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbClaseActionPerformed

    private void congnvsiStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_congnvsiStateChanged
        if (congnvsi.isSelected()) {
            VencGnv.setEnabled(true);
        } else {
            VencGnv.setEnabled(false);
        }
    }//GEN-LAST:event_congnvsiStateChanged

    private void cmbCarroceriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCarroceriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCarroceriaActionPerformed

    private void cmbCarroceriaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbCarroceriaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCarroceriaKeyTyped

    private void txtPesoBrutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesoBrutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesoBrutoActionPerformed

    private void txtCarroceriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCarroceriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCarroceriaActionPerformed

    private void txtCarroceriaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCarroceriaKeyReleased
        if (txtCarroceria.getText().length() >= 3 && txtCarroceria.getText().length() <= 9) {
            com.soltelec.consolaentrada.models.controllers.CarroceriaJpaController1 carroceriaJPA = new com.soltelec.consolaentrada.models.controllers.CarroceriaJpaController1();
            cmbCarroceria.removeAllItems();
            List<Tipocarroceria> lstCarroceria = carroceriaJPA.findCarroceriaxTXT(txtCarroceria.getText());
            for (Tipocarroceria ca : lstCarroceria) {
                cmbCarroceria.addItem((Object) ca);
            }
        }
        if (txtCarroceria.getText().length() < 3) {
            cmbCarroceria.removeAllItems();
        }
    }//GEN-LAST:event_txtCarroceriaKeyReleased

    private void txtCarroceriaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCarroceriaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCarroceriaKeyPressed

    private void txtCarroceriaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCarroceriaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCarroceriaKeyTyped

    private void chckKilometrajeFuncionalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chckKilometrajeFuncionalStateChanged
//        if (chckKilometrajeFuncional.isSelected())
//        {
//            txtKilometraje.setText("");
//            txtKilometraje.setEnabled(true);
//        } else {
//            txtKilometraje.setEnabled(false);
//            txtKilometraje.setText("NO FUNCIONAL");
//        }
    }//GEN-LAST:event_chckKilometrajeFuncionalStateChanged

    private void chckKilometrajeFuncionalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chckKilometrajeFuncionalActionPerformed

        if (chckKilometrajeFuncional.isSelected()) {
            txtKilometraje.setText("");
            txtKilometraje.setEnabled(true);
        } else {
            txtKilometraje.setText("NO FUNCIONAL");
            txtKilometraje.setEnabled(false);
        }
    }//GEN-LAST:event_chckKilometrajeFuncionalActionPerformed

    private void txtIdentificacionPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdentificacionPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdentificacionPActionPerformed

    private void btnDerogarActionPerformed(java.awt.event.ActionEvent evt) {
        int row = tblPruebas.getSelectedRow();
        if (row >= 0) {
            EntityManager em;
            int cntClv = 1;
            String nombre;
            Prueba prueba = modeloPruebas.getListPruebas().get(tblPruebas.getSelectedRow());
            if (prueba.getTipoPrueba().getId() == 3) {
                JOptionPane.showMessageDialog(null, "Disculpe; las Pruebas de Fotos no se pueden QUITAR  ");
                return;
            }
            if (prueba.getHojaPruebas().getPreventiva().equalsIgnoreCase("N")) {
                JOptionPane.showMessageDialog(null, "Disculpe; esta opcion es SOLO para Revision Preventiva ..! ");
                return;
            }
            if (hojaPruebasActual.getEstadoSICOV().equalsIgnoreCase("SINCRONIZADO")) {
                JOptionPane.showMessageDialog(null, "Disculpe, NO SE PUEDE  QUITAR pruebas que ya esten REPORTADAS al SICOV ..! ");
                return;
            }
            if (hojaPruebasActual.getEstadoSICOV().equalsIgnoreCase("Env1FUR")) {
                JOptionPane.showMessageDialog(null, "Disculpe, NO SE PUEDE  QUITAR pruebas que ya esten REPORTADAS al SICOV ..! ");
                return;
            }
            if (prueba.getAutorizada().equalsIgnoreCase("A") && prueba.getHojaPruebas().getPreventiva().equalsIgnoreCase("N")) {
                JOptionPane.showMessageDialog(null, "Disculpe; Esta prueba Usted no esta AUTORIZADO por el Momento a  Eliminar ..!  ");
                return;
            }
            while (true) {
                FrmPassword frmP = new FrmPassword(null, JDialog.ModalityType.MODELESS);
                frmP.setModal(true);
                frmP.setLocationRelativeTo(null);
                if (prueba.getHojaPruebas().getPreventiva() == "N") {
                    System.out.println("-------------------------------------------------LA PRUEBA NO ES PREVENTIVA POR LO CUAL SE VA A VALIDAR LA CLAVE");
                    frmP.setVisible(true);
                    UsuarioJpaController usrCon = new UsuarioJpaController();
                    em = usrCon.getEntityManager();
                    nombre = UsuarioJpaController.findUsuariosByAdmNombre(FrmPassword.parametro, em);

                    if (nombre.equalsIgnoreCase("0")) {
                        if (cntClv == 2) {
                            FrmPassword.desistir = true;
                            FrmPassword.msgApp = "";
                            break;
                        }
                        FrmPassword.msgApp = "La Clave Digitada es Incorrecta";
                        cntClv++;
                    } else {
                        FrmPassword.desistir = false;
                        break;
                    }
                }else{
                    nombre = " ";
                    break;
                }

            }
            if (nombre.equalsIgnoreCase("0")) {
                return;
            }

            int seleccion = JOptionPane.showOptionDialog(null, "".concat(nombre).concat("; Esta a punto de QUITAR una Prueba  ¿Esta Seguro que Desea continuar ? "),
                    "Confirmacion de Operacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (seleccion == JOptionPane.NO_OPTION) {
                return;
            }
            seleccion = JOptionPane.showOptionDialog(null, "".concat("RECUERDE que Continuar con esta OPERACION no Tiene Vuelta Atras ¿Desea Continuar? "),
                    "Confirmacion de Operacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (seleccion == JOptionPane.NO_OPTION) {
                return;
            }
            if (prueba.getFinalizada().equalsIgnoreCase("N") || prueba.getHojaPruebas().getPreventiva().equalsIgnoreCase("Y")) {
                for (int i = 0; i < hojaPruebasActual.getListPruebas().size(); i++) {
                    if (Objects.equals(hojaPruebasActual.getListPruebas().get(i).getId(), prueba.getId()) & Objects.equals(hojaPruebasActual.getListPruebas().get(i).getTipoPrueba().getId(), prueba.getTipoPrueba().getId())) {
                        try {
                            PruebaJpaController pru = new PruebaJpaController();
                            Prueba p = hojaPruebasActual.getListPruebas().get(i);
                            if (p.getId() != null) {
                                pru.destroy(hojaPruebasActual.getListPruebas().get(i).getId());
                            }
                            hojaPruebasActual.getListPruebas().remove(i);
                            HojaPruebasJpaController contro = new HojaPruebasJpaController();
                            try {
                                contro.edit(hojaPruebasActual);
                            } catch (Exception ex) {
                            }
                            break;
                        } catch (NonexistentEntityException | ClassNotFoundException | IOException | SQLException ex) {
                            Logger.getLogger(PanelRevisiones.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }/// FIN DE LOGICA DE ELIMINACION DE PRUEBA
                List<Prueba> lstPruebas = pruebasJPA.findUltimasPruebasByHoja(hojaPruebasActual.getId());
                datosPruebas(hojaPruebasActual.getListPruebas());

            } else {
                if (prueba.getAbortado().equalsIgnoreCase("A")) {
                    JOptionPane.showMessageDialog(null, "Disculpe; No se Puede Repetir un Prueba si su CONDICION ya es ANULADA ");
                } else {
                    JOptionPane.showMessageDialog(null, "Disculpe; No se Puede Repetir un Prueba si su CONDICION es PENDIENTE ");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Disculpe; No ha Seleccionado ninguna Prueba para Eliminar ");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(PanelRevisiones.class.getName()).log(Level.SEVERE, null, ex);
        }
        JFrame frame = new JFrame();
        PanelRevisiones panel = new PanelRevisiones();
        frame.setContentPane(panel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup ConversionGNV;
    private org.jdesktop.swingx.JXDatePicker VencGnv;
    private javax.swing.JCheckBox apliEnsezas;
    private javax.swing.JButton btnAnular;
    private javax.swing.JButton btnAnular1;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelarHojaPruebas;
    private javax.swing.JButton btnDerogar;
    private javax.swing.JButton btnDesviacion;
    private javax.swing.JButton btnEditarPlaca;
    private javax.swing.JButton btnFoto;
    private javax.swing.JButton btnFrenos;
    private javax.swing.JButton btnGases;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnHabilitar;
    private javax.swing.JButton btnHojaPruebas;
    private javax.swing.JButton btnLuces;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JButton btnReinspeccion;
    private javax.swing.JButton btnRuido;
    private javax.swing.JButton btnSugerir;
    private javax.swing.JButton btnSuspension;
    private javax.swing.JButton btnTaximetro;
    private javax.swing.JButton btnVisual;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox chbConductorPropietario;
    private javax.swing.JCheckBox chbScooter;
    private javax.swing.JCheckBox chckKilometrajeFuncional;
    private javax.swing.JComboBox cmbAseguradora;
    private javax.swing.JComboBox cmbCarroceria;
    private javax.swing.JComboBox cmbCiudadC;
    private javax.swing.JComboBox cmbCiudadP;
    private javax.swing.JComboBox cmbClase;
    private javax.swing.JComboBox cmbColor;
    private javax.swing.JComboBox cmbCombustible;
    private javax.swing.JComboBox cmbDepartamentoC;
    private javax.swing.JComboBox cmbDepartamentoP;
    private javax.swing.JComboBox cmbDtResp;
    private javax.swing.JComboBox cmbLinea;
    private javax.swing.JComboBox cmbMarca;
    private javax.swing.JComboBox cmbNacionalidad;
    private javax.swing.JComboBox cmbPais;
    private javax.swing.JComboBox cmbServEspeciales;
    private javax.swing.JComboBox cmbServicio;
    private javax.swing.JComboBox cmbTipo;
    private javax.swing.JComboBox cmbTipoIdentificacionC;
    private javax.swing.JComboBox cmbTipoIdentificacionP;
    private javax.swing.JComboBox cmbTipoLicenciaC;
    private javax.swing.JComboBox cmbTipoLicenciaP;
    private javax.swing.JComboBox cmbTipoLlanta;
    private javax.swing.JRadioButton congnvno;
    private javax.swing.JRadioButton congnvnoaplica;
    private javax.swing.JRadioButton congnvsi;
    private org.jdesktop.swingx.JXDatePicker dprExpSoat;
    private org.jdesktop.swingx.JXDatePicker dprFechaMatricula;
    private org.jdesktop.swingx.JXDatePicker dprVencSoat;
    private javax.swing.ButtonGroup groupBlindaje;
    private javax.swing.ButtonGroup groupPolarizados;
    private javax.swing.ButtonGroup grupocatalizador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblCodInterno;
    private javax.swing.JLabel lblCodInterno1;
    private javax.swing.JLabel lblCodInterno2;
    public javax.swing.JPanel pnlConductor;
    private javax.swing.JPanel pnlDerecho;
    private javax.swing.JPanel pnlHojaPruebas;
    private javax.swing.JPanel pnlIzquierdo;
    public javax.swing.JPanel pnlPropietario;
    private javax.swing.JPanel pnlPruebas;
    private javax.swing.JPanel pnlRegistroPropietario;
    private javax.swing.JPanel pnlRegistroPruebas;
    private javax.swing.JTabbedPane pnlRevisiones;
    public javax.swing.JPanel pnlVehiculo;
    private javax.swing.JRadioButton rdbBlindajeNo;
    private javax.swing.JRadioButton rdbBlindajeSi;
    private javax.swing.JRadioButton rdbPolarizadosNo;
    private javax.swing.JRadioButton rdbPolarizadosSi;
    private javax.swing.JSpinner sprEjes;
    private javax.swing.JSpinner sprExostos;
    private javax.swing.JSpinner sprModelo;
    private javax.swing.JSpinner sprSillas;
    private javax.swing.JSpinner sprTiemposMotor;
    private javax.swing.JTable tblHojaPruebas;
    private javax.swing.JTable tblPruebas;
    private javax.swing.JTextField txtApellidosC;
    private javax.swing.JTextField txtApellidosP;
    private javax.swing.JTextField txtCarroceria;
    private javax.swing.JTextField txtCelularC;
    private javax.swing.JTextField txtCelularP;
    private javax.swing.JTextField txtChasis;
    private javax.swing.JTextField txtCilindraje;
    private javax.swing.JTextField txtCodInterno;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtDireccionC;
    private javax.swing.JTextField txtDireccionP;
    private javax.swing.JTextField txtEmailC;
    private javax.swing.JTextField txtEmailP;
    private javax.swing.JTextField txtIdentificacionC;
    private javax.swing.JTextField txtIdentificacionP;
    private javax.swing.JTextField txtKilometraje;
    private javax.swing.JTextField txtLicenciaC;
    private javax.swing.JTextField txtLicenciaP;
    private javax.swing.JTextField txtLinea;
    private javax.swing.JTextField txtNombresC;
    private javax.swing.JTextField txtNombresP;
    private javax.swing.JTextField txtNumLicencia;
    private javax.swing.JTextField txtNumMotor;
    private javax.swing.JTextField txtNumSerie;
    private javax.swing.JTextField txtNumSoat;
    private javax.swing.JTextField txtPesoBruto;
    private javax.swing.JTextField txtPlaca;
    private javax.swing.JTextField txtPotencia;
    private javax.swing.JTextField txtTelefonoC;
    private javax.swing.JTextField txtTelefonoP;
    // End of variables declaration//GEN-END:variables

    private void datosVehiculoHojaPrueba(HojaPruebas hojaPruebasActual) {
        VencGnv.setDate(hojaPruebasActual.getFechaVencimientoGnv());
//        txtKilometraje.setText(hojaPruebasActual.getKilometraje());
    }
}

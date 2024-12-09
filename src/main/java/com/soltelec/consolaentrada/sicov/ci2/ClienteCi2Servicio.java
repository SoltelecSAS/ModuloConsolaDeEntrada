package com.soltelec.consolaentrada.sicov.ci2;

import com.soltelec.consolaentrada.configuration.Conexion;
import com.soltelec.consolaentrada.indra.dto.DatosFotos;
import com.soltelec.consolaentrada.models.controllers.CdaJpaController;
import com.soltelec.consolaentrada.utilities.UtilPropiedades;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.soltelec.consolaentrada.models.entities.Cda;
import com.soltelec.consolaentrada.models.entities.Certificado;
import com.soltelec.consolaentrada.models.entities.Defxprueba;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.Medida;
import com.soltelec.consolaentrada.models.entities.Prueba;
import com.soltelec.consolaentrada.models.controllers.CertificadoJpaController;
import com.soltelec.consolaentrada.models.controllers.EquiposJpaController1;
import com.soltelec.consolaentrada.models.controllers.HojaPruebasJpaController;
import com.soltelec.consolaentrada.models.controllers.PruebaJpaController;
import static com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController.getEntityManager;
import com.soltelec.consolaentrada.models.entities.Equipo;
import com.soltelec.consolaentrada.models.entities.Reinspeccion;
import com.soltelec.consolaentrada.sicov.indra.ClienteIndraServicio;
import com.soltelec.consolaentrada.utilities.FormulaOpacidad;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.jfree.util.Log;
import org.jboss.resteasy.util.Base64;

/**
 *
 * @author GerenciaDesarrollo
 */
public class ClienteCi2Servicio {

    private HojaPruebas hojaPrueba;
    private Formulario_v3 formulario;
    private Cda cda;
    private List<Prueba> pruebasHoja = new ArrayList<Prueba>();
    private Pin pin;
    private Integer idTipoVehiculo;
    private Integer modelo;
    private Integer cilindraje;
    List<String> tecnicos = new ArrayList<>();
    private Integer tiempoMotor;
    private Integer tipoCombustible;
    private Integer claseVehiculo;
    Logger log = Logger.getLogger(ClienteCi2Servicio.class);
    private DatosFotos datosFotos;
    private String resolucion_gases = "";
    private String comentarioOpacidad;

    public ClienteCi2Servicio() {

    }

    public ClienteCi2Servicio(HojaPruebas ctxHojaPrueba, Cda cda) {
        formulario = new Formulario_v3();
        this.hojaPrueba = ctxHojaPrueba;
        this.cda = cda;
        idTipoVehiculo = this.hojaPrueba.getVehiculo().getTipoVehiculo().getId();
        tiempoMotor = this.hojaPrueba.getVehiculo().getTiemposMotor();
        modelo = this.hojaPrueba.getVehiculo().getModelo();
        cilindraje = this.hojaPrueba.getVehiculo().getCilindraje();

        tipoCombustible = this.hojaPrueba.getVehiculo().getTipoGasolina().getId();
        claseVehiculo = this.hojaPrueba.getVehiculo().getClaseVehiculo().getId();
        //Verifica si tiene reinspeccion si es positivo carga las pruebas de la ultima revision        

        if (ctxHojaPrueba.getReinspeccionList().size() == 0) {
            for (Prueba ctxpru : ctxHojaPrueba.getListPruebas()) {
                if (ctxpru.getAbortado().equalsIgnoreCase("N")) {
                    pruebasHoja.add(ctxpru);
                }
            }
        } else {
            Reinspeccion r = ctxHojaPrueba.getReinspeccionList().get(0);
            List<Prueba> tempListPrueba = ctxHojaPrueba.getListPruebas();
            Boolean encontrado = false;
            for (Prueba ctxpru : tempListPrueba) {
                for (Prueba prReins : r.getPruebaList()) {
                    if (ctxpru.getTipoPrueba().getId() == prReins.getTipoPrueba().getId()) {
                        encontrado = true;
                        break;
                    }
                }
                if (encontrado == false && ctxpru.getAbortado().equalsIgnoreCase("N")) {
                    pruebasHoja.add(ctxpru);
                }
                encontrado = false;
            }
            for (Prueba prReins : r.getPruebaList()) {
                if (prReins.getAbortado().equalsIgnoreCase("N")) {
                    pruebasHoja.add(prReins);
                }
            }
        }
        this.hojaPrueba.setListPruebas(pruebasHoja);
        pin = new Pin();
        cargarInformacionPin();
    }

    public ClienteCi2Servicio(Cda cda) {
        this.cda = cda;
    }

    /**
     * Se cargan los datos del cda y las credenciales del servicio web
     *
     * @param aprobado
     */
    public void cargarInf2EnvFUR(Long nroCertificado) {
        formulario = new Formulario_v3();
        formulario.setUsuario(cda.getUsuarioSicov());
        formulario.setClave(cda.getPasswordSicov());
        formulario.setP_pin(hojaPrueba.getPin());
        formulario.setP_e_con_run(hojaPrueba.getConsecutivoRunt());
        formulario.setP_3_plac(hojaPrueba.getVehiculo().getPlaca().trim());
        formulario.setP_3_plac(formulario.getP_3_plac().toUpperCase());
        if (hojaPrueba.getAprobado().equalsIgnoreCase("Y")) {
            formulario.setP_tw01(nroCertificado.toString());

        }
//        String furNum = String.format("%s%s%05d",
//                cda.getCodigoRuntCda(), //3 caracteres codigo runt cds
//                cda.getCodigoRuntProveedor(),//3 caracteres codigo runt proveedor del software
//                hojaPrueba.getCon_hoja_prueba()//5 Oonsecutivos del fur impreso por el cda 
//        );

        formulario.setP_fur_num(String.valueOf(hojaPrueba.getCon_hoja_prueba()));
        formulario.setP_ema(cda.getCorreo());
    }

    public void cargarInformacionReenvio() {
        formulario = new Formulario_v3();
        formulario.setP_pin(hojaPrueba.getPin());
        formulario.setP_3_plac(String.valueOf(hojaPrueba.getVehiculo().getId()));
        formulario.setP_3_plac(formulario.getP_3_plac().toUpperCase());
        formulario.setP_e_con_run(hojaPrueba.getConsecutivoRunt());
        if (hojaPrueba.getEstado().equals("APROBADA")) {
            CertificadoJpaController cJpa = new CertificadoJpaController();
            Certificado c = cJpa.findCertificadosXHoja(hojaPrueba.getId());
            formulario.setP_tw01(String.valueOf(c.getConsecutivo()));
        } else {
            formulario.setP_tw01("");
        }

    }

    public void cargarInformacionBasica() {
        //Cargar informacion basica de accesos al WS
        formulario.setUsuario(cda.getUsuarioSicov());
        formulario.setClave(cda.getPasswordSicov());
        formulario.setP_pin(hojaPrueba.getPin());
        // Informacion del cda.
        if (hojaPrueba.getIntentos() > 1) {
            formulario.setP_fur_aso(String.valueOf(hojaPrueba.getCon_hoja_prueba()).concat("-1"));
        } else {
            formulario.setP_fur_aso(String.valueOf(hojaPrueba.getCon_hoja_prueba()).concat("-1;").concat(String.valueOf(hojaPrueba.getCon_hoja_prueba()).concat("-2")));
        }
        formulario.setP_cda(cda.getNombre());
        formulario.setP_nit(cda.getNit());
        formulario.setP_div(cda.getDivipola().toString());
        formulario.setP_dir(cda.getDireccion());
        formulario.setP_ciu(cda.getCiudad());
        formulario.setP_tel(cda.getTelefono());
        formulario.setP_g_nom_fir_dir_tec(hojaPrueba.getResponsable().getNombre());

        if (hojaPrueba.getVehiculo().getTipoGasolina().getId() == 5) {
            formulario.setP_v01("X");
        }
        if (hojaPrueba.getVehiculo().getTipoGasolina().getId() == 6) {
            formulario.setP_v02("X");
        }
        formulario.setP_e_con_run("");
        String estPrueba = null;
        formulario.setP_tw01("");
        if (hojaPrueba.getEstado().equalsIgnoreCase("APROBADA")) {
            formulario.setP_tw01("");
            estPrueba = "SI";
        } else {
            formulario.setP_tw01("");
            estPrueba = "NO";
        }
        formulario.setP_e_apr(estPrueba);

        if (hojaPrueba.getEstado().equals("REPROBADA")) {
            for (Prueba pruebas : pruebasHoja) {
                for (Defxprueba defxprueba : pruebas.getDefxpruebaList()) {
                    formulario.setP_causa_rechazo(formulario.getP_causa_rechazo().concat(defxprueba.getDefectos().getCodigoResolucion().concat(";")));
//                  formulario.setP_causa_rechazo(formulario.getP_causa_rechazo().concat(defxprueba.getDefectos().getCodigoSuperintendencia().concat(";")));
                }
            }//eliminar ultimo token ;            
            if (formulario.getP_causa_rechazo().length() > 0) {
                formulario.setP_causa_rechazo(formulario.getP_causa_rechazo().substring(0, formulario.getP_causa_rechazo().length() - 1));
            }
        }
        String furNum = String.format("%s%s%05d",
                cda.getCodigoRuntCda(), //3 caracteres codigo runt cds
                cda.getCodigoRuntProveedor(),//3 caracteres codigo runt proveedor del software
                hojaPrueba.getCon_hoja_prueba()//5 Oonsecutivos del fur impreso por el cda 
        );
        formulario.setP_fur_num(furNum);
        formulario.setP_ema(cda.getCorreo());
    }
//   

    public void datosFotos() {
        datosFotos = new DatosFotos();
        datosFotos.setPlaca(hojaPrueba.getVehiculo().getPlaca().trim());
        try {
            datosFotos.setFotoDelantera(Base64.encodeBytes(hojaPrueba.getFotosList().get(0).getFoto1()));
            datosFotos.setFotoTrasera(Base64.encodeBytes(hojaPrueba.getFotosList().get(0).getFoto2()));
            formulario.setP_foto(datosFotos.toString2());
            System.out.println("Ubic ClienteIndraServicio, Se han Cargado el arrays de Bytes");
        } catch (Exception e) {
            System.out.println("Se cayo la carga de los arrys de imagenes");
            //throw new SartComunicadorException(500, "La revision no tiene las imagenes completas");
        }
    }

    public void cargarInformacionPropietario() {
        String tDoc = null;
        if (hojaPrueba.getReinspeccionList().size() > 0) {
            formulario.setP_1_fec_pru(UtilPropiedades.convertiFechas(hojaPrueba.getReinspeccionList().iterator().next().getFechaSiguiente(), "ddMMyyyy HH:mm"));
        } else {
            formulario.setP_1_fec_pru(UtilPropiedades.convertiFechas(hojaPrueba.getFechaIngreso(), "ddMMyyyy HH:mm"));
        }

        formulario.setP_2_nom_raz(hojaPrueba.getPropietario().getNombres() + " " + hojaPrueba.getPropietario().getApellidos());
        switch ((hojaPrueba.getPropietario().getTipoIdentificacion().name())) {
            case "CC":
                tDoc = "1";
                break;
            case "NIT":
                tDoc = "2";
                break;
            case "CE":
                tDoc = "3";
                break;
            case "U":
                tDoc = "4";
                break;
            case "P":
                tDoc = "5";
        }
        formulario.setP_2_doc_tip(tDoc);
        formulario.setP_2_doc(hojaPrueba.getPropietario().getId().toString());
        formulario.setP_2_dir(hojaPrueba.getPropietario().getDireccion());
        if (hojaPrueba.getPropietario().getCelular().length() > 0) {
            formulario.setP_2_tel(hojaPrueba.getPropietario().getCelular());
        } else {
            formulario.setP_2_tel(hojaPrueba.getPropietario().getTelefono());
        }
        formulario.setP_2_ciu(hojaPrueba.getPropietario().getCiudad().getCiudadPrincipal());
        formulario.setP_2_dep(hojaPrueba.getPropietario().getCiudad().getDepartamento().getNombre());

        //Asjustes sicov v2
        formulario.setP_2_ema(hojaPrueba.getPropietario().getEmail());
    }

    public void cargarInformacionVehiculo(String kilometraje) {
        Log.info("-------------------------------------------------------------");
        Log.info("Cargando informacion de Vehiculo");
        Log.info("-------------------------------------------------------------");
        try {
            formulario.setP_3_plac(hojaPrueba.getVehiculo().getPlaca());
            formulario.setP_3_plac(hojaPrueba.getVehiculo().getPlaca().trim());
            formulario.setP_3_mar(hojaPrueba.getVehiculo().getMarca().getNombre());
            formulario.setP_3_lin(hojaPrueba.getVehiculo().getLineaVehiculo().getNombre());
            formulario.setP_3_cla(hojaPrueba.getVehiculo().getClaseVehiculo().getNombre());
            formulario.setP_3_mod(String.valueOf(hojaPrueba.getVehiculo().getModelo()));
            formulario.setP_3_cil(hojaPrueba.getVehiculo().getCilindraje().toString());
            formulario.setP_3_ser(hojaPrueba.getVehiculo().getServicios().getNombre().toUpperCase());
            formulario.setP_3_vin(hojaPrueba.getVehiculo().getChasis());
            formulario.setP_3_mot(hojaPrueba.getVehiculo().getMotor());
            formulario.setP_3_lic(hojaPrueba.getVehiculo().getLicencia());

            String TipoCombustible = hojaPrueba.getVehiculo().getTipoGasolina().getNombre();
            Log.info("Validando tipo de Combustible" + TipoCombustible);
            if (hojaPrueba.getVehiculo().getTipoGasolina().getNombre().equalsIgnoreCase("GAS NATURAL VEHICULAR")) {
                TipoCombustible = "GNV";
            }
            if (hojaPrueba.getVehiculo().getTipoGasolina().getNombre().equalsIgnoreCase("GLP")) {
                TipoCombustible = "GLP";
            }
            if (hojaPrueba.getVehiculo().getTipoGasolina().getNombre().equalsIgnoreCase("GAS - GASOLINA")) {
                TipoCombustible = "GAS GASOL";
            }
            if (hojaPrueba.getVehiculo().getTipoGasolina().getNombre().equalsIgnoreCase("GASOLINA - ELECTRICO")) {
                TipoCombustible = "GASO ELEC";
            }
            formulario.setP_3_com(TipoCombustible);

            formulario.setP_3_col(hojaPrueba.getVehiculo().getColor().getNombre());
            formulario.setP_3_nac(hojaPrueba.getVehiculo().getPais().getNombre());
            formulario.setP_3_fec_lic(UtilPropiedades.convertiFechas(hojaPrueba.getVehiculo().getFechaRegistro(), "ddMMyyyy"));
            formulario.setP_3_tip_mot(hojaPrueba.getVehiculo().getTiemposMotor().toString());
            if (kilometraje.equalsIgnoreCase("0")) {
                kilometraje = "";
            }
            formulario.setP_3_kil(kilometraje);
            if (hojaPrueba.getVehiculo().getVidriosPolarizados().equals("S")) {
                formulario.setP_3_vid_pol("SI");
            } else {
                formulario.setP_3_vid_pol("NO");
            }
            if (hojaPrueba.getVehiculo().getBlindaje().equals("S")) {
                formulario.setP_3_bli("SI");
            } else {
                formulario.setP_3_bli("NO");
            }
            formulario.setP_3_sil(hojaPrueba.getVehiculo().getSillas().toString());
//        try {
            formulario.setP_foto(" ");
//        } catch (Exception e) {
//            //throw new SartComunicadorException(500, "No se lograron cargar las fotos");
//        }
            if (hojaPrueba.getVehiculo().getPotencia() != null) {
                formulario.setP_3_pot(hojaPrueba.getVehiculo().getPotencia().toString());
            } else {
                formulario.setP_3_pot("0");
            }
            if (hojaPrueba.getVehiculo().getTipoCarroceria() != null) {
                formulario.setP_3_tip_car(hojaPrueba.getVehiculo().getTipoCarroceria().getNombre());
            } else {
                formulario.setP_3_tip_car("SIN CARROCERIA");
            }

            if (hojaPrueba.getVehiculo().getFechaSOAT() != null) {
                formulario.setP_3_fec_ven_soa(UtilPropiedades.convertiFechas(hojaPrueba.getVehiculo().getFechaSOAT(), "ddMMyyyy"));
            }
            if (hojaPrueba.getVehiculo().getEsConversionGnv() != null) {
                switch (hojaPrueba.getVehiculo().getEsConversionGnv()) {
                    case "Y":
                        formulario.setP_3_con_gnv("SI");
                        break;
                    case "N":
                        formulario.setP_3_con_gnv("NO");
                        break;
                    default:
                        formulario.setP_3_con_gnv("NA");
                        break;
                }
            } else {
                formulario.setP_3_con_gnv("NA");
            }

            if (hojaPrueba.getVehiculo().getFechaVencimientoGnv() != null) {
                formulario.setP_3_fec_ven_gnv(UtilPropiedades.convertiFechas(hojaPrueba.getVehiculo().getFechaVencimientoGnv(), "ddMMyyyy"));
            }
        } catch (Exception e) {
            Log.error("Error en el metodo :cargarInformacionVehiculo()" + e.getMessage());
        }
    }

    public void cargarInformacionEmisionesAudible() {
        double promedioRuidoMotor = 0;
        String ruidoMotor = null;
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
        for (Prueba p : pruebasHoja) {
            if (p.getTipoPrueba().getId() == 7) {
                for (Medida m : p.getMedidaList()) {
                    switch (m.getTipoMedida().getId()) {
                        case 7005:
                        case 7002:
                            formulario.setP_4_rui_val(m.getValor().toString());
                            formulario.setP_4_rui_max("");
                            //formulario.setP_r03("");
                            //formulario.setP_r04("");
                            break;
                    }
                }
            }
        }
    }

    public void cargarInformacionIntencidadLucesBajas() { //no
        for (Prueba p : pruebasHoja) {
            if (p.getTipoPrueba().getId() == 2) {
                //TODO: Colocar valores verdaderos

                for (Medida medidas : p.getMedidaList()) {
                    switch (medidas.getTipoMedida().getId()) {
                        case 2024: //Intencidad de la luz baja derecha para vehiculo
                        case 2014: //Intencidad de la luz baja derecha para moto
                        case 2018: //Intencidad de la luz baja derecha para moto carro
                            formulario.setP_5_der_int_b1(medidas.getValor().toString());
                            if (medidas.getTipoMedida().getId() == 2024) {
                                formulario.setP_5_sim_der_b((medidas.getSimult().equalsIgnoreCase("Y") ? "SI" : "NO"));//*envia si es simultanea
                            }

                            break;

                        case 2031://Intencidad de las luces baja izquierda para vehiculo
                        case 2015://Intencidad de las luces baja izquierda para moto
                        case 2019://Intencidad de las luces baja izquierda para motocarro
                            formulario.setP_5_izq_int_b1(medidas.getValor().toString());

                            if (medidas.getTipoMedida().getId() == 2031) {
                                formulario.setP_5_sim_izq_b((medidas.getSimult().equalsIgnoreCase("Y") ? "SI" : "NO"));//*envia si es simultanea
                            }
                            formulario.setP_5_izq_min("2.5");
                            break;

                        case 2040://Inclinacion de luz baja derecha para vehiculos
                        case 2013://Inclinacion de luz baja derecha para Moto
                        case 2021://Inclinacion de luz baja derecha para Moto carro
                            formulario.setP_5_der_inc_b1(medidas.getValor().toString());
                            formulario.setP_5_der_ran("0.5-3.5");

                            break;

                        case 2044://Inclinacion de luz baja izquierda para vehiculo
                        case 2002://Inclinacion de luz baja izquierda para moto
                        case 2020://Inclinacion de luz baja izquierda para moto carro
                            formulario.setP_5_izq_inc_b1(medidas.getValor().toString()); //TODO: Verificar campo

                            formulario.setP_5_izq_ran("0.5-3.5");

                            break;

                        case 2032://Intencidad de luz alta izquierda para vehiculo
                        case 9074://Intensidad alta izquierda 1
                        case 2057://Intencidad de luz alta izquierda para moto
                        case 2017://Intencidad de luz alta izquierda para moto carro
                            formulario.setP_5_izq_int_a1(medidas.getValor().toString());
                            if (idTipoVehiculo != 4) { //Moto Por norma no se debe validar la sumatoria total
                                formulario.setP_5_sim_izq_a((medidas.getSimult().equalsIgnoreCase("Y") ? "SI" : "NO"));//*envia si es simultanea
                                break;
                            }

                            break;

                        case 2036://Intencidad de luz alta derecha para vehiculo
                        case 2056://Intencidad de luz alta derecha para moto
                        case 2016://Intencidad de luz alta derecha para moto carro
                            formulario.setP_5_der_int_a1(medidas.getValor().toString());
                            if (idTipoVehiculo != 4) { //Moto Por norma no se debe validar la sumatoria total                                                            
                                formulario.setP_5_sim_der_a((medidas.getSimult().equalsIgnoreCase("Y") ? "SI" : "NO"));//*envia si es simultanea
                            }
                            break;

                        case 2011://Suma de todas las intensidades de luz para vehiculos
                            if (idTipoVehiculo == 4) { //Moto Por norma no se debe validar la sumatoria total
                                break;
                            }
                            formulario.setP_6_int(medidas.getValor().toString());
                            formulario.setP_6_max("225");
                            break;

                        case 9057://Intensidad baja derecha 2
                        case 2025://BAJA DERECHA FAROLA 2
                            formulario.setP_5_der_int_b2(medidas.getValor().toString());

                            break;
                        case 9058://Intensidad baja derecha 3
                        case 2026://BAJA DERECHA FAROLA 3
                            formulario.setP_5_der_int_b3(medidas.getValor().toString());
                            break;

                        case 9061://Intencidad baja izquierda 2
                        case 2030://BAJA IZQUIERDA FAROLA 2
                            formulario.setP_5_izq_int_b2(medidas.getValor().toString());
                            break;

                        case 9062://Intencidad baja izquierda 3
                        case 2029://BAJA IZQUIERDA FAROLA 3
                            formulario.setP_5_izq_int_b3(medidas.getValor().toString());
                            break;

                        case 9065://Inclinacion baja derecha 2
                        case 2041://INCLINACION DERECHA FAROLA 2
                            formulario.setP_5_der_inc_b2(medidas.getValor().toString());
                            break;

                        case 9066://Inclinacion baja derecha 3
                        case 2042://INCLINACION DERECHA FAROLA 3
                            formulario.setP_5_der_inc_b3(medidas.getValor().toString());
                            break;

                        case 9068://Inclinacion baja izquierda 2
                        case 2045://INCLINACION IZQUIERDA FAROLA 2
                            formulario.setP_5_izq_inc_b2(medidas.getValor().toString());
                            break;

                        case 9069://Inclinacion baja izquierda 3
                        case 2046://INCLINACION IZQUIERDA FAROLA 3
                            formulario.setP_5_izq_inc_b3(medidas.getValor().toString());
                            break;

                        case 9071://Intensidad alta derecha 2
                        case 2037://ALTA DERECHA FAROLA 2
                            formulario.setP_5_der_int_a2(medidas.getValor().toString());
                            break;

                        case 9072://Intensidad alta derecha 3
                        case 2038://ALTA DERECHA FAROLA 3
                            formulario.setP_5_der_int_a3(medidas.getValor().toString());
                            break;

                        case 9075://Intensidad alta izquierda 2
                        case 2033://ALTA IZQUIERDA FAROLA 2
                            formulario.setP_5_izq_int_a2(medidas.getValor().toString());
                            break;

                        case 9076://Intensidad alta izquierda 3
                        case 2034://ALTA IZQUIERDA FAROLA 3
                            formulario.setP_5_izq_int_a3(medidas.getValor().toString());
                            break;

                        case 9078://Exploradoras derechas 1
                        case 2050://INTENSIDAD EXPLORADORA DERECHA 1
                            formulario.setP_5_sim_der_e((medidas.getSimult().equalsIgnoreCase("Y") ? "SI" : "NO"));//*envia si es simultanea
                            formulario.setP_5_der_int_e1(medidas.getValor().toString());
                            break;

                        case 9079://Exploradoras derechas 2
                        case 2051://INTENSIDAD EXPLORADORA DERECHA 2
                            formulario.setP_5_der_int_e2(medidas.getValor().toString());
                            break;

                        case 9080://Exploradoras derechas 3
                        case 2052://INTENSIDAD EXPLORADORA DERECHA 3
                            formulario.setP_5_der_int_e3(medidas.getValor().toString());
                            break;

                        case 9082://Exploradoras izquierda 1
                        case 2053://INTENSIDAD EXPLORADORA IZQUIERDA 1
                            formulario.setP_5_sim_izq_e((medidas.getSimult().equalsIgnoreCase("Y") ? "SI" : "NO"));//*envia si es simultanea
                            formulario.setP_5_izq_int_e1(medidas.getValor().toString());
                            break;
                        case 9083://Exploradoras izquierda 2
                        case 2054://INTENSIDAD EXPLORADORA IZQUIERDA 2
                            formulario.setP_5_izq_int_e2(medidas.getValor().toString());
                            break;

                        case 9084://Exploradoras izquierda 3
                        case 2055://INTENSIDAD EXPLORADORA IZQUIERDA 3                            
                            formulario.setP_5_izq_int_e3(medidas.getValor().toString());
                            break;

//                            Pendiene para colocar medidas     ////////////////////////////////////////////////////////////////
                        case 9059://Luces bajas simultaneas derecha
                            formulario.setP_5_sim_der_b(medidas.getValor().toString());
                            break;

                        case 9063://Luces bajas simultaneas izquierda                        
                            formulario.setP_5_sim_izq_b(medidas.getValor().toString());
                            break;

                        case 9073://Luces bajas simultaneas derecha
                            formulario.setP_5_sim_der_a(medidas.getValor().toString());
                            break;

                        case 9085://Exploradoras izquierda simultaneas
                            formulario.setP_5_sim_izq_e(medidas.getValor().toString());
                            break;

                        case 9081://Exploradoras derechas simultaneas
                            formulario.setP_5_sim_der_e(medidas.getValor().toString());
                            break;

                        case 9077://Luces altas simultaneas izquierda
                            formulario.setP_5_sim_izq_a(medidas.getValor().toString());
                            break;
                    }

                }
            }
        }

        formulario.setP_5_der_min("2.5");
        formulario.setP_5_izq_min("2.5");
        /* formulario.setP_5_der_min_a("2.5");
        formulario.setP_5_izq_min_a("2.5");
        formulario.setP_5_der_min_e("2.5");
        formulario.setP_5_izq_min_e("2.5");*/
    }

    public void cargarInformacionSuspencion() {
        for (Prueba p : pruebasHoja) {
            if (p.getTipoPrueba().getId() == 6) {
                for (Medida medidas : p.getMedidaList()) {
                    switch (medidas.getTipoMedida().getId()) {
                        case 6016: //Suspension derecha por eje
                            formulario.setP_7_del_der_val(medidas.getValor().toString());
                            break;
                        case 6020://Suspension izquierda por eje
                            formulario.setP_7_del_izq_val(medidas.getValor().toString());
                            break;
                        case 6017://Suspension derecha por eje
                            formulario.setP_7_tra_der_val(medidas.getValor().toString());
                            break;
                        case 6021://Suspension izquierda  por eje
                            formulario.setP_7_tra_izq_val(medidas.getValor().toString());
                            break;
                    }
                }
            }
        }
    }

    public void cargarInformacionFrenos() {//ok

        for (Prueba p : pruebasHoja) {
            int tipo_vehiculo = p.getHojaPruebas().getVehiculo().getTipoVehiculo().getId();
            if (p.getTipoPrueba().getId() == 5) {
                Float derFuerzaAux = 0f;
                Float izqFuerzaAux = 0f;
                Float derAuxPeso = 0f;
                Float izqAuxPeso = 0f;
                formulario.setP_8_ej1_ran("20-30");
                formulario.setP_8_ej2_ran("20-30");
                formulario.setP_8_ej3_ran("20-30");
                formulario.setP_8_ej4_ran("20-30");
                formulario.setP_8_ej5_ran("20-30");

                //TODO: Ajustar los valores de eficacia por lado izquierdo y derecho y sumar los pesos
                for (Medida medidas : p.getMedidaList()) {
                    System.out.println("tipo de medida:" + medidas.getTipoMedida().getId());

                    switch (medidas.getTipoMedida().getId()) {
                        case 5024: //Eficacia de frenado
                            formulario.setP_8_efi_tot(medidas.getValor().toString());
                            //izqAuxPeso = izqAuxPeso + medidas.getValor();
                            break;
                        case 5008://Fuerza de frenado maxima derecha por eje (FFMDEEJE1)
                            formulario.setP_8_ej1_der_fue(medidas.getValor().toString());
                            break;
                        case 5009://Fuerza de frenado maxima derecha por eje (FFMDEEJE2)
                            formulario.setP_8_ej2_der_fue(medidas.getValor().toString());
                            break;
                        case 5010://Fuerza de frenado maxima derecha por eje (FFMDEEJE3)
                            formulario.setP_8_ej3_der_fue(medidas.getValor().toString());
                            break;
                        case 5011://Fuerza de frenado maxima derecha por eje (FFMDEEJE4)
                            formulario.setP_8_ej4_der_fue(medidas.getValor().toString());
                            break;
                        case 5012://Fuerza de frenado maxima izquierda por eje (FFMIZEJE1)
                            formulario.setP_8_ej1_izq_fue(medidas.getValor().toString());
                            break;
                        case 5013://Fuerza de frenado maxima izquierda por eje (FFMIZEJE2)
                            formulario.setP_8_ej2_izq_fue(medidas.getValor().toString());
                            break;
                        case 5014://Fuerza de frenado maxima izquierda por eje (FFMIZEJE3)
                            formulario.setP_8_ej3_izq_fue(medidas.getValor().toString());
                            break;
                        case 5015://Fuerza de frenado maxima izquierda por eje (FFMIZEJE4)
                            formulario.setP_8_ej4_izq_fue(medidas.getValor().toString());
                            break;
                        case 5000://Peso derecho por eje (PESDEREJE1)
                            formulario.setP_8_ej1_der_pes(medidas.getValor().toString());
                            if (tipo_vehiculo != 4) {
                                derAuxPeso = derAuxPeso + medidas.getValor();
                            }
                            break;
                        case 5001://Peso derecho por eje (PESDEREJE2)
                            formulario.setP_8_ej2_der_pes(medidas.getValor().toString());
                            if (tipo_vehiculo != 4) {
                                derAuxPeso = derAuxPeso + medidas.getValor();
                            }
                            break;
                        case 5002://Peso derecho por eje (PESDEREJE3)
                            formulario.setP_8_ej3_der_pes(medidas.getValor().toString());
                            if (tipo_vehiculo != 4) {
                                derAuxPeso = derAuxPeso + medidas.getValor();
                            }
                            break;
                        case 5003://Peso derecho por eje (PESDEREJE4)
                            formulario.setP_8_ej4_der_pes(medidas.getValor().toString());
                            if (tipo_vehiculo != 4) {
                                derAuxPeso = derAuxPeso + medidas.getValor();
                            }
                            break;
                        case 5004://Peso izquierdo por eje (PESIZQEJE1)
                            formulario.setP_8_ej1_izq_pes(medidas.getValor().toString());
                            izqAuxPeso = izqAuxPeso + medidas.getValor();
                            break;
                        case 5005://Peso izquierdo por eje (PESIZQEJE2)
                            formulario.setP_8_ej2_izq_pes(medidas.getValor().toString());
                            izqAuxPeso = izqAuxPeso + medidas.getValor();
                            break;
                        case 5006://Peso izquierdo por eje (PESIZQEJE3)
                            formulario.setP_8_ej3_izq_pes(medidas.getValor().toString());
                            izqAuxPeso = izqAuxPeso + medidas.getValor();
                            break;
                        case 5007://Peso izquierdo por eje (PESIZQEJE4)
                            formulario.setP_8_ej4_izq_pes(medidas.getValor().toString());
                            izqAuxPeso = izqAuxPeso + medidas.getValor();
                            break;
                        case 5032://Desequilibrio por eje (DESEJE1)
                            formulario.setP_8_ej1_des(medidas.getValor().toString());
                            break;
                        case 5033://Desequilibrio por eje (DESEJE2)
                            formulario.setP_8_ej2_des(medidas.getValor().toString());
                            break;
                        case 5034://Desequilibrio por eje (DESEJE3)
                            formulario.setP_8_ej3_des(medidas.getValor().toString());
                            break;
                        case 5035://Desequilibrio por eje (DESEJE4)
                            formulario.setP_8_ej4_des(medidas.getValor().toString());
                            break;
                        case 5036://Eficacia de freno de mano (EFIFREMAN) 
                            formulario.setP_8_efi_aux(medidas.getValor().toString());
                            break;
                        case 5016://
                        case 5017://
                        case 5018://
                        case 5019://
                        case 5029://
                            derFuerzaAux = derFuerzaAux + medidas.getValor();
                            break;
                        case 5020://
                        case 5021://
                        case 5022://
                        case 5023://
                        case 5030://
                            izqFuerzaAux = izqFuerzaAux + medidas.getValor();
                            break;
                        case 5025://Peso derecho por eje (PESDEREJE5)
                            formulario.setP_8_ej5_der_pes(medidas.getValor().toString());
                            if (tipo_vehiculo != 4) {
                                derAuxPeso = derAuxPeso + medidas.getValor();
                            }
                            break;
                        case 5026://Peso izquierdo por eje (PESIZQEJE5)
                            formulario.setP_8_ej5_izq_pes(medidas.getValor().toString());
                            ;
                            izqAuxPeso = izqAuxPeso + medidas.getValor();
                            break;
                    }
                }
                formulario.setP_8_sum_izq_aux_fue(String.valueOf(ponerVacio(izqFuerzaAux)));//sumafi
                formulario.setP_8_sum_izq_aux_pes(String.valueOf(ponerVacio(izqAuxPeso)));//sumapi
                formulario.setP_8_sum_der_aux_fue(String.valueOf(ponerVacio(derFuerzaAux)));//sumafd
                formulario.setP_8_sum_der_aux_pes(String.valueOf(ponerVacio(derAuxPeso)));//sumapd
            }
        }

    }

    public void cargarInformacionDesviacion() {
        for (Prueba p : pruebasHoja) {
            if (p.getTipoPrueba().getId() == 4) {
                for (Medida medidas : p.getMedidaList()) {
                    switch (medidas.getTipoMedida().getId()) {
                        case 4000: //Desviacion del eje numero 1 (DESVEJE1)
                            formulario.setP_9_ej1(medidas.getValor().toString());
                            break;
                        case 4001: //Desviacion del eje numero 2 (DESVEJE2)
                            formulario.setP_9_ej2(medidas.getValor().toString());
                            break;
                        case 4002: //Desviacion del eje numero 3 (DESVEJE3)
                            formulario.setP_9_ej3(medidas.getValor().toString());
                            break;
                        case 4003: //Desviacion del eje numero 4 (DESVEJE4)
                            formulario.setP_9_ej4(medidas.getValor().toString());
                            break;
                    }
                }
            }
        }

    }

    public void cargarInformacionTaximetro() {
        for (Prueba pruebas : hojaPrueba.getListPruebas()) {
            if (pruebas.getTipoPrueba().getId() == 9 && pruebas.getAprobado().equals("Y")) {
                for (Medida medidas : pruebas.getMedidaList()) {
                    switch (medidas.getTipoMedida().getId()) {
                        case 9003: //Error de taximetro en distancia (ERRORXDIST)
                            formulario.setP_10_err_dis(medidas.getValor().toString());
                            formulario.setP_10_ref_com_lla(hojaPrueba.getVehiculo().getLlantas().getNombre());
                            break;
                        case 9002: //Error de taximetro en tirmpo (ERRORXTIEM)
                            formulario.setP_10_err_tie(medidas.getValor().toString());
                            break;
                    }
                }
            }
        }
    }

    public void cargarInformacionEmisionGasesCicloOtto() {
        for (Prueba pruebas : hojaPrueba.getListPruebas()) {
            if (pruebas.getTipoPrueba().getId() == 8) {

                for (Medida medidas : pruebas.getMedidaList()) {
                    switch (medidas.getTipoMedida().getId()) {
                        case 8006: //Temperatura en ralenti (TEMPR)
                        case 8022: // temperatura moto 2t
                            formulario.setP_11_tem_ral(medidas.getValor().toString());
                            break;
                        case 8011: //Revoluciones por minuto en crucero (RPMC)
                            formulario.setP_11_rpm_cru(medidas.getValor().toString());
                            break;
                        case 8005: //Revoluciones por minuto en ralenty (RPMR)
                        case 8028: // aplicado moto 2t
                            formulario.setP_11_rpm_ral((medidas.getValor().toString()));
                            break;
                        case 8002: //Monoxido de carbono en ralenty (COR)
                        case 8020: // moto 2t
                            formulario.setP_11_co_ral_val(medidas.getValor().toString());
                            break;
                        case 8003: //Dioxido de carbono en ralenty (CO2R)
                        case 8019:
                            formulario.setP_11_co2_ral_val(medidas.getValor().toString());
                            break;
                        case 8004: //Oxigeno en ralenty (O2R)
                        case 8021: // aplicado moto de 2t
                            formulario.setP_11_o2_ral_val(medidas.getValor().toString());
                            break;
                        case 8001: //HidroCarburos en ralenty (HCR)
                        case 8018: // aplicado a moto de 2 tiempos
                            formulario.setP_11_hc_ral_val(medidas.getValor().toString());
                            break;
                        case 8008: //Monoxido de carbono en crucero (COC)
                            formulario.setP_11_co_cru_val(medidas.getValor().toString());
                            break;
                        case 8009: //Dioxido de carbono en crucero (CO2C)
                            formulario.setP_11_co2_cru_val(medidas.getValor().toString());
                            break;
                        case 8010: //Oxigeno en crucero (O2C)
                            formulario.setP_11_o2_cru_val(medidas.getValor().toString());
                            break;
                        case 8007: //HidroCarburos en crucero (HCC)
                            formulario.setP_11_hc_cru_val(medidas.getValor().toString());
                        case 8012: //Temperatura en crucero (TEMPC)
                            formulario.setP_11_tem_cru((medidas.getValor().toString()));
                            break;
                        case 9137: //
                            formulario.setP_11_cat((medidas.getValor().toString()));
                            break;
                        case 8031: // 
                            //formulario.setP_11_hum_amb(redondear(medidas.getValor().doubleValue()).toString());
                            formulario.setP_11_hum_amb((medidas.getValor().toString()));//16-05-2023
                            break;
                        case 8032: //
                            formulario.setP_11_hum_rel((medidas.getValor().toString()));
                            break;
                        case 9146: //
                            formulario.setP_11_no_ral_val((medidas.getValor().toString()));
                            break;
                        case 9147: //
                            formulario.setP_11_no_cru_val((medidas.getValor().toString()));
                            break;

//                        case 9140: //
//                            formulario.setP_11_b_tem_ini(redondear(medidas.getValor().doubleValue()).toString());
//                            break;
//                        case 9141: //
//                            formulario.setP_11_b_tem_fin(redondear(medidas.getValor().doubleValue()).toString());
//                            break;
//                        case 9142: //
//                            formulario.setP_11_b_tem_amb(redondear(medidas.getValor().doubleValue()).toString());
//                            break;
//                        case 9143: //
//                            formulario.setP_11_b_hum(redondear(medidas.getValor().doubleValue()).toString());
//                            break;
//                        case 9144: //
//                            formulario.setP_11_b_lot(redondear(medidas.getValor().doubleValue()).toString());
//                            break;
                    }
                }
            }
        }
        formulario.setP_11_cat(this.hojaPrueba.getFormaMedTemperatura() == 'C' ? "SI" : "NO");
    }

    public void cargarInformacionEmisionGasesDiesel() {
        for (Prueba pruebas : hojaPrueba.getListPruebas()) {
            if (pruebas.getTipoPrueba().getId() == 8 && pruebas.getAprobado().equals("Y")) {
                for (Medida medidas : pruebas.getMedidaList()) {
                    switch (medidas.getTipoMedida().getId()) {
                        case 8035: //Velocidad minima prueba Diesel (VELMINIMA)
//                            dataRow[0] = medidas.getValormedida();
                            break;
                        case 8036: //Velocidad gobernadas prueba Diesel (VELGOBERNADAS)
                            formulario.setP_11_b_rpm(medidas.getValor().toString());
                            break;
                        case 8034: //Temperatura del motor prueba Diesel (TEMPMOTORDIESEL)
                            formulario.setP_11_b_tem_amb(medidas.getValor().toString()); //TODO: Verificar campo
                            formulario.setP_11_b_tem_ini(redondear(medidas.getValor().doubleValue()).toString());
                            break;
                        case 8033: //Primer ciclo para el opacimetro (CICLO1OP)
                            //formulario.setP_11_b_ci1(medidas.getValor().toString());
                            formulario.setP_11_b_ci1_d(medidas.getValor().toString());
                            break;
                        case 8013: //Segundo ciclo para el opacimetro (CICLO2OP)
                            //formulario.setP_11_b_ci2(medidas.getValor().toString());
                            formulario.setP_11_b_ci2_d(medidas.getValor().toString());
                            break;
                        case 8014: //Tercer ciclo para el opacimetro (CICLO3OP)
                            //formulario.setP_11_b_ci3(medidas.getValor().toString());
                            formulario.setP_11_b_ci3_d(medidas.getValor().toString());
                            break;
                        case 8015: //TODO: Falta Cuarto ciclo para el opacimetro
                            //formulario.setP_11_b_ci4(medidas.getValor().toString());
                            formulario.setP_11_b_ci4_d(medidas.getValor().toString());
                            break;
                        case 8017: //Promedio valor de los tres ciclos de la prueba de opacimetro (PROMVALOP)
                            //formulario.setP_11_b_res_val(medidas.getValor().toString());
                            formulario.setP_11_b_res_val_d(medidas.getValor().toString());
                            break;
                        // estos casos estaban en el método de gases de ciclo otto
                        case 8037: //
                            formulario.setP_11_b_tem_fin(redondear(medidas.getValor().doubleValue()).toString());
                            break;
                        case 8031: //
                            formulario.setP_11_b_tem_amb(redondear(medidas.getValor().doubleValue()).toString());
                            break;
                        case 8032: //
                            formulario.setP_11_b_hum(redondear(medidas.getValor().doubleValue()).toString());
                            break;
                        case 8038: //
                            formulario.setP_11_b_c1_gob(redondear(medidas.getValor().doubleValue()).toString());
                            break;
                        case 8039: //
                            formulario.setP_11_b_c2_gob(redondear(medidas.getValor().doubleValue()).toString());
                            break;
                        case 8040: //
                            formulario.setP_11_b_c3_gob(redondear(medidas.getValor().doubleValue()).toString());
                            break;
                        case 8041: //
                            formulario.setP_11_b_c4_gob(redondear(medidas.getValor().doubleValue()).toString());
                            break;

                    }
                }
            }
        }
//            EquiposJpaController1 equipos = new EquiposJpaController1();
//            equipos.buscarPorSerial(equipos.buscarPorSerial(pruebas.getSerialEquipo()).getLtoe());
        if (this.hojaPrueba.getVehiculo().getDiametro() != null) {
            formulario.setP_11_b_lot(this.hojaPrueba.getVehiculo().getDiametro().toString());
        } else {
            formulario.setP_11_b_lot("");
        }

    }

    public void datosVisual() {
        int countTipoAEnsenanza = 0;
        int countTipoBEnsenanza = 0;
        int countTipoAVisual = 0;
        int countTipoBVisual = 0;
        int countTipoAMecanica = 0;
        int countTipoBMecanica = 0;
        int countDefectosAnexo = 0;
        int countDefectosVisual = 0;
        int countDefectosMecanica = 0;
        for (Prueba pruebas : pruebasHoja) {
            tecnicos.add(pruebas.getTipoPrueba().getNombre().concat(":").concat(pruebas.getUsuarioFor().getNombre()));
            if (this.hojaPrueba.getVehiculo().getEsEnsenaza() == 1) { //Servicio enseñanza
                formulario.setP_e1_apr("SI");
            }

            try {
                for (Defxprueba defxprueba : pruebas.getDefxpruebaList()) {
                    if (defxprueba.getTercerEstado() != null && defxprueba.getTercerEstado().equalsIgnoreCase("na")) {
                        continue;
                    }
                    try {
                        if (defxprueba.getDefectos().getSubGrupo().getGrupo().getDefgroup() == 21) { //Servicio enseñanza                         
                            if (countDefectosAnexo == 0) {
                                //formulario.setP_d1_cod(defxprueba.getDefectos().getCardefault().toString());
//                              formulario.setP_d1_cod(defxprueba.getDefectos().getCodigoSuperintendencia());
                                formulario.setP_d1_cod(defxprueba.getDefectos().getCodigoResolucion());
                                formulario.setP_d1_des(String.format("%s", defxprueba.getDefectos().getNombreproblema()));
                                formulario.setP_d1_gru(String.format("%s", defxprueba.getDefectos().getSubGrupo().getDefectosList().get(0).getSubGrupo().getNombre()));
                            } else {
                                //formulario.setP_d1_cod(String.format("%s;%s", formulario.getP_d1_cod(), defxprueba.getDefectos().getCardefault().toString()));
                                //formulario.setP_d1_cod(String.format("%s;%s", formulario.getP_d1_cod(), defxprueba.getDefectos().getCodigoSuperintendencia()));
                                formulario.setP_d1_cod(String.format("%s;%s", formulario.getP_d1_cod(), defxprueba.getDefectos().getCodigoResolucion()));
                                formulario.setP_d1_des(String.format("%s;%s", formulario.getP_d1_des(), defxprueba.getDefectos().getNombreproblema()));
                                formulario.setP_d1_gru(String.format("%s;%s", formulario.getP_d1_gru(), defxprueba.getDefectos().getSubGrupo().getDefectosList().get(0).getSubGrupo().getNombre()));
                            }
                            if (defxprueba.getDefectos().getTipodefecto().equals("A")) {
                                if (!formulario.getP_d1_tip_def_a().isEmpty()) {
                                    formulario.setP_d1_tip_def_a(String.format("%s;%s", formulario.getP_d1_tip_def_a(), "A"));
                                } else {
                                    formulario.setP_d1_tip_def_a(String.format("%s", "A"));
                                }
                            } else if (defxprueba.getDefectos().getTipodefecto().equals("B")) {
                                if (!formulario.getP_d1_tip_def_b().isEmpty()) {
                                    formulario.setP_d1_tip_def_b(String.format("%s;%s", formulario.getP_d1_tip_def_b(), "B"));
                                } else {
                                    formulario.setP_d1_tip_def_b(String.format("%s", "B"));
                                }
                            }
                            if (defxprueba.getDefectos().getTipodefecto().equals("A")) {
                                formulario.setP_d1_tip_def_a_tot(String.valueOf(++countTipoAEnsenanza));
                                formulario.setP_e1_apr("NO");
                            } else {

                                formulario.setP_d1_tip_def_b_tot(String.valueOf(++countTipoBEnsenanza));

                            }
                            countDefectosAnexo++;
                            continue;
                        }
                        if (defxprueba.getDefectos().getSubGrupo().getGrupo().getDefgroup() == 1) { //Prueba visual
                            if (countDefectosVisual == 0) {
                                //formulario.setP_d_cod(defxprueba.getDefectos().getCodigoSuperintendencia());
                                formulario.setP_d_cod(defxprueba.getDefectos().getCodigoResolucion());
                                formulario.setP_d_des(String.format("%s", defxprueba.getDefectos().getNombreproblema()));
                                formulario.setP_d_gru(String.format("%s", defxprueba.getDefectos().getSubGrupo().getDefectosList().get(0).getSubGrupo().getNombre()));
                            } else {
                                //formulario.setP_d_cod(String.format("%s;%s", formulario.getP_d_cod(), defxprueba.getDefectos().getCodigoSuperintendencia()));
                                formulario.setP_d_cod(String.format("%s;%s", formulario.getP_d_cod(), defxprueba.getDefectos().getCodigoResolucion()));
                                formulario.setP_d_des(String.format("%s;%s", formulario.getP_d_des(), defxprueba.getDefectos().getNombreproblema()));
                                formulario.setP_d_gru(String.format("%s;%s", formulario.getP_d_gru(), defxprueba.getDefectos().getSubGrupo().getDefectosList().get(0).getSubGrupo().getNombre()));
                            }
                            if (defxprueba.getDefectos().getTipodefecto().equals("A")) {
                                if (!formulario.getP_d_tip_def_a().isEmpty()) {
                                    formulario.setP_d_tip_def_a(String.format("%s;%s", formulario.getP_d_tip_def_a(), "A"));
                                } else {
                                    formulario.setP_d_tip_def_a(String.format("%s", "A"));
                                }
                            } else if (defxprueba.getDefectos().getTipodefecto().equals("B")) {
                                if (!formulario.getP_d_tip_def_b().isEmpty()) {
                                    formulario.setP_d_tip_def_b(String.format("%s;%s", formulario.getP_d_tip_def_b(), "B"));
                                } else {
                                    formulario.setP_d_tip_def_b(String.format("%s", "B"));
                                }
                            }
                            if (defxprueba.getDefectos().getTipodefecto().equals("A")) {
                                formulario.setP_d_tip_def_a_tot(String.valueOf(++countTipoAVisual));
                            } else {
                                formulario.setP_d_tip_def_b_tot(String.valueOf(++countTipoBVisual));
                            }
                            countDefectosVisual++;
                        } else {
                            if (countDefectosMecanica == 0) {
                                //formulario.setP_c_cod(defxprueba.getDefectos().getCardefault().toString());
                                //formulario.setP_c_cod(defxprueba.getDefectos().getCodigoSuperintendencia());
                                formulario.setP_c_cod(defxprueba.getDefectos().getCodigoResolucion());
                                formulario.setP_c_des(String.format("%s", defxprueba.getDefectos().getNombreproblema()));
                                formulario.setP_c_gru(String.format("%s", defxprueba.getDefectos().getSubGrupo().getDefectosList().get(0).getSubGrupo().getNombre()));
                            } else {
                                //formulario.setP_c_cod(String.format("%s;%s", formulario.getP_c_cod(), defxprueba.getDefectos().getCardefault().toString()));
                                //formulario.setP_c_cod(String.format("%s;%s", formulario.getP_c_cod(), defxprueba.getDefectos().getCodigoSuperintendencia()));
                                formulario.setP_c_cod(String.format("%s;%s", formulario.getP_c_cod(), defxprueba.getDefectos().getCodigoResolucion()));
                                formulario.setP_c_des(String.format("%s;%s", formulario.getP_c_des(), defxprueba.getDefectos().getNombreproblema()));
                                formulario.setP_c_gru(String.format("%s;%s", formulario.getP_c_gru(), defxprueba.getDefectos().getSubGrupo().getDefectosList().get(0).getSubGrupo().getNombre()));
                            }
                            if (defxprueba.getDefectos().getTipodefecto().equals("A")) {
                                if (!formulario.getP_c_tip_def_a().isEmpty()) {
                                    formulario.setP_c_tip_def_a(String.format("%s;%s", formulario.getP_c_tip_def_a(), "A"));
                                } else {
                                    formulario.setP_c_tip_def_a(String.format("%s", "A"));
                                }
                            } else if (defxprueba.getDefectos().getTipodefecto().equals("B")) {
                                if (!formulario.getP_c_tip_def_b().isEmpty()) {
                                    formulario.setP_c_tip_def_b(String.format("%s;%s", formulario.getP_c_tip_def_b(), "B"));
                                } else {
                                    formulario.setP_c_tip_def_b(String.format("%s", "B"));
                                }
                            }
                            if (defxprueba.getDefectos().getTipodefecto().equals("A")) {
                                formulario.setP_c_tip_def_a_tot(String.valueOf(++countTipoAMecanica));
                            } else {
                                formulario.setP_c_tip_def_b_tot(String.valueOf(++countTipoBMecanica));
                            }
                            countDefectosMecanica++;
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e);
                    }
                }
            } catch (Throwable e) {
                int eve = 0;
            }
        }
    }

    public void datosObservaciones() {
        String comentario = "";
        String cmLabr = "";
        String infLabr;
        for (Prueba pruebas : pruebasHoja) {
            comentario = pruebas.getObservaciones();
            if (pruebas.getTipoPrueba().getId() == 1 && comentario != null) {

                if (cmLabr.length() > 3) {
                    cmLabr = cmLabr.concat("\n Profundidad labrado: ");
                } else {
                    cmLabr = "Profundidad labrado: ";
                }
                String[] lstObs = comentario.split("obs");
                String[] lstEjes = lstObs[0].split("&");
                for (int i = 0; i < lstEjes.length; i++) {
                    infLabr = lstEjes[i].replace("$", "-");
                    cmLabr = cmLabr.concat("Eje" + String.valueOf(i + 1)).concat(" ").concat(infLabr).concat("mm; ");
                }
                if (lstObs.length > 1) {
                    cmLabr = cmLabr.concat("\n").concat(" ").concat(lstObs[1]);
                }
            }
            if (pruebas.getTipoPrueba().getId() == 2 && comentario != null) {
                cmLabr = cmLabr.concat(", Luces: ").concat(comentario);

            }
            if (pruebas.getTipoPrueba().getId() == 8 && comentario != null) {
                cmLabr = cmLabr.concat(", Gases: ").concat(comentario);
            }
            if (pruebas.getTipoPrueba().getId() == 5 && comentario != null) {
                cmLabr = cmLabr.concat(", Frenos: ").concat(comentario);
            }
        }
        if (cmLabr != null) {
            formulario.setP_f_com_obs(String.format("%s", cmLabr));
        }
    }

    /**
     * @return the formulario
     */
    public Formulario_v3 getFormulario() {
        return formulario;
    }

    /**
     * @return the pin
     */
    public Pin getPin() {
        return pin;
    }

    public void cargarInformacionPin() {
        pin.setUsuario(cda.getUsuarioSicov());
        pin.setClave(cda.getPasswordSicov());
        pin.setP_placa(hojaPrueba.getVehiculo().getPlaca());
    }

    public void obtenerListadoTecnicos() {
        int index = 0;
        for (String tecnico : tecnicos) {
            if (index == 0) {
                formulario.setP_h_nom_ope_rea_rev_tec(tecnico);
            } else {
                formulario.setP_h_nom_ope_rea_rev_tec(formulario.getP_h_nom_ope_rea_rev_tec().concat(";").concat(tecnico));
            }
            index++;
        }
    }

    /**
     * Metodo encargado de redondear las cifras decimales a multiplos de diez
     *
     * @param val
     * @return
     */
    public static Integer redondear(Double val) {
        Integer x = (int) Math.round((val));
        System.out.println(x);
        String valor = String.valueOf(x);
        Integer valor2 = Integer.parseInt(valor.substring(valor.length() - 1));
        if (valor2 != 0) {
            if (valor2 >= 5) {
                return Integer.parseInt(valor.substring(0, valor.length() - 1) + 0) + 10;
            } else {
                return Integer.parseInt(valor.substring(0, valor.length() - 1) + 0);
            }
        } else {
            return x;
        }
    }

    void verificarInformacio() {
        //SicovDao sicovDao = new SicovDao();
        formulario = new Formulario_v3();
        // hojaPrueba = sicovDao.buscarHojaPruebaById(4);
        // pruebasHoja = hojaPrueba.getPruebasList();
        datosVisual();
        datosPresionLabrado();
        datosObservaciones();
        obtenerListadoTecnicos();

        System.out.println(formulario.toString());
    }

    public static void main(String[] args) {
        HojaPruebasJpaController hojasjps = new HojaPruebasJpaController();
        CdaJpaController cda = new CdaJpaController();
        ClienteCi2Servicio ci2Servicio = new ClienteCi2Servicio(hojasjps.find(175116), cda.find(1));
        ci2Servicio.datosVisual();

    }

    private String consultarFechaIngresoVehiculo(int idPrueba) {
        String statement = "SELECT hp.Fecha_ingreso_vehiculo FROM pruebas as p " +
                           "INNER JOIN hoja_pruebas as hp ON p.hoja_pruebas_for = hp.TESTSHEET " +
                           "INNER JOIN vehiculos as v ON hp.Vehiculo_for = v.CAR " +
                           "WHERE hp.TESTSHEET = ?";
        try {
            Connection cn = cargarConexion();
            PreparedStatement consultaFechaIngreso = cn.prepareStatement(statement);
            consultaFechaIngreso.setInt(1, idPrueba);
            ResultSet rs = consultaFechaIngreso.executeQuery();
    
            // Reemplazar por rs.next()
            while (rs.next()) {
                System.out.println("-------sin parsear " + rs.getDate("Fecha_ingreso_vehiculo"));
                System.out.println("-------to string " + rs.getDate("Fecha_ingreso_vehiculo").toString());
                return rs.getDate("Fecha_ingreso_vehiculo").toString();
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error en el método: consultarObservacionesFrenos() " + e.getMessage() + e.getLocalizedMessage());
        }
        return null;
    }

    public Connection cargarConexion() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Conexion conex = Conexion.getInstance();
        Connection conexion = DriverManager.getConnection(conex.getUrl(), conex.getUsuario(), conex.getContrasena());
        return conexion;
    }

    /**
     * los permisibles dependen del tipo del vehiculo, del modelo del vehiculo
     * de los tiempos y del tipo de combustible, los permisibles de las pruebas
     * estan harcoded en cada hilo de la prueba
     *
     * @param cn
     * @param hojaPruebas
     * @throws SQLException
     */
    public void configurarPermisibles() throws ParseException {
        //consultaResolucion();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaInicioResolucion = sdf.parse("2022-08-18");
        int idHojaPrueba = this.hojaPrueba.getId();
        Date fechaIngresoVehiculo = sdf.parse(consultarFechaIngresoVehiculo(idHojaPrueba));

        if (idTipoVehiculo == 5) {//Motocarro
            //parametros.put("PerO2","[0-11]");
            formulario.setP_11_o2_ral_nor("---");
            formulario.setP_11_co2_ral_nor("---");

            //Desviacion
            formulario.setP_9_max("---");
            //Taximetro
            formulario.setP_10_max("---");
            //Suspension
            formulario.setP_7_min("---");
            //Frenos
            formulario.setP_8_efi_tot_min("30");
            formulario.setP_8_efi_aux_min("18");
            formulario.setP_8_ej1_max("30");
            formulario.setP_8_ej2_max("30");
            formulario.setP_8_ej3_max("30");
            formulario.setP_8_ej4_max("30");
            formulario.setP_8_ej5_max("30");
            formulario.setP_6_max("---");
            //Desviacion : el permisible es solamente uno
            //Dispositivos de cobro: el permisible es solamente uno
            //Gases :        
            if (tiempoMotor == 4) {
                formulario.setP_11_o2_ral_nor("[0-6]");
            }
            if (tiempoMotor == 2) {
                formulario.setP_11_o2_ral_nor("[0-11]");
            }
            if (tiempoMotor == 2 && modelo <= 2009) {
                formulario.setP_11_hc_ral_nor("[0-10000]");
            } else {
                formulario.setP_11_hc_ral_nor("[0-2000]");
            }
            formulario.setP_11_hc_cru_nor("---");
            formulario.setP_11_co_cru_nor("---");
            formulario.setP_11_hc_ral_nor("[0-4.5]");
            formulario.setP_11_co_ral_nor("[0-4.5]");
            formulario.setP_11_b_res_nor("---");
            //no mostrar permisibles de Opacidad porque ninguna moto es diesel
            formulario.setP_11_b_res_nor("---");

        }

        if (idTipoVehiculo == 4) {//Motocicletas
            //parametros.put("PerO2","[0-11]");
            formulario.setP_11_o2_ral_nor("---");
            formulario.setP_11_co2_ral_nor("---");

            //Desviacion
            formulario.setP_9_max("---");
            //Taximetro
            formulario.setP_10_max("---");
            //Suspension
            formulario.setP_7_min("---");
            //Frenos
            formulario.setP_8_efi_tot_min("30");
            formulario.setP_8_efi_aux_min("---");
            formulario.setP_8_ej1_max("---");
            formulario.setP_8_ej2_max("---");
            formulario.setP_8_ej3_max("---");
            formulario.setP_8_ej4_max("---");
            formulario.setP_8_ej5_max("---");
            formulario.setP_6_max("---");

            formulario.setP_11_o2_ral_nor("---");
            formulario.setP_11_co2_ral_nor("---");
            //Desviacion : el permisible es solamente uno
            //Dispositivos de cobro: el permisible es solamente uno
            //Gases :
            if (tiempoMotor == 4) {
                formulario.setP_11_o2_ral_nor("[0-6]");
            }
            if (tiempoMotor == 2) {
                formulario.setP_11_o2_ral_nor("[0-11]");
            }
            if (tiempoMotor == 2) {
                if (modelo <= 2009) {
                    formulario.setP_11_hc_ral_nor("[0-8000]");
                    formulario.setP_11_co_ral_nor("[0-3.5]");
                } else {
                    formulario.setP_11_hc_ral_nor("[[0-1600]]");
                    formulario.setP_11_co_ral_nor("[0-3.5]");
                }
            } else {//4t
                formulario.setP_11_hc_ral_nor("[0-1300]");
                formulario.setP_11_co_ral_nor("[0-3.5]");
//                if (fechaIngresoVehiculo.compareTo(fechaInicioResolucion) > 0) {
//                    formulario.setP_11_hc_ral_nor("[0-1600]");
//                } else {
//                    formulario.setP_11_hc_ral_nor("[0-1600]");
//                    System.out.println("aqui estoy--------------------------------------------");
//                }
                //-formulario.setP_11_hc_ral_nor("[0-2000]");
            }
            formulario.setP_11_hc_cru_nor("---");
            formulario.setP_11_co_cru_nor("---");
//            if (fechaIngresoVehiculo.compareTo(fechaInicioResolucion) > 0) {
//                formulario.setP_11_co_ral_nor("[0-3.5]");
//            } else {
//                formulario.setP_11_co_ral_nor("[0-4.5]");
//            }
            //-formulario.setP_11_co_ral_nor("[0-4.5]");
            //no mostrar permisibles de Opacidad porque ninguna moto es diesel
            formulario.setP_11_b_res_nor("---");
        } else if (idTipoVehiculo == 1 || idTipoVehiculo == 3 || idTipoVehiculo == 2) {// 1 -> Livianos ,  3-> pesados, 2-> 4x4
            formulario.setP_11_o2_cru_nor("[0-5]");
            formulario.setP_11_co2_cru_nor("[ <7 ]");
            formulario.setP_11_o2_ral_nor("[0-5]");
            formulario.setP_11_co2_ral_nor("[ <7 ]");
            //Desviacion
            formulario.setP_9_max("10");
            //Taximetro
            if (claseVehiculo == 2)//si es taxi poner esto:...
            {
                formulario.setP_10_max("2");
            } else {
                formulario.setP_10_max("---");
            }
            //Suspension: se realiza suspension a todos los vehiculos livianos
            //if(idTipoVehiculo== 1)
            formulario.setP_7_min("40");
            //else
            //parametros.put("PerSusp","---");//a pesados no se realiza suspension
            //Frenos:

            formulario.setP_8_efi_tot_min("50");
            formulario.setP_8_efi_aux_min("18");
            formulario.setP_8_ej1_max("30");
            formulario.setP_8_ej2_max("30");
            formulario.setP_8_ej3_max("30");
            formulario.setP_8_ej4_max("30");
            formulario.setP_8_ej5_max("30");
            formulario.setP_6_max("225");
            //Gases 
            switch (tipoCombustible) {
                case 1:
                    //si es gasolina
                    if (modelo <= 1984) {
                        formulario.setP_11_hc_ral_nor("[0-650]");
                        formulario.setP_11_co_ral_nor("[0-4.0]");
                        formulario.setP_11_hc_cru_nor("[0-650]");
                        formulario.setP_11_co_cru_nor("[0-4.0]");
                    } else if (modelo >= 1985 && modelo <= 1997) {
                        formulario.setP_11_hc_ral_nor("[0-400]");
                        formulario.setP_11_co_ral_nor("[0-3.0]");
                        formulario.setP_11_hc_cru_nor("[0-400]");
                        formulario.setP_11_co_cru_nor("[0-3.0]");
                    } else if (modelo >= 1998 && modelo <= 2009) {
                        formulario.setP_11_hc_ral_nor("[0-200]");
                        formulario.setP_11_co_ral_nor("[0-1.0]");
                        formulario.setP_11_hc_cru_nor("[0-200]");
                        formulario.setP_11_co_cru_nor("[0-1.0]");
                    } else if (modelo >= 2010) {
                        formulario.setP_11_hc_ral_nor("[0-160]");
                        formulario.setP_11_co_ral_nor("[0-0.8]");
                        formulario.setP_11_hc_cru_nor("[0-160]");
                        formulario.setP_11_co_cru_nor("[0-0.8]");
                    }
                    formulario.setP_11_b_res_nor("---");
                    break;
                case 4:
                case 2:
                    //si es gas gasolina o es gas natural vehicular
                    if (modelo <= 1970) {
                        formulario.setP_11_hc_ral_nor("[0-800]");
                        formulario.setP_11_co_ral_nor("[0-5.0]");
                        formulario.setP_11_hc_cru_nor("[0-800]");
                        formulario.setP_11_co_cru_nor("[0-5.0]");
                    } else if (modelo > 1970 && modelo <= 1984) {
                        formulario.setP_11_hc_ral_nor("[0-650]");
                        formulario.setP_11_co_ral_nor("[0-4.0]");
                        formulario.setP_11_hc_cru_nor("[0-650]");
                        formulario.setP_11_co_cru_nor("[0-4.0]");
                    } else if (modelo > 1984 && modelo <= 1997) {
                        formulario.setP_11_hc_ral_nor("[0-400]");
                        formulario.setP_11_co_ral_nor("[0-3.0]");
                        formulario.setP_11_hc_cru_nor("[0-400]");
                        formulario.setP_11_co_cru_nor("[0-3.0]");
                    } else if (modelo > 1997) {
                        formulario.setP_11_hc_ral_nor("[0-200]");
                        formulario.setP_11_co_ral_nor("[0-1.0]");
                        formulario.setP_11_hc_cru_nor("[0-200]");
                        formulario.setP_11_co_cru_nor("[0-1.0]");
                    }
                    break;
                //end of diesel
                case 3:
                    //si es diesel
                    formulario.setP_11_hc_ral_nor("---");
                    formulario.setP_11_co_ral_nor("---");
                    formulario.setP_11_hc_cru_nor("---");
                    formulario.setP_11_co_cru_nor("---");
//                    if (modelo <= 1970) {
//                        formulario.setP_11_b_res_nor("50");
//                    } else if (modelo > 1970 && modelo <= 1984) {
//                        formulario.setP_11_b_res_nor("45");
//                    } else if (modelo > 1984 && modelo <= 1997) {
//                        formulario.setP_11_b_res_nor("40");
//                    } else if (modelo > 1997) {
//                        formulario.setP_11_b_res_nor("35");
//                    }
                    formulario.setP_11_b_res_nor("");
                    if (modelo <= 2000) {
                        if (cilindraje < 5000) {
                            formulario.setP_11_b_res_nor_d("6.0");
                            formulario.setP_11_b_res_nor_d_CC("CC <5000");
                        } else if (cilindraje >= 5000) {
                            formulario.setP_11_b_res_nor_d("5.5");
                            formulario.setP_11_b_res_nor_d_CC(" CC ≥ 5000");
                        }
                    } else if (modelo > 2000 && modelo <= 2015) {
                        if (cilindraje < 5000) {
                            formulario.setP_11_b_res_nor_d("5.0");
                            formulario.setP_11_b_res_nor_d_CC("CC <5000");
                        } else if (cilindraje >= 5000) {
                            formulario.setP_11_b_res_nor_d("4.5");
                            formulario.setP_11_b_res_nor_d_CC(" CC ≥ 5000");
                        }
                    } else {
                        if (modelo < 5000) {
                            formulario.setP_11_b_res_nor_d("4.0");
                            formulario.setP_11_b_res_nor_d_CC("CC <5000");
                        } else if (modelo >= 5000) {
                            formulario.setP_11_b_res_nor_d("3.5");
                            formulario.setP_11_b_res_nor_d_CC(" CC ≥ 5000");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }//end of methoc configurarPermisibles

    public void datosPresionLabrado() {
        int tipoVehiculo = hojaPrueba.getVehiculo().getTipoVehiculo().getId();
        for (Prueba pruebas : hojaPrueba.getListPruebas()) {
            if (pruebas.getTipoPrueba().getId() == 1) {
                List<Medida> m = pruebas.getMedidaList();

//                formulario.setP_d2_ej1_izq(lp2(m, 9004));
//                formulario.setP_d2_ej2_izq_r1(lp2(m, 9005));
//                formulario.setP_d2_ej2_izq_r2(lp2(m, 9009));
//                formulario.setP_d2_ej3_izq_r1(lp2(m, 9006));
//                formulario.setP_d2_ej3_izq_r2(lp2(m, 9010));
//                formulario.setP_d2_ej4_izq_r1(lp2(m, 9007));
//                formulario.setP_d2_ej4_izq_r2(lp2(m, 9011));
//                formulario.setP_d2_ej5_izq_r1(lp2(m, 9009));
//                formulario.setP_d2_ej5_izq_r2(lp2(m, 9009));
//                if (tipoVehiculo == 4) {
//                    formulario.setP_d2_ej1_der(lp2(m, 9046));
//                    formulario.setP_d2_ej2_der_r1(lp2(m, 9047));
//                } else {
//                    formulario.setP_d2_ej1_der(lp2(m, 9013));
//                    formulario.setP_d2_ej2_der_r1(lp2(m, 9014));
//                }
//                formulario.setP_d2_ej2_der_r2(lp2(m, 9018));
//                formulario.setP_d2_ej3_der_r1(lp2(m, 9015));
//                formulario.setP_d2_ej3_der_r2(lp2(m, 9019));
//                formulario.setP_d2_ej4_der_r1(lp2(m, 9016));
//                formulario.setP_d2_ej4_der_r2(lp2(m, 9020));
//                formulario.setP_d2_ej5_der_r1(lp2(m, 9017));
//                formulario.setP_d2_ej5_der_r2(lp2(m, 9021));
//                formulario.setP_d2_rep_r1(lp2(m, 9040));
//                formulario.setP_d2_rep_r2(lp2(m, 9041));
                formulario.setP_d2_ej1_izq(lp(m, 9004, 9022));
                formulario.setP_d2_ej2_izq_r1(lp(m, 9005, 9023));
                formulario.setP_d2_ej2_izq_r2(lp(m, 9009, 9027));
                formulario.setP_d2_ej3_izq_r1(lp(m, 9006, 9024));
                formulario.setP_d2_ej3_izq_r2(lp(m, 9010, 9028));
                formulario.setP_d2_ej4_izq_r1(lp(m, 9007, 9025));
                formulario.setP_d2_ej4_izq_r2(lp(m, 9011, 9029));
                formulario.setP_d2_ej5_izq_r1(lp(m, 9009, 9027));
                formulario.setP_d2_ej5_izq_r2(lp(m, 9009, 9027));
                if (tipoVehiculo == 4) {
                    formulario.setP_d2_ej1_der(lp(m, 9046, 9048));
                    formulario.setP_d2_ej2_der_r1(lp(m, 9047, 9049));
                } else {
                    formulario.setP_d2_ej1_der(lp(m, 9013, 9031));
                    formulario.setP_d2_ej2_der_r1(lp(m, 9014, 9032));
                }
                formulario.setP_d2_ej2_der_r2(lp(m, 9018, 9036));
                formulario.setP_d2_ej3_der_r1(lp(m, 9015, 9033));
                formulario.setP_d2_ej3_der_r2(lp(m, 9019, 9037));
                formulario.setP_d2_ej4_der_r1(lp(m, 9016, 9034));
                formulario.setP_d2_ej4_der_r2(lp(m, 9020, 9038));
                formulario.setP_d2_ej5_der_r1(lp(m, 9017, 9035));
                formulario.setP_d2_ej5_der_r2(lp(m, 9021, 9039));
                formulario.setP_d2_rep_r1(lp(m, 9040, 9043));
                formulario.setP_d2_rep_r2(lp(m, 9041, 9044));
                formulario.setP_d2_p_ej1_izq("");
                formulario.setP_d2_p_ej2_izq_r1("");
                formulario.setP_d2_p_ej2_izq_r2("");
                formulario.setP_d2_p_ej3_izq_r1 ("");
                formulario.setP_d2_p_ej3_izq_r2("");
                formulario.setP_d2_p_ej4_izq_r1("");
                formulario.setP_d2_p_ej4_izq_r2("");
                formulario.setP_d2_p_ej5_izq_r1("");
                formulario.setP_d2_p_ej5_izq_r2("");
                formulario.setP_d2_p_ej1_der("");
                formulario.setP_d2_p_ej2_der_r1("");
                formulario.setP_d2_p_ej2_der_r2("");
                formulario.setP_d2_p_ej3_der_r1("");
                formulario.setP_d2_p_ej3_der_r2("");
                formulario.setP_d2_p_ej4_der_r1("");
                formulario.setP_d2_p_ej4_der_r2("");
                formulario.setP_d2_p_ej5_der_r1("");
                formulario.setP_d2_p_ej5_der_r2("");
                formulario.setP_d2_p_rep_r1("");
                formulario.setP_d2_p_rep_r2("");
            }
        }
    }

    private String lp(List<Medida> m, int med1, int med2) {
        String valor = "";
        for (Medida medida : m) {
            if (medida.getTipoMedida().getId().equals(med1)) {
                valor = medida.getValor().toString();
            }
        }

        for (Medida medida : m) {
            if (medida.getTipoMedida().getId().equals(med2)) {
                valor = valor.concat(";").concat(medida.getValor().toString());
            }
        }

        return valor;
    }

    private String lp2(List<Medida> m, int med1) {
        String valor = "";
        for (Medida medida : m) {
            if (medida.getTipoMedida().getId().equals(med1)) {
                valor = medida.getValor().toString();
            }
        }
        return valor;
    }

    public void otrosDatos() {
        formulario.setP_i_sof_rev(this.cda.getNombreSoftware());
        EquiposJpaController1 equiposJpaController1 = new EquiposJpaController1();
        for (Prueba prueba : this.hojaPrueba.getListPruebas()) {
            Equipo equipo = equiposJpaController1.buscarPorSerial(prueba.getSerialEquipo());
            if (equipo != null) {
                formulario.setP_h_equ_rev(formulario.getP_h_equ_rev().concat(String.format("%s:%s;", equipo.getMarca(), equipo.getResolucionambiental())));
            }else if(prueba.getTipoPrueba().getId() == 8 && prueba.getSerialEquipo().contains("~")) {
                String pef = prueba.getSerialEquipo().split("~")[2].split("-")[0];
                formulario.setP_h_equ_rev(formulario.getP_h_equ_rev().concat(String.format("%s:%s;", prueba.getSerialEquipo().split("~")[1].split(";")[0], prueba.getSerialEquipo().split("~")[2].replace(pef, ""))));
            }

        }
    }

    private String ponerVacio(float numero) {
        if (numero != 0) {
            return String.valueOf(numero);
        }
        return "";
    }

}

/*
 * Copyright 2016 SOLTELEC S.A.S.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soltelec.consolaentrada.sicov.indra;

import com.ctc.wstx.shaded.msv_core.util.Util;
import com.soltelec.consolaentrada.indra.dto.*;
import com.soltelec.consolaentrada.models.entities.Cda;
import com.soltelec.consolaentrada.models.entities.Defxprueba;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.Medida;
import com.soltelec.consolaentrada.models.entities.Prueba;
import com.soltelec.consolaentrada.models.controllers.EquiposJpaController1;
import com.soltelec.consolaentrada.models.controllers.UsuarioJpaController;
import com.soltelec.consolaentrada.models.entities.Equipo;
import com.soltelec.consolaentrada.models.entities.Reinspeccion;
import com.soltelec.consolaentrada.models.entities.Usuario;
import com.soltelec.consolaentrada.reporte.LlamarReporte;
import com.soltelec.consolaentrada.utilities.Mensajes;
import com.soltelec.consolaentrada.utilities.UtilPropiedades;
import com.soltelec.consolaentrada.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.management.RuntimeErrorException;

import org.jboss.resteasy.util.Base64;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jfree.util.Log;

/**
 *
 * @author GerenciaDesarollo
 */
public class ClienteIndraServicio {

    private HojaPruebas hojaPrueba;
    private Cda cda;
    private DatosTaximetro datosTaximetro;
    private DatosVisual datosVisual;
    private DatosFas datosFas;
    private DatosGasesMotos datosGasesMotos;
    private DatosGasesGasolina datosGasesGasolina;
    private PreviaGasesVehiculosDisel gasesVehiculosDisel;
    private PreviaGasesVehiculos previaGasesVehiculos;
    private DatosGasesDiesel datosGasesDiesel;
    private DatosFotos datosFotos;
    private Vehiculo vehiculo;
    private Propietario propietario;
    private String datosObservaciones;
    private DatosLuces datosLuces;
    private DatosRuido datosRuido;
    private PreviaGasesMotos previaGasesMotos;
    private DatosRunt datosRunt;
    private DatosLlantas datosLlantas;
    private DatosEquipos datosEquipos;
    private DatosFirma datosFirma;
    private DatosSoftware datossoftware;
    private DatosFurAsociados datosFurAsociados;
    private List<Prueba> pruebasHoja;
    private Reinspeccion Reinspeccion;
    private JsonEquipos equipos;

    Logger log = Logger.getLogger(ClienteIndraServicio.class);

    public ClienteIndraServicio() {
        datosRunt = new DatosRunt();
    }

    public ClienteIndraServicio(HojaPruebas ctxHojaPrueba, Cda cda) {
        this.hojaPrueba = ctxHojaPrueba;
        this.cda = cda;
        pruebasHoja = new ArrayList();
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
        datosRunt = new DatosRunt();
    }

    public void datosPropietario() {
        propietario = new Propietario();
        propietario.setNombreRazon(String.format("%s %s", this.hojaPrueba.getPropietario().getNombres(), this.hojaPrueba.getPropietario().getApellidos()));
        propietario.setTipoDocumentoIdentidad(cargarTipoIDentificacion(this.hojaPrueba.getPropietario().getTipoIdentificacion().name())); //TODO: Cambiar en la base de datos la forma en que se cargan los tipos de documentos 
        propietario.setNoIdentidad(this.hojaPrueba.getPropietario().getId().toString());
        propietario.setDireccion(this.hojaPrueba.getPropietario().getDireccion());
        if (this.hojaPrueba.getPropietario().getCelular() != null) {
            propietario.setTelefono(this.hojaPrueba.getPropietario().getCelular());
        } else {
            propietario.setTelefono(this.hojaPrueba.getPropietario().getTelefono());
        }
        propietario.setCiudad(this.hojaPrueba.getPropietario().getCiudad().getNombre());
        propietario.setDepartamento(this.hojaPrueba.getPropietario().getCiudad().getDepartamento().getNombre());
        propietario.setCorreo(this.hojaPrueba.getPropietario().getEmail());
    }

    public void datosVehiculo(String kilometraje) {
        vehiculo = new Vehiculo();
        vehiculo.setOperario(this.hojaPrueba.getResponsable().getNombre());
        vehiculo.setNoIdentificaiconOperario(this.hojaPrueba.getResponsable().getCedula());
        vehiculo.setPlaca(this.hojaPrueba.getVehiculo().getPlaca().trim().toUpperCase());
        vehiculo.setPais(this.hojaPrueba.getVehiculo().getPais().getNombre());
        vehiculo.setServicio(this.hojaPrueba.getVehiculo().getServicios().getServicioSuper().toString());//envio del codigo servicio de la super
        vehiculo.setClase(this.hojaPrueba.getVehiculo().getClaseVehiculo().getId().toString());//envio del codigo de la clase
        vehiculo.setMarca(this.hojaPrueba.getVehiculo().getMarca().getNombre());
        vehiculo.setLinea(this.hojaPrueba.getVehiculo().getLineaVehiculo().getNombre());
        vehiculo.setModelo(this.hojaPrueba.getVehiculo().getModelo());
        vehiculo.setNoLicenciaDeTransito(this.hojaPrueba.getVehiculo().getLicencia());
        vehiculo.setFechaMatricula(UtilPropiedades.convertiFechas(this.hojaPrueba.getVehiculo().getFechaRegistro(), "yyyy-MM-dd hh:mm:ss"));
        vehiculo.setColor(this.hojaPrueba.getVehiculo().getColor().getNombre());
        vehiculo.setCombustible(this.hojaPrueba.getVehiculo().getTipoGasolina().getId());
        vehiculo.setVinchasis(this.hojaPrueba.getVehiculo().getVin());
        vehiculo.setNoMotor(this.hojaPrueba.getVehiculo().getMotor());
        vehiculo.setTipoMotor(this.hojaPrueba.getVehiculo().getTiemposMotor() == 4 ? 2 : 1);
        vehiculo.setCilindraje(this.hojaPrueba.getVehiculo().getCilindraje());
        //JFM-------------------------------------------------------------------------------------------------------------------------------------------------------
        if (kilometraje.equalsIgnoreCase("0")) {
            kilometraje = "";
            vehiculo.setKilometraje(kilometraje);
        } else {
            vehiculo.setKilometraje(kilometraje);
        }
        //JFM-------------------------------------------------------------------------------------------------------------------------------------------------------

        vehiculo.setKilometraje(kilometraje);
        vehiculo.setNumSillas(this.hojaPrueba.getVehiculo().getSillas());
        vehiculo.setBlindaje(this.hojaPrueba.getVehiculo().getBlindaje().equals("Y"));

        //pendiente 
        if (this.hojaPrueba.getVehiculo().getPotencia() != null) {
            vehiculo.setPotencia(this.hojaPrueba.getVehiculo().getPotencia());
        } else {
            vehiculo.setPotencia(0);
        }

        if (this.hojaPrueba.getVehiculo().getTipoCarroceria() != null) {
            System.out.print("------------");
            System.out.print("tipo de carroceria:" + this.hojaPrueba.getVehiculo().getTipoCarroceria().getTipo());
            vehiculo.setTipoCarroceria(this.hojaPrueba.getVehiculo().getTipoCarroceria().getTipo());
            System.out.print("------------");
        } else {
            vehiculo.setTipoCarroceria(0);
        }

        if (this.hojaPrueba.getVehiculo().getFechaVencimientoGnv() != null) {
            vehiculo.setFechaVencimientoConversionGNV(UtilPropiedades.convertiFechas(this.hojaPrueba.getVehiculo().getFechaVencimientoGnv(),
                    "yyyy-MM-dd"));
        } else {
            vehiculo.setFechaVencimientoConversionGNV("");
        }

        if (this.hojaPrueba.getFechaVencSoat() != null) {
            vehiculo.setFechaVencimientoSoat(UtilPropiedades.convertiFechas(this.hojaPrueba.getFechaVencSoat(),
                    "yyyy-MM-dd"));
        } else {
            vehiculo.setFechaVencimientoSoat("0000-00-00");
        }

        if (this.hojaPrueba.getVehiculo().getEsConversionGnv() != null) {
            switch (this.hojaPrueba.getVehiculo().getEsConversionGnv()) {
                case "SI":
                    vehiculo.setConversionGNV("S");
                    break;
                case "NO":
                    vehiculo.setConversionGNV("N");
                    break;
                default:
                    vehiculo.setConversionGNV("NA");
                    break;
            }
        }

        if (this.hojaPrueba.getReinspeccionList().size() > 0) {
            vehiculo.setReinspeccion(true);
            Reinspeccion = this.hojaPrueba.getReinspeccionList().iterator().next();
            vehiculo.setFechaPrueba(UtilPropiedades.convertiFechas(Reinspeccion.getFechaSiguiente(), "yyyy-MM-dd hh:mm:ss"));
        } else {
            vehiculo.setReinspeccion(false);
            vehiculo.setFechaPrueba(UtilPropiedades.convertiFechas(this.hojaPrueba.getFechaIngreso(), "yyyy-MM-dd hh:mm:ss"));
        }

        if (this.hojaPrueba.getAprobado().equalsIgnoreCase("Y")) {
            vehiculo.setEdoRevision(1);
            System.out.println("El estado revision tomado es 1: ");
        } else {
            vehiculo.setEdoRevision(2);
            System.out.println("El estado revision tomado es 2: ");
        }

    }

    public void datosVehiculoSegFur() {

    }

    public void datosFotos() {
        datosFotos = new DatosFotos();
        datosFotos.setPlaca(this.hojaPrueba.getVehiculo().getPlaca().trim());
        try {
            datosFotos.setFotoDelantera(Base64.encodeBytes(this.hojaPrueba.getFotosList().get(0).getFoto1()));
            datosFotos.setFotoTrasera(Base64.encodeBytes(this.hojaPrueba.getFotosList().get(0).getFoto2()));
            System.out.println("Ubic ClienteIndraServicio, Se han Cargado el arrays de Bytes");
        } catch (Exception e) {
            System.out.println("Se cayo la carga de los arrys de imagenes");
            //throw new SartComunicadorException(500, "La revision no tiene las imagenes completas");
        }
    }

    //--------------------------------------------------------------------------
    //-----------------------          GASES               ---------------------
    //--------------------------------------------------------------------------
    public void datosGasesDiesel() {
        datosGasesDiesel = new DatosGasesDiesel();
        for (Prueba pruebas : pruebasHoja) {
            if (pruebas.getTipoPrueba().getId() == 8 && hojaPrueba.getVehiculo().getTipoGasolina().getId() == 3) {
                for (Medida medidas : pruebas.getMedidaList()) {
                    switch (medidas.getTipoMedida().getId()) {
                        case 8033: //ciclo 0 para el opacimetro (CICLO0 OP)
                            //datosGasesDiesel.setAceleracion0(UtilPropiedades.decimalFormat(medidas.getValor()));
                            datosGasesDiesel.setDensidadHumo0(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8036: //Velocidad gobernadas prueba Diesel (VELGOBERNADAS)
                            datosGasesDiesel.setRpmGobernada(UtilPropiedades.decimalFormat(Math.ceil(medidas.getValor())));
                            break;
                        case 8031: //Temperatura del motor prueba Diesel (TEMPMOTORDIESEL)
                            datosGasesDiesel.setTemperatura(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8013: //Primer ciclo para el opacimetro (CICLO1OP)
                            //datosGasesDiesel.setAceleracion1(UtilPropiedades.decimalFormat(medidas.getValor()));
                            datosGasesDiesel.setDensidadHumo1(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8014: //Segundo ciclo para el opacimetro (CICLO2OP)
                            //datosGasesDiesel.setAceleracion2(UtilPropiedades.decimalFormat(medidas.getValor()));
                            datosGasesDiesel.setDensidadHumo2(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8015: //Tercer ciclo para el opacimetro (CICLO3OP)
                            //datosGasesDiesel.setAceleracion3(UtilPropiedades.decimalFormat(medidas.getValor()));
                            datosGasesDiesel.setDensidadHumo3(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8017: //Promedio valor de los tres ciclos de la prueba de opacimetro (PROMVALOP)
                            //datosGasesDiesel.setValorFinal(UtilPropiedades.decimalFormat(medidas.getValor()));
                            datosGasesDiesel.setValorFinalDensidad(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8035:
                            datosGasesDiesel.setRpmRalenti(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8038:
                            datosGasesDiesel.setVelocidadGobernada0(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8039:
                            datosGasesDiesel.setVelocidadGobernada1(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8040:
                            datosGasesDiesel.setVelocidadGobernada2(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8041:
                            datosGasesDiesel.setVelocidadGobernada3(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8034:
                            datosGasesDiesel.setTemperaturaInicial(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8037:
                            datosGasesDiesel.setTemperaturaFinal(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8032:
                            datosGasesDiesel.setHumedadRelativa(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                    }
                }
                if (this.hojaPrueba.getVehiculo().getDiametro() != null) {
                    datosGasesDiesel.setLTOEStandar(this.hojaPrueba.getVehiculo().getDiametro().toString());
                } else {
                    datosGasesDiesel.setLTOEStandar("");
                }

                //Se comentarea el codigo dado que en la operatia getGasesVehiculosDisel se esta realizando la toma de datos de inspeccion previa
//                gasesVehiculosDisel = new PreviaGasesVehiculosDisel();
//                for (Defxprueba defxprueba : pruebas.getDefxpruebaList()) {
//                    switch (defxprueba.getDefectos().getCardefault()) {
//                        case 84027: //Fugas en el tubo, uniones del multiple y silencioador del sistema de escape del vehiculo
//                            gasesVehiculosDisel.setFugasTuboEscape(true);
//                            break;
//                        case 84030: //Salidas adicionales en el sistema de escape diferentes a las del diseño original del vehiculo
//                            gasesVehiculosDisel.setSalidasAdicionales(true);
//                            break;
//                        case 84031: //Ausencia de tapones de aceite o fugas en el mismo
//                            gasesVehiculosDisel.setTapaAceite(true);
//                            break;
//                        case 84032: //Ausencia de tapones de combustible o fugas en el mismo
//                            gasesVehiculosDisel.setTapaCombustible(true);
//                            break;
//                        case 84020: //Instalacion de accesorios o deformaciones en el tubo de escape que no permitan la introduccion del acople
//                            gasesVehiculosDisel.setFugasTuboEscape(true);
//                            break;
//                        case 84021: //Incorrecta operacion del sistema de refrigeracion
//                            gasesVehiculosDisel.setSistemaRefrigeracion(true);
//                            break;
//                        case 84022: //Ausencia o incorrecta instalacion del filtro de aire
//                            gasesVehiculosDisel.setFiltroAire(true);
//                            break;
//                        case 84019: //Revoluciones fuera de rango
//                            gasesVehiculosDisel.setRevolucionesFueraRango(true);
//                            break;
//
////TODO: FALTAN Registros inspeccion previa diesel
//                    }
//                }
                datosGasesDiesel.setOperarioGases(pruebas.getUsuarioFor().getNombre());
                datosGasesDiesel.setNoIdentificacionOpGases(pruebas.getUsuarioFor().getCedula());
            }
        }

    }

    public void datosGasesGasolina() {
        datosGasesGasolina = new DatosGasesGasolina();
        for (Prueba pruebas : pruebasHoja) {
            if (pruebas.getTipoPrueba().getId() == 8) {
                for (Medida medidas : pruebas.getMedidaList()) {
                    switch (medidas.getTipoMedida().getId()) {
                        case 8001:                                  //HC ralenti
                            datosGasesGasolina.setHcRalenti(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8002:                                  //CO ralenti
                            datosGasesGasolina.setCoRalenti(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8003:                                  //CO2 ralenti
                            datosGasesGasolina.setCo2RAlenti(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8004:                                  //O2 ralenti
                            datosGasesGasolina.setO2Ralenti(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8011:                                  //RPM Crucero
                            datosGasesGasolina.setRmpCrucero(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8007:                                  //HC Crucero
                            datosGasesGasolina.setHcCrucero(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8008:                                  //CO Crucero
                            datosGasesGasolina.setCoCrucero(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8009:                                  //CO2 Crucero
                            datosGasesGasolina.setCo2Crucero(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8010:                                  //O2 Crucero
                            datosGasesGasolina.setO2Crucero(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8005:                                  //rpm ralenti
                            datosGasesGasolina.setRpmRalenti(UtilPropiedades.decimalFormat(Math.ceil(medidas.getValor())));
                            break;
                        case 9115:                                  //temperatura de prueba
                            datosGasesGasolina.setTemperaturaPrueba(UtilPropiedades.decimalFormat(Math.ceil(medidas.getValor())));
                            break;
                        case 8031:                                  //temperatura ambiente
                            datosGasesGasolina.setTemperaturaAmbiente(UtilPropiedades.decimalFormat(Math.ceil(medidas.getValor())));
                            break;
                        case 8032:                                  //humedad relativa
                            datosGasesGasolina.setHumedadRelativa(UtilPropiedades.decimalFormat(Math.ceil(medidas.getValor())));
                            break;

                    }//end switch de los medidas
                    datosGasesGasolina.setCatalizador(this.hojaPrueba.getFormaMedTemperatura() == 'C' ? "S" : "N");

                    if (pruebas.getComentario() != null && pruebas.getComentario().equalsIgnoreCase("DILUCION DE MUESTRA")) {
                        datosGasesGasolina.setDilucion(true);
                    }
                    //Esta logica se realiza en la operatoria getPreviaGasesVehiculos, por eso se comenta
//                    setPreviaGasesVehiculos(new PreviaGasesVehiculos());
//                    for (Defxprueba defxprueba : pruebas.getDefxpruebaList()) {
//                        switch (defxprueba.getDefectos().getCardefault()) {
//                            case 84027: //Fugas en el tubo, uniones del multiple y silencioador del sistema de escape del vehiculo
//                                previaGasesVehiculos.setFugasTuboEscape(true);
//                                previaGasesVehiculos.setFugasSilenciador(true);
//                                break;
//                            case 84030: //Salidas adicionales en el sistema de escape diferentes a las del diseño original del vehiculo
//                                previaGasesVehiculos.setSalidasAdicionales(true);
//                                break;
//                            case 84031: //Ausencia de tapones de aceite o fugas en el mismo
//                                previaGasesVehiculos.setTapaAceite(true);
//                                break;
//                            case 84032: //Ausencia de tapones de combustible o fugas en el mismo
//                                previaGasesVehiculos.setTapaCombustible(true);
//                                break;
//                            case 84004: //Instalacion de accesorios o deformaciones en el tubo de escape que no permitan la introduccion del acople
//                                previaGasesVehiculos.setPresenciaHumos(true);
//                                break;
//                            case 84021: //Incorrecta operacion del sistema de refrigeracion
//                                previaGasesVehiculos.setFallaSistemaRefrigeracion(true);
//                                break;
//                            case 84019: //Revoluciones fuera de rango
//                                previaGasesVehiculos.setRevolucionesFueraRango(true);
//                                break;
//                        }
//                    }
                }
                datosGasesGasolina.setOperarioGases(pruebas.getUsuarioFor().getNombre());
                datosGasesGasolina.setNoIdentificacionOpGases(pruebas.getUsuarioFor().getCedula());
            }
        }

    }

    public void datosGasesMoto() {
        datosGasesMotos = new DatosGasesMotos();
        for (Prueba pruebas : pruebasHoja) {
            if (pruebas.getTipoPrueba().getId() == 8 && hojaPrueba.getVehiculo().getTipoGasolina().getId() == 1) {
                for (Medida medidas : pruebas.getMedidaList()) {
                    switch (medidas.getTipoMedida().getId()) {
                        case 8006:          //TEMP Ralenti
                            datosGasesMotos.setTempRalenti(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8005: //RPM ralenti
                        case 8028: //RPM ralenti 2t
                            datosGasesMotos.setRpmRalenti(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8001:                            //HC ralenti
                        case 8018:                            //HC ralenti 2t
                            datosGasesMotos.setHcRalenti(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8002:                            //CO ralenti
                        case 8020:                            //CO ralenti 2t
                            datosGasesMotos.setCoRalenti(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8003:                            //CO2 ralenti
                        case 8019:                            //CO2 ralenti 2t
                            datosGasesMotos.setCo2RAlenti(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8004:                            //O2 ralenti
                        case 8021:                            //O2 ralenti 2t
                            datosGasesMotos.setO2Ralenti(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8031:                            //temperatura ambiente
                            datosGasesMotos.setTemperaturaAmbiente(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                        case 8032:                            //humedad relativa
                            datosGasesMotos.setHumedadRelativa(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                    }//end switch de los medidas
                }
            }
            datosGasesMotos.setOperarioGases(pruebas.getUsuarioFor().getNombre());
            datosGasesMotos.setNoIdentificacionOpGases(pruebas.getUsuarioFor().getCedula());
            setPreviaGasesMotos(new PreviaGasesMotos());

            for (Defxprueba defxprueba : pruebas.getDefxpruebaList()) {
                switch (defxprueba.getDefectos().getCardefault()) {
                    case 84027: //Fugas en el tubo, uniones del multiple y silencioador del sistema de escape del vehiculo
                        getPreviaGasesMotos().setFugasSilenciador(true);
                        getPreviaGasesMotos().setFugasTuboEscape(true);
                        break;
                    case 84030: //Salidas adicionales en el sistema de escape diferentes a las del diseño original del vehiculo                            
                        getPreviaGasesMotos().setSalidasAdicionales(true);
                        break;
                    case 84031: //Ausencia de tapones de aceite o fugas en el mismo
                        getPreviaGasesMotos().setTapaAceite(true);
                        getPreviaGasesMotos().setTapaCombustible(true);
                        break;
                    case 84019: //Revoluciones fuera de rango
                        getPreviaGasesMotos().setRevolucionesFueraRango(true);
                        break;
                    case 84004: //Presencia de Humo Negro o Azul
                        getPreviaGasesMotos().setPresenciaHumos(true);
                        break;

                }//end switch de los medidas
            }

        }

    }

    public void datosFas() {
        System.out.println("----------------------------------------------------");
        System.out.println("-------------------- DatosFas() --------------------");
        System.out.println("----------------------------------------------------");
        datosFas = new DatosFas();
        try {
            datosFas.setNoEjes(hojaPrueba.getVehiculo().getEjes().toString());

            for (Prueba pruebas : pruebasHoja) {
                int tipo_vehiculo = pruebas.getHojaPruebas().getVehiculo().getTipoVehiculo().getId();
                if (pruebas.getTipoPrueba().getId() == 4 || pruebas.getTipoPrueba().getId() == 5 || pruebas.getTipoPrueba().getId() == 6) { //Frenos y desviacion
                    Float derFuerzaAux = 0f;
                    Float derFuerzaPeso = 0f;
                    Float izqFuerzaAux = 0f;
                    Float izqFuerzaPeso = 0f;

                    for (Medida medidas : pruebas.getMedidaList()) {
                        switch (medidas.getTipoMedida().getId()) {
                            //en codigo de freno añadir cuando sea diferente de cuatro(tipo vehiculo moto)
                            case 5024: //Eficacia de frenado
                                datosFas.setEficaciaTotal(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            //--------------------------------------------------  
                            //-------------            Eje 1       -------------
                            //--------------------------------------------------    
                            case 5008://Fuerza de frenado maxima derecha por eje (FFMDEEJE1)
                                datosFas.setFuerzaFrenadoDerEje1(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;
                            case 5012://Fuerza de frenado maxima izquierda por eje (FFMIZEJE1)
                                datosFas.setFuerzaFrenadoIzqEje1(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 5004://Peso izquierdo por eje (PESIZQEJE1)
                                datosFas.setPesoIzqEje1(UtilPropiedades.decimalFormat(medidas.getValor()));
                                //derFuerzaPeso = derFuerzaPeso + medidas.getValor();
                                izqFuerzaPeso = izqFuerzaPeso + medidas.getValor();
                                break;

                            case 5000://Peso derecho por eje (PESDEREJE1)
                                datosFas.setPesoDerEje1(UtilPropiedades.decimalFormat(medidas.getValor()));
                                if (tipo_vehiculo != 4) {
                                    derFuerzaPeso = derFuerzaPeso + medidas.getValor();
                                }
                                break;

                            case 5032://Desequilibrio por eje (DESEJE1)
                                datosFas.setDesequilibrioEje1(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 6016: //Suspension derecha por eje
                                datosFas.setSuspDerEje1(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 6020://Suspension izquierda por eje
                                datosFas.setSuspIzqEje1(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 4000://Desviacion del eje numero 1
                                datosFas.setDesviacionLateralEje1(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            //--------------------------------------------------  
                            //-------------            Eje 2       -------------
                            //--------------------------------------------------
                            case 5009://Fuerza de frenado maxima derecha por eje (FFMDEEJE2)
                                datosFas.setFuerzaFrenadoDerEje2(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 5013://Fuerza de frenado maxima izquierda por eje (FFMIZEJE2)
                                datosFas.setFuerzaFrenadoIzqEje2(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 5005://Peso izquierdo por eje (PESIZQEJE2)
                                datosFas.setPesoIzqEje2(UtilPropiedades.decimalFormat(medidas.getValor()));
                                izqFuerzaPeso = izqFuerzaPeso + medidas.getValor();
                                break;

                            case 5001://Peso derecho por eje (PESDEREJE2)
                                datosFas.setPesoDerEje2(UtilPropiedades.decimalFormat(medidas.getValor()));
                                if (tipo_vehiculo != 4) {
                                    derFuerzaPeso = derFuerzaPeso + medidas.getValor();
                                }
                                break;

                            case 5033://Desequilibrio por eje (DESEJE2)
                                datosFas.setDesequilibrioEje2(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 6017://Suspension derecha por eje
                                datosFas.setSuspDerEje2(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;
                            case 6021://Suspension izquierda  por eje
                                datosFas.setSuspIzqEje2(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 4001://Desviacion del eje numero 2
                                datosFas.setDesviacionLateralEje2(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            //--------------------------------------------------  
                            //-------------            Eje 3       -------------
                            //--------------------------------------------------    
                            case 5010://Fuerza de frenado maxima derecha por eje (FFMDEEJE3)
                                datosFas.setFuerzaFrenadoDerEje3(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 5014://Fuerza de frenado maxima izquierda por eje (FFMIZEJE3)
                                datosFas.setFuerzaFrenadoIzqEje3(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 5002://Peso derecho por eje (PESDEREJE3)
                                datosFas.setPesoDerEje3(UtilPropiedades.decimalFormat(medidas.getValor()));
                                if (tipo_vehiculo != 4) {
                                    derFuerzaPeso = derFuerzaPeso + medidas.getValor();
                                }
                                break;

                            case 5006://Peso izquierdo por eje (PESIZQEJE3)
                                datosFas.setPesoIzqEje3(UtilPropiedades.decimalFormat(medidas.getValor()));
                                izqFuerzaPeso = izqFuerzaPeso + medidas.getValor();
                                break;

                            case 5034://Desequilibrio por eje (DESEJE3)
                                datosFas.setDesequilibrioEje3(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 4002://Desviacion del eje numero 3
                                datosFas.setDesviacionLateralEje3(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            //--------------------------------------------------  
                            //-------------            Eje 4       -------------
                            //--------------------------------------------------        
                            case 5011://Fuerza de frenado maxima derecha por eje (FFMDEEJE4)
                                datosFas.setFuerzaFrenadoDerEje4(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 5015://Fuerza de frenado maxima izquierda por eje (FFMIZEJE4)
                                datosFas.setFuerzaFrenadoIzqEje4(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 5003://Peso derecho por eje (PESDEREJE4)
                                datosFas.setPesoDerEje4(UtilPropiedades.decimalFormat(medidas.getValor()));
                                if (tipo_vehiculo != 4) {
                                    derFuerzaPeso = derFuerzaPeso + medidas.getValor();
                                }
                                break;

                            case 5007://Peso izquierdo por eje (PESIZQEJE4)
                                datosFas.setPesoIzqEje4(UtilPropiedades.decimalFormat(medidas.getValor()));
                                izqFuerzaPeso = izqFuerzaPeso + medidas.getValor();
                                break;

                            case 5035://Desequilibrio por eje (DESEJE4)
                                datosFas.setDesequilibrioEje4(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 4003://Desviacion del eje numero 4
                                datosFas.setDesviacionLateralEje4(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;
                            //--------------------------------------------------  
                            //-------------            Eje 5       -------------
                            //--------------------------------------------------     

                            case 5027://Fuerza de frenado maxima derecha por eje (FFMDEEJE5)
                                datosFas.setFuerzaFrenadoDerEje5(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 5028://Fuerza de frenado maxima izquierda por eje (FFMIZEJE5)
                                datosFas.setFuerzaFrenadoIzqEje5(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 5025://Peso derecho por eje (PESDEREJE5)
                                datosFas.setPesoDerEje5(UtilPropiedades.decimalFormat(medidas.getValor()));
                                if (tipo_vehiculo != 4) {
                                    derFuerzaPeso = derFuerzaPeso + medidas.getValor();
                                }
                                break;

                            case 5026://Peso izquierdo por eje (PESIZQEJE5)
                                datosFas.setPesoIzqEje5(UtilPropiedades.decimalFormat(medidas.getValor()));
                                izqFuerzaAux = izqFuerzaAux + medidas.getValor();//suma a la variable incorrecta
                                break;

                            case 5031://Desequilibrio por eje (DESEJE5)
                                datosFas.setDesequilibrioEje5(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            case 4004://Desviacion del eje numero 5
                                datosFas.setDesviacionLateralEje5(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;

                            //--------------------------------------------------  
                            //-------------     FRENO AUXILIAR     -------------
                            //--------------------------------------------------  
                            case 5036://Eficacia de freno de mano (EFIFREMAN)
                                if (hojaPrueba.getVehiculo().getTipoVehiculo().getId() == 4) {
                                    datosFas.setEficaciaAuxiliar(UtilPropiedades.decimalFormat(0.0F));
                                } else {
                                    datosFas.setEficaciaAuxiliar(UtilPropiedades.decimalFormat(medidas.getValor()));
                                }
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
                            default:
                                System.out.println("----------------------------------------");
                                System.out.println("Error: No se encuentra el Codigo : " + medidas.getTipoMedida().getId());
                                System.out.println("-----------------------------------------");
                                break;
                        }
                    }
                    System.out.print("valores a revisar----------------------");
                    System.out.print("" + derFuerzaAux);
                    System.out.print("" + derFuerzaPeso);
                    System.out.print("" + izqFuerzaAux);
                    System.out.print("" + izqFuerzaPeso);
                    datosFas.setDerFuerzaAuxiliar(UtilPropiedades.decimalFormat(derFuerzaAux));
                    datosFas.setDerFuerzaPeso(UtilPropiedades.decimalFormat(derFuerzaPeso));
                    datosFas.setIzqFuerzaAuxiliar(UtilPropiedades.decimalFormat(izqFuerzaAux));
                    datosFas.setIzqFuerzaPeso(UtilPropiedades.decimalFormat(izqFuerzaPeso));

                    datosFas.setOperariosFas(pruebas.getUsuarioFor().getNombre());
                    datosFas.setNoIdentificacionOpFas(pruebas.getUsuarioFor().getCedula());
                }
            }
        } catch (Exception e) {
            System.out.println(" Error en el  metodo : datosFas()" + e.getMessage() + e.getLocalizedMessage());
        }

    }

    public void datosLlantas() {
        datosLlantas = new DatosLlantas();
        for (Prueba pruebas : pruebasHoja) {
            if (pruebas.getTipoPrueba().getId() == 1) {
                List<Medida> mds = pruebas.getMedidaList();
                if (hojaPrueba.getVehiculo().getTipoVehiculo().getId() == 4) { //Motos
                    datosLlantas.setDerProfundidadEje1(rm(mds, 9046));
                    datosLlantas.setDerProfundidadExternaEje2(rm(mds, 9047));
                    datosLlantas.setIzqProfundidadEje1(rm(mds, 9046));
                    datosLlantas.setIzqProfundidadExternaEje2(rm(mds, 9047));

                } else if (hojaPrueba.getVehiculo().getTipoVehiculo().getId() == 5) {// Motocarro
                    datosLlantas.setDerProfundidadEje1(rm(mds, 9050));
                    datosLlantas.setDerProfundidadExternaEje2(rm(mds, 9051));
                    datosLlantas.setDerProfundidadInternaEje2(rm(mds, 9052));
                    datosLlantas.setIzqProfundidadEje1(rm(mds, 9053));
                    datosLlantas.setIzqProfundidadExternaEje2(rm(mds, 9054));
                    datosLlantas.setIzqProfundidadInternaEje2(rm(mds, 9055));
                } else {
                    datosLlantas.setIzqProfundidadEje1(rm(mds, 9004));
                    datosLlantas.setIzqProfundidadExternaEje2(rm(mds, 9005));
                    datosLlantas.setIzqProfundidadExternaEje3(rm(mds, 9006));
                    datosLlantas.setIzqProfundidadExternaEje4(rm(mds, 9007));
                    datosLlantas.setIzqProfundidadExternaEje5(rm(mds, 9008));

                    datosLlantas.setIzqProfundidadInternaEje2(rm(mds, 9009));
                    datosLlantas.setIzqProfundidadInternaEje3(rm(mds, 9010));
                    datosLlantas.setIzqProfundidadInternaEje4(rm(mds, 9011));
                    datosLlantas.setIzqProfundidadInternaEje5(rm(mds, 9012));

                    datosLlantas.setDerProfundidadEje1(rm(mds, 9013));
                    datosLlantas.setDerProfundidadExternaEje2(rm(mds, 9014));
                    datosLlantas.setDerProfundidadExternaEje3(rm(mds, 9015));
                    datosLlantas.setDerProfundidadExternaEje4(rm(mds, 9016));
                    datosLlantas.setDerProfundidadExternaEje5(rm(mds, 9017));

                    datosLlantas.setDerProfundidadInternaEje2(rm(mds, 9018));
                    datosLlantas.setDerProfundidadInternaEje3(rm(mds, 9019));
                    datosLlantas.setDerProfundidadInternaEje4(rm(mds, 9020));
                    datosLlantas.setDerProfundidadInternaEje5(rm(mds, 9021));

                    datosLlantas.setIzqPresionEje1(rm(mds, 9022));
                    datosLlantas.setIzqPresionexterneEje2(rm(mds, 9023));
                    datosLlantas.setIzqPresionExternaEje3(rm(mds, 9024));
                    datosLlantas.setIzqPresionExternaEje4(rm(mds, 9025));
                    datosLlantas.setIzqPresionExternaEje5(rm(mds, 9026));

                    datosLlantas.setIzqPresionInternaEje2(rm(mds, 9027));
                    datosLlantas.setIzqPresionInternaEje3(rm(mds, 9028));
                    datosLlantas.setIzqPresionInternaEje4(rm(mds, 9029));
                    datosLlantas.setIzqPresionInternaEje5(rm(mds, 9030));

                    datosLlantas.setDerPresionEje1(rm(mds, 9031));
                    datosLlantas.setDerPresionExternaEje2(rm(mds, 9032));
                    datosLlantas.setDerPresionExternaEje3(rm(mds, 9033));
                    datosLlantas.setDerPresionExternaEje4(rm(mds, 9034));
                    datosLlantas.setDerPresionExternaEje5(rm(mds, 9035));

                    datosLlantas.setDerPresionInternaEje2(rm(mds, 9036));
                    datosLlantas.setDerPresionInternaEje3(rm(mds, 9037));
                    datosLlantas.setDerPresionInternaEje4(rm(mds, 9038));
                    datosLlantas.setDerPresionInternaEje5(rm(mds, 9039));

                    datosLlantas.setRepuestoPresion(rm(mds, 9040));
                    System.out.println("Revisar aqui***____mds:" + mds);
                    datosLlantas.setRepuesto2Profundidad(rm(mds, 9041));
                    datosLlantas.setRepuestoProfundidad(rm(mds, 9040));

                    datosLlantas.setRepuestoPresion(rm(mds, 9043));
                    datosLlantas.setRepuesto2Presion(rm(mds, 9044));
                    System.out.println("datos llantas desde indra:" +  datosLlantas.toString());
                    System.out.println("DerPresionEje1Eje1: " + datosLlantas.getDerPresionEje1());
                    System.out.println("IzqProfundidadEje1: " + datosLlantas.getIzqProfundidadEje1());

                }
                datosLlantas.setOperarioLlantas(pruebas.getUsuarioFor().getNombre());
                datosLlantas.setNoIdentificacionOpLlantas(pruebas.getUsuarioFor().getCedula());
            }
        }
    }

    public void datosEquipos() {
        EquiposJpaController1 equiposJpaController1 = new EquiposJpaController1();
        equipos = new JsonEquipos();
        equipos.setEquipos(new ArrayList<DatosEquipos>());
        for(Prueba prueba : this.hojaPrueba.getListPruebas()) {

            System.out.println(prueba);
            System.out.println(prueba.getSerialEquipo());
            System.out.println(prueba.getTipoPrueba());
            if (
            prueba.getSerialEquipo() != null &&
            prueba.getSerialEquipo().contains("~") && 
            prueba.getTipoPrueba().getId() == 8) {
                datosEquipos = new DatosEquipos();
                String clase = prueba.getSerialEquipo().split("~")[0];
                String ltoePef = prueba.getSerialEquipo().split("~")[2].split("-")[0];
                String marca = prueba.getSerialEquipo().split("~")[1].split(";")[1];
                String serialesAnalizador = prueba.getSerialEquipo().split("~")[2].split(";")[0];
                datosEquipos.setNombre(clase.equalsIgnoreCase("otto") ? "Gases" : "Diesel"); 
                datosEquipos.setLtoe(clase.equalsIgnoreCase("otto") ? "" : ltoePef);
                datosEquipos.setMarca(marca);
                datosEquipos.setNoserie(serialesAnalizador.split("-")[1]);
                datosEquipos.setNoseriebench(clase.equalsIgnoreCase("otto") ? (serialesAnalizador.split("-").length <3 ? serialesAnalizador.split("-")[1] : serialesAnalizador.split("-")[2]) : "");
                datosEquipos.setPef(clase.equalsIgnoreCase("otto") ? ltoePef : "");
                datosEquipos.setPrueba(vd(prueba.getTipoPrueba().getId().toString()));
                System.out.println("-----------------------------------------------------------------------datos equipos: " + datosEquipos );

                System.out.println(clase.equalsIgnoreCase("otto") ? "Otto" : "Diesel"); 
                System.out.println(clase.equalsIgnoreCase("otto") ? "" : ltoePef);
                System.out.println(marca);
                System.out.println(serialesAnalizador.split("-")[1]);
                System.out.println(clase.equalsIgnoreCase("otto") ? (serialesAnalizador.split("-").length <3 ? serialesAnalizador.split("-")[1] : serialesAnalizador.split("-")[2]) : "");
                System.out.println(clase.equalsIgnoreCase("otto") ? ltoePef : "");
                System.out.println(vd(prueba.getTipoPrueba().getId().toString()));

                System.out.println();
                continue;
            }


            if (prueba.getTipoPrueba().getId() == 1) { // no incluir prueba visual
                continue;
            }
            datosEquipos = new DatosEquipos();
            Equipo equipo = equiposJpaController1.buscarPorSerial(prueba.getSerialEquipo());

            if (equipo == null) {
                continue;
            }

            if (equipo.getPeriferico() == null) {
                datosEquipos.setEsperiferico("N");
            } else {
                datosEquipos.setEsperiferico(equipo.getPeriferico().equals("Y") ? "S" : "N");
            }
            datosEquipos.setNombre(vd(equipo.getDescripcion())); 
            datosEquipos.setLtoe(vd(equipo.getLtoe()));
            datosEquipos.setMarca(vd(equipo.getMarca()));
            datosEquipos.setNoserie(vd(equipo.getSerial()));
            datosEquipos.setNoseriebench(vd(equipo.getNumSerialBench()));
            datosEquipos.setPef(vd(String.valueOf(equipo.getPef())));
            datosEquipos.setPrueba(vd(prueba.getTipoPrueba().getId().toString()));
            System.out.println("-----------------------------------------------------------------------datos equipos: " + datosEquipos );
            equipos.getEquipos().add(datosEquipos);
        }
    }

    public void datosSoftware() {
        datossoftware = new DatosSoftware();
        datossoftware.setNombreAplicacion(this.cda.getNombreSoftware());
    }

    /**
     * Metodo que extrae el usuario responsable de la prueba a realizar
     */
    public void datosFirma() {
        try {
            log.info("Extrayendo el usuario responsable de la prueba a realizar");
            UsuarioJpaController controller = new UsuarioJpaController();
            datosFirma = new DatosFirma();
            Integer idUserReponsable = this.hojaPrueba.getResponsable().getUsuario();
            log.info("id del usuario responsable :" + idUserReponsable);
            Usuario usuarioResponsable = controller.find(idUserReponsable);
            datosFirma.setFirmaDirectorTecnico(usuarioResponsable.getNombre());
            datosFirma.setNoIdentificacionDirectorTec(usuarioResponsable.getCedula());
            log.info("Nombre  : " + usuarioResponsable.getNombre() + "y  Cedula: " + usuarioResponsable.getCedula() + " usuario responsable");
        } catch (Exception e) {
            log.error("Error en el metodo :datosFirma()" + e.getMessage());
        }
    }

    /**
     * Metodo que carga el numero del furAsociado
     */
    public void datosFurAsociado() {
        System.out.println("-------------------------------------------------------------");
        System.out.println("------------------Cargando FUR ASOCIADO-----------------------");
        System.out.println("-------------------------------------------------------------");
        datosFurAsociados = new DatosFurAsociados();
        try {
            if (this.hojaPrueba.getIntentos() > 1) {
                int nHojasRegistros = Utils.contarRegistrosHojaPruebas(this.hojaPrueba.getCon_hoja_prueba());
                if (nHojasRegistros == 0) {
                    Mensajes.mensajeError("Hoja de pruebas con la columna con_hoja_prueba = '"+this.hojaPrueba.getCon_hoja_prueba()+"' no encontrada");
                    throw new RuntimeException("Hoja de pruebas con la columna con_hoja_prueba = '"+this.hojaPrueba.getCon_hoja_prueba()+"' no encontrada");
                }
                if (nHojasRegistros < 2) {
                    datosFurAsociados.setNumeroFur(String.format("%d1", this.hojaPrueba.getCon_hoja_prueba()));
                    datosFurAsociados.setFechaHoraFur(UtilPropiedades.convertiFechas(this.hojaPrueba.getFechaIngreso(), "yyyy-MM-dd hh:mm:ss"));
                    System.out.println("Valor para el campo FUR ASOCIADO : " + datosFurAsociados.getNumeroFur());
                    System.out.println("Fecha para el campo FUR ASOCIADO : " + hojaPrueba.getFechaIngreso());
                }else{
                    String fechaIngresoPrimeraRevision = null;
                    Date fechaAnterior = Utils.obtenerFechaAnterior(this.hojaPrueba.getId());
                    if (fechaAnterior != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        fechaIngresoPrimeraRevision = dateFormat.format(fechaAnterior);
                        datosFurAsociados.setFechaHoraFur(fechaIngresoPrimeraRevision);
                    }else{
                        fechaIngresoPrimeraRevision = Utils.obtenerFechaPrimeraRevision(this.hojaPrueba.getCon_hoja_prueba(), this.hojaPrueba.getId());
                        datosFurAsociados.setFechaHoraFur(fechaIngresoPrimeraRevision);
                    }
                    
                    datosFurAsociados.setNumeroFur(String.format("%d1", this.hojaPrueba.getCon_hoja_prueba()));
                    System.out.println("2Valor para el campo FUR ASOCIADO : " + datosFurAsociados.getNumeroFur());
                    System.out.println("2Fecha para el campo FUR ASOCIADO : " + fechaIngresoPrimeraRevision);
                }
                
            } else {
                datosFurAsociados.setNumeroFur("");
            }
        } catch (Exception e) {
            System.out.println("Error en el metodo : datosFurAsociado()" + e.getMessage());
        }

    }

    public void datosVisualIndra() {
        datosVisual = new DatosVisual();
        for (Prueba pruebas : pruebasHoja) {
            if (pruebas.getTipoPrueba().getId() == 1) {
                datosVisual.setOperarioVisual(pruebas.getUsuarioFor().getNombre());
                datosVisual.setNoIdentificacionOpVisual(pruebas.getUsuarioFor().getCedula());
            }

            for (Defxprueba defxprueba : pruebas.getDefxpruebaList()) {
                try {
                    if (datosVisual.getCodigoRechazo().isEmpty()) {
                        datosVisual.setCodigoRechazo(defxprueba.getDefectos().getCodigoResolucion());
                    } else {
                        datosVisual.setCodigoRechazo(datosVisual.getCodigoRechazo().concat("_").concat(defxprueba.getDefectos().getCodigoResolucion()));
                    }
                } catch (Exception e) {
                    System.out.println("Error: en armando lista de defectos visuales " + e);
                }
            }
        }
    }

    public void datosVisual() {
        datosVisual = new DatosVisual();
        int count = 0;
        for (Prueba pruebas : pruebasHoja) {
            if (pruebas.getTipoPrueba().getId() == 1) {// Prueba visual
                datosVisual.setOperarioVisual(pruebas.getUsuarioFor().getNombre());
                datosVisual.setNoIdentificacionOpVisual(pruebas.getUsuarioFor().getCedula());
                for (Defxprueba defxprueba : pruebas.getDefxpruebaList()) {
                    if (count == 0) {
                        datosVisual.setCodigoRechazo(String.format("%s", defxprueba.getDefectos().getCodigoResolucion()));
                    } else {
                        datosVisual.setCodigoRechazo(String.format("%s_%s", datosVisual.getCodigoRechazo(), defxprueba.getDefectos().getCodigoResolucion()));
                    }
                    count++;
                }
            }
        }
    }

    public void datosTaximetro(boolean aplicaTest) {
        datosTaximetro = new DatosTaximetro();
        if (aplicaTest) {
            for (Prueba pruebas : pruebasHoja) {
                if (pruebas.getTipoPrueba().getId() == 9) {
                    datosTaximetro.setAplicaTaximetro(aplicaTest);
                    datosTaximetro.setTieneTaximetro(aplicaTest);
                    datosTaximetro.setTaximetroVisible(aplicaTest);
                    for (Medida medidas : pruebas.getMedidaList()) {
                        switch (medidas.getTipoMedida().getId()) {
                            /* case 9000: //Tiene Taximetro
                                datosTaximetro.setTieneTaximetro(false);
                                break; */
                            case 9002: //Error de taximetro en distancia (ERRORXDIST)
                                datosTaximetro.setErrorDistancia(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;
                            case 9003: //Error de taximetro en tirmpo (ERRORXTIEM)
                                datosTaximetro.setErrorTiempo(UtilPropiedades.decimalFormat(medidas.getValor()));
                                break;
                            /* case 9003: //Tiene Taximetro Visible
                                datosTaximetro.setTaximetroVisible(false);
                                break; */
                        }//end switch de los medidas
                    }
                    datosTaximetro.setOperarioTaximetro(pruebas.getUsuarioFor().getNombre());
                    datosTaximetro.setNoIdentificacionOpTaximetro(pruebas.getUsuarioFor().getCedula());
                    datosTaximetro.setReferenciallanta(this.hojaPrueba.getVehiculo().getLlantas().getNombre());
                }// aplicado para la prueba Taximetro
            }// Fin del Ciclo de la Hoja Pruebas
        }// Fin del Ciclo de la Hoja Pruebas
    }

    public void datosLuces() {
        datosLuces = new DatosLuces();
        for (Prueba pruebas : pruebasHoja) {
            if (pruebas.getTipoPrueba().getId() == 2) {
                List<Medida> mds = pruebas.getMedidaList();

                getDatosLuces().setSumatoriaIntensidad(rm(mds, 2011));

                if (hojaPrueba.getVehiculo().getTipoVehiculo().getId() == 4 || hojaPrueba.getVehiculo().getTipoVehiculo().getId() == 5) { //Moto y motocarro
                    getDatosLuces().setDerBajaIntensidadValor1(rm(mds, 2014));///////--------------------
                    getDatosLuces().setDerBajaIntensidadValor2(rm(mds, 2015));///GUARDA LUCES BAJAS PARA LA TRAMA 
                    getDatosLuces().setDerBajaIntensidadValor3(rm(mds, 2000));///////--------------------

                    getDatosLuces().setDerAltaIntensidadValor1(rm(mds, 2056));///////--------------------
                    getDatosLuces().setDerAltaIntensidadValor2(rm(mds, 2057));//Guarda luces altas para enviarlas en la trama
                    getDatosLuces().setDerAltaIntensidadValor2(rm(mds, 2061));///////--------------------

                    getDatosLuces().setDerBajaInclinacionValor1(rm(mds, 2013));///////--------------------
                    getDatosLuces().setDerBajaInclinacionValor2(rm(mds, 2002));///GUARDA ANGULO DE INCLINACION PARA LA TRAMA
                    getDatosLuces().setDerBajaInclinacionValor3(rm(mds, 2001));///////--------------------
                } else { //El resto

                    getDatosLuces().setDerBajaIntensidadValor1(rm(mds, 2024));
                    getDatosLuces().setDerBajaIntensidadValor2(rm(mds, 2025));
                    getDatosLuces().setDerBajaIntensidadValor3(rm(mds, 2026));

                    getDatosLuces().setIzqBajaIntensidadValor1(rm(mds, 2031));
                    getDatosLuces().setIzqBajaIntensidadValor2(rm(mds, 2030));
                    getDatosLuces().setIzqBajaIntensidadValor3(rm(mds, 2029));

                    getDatosLuces().setDerAltaIntensidadValor1(rm(mds, 2036));//2036//2032
                    getDatosLuces().setDerAltaIntensidadValor2(rm(mds, 2037));//2037//2033
                    getDatosLuces().setDerAltaIntensidadValor3(rm(mds, 2038));//2038//2034

                    getDatosLuces().setIzqAltaIntesidadValor1(rm(mds, 2032));//2032//36
                    getDatosLuces().setIzqAltaIntesidadValor2(rm(mds, 2033));//2033//37
                    getDatosLuces().setIzqAltaIntesidadValor3(rm(mds, 2034));//2034//38

                    getDatosLuces().setDerBajaInclinacionValor1(rm(mds, 2040));
                    getDatosLuces().setDerBajaInclinacionValor2(rm(mds, 2041));
                    getDatosLuces().setDerBajaInclinacionValor3(rm(mds, 2042));

                    getDatosLuces().setIzqBajaInclinacionValor1(rm(mds, 2044));
                    getDatosLuces().setIzqBajaInclinacionValor2(rm(mds, 2045));
                    getDatosLuces().setIzqBajaInclinacionValor3(rm(mds, 2046));

                    getDatosLuces().setDerExplorardorasValor1(rm(mds, 2050));
                    getDatosLuces().setDerExplorardorasValor2(rm(mds, 2051));
                    getDatosLuces().setDerExplorardorasValor3(rm(mds, 2052));
                    getDatosLuces().setIzqExplorardorasValor1(rm(mds, 2053));
                    getDatosLuces().setIzqExplorardorasValor2(rm(mds, 2054));
                    getDatosLuces().setIzqExplorardorasValor3(rm(mds, 2055));

                }

                getDatosLuces().setDerAltasSimultaneas("N");
                getDatosLuces().setDerBajaSimultaneas("N");

                getDatosLuces().setIzqAltasSimultaneas("N");
                getDatosLuces().setIzqBajaSimultaneas("N");

                getDatosLuces().setIzqExploradorasSimultaneas("N");
                getDatosLuces().setDerExploradorasSimultaneas("N");

                getDatosLuces().setOperarioLuces(pruebas.getUsuarioFor().getNombre());
                getDatosLuces().setNoIdentificacionOpLuces(pruebas.getUsuarioFor().getCedula());
            }
            //TODO: falta derBajaAlineacion, izqBajaAlineacion, simultaneidadAltasBajas, intensidadExploradoras
        }
    }

    public void datosRuido() {
        datosRuido = new DatosRuido();
        for (Prueba pruebas : pruebasHoja) {
            if (pruebas.getTipoPrueba().getId() == 7) {
                for (Medida medidas : pruebas.getMedidaList()) {
                    switch (medidas.getTipoMedida().getId()) {
                        case 7005:
                        case 7002:
                            getDatosRuido().setValor(UtilPropiedades.decimalFormat(medidas.getValor()));
                            break;
                    }
                }
                getDatosRuido().setOperarioSonometria(pruebas.getUsuarioFor().getNombre());
                getDatosRuido().setNoIdentificacionOpSonometria(pruebas.getUsuarioFor().getCedula());
            }
        }
    }

    public void datosObservaciones() {
        System.out.println("--------------------- entre en modulo datosObservaciones----------");
        datosObservaciones = "";
        String comentario = "";
        String cmLabr = "datos de labrado";
        String infLabr;
        try {
            for (Prueba pruebas : pruebasHoja) {
                comentario = pruebas.getObservaciones();
                System.out.println("pruebas:" + pruebas.toString() + "-----------------------");
                System.out.println("comentario:" + comentario);
                System.out.println("------ voy aca---------------------------------------------------------");
                if (pruebas.getTipoPrueba().getId() == 1 && comentario != "") {
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
                System.out.println("-----------------------------------------------------------------");
                if (pruebas.getTipoPrueba().getId() == 2 && comentario != "") {
                    cmLabr = cmLabr.concat("_Luces: ").concat(comentario);
                    System.out.println("-----------luces:" + cmLabr + "-------------------------------");
                }
                if (pruebas.getTipoPrueba().getId() == 8 && comentario != "") {
                    cmLabr = cmLabr.concat("_Gases: ").concat(comentario);
                    System.out.println("-----------gases:" + cmLabr + "-------------------------------");
                }
                if (pruebas.getTipoPrueba().getId() == 5 && comentario != "") {
                    cmLabr = cmLabr.concat("_Frenos: ").concat(comentario);
                    System.out.println("-----------frenos:" + cmLabr + "-------------------------------");
                }
            }
            if (cmLabr != "") {
                datosObservaciones = datosObservaciones.concat(String.format("%s", cmLabr));
                System.out.println("-----------luces:" + datosObservaciones + "-------------------------------");
            }
        } catch (Exception e) {
            System.out.println("mensaje de error-----" + e.getMessage() + "-------------------------");
        }

    }

    public void datosRunt(Cda cda, Integer verifica, String sufFUR) {
        this.datosRunt = new DatosRunt();
        this.datosRunt.setNoFur(String.valueOf(this.hojaPrueba.getCon_hoja_prueba()).concat(sufFUR));
        this.datosRunt.setIdRunt(cda.getIdRunt());
    }

    /**
     * @return the cda
     */
    public Cda getCda() {
        return cda;
    }

    /**
     * @return the datosTaximetro
     */
    public DatosTaximetro getDatosTaximetro() {
        return datosTaximetro;
    }

    /**
     * @return the datosVisual
     */
    public DatosVisual getDatosVisual() {
        return datosVisual;
    }

    /**
     * @return the datosFas
     */
    public DatosFas getDatosFas() {
        return datosFas;
    }

    /**
     * @return the datosGasesMotos
     */
    public DatosGasesMotos getDatosGasesMotos() {
        return datosGasesMotos;
    }

    /**
     * @return the datosGasesGasolina
     */
    public DatosGasesGasolina getDatosGasesGasolina() {
        return datosGasesGasolina;
    }

    /**
     * @return the gasesVehiculosDisel
     */
    public PreviaGasesVehiculosDisel getGasesVehiculosDisel() {
        if (gasesVehiculosDisel == null) {
            gasesVehiculosDisel = new PreviaGasesVehiculosDisel();
        }
        for (Prueba pruebas : pruebasHoja) {
            if (pruebas.getTipoPrueba().getId() == 8) {
                gasesVehiculosDisel.setFugasTuboEscape(false);
                gasesVehiculosDisel.setFugasSilenciador(false);
                gasesVehiculosDisel.setSalidasAdicionales(false);
                gasesVehiculosDisel.setSistemaRefrigeracion(false);
                gasesVehiculosDisel.setFiltroAire(false);
                gasesVehiculosDisel.setTapaAceite(false);
                gasesVehiculosDisel.setTapaCombustible(false);
                if (pruebas.getComentario() != null) {
                    if (pruebas.getComentario().equalsIgnoreCase("Condiciones Anormales")) {
                        String[] arrValor = pruebas.getObservaciones().split(".-");
                        if (arrValor.length > 1) {
                            String[] arrCondiciones = arrValor[1].split(";");
                            if (arrCondiciones.length > 0) {
                                for (String arrCondicione : arrCondiciones) {
                                    if (arrCondicione.startsWith("Fugas en el tubo")) {
                                        gasesVehiculosDisel.setFugasTuboEscape(true);
                                        gasesVehiculosDisel.setFugasSilenciador(true);
                                    }
                                    if (arrCondicione.startsWith(" Salidas adicionales") || arrCondicione.startsWith("Salidas adicionales")) {
                                        gasesVehiculosDisel.setSalidasAdicionales(true);
                                    }
                                    if (arrCondicione.startsWith(" Ausencia de tapones de combustible") || arrCondicione.startsWith("Ausencia de tapones de combustible")) {
                                        gasesVehiculosDisel.setTapaCombustible(true);
                                    }
                                    if (arrCondicione.startsWith(" Ausencia de tapones de aceites") || arrCondicione.startsWith("Ausencia de tapones de aceite")) {
                                        gasesVehiculosDisel.setTapaAceite(true);
                                    }
                                    if (arrCondicione.startsWith(" Instalacion de accesorios o deformaciones") || arrCondicione.startsWith("Instalacion de accesorios o deformaciones")) {
                                        gasesVehiculosDisel.setFugasTuboEscape(true);
                                        gasesVehiculosDisel.setFugasSilenciador(true);
                                    }
                                    if (arrCondicione.startsWith(" Incorrecta operacion del sistema") || arrCondicione.startsWith("Incorrecta operacion del sistema")) {
                                        gasesVehiculosDisel.setSistemaRefrigeracion(true);
                                    }
                                    if (arrCondicione.startsWith(" Ausencia o incorrecta instalacion del") || arrCondicione.startsWith("Ausencia o incorrecta instalacion del")) {
                                        gasesVehiculosDisel.setFiltroAire(true);
                                    }
                                    if (arrCondicione.startsWith(" Activacion de dispositivos instalados en el motor") || arrCondicione.startsWith("Activacion de dispositivos instalados en el motor")) {
                                        gasesVehiculosDisel.setSistemaMuestreo(true);
                                    }
                                } //fin del ciclo for find naturaleza condiciones
                            }//fin find  condiciones                            
                        }//voy  find  condiciones
                    }// fin de la condicional de gases Inspeccion sensorial                    
                }// if comentario is dif null
                if (pruebas.getComentario() != null) {
                    if (pruebas.getComentario().equalsIgnoreCase("Revoluciones Fuera Rango")) {
                        gasesVehiculosDisel.setRevolucionesFueraRango(true);
                    } else {
                        gasesVehiculosDisel.setRevolucionesFueraRango(false);
                    }
                }
            }// if prueba es de naturaleza gases
        }// for find de prueba gases
        return gasesVehiculosDisel;
    }

    /**
     * @return the datosGasesDiesel
     */
    public DatosGasesDiesel getDatosGasesDiesel() {
        return datosGasesDiesel;
    }

    /**
     * @return the datosFotos
     */
    public DatosFotos getDatosFotos() {
        return datosFotos;
    }

    /**
     * @return the vehiculo
     */
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    /**
     * @return the propietario
     */
    public Propietario getPropietario() {
        return propietario;
    }

    /**
     * @return the datosObservaciones
     */
    public String getDatosObservaciones() {
        return datosObservaciones;
    }

    /**
     * @param datosObservaciones the datosObservaciones to set
     */
    public void setDatosObservaciones(String datosObservaciones) {
        this.datosObservaciones = datosObservaciones;
    }

    /**
     * @return the datosLuces
     */
    public DatosLuces getDatosLuces() {
        return datosLuces;
    }

    /**
     * @return the datosRuido
     */
    public DatosRuido getDatosRuido() {
        return datosRuido;
    }

    /**
     * @return the previaGasesMotos
     */
    public PreviaGasesMotos getPreviaGasesMotos() {
        if (previaGasesMotos == null) {
            previaGasesMotos = new PreviaGasesMotos();
        }
        for (Prueba pruebas : pruebasHoja) {
            if (pruebas.getTipoPrueba().getId() == 8) {
                if (pruebas.getComentario() != null) {
                    if (pruebas.getComentario().equalsIgnoreCase("Condiciones Anormales")) {
                        String[] arrValor = pruebas.getObservaciones().split(".-");
                        if (arrValor.length > 1) {
                            String[] arrCondiciones = arrValor[1].split(";");
                            if (arrCondiciones.length > 0) {
                                for (String arrCondicione : arrCondiciones) {
                                    if (arrCondicione.startsWith("Existencia de fugas")) {
                                        previaGasesMotos.setFugasTuboEscape(true);
                                        previaGasesMotos.setFugasSilenciador(true);
                                    }
                                    if (arrCondicione.startsWith(" Salidas adicionales") || arrCondicione.startsWith("Salidas adicionales")) {
                                        previaGasesMotos.setSalidasAdicionales(true);
                                    }
                                    if (arrCondicione.startsWith(" Ausencia de tapones") || arrCondicione.startsWith("Ausencia de tapones")) {
                                        previaGasesMotos.setTapaCombustible(true);
                                        previaGasesMotos.setTapaAceite(true);
                                    }
                                    //TODO: Ajustar el defecto de Revoluciones fuera de rango
                                    if (arrCondicione.startsWith("  La Temperatura debe") || arrCondicione.startsWith("La Temperatura debe")) {
                                        previaGasesMotos.setRevolucionesFueraRango(true);
                                    }
                                }
                            }// fin del ciclo de condiciones anormales
                        }// fin del ciclo de condiciones anormales

                        if (pruebas.getObservaciones().startsWith("Presencia Humo")) {
                            previaGasesMotos.setPresenciaHumos(true);
                        } else {
                            previaGasesMotos.setPresenciaHumos(false);
                        }
                        if (pruebas.getObservaciones().startsWith("REVOLUCIONES FUERA RANGO")) {
                            previaGasesMotos.setRevolucionesFueraRango(true);
                        } else {
                            previaGasesMotos.setRevolucionesFueraRango(false);
                        }
                    }
                }// fin de la condicional de gases Inspeccion sensorial
            }
        }// fin delciclofor
        return previaGasesMotos;
    }

    /**
     * @return the datosRunt
     */
    public DatosRunt getDatosRunt() {
        return datosRunt;
    }

    /**
     * @param previaGasesMotos the previaGasesMotos to set
     */
    public void setPreviaGasesMotos(PreviaGasesMotos previaGasesMotos) {
        this.previaGasesMotos = previaGasesMotos;
    }

    /**
     * @return the previaGasesVehiculos
     */
    public PreviaGasesVehiculos getPreviaGasesVehiculos() {
        if (previaGasesVehiculos == null) {
            previaGasesVehiculos = new PreviaGasesVehiculos();
        }
        for (Prueba pruebas : pruebasHoja) {
            if (pruebas.getTipoPrueba().getId() == 8) {
                if (pruebas.getComentario() != null) {
                    if (pruebas.getComentario().equalsIgnoreCase("Condiciones Anormales")) {
                        String[] arrValor = pruebas.getObservaciones().split("obs:");
                        if (arrValor.length > 1) {
                            String[] arrCondiciones = arrValor[0].split(";");
                            if (arrCondiciones.length > 0) {
                                for (String arrCondicione : arrCondiciones) {
                                    if (arrCondicione.startsWith("Existencia de fugas en el tubo")) {
                                        previaGasesVehiculos.setFugasTuboEscape(true);
                                        previaGasesVehiculos.setFugasSilenciador(true);
                                    }
                                    if (arrCondicione.startsWith(" Salidas adicionales en el sistema") || arrCondicione.startsWith("Salidas adicionales en el sistema")) {
                                        previaGasesVehiculos.setSalidasAdicionales(true);
                                    }
                                    if (arrCondicione.startsWith(" Ausencia de tapones de aceite") || arrCondicione.startsWith("Ausencia de tapones de aceite")) {
                                        previaGasesVehiculos.setTapaAceite(true);
                                    }
                                    if (arrCondicione.startsWith(" Ausencia de tapas o tapones de combustible") || arrCondicione.startsWith("Ausencia de tapas o tapones de combustible")) {
                                        previaGasesVehiculos.setTapaCombustible(true);
                                    }
                                    if (arrCondicione.startsWith(" Sistema de admisión de aire") || arrCondicione.startsWith("Sistema de admisión de aire")) {
                                        previaGasesVehiculos.setFallaSistemaRefrigeracion(true);
                                    }
                                    if (arrCondicione.startsWith(" Desconexión de sistemas de recirculación de gases") || arrCondicione.startsWith("Desconexión de sistemas de recirculación de gases")) {
                                        previaGasesVehiculos.setFallaSistemaRefrigeracion(true);
                                    }
                                    if (arrCondicione.startsWith(" Instalación de accesorios o deformaciones") || arrCondicione.startsWith("Instalación de accesorios o deformaciones")) {
                                        previaGasesVehiculos.setFugasTuboEscape(true);
                                        previaGasesVehiculos.setFugasSilenciador(true);
                                    }
                                    if (arrCondicione.startsWith(" Incorrecta operación del sistema") || arrCondicione.startsWith("Incorrecta operación del sistema")) {
                                        previaGasesVehiculos.setFallaSistemaRefrigeracion(true);
                                    }
                                } // fin del ciclo for 
                            }// arr of condiciones
                        } else {
                            if (pruebas.getObservaciones().startsWith("Presencia Humo")) {
                                previaGasesVehiculos.setPresenciaHumos(true);
                            }
                            if (pruebas.getObservaciones().startsWith("Revoluciones Fuera Rango")) {
                                previaGasesVehiculos.setRevolucionesFueraRango(true);
                            }
                        }// condicional de exitencia de CODICIONES ANORMALES

                    }//verificar si existen codiciones anormales
                }//si hay algun comentario verifica el null            }
            }// fin de la condicional de gases Inspeccion sensorial    
        }// finl ciclo for find pruebas de gases
        return previaGasesVehiculos;
    }

    /**
     * @param previaGasesVehiculos the previaGasesVehiculos to set
     */
    public void setPreviaGasesVehiculos(PreviaGasesVehiculos previaGasesVehiculos) {
        this.previaGasesVehiculos = previaGasesVehiculos;
    }

    private String cargarTipoIDentificacion(String tipoidentificacion) {
        switch (tipoidentificacion) {
            case "T":
                return "T";
            case "CC":
                return "C";
            case "NIT":
                return "N";
            case "CE":
                return "E";
            case "P":
                return "P";
            default:
                return "1";
        }
    }

    /*public void datosVisual() {
     datosVisual = new DatosVisual();
     int countTipoA = 0;
     int countTipoB = 0;
     int countAnexo = 0;
     for (Pruebas pruebas : pruebasHoja) {
     datosVisual.setOperarioVisual(pruebas.getUsuarioFor().getNombreusuario());
     datosVisual.setNoIdentificacionOpVisual(pruebas.getUsuarioFor().getCedula());

     for (Defxprueba defxprueba : pruebas.getDefxpruebaList()) {
     try {
     if (defxprueba.getDefectos().getDefgroupssub().getTesttype().getTesttype() == 1) { //Prueba visual
     if (defxprueba.getDefectos().getTipodefecto().equals("A")) {
     if (this.hojaPrueba.getVehiculofor().getClass1().getClass1().equals(5)) { //Servicio enseñanza

     if (countAnexo == 0) {
     datosVisual.setVisualTexTAnexo(String.format("Anexo A (TIPO A) %s", defxprueba.getDefectos().getNombreproblema()));
     } else {
     datosVisual.setVisualTexTAnexo(String.format("%s_Anexo A (TIPO A) %s", datosVisual.getVisualTexTAnexo(),
     defxprueba.getDefectos().getNombreproblema()));
     }
     datosVisual.setSumAnexo(String.valueOf(++countAnexo));
     } else {

     if (countTipoA == 0) {
     datosVisual.setVisualTexTA(String.format("6.1.1(TIPO A) %s", defxprueba.getDefectos().getNombreproblema()));
     } else {
     datosVisual.setVisualTexTA(String.format("%s_6.1.1(TIPO A) %s", datosVisual.getVisualTexTA(),
     defxprueba.getDefectos().getNombreproblema()));
     }
     datosVisual.setSumaA(String.valueOf(++countTipoA));
     }
     } else if (defxprueba.getDefectos().getTipodefecto().equals("B")) {
     if (countTipoB == 0) {
     datosVisual.setVisualTexTB(String.format("6.1.1(TIPO B) %s", defxprueba.getDefectos().getNombreproblema()));
     } else {
     datosVisual.setVisualTexTB(String.format("%s_6.1.1(TIPO B) %s", datosVisual.getVisualTexTB(),
     defxprueba.getDefectos().getNombreproblema()));
     }
     datosVisual.setSumaB(String.valueOf(++countTipoB));
     }
     //datosVisual.setOperarioVisual(pruebas.getUsuarioFor().getNombreusuario());
     //datosVisual.setNoIdentificacionOpVisual(pruebas.getUsuarioFor().getCedula());
     datosVisual.setCodigoRechazo(defxprueba.getDefectos().getCodigoSuperintendencia());
     }
     } catch (Exception e) {
     System.out.println("Error: " + e);
     }

     }
     }
     }*/
    /**
     * @return the datosLlantas
     */
    public DatosLlantas getDatosLlantas() {
        return datosLlantas;
    }

    /**
     * @return the equipos
     */
    public JsonEquipos getEquipos() {
        return equipos;
    }

    public DatosFirma getDatosFirma() {
        return datosFirma;
    }

    private String vd(String dato) {
        if (dato == null) {
            return "";
        }
        return dato;
    }

    /**
     * @return the datossoftware
     */
    public DatosSoftware getDatossoftware() {
        return datossoftware;
    }

    /**
     * @return the datosFurAsociados
     */
    public DatosFurAsociados getDatosFurAsociados() {
        return datosFurAsociados;
    }

    private String rm(List<Medida> medidaList, Integer medida) {
        try {
            for (Medida medidas : medidaList) {
                if (Objects.equals(medidas.getTipoMedida().getId(), medida)) {
                    if (medidas.getValor() != null) {
                        return UtilPropiedades.decimalFormat(medidas.getValor());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error en el metodo : rm()" + e.getMessage());
        }
        return "";
    }

}

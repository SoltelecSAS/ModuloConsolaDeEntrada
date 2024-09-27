/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.reporte;

import com.soltelec.consolaentrada.configuration.Conexion;
import com.soltelec.consolaentrada.indra.dto.DatosFur;
import com.soltelec.consolaentrada.models.controllers.CdaJpaController;
import com.soltelec.consolaentrada.models.controllers.CertificadoJpaController;
import com.soltelec.consolaentrada.models.controllers.EventosDao;
import com.soltelec.consolaentrada.models.controllers.EventosSicovJpaController;
import com.soltelec.consolaentrada.models.controllers.HojaPruebasJpaController;
import com.soltelec.consolaentrada.models.controllers.PruebaJpaController;
import com.soltelec.consolaentrada.models.entities.AuditoriaSicov;
import com.soltelec.consolaentrada.models.entities.Cda;
import com.soltelec.consolaentrada.models.entities.Certificado;
import com.soltelec.consolaentrada.models.entities.EventosSicov;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.Prueba;
import com.soltelec.consolaentrada.models.entities.ResponseDTO;
import com.soltelec.consolaentrada.models.entities.RespuestaDTO;
import com.soltelec.consolaentrada.models.entities.UsuarioLogueado;
import com.soltelec.consolaentrada.sicov.ci2.ClienteCi2;
import com.soltelec.consolaentrada.sicov.ci2.ClienteCi2Servicio;
import com.soltelec.consolaentrada.sicov.indra.ClienteIndra;
import com.soltelec.consolaentrada.sicov.indra.ClienteIndraServicio;
import com.soltelec.consolaentrada.sicov.indra.EnviarRuntSicov;
import com.soltelec.consolaentrada.utilities.Mensajes;
import com.soltelec.consolaentrada.utilities.UtilConexion;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que implementa la logica de impresion del FUR y del certificado
 * teniendo en cuenta los consecutivos del RUNT.
 *
 * @author GerenciaDesarrollofvftggggg
 */
public class ImpresionReporte {

    
    EventosSicovJpaController controllerEventos = new EventosSicovJpaController();
    EventosDao ev;
    private Long numeroHojaPrueba;
    private final LlamarReporte llamarReporte;
    
    
    
    
    
    
    private boolean imprimirPdf;     
    private HojaPruebas ctxHojaPrueba;
    private Cda ctxCDA;
    private Certificado ctxCertificado = null;
    private List lstCertificado = null;
    private Boolean envio = false;
    private HojaPruebasJpaController controller = new HojaPruebasJpaController();
    private Long nroCert = 0L;
    private CertificadoJpaController controlerCertificado = null;

    public ImpresionReporte() {
        this.numeroHojaPrueba = -1L;
        llamarReporte = new LlamarReporte();
    }

    /**
     *
     * @param ctxHojaPrueba
     */
    public void preguntarConsecutivos(HojaPruebas ctxHojaPrueba) {
        this.ctxHojaPrueba = ctxHojaPrueba;
        llamarReporte.setImprimirPdf(isImprimirPdf());//con esto se indica si se imprime en pdf o no
//        Connection cn = null;
        try {
            if (numeroHojaPrueba > 0) {
                CdaJpaController cdaControler = cdaControler = new CdaJpaController();
                this.ctxHojaPrueba = controller.find(numeroHojaPrueba.intValue());
                ctxCDA = cdaControler.find(1);

                try {
                    if (!this.ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("NO_APLICA")) {
                        if (this.ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("Env1FUR")) {
                            validacionesPrimerFur();
                        }
//validar si se envio el fur 
                        if (ctxCDA.getProveedorSicov().equalsIgnoreCase("CI2")) {
                            validandoEnvioCI2();
                        }

                        if (ctxCDA.getProveedorSicov().equalsIgnoreCase("INDRA")) {
                            validandoEnvioIndra();
                        }
                        

                    } else {
                        validandoTiPoPrueba();
                    }

                    cargarReporte();

                } catch (Exception ex) {

                }
            } else {
                Mensajes.mensajeAdvertencia("Disculpe, No ha Seleccionado una hoja de prueba");
            }//end else
        }//end try
        catch (ArrayIndexOutOfBoundsException aioe) {
            Mensajes.mensajeAdvertencia("Disculpe, No ha Seleccionado una hoja de prueba");
        }
    }

    /**
     * @autor ELKIN B
     *
     * Metodo que envia el primer FUR
     *
     */
    private void validacionesPrimerFur() {

        System.out.println("----------------------------------------------------");
        System.out.println("----------------ENVIANDO SEGUNDO FUR ----------------");
        System.out.println("----------------------------------------------------");
        try {
            String strConsecutivo = null;
            int seleccion = JOptionPane.showOptionDialog(null, "¿Desea Enviar el Segundo FUR de esta Revision TecnoMecanica ?", "Envio Segundo FUR", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (seleccion == JOptionPane.YES_OPTION) {
                long i;
                envio = true;
                Boolean nunValido = false;
                lstCertificado = new ArrayList();
                PruebaJpaController pruebasJPA = new PruebaJpaController();
                List<Prueba> pruebas = pruebasJPA.findUltimasPruebasByHoja(ctxHojaPrueba.getId());
                String even = "";
                int posTrama = 0;
                boolean encontrado = false;
                List<AuditoriaSicov> lstTramas = controller.recogerTramasExist(this.ctxHojaPrueba);
                for (Prueba p : pruebas) {
                    for (AuditoriaSicov auScv : lstTramas) {
                        posTrama = auScv.getTRAMA().indexOf("idRegistro");
                        even = auScv.getTRAMA().substring(posTrama + 13, auScv.getTRAMA().length() - 2);
                        if (p.getId() == Integer.parseInt(even) && p.getTipoPrueba().getId() != 3) {
                            encontrado = true;
                            break;
                        }
                        if (p.getTipoPrueba().getId() == 3) {
                            encontrado = true;
                            break;
                        }
                    }

                    if (encontrado == false) {
                        if (p.getFechaAborto() != null) {
                            int indicador = p.getFechaAborto().indexOf(";");
                            if (indicador > 0) {

                            } else {
                                p = pruebasJPA.obtSeqSicov(p);
                            }
                        }

                        if (p.getTipoPrueba().getId() == 1) {
                            pruebasJPA.TramaSicovVisual(p, ctxCDA.getIdRunt(), ctxHojaPrueba.getVehiculo().getPlaca());
                        }

                        if (p.getTipoPrueba().getId() == 2) {
                            pruebasJPA.tramaSicovLuces(p, ctxCDA.getIdRunt(), ctxHojaPrueba.getVehiculo().getPlaca());
                        }

                        if (p.getTipoPrueba().getId() == 4) {
                            pruebasJPA.tramaSicovRuidoDesviacion(p, ctxCDA.getIdRunt(), ctxHojaPrueba.getVehiculo().getPlaca());
                        }

                        if (p.getTipoPrueba().getId() == 5) {
                            pruebasJPA.tramaSicovFrenos(p, ctxCDA.getIdRunt(), ctxHojaPrueba.getVehiculo().getPlaca());
                        }

                        if (p.getTipoPrueba().getId() == 6) {
                            pruebasJPA.TramaSicovSuspension(p, ctxCDA.getIdRunt(), ctxHojaPrueba.getVehiculo().getPlaca());
                        }

                        if (p.getTipoPrueba().getId() == 7) {
                            pruebasJPA.tramaSicovRuido(p, ctxCDA.getIdRunt(), ctxHojaPrueba.getVehiculo().getPlaca());
                        }

                        if (p.getTipoPrueba().getId() == 8) {
                            Integer idRun = ctxCDA.getIdRunt();
                            String placa = ctxHojaPrueba.getVehiculo().getPlaca();
                            char temperatura = ctxHojaPrueba.getFormaMedTemperatura();
                            String diametro = null;
                            if (ctxHojaPrueba.getVehiculo().getDiametro() != null) {
                                diametro = String.valueOf(ctxHojaPrueba.getVehiculo().getDiametro());
                            }
                            int tipoGasolina = ctxHojaPrueba.getVehiculo().getTipoGasolina().getId();

                            pruebasJPA.tramaSicovGases(p, idRun, placa, temperatura, diametro, tipoGasolina);
                        }

                        if (p.getTipoPrueba().getId() == 9) {
                            pruebasJPA.TramaSicovTaxcimetro(p, ctxCDA.getIdRunt(), ctxHojaPrueba.getVehiculo().getPlaca());
                        }

//                        pruebasJPA.restauracionTramaSicovLlantas(p, ctxCDA.getIdRunt(),ctxHojaPrueba.getVehiculo().getPlaca());
                    }
                    encontrado = false;
                }

                encontrado = false;

                while (nunValido == false) {

                    strConsecutivo = JOptionPane.showInputDialog("Ingrese el numero de certificado asignado por el RUNT");

                    if (strConsecutivo.length() > 2) {
                        try {
                            this.ctxHojaPrueba.setConsecutivoRunt(strConsecutivo);
                            int opcion = JOptionPane.showOptionDialog(null, "Este es el certificado RUNT Asignado, " + strConsecutivo + " ¿Es Correcto? ", "Confirmacion Consecutivo RUNT", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                            if (opcion == JOptionPane.OK_OPTION) {
                                i = Long.parseLong(strConsecutivo);
                                nunValido = true;
                                System.out.println(" volvi nunvalido: " + nunValido);
                            }
                        } catch (NumberFormatException ne) {
                            JOptionPane.showMessageDialog(null, "Disculpe el numero de certificado Ingresado no es Numerico");
                            System.out.println("ingreso caracteres no validos en el codigo del runt");
                            nunValido = false;

                        }
                    } else {
                        JOptionPane.showOptionDialog(null, "Disculpe el numero de certificado RUNT no cumple con las especificaciones minima ,\n Por favor Asegurese que este bien escrito y vuelva a intentarlo",
                                "Envio Segundo FUR", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    }
                } // fin de ciclo de validacion

                lstTramas = controller.recogerTramasExist(this.ctxHojaPrueba);

                for (Prueba p : pruebas) {
                    for (AuditoriaSicov auScv : lstTramas) {
                        posTrama = auScv.getTRAMA().indexOf("idRegistro");
                        even = auScv.getTRAMA().substring(posTrama + 13, auScv.getTRAMA().length() - 2);

                        System.out.println("evento: "+even);
                        System.out.println("id prueba: "+even);

                        if (p.getTipoPrueba().getId() == 2) {
                            System.out.println("evento prueba luces: "+even);
                        }

                        if (p.getTipoPrueba().getId() == 3) {
                            encontrado = true;
                            break;
                        }

                        if (p.getId() == Integer.parseInt(even)) {
                            encontrado = true;
                            break;
                        }
                    }
                    
                    
                    /* if (encontrado == false) {

                        JOptionPane.showMessageDialog(null, 
                        "Disculpe; No Puedo SINCRONIZAR esta Revision TecnoMecanica al SICOV debido a que me Falta la prueba de " 
                        + p.getTipoPrueba().getNombre() + 
                        " en la tabla AUDITORIA SICOV. \n Por favor contacte al departamento de Soporte Tecnico ");
                        return;
                    } */
                    encontrado = false;
                }

                if (this.ctxHojaPrueba.getEstado().equalsIgnoreCase("APROBADA")) {
                    nunValido = false;
                    ctxCertificado = new Certificado();
                    controlerCertificado = new CertificadoJpaController();
                    String cert = controlerCertificado.maxCertificado();
                    System.out.println("cargo el " + cert);
                    cert = String.valueOf((Long.parseLong(cert) + 1));
                    System.out.println("llegue hasta aqui");
                    while (!nunValido) {
                        System.out.println("entro al while");
                        //strConsecutivo = JOptionPane.showInputDialog("Ingrese el Numero de certificado asignado por el RUNT");
                        System.out.println(" valor de nunvalido: " + nunValido + ", valor enviado: " + strConsecutivo);

                        //String strConsecutivo = null;                        
                        try {// Lineas comentadas para eliminar el consecutivo del 2do Fur.
                            ///strConsecutivo = JOptionPane.showInputDialog("Ingrese el numero de Consecutivo Asignado en el  Preimpreso", cert.toString());
                            strConsecutivo = this.ctxHojaPrueba.getConsecutivoRunt();
                            //int opcion = JOptionPane.showOptionDialog(null, "Este es el  PREIMPRESO Ingresado, " + strConsecutivo + " ¿Es correcto? ", "Confirmacion Consecutivo",///
                            //        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);///
                            //if (opcion == JOptionPane.OK_OPTION) {///
                            //ctxCertificado.setConsecutivo(Long.parseLong(strConsecutivo));
                            ctxCertificado.setConsecutivo(Long.parseLong(strConsecutivo));
                            i = Long.parseLong(strConsecutivo);
                            nunValido = true;
                            //}///
                        } catch (NumberFormatException ne) {
                            JOptionPane.showMessageDialog(null, "Disculpe el Consecutivo Preimpreso no es Numerico, Vuelva a Ingresarlo Por favor....");
                            System.out.println("se ingresaron letras en vez de valores numericos");
                            nunValido = false;
                        }

                    }// fin de validacion de ciclo ingresao de certificado                            
                    ctxCertificado.setConsecutivoRunt(this.ctxHojaPrueba.getConsecutivoRunt());
                    ctxCertificado.setHojaPruebas(this.ctxHojaPrueba);
                    ctxCertificado.setFechaImpresion(new Date());
                    ctxCertificado.setFechaExpedicion(new Date());
                    ctxCertificado.setTipo("A");
                    ctxCertificado.setAnulado("N");
                    ctxCertificado.setComentario(" ");
                    ctxCertificado.setImpreso("Y");
                    lstCertificado.add(ctxCertificado);
                    this.ctxHojaPrueba.setCertificados(lstCertificado);
                    nroCert = ctxCertificado.getConsecutivo();
                }
/////////////////////////////////AQUI DEBE IR CODIGO PARA GUARDAR QUIEN ENVIO EL FUR    
            }
        } catch (Exception e) {
            System.err.println("Error en el metodo : enviarPrimerFur() " + e.getMessage());
        }
    }

    private void validandoEnvioEvento() throws Exception {
        System.out.println("ENTRO A validandoEnvioEvento");
        this.ctxHojaPrueba.setEstadoSICOV("SINCRONIZADO");
        if (this.ctxHojaPrueba.getEstado().equalsIgnoreCase("APROBADA"))
        {
            controlerCertificado.nvoCertificado(ctxCertificado, this.ctxHojaPrueba);
        }
        controller.update(this.ctxHojaPrueba);
        JOptionPane.showMessageDialog(null, "Se ha Enviado el 2do FUR  perteneciente a la Placa  " + ctxHojaPrueba.getVehiculo().getPlaca() + " con exito ..!");
        System.out.println("entro a validandoEnvioEvento ");
        ev = new EventosDao();
      
        System.out.println(ev.InsertarEvento(UsuarioLogueado.getNick(), this.ctxHojaPrueba.getVehiculo().getPlaca(), this.ctxHojaPrueba));

    }

    /**
     * @autor ELKIN B
     *
     * Metodo que validando envio CI2
     *
     */
    private void validandoEnvioCI2() {

        System.out.println("----------------------------------------------------");
        System.out.println("----------------VALIDANDO ENVIO A CI2 ----------------");
        System.out.println("----------------------------------------------------");

        try {
            if (envio == true) {
                if (this.ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("Env1FUR")) {
                    JLabel label = new JLabel("He presentado Problemas al Momento del Envio FUR, comuniquese con el Equipo de Soporte de SOLTELEC  ..!");
                    label.setLocation(10, 10);
                    label.setSize(100, 75);
                    JFrame ventanaPrincipal = new JFrame("Ventana principal");
                    JDialog app = new JDialog(ventanaPrincipal, "Enviando FUR al Servidor SICOV");
                    app.setModal(false);
                    app.setLocation(200, 330);
                    app.setSize(600, 120);
                    app.add(label);
                    app.setVisible(true);
                    ClienteCi2Servicio clienteSincoFur = new ClienteCi2Servicio(this.ctxHojaPrueba, ctxCDA);
                    clienteSincoFur.cargarInf2EnvFUR(nroCert);///----------------------------------------------------
                    ClienteCi2 clienteCi2 = new ClienteCi2(ctxCDA.getUrlServicioSicov());
                    RespuestaDTO respServidor = clienteCi2.enviarFur(clienteSincoFur.getFormulario());
                    app.dispose();
                    if (respServidor == null) {
                        JOptionPane.showMessageDialog(null, "Disculpe, no Puede Enviar el 2do. FUR debido a que no Tengo Comunicacion en estos Momentos con el Servidor SICOV ..! \n Compruebe que el servicio este Levantado sino es asi Comuniquese Por favor con la mesa de Ayuda de CI2 ");
                    }
                    if (respServidor.getCodigoRespuesta().equals("0000")) { //ok

                        this.ctxHojaPrueba.setEstadoSICOV("SINCRONIZADO");
                        if (this.ctxHojaPrueba.getEstado().equalsIgnoreCase("APROBADA")) {
                            controlerCertificado.nvoCertificado(ctxCertificado, this.ctxHojaPrueba);
                        }
                        controller.update(this.ctxHojaPrueba);
                        JOptionPane.showMessageDialog(null, "Se ha Enviado el 2do FUR  perteneciente a la Placa  " + ctxHojaPrueba.getVehiculo().getPlaca() + " con exito ..!");
//////////////////////////////////////////////////////////////////////////////////INSERTO INFORMACION EN LA TABLA EVENTO SICOV/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        ev = new EventosDao();
                        System.out.println(ev.InsertarEvento(UsuarioLogueado.getNick(), this.ctxHojaPrueba.getVehiculo().getPlaca(), this.ctxHojaPrueba));
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    } else {
                        JOptionPane.showMessageDialog(null, "No pude enviar el 2do FUR perteneciente a  la Placa " + ctxHojaPrueba.getVehiculo().getPlaca() + " debido a  " + respServidor.getMensajeRespuesta() + "..!");
                        System.out.println("Fallo por " + respServidor.getMensajeRespuesta());
                        System.out.println("este es 2sd fur");
                    }

                }// fin de logica de envio de doble FUR APLICADO CI2
            }// Fin de Logica de Envio CI2  
        } catch (Exception e) {
            System.err.println("Error en el metodo : validandoCI2() " + e.getMessage());
        }
    }

    /**
     * @autor ELKIN B
     *
     * Metodo que valida el envio a indra
     */
    private void validandoEnvioIndra() {
        System.out.println("----------------------------------------------------");
        System.out.println("----------------VALIDANDO ENVIO A INDRA ------------");
        System.out.println("------------------------- ---------------------------");
        try {
            if (envio == true) {
                if (this.ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("Env1FUR")) {
                    JLabel label = new JLabel("He presentado Problemas al Momento del Envio FUR, comuniquese con el Equipo de Soporte de SOLTELEC  ..!");
                    label.setLocation(10, 10);
                    label.setSize(100, 75);
                    JFrame ventanaPrincipal = new JFrame("Ventana principal");
                    JDialog app = new JDialog(ventanaPrincipal, "Enviando FUR al Servidor SICOV");
                    app.setModal(false);
                    app.setLocation(200, 330);
                    app.setSize(600, 120);
                    app.add(label);
                    app.setVisible(true);

                    EnviarRuntSicov enviarRuntSicov = new EnviarRuntSicov();
                    enviarRuntSicov.setPlaca(this.ctxHojaPrueba.getVehiculo().getPlaca());
                    enviarRuntSicov.setConsecutivoRUNT(this.ctxHojaPrueba.getConsecutivoRunt());
                    enviarRuntSicov.setDireccionIpEquipo(this.ctxCDA.getIp());
                    enviarRuntSicov.setExtranjero(this.ctxHojaPrueba.getVehiculo().getNacionalidad().equals("E") ? "S" : "N");
                    enviarRuntSicov.setIdRunt(this.ctxCDA.getIdRunt().toString());
                    enviarRuntSicov.setNombreEmpleado(this.ctxHojaPrueba.getResponsable().getNombre());
                    enviarRuntSicov.setNumeroIdentificacion(this.ctxHojaPrueba.getResponsable().getCedula());

                    System.out.println("--------------------------------------------------------------");
                    System.out.println("Placa: "+ enviarRuntSicov.getPlaca());
                    System.out.println("Consecutivo: "+ enviarRuntSicov.getConsecutivoRUNT());
                    System.out.println("DireccionIdEquipo: "+ enviarRuntSicov.getDireccionIpEquipo());
                    System.out.println("Extrangero: "+ enviarRuntSicov.getExtranjero());
                    System.out.println("idRunt: "+ enviarRuntSicov.getIdRunt());
                    System.out.println("nombreEmpleado: "+ enviarRuntSicov.getNombreEmpleado());
                    System.out.println("numeroIdentificacion: "+ enviarRuntSicov.getNumeroIdentificacion());
                    System.out.println("--------------------------------------------------------------");

                    ClienteIndra envStub = new ClienteIndra();
                    envStub.setUrlSicov(ctxCDA.getUrlServicioSicov());
                    envStub.setUrlSicov2(ctxCDA.getUrlServicioSicov2());
                    envStub.setUrlSicovEncript(ctxCDA.getUrlServicioEncript());
                    System.out.println("Ubic Cliente sicov, Invocando metodo para envio del segundo FUR SetFur del clienteIndra");
                    ResponseDTO responseDTO = envStub.finalizaFur(enviarRuntSicov);
                    //SIMULAR ENVIO DE SEGUNDO FUR A INDRA
                    // ResponseDTO responseDTO = new ResponseDTO();
                    // responseDTO.setCodigoRespuesta("1");
                    
                                    

                    app.dispose();
                    if (responseDTO == null) {
                        JOptionPane.showMessageDialog(null, "Disculpe, no Puede Enviar el 2do. FUR debido a que no Tengo Comunicacion en estos Momentos con el Servidor SICOV ..! \n Compruebe que el servicio este Levantado sino es asi Comuniquese Por favor con la mesa de Ayuda de INDRA ");
                    }
                    System.out.println("Codigo de respuesta sicov: "+responseDTO.getCodigoRespuesta());
                    System.out.println("Descripcion de la respuesta sicov: "+responseDTO.getMensajeRespuesta());
                    if (responseDTO.getCodigoRespuesta().equals("1") || //ok
                        responseDTO.getMensajeRespuesta() == "#La revisión se encuentra en estado Finalizada.") { //ok

                        this.ctxHojaPrueba.setEstadoSICOV("SINCRONIZADO");
                        if (this.ctxHojaPrueba.getEstado().equalsIgnoreCase("APROBADA")) {
                            controlerCertificado.nvoCertificado(ctxCertificado, this.ctxHojaPrueba);
                        }
                        controller.update(this.ctxHojaPrueba);
                        JOptionPane.showMessageDialog(null, "Se ha Enviado el 2do FUR  perteneciente a la Placa  " + ctxHojaPrueba.getVehiculo().getPlaca() + " con exito ..!");
//////////////////////////////////////////////////////////////////////////////////INSERTO INFORMACION EN LA TABLA EVENTO SICOV/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        ev = new EventosDao();
                        System.out.println(ev.InsertarEvento(UsuarioLogueado.getNick(), this.ctxHojaPrueba.getVehiculo().getPlaca(), this.ctxHojaPrueba));
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    }else {
                        System.out.println("Fallo por " + responseDTO.getMensajeRespuesta());
                        JOptionPane.showMessageDialog(null, "No pude SINCRONIZAR FUR perteneciente a  la Placa " + ctxHojaPrueba.getVehiculo().getPlaca() + " debido a  " + responseDTO.getMensajeRespuesta() + "..!");
                    }

                }
            }// fin de logica de envio de doble FUR APLICADO INDRA 

        } catch (Exception e) {
            System.err.println("Error en el metodo : validandoEnvioIndra() " + e.getMessage());
        }
    }

    /**
     *
     *
     * @autor ELKIN B
     */
    private void validandoTiPoPrueba() {
        System.out.println("----------------------------------------------------");
        System.out.println("----------------VALIDANDO TIPO PRUEBA ---------------");
        System.out.println("----------------------------------------------------");

        try {
            boolean nunValido = false;
            this.ctxHojaPrueba.setEstado(controller.verificarHojaFinalizada(this.ctxHojaPrueba));

            if (this.ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("NO_APLICA") && this.ctxHojaPrueba.getPreventiva().equalsIgnoreCase("N")) {
                if (this.ctxHojaPrueba.getEstado().equalsIgnoreCase("APROBADA")) {
                    this.ctxHojaPrueba.setAprobado("Y");
                }
                if (this.ctxHojaPrueba.getEstado().equalsIgnoreCase("APROBADA") || this.ctxHojaPrueba.getEstado().equalsIgnoreCase("REPROBADA")) {
                    this.ctxHojaPrueba.setFinalizada("Y");
                }
                if (this.ctxHojaPrueba.getEstado().equalsIgnoreCase("PENDIENTE")) {
                    JOptionPane.showMessageDialog(null, "Se Le informa que esta Revision TecnoMecanica Todavia posee Pruebas Pendientes o no FINALIZADAS  de la Placa  " + ctxHojaPrueba.getVehiculo().getPlaca() + ", Asegurese de Terminarla y vuelva intertarlo ..Â¡");
                }
                if (this.ctxHojaPrueba.getFinalizada().equalsIgnoreCase("Y")) {
                    while (nunValido == false) {
                        String strConsecutivo = JOptionPane.showInputDialog("Ingrese el numero de certificado asignado por el RUNT ");
                        if (strConsecutivo.length() > 2) {
                            try {
                                this.ctxHojaPrueba.setConsecutivoRunt(strConsecutivo);
                                int opcion = JOptionPane.showOptionDialog(null, "Este es el  Consecutivo RUNT Asignado, " + strConsecutivo + " ¿Es Correcto? ", "Confirmacion Consecutivo RUNT",
                                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                                if (opcion == JOptionPane.OK_OPTION) {
                                    nunValido = true;
                                }
                            } catch (NumberFormatException ne) {
                                JOptionPane.showMessageDialog(null, "Disculpe el Consecutivo Ingresado no es Numerico");
                            }
                        } else {
                            JOptionPane.showOptionDialog(null, "Disculpe el numero CONSECUTIVO RUNT no cumple con las espcificaciones minima,\n Por favor Asegurese que este bien escrito y vuelva a intentarlo",
                                    "Envio Segundo FUR", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                        }
                    } // fin de ciclo de validacion                         
                    if (this.ctxHojaPrueba.getEstado().equalsIgnoreCase("APROBADA")) {
                        lstCertificado = new ArrayList();
                        nunValido = false;
                        ctxCertificado = new Certificado();
                        while (nunValido == false) {
                            String strConsecutivo = null;
                            try {
                                strConsecutivo = JOptionPane.showInputDialog("Ingrese el numero de certificado asignado en el  Preimpreso");
                                int opcion = JOptionPane.showOptionDialog(null, "Este es el  PREIMPRESO Ingresado, " + strConsecutivo + " ¿Es correcto? ", "Confirmacion Consecutivo",
                                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                                if (opcion == JOptionPane.OK_OPTION) {
                                    ctxCertificado.setConsecutivo(Long.parseLong(strConsecutivo));
                                    nunValido = true;
                                }
                            } catch (NumberFormatException ne) {
                                JOptionPane.showMessageDialog(null, "Disculpe el Consecutivo Preimpreso no es Numerico, Vuelva a Ingresarlo Por favor");
                            }
                        }// fin de validacion de ciclo ingresao de certificado                            
                        controlerCertificado = new CertificadoJpaController();
                        ctxCertificado.setConsecutivoRunt(this.ctxHojaPrueba.getConsecutivoRunt());
                        ctxCertificado.setHojaPruebas(this.ctxHojaPrueba);
                        ctxCertificado.setFechaImpresion(new Date());
                        ctxCertificado.setFechaExpedicion(new Date());
                        ctxCertificado.setTipo("A");
                        ctxCertificado.setAnulado("");
                        ctxCertificado.setComentario(" ");
                        ctxCertificado.setImpreso(" ");
                        lstCertificado.add(ctxCertificado);
                        ctxHojaPrueba.setCertificados(lstCertificado);
                        controlerCertificado.nvoCertificado(ctxCertificado, this.ctxHojaPrueba);
                    }
                    this.ctxHojaPrueba.setEstadoSICOV("SINCRONIZADO");
                    controller.update(this.ctxHojaPrueba);
                }
            }

        } catch (Exception e) {
            System.err.println("Error en el metodo : validandoTiPoPrueba() " + e.getMessage());
        }
    }

    /**
     *
     * @autor ELKIN B
     */
    private void cargarReporte() {
        System.out.println("----------------------------------------------------");
        System.out.println("----------------CARGANDO REPORTE--------------------");
        System.out.println("----------------------------------------------------");
        Connection cn = null;

        try {
            cn = UtilConexion.obtenerConexion();
            Consultas consultas = new Consultas();
            String estadoHojaPrueba = consultas.obtenerEstadoRevision(this.ctxHojaPrueba);

            if (estadoHojaPrueba.equalsIgnoreCase("Anulada")) {
                JOptionPane.showMessageDialog(null, "Revison Anulada");

            } else if (estadoHojaPrueba.equalsIgnoreCase("NoFinalizada")) { //la hoja de prueba no ha sido reportada como Finalizada, es decir no se ha cerrado ya sea por que ha caducado o porque no se ha impreso el certificado                
                String evaluacionPrueba = consultas.evaluarPrueba(cn, numeroHojaPrueba);

                if (evaluacionPrueba.equalsIgnoreCase("noFinalizada")) {//es decir no se han terminado todas las pruebas
                    System.out.println("hoja pruebas: "+this.ctxHojaPrueba);
                    System.out.println("cda: "+this.ctxCDA);
                    System.out.println("vehiculo: "+this.ctxHojaPrueba.getVehiculo());
                    System.out.println("placa: "+this.ctxHojaPrueba.getVehiculo().getPlaca());
                    Mensajes.mensajeAdvertencia("Se le Recuerda que No se han finalizado todas las pruebas ");
                    llamarReporte.cargarReporte(this.ctxHojaPrueba, this.ctxCDA, 0, this.ctxHojaPrueba.getVehiculo().getPlaca());//simplemente imprime el reporte
                } else if (evaluacionPrueba.equalsIgnoreCase("aprobada")) {//si la revision se da como aprobada                        
                    //Finalizar HP PREVENTIVA
                    if (consultas.isRevisionPreventiva(this.ctxHojaPrueba)) {
                        consultas.cerrarRevision(0, numeroHojaPrueba, true, cn);
                    } else {
                        System.out.println("Prueba Aprobada sin Finalizar");
                    }
                    llamarReporte.cargarReporte(this.ctxHojaPrueba, this.ctxCDA, 0, this.ctxHojaPrueba.getVehiculo().getPlaca());//mostrar el reporte
                } else if (evaluacionPrueba.equalsIgnoreCase("reprobada")) {//
                    llamarReporte.cargarReporte(this.ctxHojaPrueba, this.ctxCDA, 0, this.ctxHojaPrueba.getVehiculo().getPlaca());//mostrar el reporte                        
                } else {//cuando la hoja de prueba no esta aprobada ni reprobada no no Finalizada
                    JOptionPane.showMessageDialog(null, "Estado Indeterminado de la RevisionTecnoMecanica");
                    llamarReporte.cargarReporte(this.ctxHojaPrueba, this.ctxCDA, 0, this.ctxHojaPrueba.getVehiculo().getPlaca());//mostrar el reporte  
                }
            }//final de cuando la revision es nueva RevGasoCruco no se ha dado por terminada y/o aprobada
            else if (estadoHojaPrueba.equalsIgnoreCase("FinalizadaAprobada") || estadoHojaPrueba.equalsIgnoreCase("FinalizadaReprobada")) {
                llamarReporte.cargarReporte(this.ctxHojaPrueba, this.ctxCDA, 0, this.ctxHojaPrueba.getVehiculo().getPlaca());
            }//final del else de estado de la hoja de Revision.
            cn.close();

        } catch (Exception ex) {
            System.err.println("Error en el metodo : cargarReporte() " + ex.getMessage() + "---" + ex.getLocalizedMessage());

            corregirErrorColumnaFaltante(ex.getMessage());

            System.out.println("------------------------------------------Error");
            System.out.println(ex.toString());
            ex.printStackTrace();
        }finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException e) {
                }
            }

        }

    }

    private void corregirErrorColumnaFaltante(String errorMessage) {
        String verificarSiExisteLaColumnaSql = 
                "SELECT COUNT(*) AS column_exists FROM information_schema.columns " +
                "WHERE table_schema = ? AND table_name = 'defectos' AND column_name = 'grupo'";
    
        String updateTablaDefectosSql = "ALTER TABLE defectos ADD COLUMN grupo VARCHAR(100) DEFAULT 'En la tabla defectos puede actualizar el nombre del grupo para este defecto solamente'";
        
        System.out.println("----------------------------------------------------------------------");
        System.out.println("-----------Insertando variable para eliminar el error----------------");
        System.out.println("----------------------------------------------------------------------");
        
        Conexion.setConexionFromFile();
        
        try (java.sql.Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement existeLaColumna = conexion.prepareStatement(verificarSiExisteLaColumnaSql);
             PreparedStatement updateTabla = conexion.prepareStatement(updateTablaDefectosSql)) {
    
            // Pasar el nombre de la base de datos como parámetro
            existeLaColumna.setString(1, Conexion.getBaseDatos());
    
            try (ResultSet rc = existeLaColumna.executeQuery()) {
                if (rc.next() && rc.getInt("column_exists") == 0) {
                    // Ejecutar la actualización
                    updateTabla.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Por favor intente nuevamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Se presentó un problema: " + errorMessage + ". Por favor contacte con soporte Soltelec", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            
        } catch (SQLException ex) { 
            ex.printStackTrace();
            System.out.println("Error al tratar de corregir la tabla: " + ex.getMessage());
        }
    }

    public long getNumeroHojaPrueba() {
        return numeroHojaPrueba;
    }

    public void setNumeroHojaPrueba(long numeroHojaPrueba) {
        this.numeroHojaPrueba = numeroHojaPrueba;
    }

    public boolean isImprimirPdf() {
        return imprimirPdf;
    }

    public void setImprimirPdf(boolean imprimirPdf) {
        this.imprimirPdf = imprimirPdf;
    }

}

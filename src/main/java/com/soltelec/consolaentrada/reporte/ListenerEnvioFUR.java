
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.reporte;

import com.soltelec.consolaentrada.indra.dto.DatosFur;
import com.soltelec.consolaentrada.indra.dto.EstructuraEquipos;
import com.soltelec.consolaentrada.models.controllers.CdaJpaController;
import com.soltelec.consolaentrada.models.controllers.HojaPruebasJpaController;
import com.soltelec.consolaentrada.models.controllers.PruebaJpaController;
import com.soltelec.consolaentrada.models.entities.AuditoriaSicov;
import com.soltelec.consolaentrada.models.entities.Cda;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.Prueba;
import com.soltelec.consolaentrada.models.entities.ResponseDTO;
import com.soltelec.consolaentrada.models.entities.RespuestaDTO;
import com.soltelec.consolaentrada.sicov.ci2.ClienteCi2;
import com.soltelec.consolaentrada.sicov.ci2.ClienteCi2Servicio;
import com.soltelec.consolaentrada.sicov.indra.ClienteIndra;
import com.soltelec.consolaentrada.sicov.indra.ClienteIndraServicio;
import com.soltelec.consolaentrada.utilities.Mensajes;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.util.Log;

/**
 * Clase que inserta los datos del certificado en la base de datos. esta clase
 * es la que se invoca cuando se da click en el boton imprimr en el JRViewer
 *
 * @author Gerencia TIC
 */
public class ListenerEnvioFUR implements ActionListener {

    private HojaPruebas ctxHojaPrueba;
    private Long idHojaPrueba;
    private Cda ctxCDA;
    private Consultas consultas;
    private ConsultasCertificados consultasCertificados;
    private List<AuditoriaSicov> lstTramasEncontradas;

    public ListenerEnvioFUR(Long idHojaPrueba, List<AuditoriaSicov> lstTramasEncontradas) {
        this.idHojaPrueba = idHojaPrueba;
        consultas = new Consultas();
        this.lstTramasEncontradas = lstTramasEncontradas;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        String kilometrajeVariable="0";
        Connection cn = null;
        HojaPruebasJpaController controller = new HojaPruebasJpaController();
        ctxHojaPrueba = controller.find(idHojaPrueba.intValue());
        if (!ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("NO_APLICA")) 
        {
            if (ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("Iniciado"))
            {
                int seleccion = JOptionPane.showOptionDialog(null, "Esta a punto de enviar el PRIMER FUR de esta revisión tecnico mecanica; ¿Desea Continuar?",
                        "Envio Primer FUR", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (seleccion == JOptionPane.YES_OPTION) 
                {
                    JLabel labelVariable = new JLabel("Validando FUR Antes de Envio..!");
                    labelVariable.setLocation(10, 10);
                    labelVariable.setSize(100, 75);
                    JFrame ventanaPrincipal = new JFrame("Ventana principal");
                    JDialog app = new JDialog(ventanaPrincipal, "Validando FUR Antes de Envio");
                    app.setModal(false);
                    app.setLocation(200, 330);
                    app.setSize(600, 120);
                    app.add(labelVariable);
                    app.repaint();
                    app.setVisible(true);
                    CdaJpaController cdaControler = new CdaJpaController();
                    ctxCDA = cdaControler.find(1);
                    if (ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("Iniciado")) 
                    {
                        PruebaJpaController pruebasJPA = new PruebaJpaController();
                        List<Prueba> pruebas = pruebasJPA.findUltimasPruebasByHoja(ctxHojaPrueba.getId());
                        String even = "";
                        int posTrama = 0;
                        boolean encontrado = false;
                        for (Prueba p : pruebas) 
                        {
                            if (p.getTipoPrueba().getId() == 1) 
                            {
                                kilometrajeVariable= LlamarReporte.cargarKilometraje(p.getId());
                            }
                            if (p.getAbortado().equalsIgnoreCase("A") || p.getAbortado().equalsIgnoreCase("Y") || p.getFinalizada().equalsIgnoreCase("N")) {
                                app.setVisible(false);
                                JOptionPane.showMessageDialog(null, "Disculpe; no se puede reportar una revision tecnico mecanica a SICOV  si alguna de la pruebas se encuentra en  estado: PENDIENTES - ANULADAS ó ABORTADAS ");
                                return;
                            }
                            for (AuditoriaSicov auScv : lstTramasEncontradas) 
                            {
                                posTrama = auScv.getTRAMA().indexOf("idRegistro");
                                even = auScv.getTRAMA().substring(posTrama + 13, auScv.getTRAMA().length() - 2);
                                if (p.getId() == Integer.parseInt(even) && p.getTipoPrueba().getId() != 3) {
                                    encontrado = true;
                                    break;
                                }
                                if ( p.getTipoPrueba().getId()== 3) {
                                    encontrado = true;
                                    break;
                                }                           
                            }
                            if (encontrado == false) 
                            {
                                if(p.getFechaAborto()!=null )
                                {   
                                    
                                }else
                                {
                                    pruebasJPA.obtenerSeqSicov(p);    
                                }
                                if(p.getTipoPrueba().getId()==8)
                                {
                                    String diametro=null;
                                    if (ctxHojaPrueba.getVehiculo().getDiametro()!=null) 
                                    {
                                        diametro=String.valueOf(ctxHojaPrueba.getVehiculo().getDiametro());
                                    }
                                    pruebasJPA.tramaSicovGases(p, ctxCDA.getIdRunt(),ctxHojaPrueba.getVehiculo().getPlaca(),ctxHojaPrueba.getFormaMedTemperatura(),diametro,ctxHojaPrueba.getVehiculo().getTipoGasolina().getId());
                                }
                                if(p.getTipoPrueba().getId()==2)
                                {
                                    //pruebasJPA.restauracionTramaSicovLuces(p,ctxCDA.getIdRunt());
                                    pruebasJPA.tramaSicovLuces(p, ctxCDA.getIdRunt(),ctxHojaPrueba.getVehiculo().getPlaca());
                                    
                                }
                                if (p.getTipoPrueba().getId() == 5) 
                                {
                                     pruebasJPA.tramaSicovFrenos(p, ctxCDA.getIdRunt(),ctxHojaPrueba.getVehiculo().getPlaca());
                                    //pruebasJPA.restauracionTramaSicovFrenos(p, ctxCDA.getIdRunt());
                                }
                            }
                            encontrado = false;
                        }
                        app.setTitle("Enviando FUR al Servidor SICOV");
                        labelVariable.setText("Enviando FUR al Servidor SICOV..!");
                        app.repaint();
                        ctxHojaPrueba.setEstado(controller.verificarHojaFinalizada(ctxHojaPrueba));
                        if (ctxHojaPrueba.getEstado().equalsIgnoreCase("APROBADA")) 
                        {
                            ctxHojaPrueba.setAprobado("Y");
                        } else {
                            ctxHojaPrueba.setAprobado("N");
                            seleccion = JOptionPane.showOptionDialog(null, "Se le Comunica que la condicion de esta Revision TecnoMecanica es RECHAZADA \n Â¿Desea Continuar?",
                                    "Confirmacion Envio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                            if (seleccion == JOptionPane.NO_OPTION) {
                                app.setVisible(false);
                                return;
                        }
                        }
                        labelVariable.setText(" He presentado Problemas al Momento del Envio FUR, comuniquese con el Equipo de Soporte de SOLTELEC  ..!");
                        if (ctxHojaPrueba.getEstado().equalsIgnoreCase("APROBADA") || ctxHojaPrueba.getEstado().equalsIgnoreCase("REPROBADA")) {
                            ctxHojaPrueba.setFinalizada("Y");
                            System.out.println("Confirmacion de cierre de Hoja de prueba su estado es:  " + ctxHojaPrueba.getAprobado());
                        }
                        if (ctxHojaPrueba.getEstado().equalsIgnoreCase("PENDIENTE")) {
                            JOptionPane.showMessageDialog(null, "Disculpe, NO PUEDO REPORTAR  Revision TecnoMecanica dado que Todavia posee Pruebas Pendientes o no FINALIZADAS \n se le sugiere que   ASEGURARSE de Terminarla y luego intetente REPORTARLA ..Â¡");
                            app.setVisible(false);
                            return;
                        }
                    }
                    
                    if (ctxCDA.getProveedorSicov().equalsIgnoreCase("CI2")) 
                    {
                        System.out.println("pin: " + ctxHojaPrueba.getPin() );
                        if (ctxHojaPrueba.getPin().length() > 2 && ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("Iniciado")) {
                            ClienteCi2Servicio clienteSincoFur = new ClienteCi2Servicio(ctxHojaPrueba, ctxCDA);
                            clienteSincoFur.cargarInformacionBasica();
                            clienteSincoFur.cargarInformacionPropietario();
                            clienteSincoFur.cargarInformacionVehiculo(kilometrajeVariable);
                            clienteSincoFur.otrosDatos();
                            clienteSincoFur.datosPresionLabrado();
                            clienteSincoFur.cargarInformacionEmisionesAudible();
                            clienteSincoFur.cargarInformacionIntencidadLucesBajas();
                            clienteSincoFur.cargarInformacionSuspencion();
                            clienteSincoFur.cargarInformacionFrenos();
                            clienteSincoFur.cargarInformacionDesviacion();
                            clienteSincoFur.cargarInformacionTaximetro();
                            clienteSincoFur.cargarInformacionEmisionGasesCicloOtto();
                            clienteSincoFur.cargarInformacionEmisionGasesDiesel();
                            clienteSincoFur.datosVisual();
                            clienteSincoFur.datosObservaciones();
                            clienteSincoFur.obtenerListadoTecnicos();
                            try {
                                clienteSincoFur.configurarPermisibles();
                            } catch (ParseException ex) {
                                Logger.getLogger(ListenerEnvioFUR.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            clienteSincoFur.datosFotos();
                            ClienteCi2 clienteCi2 = new ClienteCi2(ctxCDA.getUrlServicioSicov());
                            RespuestaDTO respServidor = clienteCi2.enviarFur(clienteSincoFur.getFormulario());
                            //RespuestaDTO respServidor = new RespuestaDTO();
                            //respServidor.setCodigoRespuesta("0000");
                            app.dispose();
                            if (respServidor == null) {
                                Mensajes.mensajeAdvertencia(
                                "No se pudo enviar el primer FUR porque no hay comunicación con el servidor CI2 en este momento.\n"
                                + "Por favor, intente nuevamente en un minuto. Si el problema persiste, comuníquese con la Mesa de Ayuda.");
                                return;
                            }
                            if (respServidor.getCodigoRespuesta().equals("0000")) { //ok
                                ctxHojaPrueba.setEstadoSICOV("Env1FUR");
                                System.out.println("envie el primer fur");
                                Mensajes.mensajeCorrecto(
                                "El primer FUR correspondiente a la placa " 
                                + ctxHojaPrueba.getVehiculo().getPlaca() 
                                + " se ha enviado con éxito.\n"
                                + "Recuerde que acaba de reportar el resultado de las pruebas al SICOV.");
                            } else {
                                System.out.println("lo que se envio a sicov:\n"+ clienteSincoFur.getFormulario());
                                Mensajes.mensajeError(
                                "No se pudo enviar el primer FUR correspondiente a la placa " 
                                + ctxHojaPrueba.getVehiculo().getPlaca() 
                                + ". Respuesta sicov:\n" 
                                + respServidor.getMensajeRespuesta() 
                                + ".");
                                System.out.println("Fallo por " + respServidor.getMensajeRespuesta());
                                System.out.println("Kilometraje 2: "+kilometrajeVariable);
                            }
                            try {
                                controller.edit(this.ctxHojaPrueba);
                            } catch (Exception ex) {
                            }
                            return;
                        }

                    }// Fin de Validacion aplicado al Proveedor CI2
                    if (ctxCDA.getProveedorSicov().equalsIgnoreCase("INDRA"))
                    {
                        if (ctxHojaPrueba.getEstadoSICOV().equalsIgnoreCase("Iniciado")) 
                        {
                            
                            ClienteIndraServicio clienteIndra = new ClienteIndraServicio(ctxHojaPrueba, ctxCDA);
                            DatosFur datosFur = new DatosFur();
                            try {
                                clienteIndra.datosEquipos();
                                clienteIndra.datosSoftware();
                                EstructuraEquipos estructuraEquipos = new EstructuraEquipos();
                                estructuraEquipos.setEquipos(clienteIndra.getEquipos());
                                String equipo = estructuraEquipos.toString();
                                clienteIndra.datosPropietario();
                                clienteIndra.datosVehiculo(kilometrajeVariable);
                                clienteIndra.datosFotos();
                                System.out.println("Voy a Cargar Luces");
                                clienteIndra.datosLuces();
                                System.out.println("Voy a Cargar Fas");
                                clienteIndra.datosFas();
                                System.out.println("Voy a Cargar Visual Indra");
                                clienteIndra.datosVisualIndra();
                                clienteIndra.datosFirma();
                                clienteIndra.datosFurAsociado();
                                System.out.println("Voy a Cargar Taximetro");
                                if (ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 110) {
                                    clienteIndra.datosTaximetro(true);
                                } else {
                                    clienteIndra.datosTaximetro(false);
                                }
                                System.out.println("Voy a Cargar Observaciones");
                                clienteIndra.datosObservaciones();
                                String sufFUR = null;
                                if (ctxHojaPrueba.getReinspeccionList().isEmpty() && ctxHojaPrueba.getIntentos() < 2) {
                                    sufFUR = "-1";
                                } else {
                                    sufFUR = "-2";
                                }
                                System.out.println("Voy a Cargar Datos Runt");
                                clienteIndra.datosRunt(ctxCDA, 0, sufFUR);
                                clienteIndra.datosLlantas();
                                datosFur.setResultadoGases("");
                                datosFur.setInspeccionPrevia("");
//                                datosFur.getInspeccionPrevia();
                                if (ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 1 // Liviano
                                        || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 3 // Pesado
                                        || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 2 //4x4
                                        || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 6 //EnseÃ±anza
                                        || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 7 //Remolque
                                        || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 109 //taxis
                                        || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 110//aplicaTaximetro 
                                        //|| ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 5
                                        ) 
                                
                                {//Motocarro
                                    if (ctxHojaPrueba.getVehiculo().getTipoGasolina().getId() == 3) { //Diesel                    
                                        clienteIndra.datosGasesDiesel();
                                        datosFur.setResultadoGases(clienteIndra.getDatosGasesDiesel().toString());
                                        datosFur.setInspeccionPrevia(clienteIndra.getGasesVehiculosDisel().toString());
                                    } else if(ctxHojaPrueba.getVehiculo().getTipoGasolina().getId() != 2) {
                                        clienteIndra.datosGasesGasolina();
                                        datosFur.setResultadoGases(clienteIndra.getDatosGasesGasolina().toString());
                                        datosFur.setInspeccionPrevia(clienteIndra.getPreviaGasesVehiculos().toString());
                                    }
                                } else {
                                    System.out.println("Voy a Cargar Gases Motos");
                                    clienteIndra.datosGasesMoto();
                                    datosFur.setResultadoGases(clienteIndra.getDatosGasesMotos().toString());
                                    System.out.println("Voy a Cargar Inspeccion previa");
                                    datosFur.setInspeccionPrevia(clienteIndra.getPreviaGasesMotos().toString());
                                    String eve = clienteIndra.getPreviaGasesMotos().toString();
                                    int p = 0;
                                }
                                System.out.println("voy a cargar el objeto de envio al servicio");
                                datosFur.setCodigoProveedor(ctxCDA.getUsuarioSicov()); //Codigo de provedor de indra         
                                datosFur.setPropietario(clienteIndra.getPropietario().toString());
                                datosFur.setVehiculo(clienteIndra.getVehiculo().toStringIndra());
                                datosFur.setDatosFotos(clienteIndra.getDatosFotos().toString());
                                datosFur.setResultadoLuces(clienteIndra.getDatosLuces().toString());
                                datosFur.setResultadoFas(clienteIndra.getDatosFas().toString());
                                datosFur.setResultadoVisual(clienteIndra.getDatosVisual().toString());
                                datosFur.setResultadoTaximero(clienteIndra.getDatosTaximetro().toString());
                                datosFur.setObservaciones(clienteIndra.getDatosObservaciones());
                                datosFur.setRunt(clienteIndra.getDatosRunt().toString());
                                datosFur.setLlantas(clienteIndra.getDatosLlantas().toString());
                                datosFur.setEquipos(equipo);
                                datosFur.setSoftware(clienteIndra.getDatossoftware().toString());
                                datosFur.setFirma(clienteIndra.getDatosFirma().toString());
                                //TODO: asjustar fur asociados
                                System.out.println("CARGANDO FUR  ASOCIADO ");
                                if(clienteIndra.getDatosFurAsociados().toString().equalsIgnoreCase(""))
                                {
                                    datosFur.setFurAsociado(";");
                                    System.out.println(datosFur.getFurAsociado());
                                }else{
                                    datosFur.setFurAsociado(clienteIndra.getDatosFurAsociados().toString());
                                }
                               
                            } catch (Exception ex) {
                                throw ex;
                            }
                            //aqui es donde se genera la trama
                            ClienteIndra clienteIn = new ClienteIndra();
                            System.out.println("Configurando el stub");
                            clienteIn.setUrlSicov(ctxCDA.getUrlServicioSicov());
                            clienteIn.setUrlSicov2(ctxCDA.getUrlServicioSicov2());
                            System.out.println("URL Sicov " + ctxCDA.getUrlServicioSicov2());
                            clienteIn.setUrlSicovEncript(ctxCDA.getUrlServicioEncript());
                            System.out.println("URL Sicov " + ctxCDA.getUrlServicioEncript());
                            System.out.println("Ubic Cliente sicov, Invocando metodo SetFur del clienteIndra");
                            System.out.println("Trama fur indra");
                            System.out.println(datosFur.toString());
                            ResponseDTO responseDTO = clienteIn.setFur(datosFur.toString());

                            //guardarEnArchivo(datosFur.toString());

                            System.out.println(responseDTO);
                            app.dispose();
                            if (responseDTO == null) {
                                Mensajes.mensajeAdvertencia(
                                "No se pudo enviar el primer FUR porque no hay comunicación con el servidor de INDRA en este momento.\n"
                                + "Por favor, intente nuevamente en un minuto. Si el problema persiste, comuníquese con la Mesa de Ayuda.");
                                return;
                            }
                            if (responseDTO.getCodigoRespuesta().equals("1")) { //ok
                                ctxHojaPrueba.setEstadoSICOV("Env1FUR");
                                try {
                                    controller.edit(ctxHojaPrueba);
                                } catch (Exception ex) {
                                    System.out.println("Error editConstroller: "+ ex);
                                }
                                Mensajes.mensajeCorrecto(
                                "El primer FUR correspondiente a la placa " 
                                + ctxHojaPrueba.getVehiculo().getPlaca() 
                                + " se ha enviado con éxito.\n"
                                + "Recuerde que acaba de reportar el resultado de las pruebas al SICOV.");
                            } else {
                                //JOptionPane.showMessageDialog(null, "No pude Enviar 1er. FUR perteneciente a  la Placa " + ctxHojaPrueba.getVehiculo().getPlaca() + " debido a  " + responseDTO.getMensajeRespuesta() + "..!");
                                System.out.println("Lo que se envio a sicov:\n"+datosFur);
                                Mensajes.mensajeError(
                                "No se pudo enviar el primer FUR correspondiente a la placa " 
                                + ctxHojaPrueba.getVehiculo().getPlaca() 
                                + ". Respuesta sicov:\n" 
                                + responseDTO.getMensajeRespuesta());
                                System.out.println("Fallo por " + responseDTO.getMensajeRespuesta());
                                System.out.println("Kilometraje 1: "+kilometrajeVariable);
                            }
                            
                        }// fin de Logica de Envio del Primer FUR

                    }// Fin de Validacion aplicado al Proveedor INDRA
                }// Fin de Validacion Envio Primer FUR
            }//validacion de estado Inicial para enviar el primer FUR
        }
        try {
            if (ctxHojaPrueba.getFinalizada().equalsIgnoreCase("N")) {
                ctxHojaPrueba.setFinalizada(controller.verificarHojaFinalizada(ctxHojaPrueba));
                controller.edit(ctxHojaPrueba);
            }
        } catch (Exception ex) {
        }
    }

    public void guardarEnArchivo(String contenido) {
        // Formatear la fecha y hora actual para el nombre del archivo
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fechaHora = formatter.format(new Date());

        // Crear el nombre del archivo con la fecha y hora actuales
        String fileName = "datosFur_" + fechaHora + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Escribir los datos en el archivo
            writer.write(contenido);
            System.out.println("Archivo guardado como: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

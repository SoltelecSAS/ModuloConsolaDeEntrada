/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.reporte;

import com.soltelec.consolaentrada.models.entities.Medida;
import com.soltelec.consolaentrada.models.entities.Reinspeccion;
import com.soltelec.consolaentrada.models.entities.Cda;
import com.soltelec.consolaentrada.models.entities.Certificado;
import com.soltelec.consolaentrada.models.entities.Prueba;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.configuration.Conexion;
import com.soltelec.consolaentrada.custom.ModeloTablaImpresionFecha;
import com.soltelec.consolaentrada.models.controllers.CdaJpaController;
import com.soltelec.consolaentrada.models.controllers.HojaPruebasJpaController;
import com.soltelec.consolaentrada.models.controllers.ReinspeccionJpaController;
import com.soltelec.consolaentrada.models.controllers.ReporteJpaController;
import com.soltelec.consolaentrada.models.entities.AuditoriaSicov;
import com.soltelec.consolaentrada.utilities.CargarArchivos;
import com.soltelec.consolaentrada.utilities.Mensajes;
import com.soltelec.consolaentrada.utilities.SepararImagenes;
import com.soltelec.consolaentrada.utilities.Validaciones;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase se encarga de llenar el reporte en la base de datos. En esta clase
 * esta la logica de la evaluacion de la prueba
 *
 * @author Gerencia TIC
 */
public class LlamarReporte3625 {

    Map parametros = null;
    private boolean imprimirPdf;
    JasperReport report, report2;
    final String rutaReporte = "FUR03625.jasper";
//    final String rutaReporte = "./report1.jasper";
//    final String rutaReporte2 = "./report2.jasper";
    final String rutaReporte2 = "report2.jasper";
    ConsultasCertificados consultasCertificados;
    Consultas consultas;
    private ListenerPrimerCertificado listenerPrimerCertificado;
    private double ang, Int, angv, Intv, angv_izq, Intv_izq;
    public boolean int1, int2;
    private Reinspeccion reinspecionActual;
    private boolean reinspeccion;
    private HojaPruebas ctxHojaPrueba;
    private Cda ctxCDA;

    public LlamarReporte3625() {
        try {
            consultasCertificados = new ConsultasCertificados();
//            report = (JasperReport) JRLoader.loadObjectFromFile(rutaReporte);
            report = (JasperReport) JRLoader.loadObject(CargarArchivos.cargarArchivo(rutaReporte));
//            report2 = (JasperReport) JRLoader.loadObjectFromFile(rutaReporte2);
            report2 = (JasperReport) JRLoader.loadObject(CargarArchivos.cargarArchivo(rutaReporte2));
            consultas = new Consultas();
        } catch (Throwable ex) {
            // Mensajes.mostrarExcepcion(ex);
            int even = 0;
        }
    }

    public LlamarReporte3625(boolean imprimirPdf) {
        this.imprimirPdf = imprimirPdf;
        try {
            consultasCertificados = new ConsultasCertificados();
//            report = (JasperReport) JRLoader.loadObjectFromFile(rutaReporte);
            report = (JasperReport) JRLoader.loadObject(CargarArchivos.cargarArchivo(rutaReporte));
//            report2 = (JasperReport) JRLoader.loadObjectFromFile(rutaReporte2);
            report2 = (JasperReport) JRLoader.loadObject(CargarArchivos.cargarArchivo(rutaReporte2));
            consultas = new Consultas();
        } catch (Throwable ex) {
            // Mensajes.mostrarExcepcion(ex);
            int even = 0;
        }
    }

    /**
     * Metodo de entrada o de inicio a partir de este se llaman todos los demas
     * metodos de la clase se abre solamente una conexion
     *
     * Se modifica para hacerlo mas legible y que no cargue tanta informacion
     * que no ira a utilizar.
     *
     * @param hojaPruebas
     * @param consecutivoRUNT
     * @throws JRException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void cargarReporte(HojaPruebas ctxHojaPrueba, Cda ctxCDA, long consecutivoRUNT, String txtPlaca) throws JRException, SQLException, ClassNotFoundException {
        this.ctxHojaPrueba = ctxHojaPrueba;
        this.ctxCDA = ctxCDA;
        List<Prueba> ctxIndPruebas = null;
        try (Connection cn = cargarConexion()) {
            llamarProcedimientoMedidas(cn, this.ctxHojaPrueba.getId());
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }
            parametros = new HashMap();
            reinspeccion = this.ctxHojaPrueba.getReinspeccionList().size() > 0;  //consultas.isReinspeccion(this.ctxHojaPrueba.getId() , cn);            
            if (reinspeccion) {
                reinspecionActual = this.ctxHojaPrueba.getReinspeccionList().iterator().next();
                parametros.put("fecha", new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(reinspecionActual.getFechaSiguiente()));
                parametros.put("isReinspeccion", "X");
                parametros.put("idReinspeccion", reinspecionActual.getId());
                ctxIndPruebas = cargarIdPruebasNoDuplicadas(this.ctxHojaPrueba, "R");
                parametros.put("Comentarios", cargarComentariosFUR(this.ctxHojaPrueba, "R", ctxIndPruebas));
                cargarDilucion("R");
                cargarMedidas(ctxIndPruebas);
            } else {
                parametros.put("fecha", new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(this.ctxHojaPrueba.getFechaIngreso()));
                parametros.put("isReinspeccion", "NO");
                ctxIndPruebas = cargarIdPruebasNoDuplicadas(this.ctxHojaPrueba, "I");
                parametros.put("Comentarios", cargarComentariosFUR(this.ctxHojaPrueba, "I", ctxIndPruebas));
                cargarDilucion("I");
                cargarMedidas(ctxIndPruebas);
            }
            cargarInfo();
            configurarPermisibles();
            try {
                cargarImagen(reinspeccion, parametros, reinspeccion ? 1 : 0, false);
            } catch (Exception ex) {
            }

            cargarDefectos(cn, this.ctxHojaPrueba.getId(), txtPlaca, this.ctxHojaPrueba.getNroPruebasRegistradas());//Este evalua la prueba
            HojaPruebasJpaController hpContJpa = new HojaPruebasJpaController();
            String edoHP = (hpContJpa.verificarHojaFinalizada(ctxHojaPrueba));
            parametros.put("Aprobado", "");
            parametros.put("Reprobado", "");
            if (edoHP.equalsIgnoreCase("APROBADA")) {
                parametros.put("Aprobado", "X");
            }
            if (edoHP.equalsIgnoreCase("REPROBADA")) {
                parametros.put("Reprobado", "X");
                if (ctxHojaPrueba.getIntentos() == 1) {
                    Calendar calDias = Calendar.getInstance();
                    calDias.setTime(ctxHojaPrueba.getFechaIngreso());
                    calDias.add(Calendar.DATE, 14);
                    int m = calDias.get(Calendar.MONTH) + 1;
                    String mS;
                    if (m <= 9) {
                        mS = "0" + m;
                    } else {
                        mS = String.valueOf(m);
                    }
                    parametros.put("fechaFinRep", calDias.get(Calendar.DATE) + "/" + mS + "/" + calDias.get(Calendar.YEAR) + " Hora:" + calDias.get(Calendar.HOUR_OF_DAY) + ":" + calDias.get(Calendar.MINUTE) + ":" + calDias.get(Calendar.SECOND));
                } else {
                    parametros.put("fechaFinRep", " ");
                }
            }
            JasperPrint fillReport;

            boolean preventiva = consultas.isRevisionPreventiva(this.ctxHojaPrueba);
            if (preventiva) {
                fillReport = JasperFillManager.fillReport(report2, parametros, cn);
            } else {
                fillReport = JasperFillManager.fillReport(report, parametros, cn);
            }
//          JasperPrint fillReport = JasperFillManager.fillReport(report, parametros, cn);
//          JasperPrint fillReport2 = JasperFillManager.fillReport(report2, parametros, cn);
//          String destFileNamePdf="./certificado"+hojaPruebas+".pdf";

            File carpetaReportes = new File("C:\\Reportes");

            if (!carpetaReportes.exists()) {
                carpetaReportes.mkdir();
            }

            HojaPruebasJpaController hpControler = new HojaPruebasJpaController();
            List<AuditoriaSicov> lstTramas = hpControler.recogerTramasExist(this.ctxHojaPrueba);
            ListenerEnvioFUR listenerEnvioFUR = new ListenerEnvioFUR(this.ctxHojaPrueba.getId().longValue(), lstTramas);
            JRViewerModificado jvModificado = new JRViewerModificado(fillReport);
            jvModificado.setListenerImprimir(listenerEnvioFUR);
            JFrame app = new JFrame("TEST");
            app.setTitle("Impresion del FUR");
            app.setContentPane(jvModificado);

            String destFileNamePdf = "C:\\Reportes\\reporte" + this.ctxHojaPrueba.getId() + ".pdf";

            if (imprimirPdf) {
                JasperExportManager.exportReportToPdfFile(fillReport, destFileNamePdf);
                try {
                    File path = new File(destFileNamePdf);
                    Desktop.getDesktop().open(path);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
            } else {
                //Cambio FUR Preventiva
//                if (consultas.isRevisionPreventiva(hojaPruebas, cn)) {
//                    JasperViewer jasperViewer2 = new JasperViewer(fillReport2, false);
//                    jasperViewer2.setVisible(true);
//                } else {
                JasperViewer jasperViewer = new JasperViewer(fillReport, false);
                jasperViewer.setContentPane(jvModificado);
                if (preventiva) {
                    jasperViewer.setTitle("Impresion PREVENTIVA");
                } else {
                    jasperViewer.setTitle("Impresion FUR");
                }
                jasperViewer.setVisible(true);
            }

            //VERIFICAR QUE NO SEA UNA PREVENTIVA
//            consultas = new Consultas();
//            if (consultas.isRevisionPreventiva(hojaPruebas, cn)) {
            if (preventiva) {
                JOptionPane.showMessageDialog(null, "REVISION PREVENTIVA NO SE IMPRIME CERTIFICADO");
            } else if ((parametros.get("Aprobado")) != null && parametros.get("Aprobado").equals("X") && consecutivoRUNT > 0) {
                imprimirCertificado(this.ctxHojaPrueba.getId(), consecutivoRUNT, cn);
            }
        }
    }

    public void cargaEmegencia() {
        try {
            consultasCertificados = new ConsultasCertificados();
//            report = (JasperReport) JRLoader.loadObjectFromFile(rutaReporte);
            report = (JasperReport) JRLoader.loadObject(CargarArchivos.cargarArchivo(rutaReporte));
//            report2 = (JasperReport) JRLoader.loadObjectFromFile(rutaReporte2);
            consultas = new Consultas();
            report2 = (JasperReport) JRLoader.loadObject(CargarArchivos.cargarArchivo(rutaReporte2));
        } catch (Throwable ex) {
            //  Mensajes.mostrarExcepcion(ex);
            int eve = 0;
        }
    }

    /**
     * Este mÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â©todo
     * cargarÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ un reporte pero de
     * reinspecciÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â³n, es decir un
     * reporte que se desea mantener en la base de datos
     *
     * @param reinspeccion
     * @throws java.lang.Exception
     */
    public void cargarReporteReinspeccion(Reinspeccion reinspeccion) throws Exception {

        parametros = new HashMap();
        //cargar la hoja de prueba con la que esta asociada la reinspeccion
        parametros.put("idReinspeccion", reinspeccion.getId());
        //no llama al procedimiento de medidas, puede ser necesario crear otro procedimiento pero para
        //reinspeccion
        try (Connection cn = cargarConexion()) {
            //no llama al procedimiento de medidas, puede ser necesario crear otro procedimiento pero para
            //reinspeccio
            ConsultasReinspeccion crei = new ConsultasReinspeccion();
            int idHojaPrueba = crei.obtenerIdHojaPruebas(reinspeccion.getId(), cn);
            HojaPruebasJpaController hpc = new HojaPruebasJpaController();
            this.ctxHojaPrueba = hpc.find(idHojaPrueba);
            CdaJpaController cdaControler = cdaControler = new CdaJpaController();
            ctxCDA = cdaControler.find(1);
            configurarPermisibles();
            List<Integer> listaPruebasReinspeccion = crei.obtenerPruebasReinspeccion(idHojaPrueba, cn);
            ReporteJpaController rc = new ReporteJpaController();
            List<Prueba> listaPruebas = new ArrayList();
            for (Integer id : listaPruebasReinspeccion) {
                listaPruebas.add(rc.findPruebas(id));
            }
            cargarMedidas(listaPruebas);
            cargarDilucion("I");
            cargarInfo();
            parametros.put("Comentarios", cargarComentariosFUR(this.ctxHojaPrueba, "I", listaPruebas));
            parametros.put("consecutivoRUNT", this.ctxHojaPrueba.getConsecutivoRunt());
            parametros.put("numeroIntento", reinspeccion.getIntento());
            parametros.put("isReinspeccion", "NO");
            reinspecionActual = new ReinspeccionJpaController().findReinspeccionByHoja(idHojaPrueba);
            parametros.put("fecha", new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(reinspecionActual.getFechaAnterior()));
            cargarImagen(true, parametros, 0, true);
            if (reinspeccion.getAprobada().equals("Y")) {
                parametros.put("Aprobado", "X");
            } else {
                parametros.put("Reprobado", "X");
            }
            JasperPrint fillReport = JasperFillManager.fillReport(report, parametros, cn);
            //String destFileNamePdf="./certificado"+hojaPruebas+".pdf";
            File carpetaReportes = new File("C:\\Reportes");

            if (!carpetaReportes.exists()) {
                carpetaReportes.mkdir();
            }

            String destFileNamePdf = "C:\\Reportes\\reporte" + idHojaPrueba + ".pdf";

            if (imprimirPdf) {
                JasperExportManager.exportReportToPdfFile(fillReport, destFileNamePdf);
                // JasperRunManager.runReportToPdf(destFileNamePdf, parametros);
                try {
                    File path = new File(destFileNamePdf);
                    Desktop.getDesktop().open(path);
                } catch (IOException ex) {
                    Mensajes.mostrarExcepcion(ex);
                }
            } else {
                JasperViewer jasperViewer = new JasperViewer(fillReport, false); //generas tu visor del reporte
                jasperViewer.setVisible(true); //lo haces visible
            }
        }
    }

    /**
     * Abre una conexion con la base de datos
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection cargarConexion() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Conexion conex = Conexion.getInstance();
        Connection conexion = DriverManager.getConnection(conex.getUrl(), conex.getUsuario(), conex.getContrasena());
        return conexion;
    }

//    /**
//     * metodo de prueba...
//     *
//     * @param args
//     */
//    public static void main(String args[]) {
//        try {
//            LlamarReporte l = new LlamarReporte(false);
//            l.cargarReporte(16, 1500);
//        } catch (SQLException | ClassNotFoundException | JRException exc) {
//            Mensajes.mostrarExcepcion(exc);
//        }
//    }
    /**
     * Carga las medidas de las ultimas pruebas FINALIZADAS es decir si existe
     * una prueba no finalizada y autorizada el reporte mostrara las medidas de
     * la prueba anterior
     *
     * @param listaIds - La lista de las ultimas pruebas finalizadas
     * @param idHojaPrueba - La hoja de prueba
     */
    ///***********//EVALUACION TIPO SERVICION ENSEÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¹Ã…â€œANZA//////////****************************///////////*********
    public void cargarMedidas(List<Prueba> ctxListPrueba) throws ClassNotFoundException {
        parametros.put("ID_HP", (long) this.ctxHojaPrueba.getId());

        parametros.put("rpmCrucero", 0);

        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
        df.applyPattern("#0.00");//averiguar sobre DecimalFormat
        //09 06 2011
        //Quitar las pruebas duplicadas, dejar solamente una prueba para cada tipo
        /**
         * ***************************************************************************
         */
        //y que sea la ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Âºltima insertada es decir ordenada, si se ordena de mayor a menor la
        //primera prueba que encuentre va a ser la de id mayor
        //Ordenar la lista por id de pruebas
        //buscar las pruebas de Inspeccion Sensorial y seleccionar la prueba con id mayor 1
        //buscar las pruebas de luces y seleccionar la prueba con id mayor 2
        //buscar las pruebas de desviacin y seleccionar la prueba con id mayor 4
        //buscar las pruebas de freno y seleccionar la prueba con id mayor 5
        //buscar las pruebas de suspension y seleccionar la prueba con id mayor 6
        //busscar las pruebas de ruido y seleccionar la prueba con mayor id 7
        //buscar las pruebas de gases y seleccionar la prueba con mayor id 8
        //buscar la prueba de taximetro y seleccionar la prueba con mayor id 9
        for (Prueba p : ctxListPrueba) {
            List<Medida> listaMedidas = p.getMedidaList();
            if (listaMedidas != null) {
                if (p.getTipoPrueba().getId() == 8) {//pruebas de gases
                    for (Medida m : listaMedidas) {
                        switch (m.getTipoMedida().getId()) {
                            case 8001://medida de HC Ralenti Cuatro Tiempos
                                df.applyPattern("#");
                                parametros.put("HCRalenti", df.format(m.getValor()) + m.getCondicion());//Esta no se da nunca con decimales garantizado desde prueba
                                break;
                            case 8002://medida de CO Ralenti Cuatro tiempos
                                df.applyPattern("#0.00");
                                parametros.put("CORalenti", df.format(m.getValor()) + m.getCondicion());//Dos decimales
                                break;
                            case 8003:
                                df.applyPattern("#0.0");
                                if (p.getHojaPruebas().getVehiculo().getTipoGasolina().getId() == 1) {
                                    parametros.put("CO2Ralenti", df.format(m.getValor()) + m.getCondicion());//Un solo decimal
                                } else {
                                    parametros.put("CO2Ralenti", df.format(m.getValor()));//Un solo decimal
                                }
                                break;//medida de CO2 Ralenti Cuatro Tiempos
                            case 8004:
                                df.applyPattern("#0.00");
                                if (p.getHojaPruebas().getVehiculo().getTipoGasolina().getId() == 1) {
                                    parametros.put("O2Ralenti", df.format(m.getValor()) + m.getCondicion());//dos decimales   
                                } else {

                                    parametros.put("O2Ralenti", df.format(m.getValor()));
                                }
                                break;//medida de O2 Ralenti Cuatro Tiempos
                            case 8005:
                                df.applyPattern("##");
//                                parametros.put("RevGasoRal", df.format(m.getValor())); SART-26 Ajustes varios solicitados por auditorias punto 2
                                parametros.put("RevGasoRal", /*Validaciones.redondear(Double.parseDouble(*/ df.format(m.getValor()/*))*/));
                                break; //Temperatura de Aceite
                            case 8006://Revoluciones
                                df.applyPattern("##");
//                                parametros.put("TempGasoRal", df.format(m.getValor())); SART-26 Ajustes varios solicitados por auditorias punto 3
                                char temp = p.getHojaPruebas().getFormaMedTemperatura();
                                if (temp == 'C') {
                                    parametros.put("TempGasoRal", "");
                                } else {
                                    parametros.put("TempGasoRal", df.format(m.getValor()));
                                }
                                break;
                            case 8011://Revoluciones
                                df.applyPattern("##");
//                                parametros.put("RevGasoCruc", df.format(m.getValor()));  SART-26 Ajustes varios solicitados por auditorias punto 2
                                parametros.put("RevGasoCruc", Validaciones.redondear(Double.parseDouble(df.format(m.getValor()))));
                                break;
                            case 8012://Revoluciones
                                df.applyPattern("##");
//                                parametros.put("TempGasoCruc", df.format(m.getValor())); SART-26 Ajustes varios solicitados por auditorias punto 2 y 3
                                String valor = df.format(m.getValor());
                                char tempC = p.getHojaPruebas().getFormaMedTemperatura();
                                if (tempC == 'C') {
                                    parametros.put("TempGasoCruc", "");
                                } else {
                                    parametros.put("TempGasoCruc", valor);
                                }
                                break;
                            case 8018://medida de HC Ralenti Dos tiempos
                                df.applyPattern("#");
                                parametros.put("HCRalenti", df.format(m.getValor()) + m.getCondicion());//Sin decimales
                                break;
                            case 8019:// medida de CO Ralenti dos tiempos
                                df.applyPattern("#0.0");
                                if (p.getHojaPruebas().getVehiculo().getTipoGasolina().getId() == 1) {
                                    parametros.put("CO2Ralenti", df.format(m.getValor()) + m.getCondicion());//Dos decimales
                                } else {
                                    parametros.put("CO2Ralenti", df.format(m.getValor()));//Dos decimales
                                }
                                break;
                            case 8020://medida de CO2 Ralenti dos tiempos
                                df.applyPattern("#0.00");
                                parametros.put("CORalenti", df.format(m.getValor()) + m.getCondicion());//Un decimal
                                break;
                            case 8021://medida de O2 Ralenti dos tiempos
                                df.applyPattern("#0.00");
                                if (p.getHojaPruebas().getVehiculo().getTipoGasolina().getId() == 1) {
                                    parametros.put("O2Ralenti", df.format(m.getValor()) + m.getCondicion());//Dos decimales
                                } else {
                                    parametros.put("O2Ralenti", df.format(m.getValor()));//Dos decimales
                                }
                                break;
                            case 8022://medida de O2 Ralenti dos tiempos
                                df.applyPattern("##");
//                                parametros.put("TempGasoRal", df.format(m.getValor()) + m.getCondicion()); SART-26 Ajustes varios solicitados por auditorias punto 3

                                parametros.put("TempGasoRal", df.format(m.getValor()).equals("0") ? "" : df.format(m.getValor()));//Dos decimales

                                break;
                            case 8028://medida de O2 Ralenti dos tiempos
                                df.applyPattern("##");
                                parametros.put("RevGasoRal", df.format(m.getValor()) + m.getCondicion());//Dos decimales
                                break;
                            case 8007://medida de HC Crucero Cuatro tiempos
                                df.applyPattern("#");
                                parametros.put("HCCrucero", df.format(m.getValor()) + m.getCondicion());//No decimales
                                break;
                            case 8008: // medida de CO Crucero Cuatro tiempos
                                df.applyPattern("#0.00");
                                parametros.put("COCrucero", df.format(m.getValor()) + m.getCondicion());//dos decimales
                                break;
                            case 8009://medida de CO2  Crucero cuatro tiempos
                                df.applyPattern("#0.00");
                                if (p.getHojaPruebas().getVehiculo().getTipoGasolina().getId() == 1) {
                                    parametros.put("CO2Crucero", df.format(m.getValor()) + m.getCondicion());//Un decimal
                                } else {
                                    parametros.put("CO2Crucero", df.format(m.getValor()));//Un decimal
                                }
                                break;
                            case 8010://medida de O2 Crucero cuatro tiempos                               
                                df.applyPattern("#0.00");
                                if (p.getHojaPruebas().getVehiculo().getTipoGasolina().getId() == 1) {
                                    parametros.put("O2Crucero", df.format(m.getValor()) + m.getCondicion());//dos decimales 
                                } else {
                                    parametros.put("O2Crucero", df.format(m.getValor()));//dos decimales
                                }
                                break;
                            //MEDIDAS DE OPACIMETRO
                            case 8013:
                                df.applyPattern("#0.00");
                                parametros.put("OpCiclo2", df.format(m.getValor()) + m.getCondicion());//un decimal
                                break;//OPACIDAD CICLO2
                            case 8014:
                                df.applyPattern("#0.00");
                                parametros.put("OpCiclo3", df.format(m.getValor()) + m.getCondicion());//un decimal
                                break;//OPACIDAD CICLO3
                            case 8015:
                                df.applyPattern("#0.00");
                                parametros.put("OpCiclo4", df.format(m.getValor()) + m.getCondicion());//un decimal
                                break;//OPACIDAD CICLO4
                            case 8017:
                                df.applyPattern("#0.00");
                                parametros.put("ResultadoOp", df.format(m.getValor()) + m.getCondicion());//un decimal
                                break;
                            case 8033:
                                df.applyPattern("#0.00");
                                parametros.put("OpCiclo1", df.format(m.getValor()) + m.getCondicion());
                                break;
                            case 8034:
                                df.applyPattern("#");
//                                parametros.put("TempDiesel", df.format(m.getValor()) + m.getCondicion()); SART-26 Ajustes varios solicitados por auditorias punto 3
                                parametros.put("TempDiesel", df.format(m.getValor()).equals("0") ? "" : df.format(m.getValor()) + m.getCondicion());
                                break;
                            case 8036:
                                df.applyPattern("#");
//                                parametros.put("RevDiesel", df.format(m.getValor()) + m.getCondicion()); SART-26 Ajustes varios solicitados por auditorias punto 2
                                parametros.put("RevDiesel", Validaciones.redondear(Double.parseDouble(df.format(m.getValor()))) + m.getCondicion());
                                break;
                            default://Error con el codigo de la medida pra gases
                                break;
                        }//end of switch
                    }//end of for
                }//end of pruebagases
                if (p.getTipoPrueba().getId() == 2) {//pruebas de luces los decimales los da el equipo
                    for (Medida m : p.getMedidaList()) {
                        switch (m.getTipoMedida().getId()) {
                            case 2000://luz alta derecha esto no sale en el reporte
                                //parametros.put("",String.valueOf(m.getValor()));
                                break;
                            case 2001://luz alta izquierda no sale en el reporte
                                //parametros.put("SumaLuces",String.valueOf(m.getValor()));
                                break;
                            case 2002://angulo baja izquierda
                                df.applyPattern("#0.0");
                                parametros.put("AngInclIzq", df.format(m.getValor()) + m.getCondicion());
                                break;
                            case 2003://angulo baja derecha para vehiculos
                                //Cambio solo para Popayan, por problema en Luxometro
                                //angv = m.getValor();
                                df.applyPattern("#0.0");
                                parametros.put("AngInclDerecha", df.format(m.getValor()) + m.getCondicion());
                                break;
                            case 2005://angulo baja izquierda para vehiculos
                                //angv_izq = m.getValor();
                                df.applyPattern("#0.0");
                                parametros.put("AngInclIzq", df.format(m.getValor()) + m.getCondicion());
                                break;
                            case 2006://intensidad luz baja derecha para vehiculos
                                df.applyPattern("#0.0");
                                parametros.put("IntBajaDerecha", df.format(m.getValor()) + m.getCondicion());
//                                Intv = m.getValor();
//                                if (angv == 0 && Intv == 0) {
//                                    parametros.put("IntBajaDerecha", 0);
//                                } else {
//                                    parametros.put("IntBajaDerecha", df.format(m.getValor()) + m.getCondicion());
//                                }
//                                if (angv != 0 && Intv == 0) {
//                                    parametros.put("IntBajaDerecha", 1);
//                                    int1 = true;
//                                } else {
//                                    parametros.put("IntBajaDerecha", df.format(m.getValor()) + m.getCondicion());
//                                }
                                break;
                            case 2007://intensidad de la luz alta derecha para vehiculos no sale en los reportes oficialmente
                                //parametros.put("AngInclIzq",String.valueOf(m.getValor()));
                                break;
                            case 2008://intensidad de las luces exploradoras
                                //esta medida no sale en el reporte impreso
                                break;
                            case 2009://intensidad de la luz baja izquierda para vehiculos
                                df.applyPattern("#0.0");
                                parametros.put("IntBajaIzq", df.format(m.getValor()) + m.getCondicion());
//                                Intv_izq = m.getValor();
//                                if (angv_izq == 0 && Intv_izq == 0) {
//                                    parametros.put("IntBajaIzq", 0);
//                                } else {
//                                    parametros.put("IntBajaIzq", df.format(m.getValor()) + m.getCondicion());
//                                }
//                                if (angv_izq != 0 && Intv_izq == 0) {
//                                    parametros.put("IntBajaIzq", 1);
//                                    int2 = true;
//                                } else {
//                                    parametros.put("IntBajaIzq", df.format(m.getValor()) + m.getCondicion());
//                                }
                                break;
                            case 2010://intensidad de la luz alta izquierda para vehiculo
                                //Esta medida no sale en los reportes
                                break;
                            case 2011: //Sumatoria de luces en vehiculos
                                df.applyPattern("#0.0");
                                parametros.put("SumaLuces", df.format(m.getValor()) + m.getCondicion());
                                break;
                            case 2013://angulo baja derecha motos
                                df.applyPattern("#0.0");
                                ang = m.getValor();
                                parametros.put("AngInclDerecha", df.format(m.getValor()) + m.getCondicion());
                                break;
                            case 2014://luz baja derecha Motos
                                df.applyPattern("#0.0");
                                parametros.put("IntBajaDerecha", df.format(m.getValor()) + m.getCondicion());
//                                Int = m.getValor();
//                                //Cambio solo para Popayan, por problema en Luxometro
//                                System.out.println("Angulo:" + ang + "Intensidad:" + Int);
//                                if (ang == 0 && Int == 0) {
//                                    parametros.put("IntBajaDerecha", 0);
//                                } else {
//                                    parametros.put("IntBajaDerecha", df.format(m.getValor()) + m.getCondicion());
//                                }
//                                if (ang != 0 && Int == 0) {
//                                    parametros.put("IntBajaDerecha", 1);
//                                } else {
//                                    parametros.put("IntBajaDerecha", df.format(m.getValor()) + m.getCondicion());
//                                }
                                break;
                            case 2015://luz baja izquierda
                                df.applyPattern("#0.0");
                                parametros.put("IntBajaIzq", df.format(m.getValor()) + m.getCondicion());
                                break;
                            case 2016://intensidad alt derecha en motocarro
                                //parametros.put("SumaLuces",String.valueOf(m.getValor()));//no sale en el reporte
                                break;
                            case 2017://intensidad alta izquierda en motocarro
                                //parametros.put("SumaLuces",String.valueOf(m.getValor()));
                                break;
                            case 2018://intensidad baja derecha motocarro
                                df.applyPattern("#0.0");
                                parametros.put("IntBajaDerecha", df.format(m.getValor()) + m.getCondicion());
                                break;
                            case 2019://intensidad baja izquierda en motocarro
                                parametros.put("IntBajaIzq", df.format(m.getValor()) + m.getCondicion());
                                break;
                            case 2020://
                                df.applyPattern("#0.0");
                                parametros.put("AngInclIzq", df.format(m.getValor()) + m.getCondicion());
                                break;
                            case 2021://luz alta izquierda
                                df.applyPattern("#0.0");
                                parametros.put("AngInclDerecha", df.format(m.getValor()) + m.getCondicion());
                                break;
                            default:
                                break;
                        }//end of switch
                    }//end of for ista de medidas
                }//end of if pruebas de luces
                if (int1 && int2) {
                    parametros.put("SumaLuces", 2);
                }

                if (p.getTipoPrueba().getId() == 5) {//pruebas de frenos sin decimales
                    df.applyPattern("#");
                    Float PsEje1Der = 0.0F;
                    Float PsEje2Der = 0.0F;
                    Float PsEje3Der = 0.0F;
                    Float PsEje4Der = 0.0F;
                    Float PsEje5Der = 0.0F;
                    Float PsEje1Izq = 0.0F;
                    Float PsEje2Izq = 0.0F;
                    Float PsEje3Izq = 0.0F;
                    Float PsEje4Izq = 0.0F;
                    Float PsEje5Izq = 0.0F;

                    Float FrzEje1Der = 0.0F;
                    Float FrzEje2Der = 0.0F;
                    Float FrzEje3Der = 0.0F;
                    Float FrzEje4Der = 0.0F;
                    Float FrzEje5Der = 0.0F;
                    Float FrzEje1Izq = 0.0F;
                    Float FrzEje2Izq = 0.0F;
                    Float FrzEje3Izq = 0.0F;
                    Float FrzEje4Izq = 0.0F;
                    Float FrzEje5Izq = 0.0F;

                    for (Medida m : p.getMedidaList()) {
                        switch (m.getTipoMedida().getId()) {
                            case 5000://peso derecho eje 1
                                df.applyPattern("#");
                                parametros.put("PsEje1Der", df.format(m.getValor()) + m.getCondicion());
                                PsEje1Der = m.getValor();
                                break;
                            case 5001://peso derecho eje 2
                                df.applyPattern("#");
                                parametros.put("PsEje2Der", df.format(m.getValor()) + m.getCondicion());
                                PsEje2Der = m.getValor();
                                break;
                            case 5002:
                                parametros.put("PsEje3Der", df.format(m.getValor()) + m.getCondicion());
                                PsEje3Der = m.getValor();
                                break;
                            case 5003:
                                parametros.put("PsEje4Der", df.format(m.getValor()) + m.getCondicion());
                                PsEje4Der = m.getValor();
                                break;
                            case 5025:
                                parametros.put("PsEje5Der", df.format(m.getValor()) + m.getCondicion());
                                PsEje5Der = m.getValor();
                                break;
                            case 5004:
                                parametros.put("PsEje1Izq", df.format(m.getValor()) + m.getCondicion());
                                PsEje1Izq = m.getValor();
                                break;
                            case 5005:
                                parametros.put("PsEje2Izq", df.format(m.getValor()) + m.getCondicion());
                                PsEje2Izq = m.getValor();
                                break;
                            case 5006:
                                parametros.put("PsEje3Izq", df.format(m.getValor()) + m.getCondicion());
                                PsEje3Izq = m.getValor();
                                break;
                            case 5007:
                                parametros.put("PsEje4Izq", df.format(m.getValor()) + m.getCondicion());
                                PsEje4Izq = m.getValor();
                                break;
                            case 5026:
                                parametros.put("PsEje5Izq", df.format(m.getValor()) + m.getCondicion());
                                PsEje5Izq = m.getValor();
                                break;
                            case 5008://fuerza de frenado eje 1
                                df.applyPattern("#");
                                parametros.put("FrzEje1Der", df.format(m.getValor()) + m.getCondicion());
                                FrzEje1Der = m.getValor();
                                break;
                            case 5009://fuerza de frenado eje2
                                df.applyPattern("#");
                                parametros.put("FrzEje2Der", df.format(m.getValor()) + m.getCondicion());
                                FrzEje2Der = m.getValor();
                                break;
                            case 5010://fuerza de frenado eje2
                                parametros.put("FrzEje3Der", df.format(m.getValor()) + m.getCondicion());
                                FrzEje3Der = m.getValor();
                                break;
                            case 5011://fuerza de frenado eje2
                                parametros.put("FrzEje4Der", df.format(m.getValor()) + m.getCondicion());
                                FrzEje4Der = m.getValor();
                                break;
                            case 5027:
                                parametros.put("FrzEje5Der", df.format(m.getValor()) + m.getCondicion());
                                FrzEje5Der = m.getValor();
                                break;
                            case 5012://fuerza de frenado eje2
                                df.applyPattern("#");
                                parametros.put("FrzEje1Izq", df.format(m.getValor()) + m.getCondicion());
                                FrzEje1Izq = m.getValor();
                                break;
                            case 5013://fuerza de frenado eje2
                                df.applyPattern("#");
                                parametros.put("FrzEje2Izq", df.format(m.getValor()) + m.getCondicion());
                                FrzEje2Izq = m.getValor();
                                break;
                            case 5014://fuerza de frenado eje2
                                parametros.put("FrzEje3Izq", df.format(m.getValor()) + m.getCondicion());
                                FrzEje3Izq = m.getValor();
                                break;
                            case 5015://fuerza de frenado eje2
                                parametros.put("FrzEje4Izq", df.format(m.getValor()) + m.getCondicion());
                                FrzEje4Izq = m.getValor();
                                break;
                            case 5028://fuerza de frenado eje2
                                parametros.put("FrzEje5Izq", df.format(m.getValor()) + m.getCondicion());
                                FrzEje5Izq = m.getValor();
                                break;
                            case 5024://Eficacia de frenado
                                df.applyPattern("#0.0");
                                parametros.put("EficTotal", df.format(m.getValor()) + m.getCondicion());//no decimal
                                break;
                            case 5032:
                                df.applyPattern("#0.00");
                                parametros.put("DesEje1", df.format(m.getValor()) + m.getCondicion());//no decimal
                                break;
                            case 5033:
                                df.applyPattern("#0.00");
                                parametros.put("DesEje2", df.format(m.getValor()) + m.getCondicion());//no decimal
                                break;
                            case 5034:
                                df.applyPattern("#0.00");
                                parametros.put("DesEje3", df.format(m.getValor()) + m.getCondicion());//no decimal
                                break;
                            case 5035:
                                df.applyPattern("#0.00");
                                parametros.put("DesEje4", df.format(m.getValor()) + m.getCondicion());//no decimal
                                break;
                            case 5031:
                                df.applyPattern("#0.00");
                                parametros.put("DesEje5", df.format(m.getValor()) + m.getCondicion());//no decimal
                                break;
                            case 5036:
                                df.applyPattern("#0.00");
                                df.setMaximumFractionDigits(2);
                                parametros.put("EficAux", df.format(m.getValor()) + m.getCondicion());//un decimal
                            default:
                                break;
                        }//end of switch
                    }//end of for ista de medidas
                }
                if (p.getTipoPrueba().getId() == 7) {//pruebas de sonometro sin decimales
                    double promedioRuidoPito = 0;
                    double promedioRuidoMotor = 0;
                    int tipoMedida;
                    for (Medida m : p.getMedidaList()) {
                        tipoMedida = m.getTipoMedida().getId();
                        if (tipoMedida == 7002) {
                            promedioRuidoPito = m.getValor();
                        }
                        if (tipoMedida == 7005) {
                            promedioRuidoMotor = m.getValor();
                        }
                        //promedioRuidoPito /= 3;
                        df.applyPattern("#.0#");
                        df.setMaximumFractionDigits(1);
                        parametros.put("RuidoPito", df.format(promedioRuidoPito) + m.getCondicion());//un solo decimal
                        //promedioRuidoMotor /= 3;
                        parametros.put("RuidoMotor", df.format(promedioRuidoMotor) + m.getCondicion());//un solo decimal
                    }
                }
                if (p.getTipoPrueba().getId() == 6) {//pruebas de suspension no decimales
                    df.applyPattern("#0.00");//un solo digito no creo no
                    for (Medida m : p.getMedidaList()) {
                        switch (m.getTipoMedida().getId()) {
                            case 6016:
                                parametros.put("DelanteraDer", df.format(m.getValor()) + m.getCondicion());//sin decimales
                                break;
                            case 6020:
                                parametros.put("DelanteraIzq", df.format(m.getValor()) + m.getCondicion());//sin decimales
                                break;
                            case 6017:
                                parametros.put("TraseraDerecha", df.format(m.getValor()) + m.getCondicion());//sin decimales
                                break;
                            case 6021:
                                parametros.put("TraseraIzq", df.format(m.getValor()) + m.getCondicion());//sin decimales
                                break;
                            default:
                                break;
                        }//end of switch
                    }//end of for ista de medidas
                }
                if (p.getTipoPrueba().getId() == 4) {//prueba de desviacion ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¿cuantos decimales?
                    df.applyPattern("#0.0#");
                    df.setMaximumFractionDigits(1);
                    for (Medida m : p.getMedidaList()) {
                        switch (m.getTipoMedida().getId()) {
                            case 4000:
                                parametros.put("DvcnEje1", df.format(m.getValor()) + m.getCondicion());//un decimal
                                break;
                            case 4001:
                                parametros.put("DvcnEje2", df.format(m.getValor()) + m.getCondicion());//un decimal
                                break;
                            case 4002:
                                parametros.put("DvcnEje3", df.format(m.getValor()) + m.getCondicion());//un decimal
                                break;
                            case 4003:
                                parametros.put("DvcnEje4", df.format(m.getValor()) + m.getCondicion());//un decimal
                                break;
                            case 4004:
                                parametros.put("DvcnEje5", df.format(m.getValor()) + m.getCondicion());//un decimal
                                break;
                            default:
                                break;
                        }//end of switch
                    }//end of for ista de medidas
                }//end if
                if (p.getTipoPrueba().getId() == 9) {//prueba de taximetro cuantos decimales
                    df.applyPattern("#0.0");
                    df.setMaximumFractionDigits(1);
                    for (Medida m : p.getMedidaList()) {
                        switch (m.getTipoMedida().getId()) {
//                            case 9000: Se remmplaza para dar solucion al caso SART-9 Modificar cÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â³digos de medidas de taxÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â­metros en fur
                            case 9002:
                                parametros.put("ErrorDistancia", df.format(m.getValor()) + m.getCondicion());//un solo decimal
                                break;
//                            case 9001: Se remmplaza para dar solucion al caso SART-9 Modificar cÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â³digos de medidas de taxÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â­metros en fur
                            case 9003:
                                parametros.put("ErrorTiempo", df.format(m.getValor()) + m.getCondicion());//un solo decimal
                                break;
                            default:
                                break;

                        }//end of switch
                    }//end of for lista de medidas
                    parametros.put("PerTaxi", "+/-2");//pone el permisible del taximetro
                    //pone la referencia comercial de la llanta
                    if (this.ctxHojaPrueba != null) {
                        String refLlanta = this.ctxHojaPrueba.getVehiculo().getLlantas().getNombre();
                        parametros.put("RefLlanta", refLlanta);
                    }
                }

//                 if (p.getTipoPrueba().getId() == 3) {//prueba foto
//                    String sql = "SELECT CARTYPE,Modelo,Tiempos_motor,FUELTYPE,SERVICE,v.Diametro from vehiculos as v inner join hoja_pruebas as hp on v.CAR=hp.Vehiculo_for where hp.TESTSHEET = ?";
//                    PreparedStatement ps = cn.prepareStatement(sql);
//                    ps.setLong(1, idHojaPrueba);
//                    ResultSet rs = ps.executeQuery();
//                    rs.next();//primer registro
//
//        if (rs.getInt("CARTYPE") == 5) {}
//                 }
            }//edn of listaMedidas != null
        }//end of for listaPruebas
    }//end of method configurarMedidas

    public void cargarFotos(Integer s, long idHojaPrueba) {

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
    private void configurarPermisibles() throws SQLException {
        //los permisibles dependen del tipo del vehiculo, del modelo del vehiculo
        //de los tiempos y del tipo de combustible

//        if (rs.getInt("FUELTYPE") == 2) {
//            parametros.put("CO2Ralenti", df.format(m.getValor()));
//        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date fechaInicioResolucion = null;
        Date fechaIngresoVehiculo = null;

        if (this.ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 5) {//Motocarro
            //parametros.put("PerO2","[0-11]");
            parametros.put("PerCO2", "---");
            //Desviacion
            parametros.put("PerDesv", "---");
            //Taximetro
            parametros.put("PerTaxi", "---");
            //Suspension
            parametros.put("PerSusp", "---");
            //Frenos
            parametros.put("PerEficTotal", "30");//30%
            parametros.put("PerEficAux", "18");//no hay desequilibrio
            parametros.put("PerDeseq", "30");
            parametros.put("PerSumaLuces", "---");
            //Desviacion : el permisible es solamente uno
            //Dispositivos de cobro: el permisible es solamente uno
            //Gases :

            if (this.ctxHojaPrueba.getVehiculo().getTiemposMotor() == 4) {
                parametros.put("PerO2", "[0-6]");
            }
            if (this.ctxHojaPrueba.getVehiculo().getTiemposMotor() == 2 && this.ctxHojaPrueba.getVehiculo().getModelo() < 2010) {
                parametros.put("PerO2", "[0-11]");
            } else {
                parametros.put("PerO2", "[0-6]");
            }

            if (this.ctxHojaPrueba.getVehiculo().getTiemposMotor() == 2 && this.ctxHojaPrueba.getVehiculo().getModelo() <= 2009) {
                parametros.put("PerHCRal", "[0-10000]");
            } else {
                parametros.put("PerHCRal", "[0-2000]");
            }
            parametros.put("PerHCCruc", "---");
            parametros.put("PerCOCruc", "---");
            parametros.put("PerCORal", "[0-4.5]");
            //no mostrar permisibles de Opacidad porque ninguna moto es diesel
            parametros.put("PerOpac", "---");
        }

        if (this.ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 4) {//Motocicletas
            //parametros.put("PerO2","[0-11]");
            parametros.put("PerCO2", "---");
            //Desviacion
            parametros.put("PerDesv", "---");
            //Taximetro
            parametros.put("PerTaxi", "---");
            //Suspension
            parametros.put("PerSusp", "---");
            //Frenos
            parametros.put("PerEficTotal", "30");//30%
            parametros.put("PerEficAux", "---");//no hay desequilibrio
            parametros.put("PerDeseq", "---");
            parametros.put("PerSumaLuces", "---");
            //Desviacion : el permisible es solamente uno
            //Dispositivos de cobro: el permisible es solamente uno
            //Gases :
            if (this.ctxHojaPrueba.getVehiculo().getTiemposMotor() == 4) {
                parametros.put("PerO2", "[0-6]");
            }
            if (this.ctxHojaPrueba.getVehiculo().getTiemposMotor() == 2 && this.ctxHojaPrueba.getVehiculo().getModelo() < 2010) {
                parametros.put("PerO2", "[0-11]");
            } else {
                parametros.put("PerO2", "[0-6]");
            }
            if (this.ctxHojaPrueba.getVehiculo().getTiemposMotor() == 2 && this.ctxHojaPrueba.getVehiculo().getModelo() <= 2009) {
                parametros.put("PerHCRal", "[0-10000]");
            } else {
                if (fechaIngresoVehiculo.compareTo(fechaInicioResolucion) > 0) {
                    parametros.put("PerHCRal", "[0-1600]");
                } else {
                    parametros.put("PerHCRal", "[0-2000]");
                }
            }

            try {
                fechaInicioResolucion = sdf.parse("2022-08-18");
                fechaIngresoVehiculo = sdf.parse(this.ctxHojaPrueba.getFechaIngreso().toString());
            } catch (ParseException ex) {
                Logger.getLogger(LlamarReporte3625.class.getName()).log(Level.SEVERE, null, ex);
            }

            parametros.put("PerHCCruc", "---");
            parametros.put("PerCOCruc", "---");
            if (fechaIngresoVehiculo.compareTo(fechaInicioResolucion) > 0) {
                parametros.put("PerCORal", "[0-3.6]");
            } else {
                parametros.put("PerCORal", "[0-4.5]");
            }
            //no mostrar permisibles de Opacidad porque ninguna moto es diesel
            parametros.put("PerOpac", "---");
        } else if (this.ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 1 || this.ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 3 || this.ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 2 || this.ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 109 || this.ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 110) {// 1 -> Livianos ,  3-> pesados, 2-> 4x4 109 or 110-> aplica a taxi 
            if (this.ctxHojaPrueba.getVehiculo().getTipoGasolina().getId() == 1 || this.ctxHojaPrueba.getVehiculo().getTipoGasolina().getId() == 4) {
                parametros.put("PerO2", "[0-5]");
                parametros.put("PerCO2", "[7-17]");
            } else {
                parametros.put("PerO2", " ");
                parametros.put("PerCO2", " ");
            }
            //Desviacion
            parametros.put("PerDesv", "10");
            //Taximetro
            if (this.ctxHojaPrueba.getVehiculo().getServicios().getId() == 2) {
                parametros.put("PerTaxi", "+/-2");
            } else {
                parametros.put("PerTaxi", "---");
            }
            //Suspension: se realiza suspension a todos los vehiculos livianos
            //if(rs.getInt("CARTYPE")== 1)
            parametros.put("PerSusp", "40");
            //else
            //parametros.put("PerSusp","---");//a pesados no se realiza suspension
            //Frenos:
            parametros.put("PerEficTotal", "50");
            parametros.put("PerEficAux", "18");
            parametros.put("PerDeseq", "30");//ojo de 20 a 30 es defecto tipo B
            parametros.put("PerSumaLuces", "225");
            //Gases
            if (this.ctxHojaPrueba.getVehiculo().getTipoGasolina().getId() == 1) {//si es gasolina
                if (this.ctxHojaPrueba.getVehiculo().getModelo() <= 1970) {
                    parametros.put("PerHCRal", "[0-800]");
                    parametros.put("PerCORal", "[0-5.0]");
                    parametros.put("PerHCCruc", "[0-800]");
                    parametros.put("PerCOCruc", "[0-5.0]");
                } else if (this.ctxHojaPrueba.getVehiculo().getModelo() > 1970 && this.ctxHojaPrueba.getVehiculo().getModelo() <= 1984) {
                    parametros.put("PerHCRal", "[0-650]");
                    parametros.put("PerCORal", "[0-4.0]");
                    parametros.put("PerHCCruc", "[0-650]");
                    parametros.put("PerCOCruc", "[0-4.0]");
                } else if (this.ctxHojaPrueba.getVehiculo().getModelo() > 1984 && this.ctxHojaPrueba.getVehiculo().getModelo() <= 1997) {
                    parametros.put("PerHCRal", "[0-400]");
                    parametros.put("PerCORal", "[0-3.0]");
                    parametros.put("PerHCCruc", "[0-400]");
                    parametros.put("PerCOCruc", "[0-3.0]");
                } else if (this.ctxHojaPrueba.getVehiculo().getModelo() > 1997) {
                    parametros.put("PerHCRal", "[0-200]");
                    parametros.put("PerCORal", "[0-1.0]");
                    parametros.put("PerHCCruc", "[0-200]");
                    parametros.put("PerCOCruc", "[0-1.0]");
                }
                parametros.put("PerOpac", "---");
            } else if (this.ctxHojaPrueba.getVehiculo().getTipoGasolina().getId() == 4) {//si es gas gasolina o es gas natural vehicular
                if (this.ctxHojaPrueba.getVehiculo().getModelo() <= 1970) {
                    parametros.put("PerHCRal", "[0-800]");
                    parametros.put("PerCORal", "[0-5.0]");
                    parametros.put("PerHCCruc", "[0-800]");
                    parametros.put("PerCOCruc", "[0-5.0]");
                } else if (this.ctxHojaPrueba.getVehiculo().getModelo() > 1970 && this.ctxHojaPrueba.getVehiculo().getModelo() <= 1984) {
                    parametros.put("PerHCRal", "[0-650]");
                    parametros.put("PerCORal", "[0-4.0]");
                    parametros.put("PerHCCruc", "[0-650]");
                    parametros.put("PerCOCruc", "[0-4.0]");
                } else if (this.ctxHojaPrueba.getVehiculo().getModelo() > 1984 && this.ctxHojaPrueba.getVehiculo().getModelo() <= 1997) {
                    parametros.put("PerHCRal", "[0-400]");
                    parametros.put("PerCORal", "[0-3.0]");
                    parametros.put("PerHCCruc", "[0-400]");
                    parametros.put("PerCOCruc", "[0-3.0]");
                } else if (this.ctxHojaPrueba.getVehiculo().getModelo() > 1997) {
                    parametros.put("PerHCRal", "[0-200]");
                    parametros.put("PerCORal", "[0-1.0]");
                    parametros.put("PerHCCruc", "[0-200]");
                    parametros.put("PerCOCruc", "[0-1.0]");
                }
            } else if (this.ctxHojaPrueba.getVehiculo().getTipoGasolina().getId() == 3) {//si es diesel
                parametros.put("PerHCRal", "---");
                parametros.put("PerCORal", "---");
                parametros.put("PerHCCruc", "---");
                parametros.put("PerCOCruc", "---");
                if (this.ctxHojaPrueba.getVehiculo().getModelo() <= 1970) {
                    parametros.put("PerOpac", "50");
                } else if (this.ctxHojaPrueba.getVehiculo().getModelo() > 1970 && this.ctxHojaPrueba.getVehiculo().getModelo() <= 1984) {
                    parametros.put("PerOpac", "45");
                } else if (this.ctxHojaPrueba.getVehiculo().getModelo() > 1984 && this.ctxHojaPrueba.getVehiculo().getModelo() <= 1997) {
                    parametros.put("PerOpac", "40");
                } else if (this.ctxHojaPrueba.getVehiculo().getModelo() > 1997) {
                    parametros.put("PerOpac", "35");
                }
                int diametro = 430;
                parametros.put("diametro", String.valueOf(diametro));
            }//end of diesel
        }
    }//end of methoc configurarPermisibles

    /**
     * Llena la tabla de resumen de los defectos por grupo Evalua la prueba
     * colocando el parametro aprobado o no aprobado Los defectos se cargan de
     * la ultima prueba Finalizada LOGICA DE EVALUACION DE LA REVISON TECNICO
     * MECANICA EN ESTE METODO
     *
     * @param cn
     * @param hojaPruebas
     * @throws SQLException
     */
    private void cargarDefectos(Connection cn, long hojaPruebas, String placa, Integer numeroPruebasAutorizadas) throws SQLException {
        ResultSet rs;

//        String strTotal = "select count(d.Tipo_defecto) from pruebas as pr "
//                + "inner join defxprueba as dp on pr.id_pruebas=dp.id_prueba "
//                + "inner join defectos as d on dp.id_defecto = d.CARDEFAULT "
//                + "inner join grupos as g on d.DEFGROUP = g.DEFGROUP "
//                + "inner join sub_grupos as sg on g.DEFGROUP = sg.SCDEFGROUPSUB "
//                + "where pr.id_pruebas in "
//                + "(select max(id_pruebas) from pruebas as p where p.hoja_pruebas_for = ?  AND p.Finalizada = 'Y' group by  p.Tipo_prueba_for ) "
//                + "and  d.Tipo_defecto=? and pr.hoja_pruebas_for = ? and pr.Finalizada = 'Y'";
        String strTotal = "SELECT COUNT(d.Tipo_defecto)\n"
                + "FROM pruebas AS pr\n"
                + "INNER JOIN defxprueba AS dp ON pr.id_pruebas=dp.id_prueba\n"
                + "INNER JOIN defectos AS d ON dp.id_defecto = d.CARDEFAULT\n"
                + "INNER JOIN sub_grupos AS sg ON sg.SCDEFGROUPSUB = d.DEFGROUPSSUB\n"
                + "WHERE pr.id_pruebas IN \n"
                + " (\n"
                + "SELECT MAX(id_pruebas)\n"
                + "FROM pruebas AS p\n"
                + "WHERE p.hoja_pruebas_for = ? AND p.Finalizada = 'Y' AND p.abortada='N'\n"
                + "GROUP BY p.Tipo_prueba_for) AND d.Tipo_defecto=? AND pr.hoja_pruebas_for = ? AND pr.Finalizada = 'Y' AND pr.abortada='N'";
        PreparedStatement consultaTotal = cn.prepareStatement(strTotal);
        consultaTotal.setLong(1, hojaPruebas);
        consultaTotal.setString(2, "A");
        consultaTotal.setLong(3, hojaPruebas);
        rs = consultaTotal.executeQuery();
        rs.first();
        int totalDefA = rs.getInt(1);
        parametros.put("TotalDefA", String.valueOf(totalDefA));
        consultaTotal.clearParameters();
        consultaTotal.setLong(1, hojaPruebas);
        consultaTotal.setString(2, "B");
        consultaTotal.setLong(3, hojaPruebas);
        rs = consultaTotal.executeQuery();
        rs.first();
        int totalDefB = rs.getInt(1);
        parametros.put("TotalDefB", String.valueOf(totalDefB));

        ///***********//EVALUACION TIPO SERVICION ENSEÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¹Ã…â€œANZA//////////****************************///////////*********
        String ensenanza = "select count(*)  FROM vehiculos as v "
                + "WHERE v.esEnsenaza=1  AND v.CARPLATE= ?";
        PreparedStatement validaTipo = cn.prepareStatement(ensenanza);
        validaTipo.setString(1, placa);
        rs = validaTipo.executeQuery();
        rs.next();
        int valida = rs.getInt(1);

        ///***********//EVALUACION DE LA PRUEBA DE ENSEÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¹Ã…â€œANZA//////////****************************///////////*********
//        String strTotalDefEnsenanza = "select count(*)  from hoja_pruebas as hp "
//                + "INNER JOIN pruebas as p ON hp.TESTSHEET=p.hoja_pruebas_for "
//                + "INNER JOIN defxprueba as dp ON p.Id_Pruebas=dp.id_prueba "
//                + "INNER JOIN defectos as d ON d.CARDEFAULT=dp.id_defecto "
//                + "WHERE hp.TESTSHEET = ? "
//                + "AND p.Tipo_prueba_for = 1 "
//                + "AND d.DEFGROUP=21";
        String strTotalDefEnsenanza = "SELECT COUNT(*)\n"
                + "FROM hoja_pruebas AS hp\n"
                + "INNER JOIN pruebas AS p ON hp.TESTSHEET=p.hoja_pruebas_for\n"
                + "INNER JOIN defxprueba AS dp ON p.Id_Pruebas=dp.id_prueba\n"
                + "INNER JOIN defectos AS d ON d.CARDEFAULT=dp.id_defecto\n"
                + "INNER JOIN grupos_sub_grupos gsg on gsg.SCDEFGROUPSUB = d.DEFGROUPSSUB\n"
                + "WHERE hp.TESTSHEET = ? AND p.Tipo_prueba_for = 1 AND gsg.DEFGROUP=21;";

        PreparedStatement psDefectosVisualEnse = cn.prepareStatement(strTotalDefEnsenanza);
        psDefectosVisualEnse.setLong(1, hojaPruebas);
        rs = psDefectosVisualEnse.executeQuery();
        rs.next();
        int defEnse = rs.getInt(1);

        if (valida > 0) {
            if (defEnse > 0) {
                parametros.put("ReprobadoEnse", "X");
            } else {
                parametros.put("AprobadoEnse", "X");
            }
        }

        ///***********//EVALUACION DE LA PRUEBA DE Inspeccion Sensorial//////////****************************///////////*********
        //Si existe un defecto tipo A entonces rechazar la prueba
        String strIVTipoA = "select count(d.Tipo_defecto) from pruebas as pr "
                + "inner join defxprueba as dp on pr.id_pruebas=dp.id_prueba "
                + "inner join defectos as d on dp.id_defecto = d.CARDEFAULT "
                + "where pr.id_pruebas = "
                + "(select max(id_pruebas) from pruebas as p where p.hoja_pruebas_for = ? and p.Tipo_prueba_for = 1 and p.Finalizada = 'Y' AND p.abortada='N') "
                + " and d.Tipo_defecto=? and pr.hoja_pruebas_for = ? and pr.Tipo_prueba_for = 1 and pr.Finalizada = 'Y' AND pr.Abortada='N' ";
        PreparedStatement psDefectosVisual = cn.prepareStatement(strIVTipoA);
        psDefectosVisual.setLong(1, hojaPruebas);
        psDefectosVisual.setString(2, "A");
        psDefectosVisual.setLong(3, hojaPruebas);
        rs = psDefectosVisual.executeQuery();
        rs.next();
        int defectosA = rs.getInt(1);
        psDefectosVisual.clearParameters();
        psDefectosVisual.setLong(1, hojaPruebas);
        psDefectosVisual.setString(2, "B");
        psDefectosVisual.setLong(3, hojaPruebas);
        rs = psDefectosVisual.executeQuery();
        rs.next();
        int defectosB = rs.getInt(1);
        //JOptionPane.showMessageDialog(null,"Defectos A: " + defectosA +"\n Defectos B:" + defectosB);
        //Consultar el numero de la prueba
        String strNumPrueba = "select max(id_pruebas) from pruebas as p where p.hoja_pruebas_for = ? and p.Tipo_prueba_for =1 and p.Finalizada = 'Y' AND p.Abortada='N'";
        PreparedStatement psNumPrueba = cn.prepareStatement(strNumPrueba);
        psNumPrueba.setLong(1, hojaPruebas);
        rs = psNumPrueba.executeQuery();
        rs.next();
        long numeroPruebaIV = rs.getLong(1);
        String strIVEval = "UPDATE pruebas SET Aprobada = ? where Id_Pruebas = ?";
        PreparedStatement psEvalIV = cn.prepareStatement(strIVEval);

        if (defectosA > 0) {//solo desaprueba la Inspeccion Sensorial si encuentra defectos tipo A o en el peor caso de defectos tipo B
            psEvalIV.setString(1, "N");
        } else {
            psEvalIV.setString(1, "Y");
        }

        psEvalIV.setLong(2, numeroPruebaIV);
        int pruebasEjecutadas = psEvalIV.executeUpdate();
        System.out.println("Pruebas de IV Actualizadas: " + pruebasEjecutadas);
        //Deben estar terminadas todas las pruebas y aprobadas todas
        //porque se da el caso que las pruebas estan terminadas pero existen algunas
        //no aprobadas o que no registraban el defecto lo cual
        //conduce a inconsistencias
        //si no estan terminadas entonces nada no se da rechazado ni aprobado
        //para motos: iv,gases,sonometro,frenos,luces una consulta
        //independientemente del tipo del vehiculo... si el numero de defectos tipo A es mayor a 0 es rechazado
        //deben estar aprobadas todas las pruebas autorizadas

        //como saber cuantas pruebas estan autorizadas para que todas esten finalizadas y
        //aprobadas sin importar el detalle del sonometro
        //Cuenta las pruebas finalizadas y aprobadas numero que debe ser igual al numero de pruebas
        //autorizadas
        String sqlPruebasFinalizadas = "select count(*) from ( "
                + "select max(id_pruebas) from pruebas as p "
                + "inner join hoja_pruebas as hp on p.hoja_pruebas_for = hp.TESTSHEET "
                + "where hp.TESTSHEET = ? and p.Finalizada = 'Y' AND p.Abortada='N' group by p.tipo_prueba_for) as tmp";

        //Cuenta las pruebas finalizadas y aprobadas numero que debe ser igual al numero de pruebas
        //autorizadas
        PreparedStatement consultaPruebasFinalizadas = cn.prepareStatement(sqlPruebasFinalizadas);
        consultaPruebasFinalizadas.setLong(1, hojaPruebas);
        rs = consultaPruebasFinalizadas.executeQuery();
        rs.next();
        int numeroPruebasFinalizadas = rs.getInt(1);//numero de pruebas finalizadas y aprobadas no es exacto en el caso cuando
        //se autoriza de nuevo una prueba de foto porque cuenta la prueba anterior.
        //Contar las pruebas autorizadas es decir contar los tipos de pruebas autorizadas

        /*String pruebasAutorizadas = "select count(*) from ( "
         + "select max(id_pruebas) from pruebas as p "
         + "inner join hoja_pruebas as hp on p.hoja_pruebas_for = hp.TESTSHEET "
         + "where hp.TESTSHEET = ? group by p.tipo_prueba_for) as tmp";
         PreparedStatement consultaPruebasAutorizadas = cn.prepareStatement(pruebasAutorizadas);
         consultaPruebasAutorizadas.setLong(1, hojaPruebas);
         rs = consultaPruebasAutorizadas.executeQuery();
         rs.next();
         int numeroPruebasAutorizadas = rs.getInt(1);//trae el numero de pruebas autorizadas las cuales*/
        //deben ser iguales a el numero de pruebas finalizadas y aprobadas
        String strPruebasNoFinalizadas = "select count(*) from ("
                + "select max(id_pruebas) from pruebas as p inner join tipo_prueba as tp "
                + "on p.Tipo_prueba_for = tp.TESTTYPE where p.hoja_pruebas_for = ? and p.Finalizada = 'Y' AND p.Abortada='N' AND p.Aprobada = 'N' group by p.Tipo_prueba_for) as tmp";
        PreparedStatement psNoFinalizadas = cn.prepareStatement(strPruebasNoFinalizadas);
        psNoFinalizadas.setLong(1, hojaPruebas);
        rs = psNoFinalizadas.executeQuery();
        int numeroPruebasNoFinalizadas = 0;
        if (rs.next()) {
            numeroPruebasNoFinalizadas = rs.getInt(1);
        }

        //Numero de pruebas finalizadas = numero de pruebas finalizadas - numero de pruebas pendientes;
        //numeroPruebasFinalizadas = numeroPruebasFinalizadas + numeroPruebasNoFinalizadas;
        //Los parametros del vehiculo
        String sql = "SELECT CARTYPE,Modelo,Tiempos_motor,FUELTYPE,SERVICE,CLASS,SPSERVICE from vehiculos as v inner join hoja_pruebas as hp on v.CAR=hp.Vehiculo_for where hp.TESTSHEET = ?";
        PreparedStatement ps = cn.prepareStatement(sql);
        ps.setLong(1, hojaPruebas);
        ResultSet rs1 = ps.executeQuery();
        rs1.next();//primer registroy
        switch (rs1.getInt("CARTYPE")) {
            case 4:
                //Motos
                //iv,gases,frenos,luces,sonometro,foto
                //debe hacer mÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡s de 5 pruebas teniendo en cuenta que el sonometro se realize asi no se tomen medidas
                //debe haber aprobado el mismo numero de pruebas que se autorizaron
                if (numeroPruebasFinalizadas >= 5 && (numeroPruebasFinalizadas == numeroPruebasAutorizadas)) {
                    if (totalDefA > 0) {
                        parametros.put("Reprobado", "X");
                    } else//si no tiene defectos tipo A
                    {
                        if (totalDefB <= 4) {
                            parametros.put("Aprobado", "X");
                        } else {
                            parametros.put("ComentDefectos", "Vehiculo Reprobado Defectos tipo B es Mayor a 5");
                            parametros.put("Reprobado", "X");
                        }//fin si no tiene defectos tipo A
                    }
                } else {//si no ha terminado todas las pruebas
                    parametros.put("Reprobado", "");
                }//fin else mas de un defecto tipo A
                break;
            case 3:
                //Pesados
                //Pesado no hace suspension minimo 6 pruebas obligatorias
                //sinembargo debe haber terminado y aprobado todas las pruebas
                if (numeroPruebasFinalizadas >= 6 && numeroPruebasFinalizadas == numeroPruebasAutorizadas) {
                    if (totalDefA > 0) {//si tiene un defecto tipo A no aprueba independientemente del tipo de servicio
                        parametros.put("Reprobado", "X");
                    } else {//si es de servicio publico
                        int servicio = rs1.getInt("SERVICE");
                        if (servicio == 3 || servicio == 1 || servicio == 4) {//particular u oficial o diplomatico
                            if (totalDefB < 10) {
                                parametros.put("Aprobado", "X");
                            } else {
                                parametros.put("Reprobado", "X");
                            }
                        } else if (servicio == 2 || servicio == 5) {//publico y ensenanza
                            if (totalDefB < 5) {//con 5 o mas se reprueba
                                parametros.put("Aprobado", "X");
                            } else {
                                parametros.put("Reprobado", "X");
                            }
                        }//end servicio publico
//                    int ensenanza = rs1.getInt("CLASS");

                    }//fin else defectos tipoA
                } else {//si no ha terminado todas las pruebas
                    parametros.put("Reprobado", "");
                }//fin else de no terminar un minimo de pruebas
                break;
            case 2:
                //4x4 debe hacer minimo 6 pruebas
                //1.Inspeccion Sensorial 2.Gases 3.Frenos 4.Luces 5. Suspension es opcional
                //     6.Foto //7.sonometro 7 pruebas como minimo de ahi hay que mirar si faltan pruebas
                if (numeroPruebasFinalizadas >= 6 && numeroPruebasAutorizadas == numeroPruebasFinalizadas) {
                    if (totalDefA > 0) {
                        parametros.put("Reprobado", "X");
                    } else {//si no hay defectos tipo A verficar los defectos tipo B
                        int servicio = rs1.getInt("SERVICE");
                        if (servicio == 3 || servicio == 1 || servicio == 4) {//si es particular, oficial o diplomatico 9 defectos
                            if (totalDefB < 10) {
                                parametros.put("Aprobado", "X");
                            } else {
                                parametros.put("Reprobado", "X");
                            }
                        } else if (servicio == 2 || servicio == 5) {//publico
                            if (totalDefB < 5) {//con 5 o mas se reprueba
                                parametros.put("Aprobado", "X");
                            } else {
                                parametros.put("Reprobado", "X");
                            }
                        }
//                    int ensenanza = rs1.getInt("CLASS");
                        if (rs1.getInt("CLASS") == 27) {//si es de ensenanza
                            if (totalDefB < 5) {//con 5 o mas se reprueba
                                parametros.put("Aprobado", "X");
                            } else {
                                parametros.put("Reprobado", "X");
                            }
                        }//fin ensenanza
                    }//fin else defectos tipoA
                } else {//si no ha terminado todas las pruebas
                    parametros.put("Reprobado", "");
                }//fn else de no terminar un minimo de pruebas
                break;
            //fin de livianos
            case 1:
                //Livianos
                //1.Inspeccion Sensorial 2.Gases 3.Frenos 4.Luces 5. Suspension 6. Desviacion
                //7.Foto //8.sonometro 7 no a todos los livianos se les hace suspension
                if (numeroPruebasFinalizadas >= 7 && numeroPruebasFinalizadas == numeroPruebasAutorizadas) {
                    //si es de servicio publico
                    if (totalDefA > 0) {
                        parametros.put("Reprobado", "X");
                    } else {//si no hay defectos tipo A
                        int servicio = rs1.getInt("SERVICE");
                        if (servicio == 3 || servicio == 1 || servicio == 4) {//particular diplomatico u oficial
                            if (totalDefB < 10) {
                                parametros.put("Aprobado", "X");
                            } else {
                                parametros.put("Reprobado", "X");
                            }
                        } else if (servicio == 2 || servicio == 5) {//publico o servicio especial
                            if (totalDefB < 5) {//con 5 o mas se reprueba
                                parametros.put("Aprobado", "X");
                            } else {
                                parametros.put("Reprobado", "X");
                            }
                        }//end servicio publico
//                    

                    }//fin else defectos tipoA
                } else {//si no ha terminado todas las pruebas
                    parametros.put("Reprobado", "");
                }//fn else de no terminar un minimo de pruebas pero terminar todas si es mayor el numero de pruebas al minimo
                break;
            //fin de motocarros
            case 5:
                //MotoCarro
                //1.Inspeccion Sensorial 2.Gases 3.Frenos 4.Luces 5. Foto suspension seria opcional.
                if (numeroPruebasFinalizadas >= 5 && numeroPruebasFinalizadas == numeroPruebasAutorizadas) {
                    //si es de servicio publico
                    if (totalDefA > 0) {
                        parametros.put("Reprobado", "X");
                    } else {//si no hay defectos tipo A
                        int servicio = rs1.getInt("SERVICE");
                        if (servicio == 3 || servicio == 1 || servicio == 4) {//particular diplomatico u oficial
                            if (totalDefB < 7) {
                                parametros.put("Aprobado", "X");
                            } else {
                                parametros.put("Reprobado", "X");
                            }
                        } else if (servicio == 2 || servicio == 5) {//publico o servicio ensenanza
                            if (totalDefB < 5) {//con 5 o mas se reprueba
                                parametros.put("Aprobado", "X");
                            } else {
                                parametros.put("Reprobado", "X");
                            }
                        }//end servicio publico
//                    int ensenanza = rs1.getInt("CLASS");

                    }//fin else defectos tipoA
                } else {//si no ha terminado todas las pruebas
                    parametros.put("Reprobado", "");
                }//fn else de no terminar un minimo de pruebas pero terminar todas si es mayor el numero de pruebas al minimo
                break;
            //fin de livianos
            case 7:
                //Remolques
                //1.Inspeccion Sensorial 2.Frenos  3.Foto
                if (numeroPruebasFinalizadas >= 1 && numeroPruebasFinalizadas == numeroPruebasAutorizadas) {
                    //si es de servicio publico
                    if (totalDefA > 0) {
                        parametros.put("Reprobado", "X");
                    } else {//si no hay defectos tipo A
                        int servicio = rs1.getInt("SERVICE");
                        if (servicio == 3 || servicio == 1 || servicio == 4) {//particular diplomatico u oficial
                            if (totalDefB < 10) {
                                parametros.put("Aprobado", "X");
                            } else {
                                parametros.put("Reprobado", "X");
                            }
                        } else if (servicio == 2 || servicio == 5) {//publico o servicio especial
                            if (totalDefB < 5) {//con 5 o mas se reprueba
                                parametros.put("Aprobado", "X");
                            } else {
                                parametros.put("Reprobado", "X");
                            }
                        }//end servicio publico
//                    int ensenanza = rs1.getInt("CLASS");

                    }//fin else defectos tipoA
                } else {//si no ha terminado todas las pruebas
                    parametros.put("Reprobado", "");
                }//fn else de no terminar un minimo de pruebas pero terminar todas si es mayor el numero de pruebas al minimo
                break;
            case 110:
                //Aplica prueba Taximetro
                //1.Inspeccion Sensorial 2.Gases 3.Frenos 4.Luces 5. Suspension 6. Desviacion
                //7.Foto //8.sonometro 7 no a todos los livianos se les hace suspension
                if (numeroPruebasFinalizadas >= 7 && numeroPruebasFinalizadas == numeroPruebasAutorizadas) {
                    //si es de servicio publico
                    if (totalDefA > 0) {
                        parametros.put("Reprobado", "X");
                    } else {//si no hay defectos tipo A                        
                        if (totalDefB < 10) {
                            parametros.put("Aprobado", "X");
                        } else {
                            parametros.put("Reprobado", "X");
                        }
                    }//end servicio publico/                  
                } else {//si no ha terminado todas las pruebas
                    parametros.put("Reprobado", "");
                }//fn else de no terminar un minimo de pruebas pero terminar todas si es mayor el numero de pruebas al minimo
                break;
            //fin de motocarros
            default:
                break;
        }
    }//fin del metodo cargarDefectos

    /**
     * Pone el tipo de documento.. el comentario del CDA y los datos del CDA
     * antes colocaba las pruebas no finalizadas
     *
     * @param hojaPruebas
     * @param cn
     */
    private void cargarInfo() {

        HojaPruebasJpaController hojaPruebasController = new HojaPruebasJpaController();
        HojaPruebas tempHojaPruebas = hojaPruebasController.find((int) this.ctxHojaPrueba.getId());
        ConsultasReinspeccion crei = new ConsultasReinspeccion();
        ResultSet rs;

        String tipoDoc = this.ctxHojaPrueba.getPropietario().getTipoIdentificacion().name();
        //Colocar la expresion

        StringBuilder sb = new StringBuilder();
        sb.append("CC.(");
        String tmpString = tipoDoc.equals("CC") ? "X" : " ";
        sb.append(tmpString);
        sb.append(") NIT.(");
        tmpString = tipoDoc.equals("NIT") ? "X" : " ";
        sb.append(tmpString);
//        sb.append(") CE.( ");
//        tmpString = tipoDoc.equals("CE") ? "X" : " ";
//        sb.append(tmpString);
        sb.append(")");
        parametros.put("exprTipoDoc", sb.toString());

        String numeroIntento = String.valueOf(this.ctxHojaPrueba.getIntentos());
        String consecutivoRunt = this.ctxHojaPrueba.getConsecutivoRunt();
        parametros.put("numeroIntento", numeroIntento);
        //FUR ASOCIADO
        if (Integer.valueOf(numeroIntento) == 2) {
            parametros.put("intentoasoc", "1");
        }
        parametros.put("consecutivoRUNT", consecutivoRunt);
        //Cargar la informacion del CDA

        parametros.put("NITCDA", this.ctxCDA.getNit());
        parametros.put("NombreCDA", this.ctxCDA.getNombre());
        parametros.put("DireccionCDA", this.ctxCDA.getDireccion());
        parametros.put("TelefonoCDA", this.ctxCDA.getTelefono());
        parametros.put("ciudadCDA", this.ctxCDA.getCiudad());

        parametros.put("nombreResp", this.ctxHojaPrueba.getResponsable().getNombre());
        if (this.ctxHojaPrueba.getVehiculo().getDiseno().name().equalsIgnoreCase("Scooter")) {
            parametros.put("TempGasoCruc", "0");
            parametros.put("TempGasoRal", "0");
        }
    }//end of method cargarInfo

    //Carga las ultimas ids de las pruebas que esten finalizadas
    //Deberia cargar solamente las ultimas ids de las pruebas asi no esten finalizadas????
    /**
     * Metodo que retorna una lista de ids de las ultimas pruebas finalizadas
     *
     * @param cn
     * @param hojaPruebas
     * @return la ultima prueba finalizada
     */
    private List<Prueba> cargarIdPruebasNoDuplicadas(HojaPruebas ctxHojaPrueba, String estado) {
        //ultimas pruebas de cada tipo finalizadas para ahorrar tiempo de consulta

        List<Prueba> ctxListPrueba = new ArrayList();
        if (estado.equalsIgnoreCase("I")) {
            ctxListPrueba = ctxHojaPrueba.getListPruebas();
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
                if (encontrado == false) {
                    if (!ctxpru.getAbortado().equalsIgnoreCase("A") && !ctxpru.getFinalizada().equalsIgnoreCase("x")) {
                        ctxListPrueba.add(ctxpru);
                    }
                }
                encontrado = false;
            }
            for (Prueba prReins : r.getPruebaList()) {
                if (!prReins.getAbortado().equalsIgnoreCase("A") && !prReins.getFinalizada().equalsIgnoreCase("x")) {
                    ctxListPrueba.add(prReins);
                }

            }
        }

        List<Prueba> listaIdsPruebas = new ArrayList();
        for (Prueba pr : ctxListPrueba) {
            if (!pr.getAbortado().equalsIgnoreCase("A") && !pr.getFinalizada().equalsIgnoreCase("x")) {
                listaIdsPruebas.add(pr);
            }
        }
        return listaIdsPruebas;
    }//end of cargarIdPruebasNoDuplicadas

    /**
     * Verifica si el certificado ya fue impreso.. si en efecto entonces pide
     * las razones para sacar el duplicado y llama a imprimirDuplicado Si no ha
     * sido impreso entonces verifica el consecutivo y llama al metodo imprimir
     *
     * @param idHojaPrueba
     * @param cn
     */
    public void imprimirCertificado(long idHojaPrueba, long consecutivoRUNT, Connection cn) {

        try {
            //traer los parametros para el certificado
            Certificado certificado = consultasCertificados.obtenerCertificadoPorHojaPrueba((int) idHojaPrueba, cn);

            if (certificado != null) {//Imprimir duplicado
                int anularCertificado = preguntarAnularCertificado(certificado);
                long consecutivoRUNTAnterior = consultas.consultarCertificadoRUNTAnteriorCertificado(certificado.getId(), cn);
                Date fechaExpedicionAnterior = consultasCertificados.obtenerFechaExpedicionCertificado(certificado.getId(), cn);
                Date fechaVecimientoCertificadoAnteror = consultasCertificados.obtenerFechaVencimientoCertificado(certificado.getId(), cn);
                if (anularCertificado == JOptionPane.YES_OPTION) {

                    int cambiarConsecutivo = preguntarCambiarConsecutivo(certificado);
                    if (cambiarConsecutivo == JOptionPane.YES_OPTION) {
                        boolean cambiarConsecutivoRunt = cambiarConsecutivoRunt(certificado, cn);
                        if (!cambiarConsecutivoRunt) {//Si ocurrio un error en el cambio del consecutivo no proceder mas
                            return;
                        }
                    }//end of if cambiarConsecutivo   
                    // CDAVcali2018
                    ListenerDuplicado listenerDuplicado = new ListenerDuplicado(idHojaPrueba, certificado.getId(), new Timestamp(fechaExpedicionAnterior.getTime()), new Timestamp(fechaVecimientoCertificadoAnteror.getTime()));
                    JOptionPane.showMessageDialog(null, " He Generado un Nuevo Consecutivo Runt de Manera Exitosa ..!");
                    /* JasperPrint fillReport = generarReporte(cn, idHojaPrueba, consecutivoRUNTAnterior, fechaExpedicionAnterior, fechaVecimientoCertificadoAnteror);
                     mostrarCertificadoViewer(fillReport, listenerDuplicado); */
                } else {
                    String motivoAnulacion = UtilDuplicado.preguntarMotivoAnulacion();
                    int consecutivoPreimpreso = UtilDuplicado.preguntarConsecutivo();
                    if (motivoAnulacion != null && !motivoAnulacion.isEmpty() && consecutivoPreimpreso > 0) {
                        cn.setAutoCommit(false);

                        consultasCertificados.anularCertificado(certificado.getId(), motivoAnulacion, cn);
                        long consecutivoRUNTCertificadoAnterior = consultas.consultarCertificadoRUNTAnteriorCertificado(certificado.getId(), cn);
                        consultasCertificados.registrarNuevoCertificado(consecutivoPreimpreso, idHojaPrueba, fechaExpedicionAnterior, fechaVecimientoCertificadoAnteror, consecutivoRUNTCertificadoAnterior, cn);
                        consultas.cerrarRevision(consecutivoRUNTCertificadoAnterior, idHojaPrueba, true, cn);//se supone que la revision ya debe hacer cerrado
                        JasperPrint fillReport = generarReporte(cn, idHojaPrueba, consecutivoRUNTAnterior, fechaExpedicionAnterior, fechaVecimientoCertificadoAnteror);
                        mostrarCertificado_o_DuplicadoPdf(fillReport, (int) idHojaPrueba);
                        cn.commit();
                        cn.setAutoCommit(true);
                        JOptionPane.showMessageDialog(null, " He Generado un Nuevo Certificado de Manera Exitosa ..!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Por Favor Introduzca valores validos para el  motivo de anulacion y/o consecutivo preimpreso");
                    }
                }

            }//fin de imprimir duplicado
            else {// si no es un duplicado
                //Esta hoja de prueba ha sido aprobada desea imprimir certificado.               

            }//end else no es un certificado
        } catch (JRException | SQLException | IOException ex) {
            Mensajes.mostrarExcepcion(ex);
            System.out.println(ex);
            rollbackOperacion(cn);
        }
    }

    /**
     * Genera el jasperPrint de un certificado o duplicado
     *
     * @param cn
     * @param idHojaPrueba
     * @throws JRException
     * @throws SQLException
     * @throws HeadlessException
     */
    private JasperPrint generarReporte(Connection cn, long idHojaPrueba, long consecutivoRUNTCertificados, Date fechaExpedicion, Date fechaVencimiento) throws JRException, SQLException, HeadlessException {
        String nombreReporte = "certificado.jasper";
        Map parametrosCertificado = new HashMap();
        Cda infoCda = consultasCertificados.obtenerInfoCDA(cn);
        ponerInfoCDAParametros(parametrosCertificado, infoCda);
        //poner los parametros para el certificado
        parametrosCertificado.put("ID_HP", (long) idHojaPrueba); //poner la hoja de prueba
        parametrosCertificado.put("ConsecutivoRUNT", consecutivoRUNTCertificados);
        //traer la fecha desde el servidor y ponerla

        //Pasar la fecha de hoy como parametro
        Calendar calHoy = Calendar.getInstance();
        calHoy.setTime(fechaExpedicion);
        ponerInfoFechaExpedicionParametros(calHoy, parametrosCertificado);

        Calendar calVencimiento = Calendar.getInstance();
        calVencimiento.setTime(fechaVencimiento);//le suma un anio a la fecha de expedicion
        Timestamp timeStampVencimiento = ponerParametrosFechaVencimiento(calVencimiento, parametrosCertificado);
        listenerPrimerCertificado = new ListenerPrimerCertificado(idHojaPrueba, new java.sql.Timestamp(fechaExpedicion.getTime()), timeStampVencimiento, consecutivoRUNTCertificados);
        //Ajuste que corresponde a la migracion a maven
        JasperReport certificado = (JasperReport) JRLoader.loadObject(CargarArchivos.cargarArchivo(nombreReporte));
//        JasperPrint fillReport = JasperFillManager.fillReport(nombreReporte, parametrosCertificado, cn);
        JasperPrint fillReport = JasperFillManager.fillReport(certificado, parametrosCertificado, cn);
        return fillReport;
    }

    public void setImprimirPdf(boolean imprimirPdf) {
        this.imprimirPdf = imprimirPdf;
    }

    private void llamarProcedimientoMedidas(Connection cn, long hojaPruebas) {
        try {
            CallableStatement llamarProcedimiento = cn.prepareCall("{call PonerAsteriscosMedidas(?)}");
            llamarProcedimiento.setLong(1, hojaPruebas);
            llamarProcedimiento.execute();
        } catch (SQLException exc) {
            Mensajes.mostrarExcepcion(exc);
        }
    }

    /**
     * Metodo que chequea la dilucion en la prueba de gases.
     *
     */
    private void cargarDilucion(String estado) {
        List<Prueba> ctxListPrueba = null;
        if (estado.equalsIgnoreCase("I")) {
            ctxListPrueba = ctxHojaPrueba.getListPruebas();
        } else {
            Reinspeccion r = ctxHojaPrueba.getReinspeccionList().get(0);
            ctxListPrueba = r.getPruebaList();
        }
        for (Prueba pr : ctxListPrueba) {
            if (pr.getTipoPrueba().getId() == 8 && pr.getFinalizada().equalsIgnoreCase("Y") && !pr.getAbortado().equalsIgnoreCase("A")) {
                if (pr.getComentario() != null && pr.getComentario().equalsIgnoreCase("DILUCION DE MUESTRA")) {
                    parametros.put("dilusion", "X");
                }
            }
        }
    }

    private String cargarComentariosFUR(HojaPruebas ctxHojaPrueba, String estado, List<Prueba> ctxListPrueba) throws SQLException {
        String comentario = "";
        String strConsulta = null;
        String cmLabr = "";
        String infLabr;
        String serial_equipos = "";
        for (Prueba pr : ctxListPrueba) {
            comentario = pr.getObservaciones();
            if (comentario != null) {
                if (pr.getTipoPrueba().getId() == 1 && comentario.length() > 2) {
                    if (cmLabr.length() > 3) {
                        cmLabr = cmLabr.concat("\n Profundidad labrado: ");
                    } else {
                        cmLabr = "Profundidad labrado: ";
                    }
                    String[] lstObs = comentario.split("obs");
                    String[] lstTrama = lstObs[0].split("@@");
                    String[] lstEjes = lstTrama[0].split("&");
                    for (int i = 0; i < lstEjes.length; i++) {
                        infLabr = lstEjes[i].replace("$", "-");
                        cmLabr = cmLabr.concat("Eje" + String.valueOf(i + 1)).concat(" ").concat(infLabr).concat("mm; ");
                    }
                    if (lstTrama.length > 1) {
                        if (ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() != 4 && ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() != 5) {
                            String[] llantaRespuesto = lstTrama[1].split("%");
                            cmLabr = cmLabr.concat("LLanta de Repuesto:" + llantaRespuesto[0]).concat("mm; ");
                            if (llantaRespuesto.length > 1) {
                                cmLabr = cmLabr.concat("LLanta de Repuesto(2):" + llantaRespuesto[1]).concat("mm; ");
                            }
                        }
                    }
                    if (lstObs.length > 1) {
                        cmLabr = cmLabr.concat("\n").concat(" ").concat(lstObs[1]);
                    }
                }
                if (pr.getTipoPrueba().getId() == 2 && comentario.length() > 2) {
                    cmLabr = cmLabr.concat("\n Luces: ").concat(comentario);

                }
                if (pr.getTipoPrueba().getId() == 8 && comentario.length() > 2) {
                    cmLabr = cmLabr.concat("\n Gases: ").concat(comentario);
                }
                if (pr.getTipoPrueba().getId() == 5 && comentario.length() > 2) {
                    cmLabr = cmLabr.concat("\n Frenos: ").concat(comentario);
                }
            }
            //// Incorporar seriales de equipos a las 
            System.out.println("Imprimir SERIALES");
            if (pr.getTipoPrueba().getId() == 1 && pr.getSerialEquipo().length() > 1 && !pr.getSerialEquipo().equalsIgnoreCase("Serial No Encontrado") && pr.getFinalizada() == "Y") {
                serial_equipos = serial_equipos.concat(" Inspeccion Visual: ").concat(pr.getSerialEquipo()).concat("; ");
            }
            if (pr.getTipoPrueba().getId() == 2 && pr.getSerialEquipo().length() > 1 && !pr.getSerialEquipo().equalsIgnoreCase("Serial No Encontrado") && pr.getFinalizada() == "Y") {
                serial_equipos = serial_equipos.concat(" Luces: ").concat(pr.getSerialEquipo()).concat("; ");
            }
            if (pr.getTipoPrueba().getId() == 4 && pr.getSerialEquipo().length() > 1 && !pr.getSerialEquipo().equalsIgnoreCase("Serial No Encontrado") && pr.getFinalizada() == "Y") {
                serial_equipos = serial_equipos.concat(" Desviacion Lateral: ").concat(pr.getSerialEquipo()).concat("; ");
            }
            if (pr.getTipoPrueba().getId() == 5 && pr.getSerialEquipo().length() > 1 && !pr.getSerialEquipo().equalsIgnoreCase("Serial No Encontrado") && pr.getFinalizada() == "Y") {
                serial_equipos = serial_equipos.concat(" Frenometro: ").concat(pr.getSerialEquipo()).concat("; ");
            }
            if (pr.getTipoPrueba().getId() == 6 && pr.getSerialEquipo().length() > 1 && !pr.getSerialEquipo().equalsIgnoreCase("Serial No Encontrado") && pr.getFinalizada() == "Y") {
                serial_equipos = serial_equipos.concat(" Suspension: ").concat(pr.getSerialEquipo()).concat("; ");
            }
            if (pr.getTipoPrueba().getId() == 7 && pr.getSerialEquipo().length() > 1 && !pr.getSerialEquipo().equalsIgnoreCase("Serial No Encontrado") && pr.getFinalizada() == "Y") {
                serial_equipos = serial_equipos.concat(" Ruido: ").concat(pr.getSerialEquipo()).concat("; ");
            }
            if (pr.getTipoPrueba().getId() == 8 && pr.getSerialEquipo().length() > 1 && !pr.getSerialEquipo().equalsIgnoreCase("Serial No Encontrado") && pr.getFinalizada() == "Y") {
                serial_equipos = serial_equipos.concat(" Gases: ").concat(pr.getSerialEquipo()).concat("; ");
            }
            if (pr.getTipoPrueba().getId() == 9 && pr.getSerialEquipo().length() > 1 && !pr.getSerialEquipo().equalsIgnoreCase("Serial No Encontrado") && pr.getFinalizada() == "Y") {
                serial_equipos = serial_equipos.concat(" Taximetro: ").concat(pr.getSerialEquipo()).concat("; ");
            }
        }
        System.out.println(serial_equipos);
        String sb = ModeloTablaImpresionFecha.organizarMayores(ctxHojaPrueba.getId(), reinspeccion);

        cmLabr = cmLabr.concat("\n SERIALES EQUIPOS: ").concat(serial_equipos);

//        String defectos = "select Nombre_problema,d.Tipo_defecto,Nombre_grupo,d.CARDEFAULT from pruebas as pr inner join defxprueba as dp on pr.id_pruebas=dp.id_prueba inner join defectos as d on dp.id_defecto = d.CARDEFAULT inner join grupos as g on d.DEFGROUP = g.DEFGROUP where pr.id_pruebas in (select max(id_pruebas) from pruebas as p where p.hoja_pruebas_for = ?  and p.Tipo_prueba_for != 1  and p.Finalizada = 'Y' group by p.Tipo_prueba_for ) and pr.hoja_pruebas_for = ? and pr.Tipo_prueba_for != 1 and pr.Finalizada = 'Y' and d.CARDEFAULT not in (80000)";
//        String comentarioAborto = "select p.Comentario_aborto from pruebas p where p.hoja_pruebas_for = ? and p.Abortada = 'Y'";
//
//        ps = cn.prepareStatement(comentarioAborto);
//        ps.setLong(1, hojaPruebas);
////        ps.setLong(2, hojaPruebas);
//        rs = ps.executeQuery();
//        StringBuilder sb = new StringBuilder(comentario == null ? "" : comentario);
//
//        while (rs.next()) {
//            sb.append(rs.getString(1));
//            sb.append(", ");
//        }        
        return cmLabr;
    }//end method

    private String cargarNumeroIntento(long hojaPruebas, Connection cn) throws SQLException {

        String strNumeroIntento = "";
        String strConsulta = "SELECT Numero_intentos FROM hoja_pruebas where TESTSHEET = ?";
        PreparedStatement ps = cn.prepareStatement(strConsulta);

        ps.setLong(1, hojaPruebas);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            strNumeroIntento = rs.getString(1);
        }
        return strNumeroIntento;
    }

    private String cargarConsecutivoRunt(long hojaPruebas, Connection cn) throws SQLException {

        String strConsecutivoRunt = "";
        String strConsulta = "SELECT consecutivo_runt FROM hoja_pruebas where TESTSHEET = ?";
        PreparedStatement ps = cn.prepareStatement(strConsulta);

        ps.setLong(1, hojaPruebas);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            strConsecutivoRunt = rs.getString(1);
        }
        return strConsecutivoRunt;
    }

    private int preguntarAnularCertificado(Certificado certificado) throws HeadlessException {
        //Duplicado
        //Preguntar si se desea anular el certificado
        int seleccion = JOptionPane.showOptionDialog(null, "El certificado ya existe bajo el consecutivo PREIMPRESO:" + certificado.getConsecutivo() + "\n"
                + "consecutivo RUNT:" + certificado.getConsecutivoRunt() + "Que Operacion desea hacer  Ã‚Â¿Cambiar Nro Runt o ANULARLO ? ",
                "Seleccione una opcion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        return seleccion;
    }

    private int preguntarCambiarConsecutivo(Certificado certificado) throws HeadlessException {
        int opcion = JOptionPane.showOptionDialog(null, "Desea cambiar el CONSECUTIVO RUNT de este certificado: " + certificado.getConsecutivoRunt(),
                "Seleccione una opcion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        return opcion;
    }

    private boolean cambiarConsecutivoRunt(Certificado certificado, Connection cn) throws SQLException {

        String strNuevoConsecutivoRUNT = JOptionPane.showInputDialog("Digite nuevoConsecutivo RUNT");
        long nuevoConsecutivoRUNT;
        try {
            nuevoConsecutivoRUNT = Long.parseLong(strNuevoConsecutivoRUNT);//ingresa un consecutivo nuevo
        } catch (NumberFormatException ne) {
            Mensajes.mensajeAdvertencia("Error ha digitado un consecutivo invalido");
            return false;
        }//end of try catch

        if (nuevoConsecutivoRUNT <= 0) {
            return false;
        }

        consultas = new Consultas();
        consultas.actualizarConsecuivoRUNTdeCertificado(certificado.getId(), nuevoConsecutivoRUNT, cn);
        consultas.actualizarConsecutivoRUNTCertificados(nuevoConsecutivoRUNT, cn);

        return true;
    }

    /**
     * Obtiene la fecha actual del servidor mediante una funcion de sql del
     * servidor
     *
     * @param cn
     * @return la fecha del servidor en el momento de la invocacion
     */
    private Date obtenerFechaHoy(Connection cn) throws SQLException {

        String strFecha = "SELECT NOW()"; //insertar el certificado y el consecutivo
        PreparedStatement consultaFecha = cn.prepareStatement(strFecha);
        ResultSet rsFecha = consultaFecha.executeQuery();
        rsFecha.next();
        java.sql.Timestamp mysqlTimestamp = rsFecha.getTimestamp(1);
        Date fechaHoy = new Date(mysqlTimestamp.getTime());

        return fechaHoy;
    }

    private void ponerInfoCDAParametros(Map parametrosCertificado, Cda infoCda) {
        //poner informacion dle cda en los paramentros
        parametrosCertificado.put("NombreCDA", infoCda.getNombre());
        parametrosCertificado.put("NITCDA", infoCda.getNit());
        parametrosCertificado.put("Responsable", infoCda.getNombreResponsable());//Bug, pero debido a que el Runt lo deja en blanco no parece importante
        parametrosCertificado.put("CertificadosConformidad", infoCda.getCertificado());
    }

    private void ponerInfoFechaExpedicionParametros(Calendar calHoy, Map parametrosCertificado) {
        //Sacar el ano el mes y el dia en Cadenas
        int anoFechaExpedicion = calHoy.get(Calendar.YEAR);
        int mesFechaExpedicion = calHoy.get(Calendar.MONTH) + 1; //sumarle uno al mes cuestion de presentacion
        int diaFechaExpedicion = calHoy.get(Calendar.DAY_OF_MONTH);
        //Pasar esos parametros
        parametrosCertificado.put("AÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â±oExpedicion", String.valueOf(anoFechaExpedicion));
        parametrosCertificado.put("MesExpedicion", String.valueOf(mesFechaExpedicion));
        parametrosCertificado.put("DiaExpedicion", String.valueOf(diaFechaExpedicion));
    }

    private Timestamp ponerParametrosFechaVencimiento(Calendar calHoy, Map parametrosCertificado) {

        int anoFechaVencimiento = calHoy.get(Calendar.YEAR);
        int mesFechaVencimiento = calHoy.get(Calendar.MONTH) + 1; //sumarle uno al mes
        int diaFechaVencimiento = calHoy.get(Calendar.DAY_OF_MONTH);
        java.sql.Timestamp timeStampVencimiento = new java.sql.Timestamp(calHoy.getTimeInMillis());
        parametrosCertificado.put("AÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â±oVencimiento", String.valueOf(anoFechaVencimiento));
        parametrosCertificado.put("MesVencimiento", String.valueOf(mesFechaVencimiento));
        parametrosCertificado.put("DiaVencimiento", String.valueOf(diaFechaVencimiento));
        return timeStampVencimiento;

    }

    private void mostrarCertificadoViewer(JasperPrint fillReport, ActionListener listener) throws HeadlessException, JRException {
        JFrame app = new JFrame("Certificado");
        JRViewerModificado jvModificado = new JRViewerModificado(fillReport);
        jvModificado.setListenerImprimir(listener);
        app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        app.setExtendedState(JFrame.MAXIMIZED_BOTH);
        app.setContentPane(jvModificado);
        app.setVisible(true);
    }

    public long preguntarConsecutivoPreimpreso(Connection cn) throws SQLException {
        long consecutivoPreimpreso = consultas.obtenerSiguienteConsecutivoPreimpreso(cn);

        int opcion = JOptionPane.showOptionDialog(null, "El siguiente consecutivo es correcto? " + (++consecutivoPreimpreso), "Seleccione una opcion",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (opcion == JOptionPane.NO_OPTION) {
            String strConsecutivoPreimpreso = JOptionPane.showInputDialog("Digite Consecutivo");
            long longConsecutivoPreimpreso;
            try {
                longConsecutivoPreimpreso = Long.parseLong(strConsecutivoPreimpreso);
                consecutivoPreimpreso = longConsecutivoPreimpreso;
            } catch (NumberFormatException ne) {
                Mensajes.mensajeError("Error ha digitado un consecutivo invalido");
                System.exit(1);//no deja continuar
            }
        }

        return consecutivoPreimpreso;
    }

    public void mostrarCertificado_o_DuplicadoPdf(JasperPrint fillReport, int idHojaPrueba) throws JRException, IOException {
        /*String destFileNamePdf = "C:\\Certificados\\certificado" + idHojaPrueba + ".pdf";
         JasperExportManager.exportReportToPdfFile(fillReport, destFileNamePdf);

         File path = new File(destFileNamePdf);
         Desktop.getDesktop().open(path);*/
    }

    private void imprimir(Connection cn, long idHojaPrueba) throws JRException, SQLException, HeadlessException {

        //Esta hoja de prueba ha sido aprobada desea imprimir certificado.
        int seleccion = JOptionPane.showOptionDialog(null, "Esta hoja de prueba ha sido aprobada ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¿DESEA IMPRIMIR CERTIFICADO?",
                "Seleccione una opcion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (seleccion == JOptionPane.NO_OPTION) {
            return;
        }

        consultas = new Consultas();
        long consecutivoRUNTCertificados;

        //traer el siguiente consecutivo que sugiere el sistema
        consecutivoRUNTCertificados = consultas.consultarConsecutivoRUNTCertificados(cn);
        int opcion = JOptionPane.showOptionDialog(null, "El consecutivo  RUNT para certificados sugerido es " + (consecutivoRUNTCertificados + 1) + " ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¿Desea digitar uno distinto?", "Seleccione una opcion",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (opcion == JOptionPane.YES_OPTION) {//digitar otro consecutivo distinto al sugerido
            String strConsecutivo = JOptionPane.showInputDialog("Digite Consecutivo");
            try {
                consecutivoRUNTCertificados = Long.parseLong(strConsecutivo);
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(null, "Error ha digitado un consecutivo invalido");
                return;
            }
        } else if (opcion == JOptionPane.NO_OPTION) {
            consecutivoRUNTCertificados++;
        }

        String nombreReporte = "certificado.jasper";
        Map parametrosCertificado = new HashMap();
        String str = "SELECT * FROM cda WHERE id_cda = 1";
        PreparedStatement consultaInfoCDA = cn.prepareStatement(str);
        ResultSet rsInfoCDA = consultaInfoCDA.executeQuery();
        rsInfoCDA.next();
        parametrosCertificado.put("NombreCDA", rsInfoCDA.getString("nombre"));
        parametrosCertificado.put("NITCDA", rsInfoCDA.getString("NIT"));
        parametrosCertificado.put("Responsable", rsInfoCDA.getString("nom_resp_certificados"));
        parametrosCertificado.put("CertificadosConformidad", rsInfoCDA.getString("Certificado_conformidad"));
        //poner los parametros para el certificado
        parametrosCertificado.put("ID_HP", (long) idHojaPrueba); //poner la hoja de prueba
        parametrosCertificado.put("ConsecutivoRUNT", consecutivoRUNTCertificados);
        //traer la fecha desde el servidor y ponerla
        String strFecha = "SELECT NOW()"; //insertar el certificado y el consecutivo
        PreparedStatement consultaFecha = cn.prepareStatement(strFecha);
        ResultSet rsFecha = consultaFecha.executeQuery();
        rsFecha.next();
        java.sql.Timestamp mysqlTimestamp = rsFecha.getTimestamp(1);
        Date fechaHoy = new Date(mysqlTimestamp.getTime());
        //Pasar la fecha de hoy como parametro
        Calendar calHoy = Calendar.getInstance();
        calHoy.setTime(fechaHoy);
        //Sacar el ano el mes y el dia en Cadenas
        int anoFechaExpedicion = calHoy.get(Calendar.YEAR);
        int mesFechaExpedicion = calHoy.get(Calendar.MONTH) + 1; //sumarle uno al mes cuestion de presentacion
        int diaFechaExpedicion = calHoy.get(Calendar.DAY_OF_MONTH);
        //Pasar esos parametros
        parametrosCertificado.put("AÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â±oExpedicion", String.valueOf(anoFechaExpedicion));
        parametrosCertificado.put("MesExpedicion", String.valueOf(mesFechaExpedicion));
        parametrosCertificado.put("DiaExpedicion", String.valueOf(diaFechaExpedicion));
        //dos anos
        String strFechaMatricula = "Select v.Fecha_registro,v.SERVICE,v.CARTYPE,v.SPSERVICE from vehiculos as v inner join hoja_pruebas as hp "
                + "on v.CAR = hp.Vehiculo_for and hp.TESTSHEET = ?";
        PreparedStatement psFechaMatricula = cn.prepareStatement(strFechaMatricula);
        psFechaMatricula.setLong(1, idHojaPrueba);
        ResultSet rsFechaMatricula = psFechaMatricula.executeQuery();
        rsFechaMatricula.next();
        Date dateFechaMatricula = rsFechaMatricula.getDate(1);
        int servicio = rsFechaMatricula.getInt(2);//obtener el servicio
        int tipoVehiculo = rsFechaMatricula.getInt(3);//obtener el tipo de Vehiculo
        int servicioEspecial = rsFechaMatricula.getInt(4);//obtener el servicio especial
        DateFormat df = DateFormat.getDateInstance();
        System.out.println(df.format(dateFechaMatricula));
        //Calendar de la fecha de matricula
        Calendar cFechaRegistro = Calendar.getInstance();
        cFechaRegistro.setTime(dateFechaMatricula);
        //Fecha de Hoy - 6 anos mayor a fecha de Registro
        Calendar cHoyMenosSeis = Calendar.getInstance();
        cHoyMenosSeis.setTime(fechaHoy);
        cHoyMenosSeis.add(Calendar.YEAR, -6);

        System.out.println(df.format(cHoyMenosSeis.getTime()));

        //Comparacion
        boolean imprimirADosAnos = cFechaRegistro.compareTo(cHoyMenosSeis) >= 0;

        if (servicio == 3 && imprimirADosAnos && (tipoVehiculo != 4) && servicioEspecial == 1) {//Que no sea moto que sea particular y no servicio especial
            calHoy.add(Calendar.YEAR, 1);
        } else {
            calHoy.add(Calendar.YEAR, 1);
        }
        //calHoy.add(Calendar.YEAR,1);        //no es calHoy es calFechaVencimiento
        int anoFechaVencimiento = calHoy.get(Calendar.YEAR);
        int mesFechaVencimiento = calHoy.get(Calendar.MONTH) + 1; //sumarle uno al mes
        int diaFechaVencimiento = calHoy.get(Calendar.DAY_OF_MONTH);
        java.sql.Timestamp timeStampVencimiento = new java.sql.Timestamp(calHoy.getTimeInMillis());
        parametrosCertificado.put("AÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â±oVencimiento", String.valueOf(anoFechaVencimiento));
        parametrosCertificado.put("MesVencimiento", String.valueOf(mesFechaVencimiento));
        parametrosCertificado.put("DiaVencimiento", String.valueOf(diaFechaVencimiento));

        listenerPrimerCertificado = new ListenerPrimerCertificado(idHojaPrueba, mysqlTimestamp, timeStampVencimiento, consecutivoRUNTCertificados);
        JasperReport certificado = (JasperReport) JRLoader.loadObject(CargarArchivos.cargarArchivo(nombreReporte));
        JasperPrint fillReport = JasperFillManager.fillReport(certificado, parametrosCertificado, cn);
//        JasperPrint fillReport = JasperFillManager.fillReport(nombreReporte, parametrosCertificado, cn);

        if (!imprimirPdf) {
            JFrame app = new JFrame("Certificado");
            JRViewerModificado jvModificado = new JRViewerModificado(fillReport);
            jvModificado.setListenerImprimir(listenerPrimerCertificado);
            app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            app.setExtendedState(JFrame.MAXIMIZED_BOTH);
            app.setContentPane(jvModificado);
            app.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(null, "No se permite imprimir en pdf el CERTIFICADO");
        }
    }
//end of method imprimir

    private void rollbackOperacion(Connection cn) {
        try {
            cn.rollback();

        } catch (SQLException ex1) {
            Logger.getLogger(LlamarReporte3625.class
                    .getName()).log(Level.SEVERE, null, ex1);
        }
    }

    private void cargarImagen(boolean reinspeccion, Map parametros, int numeroImagen, boolean flag) {
        SepararImagenes separarImagenes = new SepararImagenes();
        separarImagenes.obtenerImagen(numeroImagen, this.ctxHojaPrueba, reinspeccion, flag);
        parametros.put("Foto1", separarImagenes.getFoto1());
        parametros.put("Foto2", separarImagenes.getFoto2());
    }
}

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

import com.soltelec.consolaentrada.indra.encript.Encript;
import com.soltelec.consolaentrada.models.controllers.HojaPruebasJpaController;
import com.soltelec.consolaentrada.models.entities.AuditoriaSicov;
import com.soltelec.consolaentrada.models.entities.ResponseDTO;
import com.soltelec.consolaentrada.reporte.ListenerEnvioFUR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.List;
import javax.xml.rpc.ServiceException;

/**
 *
 * @author GerenciaDesarrollo
 */
public class ClienteIndra {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ClienteIndra.class);

    private String urlSicov;
    private String urlSicov2;
    private String urlSicovEncript;

    private boolean test;
    private boolean testerror;

    public ClienteIndra() {

    }

    public ResponseDTO setEvento(String trama) {
        try {
            SicovSoap_PortType sicovSoap = new SicovLocator(urlSicov, urlSicov2).getSicovSoap();
            logger.info("Trama a encriptar: " + trama);
            String tramaEncript = Encript.encript(trama, urlSicovEncript);
            logger.info("Trama encriptada: " + tramaEncript);
            EventosRespuesta fURRespuesta = sicovSoap.enviarEventosSicov(tramaEncript);
            return new ResponseDTO(String.valueOf(fURRespuesta.getCodRespuesta()), fURRespuesta.getMsjRespuesta());
        } catch (ServiceException | RemoteException e) {
            return null;
            //throw new SartComunicadorException(500, e.getMessage());
        }
    }

    public ResponseDTO finalizaFur(EnviarRuntSicov trama) {
        try {
            SicovSoap_PortType sicovSoap = new SicovLocator(urlSicov, urlSicov2).getSicovSoap();
            FURRespuesta fURRespuesta = sicovSoap.enviarRuntSicov(
                    trama.getNombreEmpleado(),
                    trama.getNumeroIdentificacion(),
                    trama.getPlaca(),
                    trama.getExtranjero(),
                    trama.getConsecutivoRUNT(),
                    trama.getIdRunt(),
                    trama.getDireccionIpEquipo()
            );
            System.out.println("trama que le envio a sicov en el segundo fur:"+ trama.getNombreEmpleado()+" "+ trama.getNumeroIdentificacion()+
                    " "+trama.getPlaca()+
                    " "+trama.getExtranjero()+
                    " "+trama.getConsecutivoRUNT()+
                    " "+trama.getIdRunt()+
                    " "+trama.getDireccionIpEquipo() );
            
            return new ResponseDTO(String.valueOf(fURRespuesta.getCodRespuesta()), fURRespuesta.getMsjRespuesta());
        } catch (ServiceException | RemoteException e) {
            return null;
            //throw new SartComunicadorException(500, e.getMessage());
        }
    }

    public ResponseDTO setFur(String trama) {
        try {
            SicovSoap_PortType sicovSoap = new SicovLocator(urlSicov, urlSicov2).getSicovSoap();
            System.out.println("Invocando servicio de Encriptacion");
            File file = new File("C:\\opt\\sst\\TramaCruda.txt");
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
            } catch (IOException ex) {
                System.out.println("Error creando el archivo");
                ex.printStackTrace(System.err);
            }
            PrintWriter output = null;
            try {
                //MODIFICACION SI EL KILOMETRAJE ES "FUNCIONAL" EN LA TRAMA ENVIA UN ESPACIO VACIO ""
                //JFM-------------------------------------------------------------------------------------------------------------------------------------
                //System.out.println("TRAMA ANTES DE REEMPLAZAR KILOMETRAJE : " + trama);
                trama=trama.replace("NO FUNCIONAL", "");
                //System.out.println("TRAMA DESPUES  DE REEMPLAZAR KILOMETRAJE : " + trama);
                //JFM-------------------------------------------------------------------------------------------------------------------------------------                
//Generar el archivo txt de la entrada, la entrada corregida y la salida          
                output = new PrintWriter(file);
                output.println("-.** Trama Enviada .-** ");
                output.println(" ");
                output.println("-.- " + trama);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception exc) {
                exc.printStackTrace();
            } finally {
                output.close();
            }
            System.out.println("URL ENVIO ENCRIPCION " + urlSicovEncript);
            // trama ="858|2017-02-27 13:00:00|Gases|MAL797|5609Rt17|1|";
            // JOptionPane.showMessageDialog(null, "Voy a Aplicar Servicio de Encriptacion  .!");
            
            String tramaEncript = Encript.encript(trama, urlSicovEncript);
            System.out.println("Se Inf que se ha Encriptado la Trama");
            logger.info("Trama encriptada: " + trama);
            file = new File("C:\\opt\\sst\\TramaEnvioEncript.txt");
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
            } catch (IOException ex) {
                System.out.println("Error creando el archivo");
                ex.printStackTrace(System.err);
            }
            output = null;
            try {
                //Generar el archivo txt de la entrada, la entrada corregida y la salida          
                output = new PrintWriter(file);
                output.println("-.** Trama Encriptada .-** ");
                output.println(" ");
                output.println("Trama Cruda: " + tramaEncript);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception exc) {
                exc.printStackTrace();
            } finally {
                output.close();
            }

            FURRespuesta fURRespuesta = sicovSoap.enviarFurSicov(tramaEncript);
            return new ResponseDTO(String.valueOf(fURRespuesta.getCodRespuesta()), fURRespuesta.getMsjRespuesta());
        } catch (ServiceException | RemoteException e) {
            return null;
            //throw new SartComunicadorException(500, e.getMessage());
        }
    }

    public String getUrlSicov() {
        return urlSicov;
    }

    public void setUrlSicov(String urlSicov) {
        this.urlSicov = urlSicov;
    }

    public String getUrlSicov2() {
        return urlSicov2;
    }

    public void setUrlSicov2(String urlSicov2) {
        this.urlSicov2 = urlSicov2;
    }

    public String getUrlSicovEncript() {
        return urlSicovEncript;
    }

    public void setUrlSicovEncript(String urlSicovEncript) {
        this.urlSicovEncript = urlSicovEncript;

    }

    public static void main(String[] args)
    {
        HojaPruebasJpaController hpControler = new HojaPruebasJpaController();
         List<AuditoriaSicov> lstTramas = hpControler.recogerTramasExist(hpControler.find(228775));
         ListenerEnvioFUR listenerEnvioFUR = new ListenerEnvioFUR(228775L, lstTramas);
         listenerEnvioFUR.actionPerformed(null);
        
//        EnviarRuntSicov enviarRuntSicov = new EnviarRuntSicov();
//        enviarRuntSicov.setConsecutivoRUNT("123123123");
//        enviarRuntSicov.setDireccionIpEquipo("192.168.1.3");
//        enviarRuntSicov.setExtranjero("N");
//        enviarRuntSicov.setIdRunt("10754857");
//        enviarRuntSicov.setNombreEmpleado("GUSTAVO MURILLO");
//        enviarRuntSicov.setNumeroIdentificacion("10229298");
//        
//        ClienteIndra ci = new ClienteIndra();
//        ci.urlSicov = "http://172.17.4.130:8056/sicov.asmx?WSDL";
//        ci.urlSicov2 = "http://190.25.205.154:809?/sicov.asmx";
//        enviarRuntSicov.setPlaca("ZNK933");
//        System.out.println(ci.finalizaFur(enviarRuntSicov));
//        enviarRuntSicov.setPlaca("ZNK933");
//        System.out.println(ci.finalizaFur(enviarRuntSicov));
//        enviarRuntSicov.setPlaca("HYK473");
//        System.out.println(ci.finalizaFur(enviarRuntSicov));
    }
}

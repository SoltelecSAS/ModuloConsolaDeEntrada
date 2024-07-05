/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.EventosSicovJpaController;
import com.soltelec.consolaentrada.models.entities.EventosSicov;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author SOLTELEC
 */
public class EventosDao {

    private EventosSicovJpaController ejc = new EventosSicovJpaController();
    private EventosSicov evento;
    private String mensaje = "";

    public String InsertarEvento(String Nick, String placa, HojaPruebas hojaPrueba) throws Exception {
        try {
            java.util.Date fecha = new Date();
            evento = new EventosSicov();
            evento.setId(Integer.BYTES);
            evento.setMensajeWs("Envio 2do FUR, Vehiculo: " + placa + ", Por el usuario: " + Nick);
            evento.setCodigoWs("1");
            evento.setNombreEvento("ENVIO_FUR");
            evento.setEstado("Completado");
            evento.setFecha(fecha);
            evento.setPlaca(placa);
            evento.setIdPrueba(2);
            evento.setIdHojaPrueba(hojaPrueba);
            System.out.println("evento.toString() " + evento.toString());
            ejc.create(evento);
            mensaje = "se inserto el evento sicov con exito";

            return mensaje;
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            mensaje = "error al insertar el evento sicov: " + ex.getMessage();
            System.out.println("error---- " + ex.getMessage() + ", " + ex.toString());

            return mensaje;
        }
    }

}

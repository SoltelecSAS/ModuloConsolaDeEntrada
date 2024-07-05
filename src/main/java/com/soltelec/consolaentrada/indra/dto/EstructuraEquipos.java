/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.indra.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author usurio
 */
public class EstructuraEquipos {

    private JsonEquipos equipos;

    public void setEquipos(JsonEquipos equipos) {
        this.equipos = equipos;
    }

    public JsonEquipos getEquipos() {
        return this.equipos;
    }
    
    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(equipos);
        } catch (JsonProcessingException ex) {
            return "";
        }
    }
}

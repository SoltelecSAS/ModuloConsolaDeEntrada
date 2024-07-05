/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.indra.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;


/**
 *
 * @author usurio
 */
public class JsonEquipos {
    
    private List<DatosEquipos> equipos;

    /**
     * @return the equipos
     */
    public List<DatosEquipos> getEquipos() {
        return equipos;
    }

    /**
     * @param equipos the equipos to set
     */
    public void setEquipos(List<DatosEquipos> equipos) {
        this.equipos = equipos;
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

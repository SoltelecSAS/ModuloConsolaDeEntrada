/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.entities.Prueba;
import com.soltelec.consolaentrada.utilities.UtilConexion;

import java.sql.PreparedStatement;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
public class ReinspByPruebaController {
    
    public void createByList(List<Prueba> pruebas, int idReinspeccion) throws Exception {
        String strInsercion = "INSERT INTO reinspxprueba(id_reinspeccion, id_prueba_for) VALUES (?,?)";
        
        PreparedStatement ps = UtilConexion.obtenerConexion().prepareStatement(strInsercion);
        for(Prueba prueba: pruebas){
            ps.clearParameters();
            ps.setInt(1,idReinspeccion);
            ps.setInt(2, prueba.getId());
            ps.executeUpdate();
        }  
    }
}

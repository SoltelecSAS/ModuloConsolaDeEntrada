/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.controllers.conexion;

import com.soltelec.consolaentrada.configuration.Conexion;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.HashMap;

/**
 *
 * @author GerenciaDesarrollo
 */
public class PersistenceController {

    protected static EntityManager em;

    public static EntityManager getEntityManager() {      
       
        if (Conexion.getBaseDatos()==null) {
            HashMap map = new HashMap();
            Conexion conexion = Conexion.getInstance();
                map.put("javax.persistence.jdbc.url", Conexion.getUrl());
            map.put("javax.persistence.jdbc.user", Conexion.getUsuario());
            map.put("javax.persistence.jdbc.password", Conexion.getContrasena());
            em = Persistence.createEntityManagerFactory("ConsolaEntradaPU", map).createEntityManager();
        }
        System.out.println(Conexion.getUrl());
        return em;
    }
     public static void closeEntityManager() {  
         em.close();
     }
}


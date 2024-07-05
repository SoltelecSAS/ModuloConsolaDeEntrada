/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.entities.Equipo;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author GerenciaDesarrollo
 */
public class EquiposJpaController1 implements Serializable {

    private EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public Equipo buscarPorSerial(String serial) 
    {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNativeQuery("Select * from equipos e WHERE e.serialresolucion = ? ", Equipo.class);
            q.setParameter(1, serial);
            try {
                return (Equipo) q.getSingleResult();
            } catch (Exception e) {
                System.out.println("Error al buscarSerial()");
                return null;
            }
        } finally {

        }
    }
    
    public Equipo buscarSerialResolucionAmbental(String serial) 
    {
        System.out.println("---------------------------------------------------");
        System.out.println("---Buscar Serial ResolucionAmbental    ------------");
        System.out.println("---------------------------------------------------");
        
        System.out.println("Select * from equipos e where e.resolucionambiental = " + serial);
        
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNativeQuery("Select * from equipos e where e.resolucionambiental = ? ", Equipo.class);
            q.setParameter(1, serial.trim());
            try {
                return (Equipo) q.getSingleResult();
            } catch (Exception e) {
                Logger.getLogger(EquiposJpaController1.class.getName()).log(Level.SEVERE, null,e);
                return null;
            }
        } finally {

        }
    }
    
    public Equipo find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Equipo.class, id);
        } finally {

        }
    }
}

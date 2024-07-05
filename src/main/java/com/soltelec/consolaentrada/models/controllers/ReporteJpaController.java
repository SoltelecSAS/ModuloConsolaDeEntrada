/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.Prueba;

import javax.persistence.EntityManager;

/**
 *
 * @author GerenciaDesarrollo
 */
public class ReporteJpaController {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

   public Prueba findPruebas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prueba.class, id);
        } finally {
           
        }
   }

   public HojaPruebas findHojaPruebas(Integer id) {
        getEntityManager().getEntityManagerFactory().getCache().evict(HojaPruebas.class);
        getEntityManager().getEntityManagerFactory().getCache().evict(Prueba.class);
        EntityManager em = getEntityManager();
        try {
            return em.find(HojaPruebas.class, id);
        } finally {
           
        }
    }

}

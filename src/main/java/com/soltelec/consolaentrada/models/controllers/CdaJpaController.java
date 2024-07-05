/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.entities.Cda;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.Serializable;

/**
 *
 * @author  GerenciaDesarrollo
 */
public class CdaJpaController implements Serializable {

    private EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(Cda cda) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cda);
            em.getTransaction().commit();
        } finally {
           
        }
    }

    public void edit(Cda cda) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            em.merge(cda);
                      
            System.out.println("--"+cda.getId()+"--");
            em.getTransaction().commit();
       
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cda.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The cda with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cda cda;
            try {
                cda = em.getReference(Cda.class, id);
                cda.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cda with id " + id + " no longer exists.", enfe);
            }
            em.remove(cda);
            em.getTransaction().commit();
        } finally {
           
        }
    }

    public Cda find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cda.class, id);
        } finally {
          
        }
    }
}

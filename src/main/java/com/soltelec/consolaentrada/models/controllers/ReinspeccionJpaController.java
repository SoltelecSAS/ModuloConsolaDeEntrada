/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.entities.Reinspeccion;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author User
 */
public class ReinspeccionJpaController implements Serializable {
    
    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }
    public void create(Reinspeccion reinspecciones) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(reinspecciones);
            em.getTransaction().commit();
        } finally {
           
        }
    }

    public void addReispXPrueba(Reinspeccion reinspecciones)  {
       
    }
    
    public void edit(Reinspeccion reinspecciones) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            reinspecciones = em.merge(reinspecciones);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = reinspecciones.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The reinspecciones with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reinspeccion reinspecciones;
            try {
                reinspecciones = em.getReference(Reinspeccion.class, id);
                reinspecciones.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reinspecciones with id " + id + " no longer exists.", enfe);
            }
            em.remove(reinspecciones);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public Reinspeccion find(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reinspeccion.class, id);
        } finally {
            
        }
    }
    
    public Reinspeccion findReinspeccionByHoja(int idHojaPruebas) {
        EntityManager em = getEntityManager();
        TypedQuery<Reinspeccion> query = em.createQuery("SELECT r FROM Reinspeccion r WHERE r.hojaPruebas.id = " + idHojaPruebas + " ORDER BY r.id DESC", Reinspeccion.class);
        query.setMaxResults(1);
        try {
            return query.setMaxResults(1).getSingleResult();
        } catch(NoResultException ex) {
            return null;
        } finally {
            
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.entities.Tipocarroceria;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
public class CarroceriaJpaController1 {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(Tipocarroceria carroceria) {
       
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            em.persist(carroceria);
            
            em.getTransaction().commit();
        } finally {
           
        }
    }

    public void edit(Tipocarroceria carroceria) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipocarroceria persistentCarroceria = em.find(Tipocarroceria.class, carroceria.getId());
            
            
            carroceria = em.merge(carroceria);
            
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carroceria.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The carroceria with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
           
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipocarroceria carroceria;
            try {
                carroceria = em.getReference(Tipocarroceria.class, id);
                carroceria.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carroceria with id " + id + " no longer exists.", enfe);
            }
            em.remove(carroceria);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<Tipocarroceria> findAll() {
        return findCarroceriaEntities(true, -1, -1);
    }

    public List<Tipocarroceria> findCarroceriaEntities(int maxResults, int firstResult) {
        return findCarroceriaEntities(false, maxResults, firstResult);
    }

    private List<Tipocarroceria> findCarroceriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT tc FROM Tipocarroceria tc ORDER BY tc.nombre ASC");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
        }
    }
    
    public List<Tipocarroceria> findCarroceriaxTXT(String like) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT tc FROM Tipocarroceria tc where tc.nombre LIKE :txtCarroceria ORDER BY tc.nombre ASC");
            like =like.concat("%");
            q.setParameter("txtCarroceria", like);           
            return q.getResultList();
        } finally {
           
        }
    }

    public Tipocarroceria find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipocarroceria.class, id);
        } finally {
           
        }
    }
}

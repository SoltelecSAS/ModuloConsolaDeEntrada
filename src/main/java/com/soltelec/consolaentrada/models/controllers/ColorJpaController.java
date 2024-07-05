/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.entities.Color;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
public class ColorJpaController {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(Color colores) {
       
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            em.persist(colores);
            
            em.getTransaction().commit();
        } finally {
           
        }
    }

    public void edit(Color colores) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Color persistentColores = em.find(Color.class, colores.getId());
            
            
            colores = em.merge(colores);
            
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = colores.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The colores with id " + id + " no longer exists.");
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
            Color colores;
            try {
                colores = em.getReference(Color.class, id);
                colores.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The colores with id " + id + " no longer exists.", enfe);
            }
            em.remove(colores);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<Color> findAll() {
        return findColoresEntities(true, -1, -1);
    }

    public List<Color> findColoresEntities(int maxResults, int firstResult) {
        return findColoresEntities(false, maxResults, firstResult);
    }

    private List<Color> findColoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT c FROM Color c ORDER BY c.nombre ASC");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
        }
    }
    
    public List<Color> findColoresxTXT(String like) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT c FROM Color c where c.nombre LIKE :txtColor ORDER BY c.nombre ASC");
            like =like.concat("%");
            q.setParameter("txtColor", like);           
            return q.getResultList();
        } finally {
           
        }
    }

    public Color find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Color.class, id);
        } finally {
           
        }
    }
}

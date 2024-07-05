/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.entities.Ciudad;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class CiudadJpaController {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }
    public void create(Ciudad ciudades) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ciudades);
            em.getTransaction().commit();
        } finally {
           
        }
    }

    public void edit(Ciudad ciudades) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ciudades = em.merge(ciudades);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = ciudades.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The ciudades with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad ciudades;
            try {
                ciudades = em.getReference(Ciudad.class, id);
                ciudades.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ciudades with id " + id + " no longer exists.", enfe);
            }
            em.remove(ciudades);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<Ciudad> findAll() {
        return findCiudadesEntities(true, -1, -1);
    }

    public List<Ciudad> findCiudadesEntities(int maxResults, int firstResult) {
        return findCiudadesEntities(false, maxResults, firstResult);
    }

    private List<Ciudad> findCiudadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciudad.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            
        }
    }

    public Ciudad find(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciudad.class, id);
        } finally {
           
        }
    }

}

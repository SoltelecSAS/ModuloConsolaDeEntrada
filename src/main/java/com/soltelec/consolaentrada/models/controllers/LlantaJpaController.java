/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.Llanta;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
public class LlantaJpaController {

   public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(Llanta llantas) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(llantas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(llantas.getId()) != null) {
                throw new PreexistingEntityException("Llantas " + llantas + " already exists.", ex);
            }
            throw ex;
        } finally {
           
        }
    }

    public void edit(Llanta llantas) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            llantas = em.merge(llantas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = llantas.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The llantas with id " + id + " no longer exists.");
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
            Llanta llantas;
            try {
                llantas = em.getReference(Llanta.class, id);
                llantas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The llantas with id " + id + " no longer exists.", enfe);
            }           
            em.remove(llantas);
            em.getTransaction().commit();
        } finally {
           
        }
    }

    public List<Llanta> findAll() {
        return findLlantasEntities(true, -1, -1);
    }

    public List<Llanta> findLlantasEntities(int maxResults, int firstResult) {
        return findLlantasEntities(false, maxResults, firstResult);
    }

    private List<Llanta> findLlantasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Llanta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            
        }
    }

    public Llanta find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Llanta.class, id);
        } finally {
           
        }
    }

}

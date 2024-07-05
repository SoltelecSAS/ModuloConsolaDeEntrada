/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.ServicioEspecial;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class ServicioEspecialJpaController {

   public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }
   
    public void create(ServicioEspecial spservices) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(spservices);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(spservices.getId()) != null) {
                throw new PreexistingEntityException("Spservices " + spservices + " already exists.", ex);
            }
            throw ex;
        } finally {
            
        }
    }

    public void edit(ServicioEspecial spservices) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            spservices = em.merge(spservices);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = spservices.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The spservices with id " + id + " no longer exists.");
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
            ServicioEspecial spservices;
            try {
                spservices = em.getReference(ServicioEspecial.class, id);
                spservices.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The spservices with id " + id + " no longer exists.", enfe);
            }
            em.remove(spservices);
            em.getTransaction().commit();
        } finally {
           
        }
    }

    public List<ServicioEspecial> findAll() {
        return findSpservicesEntities(true, -1, -1);
    }

    public List<ServicioEspecial> findSpservicesEntities(int maxResults, int firstResult) {
        return findSpservicesEntities(false, maxResults, firstResult);
    }

    private List<ServicioEspecial> findSpservicesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ServicioEspecial.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
           
        }
    }

    public ServicioEspecial find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ServicioEspecial.class, id);
        } finally {
           
        }
    }

}

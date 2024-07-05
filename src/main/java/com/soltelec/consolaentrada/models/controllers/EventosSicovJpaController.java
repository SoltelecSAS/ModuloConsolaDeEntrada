/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.entities.EventosSicov;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author SOLTELEC
 */
public class EventosSicovJpaController implements Serializable {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public EventosSicovJpaController() 
    {
    }

    public void create(EventosSicov eventosSicov) {
        EntityManager em = null;
        try {
            em = getEntityManager();
           if (em.getTransaction().isActive() == true) {
            em.flush();
            em.clear();
           
        } 
            em.clear();
            em.getTransaction().begin();           
            em.persist(eventosSicov);
            em.getTransaction().commit();            
        } finally {
            
        }
    }
    
    
    

    public void edit(EventosSicov eventosSicov) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            if (em.getTransaction().isActive() == false) {
                em.getTransaction().begin();
            }
            eventosSicov = em.merge(eventosSicov);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = eventosSicov.getId();
                if (findEventosSicov(id) == null) {
                    throw new NonexistentEntityException("The eventosSicov with id " + id + " no longer exists.");
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
            if (em.getTransaction().isActive() == false) {
                em.getTransaction().begin();
            }
            EventosSicov eventosSicov;
            try {
                eventosSicov = em.getReference(EventosSicov.class, id);
                eventosSicov.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The eventosSicov with id " + id + " no longer exists.", enfe);
            }
            em.remove(eventosSicov);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<EventosSicov> findEventosSicovEntities() {
        return findEventosSicovEntities(true, -1, -1);
    }

    public List<EventosSicov> findEventosSicovEntities(int maxResults, int firstResult) {
        return findEventosSicovEntities(false, maxResults, firstResult);
    }

    private List<EventosSicov> findEventosSicovEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EventosSicov.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public EventosSicov findEventosSicov(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EventosSicov.class, id);
        } finally {
            em.close();
        }
    }

    public int getEventosSicovCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EventosSicov> rt = cq.from(EventosSicov.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

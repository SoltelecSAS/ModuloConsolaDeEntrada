/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.Servicio;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
public class ServicioJpaController {

   public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }
   
    public void create(Servicio servicios) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(servicios);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(servicios.getId()) != null) {
                throw new PreexistingEntityException("Servicios " + servicios + " already exists.", ex);
            }
            throw ex;
        } finally {
           
        }
    }

    public void edit(Servicio servicios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            servicios = em.merge(servicios);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = servicios.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The servicios with id " + id + " no longer exists.");
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
            Servicio servicios;
            try {
                servicios = em.getReference(Servicio.class, id);
                servicios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The servicios with id " + id + " no longer exists.", enfe);
            }
            em.remove(servicios);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<Servicio> findAll() {
        return findServiciosEntities(true, -1, -1);
    }

    public List<Servicio> findServiciosEntities(int maxResults, int firstResult) {
        return findServiciosEntities(false, maxResults, firstResult);
    }

    private List<Servicio> findServiciosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Servicio.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
           
        }
    }

    public Servicio find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Servicio.class, id);
        } finally {
           
        }
    }

}

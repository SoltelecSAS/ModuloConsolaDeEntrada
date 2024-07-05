/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.Pais;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
public class PaisJpaController implements Serializable {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(Pais pais) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(pais);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(pais.getId()) != null) {
                throw new PreexistingEntityException("Pais " + pais + " already exists.", ex);
            }
            throw ex;
        } finally {
            
        }
    }

    public void edit(Pais pais) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            pais = em.merge(pais);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pais.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The pais with id " + id + " no longer exists.");
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
            Pais pais;
            try {
                pais = em.getReference(Pais.class, id);
                pais.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pais with id " + id + " no longer exists.", enfe);
            }
            em.remove(pais);
            em.getTransaction().commit();
        } finally {
           
        }
    }

    public List<Pais> findAll() {
        return findPaisEntities(true, -1, -1);
    }

    public List<Pais> findPaisEntities(int maxResults, int firstResult) {
        return findPaisEntities(false, maxResults, firstResult);
    }

    private List<Pais> findPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pais.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            
        }
    }

    public Pais find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
           
        }
    }
    
}

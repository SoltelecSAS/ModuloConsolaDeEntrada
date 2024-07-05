/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.TipoGasolina;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class TipoGasolinaJpaController {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }
    
    public void create(TipoGasolina tiposGasolina) throws PreexistingEntityException, Exception {
        
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tiposGasolina);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(tiposGasolina.getId()) != null) {
                throw new PreexistingEntityException("TiposGasolina " + tiposGasolina + " already exists.", ex);
            }
            throw ex;
        } finally {
            
        }
    }

    public void edit(TipoGasolina tiposGasolina) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tiposGasolina = em.merge(tiposGasolina);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tiposGasolina.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The tiposGasolina with id " + id + " no longer exists.");
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
            TipoGasolina tiposGasolina;
            try {
                tiposGasolina = em.getReference(TipoGasolina.class, id);
                tiposGasolina.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiposGasolina with id " + id + " no longer exists.", enfe);
            }
            em.remove(tiposGasolina);
            em.getTransaction().commit();
        } finally {
           
        }
    }

    public List<TipoGasolina> findAll() {
        return findTiposGasolinaEntities(true, -1, -1);
    }

    public List<TipoGasolina> findTiposGasolinaEntities(int maxResults, int firstResult) {
        return findTiposGasolinaEntities(false, maxResults, firstResult);
    }

    private List<TipoGasolina> findTiposGasolinaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoGasolina.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
          
        }
    }

    public TipoGasolina find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoGasolina.class, id);
        } finally {
           
        }
    }

}

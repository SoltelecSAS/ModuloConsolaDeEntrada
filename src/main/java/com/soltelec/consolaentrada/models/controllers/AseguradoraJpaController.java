/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.Aseguradora;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class AseguradoraJpaController {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(Aseguradora aseguradoras) throws PreexistingEntityException, Exception {

        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();                       
            em.persist(aseguradoras);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(aseguradoras.getId()) != null) {
                throw new PreexistingEntityException("Aseguradoras " + aseguradoras + " already exists.", ex);
            }
            throw ex;
        } finally {
           
        }
    }

    public void edit(Aseguradora aseguradoras) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            aseguradoras = em.merge(aseguradoras);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = aseguradoras.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The aseguradoras with id " + id + " no longer exists.");
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
            Aseguradora aseguradoras;
            try {
                aseguradoras = em.getReference(Aseguradora.class, id);
                aseguradoras.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The aseguradoras with id " + id + " no longer exists.", enfe);
            }
            em.remove(aseguradoras);
            em.getTransaction().commit();
        } finally {
           
        }
    }

    public List<Aseguradora> findAll() {
        return findAseguradorasEntities(true, -1, -1);
    }

    public List<Aseguradora> findAseguradorasEntities(int maxResults, int firstResult) {
        return findAseguradorasEntities(false, maxResults, firstResult);
    }

    private List<Aseguradora> findAseguradorasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Aseguradora.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
           
        }
    }

    public Aseguradora find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Aseguradora.class, id);
        } finally {
          
        }
    }

}

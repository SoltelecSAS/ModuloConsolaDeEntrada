/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.Departamento;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class DepartamentoJpaController {

   public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(Departamento departamentos) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(departamentos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(departamentos.getId()) != null) {
                throw new PreexistingEntityException("Departamentos " + departamentos + " already exists.", ex);
            }
            throw ex;
        } finally {
           
        }
    }

    public void edit(Departamento departamentos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            departamentos = em.merge(departamentos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = departamentos.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The departamentos with id " + id + " no longer exists.");
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
            Departamento departamentos;
            try {
                departamentos = em.getReference(Departamento.class, id);
                departamentos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamentos with id " + id + " no longer exists.", enfe);
            }
            em.remove(departamentos);
            em.getTransaction().commit();
        } finally {
           
        }
    }

    public List<Departamento> findAll() {
        return findDepartamentosEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentosEntities(int maxResults, int firstResult) {
        return findDepartamentosEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
           
        }
    }

    public Departamento find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            
        }
    }

}

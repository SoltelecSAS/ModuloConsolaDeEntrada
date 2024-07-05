/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.entities;

import com.soltelec.consolaentrada.models.entities.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class AuditoriaSicovJpaController implements Serializable {

    public AuditoriaSicovJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AuditoriaSicov auditoriaSicov) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(auditoriaSicov);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AuditoriaSicov auditoriaSicov) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            auditoriaSicov = em.merge(auditoriaSicov);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = auditoriaSicov.getIdAuditoriaSICOV();
                if (findAuditoriaSicov(id) == null) {
                    throw new NonexistentEntityException("The auditoriaSicov with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AuditoriaSicov auditoriaSicov;
            try {
                auditoriaSicov = em.getReference(AuditoriaSicov.class, id);
                auditoriaSicov.getIdAuditoriaSICOV();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The auditoriaSicov with id " + id + " no longer exists.", enfe);
            }
            em.remove(auditoriaSicov);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AuditoriaSicov> findAuditoriaSicovEntities() {
        return findAuditoriaSicovEntities(true, -1, -1);
    }

    public List<AuditoriaSicov> findAuditoriaSicovEntities(int maxResults, int firstResult) {
        return findAuditoriaSicovEntities(false, maxResults, firstResult);
    }

    private List<AuditoriaSicov> findAuditoriaSicovEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AuditoriaSicov.class));
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

    public AuditoriaSicov findAuditoriaSicov(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AuditoriaSicov.class, id);
        } finally {
            em.close();
        }
    }

    public int getAuditoriaSicovCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AuditoriaSicov> rt = cq.from(AuditoriaSicov.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

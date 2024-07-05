/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.Color;
import com.soltelec.consolaentrada.models.entities.LineaVehiculo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.soltelec.consolaentrada.models.entities.Marca;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author GerenciaDesarrollo
 */
public class LineaVehiculoJpaController implements Serializable {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(LineaVehiculo lineaVehiculo) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Marca marca = lineaVehiculo.getMarca();
            if (marca != null) {
                marca = em.getReference(marca.getClass(), marca.getId());
                lineaVehiculo.setMarca(marca);
            }
            em.persist(lineaVehiculo);
            if (marca != null) {
                marca.getLineasVehiculosList().add(lineaVehiculo);
                marca = em.merge(marca);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLineaVehiculo(lineaVehiculo.getId()) != null) {
                throw new PreexistingEntityException("LineaVehiculo " + lineaVehiculo + " already exists.", ex);
            }
            throw ex;
        } finally {
            
        }
    }

    public List<LineaVehiculo> findLineaTXT(String like,Integer idMarca) {
        EntityManager em = getEntityManager();
        
        try {                                        
            Query q = em.createQuery("SELECT l FROM LineaVehiculo l JOIN FETCH l.marca m  WHERE  m.id = :nMarca  AND l.nombre   LIKE :txtLinea ORDER BY l.nombre  ");
            like =like.concat("%");
             q.setParameter("nMarca",idMarca);           
            q.setParameter("txtLinea", like);           
            List<LineaVehiculo> lstVeh = q.getResultList();           
            return q.getResultList();
        } finally {
            
        }
    }

    public void edit(LineaVehiculo lineaVehiculo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LineaVehiculo persistentLineaVehiculo = em.find(LineaVehiculo.class, lineaVehiculo.getId());
            Marca marcaOld = persistentLineaVehiculo.getMarca();
            Marca marcaNew = lineaVehiculo.getMarca();
            if (marcaNew != null) {
                marcaNew = em.getReference(marcaNew.getClass(), marcaNew.getId());
                lineaVehiculo.setMarca(marcaNew);
            }
            lineaVehiculo = em.merge(lineaVehiculo);
            if (marcaOld != null && !marcaOld.equals(marcaNew)) {
                marcaOld.getLineasVehiculosList().remove(lineaVehiculo);
                marcaOld = em.merge(marcaOld);
            }
            if (marcaNew != null && !marcaNew.equals(marcaOld)) {
                marcaNew.getLineasVehiculosList().add(lineaVehiculo);
                marcaNew = em.merge(marcaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = lineaVehiculo.getId();
                if (findLineaVehiculo(id) == null) {
                    throw new NonexistentEntityException("The lineaVehiculo with id " + id + " no longer exists.");
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
            LineaVehiculo lineaVehiculo;
            try {
                lineaVehiculo = em.getReference(LineaVehiculo.class, id);
                lineaVehiculo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lineaVehiculo with id " + id + " no longer exists.", enfe);
            }
            Marca marca = lineaVehiculo.getMarca();
            if (marca != null) {
                marca.getLineasVehiculosList().remove(lineaVehiculo);
                marca = em.merge(marca);
            }
            em.remove(lineaVehiculo);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<LineaVehiculo> findLineaVehiculoEntities() {
        return findLineaVehiculoEntities(true, -1, -1);
    }

    public List<LineaVehiculo> findLineaVehiculoEntities(int maxResults, int firstResult) {
        return findLineaVehiculoEntities(false, maxResults, firstResult);
    }

    private List<LineaVehiculo> findLineaVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LineaVehiculo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
           
        }
    }

    public LineaVehiculo findLineaVehiculo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LineaVehiculo.class, id);
        } finally {
           
        }
    }

    public int getLineaVehiculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LineaVehiculo> rt = cq.from(LineaVehiculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
           
        }
    }

}

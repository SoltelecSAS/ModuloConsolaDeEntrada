/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.ClaseVehiculo;
import com.soltelec.consolaentrada.models.entities.Vehiculo;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
public class ClaseVehiculoJpaController {

   public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(ClaseVehiculo clasesVehiculo) throws PreexistingEntityException, Exception {
        
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(clasesVehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(clasesVehiculo.getId()) != null) {
                throw new PreexistingEntityException("ClasesVehiculo " + clasesVehiculo + " already exists.", ex);
            }
            throw ex;
        } finally {
            
        }
    }

    public void edit(ClaseVehiculo clasesVehiculo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            clasesVehiculo = em.merge(clasesVehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clasesVehiculo.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The clasesVehiculo with id " + id + " no longer exists.");
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
            ClaseVehiculo clasesVehiculo;
            try {
                clasesVehiculo = em.getReference(ClaseVehiculo.class, id);
                clasesVehiculo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clasesVehiculo with id " + id + " no longer exists.", enfe);
            }
            em.remove(clasesVehiculo);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<ClaseVehiculo> findAll() {
        return findClasesVehiculoEntities(true, -1, -1);
    }

    public List<ClaseVehiculo> findClasesVehiculoEntities(int maxResults, int firstResult) {
        return findClasesVehiculoEntities(false, maxResults, firstResult);
    }

    private List<ClaseVehiculo> findClasesVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClaseVehiculo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            
        }
    }

    public ClaseVehiculo find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClaseVehiculo.class, id);
        } finally {
           
        }
    }
    
    public Vehiculo findVehiculoPlaca(String placa) 
    {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT v FROM Vehiculo v WHERE v.placa = :placa");
            query.setParameter("placa", placa);
            List<Vehiculo> results = query.getResultList();
            if (results.isEmpty()) {
                System.out.println("El vehiculo No existe");
                return null;
            }
            return results.get(0);
        } finally {
            em.close();
        }
    }
}

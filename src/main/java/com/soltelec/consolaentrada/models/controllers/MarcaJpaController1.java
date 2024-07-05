/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.soltelec.consolaentrada.models.entities.LineaVehiculo;
import com.soltelec.consolaentrada.models.entities.Marca;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author GerenciaDesarrollo
 */
public class MarcaJpaController1 implements Serializable {

    public MarcaJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Marca marca) throws PreexistingEntityException, Exception {
        if (marca.getLineasVehiculosList() == null) {
            marca.setLineasVehiculosList(new ArrayList<LineaVehiculo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<LineaVehiculo> attachedLineasVehiculosList = new ArrayList<LineaVehiculo>();
            for (LineaVehiculo lineasVehiculosListLineaVehiculoToAttach : marca.getLineasVehiculosList()) {
                lineasVehiculosListLineaVehiculoToAttach = em.getReference(lineasVehiculosListLineaVehiculoToAttach.getClass(), lineasVehiculosListLineaVehiculoToAttach.getId());
                attachedLineasVehiculosList.add(lineasVehiculosListLineaVehiculoToAttach);
            }
            marca.setLineasVehiculosList(attachedLineasVehiculosList);
            em.persist(marca);
            for (LineaVehiculo lineasVehiculosListLineaVehiculo : marca.getLineasVehiculosList()) {
                Marca oldMarcaOfLineasVehiculosListLineaVehiculo = lineasVehiculosListLineaVehiculo.getMarca();
                lineasVehiculosListLineaVehiculo.setMarca(marca);
                lineasVehiculosListLineaVehiculo = em.merge(lineasVehiculosListLineaVehiculo);
                if (oldMarcaOfLineasVehiculosListLineaVehiculo != null) {
                    oldMarcaOfLineasVehiculosListLineaVehiculo.getLineasVehiculosList().remove(lineasVehiculosListLineaVehiculo);
                    oldMarcaOfLineasVehiculosListLineaVehiculo = em.merge(oldMarcaOfLineasVehiculosListLineaVehiculo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMarca(marca.getId()) != null) {
                throw new PreexistingEntityException("Marca " + marca + " already exists.", ex);
            }
            throw ex;
        } finally {
           
        }
    }

    public void edit(Marca marca) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Marca persistentMarca = em.find(Marca.class, marca.getId());
            List<LineaVehiculo> lineasVehiculosListOld = persistentMarca.getLineasVehiculosList();
            List<LineaVehiculo> lineasVehiculosListNew = marca.getLineasVehiculosList();
            List<String> illegalOrphanMessages = null;
            for (LineaVehiculo lineasVehiculosListOldLineaVehiculo : lineasVehiculosListOld) {
                if (!lineasVehiculosListNew.contains(lineasVehiculosListOldLineaVehiculo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LineaVehiculo " + lineasVehiculosListOldLineaVehiculo + " since its marca field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<LineaVehiculo> attachedLineasVehiculosListNew = new ArrayList<LineaVehiculo>();
            for (LineaVehiculo lineasVehiculosListNewLineaVehiculoToAttach : lineasVehiculosListNew) {
                lineasVehiculosListNewLineaVehiculoToAttach = em.getReference(lineasVehiculosListNewLineaVehiculoToAttach.getClass(), lineasVehiculosListNewLineaVehiculoToAttach.getId());
                attachedLineasVehiculosListNew.add(lineasVehiculosListNewLineaVehiculoToAttach);
            }
            lineasVehiculosListNew = attachedLineasVehiculosListNew;
            marca.setLineasVehiculosList(lineasVehiculosListNew);
            marca = em.merge(marca);
            for (LineaVehiculo lineasVehiculosListNewLineaVehiculo : lineasVehiculosListNew) {
                if (!lineasVehiculosListOld.contains(lineasVehiculosListNewLineaVehiculo)) {
                    Marca oldMarcaOfLineasVehiculosListNewLineaVehiculo = lineasVehiculosListNewLineaVehiculo.getMarca();
                    lineasVehiculosListNewLineaVehiculo.setMarca(marca);
                    lineasVehiculosListNewLineaVehiculo = em.merge(lineasVehiculosListNewLineaVehiculo);
                    if (oldMarcaOfLineasVehiculosListNewLineaVehiculo != null && !oldMarcaOfLineasVehiculosListNewLineaVehiculo.equals(marca)) {
                        oldMarcaOfLineasVehiculosListNewLineaVehiculo.getLineasVehiculosList().remove(lineasVehiculosListNewLineaVehiculo);
                        oldMarcaOfLineasVehiculosListNewLineaVehiculo = em.merge(oldMarcaOfLineasVehiculosListNewLineaVehiculo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = marca.getId();
                if (findMarca(id) == null) {
                    throw new NonexistentEntityException("The marca with id " + id + " no longer exists.");
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
            Marca marca;
            try {
                marca = em.getReference(Marca.class, id);
                marca.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The marca with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<LineaVehiculo> lineasVehiculosListOrphanCheck = marca.getLineasVehiculosList();
            for (LineaVehiculo lineasVehiculosListOrphanCheckLineaVehiculo : lineasVehiculosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Marca (" + marca + ") cannot be destroyed since the LineaVehiculo " + lineasVehiculosListOrphanCheckLineaVehiculo + " in its lineasVehiculosList field has a non-nullable marca field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(marca);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<Marca> findMarcaEntities() {
        return findMarcaEntities(true, -1, -1);
    }

    public List<Marca> findMarcaEntities(int maxResults, int firstResult) {
        return findMarcaEntities(false, maxResults, firstResult);
    }

    private List<Marca> findMarcaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Marca.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            
        }
    }

    public Marca findMarca(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Marca.class, id);
        } finally {
           
        }
    }

    public int getMarcaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Marca> rt = cq.from(Marca.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
           
        }
    }
    
}

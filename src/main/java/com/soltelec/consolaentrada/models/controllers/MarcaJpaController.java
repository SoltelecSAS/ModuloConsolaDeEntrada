/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.LineaVehiculo;
import com.soltelec.consolaentrada.models.entities.Marca;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
public class MarcaJpaController {

   public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(Marca marcas) throws PreexistingEntityException, Exception {
        if (marcas.getLineasVehiculosList() == null) {
            marcas.setLineasVehiculosList(new ArrayList<LineaVehiculo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            List<LineaVehiculo> attachedLineasVehiculosList = new ArrayList<>();
            for (LineaVehiculo lineasVehiculosListLineasVehiculosToAttach : marcas.getLineasVehiculosList()) {
                lineasVehiculosListLineasVehiculosToAttach = em.getReference(lineasVehiculosListLineasVehiculosToAttach.getClass(), lineasVehiculosListLineasVehiculosToAttach.getId());
                attachedLineasVehiculosList.add(lineasVehiculosListLineasVehiculosToAttach);
            }
            marcas.setLineasVehiculosList(attachedLineasVehiculosList);
            em.persist(marcas);
            
            for (LineaVehiculo lineasVehiculosListLineasVehiculos : marcas.getLineasVehiculosList()) {
                Marca oldMarcasOfLineasVehiculosListLineasVehiculos = lineasVehiculosListLineasVehiculos.getMarca();
                lineasVehiculosListLineasVehiculos.setMarca(marcas);
                lineasVehiculosListLineasVehiculos = em.merge(lineasVehiculosListLineasVehiculos);
                if (oldMarcasOfLineasVehiculosListLineasVehiculos != null) {
                    oldMarcasOfLineasVehiculosListLineasVehiculos.getLineasVehiculosList().remove(lineasVehiculosListLineasVehiculos);
                    em.merge(oldMarcasOfLineasVehiculosListLineasVehiculos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(marcas.getId()) != null) {
                throw new PreexistingEntityException("Marcas " + marcas + " already exists.", ex);
            }
            throw ex;
        } finally {
           
        }
    }

    public void edit(Marca marcas) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Marca persistentMarcas = em.find(Marca.class, marcas.getId());
            
            List<LineaVehiculo> lineasVehiculosListOld = persistentMarcas.getLineasVehiculosList();
            List<LineaVehiculo> lineasVehiculosListNew = marcas.getLineasVehiculosList();
            List<String> illegalOrphanMessages = null;
            for (LineaVehiculo lineasVehiculosListOldLineasVehiculos : lineasVehiculosListOld) {
                if (!lineasVehiculosListNew.contains(lineasVehiculosListOldLineasVehiculos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<>();
                    }
                    illegalOrphanMessages.add("You must retain LineasVehiculos " + lineasVehiculosListOldLineasVehiculos + " since its marcas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            
            
            List<LineaVehiculo> attachedLineasVehiculosListNew = new ArrayList<>();
            for (LineaVehiculo lineasVehiculosListNewLineasVehiculosToAttach : lineasVehiculosListNew) {
                lineasVehiculosListNewLineasVehiculosToAttach = em.getReference(lineasVehiculosListNewLineasVehiculosToAttach.getClass(), lineasVehiculosListNewLineasVehiculosToAttach.getId());
                attachedLineasVehiculosListNew.add(lineasVehiculosListNewLineasVehiculosToAttach);
            }
            lineasVehiculosListNew = attachedLineasVehiculosListNew;
            marcas.setLineasVehiculosList(lineasVehiculosListNew);
            marcas = em.merge(marcas);
           
            
            for (LineaVehiculo lineasVehiculosListNewLineasVehiculos : lineasVehiculosListNew) {
                if (!lineasVehiculosListOld.contains(lineasVehiculosListNewLineasVehiculos)) {
                    Marca oldMarcasOfLineasVehiculosListNewLineasVehiculos = lineasVehiculosListNewLineasVehiculos.getMarca();
                    lineasVehiculosListNewLineasVehiculos.setMarca(marcas);
                    lineasVehiculosListNewLineasVehiculos = em.merge(lineasVehiculosListNewLineasVehiculos);
                    if (oldMarcasOfLineasVehiculosListNewLineasVehiculos != null && !oldMarcasOfLineasVehiculosListNewLineasVehiculos.equals(marcas)) {
                        oldMarcasOfLineasVehiculosListNewLineasVehiculos.getLineasVehiculosList().remove(lineasVehiculosListNewLineasVehiculos);
                        em.merge(oldMarcasOfLineasVehiculosListNewLineasVehiculos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = marcas.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The marcas with id " + id + " no longer exists.");
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
            Marca marcas;
            try {
                marcas = em.getReference(Marca.class, id);
                marcas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The marcas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<LineaVehiculo> lineasVehiculosListOrphanCheck = marcas.getLineasVehiculosList();
            for (LineaVehiculo lineasVehiculosListOrphanCheckLineasVehiculos : lineasVehiculosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add("This Marcas (" + marcas + ") cannot be destroyed since the LineasVehiculos " + lineasVehiculosListOrphanCheckLineasVehiculos + " in its lineasVehiculosList field has a non-nullable marcas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
           
            
            em.remove(marcas);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<Marca> findAll() {
        return findMarcasEntities(true, -1, -1);
    }

    public List<Marca> findMarcasEntities(int maxResults, int firstResult) {
        return findMarcasEntities(false, maxResults, firstResult);
    }

    private List<Marca> findMarcasEntities(boolean all, int maxResults, int firstResult) {
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

    public Marca find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Marca.class, id);
        } finally {
           
        }
    }

}

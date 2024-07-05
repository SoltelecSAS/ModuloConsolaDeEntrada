/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.Propietario;
import com.soltelec.consolaentrada.models.entities.Vehiculo;

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
public class PropietarioJpaController {

  public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }
  
    public void create(Propietario propietarios) throws PreexistingEntityException, Exception {
        if (propietarios.getVehiculoList() == null) {
            propietarios.setVehiculoList(new ArrayList<Vehiculo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Vehiculo> attachedVehiculosList = new ArrayList<>();
            for (Vehiculo vehiculosListVehiculosToAttach : propietarios.getVehiculoList()) {
                vehiculosListVehiculosToAttach = em.getReference(vehiculosListVehiculosToAttach.getClass(), vehiculosListVehiculosToAttach.getId());
                attachedVehiculosList.add(vehiculosListVehiculosToAttach);
            }
            propietarios.setVehiculoList(attachedVehiculosList);
            em.persist(propietarios);
            for (Vehiculo vehiculosListVehiculos : propietarios.getVehiculoList()) {
                Propietario oldPropietariosOfVehiculosListVehiculos = vehiculosListVehiculos.getPropietario();
                vehiculosListVehiculos.setPropietario(propietarios);
                vehiculosListVehiculos = em.merge(vehiculosListVehiculos);
                if (oldPropietariosOfVehiculosListVehiculos != null) {
                    oldPropietariosOfVehiculosListVehiculos.getVehiculoList().remove(vehiculosListVehiculos);
                    oldPropietariosOfVehiculosListVehiculos = em.merge(oldPropietariosOfVehiculosListVehiculos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(propietarios.getId()) != null) {
                throw new PreexistingEntityException("Propietarios " + propietarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            
        }
    }

    public void edit(Propietario propietarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Propietario persistentPropietarios = em.find(Propietario.class, propietarios.getId());
            List<Vehiculo> vehiculosListOld = persistentPropietarios.getVehiculoList();
            List<Vehiculo> vehiculosListNew = propietarios.getVehiculoList();
            List<String> illegalOrphanMessages = null;
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Vehiculo> attachedVehiculosListNew = new ArrayList<>();
            for (Vehiculo vehiculosListNewVehiculosToAttach : vehiculosListNew) {
                vehiculosListNewVehiculosToAttach = em.getReference(vehiculosListNewVehiculosToAttach.getClass(), vehiculosListNewVehiculosToAttach.getId());
                attachedVehiculosListNew.add(vehiculosListNewVehiculosToAttach);
            }
            vehiculosListNew = attachedVehiculosListNew;
            propietarios.setVehiculoList(vehiculosListNew);
            propietarios = em.merge(propietarios);
            for (Vehiculo vehiculosListNewVehiculos : vehiculosListNew) {
                if (!vehiculosListOld.contains(vehiculosListNewVehiculos)) {
                    Propietario oldPropietariosOfVehiculosListNewVehiculos = vehiculosListNewVehiculos.getPropietario();
                    vehiculosListNewVehiculos.setPropietario(propietarios);
                    vehiculosListNewVehiculos = em.merge(vehiculosListNewVehiculos);
                    if (oldPropietariosOfVehiculosListNewVehiculos != null && !oldPropietariosOfVehiculosListNewVehiculos.equals(propietarios)) {
                        oldPropietariosOfVehiculosListNewVehiculos.getVehiculoList().remove(vehiculosListNewVehiculos);
                        oldPropietariosOfVehiculosListNewVehiculos = em.merge(oldPropietariosOfVehiculosListNewVehiculos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (IllegalOrphanException ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = propietarios.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The propietarios with id " + id + " no longer exists.");
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
            Propietario propietarios;
            try {
                propietarios = em.getReference(Propietario.class, id);
                propietarios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The propietarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Vehiculo> vehiculosListOrphanCheck = propietarios.getVehiculoList();
            for (Vehiculo vehiculosListOrphanCheckVehiculos : vehiculosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add("This Propietarios (" + propietarios + ") cannot be destroyed since the Vehiculos " + vehiculosListOrphanCheckVehiculos + " in its vehiculosList field has a non-nullable propietarios field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(propietarios);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<Propietario> findAll() {
        return findPropietariosEntities(true, -1, -1);
    }

    public List<Propietario> findPropietariosEntities(int maxResults, int firstResult) {
        return findPropietariosEntities(false, maxResults, firstResult);
    }

    private List<Propietario> findPropietariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Propietario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
           
        }
    }

    public Propietario find(Long id) {
        EntityManager em = getEntityManager();
        getEntityManager().getEntityManagerFactory().getCache().evict(Propietario.class);
        try {
            return em.find(Propietario.class, id);
        } finally {
         
        }
    }

}

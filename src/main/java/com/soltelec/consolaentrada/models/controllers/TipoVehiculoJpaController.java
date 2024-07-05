/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.Permisibles;
import com.soltelec.consolaentrada.models.entities.TipoVehiculo;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author GerenciaDesarrollo
 */
public class TipoVehiculoJpaController {

  public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }
  

    public void create(TipoVehiculo tipoVehiculo) throws PreexistingEntityException, Exception {
        if (tipoVehiculo.getPermisiblesList() == null) {
            tipoVehiculo.setPermisiblesList(new ArrayList<Permisibles>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Permisibles> attachedPermisiblesList = new ArrayList<>();
            for (Permisibles permisiblesListPermisiblesToAttach : tipoVehiculo.getPermisiblesList()) {
                permisiblesListPermisiblesToAttach = em.getReference(permisiblesListPermisiblesToAttach.getClass(), permisiblesListPermisiblesToAttach.getIdpermisible());
                attachedPermisiblesList.add(permisiblesListPermisiblesToAttach);
            }
            tipoVehiculo.setPermisiblesList(attachedPermisiblesList);
            em.persist(tipoVehiculo);
            for (Permisibles permisiblesListPermisibles : tipoVehiculo.getPermisiblesList()) {
                TipoVehiculo oldTipoVehiculoOfPermisiblesListPermisibles = permisiblesListPermisibles.getTipoVehiculo();
                permisiblesListPermisibles.setTipoVehiculo(tipoVehiculo);
                permisiblesListPermisibles = em.merge(permisiblesListPermisibles);
                if (oldTipoVehiculoOfPermisiblesListPermisibles != null) {
                    oldTipoVehiculoOfPermisiblesListPermisibles.getPermisiblesList().remove(permisiblesListPermisibles);
                    oldTipoVehiculoOfPermisiblesListPermisibles = em.merge(oldTipoVehiculoOfPermisiblesListPermisibles);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(tipoVehiculo.getId()) != null) {
                throw new PreexistingEntityException("TipoVehiculo " + tipoVehiculo + " already exists.", ex);
            }
            throw ex;
        } finally {
           
        }
    }

    public void edit(TipoVehiculo tipoVehiculo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoVehiculo persistentTipoVehiculo = em.find(TipoVehiculo.class, tipoVehiculo.getId());
            List<Permisibles> permisiblesListOld = persistentTipoVehiculo.getPermisiblesList();
            List<Permisibles> permisiblesListNew = tipoVehiculo.getPermisiblesList();
            List<String> illegalOrphanMessages = null;
            for (Permisibles permisiblesListOldPermisibles : permisiblesListOld) {
                if (!permisiblesListNew.contains(permisiblesListOldPermisibles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<>();
                    }
                    illegalOrphanMessages.add("You must retain Permisibles " + permisiblesListOldPermisibles + " since its tipoVehiculo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Permisibles> attachedPermisiblesListNew = new ArrayList<>();
            for (Permisibles permisiblesListNewPermisiblesToAttach : permisiblesListNew) {
                permisiblesListNewPermisiblesToAttach = em.getReference(permisiblesListNewPermisiblesToAttach.getClass(), permisiblesListNewPermisiblesToAttach.getIdpermisible());
                attachedPermisiblesListNew.add(permisiblesListNewPermisiblesToAttach);
            }
            permisiblesListNew = attachedPermisiblesListNew;
            tipoVehiculo.setPermisiblesList(permisiblesListNew);
            tipoVehiculo = em.merge(tipoVehiculo);
            for (Permisibles permisiblesListNewPermisibles : permisiblesListNew) {
                if (!permisiblesListOld.contains(permisiblesListNewPermisibles)) {
                    TipoVehiculo oldTipoVehiculoOfPermisiblesListNewPermisibles = permisiblesListNewPermisibles.getTipoVehiculo();
                    permisiblesListNewPermisibles.setTipoVehiculo(tipoVehiculo);
                    permisiblesListNewPermisibles = em.merge(permisiblesListNewPermisibles);
                    if (oldTipoVehiculoOfPermisiblesListNewPermisibles != null && !oldTipoVehiculoOfPermisiblesListNewPermisibles.equals(tipoVehiculo)) {
                        oldTipoVehiculoOfPermisiblesListNewPermisibles.getPermisiblesList().remove(permisiblesListNewPermisibles);
                        oldTipoVehiculoOfPermisiblesListNewPermisibles = em.merge(oldTipoVehiculoOfPermisiblesListNewPermisibles);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoVehiculo.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The tipoVehiculo with id " + id + " no longer exists.");
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
            TipoVehiculo tipoVehiculo;
            try {
                tipoVehiculo = em.getReference(TipoVehiculo.class, id);
                tipoVehiculo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoVehiculo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            
            List<Permisibles> permisiblesListOrphanCheck = tipoVehiculo.getPermisiblesList();
            for (Permisibles permisiblesListOrphanCheckPermisibles : permisiblesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add("This TipoVehiculo (" + tipoVehiculo + ") cannot be destroyed since the Permisibles " + permisiblesListOrphanCheckPermisibles + " in its permisiblesList field has a non-nullable tipoVehiculo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoVehiculo);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<TipoVehiculo> findAll() {
        return findTipoVehiculoEntities(true, -1, -1);
    }
    public List<TipoVehiculo> findAllAutorizados() {
        EntityManager em = getEntityManager();
        String quer = "SELECT t FROM TipoVehiculo t  WHERE t.id in( 1,2,3,4,5,7,109,110) ";
        System.out.println(quer);
        TypedQuery<TipoVehiculo> query = em.createQuery(quer, TipoVehiculo.class);        
        System.out.println(query);
        try {
            return query.getResultList();
        } catch (NoResultException ex) {
            return null;
        } finally {

        }
    }

    public List<TipoVehiculo> findTipoVehiculoEntities(int maxResults, int firstResult) {
        return findTipoVehiculoEntities(false, maxResults, firstResult);
    }

    private List<TipoVehiculo> findTipoVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoVehiculo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
           
        }
    }

    public TipoVehiculo find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoVehiculo.class, id);
        } finally {
           
        }
    }

}

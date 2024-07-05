/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.Grupo;
import com.soltelec.consolaentrada.models.entities.TipoPrueba;

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
public class TipoPruebaJpaController {

   public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }
   
    public void create(TipoPrueba tipoPrueba) throws PreexistingEntityException, Exception {
        if (tipoPrueba.getGruposList() == null) {
            tipoPrueba.setGruposList(new ArrayList<Grupo>());
        }
        
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Grupo> attachedGruposList = new ArrayList<>();
            for (Grupo gruposListGruposToAttach : tipoPrueba.getGruposList()) {
                //gruposListGruposToAttach = em.getReference(gruposListGruposToAttach.getClass(), gruposListGruposToAttach.getId());
                attachedGruposList.add(gruposListGruposToAttach);
            }
            tipoPrueba.setGruposList(attachedGruposList);
            em.persist(tipoPrueba);
            for (Grupo gruposListGrupos : tipoPrueba.getGruposList()) {
                //gruposListGrupos.getTipoPruebaList().add(tipoPrueba);
                gruposListGrupos = em.merge(gruposListGrupos);
            }
           
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(tipoPrueba.getId()) != null) {
                throw new PreexistingEntityException("TipoPrueba " + tipoPrueba + " already exists.", ex);
            }
            throw ex;
        } finally {
            
        }
    }

    public void edit(TipoPrueba tipoPrueba) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPrueba persistentTipoPrueba = em.find(TipoPrueba.class, tipoPrueba.getId());
            List<Grupo> gruposListOld = persistentTipoPrueba.getGruposList();
            List<Grupo> gruposListNew = tipoPrueba.getGruposList();
            
            
            List<Grupo> attachedGruposListNew = new ArrayList<>();
            for (Grupo gruposListNewGruposToAttach : gruposListNew) {
                //gruposListNewGruposToAttach = em.getReference(gruposListNewGruposToAttach.getClass(), gruposListNewGruposToAttach.getId());
                attachedGruposListNew.add(gruposListNewGruposToAttach);
            }
            gruposListNew = attachedGruposListNew;
            tipoPrueba.setGruposList(gruposListNew);
            
            tipoPrueba = em.merge(tipoPrueba);
            for (Grupo gruposListOldGrupos : gruposListOld) {
                if (!gruposListNew.contains(gruposListOldGrupos)) {
                    //gruposListOldGrupos.getTipoPruebaList().remove(tipoPrueba);
                    gruposListOldGrupos = em.merge(gruposListOldGrupos);
                }
            }
            for (Grupo gruposListNewGrupos : gruposListNew) {
                if (!gruposListOld.contains(gruposListNewGrupos)) {
                    //gruposListNewGrupos.getTipoPruebaList().add(tipoPrueba);
                    gruposListNewGrupos = em.merge(gruposListNewGrupos);
                }
            }
            
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoPrueba.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The tipoPrueba with id " + id + " no longer exists.");
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
            TipoPrueba tipoPrueba;
            try {
                tipoPrueba = em.getReference(TipoPrueba.class, id);
                tipoPrueba.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoPrueba with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
           
            
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Grupo> gruposList = tipoPrueba.getGruposList();
            for (Grupo gruposListGrupos : gruposList) {
                //gruposListGrupos.getTipoPruebaList().remove(tipoPrueba);
                gruposListGrupos = em.merge(gruposListGrupos);
            }
            em.remove(tipoPrueba);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<TipoPrueba> findAll() {
        return findTipoPruebaEntities(true, -1, -1);
    }

    public List<TipoPrueba> findTipoPruebaEntities(int maxResults, int firstResult) {
        return findTipoPruebaEntities(false, maxResults, firstResult);
    }

    private List<TipoPrueba> findTipoPruebaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoPrueba.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
          
        }
    }

    public TipoPrueba find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoPrueba.class, id);
        } finally {
          
        }
    }

}

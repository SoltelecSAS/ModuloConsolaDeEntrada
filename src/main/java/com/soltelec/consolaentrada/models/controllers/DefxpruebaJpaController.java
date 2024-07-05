/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 package com.soltelec.consolaentrada.models.controllers;


import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.soltelec.consolaentrada.models.entities.Defectos;
import com.soltelec.consolaentrada.models.entities.Defxprueba;
import com.soltelec.consolaentrada.models.entities.DefxpruebaPK;
import com.soltelec.consolaentrada.models.entities.Prueba;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author GerenciaDesarrollo
 */
public class DefxpruebaJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Defxprueba defxprueba) throws  Exception {
        if (defxprueba.getDefxpruebaPK() == null) {
            defxprueba.setDefxpruebaPK(new DefxpruebaPK());
        }
        defxprueba.getDefxpruebaPK().setIdDefecto(defxprueba.getDefectos().getCardefault());
        defxprueba.getDefxpruebaPK().setIdPrueba(defxprueba.getPruebas().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Defectos defectos = defxprueba.getDefectos();
            if (defectos != null) {
                defectos = em.getReference(defectos.getClass(), defectos.getCardefault());
                defxprueba.setDefectos(defectos);
            }
            Prueba pruebas = defxprueba.getPruebas();
            if (pruebas != null) {
                pruebas = em.getReference(pruebas.getClass(), pruebas.getId());
                defxprueba.setPruebas(pruebas);
            }
            em.persist(defxprueba);
            if (defectos != null) {
                defectos.getDefxpruebaList().add(defxprueba);
                defectos = em.merge(defectos);
            }
            if (pruebas != null) {
                pruebas.getDefxpruebaList().add(defxprueba);
                pruebas = em.merge(pruebas);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDefxprueba(defxprueba.getDefxpruebaPK()) != null) {
                //throw new PreexistingEntityException("Defxprueba " + defxprueba + " already exists.", ex);
            }
            throw ex;
        } finally {
            
        }
    }

    public void edit(Defxprueba defxprueba) throws Exception {
        defxprueba.getDefxpruebaPK().setIdDefecto(defxprueba.getDefectos().getCardefault());
        defxprueba.getDefxpruebaPK().setIdPrueba(defxprueba.getPruebas().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Defxprueba persistentDefxprueba = em.find(Defxprueba.class, defxprueba.getDefxpruebaPK());
            Defectos defectosOld = persistentDefxprueba.getDefectos();
            Defectos defectosNew = defxprueba.getDefectos();
            Prueba pruebasOld = persistentDefxprueba.getPruebas();
            Prueba pruebasNew = defxprueba.getPruebas();
            if (defectosNew != null) {
                defectosNew = em.getReference(defectosNew.getClass(), defectosNew.getCardefault());
                defxprueba.setDefectos(defectosNew);
            }
            if (pruebasNew != null) {
                pruebasNew = em.getReference(pruebasNew.getClass(), pruebasNew.getId());
                defxprueba.setPruebas(pruebasNew);
            }
            defxprueba = em.merge(defxprueba);
            if (defectosOld != null && !defectosOld.equals(defectosNew)) {
                defectosOld.getDefxpruebaList().remove(defxprueba);
                defectosOld = em.merge(defectosOld);
            }
            if (defectosNew != null && !defectosNew.equals(defectosOld)) {
                defectosNew.getDefxpruebaList().add(defxprueba);
                defectosNew = em.merge(defectosNew);
            }
            if (pruebasOld != null && !pruebasOld.equals(pruebasNew)) {
                pruebasOld.getDefxpruebaList().remove(defxprueba);
                pruebasOld = em.merge(pruebasOld);
            }
            if (pruebasNew != null && !pruebasNew.equals(pruebasOld)) {
                pruebasNew.getDefxpruebaList().add(defxprueba);
                pruebasNew = em.merge(pruebasNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DefxpruebaPK id = defxprueba.getDefxpruebaPK();
                if (findDefxprueba(id) == null) {
                    //throw new NonexistentEntityException("The defxprueba with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            
        }
    }

    public void destroy(DefxpruebaPK id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Defxprueba defxprueba= null;
            try {
                defxprueba = em.getReference(Defxprueba.class, id);
                defxprueba.getDefxpruebaPK();
            } catch (EntityNotFoundException enfe) {
                //throw new NonexistentEntityException("The defxprueba with id " + id + " no longer exists.", enfe);
            }
            Defectos defectos = defxprueba.getDefectos();
            if (defectos != null) {
                defectos.getDefxpruebaList().remove(defxprueba);
                defectos = em.merge(defectos);
            }
            Prueba pruebas = defxprueba.getPruebas();
            if (pruebas != null) {
                pruebas.getDefxpruebaList().remove(defxprueba);
                pruebas = em.merge(pruebas);
            }
            em.remove(defxprueba);
            em.getTransaction().commit();
        } finally {
            
        }
    }

    public List<Defxprueba> findDefxpruebaEntities() {
        return findDefxpruebaEntities(true, -1, -1);
    }

    public List<Defxprueba> findDefxpruebaEntities(int maxResults, int firstResult) {
        return findDefxpruebaEntities(false, maxResults, firstResult);
    }

    private List<Defxprueba> findDefxpruebaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Defxprueba.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
          
        }
    }

    public Defxprueba findDefxprueba(DefxpruebaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Defxprueba.class, id);
        } finally {
           
        }
    }

    public int getDefxpruebaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Defxprueba> rt = cq.from(Defxprueba.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
           
        }
    }

    public List<Defxprueba> obtenerDefectosXPrueba(int idPrueba) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNativeQuery("select * from defxprueba dp where dp.id_prueba = ?1", Defxprueba.class);
            q.setParameter(1, idPrueba);
            return q.getResultList();
        } finally {
           
        }
    }

}

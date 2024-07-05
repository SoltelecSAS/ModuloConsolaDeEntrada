/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.controllers.exceptions.PreexistingEntityException;
import com.soltelec.consolaentrada.models.entities.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
public class UsuarioJpaController implements Serializable {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(Usuario usuarios) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(usuarios);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(usuarios.getUsuario()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {

        }
    }

    public void edit(Usuario usuarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            usuarios = em.merge(usuarios);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getUsuario();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuario usuarios;
            try {
                usuarios = em.getReference(Usuario.class, id);
                usuarios.getUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {

        }
    }

    public List<Usuario> findAll() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuario> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    public List<Usuario> findDtCda() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT u FROM  Usuario u where u.nick LIKE :txtDtCda ORDER BY u.nick ASC");
            q.setParameter("txtDtCda", ".DT%");
            return q.getResultList();
        } finally {

        }
    }

    public Usuario findNombre(String nombre) 
    {
        System.out.println("");
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT u FROM  Usuario u where u.nombre= :txtDtCda");
            q.setParameter("txtDtCda", nombre);
            return (Usuario) q.getSingleResult();
        } finally {

        }
    }

    private List<Usuario> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {

        }
    }

    public Usuario find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } catch (Exception e) {
            System.out.println("Error En el metodo : find" +e.getMessage() + e.getLocalizedMessage() );
        }finally {

        }
        return null;
    }

    public Usuario findDefault() {
        EntityManager em = getEntityManager();
        try {
            Integer id = 1;
            return em.find(Usuario.class, id);
        } finally {

        }
    }

    public Usuario getUsuarioByNick(String nick) {
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("Usuario.findByNickusuario");
        q.setParameter("nickusuario", nick);
        Usuario u = null;
        try {
            u = (Usuario) q.getSingleResult();
        } catch (NoResultException nre) {
            u = null;
        }
        return u;
    }

    public Usuario findUsuariosByName(String theName) throws NoResultException {
        EntityManager em = getEntityManager();
        Usuario u = (Usuario) em.createQuery("SELECT u FROM Usuario u WHERE u.nombre = :nombre").setParameter("nombre", theName).getSingleResult();

        return u;
    }

    public static Boolean findUsuariosByAdministrador(String passW, EntityManager em) throws NoResultException {
        boolean resultado = false;
        List<Usuario> lstUsr = em.createQuery("SELECT u FROM Usuario u WHERE u.contrasena = :passWrd").setParameter("passWrd", passW).getResultList();
        if (lstUsr.size() > 0) {
            Usuario u = (Usuario) lstUsr.iterator().next();
            if (u != null) {
                if (u.getAdministrador().equalsIgnoreCase("Y") || u.getAdministrador().equalsIgnoreCase("A")) {
                    resultado = true;
                } else {
                    resultado = false;
                }
            } else {
                resultado = false;
            }
        }
        return resultado;
    }

    public static String findUsuariosByAdmNombre(String passW, EntityManager em) throws NoResultException {
        String resultado = "0";
        List<Usuario> lstUsr = em.createQuery("SELECT u FROM Usuario u WHERE u.contrasena = :passWrd").setParameter("passWrd", passW).getResultList();
        if (lstUsr.size() > 0) {
            Usuario u = (Usuario) lstUsr.iterator().next();
            if (u.getAdministrador().equalsIgnoreCase("Y") || u.getAdministrador().equalsIgnoreCase("A")) {
                resultado = u.getNombre();
            } else {
                resultado = "0";
            }
        }
        return resultado;
    }

    public static Boolean findUsuariosByAdmYellow(String passW, EntityManager em) throws NoResultException {
        boolean resultado = false;
        List<Usuario> lstUsr = em.createQuery("SELECT u FROM Usuario u WHERE u.contrasena = :passWrd").setParameter("passWrd", passW).getResultList();
        if (lstUsr.size() > 0) {
            Usuario u = (Usuario) lstUsr.iterator().next();
            if (u != null) {
                if (u.getAdministrador().equalsIgnoreCase("A")) {
                    resultado = true;
                } else {
                    resultado = false;
                }
            } else {
                resultado = false;
            }
        }
        return resultado;
    }
}

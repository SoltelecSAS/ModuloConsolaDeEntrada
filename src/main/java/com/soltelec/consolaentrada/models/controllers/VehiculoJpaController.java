/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.entities.LineaVehiculo;
import com.soltelec.consolaentrada.models.entities.Aseguradora;
import com.soltelec.consolaentrada.models.entities.Marca;
import com.soltelec.consolaentrada.models.entities.Llanta;
import com.soltelec.consolaentrada.models.entities.Servicio;
import com.soltelec.consolaentrada.models.entities.TipoGasolina;
import com.soltelec.consolaentrada.models.entities.TipoVehiculo;
import com.soltelec.consolaentrada.models.entities.Propietario;
import com.soltelec.consolaentrada.models.entities.Color;
import com.soltelec.consolaentrada.models.entities.ClaseVehiculo;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.Vehiculo;
import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.utilities.Mensajes;
import com.soltelec.consolaentrada.utilities.UtilConexion;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GerenciaDesarrollo
 */
public class VehiculoJpaController {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(Vehiculo vehiculos) {
        if (vehiculos.getHojaPruebasList() == null) {
            vehiculos.setHojaPruebasList(new ArrayList<HojaPruebas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Llanta llantas = vehiculos.getLlantas();
            if (llantas != null) {
                llantas = em.getReference(llantas.getClass(), llantas.getId());
                vehiculos.setLlantas(llantas);
            }
            Servicio servicios = vehiculos.getServicios();
            if (servicios != null) {
                servicios = em.getReference(servicios.getClass(), servicios.getId());
                vehiculos.setServicios(servicios);
            }
            Aseguradora aseguradoras = vehiculos.getAseguradora();
            if (aseguradoras != null) {
                aseguradoras = em.getReference(aseguradoras.getClass(), aseguradoras.getId());
                vehiculos.setAseguradora(aseguradoras);
            }
//            Usuarios usuarios = vehiculos.getUsuarios();
//            if (usuarios != null) {
//                usuarios = em.getReference(usuarios.getClass(), usuarios.getGeuser());
//                vehiculos.setUsuarios(usuarios);
//            }
            TipoGasolina tiposGasolina = vehiculos.getTipoGasolina();
            if (tiposGasolina != null) {
                tiposGasolina = em.getReference(tiposGasolina.getClass(), tiposGasolina.getId());
                vehiculos.setTipoGasolina(tiposGasolina);
            }
            Color colores = vehiculos.getColor();
            if (colores != null) {
                colores = em.getReference(colores.getClass(), colores.getId());
                vehiculos.setColor(colores);
            }
            ClaseVehiculo clasesVehiculo = vehiculos.getClaseVehiculo();
            if (clasesVehiculo != null) {
                clasesVehiculo = em.getReference(clasesVehiculo.getClass(), clasesVehiculo.getId());
                vehiculos.setClaseVehiculo(clasesVehiculo);
            }
            TipoVehiculo tipoVehiculo = vehiculos.getTipoVehiculo();
            if (tipoVehiculo != null) {
                tipoVehiculo = em.getReference(tipoVehiculo.getClass(), tipoVehiculo.getId());
                vehiculos.setTipoVehiculo(tipoVehiculo);
            }
            Propietario propietarios = vehiculos.getPropietario();
            if (propietarios != null) {
                propietarios = em.getReference(propietarios.getClass(), propietarios.getId());
                vehiculos.setPropietario(propietarios);
            }
            Marca marcas = vehiculos.getMarca();
            if (marcas != null) {
                marcas = em.getReference(marcas.getClass(), marcas.getId());
                vehiculos.setMarca(marcas);
            }
            LineaVehiculo lineasVehiculos = vehiculos.getLineaVehiculo();
            if (lineasVehiculos != null) {
                lineasVehiculos = em.getReference(lineasVehiculos.getClass(), lineasVehiculos.getId());
                vehiculos.setLineaVehiculo(lineasVehiculos);
            }
            List<HojaPruebas> attachedHojaPruebasList = new ArrayList<>();
            for (HojaPruebas hojaPruebasListHojaPruebasToAttach : vehiculos.getHojaPruebasList()) {
                hojaPruebasListHojaPruebasToAttach = em.getReference(hojaPruebasListHojaPruebasToAttach.getClass(), hojaPruebasListHojaPruebasToAttach.getId());
                attachedHojaPruebasList.add(hojaPruebasListHojaPruebasToAttach);
            }
            vehiculos.setHojaPruebasList(attachedHojaPruebasList);
            em.persist(vehiculos);
            //            if (usuarios != null) {
//                usuarios.getVehiculoList().add(vehiculos);
//                usuarios = em.merge(usuarios);
//            }
            if (propietarios != null) {
                propietarios.getVehiculoList().add(vehiculos);
                propietarios = em.merge(propietarios);
            }

            for (HojaPruebas hojaPruebasListHojaPruebas : vehiculos.getHojaPruebasList()) {
                Vehiculo oldVehiculoOfHojaPruebasListHojaPruebas = hojaPruebasListHojaPruebas.getVehiculo();
                hojaPruebasListHojaPruebas.setVehiculo(vehiculos);
                hojaPruebasListHojaPruebas = em.merge(hojaPruebasListHojaPruebas);
                if (oldVehiculoOfHojaPruebasListHojaPruebas != null) {
                    oldVehiculoOfHojaPruebasListHojaPruebas.getHojaPruebasList().remove(hojaPruebasListHojaPruebas);
                    oldVehiculoOfHojaPruebasListHojaPruebas = em.merge(oldVehiculoOfHojaPruebasListHojaPruebas);
                }
            }
            em.getTransaction().commit();
        } finally {

        }
    }

    public void edit1(Vehiculo vehiculo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vehiculo persistentVehiculo = em.find(Vehiculo.class, vehiculo.getId());
            Propietario propietarioOld = persistentVehiculo.getPropietario();
            Propietario propietarioNew = vehiculo.getPropietario();
            List<HojaPruebas> hojaPruebasListOld = persistentVehiculo.getHojaPruebasList();
            List<HojaPruebas> hojaPruebasListNew = vehiculo.getHojaPruebasList();
            if (propietarioNew != null) {
                propietarioNew = em.getReference(propietarioNew.getClass(), propietarioNew.getId());
                vehiculo.setPropietario(propietarioNew);
            }
            List<HojaPruebas> attachedHojaPruebasListNew = new ArrayList<HojaPruebas>();
            for (HojaPruebas hojaPruebasListNewHojaPruebasToAttach : hojaPruebasListNew) {
                if (hojaPruebasListNewHojaPruebasToAttach.getId() == null || hojaPruebasListNewHojaPruebasToAttach.getId() <= 0) {
                    break;
                }
                hojaPruebasListNewHojaPruebasToAttach = em.getReference(hojaPruebasListNewHojaPruebasToAttach.getClass(), hojaPruebasListNewHojaPruebasToAttach.getId());
                attachedHojaPruebasListNew.add(hojaPruebasListNewHojaPruebasToAttach);
            }
            hojaPruebasListNew = attachedHojaPruebasListNew;
            vehiculo.setHojaPruebasList(hojaPruebasListNew);
            vehiculo = em.merge(vehiculo);
            if (propietarioOld != null && !propietarioOld.equals(propietarioNew)) {
                propietarioOld.getVehiculoList().remove(vehiculo);
                propietarioOld = em.merge(propietarioOld);
            }
            if (propietarioNew != null && !propietarioNew.equals(propietarioOld)) {
                propietarioNew.getVehiculoList().add(vehiculo);
                propietarioNew = em.merge(propietarioNew);
            }
            for (HojaPruebas hojaPruebasListOldHojaPruebas : hojaPruebasListOld) {
                if (!hojaPruebasListNew.contains(hojaPruebasListOldHojaPruebas)) {
                    hojaPruebasListOldHojaPruebas.setVehiculo(null);
                    hojaPruebasListOldHojaPruebas = em.merge(hojaPruebasListOldHojaPruebas);
                }
            }
            for (HojaPruebas hojaPruebasListNewHojaPruebas : hojaPruebasListNew) {
                if (!hojaPruebasListOld.contains(hojaPruebasListNewHojaPruebas)) {
                    Vehiculo oldVehiculoOfHojaPruebasListNewHojaPruebas = hojaPruebasListNewHojaPruebas.getVehiculo();
                    hojaPruebasListNewHojaPruebas.setVehiculo(vehiculo);
                    hojaPruebasListNewHojaPruebas = em.merge(hojaPruebasListNewHojaPruebas);
                    if (oldVehiculoOfHojaPruebasListNewHojaPruebas != null && !oldVehiculoOfHojaPruebasListNewHojaPruebas.equals(vehiculo)) {
                        oldVehiculoOfHojaPruebasListNewHojaPruebas.getHojaPruebasList().remove(hojaPruebasListNewHojaPruebas);
                        oldVehiculoOfHojaPruebasListNewHojaPruebas = em.merge(oldVehiculoOfHojaPruebasListNewHojaPruebas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vehiculo.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The vehiculo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {

        }
    }

    public void edit(Vehiculo vehiculo) {
        try (Connection conn = UtilConexion.obtenerConexion()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            String sql = "UPDATE `vehiculos` "
                    + "SET "
                    + "`CARLINE` = '" + vehiculo.getLineaVehiculo().getId() + "', "
                    + "`CARMARK` = '" + vehiculo.getMarca().getId() + "', "
                    + "`Modelo` = '" + vehiculo.getModelo() + "', "
                    + "`diseño` = '" + vehiculo.getDiseno() + "', "
                    + "`Cinlindraje` = '" + vehiculo.getCilindraje() + "', "
                    + "`SERVICE` = '" + vehiculo.getServicios().getId() + "', "
                    + "`CLASS` = '" + vehiculo.getClaseVehiculo().getId() + "', "
                    + "`Numero_licencia` = '" + vehiculo.getLicencia() + "', "
                    + "`Numero_ejes` = '" + vehiculo.getEjes() + "', "
                    + "`CARTYPE` = '" + vehiculo.getTipoVehiculo().getId() + "', "
                    + "`GEUSER` = '" + vehiculo.getUsuario() + "', "
                    + "`CAROWNER` = '" + vehiculo.getPropietario().getId() + "', "
                    + "`Numero_exostos` = '" + vehiculo.getExostos() + "', "
                    + "`Diametro` = '" + vehiculo.getDiametro() + "', "
                    + "`FUELTYPE` = '" + vehiculo.getTipoGasolina().getId() + "', "
                    + "`Tiempos_motor` = '" + vehiculo.getTiemposMotor() + "', "
                    + "`Color` = '" + vehiculo.getColor().getId() + "', "
                    + "`Numero_SOAT` = '" + vehiculo.getNumeroSOAT() + "', "
                    + "`INSURING` = '" + vehiculo.getAseguradora().getId() + "', "
                    + "`Fecha_soat` = '" + sdf.format(vehiculo.getFechaSOAT()) + "', "
                    + "`Fecha_exp_soat` = '" + sdf.format(vehiculo.getFechaExpedicionSOAT()) + "', "
                    + "`WHEEL` = '" + vehiculo.getLlantas().getId() + "', "
                    + "`Nacionalidad` = '" + vehiculo.getNacionalidad() + "', "
                    + "`SPSERVICE` = '" + vehiculo.getServicioEspecial().getId() + "', "
                    + "`Numero_motor` = '" + vehiculo.getMotor() + "', "
                    + "`VIN` = '" + vehiculo.getVin() + "', "
                    + "`Fecha_registro` = '" + sdf.format(vehiculo.getFechaRegistro()) + "', "
                    + "`pais` = '" + vehiculo.getPais().getId() + "', "
                    + "`kilometraje` = '" + vehiculo.getKilometraje() + "', "
                    + "`numero_sillas` = '" + vehiculo.getSillas() + "', "
                    + "`vidrios_polarizados` = '" + vehiculo.getVidriosPolarizados() + "', "
                    + "`blindaje` = '" + vehiculo.getBlindaje() + "', "
                    + "`numero_chasis` = '" + vehiculo.getChasis() + "', "
                    + "`esEnsenaza` = '" + vehiculo.getEsEnsenaza() + "', "
                    + "`codigo_interno` = '" + vehiculo.getCodigoInterno() + "' "
                    + "WHERE `CAR` = '" + vehiculo.getId() + "'";

            //System.out.println("Method edit (VehiculosJPAController) SQL: " + sql);
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.executeUpdate(sql);
        } catch (Exception ex) {
            Mensajes.mostrarExcepcion(ex);
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vehiculo vehiculos;
            try {
                vehiculos = em.getReference(Vehiculo.class, id);
                vehiculos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vehiculos with id " + id + " no longer exists.", enfe);
            }

            Propietario propietarios = vehiculos.getPropietario();
            if (propietarios != null) {
                propietarios.getVehiculoList().remove(vehiculos);
                propietarios = em.merge(propietarios);
            }

            List<HojaPruebas> hojaPruebasList = vehiculos.getHojaPruebasList();
            for (HojaPruebas hojaPruebasListHojaPruebas : hojaPruebasList) {
                hojaPruebasListHojaPruebas.setVehiculo(null);
                hojaPruebasListHojaPruebas = em.merge(hojaPruebasListHojaPruebas);
            }

            em.remove(vehiculos);
            em.getTransaction().commit();
        } finally {

        }
    }

    public List<Vehiculo> findAll() {
        return findVehiculosEntities(true, -1, -1);
    }

    public List<Vehiculo> findVehiculosEntities(int maxResults, int firstResult) {
        return findVehiculosEntities(false, maxResults, firstResult);
    }

    private List<Vehiculo> findVehiculosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vehiculo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {

        }
    }

    public Vehiculo find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vehiculo.class, id);
        } finally {

        }
    }

    public Vehiculo findVehiculosByPlaca(String text) {
        EntityManager em = getEntityManager();
        em.clear();
        em.getTransaction().begin();
        em.flush();
        Vehiculo v = null;
        try {
            v = (Vehiculo) em.createQuery("SELECT v FROM Vehiculo v WHERE v.placa = :placa").setParameter("placa", text).getSingleResult();
        } catch (NoResultException ignored) {
        } finally {

        }
        em.getTransaction().commit();
        return v;
    }

}

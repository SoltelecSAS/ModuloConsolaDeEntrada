/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.entities.AuditoriaSicov;
import com.soltelec.consolaentrada.models.entities.Certificado;
import com.soltelec.consolaentrada.models.entities.ClaseVehiculo;
import com.soltelec.consolaentrada.models.entities.Color;
import com.soltelec.consolaentrada.models.entities.Defxprueba;
import com.soltelec.consolaentrada.models.entities.Diseno;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.LineaVehiculo;
import com.soltelec.consolaentrada.models.entities.Llanta;
import com.soltelec.consolaentrada.models.entities.Marca;
import com.soltelec.consolaentrada.models.entities.Pais;
import com.soltelec.consolaentrada.models.entities.Propietario;
import com.soltelec.consolaentrada.models.entities.Prueba;
import com.soltelec.consolaentrada.models.entities.PruebaDTO;
import com.soltelec.consolaentrada.models.entities.Reinspeccion;
import com.soltelec.consolaentrada.models.entities.Servicio;
import com.soltelec.consolaentrada.models.entities.ServicioEspecial;
import com.soltelec.consolaentrada.models.entities.TipoGasolina;
import com.soltelec.consolaentrada.models.entities.TipoVehiculo;
import com.soltelec.consolaentrada.models.entities.Vehiculo;
import com.soltelec.consolaentrada.models.statics.LoggedUser;
import com.soltelec.consolaentrada.utilities.Mensajes;
import com.soltelec.consolaentrada.utilities.UtilConexion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Gerencia TIC
 */
public class HojaPruebasJpaController {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(HojaPruebas hojaPruebas) throws Exception 
    {
        if (hojaPruebas.getListPruebas() == null) {
            hojaPruebas.setListPruebas(new ArrayList<Prueba>());
        }
        EntityManager em = null;
        em = getEntityManager();
        if (em.getTransaction().isActive() == true) {
            em.flush();
            em.clear();
           
        } else {
            em.clear();
            em.getTransaction().begin();
        }
        if (BuscarMaximo_hojapruebas() == null) {
            hojaPruebas.setCon_hoja_prueba(1);
        } else {
            hojaPruebas.setCon_hoja_prueba((BuscarMaximo_hojapruebas()) + 1);
        }
        if (hojaPruebas.getPropietario() != null) 
        {
            Propietario ctxPropietario = em.find(hojaPruebas.getPropietario().getClass(), hojaPruebas.getPropietario().getId());
            if (ctxPropietario != null) {
                ctxPropietario.setTipoIdentificacion(hojaPruebas.getPropietario().getTipoIdentificacion());
                ctxPropietario.setNombres(hojaPruebas.getPropietario().getNombres());
                ctxPropietario.setApellidos(hojaPruebas.getPropietario().getApellidos());
                ctxPropietario.setDireccion(hojaPruebas.getPropietario().getDireccion());
                ctxPropietario.setTelefono(hojaPruebas.getPropietario().getTelefono());
                ctxPropietario.setCelular(hojaPruebas.getPropietario().getCelular());
                ctxPropietario.setEmail(hojaPruebas.getPropietario().getEmail());
                ctxPropietario.setCiudad(hojaPruebas.getPropietario().getCiudad());
                ctxPropietario.setTipolicencia(hojaPruebas.getPropietario().getTipolicencia());
                ctxPropietario.setLicencia(hojaPruebas.getPropietario().getLicencia());
                hojaPruebas.getVehiculo().setPropietario(ctxPropietario);
                hojaPruebas.setPropietario(ctxPropietario);
            } else {
                hojaPruebas.getVehiculo().setPropietario(hojaPruebas.getPropietario());
            }
        } else {
            hojaPruebas.getVehiculo().setPropietario(hojaPruebas.getPropietario());
        }
        if (hojaPruebas.getConductor().getId() != null) {
            Propietario ctxConductor = em.find(hojaPruebas.getConductor().getClass(), hojaPruebas.getConductor().getId());
            if (ctxConductor != null) {
                ctxConductor.setTipoIdentificacion(hojaPruebas.getConductor().getTipoIdentificacion());
                ctxConductor.setNombres(hojaPruebas.getConductor().getNombres());
                ctxConductor.setApellidos(hojaPruebas.getConductor().getApellidos());
                ctxConductor.setDireccion(hojaPruebas.getConductor().getDireccion());
                ctxConductor.setTelefono(hojaPruebas.getConductor().getTelefono());
                ctxConductor.setCelular(hojaPruebas.getConductor().getCelular());
                ctxConductor.setEmail(hojaPruebas.getConductor().getEmail());
                ctxConductor.setCiudad(hojaPruebas.getConductor().getCiudad());
                ctxConductor.setTipolicencia(hojaPruebas.getConductor().getTipolicencia());
                ctxConductor.setLicencia(hojaPruebas.getConductor().getLicencia());
                hojaPruebas.setConductor(ctxConductor);
            }
        }
        if (hojaPruebas.getVehiculo().getId() != null) 
        {
            Vehiculo ctxVehiculo = em.find(hojaPruebas.getVehiculo().getClass(), hojaPruebas.getVehiculo().getId());
            if (ctxVehiculo != null) 
            {
                ctxVehiculo.setAseguradora(hojaPruebas.getVehiculo().getAseguradora());
                ctxVehiculo.setFechaSOAT(hojaPruebas.getVehiculo().getFechaSOAT());
                ctxVehiculo.setFechaExpedicionSOAT(hojaPruebas.getVehiculo().getFechaExpedicionSOAT());
                ctxVehiculo.setNumeroSOAT(hojaPruebas.getVehiculo().getNumeroSOAT());
                ctxVehiculo.setCilindraje(hojaPruebas.getVehiculo().getCilindraje());
                ctxVehiculo.setClaseVehiculo(hojaPruebas.getVehiculo().getClaseVehiculo());
                ctxVehiculo.setColor(hojaPruebas.getVehiculo().getColor());
//              ctxVehiculo.setDiametro(430); ///???   
                ctxVehiculo.setFechaRegistro(hojaPruebas.getVehiculo().getFechaRegistro());
                ctxVehiculo.setLineaVehiculo(hojaPruebas.getVehiculo().getLineaVehiculo());
                ctxVehiculo.setLlantas(hojaPruebas.getVehiculo().getLlantas());
                ctxVehiculo.setMarca(hojaPruebas.getVehiculo().getMarca());
                ctxVehiculo.setModelo(hojaPruebas.getVehiculo().getModelo());
                ctxVehiculo.setNacionalidad(hojaPruebas.getVehiculo().getNacionalidad());
                ctxVehiculo.setEjes(hojaPruebas.getVehiculo().getEjes());
                ctxVehiculo.setExostos(hojaPruebas.getVehiculo().getExostos());
                ctxVehiculo.setMotor(hojaPruebas.getVehiculo().getMotor());
                ctxVehiculo.setLicencia(hojaPruebas.getVehiculo().getLicencia());
                ctxVehiculo.setVin(hojaPruebas.getVehiculo().getVin());
                ctxVehiculo.setServicios(hojaPruebas.getVehiculo().getServicios());
                ctxVehiculo.setServicioEspecial(hojaPruebas.getVehiculo().getServicioEspecial());
                ctxVehiculo.setTiemposMotor(hojaPruebas.getVehiculo().getTiemposMotor());
                ctxVehiculo.setTipoVehiculo(hojaPruebas.getVehiculo().getTipoVehiculo());
                ctxVehiculo.setTipoGasolina(hojaPruebas.getVehiculo().getTipoGasolina());
                ctxVehiculo.setPais(hojaPruebas.getVehiculo().getPais());
                ctxVehiculo.setKilometraje(hojaPruebas.getVehiculo().getKilometraje());
                ctxVehiculo.setSillas(hojaPruebas.getVehiculo().getSillas());
                ctxVehiculo.setBlindaje(hojaPruebas.getVehiculo().getBlindaje());
                ctxVehiculo.setVidriosPolarizados(hojaPruebas.getVehiculo().getVidriosPolarizados());
                ctxVehiculo.setChasis(hojaPruebas.getVehiculo().getChasis());
                ctxVehiculo.setCodigoInterno(hojaPruebas.getVehiculo().getCodigoInterno());//Obtener Codigo interno      
                ctxVehiculo.setUsuario(hojaPruebas.getVehiculo().getUsuario());
                ctxVehiculo.setDiseno(hojaPruebas.getVehiculo().getDiseno());
                ctxVehiculo.setEsEnsenaza(hojaPruebas.getVehiculo().getEsEnsenaza());
                ctxVehiculo.setPotencia(hojaPruebas.getVehiculo().getPotencia());
                hojaPruebas.setVehiculo(ctxVehiculo);
            }
        }
        if (hojaPruebas.getVehiculo().getId() != null) {//si no es un carro nuevo
            if (em.find(hojaPruebas.getVehiculo().getClass(), hojaPruebas.getVehiculo().getId()) != null) {//si se encuentra en la bd
                hojaPruebas.setVehiculo(em.find(hojaPruebas.getVehiculo().getClass(), hojaPruebas.getVehiculo().getId()));
            }
        } else {//vehiculo nuevo
            List<HojaPruebas> arrayList = new ArrayList<>();
            arrayList.add(hojaPruebas);
            hojaPruebas.getVehiculo().setHojaPruebasList(arrayList);
        }
        em.persist(hojaPruebas);//ahora la hoja de pruebas queda en estado managed o administrada, los cambios solo se reflejan cuando la transaccion se confirme
        em.getTransaction().commit();
        
        System.out.println("*---------------------");
        System.out.println("*-- MODIFICACION------");
        System.out.println("*---------------------");
        int kilometraje=hojaPruebas.getVehiculo().getKilometraje();
        String placa=hojaPruebas.getVehiculo().getPlaca();
        logicaInsertKilometraje(placa,kilometraje);

        
    }

    private void logicaInsertKilometraje(String placa,int kilometraje)
    {
        System.out.println("----------------------------------");
        System.out.println("---- logicaInsertKilometraje------");
        System.out.println("----------------------------------");
        
        try 
        {
            Connection cn = UtilConexion.obtenerConexion();
            int idVehiculo=buscarHojaPruebas(placa,cn);
            int idhHojaPrueba=consultarHojaPruebas(idVehiculo,cn);
            int idHojaPrueba=obtenerIdPruebaVisual(idhHojaPrueba,cn);
            if (!consultarTipoMedidaKilometraje(cn)) 
            {
               insertarTipoMedidaKilometraje(cn);  
            }
            insertarKilometraje(idHojaPrueba, cn,kilometraje);
        } catch (Exception e) {
            System.out.println("Error en el metodo : logicaInsertKilometraje()" + e);
        }
    }
    
    private int buscarHojaPruebas(String placa,Connection cn)
    {
        System.out.println("----------------------------------");
        System.out.println("----  buscarHojaPruebas  ------");
        System.out.println("----------------------------------");
        try 
        {
            String consulta = "SELECT v.CAR AS codigo FROM vehiculos v WHERE v.CARPLATE=?";
            PreparedStatement sentencia = cn.prepareStatement(consulta);
            sentencia.setString(1,placa);
            ResultSet rs = sentencia.executeQuery();
            
            while (rs.next()) 
            {
               return  rs.getInt("codigo");
            }
        } catch (Exception e) 
        {
            System.out.println("Erro en el metodo : buscarHojaPruebas()" + e); 
        }
        return 0;
    }
    
    private int consultarHojaPruebas(int idVehiculo, Connection cn)
    {
        System.out.println("----------------------------------");
        System.out.println("--     consultarHojaPruebas   ----");
        System.out.println("----------------------------------");
        try 
        {
            String consulta = "SELECT max(hp.TESTSHEET) AS idhp FROM hoja_pruebas hp WHERE hp.Vehiculo_for=?";
            PreparedStatement sentencia = cn.prepareStatement(consulta);
            sentencia.setInt(1, idVehiculo);
            ResultSet rs = sentencia.executeQuery();
            while (rs.next())
            {
               return rs.getInt("idhp");
            }
        } catch (Exception e) 
        {
            System.out.println("Error en le metodo:consultarHojaPruebas()");
        }
        return 0;
    }
    
    public static int obtenerIdPruebaVisual(int idHojaPruebas,Connection cn)
    {
        System.out.println("----------------------------------");
        System.out.println("---- obtenerIdPruebaVisual  ------");
        System.out.println("----------------------------------");
        try 
        {
            String consulta = "SELECT max(p.Id_Pruebas)  as idPruebas FROM pruebas p WHERE p.hoja_pruebas_for=? AND p.Tipo_prueba_for=1";
            PreparedStatement sentencia = cn.prepareStatement(consulta);
            sentencia.setInt(1,idHojaPruebas);
            ResultSet rs = sentencia.executeQuery();
            
            while (rs.next())
            {
               return  rs.getInt("idPruebas");
            }
        } catch (Exception e) 
        {
            System.out.println("Error en el metodo : obtenerIdPruebaVisual()" + e); 
        }
        return 0;
    }
      
    private boolean consultarTipoMedidaKilometraje(Connection cn) 
    {
        System.out.println("----------------------------------");
        System.out.println("-- consultarMedidaKilometraje ----");
        System.out.println("----------------------------------");
        try {
            String consulta = "SELECT * FROM tipos_medida tm WHERE tm.MEASURETYPE=1006";
            PreparedStatement sentencia = cn.prepareStatement(consulta);
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error en le metodo:consultarMedidaKilometraje()");
        }
        return false;
    }
   
    private void insertarTipoMedidaKilometraje(Connection cn)
    {
        System.out.println("----------------------------------");
        System.out.println("--insertarTipoMedidaKilometraje --");
        System.out.println("----------------------------------");
        try 
        {
            String query="INSERT INTO tipos_medida (MEASURETYPE,TESTTYPE,Nombre_medida,Descripcion_medida,Unidad) VALUES (?,?,?,?,?)";
            PreparedStatement  st = cn.prepareStatement(query);
            st.setInt(1, 1006);
            st.setInt(2,6);
            st.setString(3,"KILOMETRAJE");
            st.setString(4,"TIPO MEDIDA PARA KILOMETRAJE");
            st.setString(5,"KM");
            st.executeUpdate();
            System.out.println("Tipo de Medida " + 1006 + " insertada correctamente");
        } catch (Exception e) {
            System.out.println("Error en el metodo: insertarMedidasKilometraje()"+e);
        }
    }
    
    public static void insertarKilometraje(int idPrueba,Connection cn,float kilometraje)
    {        
        System.out.println("----------------------------------");
        System.out.println("--        insertarKilometraje   --");
        System.out.println("----------------------------------");
        
        try 
        {
            String query="INSERT INTO medidas (MEASURETYPE,Valor_medida,TEST,Condicion,Simult) VALUES (?,?,?,?,?)";
            PreparedStatement  st = cn.prepareStatement(query);
            st.setInt(1,1006);
            st.setFloat(2,kilometraje);
            st.setInt(3,idPrueba);
            st.setString(4,"K");
            st.setString(5,"");
            st.executeUpdate();
            System.out.println("Se inserto el kilometraje : " + kilometraje + " correctamente");
        } catch (Exception e) {
            System.out.println("Error en el metodo: insertarKilometraje()"+e);
        }
    }
    
    private void actualizarKilometraje(String kilometraje,int idHojaPrueba) 
    {
        System.out.println("----------------------------------");
        System.out.println("--     actualizarKilometraje   --");
        System.out.println("----------------------------------");
        try {
            Connection cn = UtilConexion.obtenerConexion();
            int idprueba = HojaPruebasJpaController.obtenerIdPruebaVisual(idHojaPrueba, cn);
            int kilometrajeConsultado=consultarMedida(idprueba, cn);
            if (kilometrajeConsultado == 0) 
            {
                HojaPruebasJpaController.insertarKilometraje(idprueba, cn,Float.parseFloat(kilometraje));
            }else{
                HojaPruebasJpaController.actualizarKilometraje(idprueba, cn, kilometraje);
            }

        } catch (Exception e) {
            System.out.println("Error en el metodo : actualizarKilometraje()" + e);
        }
    }
    
    
    public static void actualizarKilometraje(int idPrueba, Connection cn, String kilometraje) 
    {
        System.out.println("----------------------------------");
        System.out.println("--        actualizarKilometraje   --");
        System.out.println("----------------------------------");

        try {
            String query = "UPDATE medidas m SET m.Valor_medida=? where m.MEASURETYPE=1006 AND m.TEST =?";
            PreparedStatement st = cn.prepareStatement(query);
            st.setString(1, kilometraje);
            st.setInt(2, idPrueba);
            st.executeUpdate();
            System.out.println("Se actualiazo el kilometraje : " + kilometraje + " correctamente");
        } catch (Exception e) {
            System.out.println("Error en el metodo: actualizarKilometraje()" + e);
        }
    }
    
    public static int consultarMedida(int idPrueba,Connection cn)
    {
        System.out.println("----------------------------------");
        System.out.println("--        consultarMedida     ----");
        System.out.println("----------------------------------");
        try 
        {
            String consulta = 
            "SELECT v.kilometraje FROM pruebas p\n" + //
            "INNER JOIN hoja_pruebas hp ON hp.TESTSHEET = p.hoja_pruebas_for\n" + //
            "INNER JOIN vehiculos v ON v.CAR = hp.Vehiculo_for\n" + //
            "WHERE p.Id_Pruebas = ?;";
            PreparedStatement sentencia = cn.prepareStatement(consulta);
            sentencia.setInt(1, idPrueba);
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) 
            {
                if (rs.getString("kilometraje")!=null)
                {
                    return rs.getInt("kilometraje");
                }
            }
        } catch (Exception e) 
        {
            System.out.println("Error en le metodo:consultarMedida()" + e);
        }
        return 0;
    }

    public List<AuditoriaSicov> recogerTramasExist(HojaPruebas ctxHojaPrueba) {
        EntityManager em = null;
        em = getEntityManager();
        Query q = em.createQuery("SELECT a FROM AuditoriaSicov a WHERE a.idRevision= :idHp  ");
        q.setParameter("idHp", ctxHojaPrueba.getId());
        List<AuditoriaSicov> lstEvSicov = q.getResultList();
        return lstEvSicov;
    }

    public void cambiarRTMPreventiva(HojaPruebas ctxHojaPrueba, String nombre, String funcion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            if (em.getTransaction().isActive() == true) {
                em.flush();
                em.clear();
            } else {
                em.getTransaction().begin();
                em.clear();
            }
            HojaPruebas hpBd = em.find(HojaPruebas.class, ctxHojaPrueba.getId());
            if (funcion.equalsIgnoreCase("CambioModalidad")) {
                if (ctxHojaPrueba.getPreventiva().equalsIgnoreCase("Y")) {
                    TypedQuery<Integer> query = em.createQuery("SELECT Max(h.con_hoja_prueba) FROM HojaPruebas h ", Integer.class);
                    Integer conRTM = query.getSingleResult();
                    hpBd.setCon_hoja_prueba(conRTM + 1);
                    hpBd.setCon_preventiva(0);
                    hpBd.setPreventiva("N");
                    hpBd.setEstadoSICOV("INICIADO");
                    if (hpBd.getComentario() != null) {
                        hpBd.setComentario("CAMBIO A RTM POR ".concat(nombre).concat(";").concat(hpBd.getComentario()));
                    } else {
                        hpBd.setComentario("CAMBIO A RTM POR ".concat(nombre).concat(";"));
                    }
                    if (ctxHojaPrueba.getPin() != null) {
                        hpBd.setPin(ctxHojaPrueba.getPin());
                    }
                } else {
                    TypedQuery<Integer> query = em.createQuery("SELECT Max(h.con_preventiva) FROM HojaPruebas h ", Integer.class);
                    Integer conRTM = query.getSingleResult();
                    hpBd.setCon_hoja_prueba(0);
                    hpBd.setCon_preventiva(conRTM + 1);
                    hpBd.setPreventiva("Y");
                    hpBd.setEstadoSICOV("NO_APLICA");
                    if (hpBd.getComentario() != null) {
                        hpBd.setComentario("CAMBIO A PREVENTIVA POR ".concat(nombre).concat(";").concat(hpBd.getComentario()));
                    } else {
                        hpBd.setComentario("CAMBIO A PREVENTIVA POR ".concat(nombre).concat(";"));
                    }
                }
                 hpBd.setFinalizada("N");
            }
            if (funcion.equalsIgnoreCase("CambioPin")) {
                if (ctxHojaPrueba.getPin() != null) {
                    hpBd.setPin(ctxHojaPrueba.getPin());
                }
            }           
            em.merge(hpBd);
            em.getTransaction().commit();
        } catch (Exception ex) {

        } finally {

        }
    }

    /* public String verificarHojaFinalizada(HojaPruebas ctxHojaPrueba) {
        List<Prueba> ctxListPrueba = new ArrayList();
        Integer ctxIdPruebaVis=0;
        if (ctxHojaPrueba.getReinspeccionList().size() == 0) {
            List<Prueba> tempListPrueba = ctxHojaPrueba.getListPruebas();
            for (Prueba ctxpru : tempListPrueba) {
                if (ctxpru.getAbortado().equalsIgnoreCase("N")) {
                    ctxListPrueba.add(ctxpru);
                    if(ctxpru.getTipoPrueba().getId()==1){
                       ctxIdPruebaVis = ctxpru.getId();
                    }
                }
            }
        } else {
            Reinspeccion r = ctxHojaPrueba.getReinspeccionList().get(0);
            List<Prueba> tempListPrueba = ctxHojaPrueba.getListPruebas();
            Boolean encontrado = false;
            for (Prueba ctxpru : tempListPrueba) {
                for (Prueba prReins : r.getPruebaList()) {
                    if (ctxpru.getTipoPrueba().getId() == prReins.getTipoPrueba().getId()) {
                        encontrado = true;
                        break;
                    }
                }
                if (encontrado == false) {
                    if (ctxpru.getAbortado().equalsIgnoreCase("N")) {
                        ctxListPrueba.add(ctxpru);
                    }
                }
                encontrado = false;
            }
            for (Prueba prReins : r.getPruebaList()) {
                if (prReins.getAbortado().equalsIgnoreCase("N")) {
                    ctxListPrueba.add(prReins);
                }
                if(prReins.getTipoPrueba().getId()==1){
                       ctxIdPruebaVis = prReins.getId();
                }
            }
        }
        int contPruebasFinalizadas = 0;
        int contPruebasAprobadas = 0;
        int contPruebasReprobadas = 0;
        int contTotalPruebas = 0;
        int contTestB = 0;
        int contTestA = 0;
        contTotalPruebas = ctxListPrueba.size();

        for (Prueba ctxpru : ctxListPrueba) {
            if (ctxpru.getAprobado().equals("Y") && (!ctxpru.getAbortado().equalsIgnoreCase("A")) && (!ctxpru.getAbortado().equalsIgnoreCase("Y")) && ctxpru.getFinalizada().equals("Y")) {
                contPruebasAprobadas++;
            }
            if (ctxpru.getAprobado().equals("N") && (!ctxpru.getAbortado().equalsIgnoreCase("A")) && (!ctxpru.getAbortado().equalsIgnoreCase("Y")) && ctxpru.getFinalizada().equals("Y")) {
                contPruebasReprobadas++;
            }
            if (ctxpru.getFinalizada().equals("Y") && ctxpru.getAbortado().equalsIgnoreCase("N")) {
                if (ctxpru.getDefxpruebaList().size() > 0) {
                    List<Defxprueba> ctxListDefxprueba = ctxpru.getDefxpruebaList();
                    for (Defxprueba ctxDef : ctxListDefxprueba) {
                        if(ctxDef.getTercerEstado() != null && ctxDef.getTercerEstado().equals("na")){
                            continue;
                        }
                        if (ctxDef.getDefectos().getTipodefecto().equalsIgnoreCase("A")) {
                            contTestA++;
                        } else {
                            contTestB++;
                        }
                    }
                }
            }
        }// fin del ciclo de recorrido de las pruebas reales
        System.out.println("Cuantas pruebas Hay ?"+contTotalPruebas);
        
         System.out.println("Cuantas pruebas Reprobadas ?"+contPruebasReprobadas);
         System.out.println("Cuantas pruebas Aprobadas ?"+contPruebasAprobadas);
        contPruebasFinalizadas = contPruebasReprobadas + contPruebasAprobadas;
        String desicion = "";
        if (contTotalPruebas == contPruebasFinalizadas) {
            if (contTotalPruebas == contPruebasAprobadas) {
                desicion = "APROBADA";
            } else {
                desicion = "REPROBADA";
            }
        } else {
            desicion = "PENDIENTE";
        }
        if (!desicion.equalsIgnoreCase("PENDIENTE")) {
            if (ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 1 || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 2 || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 3 || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 109 || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 110) {
                if (ctxHojaPrueba.getVehiculo().getServicios().getId() == 3 || ctxHojaPrueba.getVehiculo().getServicios().getId() == 1 || ctxHojaPrueba.getVehiculo().getServicios().getId() == 4) {
                    if (contTestB > 9) {
                        desicion = "REPROBADA";
                        servRechazoPruebaVisual(ctxIdPruebaVis);
                    }
                    if (contTestA > 0) {
                        desicion = "REPROBADA";
                    }
                }
                if (ctxHojaPrueba.getVehiculo().getServicios().getId() == 2 || ctxHojaPrueba.getVehiculo().getEsEnsenaza() == 1) {
                    if (contTestB > 4) {
                        desicion = "REPROBADA";
                        servRechazoPruebaVisual(ctxIdPruebaVis);
                    }
                    if (contTestA > 0) {
                        desicion = "REPROBADA";
                    }
                }
            }
            if (ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 4) {
                if (contTestB > 4) {
                    desicion = "REPROBADA";
                    servRechazoPruebaVisual(ctxIdPruebaVis);
                }
                if (contTestA > 0) {
                    desicion = "REPROBADA";
                }
            }
            if (ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 5) {
                if (ctxHojaPrueba.getVehiculo().getServicios().getId() == 3 || ctxHojaPrueba.getVehiculo().getServicios().getId() == 1 || ctxHojaPrueba.getVehiculo().getServicios().getId() == 4) {
                    if (contTestB > 6) {
                        desicion = "REPROBADA";
                        servRechazoPruebaVisual(ctxIdPruebaVis);
                    }
                    if (contTestA > 0) {
                        desicion = "REPROBADA";
                    }
                }
                if (ctxHojaPrueba.getVehiculo().getServicios().getId() == 2 || ctxHojaPrueba.getVehiculo().getEsEnsenaza() == 1) {
                    if (contTestB > 4) {
                        desicion = "REPROBADA";
                        servRechazoPruebaVisual(ctxIdPruebaVis);
                    }
                    if (contTestA > 0) {
                        desicion = "REPROBADA";
                    }
                }
            }
        }
        return desicion;
    } */

    public String verificarHojaFinalizada(HojaPruebas ctxHojaPrueba) {
        List<Prueba> ctxListPrueba = new ArrayList();
        Integer ctxIdPruebaVis=0;
        if (ctxHojaPrueba.getReinspeccionList().size() == 0) {
            List<Prueba> tempListPrueba = ctxHojaPrueba.getListPruebas();
            for (Prueba ctxpru : tempListPrueba) {
                if (ctxpru.getAbortado().equalsIgnoreCase("N")) {
                    ctxListPrueba.add(ctxpru);
                    if(ctxpru.getTipoPrueba().getId()==1){
                       ctxIdPruebaVis = ctxpru.getId();
                    }
                }
            }
        } else {
            Reinspeccion r = ctxHojaPrueba.getReinspeccionList().get(0);
            List<Prueba> tempListPrueba = ctxHojaPrueba.getListPruebas();
            Boolean encontrado = false;
            for (Prueba ctxpru : tempListPrueba) {
                for (Prueba prReins : r.getPruebaList()) {
                    if (ctxpru.getTipoPrueba().getId() == prReins.getTipoPrueba().getId()) {
                        encontrado = true;
                        break;
                    }
                }
                if (encontrado == false) {
                    if (ctxpru.getAbortado().equalsIgnoreCase("N")) {
                        ctxListPrueba.add(ctxpru);
                    }
                }
                encontrado = false;
            }
            for (Prueba prReins : r.getPruebaList()) {
                if (prReins.getAbortado().equalsIgnoreCase("N")) {
                    ctxListPrueba.add(prReins);
                }
                if(prReins.getTipoPrueba().getId()==1){
                       ctxIdPruebaVis = prReins.getId();
                }
            }
        }
        int contPruebasFinalizadas = 0;
        int contPruebasAprobadas = 0;
        int contPruebasReprobadas = 0;
        int contTotalPruebas = 0;
        int contTestB = 0;
        int contTestA = 0;
        contTotalPruebas = ctxListPrueba.size();

        for (Prueba ctxpru : ctxListPrueba) {
            if (ctxpru.getAprobado().equals("Y") && (!ctxpru.getAbortado().equalsIgnoreCase("A")) && (!ctxpru.getAbortado().equalsIgnoreCase("Y")) && ctxpru.getFinalizada().equals("Y")) {
                contPruebasAprobadas++;
            }
            if (ctxpru.getAprobado().equals("N") && (!ctxpru.getAbortado().equalsIgnoreCase("A")) && (!ctxpru.getAbortado().equalsIgnoreCase("Y")) && ctxpru.getFinalizada().equals("Y")) {
                contPruebasReprobadas++;
            }
            if (ctxpru.getFinalizada().equals("Y") && ctxpru.getAbortado().equalsIgnoreCase("N")) {
                if (ctxpru.getDefxpruebaList().size() > 0) {
                    List<Defxprueba> ctxListDefxprueba = ctxpru.getDefxpruebaList();
                    for (Defxprueba ctxDef : ctxListDefxprueba) {
                        if(ctxDef.getTercerEstado() != null && ctxDef.getTercerEstado().equals("na")){
                            continue;
                        }
                        if (ctxDef.getDefectos().getTipodefecto().equalsIgnoreCase("A")) {
                            contTestA++;
                        } else {
                            contTestB++;
                        }
                    }
                }
            }
        }// fin del ciclo de recorrido de las pruebas reales
        System.out.println("Cuantas pruebas Hay ?"+contTotalPruebas);
        
         System.out.println("Cuantas pruebas Reprobadas ?"+contPruebasReprobadas);
         System.out.println("Cuantas pruebas Aprobadas ?"+contPruebasAprobadas);
        contPruebasFinalizadas = contPruebasReprobadas + contPruebasAprobadas;
        String desicion = "";
        if (contTotalPruebas == contPruebasFinalizadas) {
            if (contTotalPruebas == contPruebasAprobadas) {
                desicion = "APROBADA";
            } else {
                desicion = "REPROBADA";
            }
        } else {
            desicion = "PENDIENTE";
        }
        if (!desicion.equalsIgnoreCase("PENDIENTE")) {
            if (ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 1 || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 2 || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 3 || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 109 || ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 110) {
                if (ctxHojaPrueba.getVehiculo().getServicios().getId() == 3 || ctxHojaPrueba.getVehiculo().getServicios().getId() == 1 || ctxHojaPrueba.getVehiculo().getServicios().getId() == 4) {
                    if (contTestB > 9) {
                        desicion = "REPROBADA";
                        servRechazoPruebaVisual(ctxIdPruebaVis);
                    }
                    if (contTestA > 0) {
                        desicion = "REPROBADA";
                    }
                }
                if (ctxHojaPrueba.getVehiculo().getServicios().getId() == 2 || ctxHojaPrueba.getVehiculo().getEsEnsenaza() == 1) {
                    if (contTestB > 4) {
                        desicion = "REPROBADA";
                        servRechazoPruebaVisual(ctxIdPruebaVis);
                    }
                    if (contTestA > 0) {
                        desicion = "REPROBADA";
                    }
                }
            }
            if (ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 4) {
                if (contTestB > 4) {
                    desicion = "REPROBADA";
                    servRechazoPruebaVisual(ctxIdPruebaVis);
                }
                if (contTestA > 0) {
                    desicion = "REPROBADA";
                }
            }
            if (ctxHojaPrueba.getVehiculo().getTipoVehiculo().getId() == 5) {
                if (ctxHojaPrueba.getVehiculo().getServicios().getId() == 3 || ctxHojaPrueba.getVehiculo().getServicios().getId() == 1 || ctxHojaPrueba.getVehiculo().getServicios().getId() == 4) {
                    if (contTestB > 6) {
                        desicion = "REPROBADA";
                        servRechazoPruebaVisual(ctxIdPruebaVis);
                    }
                    if (contTestA > 0) {
                        desicion = "REPROBADA";
                    }
                }
                if (ctxHojaPrueba.getVehiculo().getServicios().getId() == 2 || ctxHojaPrueba.getVehiculo().getEsEnsenaza() == 1) {
                    if (contTestB > 4) {
                        desicion = "REPROBADA";
                        servRechazoPruebaVisual(ctxIdPruebaVis);
                    }
                    if (contTestA > 0) {
                        desicion = "REPROBADA";
                    }
                }
            }
        }
        /* for (Prueba ctxpru : ctxListPrueba) {
         JOptionPane.showMessageDialog(null, " ID  " + ctxpru.getId() + ", de la Prueba .."+ctxpru.getTipoPrueba().getNombre());
         }
         JOptionPane.showMessageDialog(null, " Desicion es  " + desicion );*/
        return desicion;
    }
    
    // Submétodo auxiliar para agregar pruebas no encontradas en reinspreción
    private void agregarPruebasNoEncontradas(List<Prueba> ctxListPrueba, List<Prueba> tempListPrueba, Reinspeccion r) {
        for (Prueba ctxpru : tempListPrueba) {
            boolean encontrado = r.getPruebaList().stream().anyMatch(prReins -> prReins.getTipoPrueba().getId() == ctxpru.getTipoPrueba().getId());
            if (!encontrado && ctxpru.getAbortado().equalsIgnoreCase("N")) {
                ctxListPrueba.add(ctxpru);
            }
        }
    }
    
    // Submétodo auxiliar para agregar pruebas de reinspreción
    private void agregarPruebasReinspeccion(List<Prueba> ctxListPrueba, Reinspeccion r) {
        for (Prueba prReins : r.getPruebaList()) {
            if (prReins.getAbortado().equalsIgnoreCase("N")) {
                ctxListPrueba.add(prReins);
            }
        }
    }
   
    public void servRechazoPruebaVisual(Integer ctxIdPruebaVis){
       EntityManager em = null;
       try {
           em = getEntityManager();
           em.getTransaction().begin();
           Prueba ctxP = em.find(Prueba.class, ctxIdPruebaVis);
           ctxP.setAprobado("N");
           em.merge(ctxP);
           em.getTransaction().commit();           
       } catch (Exception ex) {
       }       
   }
    
    public Object[] removeDuplicates(Object[] A) {
        if (A.length < 2) {
            return A;
        }
        int j = 0;
        int i = 1;
        while (i < A.length) {
            if (A[i].equals(A[j])) {
                i++;
            } else {
                j++;
                A[j] = A[i];
                i++;
            }
        }
        Object[] B = Arrays.copyOf(A, j + 1);
        return B;
    }

    public void update(HojaPruebas hojaPruebas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HojaPruebas ctxHP = em.find(HojaPruebas.class, hojaPruebas.getId());
            ctxHP.setEstadoSICOV(hojaPruebas.getEstadoSICOV());
            ctxHP.setConsecutivoRunt(hojaPruebas.getConsecutivoRunt());
            ctxHP.setCertificados(hojaPruebas.getCertificados());
            em.merge(ctxHP);
            em.getTransaction().commit();
        } catch (Exception ex) {
        }
    }

    public void edit(HojaPruebas hojaPruebas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            if (em.getTransaction().isActive() == true) {
                em.flush();
                em.clear();
            } else {
                em.getTransaction().begin();
                em.clear();
            }
            HojaPruebas hpBd = em.find(HojaPruebas.class, hojaPruebas.getId());
            hpBd.setVehiculo(hojaPruebas.getVehiculo());
            hpBd.getVehiculo().setPropietario(hojaPruebas.getPropietario());
            hpBd.setPropietario(hojaPruebas.getPropietario());
            hpBd.setConductor(hojaPruebas.getConductor());
            hpBd.setIntentos(hojaPruebas.getIntentos());
            hpBd.setResponsable(hojaPruebas.getResponsable());
            hpBd = em.find(HojaPruebas.class, hojaPruebas.getId());
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            hpBd.setListPruebas(hojaPruebas.getListPruebas());
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            hpBd.setAprobado(hojaPruebas.getAprobado());
            hpBd.setFinalizada(hojaPruebas.getFinalizada());
            hpBd.setEstado(hojaPruebas.getEstado());
            hpBd.setEstadoSICOV(hojaPruebas.getEstadoSICOV());
            hojaPruebas = em.merge(hpBd);
            em.getTransaction().commit();
            String kilometraje=hojaPruebas.getVehiculo().getKilometraje().toString();
            if (kilometraje.equalsIgnoreCase("NO FUNCIONAL"))
            {
                kilometraje="0";
                actualizarKilometraje(kilometraje,hojaPruebas.getId());
            }else{
                actualizarKilometraje(kilometraje,hojaPruebas.getId());
            }

        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = hojaPruebas.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The hojaPruebas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {

        }
    }
    
    public void editSingle(HojaPruebas hojaPruebas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            if (em.getTransaction().isActive() == true) {
                em.flush();
                em.clear();
            } else {
                em.getTransaction().begin();
                em.clear();
            }
            HojaPruebas hpBd = em.find(HojaPruebas.class, hojaPruebas.getId());
            hpBd.setListPruebas(hojaPruebas.getListPruebas());
            hpBd.setAprobado(hojaPruebas.getAprobado());
            hpBd.setFinalizada(hojaPruebas.getFinalizada());
            hpBd.setEstado(hojaPruebas.getEstado());
            hpBd.setEstadoSICOV(hojaPruebas.getEstadoSICOV());
            hpBd.setAnulado(hojaPruebas.getAnulado());
            hojaPruebas = em.merge(hpBd);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = hojaPruebas.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The hojaPruebas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {

        }
    }

    public void editCasoAddPin(HojaPruebas hojaPruebas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            if (em.getTransaction().isActive() == true) {
                em.flush();
                em.clear();
            } else {
                em.getTransaction().begin();
                em.clear();
            }
            HojaPruebas hpBd = em.find(HojaPruebas.class, hojaPruebas.getId());           
            hpBd.setEstadoSICOV(hojaPruebas.getEstadoSICOV());
            hpBd.setPin(hojaPruebas.getPin());
            hojaPruebas = em.merge(hpBd);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = hojaPruebas.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The hojaPruebas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {

        }
    }

    public void editVehiculo(HojaPruebas hojaPruebas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            if (em.getTransaction().isActive() == true) {
                em.flush();
                em.clear();
            } else {
                em.getTransaction().begin();
                em.clear();
            }
            Vehiculo vehiculo = em.find(Vehiculo.class, hojaPruebas.getVehiculo().getId());
            vehiculo.setPlaca(hojaPruebas.getVehiculo().getPlaca());
            em.merge(vehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
        } finally {

        }
    }

    public void editP(Integer idHojaPrueba, String Estado) throws IllegalOrphanException, NonexistentEntityException, ClassNotFoundException, IOException, SQLException {
        Connection cn = null;
        cn = UtilConexion.obtenerConexion();
        String strCerrar = "UPDATE hoja_pruebas SET estado = ? WHERE TESTSHEET = ? ";
        PreparedStatement ps = cn.prepareStatement(strCerrar);
        ps.setString(1, Estado);
        ps.setLong(2, idHojaPrueba);
        try {
            ps.executeUpdate();
        } catch (NoResultException ex) {
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HojaPruebas hojaPruebas;

            try {
                hojaPruebas = em.getReference(HojaPruebas.class, id);
                hojaPruebas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The hojaPruebas with id " + id + " no longer exists.", enfe);
            }
            Vehiculo vehiculo = hojaPruebas.getVehiculo();
            if (vehiculo != null) {
                vehiculo.getHojaPruebasList().remove(hojaPruebas);
                em.merge(vehiculo);
            }
            Propietario propietario = hojaPruebas.getPropietario();
            if (propietario != null) {
                propietario.getListHojaPruebas().remove(hojaPruebas);
                em.merge(propietario);
            }
            List<Prueba> listPruebas = hojaPruebas.getListPruebas();
            for (Prueba listPruebasPruebas : listPruebas) {
                listPruebasPruebas.setHojaPruebas(null);
                em.merge(listPruebasPruebas);
            }
            em.remove(hojaPruebas);
            em.getTransaction().commit();
        } finally {
        }
    }

    public HojaPruebas find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            if(em.getTransaction().isActive()==true){                
                em.getTransaction().commit();  
                em.clear();
            }
            em.getTransaction().begin();
            em.clear();
            em.flush();
            HojaPruebas hp = em.find(HojaPruebas.class, id);
            em.getTransaction().commit();
            return hp;
        } finally {
        }
    }

    public String verificacionCierreHoja(HojaPruebas ctxHojaPrueba) {
        List<Prueba> ctxListPrueba = new ArrayList();
        if (ctxHojaPrueba.getReinspeccionList().size() == 0) {
            List<Prueba> tempListPrueba = ctxHojaPrueba.getListPruebas();
            for (Prueba ctxpru : tempListPrueba) {
                if (ctxpru.getAbortado().equalsIgnoreCase("N")) {
                    ctxListPrueba.add(ctxpru);
                }
            }
        } else {
            Reinspeccion r = ctxHojaPrueba.getReinspeccionList().get(0);
            List<Prueba> tempListPrueba = ctxHojaPrueba.getListPruebas();
            Boolean encontrado = false;
            for (Prueba ctxpru : tempListPrueba) {
                for (Prueba prReins : r.getPruebaList()) {
                    if (ctxpru.getTipoPrueba().getId() == prReins.getTipoPrueba().getId()) {
                        encontrado = true;
                        break;
                    }
                }
                if (encontrado == false) {
                    if (ctxpru.getAbortado().equalsIgnoreCase("N")) {
                        ctxListPrueba.add(ctxpru);
                    }
                }
                encontrado = false;
            }
            for (Prueba prReins : r.getPruebaList()) {
                if (prReins.getAbortado().equalsIgnoreCase("N")) {
                    ctxListPrueba.add(prReins);
                }
            }
        }
        int contPruebasFinalizadas = 0;
        int contPruebasAprobadas = 0;
        for (Prueba ctxpru : ctxListPrueba) {
            if (ctxpru.getFinalizada().equals("Y") && (!ctxpru.getAbortado().equalsIgnoreCase("A")) && (!ctxpru.getAbortado().equalsIgnoreCase("Y"))) {
                contPruebasFinalizadas++;
            }
            if (ctxpru.getAprobado().equals("Y") && (!ctxpru.getAbortado().equalsIgnoreCase("A"))) {
                contPruebasAprobadas++;
            }
        }// fin del ciclo de recorrido de las pruebas reales
        String estado = "N;N";
        if (contPruebasFinalizadas == ctxListPrueba.size()) {
            estado = "Y";
            if (contPruebasFinalizadas == contPruebasAprobadas) {
                estado = estado.concat(";Y");
            } else {
                estado = estado.concat(";N");
            }
        }
        return estado;

    }

    public List<Integer> pruebasMaxNoFin(long idHojaPruebas) {
        EntityManager em = getEntityManager();
        TypedQuery<Integer> q = em.createQuery("SELECT MAX(p.id) FROM Prueba p WHERE p.finalizada = 'Y' AND p.hojaPruebas.id = :idHojaPruebas GROUP BY p.tipoPrueba", Integer.class);
        q.setParameter(
                "idHojaPruebas", idHojaPruebas);
        return q.getResultList();
    }

    public Integer BuscarMaximo_preventiva() {
        EntityManager em = getEntityManager();
        String query = "SELECT MAX(con_preventiva) FROM hoja_pruebas h where h.preventiva='Y'";
        Query q = em.createNativeQuery(query);
        return (Integer) q.getSingleResult();
    }

    public Integer BuscarMaximo_hojapruebas() {
        EntityManager em = getEntityManager();
        String query = "SELECT MAX(con_hoja_prueba) FROM hoja_pruebas h where h.preventiva='N'";
        Query q = em.createNativeQuery(query);
        return (Integer) q.getSingleResult();
    }

    public Integer BuscarMaximoTestSheet() {
        EntityManager em = getEntityManager();
        String query = "SELECT MAX(TESTSHEET) FROM hoja_pruebas h where h.preventiva='N'";
        Query q = em.createNativeQuery(query);
        return (Integer) q.getSingleResult();
    }

    public List<HojaPruebas> BuscarCriterios(String consulta) {
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery(consulta, HojaPruebas.class
        );
        return q.getResultList();

    }

    public List<HojaPruebas> findHojasEdoEnv1FUR(Date dinicial, Date dfinal, String edoSicov) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            //TypedQuery<HojaPruebas> qCert = em.createQuery("SELECT hp FROM HojaPruebas hp  WHERE hp.fechaIngreso  BETWEEN :inicial and :final AND  NOT EXISTS (SELECT h FROM HojaPruebas h join h.certificados c where c.hojaPruebas.fechaIngreso BETWEEN :fi and :ff  ) ", HojaPruebas.class);
            TypedQuery<HojaPruebas> qCert = em.createQuery("SELECT hp FROM HojaPruebas hp WHERE hp.fechaIngreso  BETWEEN :inicial and :final AND hp.estadoSICOV = :edoSicov", HojaPruebas.class
            );
            DateFormat dtfInicial = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
            DateFormat dtfFinal = new SimpleDateFormat("yyyy/MM/dd 23:59:00");
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String fechaInicial = dtfInicial.format(dinicial);
            String fechaFinal = dtfFinal.format(dfinal);
            dinicial = format.parse(fechaInicial);
            dfinal = format.parse(fechaFinal);

            qCert.setParameter(
                    "inicial", dinicial);
            qCert.setParameter(
                    "final", dfinal);
            qCert.setParameter(
                    "edoSicov", edoSicov);
            List<HojaPruebas> lstHojaPrueba = qCert.getResultList();
            List<HojaPruebas> lstHoja = new ArrayList();
            for (HojaPruebas hp : lstHojaPrueba) {
                if (hp.getCertificados().isEmpty()) {
                    lstHoja.add(hp);
                    System.out.println("---------------------------------------posible pruebas pendientes");
                }
            }
            return lstHoja;
        } catch (Exception ex) {
            Mensajes.mostrarExcepcion(ex);
            return null;
        } finally {

        }
    }

    public List<HojaPruebas> findHojasPendientes(Date dinicial, Date dfinal) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            //TypedQuery<HojaPruebas> qCert = em.createQuery("SELECT hp FROM HojaPruebas hp  WHERE hp.fechaIngreso  BETWEEN :inicial and :final AND  NOT EXISTS (SELECT h FROM HojaPruebas h join h.certificados c where c.hojaPruebas.fechaIngreso BETWEEN :fi and :ff  ) ", HojaPruebas.class);
            TypedQuery<HojaPruebas> qCert = em.createQuery("SELECT hp FROM HojaPruebas hp WHERE hp.fechaIngreso BETWEEN :inicial and :final AND ((hp.estadoSICOV <> 'SINCRONIZADO' AND hp.estadoSICOV <> 'NO_APLICA') OR ((hp.preventiva = 'Y' OR hp.con_preventiva <> 0) AND hp.estado = 'PENDIENTE'))", HojaPruebas.class
            );
            DateFormat dtfInicial = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
            DateFormat dtfFinal = new SimpleDateFormat("yyyy/MM/dd 23:59:00");
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String fechaInicial = dtfInicial.format(dinicial);
            String fechaFinal = dtfFinal.format(dfinal);
            dinicial = format.parse(fechaInicial);
            dfinal = format.parse(fechaFinal);

            qCert.setParameter(
                    "inicial", dinicial);
            qCert.setParameter(
                    "final", dfinal);
            List<HojaPruebas> lstHojaPrueba = qCert.getResultList();
            return lstHojaPrueba;
        } catch (Exception ex) {
            Mensajes.mostrarExcepcion(ex);
            return null;
        } finally {

        }
    }

    public List<HojaPruebas> findHojasFinalizadas(Date dinicial, Date dfinal) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            //TypedQuery<HojaPruebas> qCert = em.createQuery("SELECT hp FROM HojaPruebas hp  WHERE hp.fechaIngreso  BETWEEN :inicial and :final AND  NOT EXISTS (SELECT h FROM HojaPruebas h join h.certificados c where c.hojaPruebas.fechaIngreso BETWEEN :fi and :ff  ) ", HojaPruebas.class);
            TypedQuery<HojaPruebas> qCert = em.createQuery("SELECT hp FROM HojaPruebas hp WHERE hp.fechaIngreso BETWEEN :inicial and :final AND (hp.estadoSICOV = 'SINCRONIZADO' OR ((hp.preventiva = 'Y' OR hp.con_preventiva <> 0) AND hp.estado <> 'PENDIENTE'))", HojaPruebas.class
            );
            DateFormat dtfInicial = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
            DateFormat dtfFinal = new SimpleDateFormat("yyyy/MM/dd 23:59:00");
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String fechaInicial = dtfInicial.format(dinicial);
            String fechaFinal = dtfFinal.format(dfinal);
            dinicial = format.parse(fechaInicial);
            dfinal = format.parse(fechaFinal);

            qCert.setParameter(
                    "inicial", dinicial);
            qCert.setParameter(
                    "final", dfinal);
            List<HojaPruebas> lstHojaPrueba = qCert.getResultList();
            return lstHojaPrueba;
        } catch (Exception ex) {
            Mensajes.mostrarExcepcion(ex);
            return null;
        } finally {

        }
    }

    public List<HojaPruebas> findHojasSinCertificadoByFecha(Date dinicial, Date dfinal) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            //TypedQuery<HojaPruebas> qCert = em.createQuery("SELECT hp FROM HojaPruebas hp  WHERE hp.fechaIngreso  BETWEEN :inicial and :final AND  NOT EXISTS (SELECT h FROM HojaPruebas h join h.certificados c where c.hojaPruebas.fechaIngreso BETWEEN :fi and :ff  ) ", HojaPruebas.class);
            TypedQuery<HojaPruebas> qCert = em.createQuery("SELECT hp FROM HojaPruebas hp   WHERE hp.fechaIngreso  BETWEEN :inicial and :final ", HojaPruebas.class
            );
            DateFormat dtfInicial = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
            DateFormat dtfFinal = new SimpleDateFormat("yyyy/MM/dd 23:59:00");
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String fechaInicial = dtfInicial.format(dinicial);
            String fechaFinal = dtfFinal.format(dfinal);
            dinicial = format.parse(fechaInicial);
            dfinal = format.parse(fechaFinal);

            qCert.setParameter(
                    "inicial", dinicial);
            qCert.setParameter(
                    "final", dfinal);
            List<HojaPruebas> lstHojaPrueba = qCert.getResultList();
            List<HojaPruebas> lstHoja = new ArrayList();
            for (HojaPruebas hp : lstHojaPrueba) {
                if (hp.getCertificados().isEmpty()) {
                    lstHoja.add(hp);
                }
            }
            return lstHoja;
        } catch (Exception ex) {
            Mensajes.mostrarExcepcion(ex);
            return null;
        } finally {

        }
    }

    public List<HojaPruebas> ctxFlujoTrabajo(Date dinicial, Date dfinal) {
        List<HojaPruebas> lstHojas = new ArrayList();
        HojaPruebas hps = new HojaPruebas();
        hps.setEstado(" ");
        hps.setEstadoSICOV(" ");
        hps.setFechaIngreso(new Date());
        hps.setVehiculo(new Vehiculo());
        hps.getVehiculo().setPlaca(" ");
        lstHojas.add(hps);
        return lstHojas;
        /*
         EntityManager em = null;
         try {            
         em = getEntityManager();
         TypedQuery<HojaPruebas> q = em.createQuery("SELECT h FROM HojaPruebas h WHERE h.fechaIngreso BETWEEN :fechainicial and :fechafinal", HojaPruebas.class);
         TypedQuery<HojaPruebas> qr = em.createQuery("SELECT h FROM Reinspeccion r JOIN r.hojaPruebas h WHERE h.aprobado = 'N' AND r.fechaSiguiente BETWEEN :fechainicial AND :fechafinal", HojaPruebas.class);
         DateFormat dtfInicial = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
         DateFormat dtfFinal = new SimpleDateFormat("yyyy/MM/dd 23:59:00");
         DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

         TypedQuery<HojaPruebas> hPend = em.createQuery("SELECT h FROM HojaPruebas h WHERE h.finalizada='N' AND  h.fechaIngreso BETWEEN :fechainicial and :fechafinal ORDER BY h.fechaIngreso ", HojaPruebas.class);
         hPend.setParameter("fechainicial", dinicial);
         hPend.setParameter("fechafinal",dfinal );
            
         String fechaInicial = dtfInicial.format(dinicial);
         String fechaFinal = dtfFinal.format(dfinal);

         dinicial = format.parse(fechaInicial);
         dfinal = format.parse(fechaFinal);

         q.setParameter("fechainicial", dinicial);
         q.setParameter("fechafinal", dfinal);
         qr.setParameter("fechainicial", dtfInicial.parse(fechaInicial));
         qr.setParameter("fechafinal", dtfFinal.parse(fechaFinal));
         List<HojaPruebas> listaHabilitaciones = q.getResultList();
         List<HojaPruebas> listaReinspecciones = qr.getResultList();
         List<HojaPruebas> listaHojasPend = hPend.getResultList();
         for(HojaPruebas hp: listaHojasPend){
         listaHabilitaciones.add(hp);
         }
         return unirListasHabilitacionRechazo(listaHabilitaciones, listaReinspecciones);
         } catch (ParseException ex) {
         Mensajes.mostrarExcepcion(ex);
         return null;
         } finally {
         if (em != null) {
        
         }
         }*/
    }

    public List<HojaPruebas> findHPbyVehiculo(String placa) {
        try {
            EntityManager em = getEntityManager();
            em.getTransaction().begin();
            em.clear();
            em.flush();
            em.getTransaction().commit();
            TypedQuery<HojaPruebas> q = em.createQuery("SELECT h FROM HojaPruebas h JOIN  h.vehiculo v WHERE h.estadoSICOV != 'SINCRONIZADO' AND v.placa = :placa ORDER BY h.fechaIngreso desc", HojaPruebas.class);
            try {
                q.setParameter("placa", placa);
            } catch (Throwable e) {
            }
            List<HojaPruebas> lstHPVehiculo = q.getResultList();
            return lstHPVehiculo;
        } catch (Exception ex) {
            Mensajes.mostrarExcepcion(ex);
            return null;
        } finally {
        }
    }

    public List<HojaPruebas> findHojaPruebasByFecha(Date dinicial, Date dfinal) {

        try {
            EntityManager em = getEntityManager();
            Query q = null;
            q = em.createNamedQuery("HojaPruebas.findByFechas");
            TypedQuery<HojaPruebas> qr = em.createQuery("SELECT h FROM Reinspeccion r JOIN r.hojaPruebas h WHERE h.aprobado = 'N' AND r.fechaSiguiente BETWEEN :fechainicial AND :fechafinal", HojaPruebas.class
            );
            DateFormat dtfInicial = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
            DateFormat dtfFinal = new SimpleDateFormat("yyyy/MM/dd 23:59:00");
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            //TypedQuery<HojaPruebas> hPend = em.createQuery("SELECT h FROM HojaPruebas h WHERE h.finalizada='N' AND  h.fechaIngreso BETWEEN :fechainicial and :fechafinal", HojaPruebas.class);
            String fechaInicial = dtfInicial.format(dinicial);
            String fechaFinal = dtfFinal.format(dfinal);

            dinicial = format.parse(fechaInicial);
            dfinal = format.parse(fechaFinal);

            try {
                q.setParameter("fechaInicial", dinicial);
                q.setParameter("fechaFinal", dfinal);
            } catch (Throwable e) {
                int eve = 1;
            }

            qr.setParameter(
                    "fechainicial", dtfInicial.parse(fechaInicial));
            qr.setParameter(
                    "fechafinal", dtfFinal.parse(fechaFinal));
            List<HojaPruebas> listaHabilitaciones = q.getResultList(); //1134
            List<HojaPruebas> listaReinspecciones = qr.getResultList();

            return unirListasHabilitacionRechazo(listaHabilitaciones, listaReinspecciones);
        } catch (ParseException | NumberFormatException ex) {
            Mensajes.mostrarExcepcion(ex);
            return null;
        } finally {

        }
    }

    public List<HojaPruebas> findHojaPreventivaPruebasByFecha(Date dinicial, Date dfinal) {
        EntityManager em = null;

        try {
            em = getEntityManager();
            TypedQuery<HojaPruebas> q = em.createQuery("SELECT h FROM HojaPruebas h WHERE h.fechaIngreso BETWEEN :fechainicial and :fechafinal AND h.preventiva = 'Y'", HojaPruebas.class
            );
            TypedQuery<HojaPruebas> qr = em.createQuery("SELECT h FROM Reinspeccion r JOIN r.hojaPruebas h WHERE h.aprobado = 'N' AND r.fechaSiguiente BETWEEN :fechainicial AND :fechafinal AND h.preventiva = 'Y'", HojaPruebas.class);
            DateFormat dtfInicial = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
            DateFormat dtfFinal = new SimpleDateFormat("yyyy/MM/dd 23:59:00");
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            String fechaInicial = dtfInicial.format(dinicial);
            String fechaFinal = dtfFinal.format(dfinal);

            dinicial = format.parse(fechaInicial);
            dfinal = format.parse(fechaFinal);

            q.setParameter(
                    "fechainicial", dinicial);
            q.setParameter(
                    "fechafinal", dfinal);

            qr.setParameter(
                    "fechainicial", dtfInicial.parse(fechaInicial));
            qr.setParameter(
                    "fechafinal", dtfFinal.parse(fechaFinal));

            List<HojaPruebas> listaHabilitaciones = q.getResultList();
            List<HojaPruebas> listaReinspecciones = qr.getResultList();

            return unirListasHabilitacionRechazo(listaHabilitaciones, listaReinspecciones);
        } catch (ParseException ex) {
            Mensajes.mostrarExcepcion(ex);
            return null;
        } finally {

        }
    }

    public List<HojaPruebas> findHojaPruebaByFecha(Date dinicial, Date dfinal) {
        EntityManager em = null;

        try {
            em = getEntityManager();
            TypedQuery<HojaPruebas> q = em.createQuery("SELECT h FROM HojaPruebas h WHERE h.fechaIngreso BETWEEN :fechainicial and :fechafinal AND h.preventiva = 'N'", HojaPruebas.class
            );
            TypedQuery<HojaPruebas> qr = em.createQuery("SELECT h FROM Reinspeccion r JOIN r.hojaPruebas h WHERE h.aprobado = 'N' AND r.fechaSiguiente BETWEEN :fechainicial AND :fechafinal AND h.preventiva = 'Y'", HojaPruebas.class);
            DateFormat dtfInicial = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
            DateFormat dtfFinal = new SimpleDateFormat("yyyy/MM/dd 23:59:00");
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            String fechaInicial = dtfInicial.format(dinicial);
            String fechaFinal = dtfFinal.format(dfinal);

            dinicial = format.parse(fechaInicial);
            dfinal = format.parse(fechaFinal);

            q.setParameter(
                    "fechainicial", dinicial);
            q.setParameter(
                    "fechafinal", dfinal);

            qr.setParameter(
                    "fechainicial", dtfInicial.parse(fechaInicial));
            qr.setParameter(
                    "fechafinal", dtfFinal.parse(fechaFinal));

            List<HojaPruebas> listaHabilitaciones = q.getResultList();
            List<HojaPruebas> listaReinspecciones = qr.getResultList();

            return unirListasHabilitacionRechazo(listaHabilitaciones, listaReinspecciones);
        } catch (ParseException ex) {
            Mensajes.mostrarExcepcion(ex);
            return null;
        } finally {

        }
    }

    private List<HojaPruebas> unirListasHabilitacionRechazo(List<HojaPruebas> listaHabilitaciones, List<HojaPruebas> listaReinspecciones) {

        for (HojaPruebas hojaPruebaRechazo : listaReinspecciones) {
            boolean yaEstaEnLaLista = false;
            for (HojaPruebas hojaPruebaHabilitada : listaHabilitaciones) {

                if (hojaPruebaRechazo.getId() == hojaPruebaHabilitada.getId()) {
                    yaEstaEnLaLista = true;
                }

            }//end inner for

            if (!yaEstaEnLaLista) {
                listaHabilitaciones.add(hojaPruebaRechazo);
            }

        }//end outer for      

        return listaHabilitaciones;
    }

    public List<Prueba> findPruebasReinspecciones(Integer idHojaPrueba) {
        EntityManager em = getEntityManager();

        try {
            Query q = em.createNativeQuery("select  p1.*\n"
                    + "from pruebas p1 \n"
                    + "inner join tipo_prueba tp\n"
                    + "        on p1.Tipo_prueba_for = tp.TESTTYPE\n"
                    + "where p1.Id_Pruebas \n"
                    + "in \n"
                    + "        (\n"
                    + "            select max(id_pruebas) \n"
                    + "            from pruebas as p \n"
                    + "            where p.hoja_pruebas_for = ?1 \n"
                    + "            and p.Finalizada = 'Y' \n"
                    + "            group by p.Tipo_prueba_for\n"
                    + "        )", Prueba.class
            );
            q.setParameter(
                    1, idHojaPrueba);
            return q.getResultList();
        } finally {

        }
    }

    public List<HojaPruebas> findHojaPruebasWithReinspeccionByPlaca(String placa) {
        EntityManager em = getEntityManager();
        String quer = "SELECT r.hojaPruebas FROM Reinspeccion r JOIN r.hojaPruebas.vehiculo v WHERE v.placa = :placa GROUP BY r.hojaPruebas";
        System.out.println(quer);
        TypedQuery<HojaPruebas> query = em.createQuery(quer, HojaPruebas.class
        );
        query.setParameter(
                "placa", placa);
        System.out.println(query);

        try {
            return query.getResultList();
        } catch (NoResultException ex) {
            return null;
        } finally {

        }
    }
}

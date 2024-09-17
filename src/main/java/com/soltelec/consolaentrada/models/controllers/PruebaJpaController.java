package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.controllers.exceptions.IllegalOrphanException;
import com.soltelec.consolaentrada.models.controllers.exceptions.NonexistentEntityException;
import com.soltelec.consolaentrada.models.entities.AuditoriaSicov;
import com.soltelec.consolaentrada.models.entities.Defxprueba;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.Medida;
import com.soltelec.consolaentrada.models.entities.Prueba;
import com.soltelec.consolaentrada.models.entities.PruebaDTO;
import com.soltelec.consolaentrada.models.entities.Reinspeccion;
import com.soltelec.consolaentrada.models.entities.SEQUENCE;
import com.soltelec.consolaentrada.models.entities.TipoMedida;
import com.soltelec.consolaentrada.tramasJson.TramaJsonDesviacion;
import com.soltelec.consolaentrada.tramasJson.TramaJsonFrenos;
import com.soltelec.consolaentrada.tramasJson.TramaJsonGases;
import com.soltelec.consolaentrada.tramasJson.TramaJsonLuces;
import com.soltelec.consolaentrada.tramasJson.TramaJsonRuido;
import com.soltelec.consolaentrada.tramasJson.TramaJsonSuspension;
import com.soltelec.consolaentrada.tramasJson.TramaJsonTaximetro;
import com.soltelec.consolaentrada.tramasJson.TramaJsonVisual;
import com.soltelec.consolaentrada.tramasJson.tramaJsonLLantas;
import com.soltelec.consolaentrada.utilities.UtilConexion;

import javax.persistence.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.jfree.util.Log;

/**
 * @author Gerencia TIC
 */
public class PruebaJpaController {

    public EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public void create(Prueba pruebas) {
        if (pruebas.getMedidaList() == null) {
            pruebas.setMedidaList(new ArrayList<Medida>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Medida> attachedMedidasList = new ArrayList<>();
            for (Medida medidasListMedidasToAttach : pruebas.getMedidaList()) {
                medidasListMedidasToAttach = em.getReference(medidasListMedidasToAttach.getClass(), medidasListMedidasToAttach.getId());
                attachedMedidasList.add(medidasListMedidasToAttach);
            }
            pruebas.setMedidaList(attachedMedidasList);
            em.persist(pruebas);
            for (Medida medidasListMedidas : pruebas.getMedidaList()) {
                Prueba oldPruebasOfMedidasListMedidas = medidasListMedidas.getPrueba();
                medidasListMedidas.setPrueba(pruebas);
                medidasListMedidas = em.merge(medidasListMedidas);
                if (oldPruebasOfMedidasListMedidas != null) {
                    oldPruebasOfMedidasListMedidas.getMedidaList().remove(medidasListMedidas);
                    oldPruebasOfMedidasListMedidas = em.merge(oldPruebasOfMedidasListMedidas);
                }
            }
            em.getTransaction().commit();
        } finally {

        }
    }

    public void edit(Prueba pruebas) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Prueba persistentPruebas = em.find(Prueba.class, pruebas.getId());
            List<Medida> medidasListOld = persistentPruebas.getMedidaList();
            List<Medida> medidasListNew = pruebas.getMedidaList();
            List<String> illegalOrphanMessages = null;
            for (Medida medidasListOldMedidas : medidasListOld) {
                if (!medidasListNew.contains(medidasListOldMedidas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<>();
                    }
                    illegalOrphanMessages.add("You must retain Medidas " + medidasListOldMedidas + " since its pruebas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Medida> attachedMedidasListNew = new ArrayList<>();
            for (Medida medidasListNewMedidasToAttach : medidasListNew) {
                medidasListNewMedidasToAttach = em.getReference(medidasListNewMedidasToAttach.getClass(), medidasListNewMedidasToAttach.getId());
                attachedMedidasListNew.add(medidasListNewMedidasToAttach);
            }
            medidasListNew = attachedMedidasListNew;
            pruebas.setMedidaList(medidasListNew);
            pruebas = em.merge(pruebas);
            for (Medida medidasListNewMedidas : medidasListNew) {
                if (!medidasListOld.contains(medidasListNewMedidas)) {
                    Prueba oldPruebasOfMedidasListNewMedidas = medidasListNewMedidas.getPrueba();
                    medidasListNewMedidas.setPrueba(pruebas);
                    medidasListNewMedidas = em.merge(medidasListNewMedidas);
                    if (oldPruebasOfMedidasListNewMedidas != null && !oldPruebasOfMedidasListNewMedidas.equals(pruebas)) {
                        oldPruebasOfMedidasListNewMedidas.getMedidaList().remove(medidasListNewMedidas);
                        oldPruebasOfMedidasListNewMedidas = em.merge(oldPruebasOfMedidasListNewMedidas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (IllegalOrphanException ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pruebas.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The pruebas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {

        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, ClassNotFoundException, IOException, SQLException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.clear();
            em.getTransaction().begin();
            Prueba pruebas;
            try {
                Query q = em.createQuery("DELETE FROM Medida m WHERE m.prueba.id = " + id);

                Integer medidasEliminadas = q.executeUpdate();
                Integer reinspeccionesEliminadas = destroyPruebasFromReinspecciones(id);
                Integer defectosEliminados = destroyPruebasFromDefectos(id);

                System.out.println("ReinsByPrueba Eliminadas: " + reinspeccionesEliminadas);
                System.out.println("Cantidad de Medidas Eliminadas: " + medidasEliminadas);
                System.out.println("Defectos Eliminados: " + defectosEliminados);

                pruebas = em.getReference(Prueba.class, id);
                pruebas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("La prueba con el id " + id + " no existe.", enfe);
            }
            em.remove(pruebas);
            em.getTransaction().commit();
        } finally {

        }
    }

    public Prueba find(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prueba.class, id);
        } finally {

        }
    }

    public List<Prueba> findUltimasPruebasByHoja(int idHojaPruebas) {
        EntityManager em = getEntityManager();
        TypedQuery<Integer> query = em.createQuery("SELECT MAX(p.id) FROM Prueba p WHERE p.hojaPruebas.id = :idHojaPruebas  GROUP BY p.tipoPrueba.id", Integer.class);
        query.setParameter("idHojaPruebas", idHojaPruebas);
        List<Integer> idPruebas = query.getResultList();
        List<Prueba> pruebas = new ArrayList<>();
        for (Integer idPrueba : idPruebas) {
            pruebas.add(em.find(Prueba.class, idPrueba));
        }
        return pruebas;
    }

    public Boolean findExisTestAnulada(int idHojaPruebas, int tipoP) {
        Boolean encontre = false;
        try {
            EntityManager em = getEntityManager();
            TypedQuery<Prueba> query = em.createQuery("SELECT p FROM Prueba p WHERE p.hojaPruebas.id = :idHojaPruebas AND p.abortado='A' AND p.finalizada='A' AND p.tipoPrueba.id = :tipoPr ORDER BY p.id DESC", Prueba.class);
            query.setParameter("idHojaPruebas", idHojaPruebas);
            query.setParameter("tipoPr", tipoP);
            List<Prueba> lstPruebas = query.getResultList();
            if (lstPruebas.size() > 3) {
                Prueba pruAnula = lstPruebas.iterator().next();
                if (pruAnula.getHojaPruebas().getReinspeccionList().size() > 0) {
                    TypedQuery<Reinspeccion> querRein = em.createQuery("SELECT r FROM Reinspeccion r WHERE r.hojaPruebas.id = :idHojaPruebas", Reinspeccion.class);
                    querRein.setParameter("idHojaPruebas", idHojaPruebas);
                    List<Reinspeccion> lstReins = querRein.getResultList();
                    List<Prueba> lstPruRein = lstReins.iterator().next().getPruebaList();
                    for (Prueba reP : lstPruRein) {
                        if (reP.getId() == pruAnula.getId()) {
                            encontre = true;
                            break;
                        }
                    }
                } else {
                    encontre = true;
                }
            }
        } catch (Exception ex) {
            int eve = 0;
        }
        return encontre;
    }

    public List<Prueba> findPrimeraPruebasByHoja(int idHojaPruebas) {
        EntityManager em = getEntityManager();
        TypedQuery<Integer> query = em.createQuery("SELECT MIN(p.id) FROM Prueba p WHERE p.hojaPruebas.id = :idHojaPruebas GROUP BY p.tipoPrueba.id", Integer.class);
        query.setParameter("idHojaPruebas", idHojaPruebas);
        List<Integer> idPruebas = query.getResultList();
        List<Prueba> pruebas = new ArrayList<>();

        for (Integer idPrueba : idPruebas) {
            pruebas.add(em.find(Prueba.class, idPrueba));
        }

        return pruebas;
    }

    public Prueba findLastByHoja(int idHojaPruebas, int idTipoPrueba) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT MAX(p.id) FROM Prueba p WHERE p.hojaPruebas.id = :idHojaPruebas AND p.tipoPrueba.id = :idTipoPrueba GROUP BY p.tipoPrueba.id");
        query.setParameter("idHojaPruebas", idHojaPruebas);
        query.setParameter("idTipoPrueba", idTipoPrueba);

        try {
            Integer idPrueba = (Integer) query.getSingleResult();
            return new PruebaJpaController().find(idPrueba);
        } catch (NoResultException ex) {
            return null;
        } finally {

        }
    }

    public List<Integer> findverificacion(int idHojaPruebas) {
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("SELECT count(*) Aprobadas \n"
                + "			  FROM pruebas ps \n"
                + "			 WHERE ps.Id_Pruebas \n"
                + "			    IN(\n"
                + "					SELECT max(p.Id_Pruebas)\n"
                + "					  FROM pruebas p \n"
                + "		     INNER JOIN hoja_pruebas hp\n"
                + "		     			 ON p.hoja_pruebas_for = hp.TESTSHEET \n"
                + "		     	    WHERE hp.TESTSHEET = ?\n"
                + "		     	      AND p.Aprobada = 'Y'\n"
                + "		     	 GROUP BY p.Tipo_prueba_for)\n"
                + "	       UNION ALL\n"
                + "			 			SELECT count(*) Finalizadas\n"
                + "			  FROM pruebas ps \n"
                + "			 WHERE ps.Id_Pruebas \n"
                + "			    IN(\n"
                + "					SELECT max(p.Id_Pruebas)\n"
                + "					  FROM pruebas p \n"
                + "		     INNER JOIN hoja_pruebas hp\n"
                + "		     			 ON p.hoja_pruebas_for = hp.TESTSHEET \n"
                + "		     	    WHERE hp.TESTSHEET = ?\n"
                + "		     	      AND p.Finalizada = 'Y'\n"
                + "		     	 GROUP BY p.Tipo_prueba_for)\n"
                + "		     	 UNION ALL\n"
                + "			 			SELECT count(*) Finalizadas\n"
                + "			  FROM pruebas ps \n"
                + "			 WHERE ps.Id_Pruebas \n"
                + "			    IN(\n"
                + "					SELECT max(p.Id_Pruebas)\n"
                + "					  FROM pruebas p \n"
                + "		     INNER JOIN hoja_pruebas hp\n"
                + "		     			 ON p.hoja_pruebas_for = hp.TESTSHEET \n"
                + "		     	    WHERE hp.TESTSHEET = ?\n"
                + "		     	      AND p.Finalizada = 'Y'\n"
                + "		     	 GROUP BY p.Tipo_prueba_for))");
        query.setParameter(1, idHojaPruebas);
        query.setParameter(2, idHojaPruebas);
        query.setParameter(3, idHojaPruebas);
        try {

            List<Integer> ds = (List<Integer>) query.getResultList();
            System.out.println(ds);
            return ds;
        } catch (NoResultException ex) {
            return null;
        } finally {

        }
    }

    public void obtenerSeqSicov(Prueba prueba) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        String ipFound = "";
        Integer evenSicov = 0;
        switch (prueba.getTipoPrueba().getId()) {
            case 1:
                evenSicov = 7;
                break;
            case 2:
                evenSicov = 2;
                break;
            case 4:
                evenSicov = 5;
                break;
            case 5:
                evenSicov = 4;
                break;
            case 6:
                evenSicov = 6;
                break;
            case 7:
                evenSicov = 3;
                break;
            case 8:
                evenSicov = 1;
                break;
            case 9:
                evenSicov = 8;
                break;
        }

        try {
            /*Query query = em.createQuery("SELECT hp FROM  HojaPruebas hp JOIN  hp.vehiculo v WHERE v.tipoGasolina.id=:tipoGas and v.tipoVehiculo.id=:tipoVeh and v.id =:v order by hp.id desc ");
            System.out.println(" Primer parametro, tipo gasolina: " + prueba.getHojaPruebas().getVehiculo().getTipoGasolina().getId());
            query.setParameter("tipoGas", prueba.getHojaPruebas().getVehiculo().getTipoGasolina().getId());
            System.out.println(" Segundo parametro, tipoveh: " + prueba.getHojaPruebas().getVehiculo().getTipoVehiculo().getId());
            query.setParameter("tipoVeh", prueba.getHojaPruebas().getVehiculo().getTipoVehiculo().getId());
            System.out.println(" tercer parametro, id del vehiculo : " + prueba.getHojaPruebas().getVehiculo().getId());
            query.setParameter("idV", prueba.getHojaPruebas().getId());
            query.setMaxResults(30);*/
            //Query query = em.createQuery("SELECT hp FROM  HojaPruebas hp JOIN  hp.vehiculo v WHERE v.id =:idV by hp.id asc ");
            Query query = em.createQuery("SELECT hp FROM  HojaPruebas hp JOIN  hp.vehiculo v WHERE v.id =:idV AND hp.id = (SELECT MAX (hp.id) FROM  HojaPruebas hp JOIN  hp.vehiculo v WHERE v.id =:idV2)");
            System.out.println(" Primer parametro, ID del vehiculo: " + prueba.getHojaPruebas().getVehiculo().getId());
            query.setParameter("idV", prueba.getHojaPruebas().getVehiculo().getId());
            System.out.println(" segundo parametro, ID del vehiculo: " + prueba.getHojaPruebas().getVehiculo().getId());
            query.setParameter("idV2", prueba.getHojaPruebas().getVehiculo().getId());
            List<HojaPruebas> lstHojaPrueba = query.getResultList();
//------aqui se queda en villavicencio
            System.out.println("entro antes de for (HojaPruebas hoja : lstHojaPrueba) ln 339, tipo de evento: " + evenSicov);
            for (HojaPruebas hoja : lstHojaPrueba) {
                System.out.println("entro al ciclo for, hoja de prueba que entro a evaluar:" + hoja.getId() + "tipo de evento :" + evenSicov);
                query = em.createQuery("SELECT a.ipEquipoMedicion FROM AuditoriaSicov a WHERE a.idRevision = :idHojaPruebas and a.tipoEvento= :evento");
                query.setParameter("idHojaPruebas", hoja.getId());
                query.setParameter("evento", evenSicov);
                int ev = query.getResultList().size();
                if (ev > 0) {
                    ipFound = (String) query.getResultList().iterator().next();
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println(" Error : " + e.getMessage());

        }

        Query q = em.createNativeQuery("SELECT MAX(SEQ_COUNT) from sequence WHERE SEQ_NAME = 'AUD_SICOV' ");
        Integer idAud = (Integer) q.getSingleResult();
        SEQUENCE s = em.find(SEQUENCE.class, "AUD_SICOV");
        int nvoAudi = idAud + 1;
        s.setSEQCOUNT(nvoAudi);
        Prueba pr = em.find(Prueba.class, prueba.getId());
        pr.setFechaAborto(ipFound.concat(";").concat(String.valueOf(idAud)));
        em.merge(s);
        em.merge(pr);
        em.getTransaction().commit();
    }

    public Prueba obtSeqSicov(Prueba prueba) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        String ipFound = "";
        Integer evenSicov = 0;
        Query query = em.createQuery("SELECT hp FROM  HojaPruebas hp JOIN  hp.vehiculo v WHERE v.tipoGasolina.id=:tipoGas and v.tipoVehiculo.id=:tipoVeh order by hp.id desc ");
        query.setParameter("tipoGas", prueba.getHojaPruebas().getVehiculo().getTipoGasolina().getId());
        query.setParameter("tipoVeh", prueba.getHojaPruebas().getVehiculo().getTipoVehiculo().getId());
        query.setMaxResults(10);
        switch (prueba.getTipoPrueba().getId()) {
            case 1:
                evenSicov = 7;
                break;
            case 2:
                evenSicov = 2;
                break;
            case 4:
                evenSicov = 5;
                break;
            case 5:
                evenSicov = 4;
                break;
            case 6:
                evenSicov = 6;
                break;
            case 7:
                evenSicov = 3;
                break;
            case 8:
                evenSicov = 1;
                break;
            case 9:
                evenSicov = 8;
                break;
        }
        List<HojaPruebas> lstHojaPrueba = query.getResultList();
        for (HojaPruebas hoja : lstHojaPrueba) {
            query = em.createQuery("SELECT a.ipEquipoMedicion FROM AuditoriaSicov a WHERE a.idRevision = :idHojaPruebas and a.tipoEvento= :evento");
            query.setParameter("idHojaPruebas", hoja.getId());
            query.setParameter("evento", evenSicov);
            int ev = query.getResultList().size();
            if (ev > 0) {
                ipFound = (String) query.getResultList().iterator().next();
                break;
            }
        }
        Query q = em.createNativeQuery("SELECT MAX(SEQ_COUNT) from sequence WHERE SEQ_NAME = 'AUD_SICOV' ");
        Integer idAud = (Integer) q.getSingleResult();
        SEQUENCE s = em.find(SEQUENCE.class, "AUD_SICOV");
        int nvoAudi = idAud + 1;
        s.setSEQCOUNT(nvoAudi);
        Prueba pr = em.find(Prueba.class, prueba.getId());
        pr.setFechaAborto(ipFound.concat(";").concat(String.valueOf(idAud)));
        em.merge(s);
        em.merge(pr);
        em.getTransaction().commit();
        return pr;
    }

//    public void restauracionTramaSicovTaximetro(Prueba prueba, Integer idRun) 
//    {
//        List<Medida> lstMedidas = prueba.getMedidaList();
//        Map<Integer, Float> hasMedidas = new HashMap<Integer, Float>();
//        for (Medida medida : lstMedidas) 
//        {
//            hasMedidas.put(medida.getTipoMedida().getId(), medida.getValor());
//        }
//        String tramaAuditoria = ("{\"refComercialLLanta").concat("\":\"").concat(String.valueOf(prueba.getHojaPruebas().getVehiculo().getLlantas().getNombre())).concat("\",").concat("\"errorDistancia").concat("\":\"").concat(String.valueOf(hasMedidas.get(9002))).concat("\",").concat("\"errorTiempo").concat("\":\"").concat(String.valueOf(hasMedidas.get(9003))).concat("\",");
//        tramaAuditoria = tramaAuditoria.concat("\"tablaAfectada\":\"medidas\",\"idRegistro\":\"").concat(String.valueOf(prueba.getId())).concat("\"}");
//        registrarTramas(prueba, tramaAuditoria, idRun, 8,"PLACA_PRU");
//    }
//    
//    public void restauracionTramaSicovVisual(Prueba test, Integer idRun) 
//    {
//        int posObs = 0;
//        if (test.getObservaciones() == null) 
//        {
//
//        } else {
//            posObs = test.getObservaciones().indexOf("obs");
//        }
//        
//        
//        String observaciones = "";
//        String tramaDefectos = "";
//        String infLabr = "";
//        String cmLabr = "";
//        
//        
//        for (Defxprueba defxprueba : test.getDefxpruebaList()) 
//        {
////            tramaDefectos = tramaDefectos.concat(defxprueba.getDefectos().getCodigoSuperintendencia().concat("_"));
//            tramaDefectos = tramaDefectos.concat(defxprueba.getDefectos().getCodigoResolucion().concat(""));
//        }
//        
//        if (tramaDefectos.length() > 0) 
//        {
//            tramaDefectos = tramaDefectos.substring(0, tramaDefectos.length() - 1);            
//        }
//        
//        
//        if (test.getObservaciones() != null) 
//        {
//            String[] lstObs = test.getObservaciones().split("obs");
//            String[] lstEjes = lstObs[0].split("&");
//            for (int i = 0; i < lstEjes.length; i++) 
//            {
//                infLabr = lstEjes[i].replace("$", "-");
//                cmLabr = cmLabr.concat("Eje" + String.valueOf(i + 1)).concat(" ").concat(infLabr).concat("mm; ");
//            }
//            if (lstObs.length > 1) { 
//                cmLabr = cmLabr.concat("\n").concat(" ");
//            }
//            observaciones = "".concat("\"").concat(cmLabr).concat("\"");
//        }
//        if (posObs > 0) 
//        {
//            //observaciones = observaciones.concat(",\"Obs\":\"").concat(test.getObservaciones().substring(posObs + 3, test.getObservaciones().length()));
//            observaciones = observaciones.concat(test.getObservaciones().substring(posObs + 3, test.getObservaciones().length()));
//        }
//        test.setObservaciones(observaciones);
//        String tramaAuditoria="";
//        tramaAuditoria = "{\"ObservacionesVisual\":\"".concat(tramaDefectos).concat("\",").concat("\"tablaAfectada\":\"defxprueba\",\"idRegistro\":\"").concat(String.valueOf(test.getId())).concat("\"}");
//        registrarTramas(test, tramaAuditoria, idRun, 7,"PLACA_PRU");
//    }
    /**
     *
     * @param ctxHojaPrueba
     * @param idRun
     */
    public void restauracionTramaSicovLlantas(Prueba prueba, Integer idRun, String placa) {
        tramaJsonLLantas dataTrama = new tramaJsonLLantas();
        System.out.println("--------------------------------------------------");
        System.out.println("-------   restauracionTramaSicovLlantas        ---");
        System.out.println("--------------------------------------------------");

        try {
//            for (Prueba prueba : ctxHojaPrueba.getListPruebas())
//            {
            if (prueba.getTipoPrueba().getId() == 1) {
                for (Medida medida : prueba.getMedidaList()) {
                    if (medida.getTipoMedida().getId() == 9046 || medida.getTipoMedida().getId() == 9050 || medida.getTipoMedida().getId() == 9013) {
                        dataTrama.setDerProfundidadEje1("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9047 || medida.getTipoMedida().getId() == 9051 || medida.getTipoMedida().getId() == 9014) {
                        dataTrama.setDerProfundidadExternaEje2("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9015) {
                        dataTrama.setDerProfundidadExternaEje3("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9016) {
                        dataTrama.setDerProfundidadExternaEje4("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9017) {
                        dataTrama.setDerProfundidadExternaEje5("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9046 || medida.getTipoMedida().getId() == 9053 || medida.getTipoMedida().getId() == 9004) {
                        dataTrama.setIzqProfundidadEje1("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9047 || medida.getTipoMedida().getId() == 9054 || medida.getTipoMedida().getId() == 9005) {
                        dataTrama.setIzqProfundidadExternaEje2("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9006) {
                        dataTrama.setIzqProfundidadExternaEje3("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9007) {
                        dataTrama.setIzqProfundidadExternaEje4("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9008) {
                        dataTrama.setIzqProfundidadExternaEje5("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9018) {
                        dataTrama.setDerProfundidadInternaEje2("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9019) {
                        dataTrama.setDerProfundidadInternaEje3("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9020) {
                        dataTrama.setDerProfundidadInternaEje4("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9021) {
                        dataTrama.setDerProfundidadInternaEje5("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9055 || medida.getTipoMedida().getId() == 9009) {
                        dataTrama.setIzqProfundidadInternaEje2("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9010) {
                        dataTrama.setIzqProfundidadInternaEje3("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9011) {
                        dataTrama.setIzqProfundidadInternaEje4("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9012) {
                        dataTrama.setIzqProfundidadInternaEje5("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9040) {
                        dataTrama.setRepuestoProfundidad("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9041) {
                        dataTrama.setRepuesto2Profundidad("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9031) {
                        dataTrama.setDerPresionEje1("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9032) {
                        dataTrama.setDerPresionExternaEje2("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9033) {
                        dataTrama.setDerPresionExternaEje3("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9034) {
                        dataTrama.setDerPresionExternaEje4("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9035) {
                        dataTrama.setDerPresionExternaEje5("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9022) {
                        dataTrama.setIzqPresionEje1("" + medida.getValor());
                    }
                    if (medida.getTipoMedida().getId() == 9024) {
                        dataTrama.setIzqPresionExternaEje3("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9025) {
                        dataTrama.setIzqPresionExternaEje4("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9026) {
                        dataTrama.setIzqPresionExternaEje5("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9036) {
                        dataTrama.setDerPresionInternaEje2("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9037) {
                        dataTrama.setDerPresionInternaEje3("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9038) {
                        dataTrama.setDerPresionInternaEje4("" + medida.getValor());
                    }
                    if (medida.getTipoMedida().getId() == 9039) {
                        dataTrama.setDerPresionInternaEje5("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9027) {
                        dataTrama.setIzqPresionInternaEje2("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9028) {
                        dataTrama.setIzqPresionInternaEje3("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9029) {
                        dataTrama.setIzqPresionInternaEje4("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9030) {
                        dataTrama.setIzqPresionInternaEje5("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9043) {
                        dataTrama.setRepuestoPresion("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9044) {
                        dataTrama.setRepuesto2Presion("" + medida.getValor());
                    }
                }

                dataTrama.setTablaAfectada("medidas");
                dataTrama.setIdRegistro("" + prueba.getId());
                System.out.println("----------------------------------------------------------");
                System.out.println("----------------- Trama json a guardar para Ruido---------");
                System.out.println("----------------------------------------------------------");
                System.out.println("trama labrado y precion en prueba de llantas: " + dataTrama.toString());
                System.out.println("----------------------------------------------------------");
                registrarTramas(prueba, dataTrama.toString(), idRun, 8, placa);
            }
//            }  
        } catch (Exception e) {
            System.out.println("Error en el metodo : restauracionTramaSicovLlantas()" + e);
        }
    }

    /**
     *
     * @param prueba
     * @param idRun
     */
    public void TramaSicovTaxcimetro(Prueba prueba, Integer idRun, String placa) {
        TramaJsonTaximetro dataTrama = new TramaJsonTaximetro();
        System.out.println("--------------------------------------------------");
        System.out.println("-------   TramaSicovT"
                + "axcimetro         -----------");
        System.out.println("--------------------------------------------------");
        try {
//            for (Prueba prueba : ctxHojaPrueba.getListPruebas())
//            {
            if (prueba.getTipoPrueba().getId() == 9) {
                for (Medida medida : prueba.getMedidaList()) {
                    if (medida.getTipoMedida().getId() == 6020) {
                        dataTrama.setErrorDistancia("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 9003) {
                        dataTrama.setErrorTiempo("" + medida.getValor());
                    }
                }
                dataTrama.setRefComericalLLanta(prueba.getHojaPruebas().getVehiculo().getLlantas().getNombre());

                dataTrama.setTablaAfectada("medidas");
                dataTrama.setIdRegistro("" + prueba.getId());
                System.out.println("----------------------------------------------------------");
                System.out.println("----------------- Trama json a guardar para Ruido---------");
                System.out.println("----------------------------------------------------------");
                System.out.println(dataTrama.toString());
                System.out.println("----------------------------------------------------------");
                registrarTramas(prueba, dataTrama.toString(), idRun, 8, placa);
            }
//            }

        } catch (Exception e) {
            System.out.println("Error en el metodo : TramaSicovTaxcimetro()" + e);
        }
    }

    /**
     *
     * @param prueba
     * @param idRun
     */
    public void TramaSicovVisual(Prueba prueba, Integer idRun, String placa) {
        TramaJsonVisual dataTrama = new TramaJsonVisual();
        System.out.println("--------------------------------------------------");
        System.out.println("-------   TramaSicovSuspension         -----------");
        System.out.println("--------------------------------------------------");
        try {
//            for (Prueba prueba : ctxHojaPrueba.getListPruebas())
//            {
//                
            if (prueba.getTipoPrueba().getId() == 1) {
                List<Defxprueba> defxprueba = prueba.getDefxpruebaList();
                for (int i = 0; i < defxprueba.size(); i++) {
                    if (i == 0 || i == defxprueba.size()) {
                        dataTrama.setCodigoRechazo(defxprueba.get(i).getDefectos().getCodigoResolucion());

                    } else {

                        dataTrama.setCodigoRechazo("_" + defxprueba.get(i).getDefectos().getCodigoResolucion());
                    }
                }
                dataTrama.setObservaciones(prueba.getObservaciones());
                dataTrama.setTablaAfectada("medidas");
                dataTrama.setIdRegistro("" + prueba.getId());
                System.out.println("----------------------------------------------------------");
                System.out.println("----------------- Trama json a guardar para Ruido---------");
                System.out.println("----------------------------------------------------------");
                System.out.println(dataTrama.toString());
                System.out.println("----------------------------------------------------------");
                registrarTramas(prueba, dataTrama.toString(), idRun, 7, placa);
//                    break;
//                }
            }

        } catch (Exception e) {
            System.out.println("Error en el metodo : TramaSicovVisual()" + e);
        }
    }

    /**
     *
     * @param ctxHojaPrueba
     * @param idRun
     */
    public void TramaSicovSuspension(Prueba prueba, Integer idRun, String placa) {
        TramaJsonSuspension dataTrama = new TramaJsonSuspension();
        System.out.println("--------------------------------------------------");
        System.out.println("-------   TramaSicovSuspension         -----------");
        System.out.println("--------------------------------------------------");
        try {
//            for (Prueba prueba : ctxHojaPrueba.getListPruebas())
//            {
            if (prueba.getTipoPrueba().getId() == 6) {
                for (Medida medida : prueba.getMedidaList()) {
                    if (medida.getTipoMedida().getId() == 6020) {
                        dataTrama.setSusPDerEje1("" + medida.getValor());
                    }
                    if (medida.getTipoMedida().getId() == 6016) {
                        dataTrama.setSuspIzqEje1("" + medida.getValor());
                    }
                    if (medida.getTipoMedida().getId() == 6021) {
                        dataTrama.setSuspIzqEje2("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 6017) {
                        dataTrama.setSusPDerEje2("" + medida.getValor());
                    }
                }

                dataTrama.setTablaAfectada("medidas");
                dataTrama.setIdRegistro("" + prueba.getId());
                System.out.println("----------------------------------------------------------");
                System.out.println("----------------- Trama json a guardar para Ruido---------");
                System.out.println("----------------------------------------------------------");
                System.out.println(dataTrama.toString());
                System.out.println("----------------------------------------------------------");
                registrarTramas(prueba, dataTrama.toString(), idRun, 6, placa);
//                    break;
//                }
            }
        } catch (Exception e) {
            System.out.println("Error en el metodo : TramaSicovSuspension()" + e);
        }
    }

    /**
     *
     * @param prueba
     * @param idRun
     */
    public void tramaSicovRuidoDesviacion(Prueba prueba, Integer idRun, String placa) {
        TramaJsonDesviacion dataTrama = new TramaJsonDesviacion();
        System.out.println("--------------------------------------------------");
        System.out.println("----------   tramaSicovDesviacion         -------------");
        System.out.println("--------------------------------------------------");
        try {
//            for (Prueba prueba : ctxHojaPrueba.getListPruebas())
//            {
            if (prueba.getTipoPrueba().getId() == 4) {
                for (Medida medida : prueba.getMedidaList()) {
                    if (medida.getTipoMedida().getId() == 4000) {
                        dataTrama.setDesviacionLateraleje1("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 4001) {
                        dataTrama.setDesviacionLateraleje2("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 4002) {
                        dataTrama.setDesviacionLateraleje3("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 4003) {
                        dataTrama.setDesviacionLateraleje4("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 4004) {
                        dataTrama.setDesviacionLateraleje5("" + medida.getValor());
                    }
                }

                dataTrama.setTablaAfectada("medidas");
                dataTrama.setIdRegistro("" + prueba.getId());
                System.out.println("----------------------------------------------------------");
                System.out.println("------------ Trama json a guardar para Desviacion---------");
                System.out.println("----------------------------------------------------------");
                System.out.println(dataTrama.toString());
                System.out.println("----------------------------------------------------------");
                registrarTramas(prueba, dataTrama.toString(), idRun, 5, placa);
//                    break;
//                }
            }
        } catch (Exception e) {
            System.out.println("Error en el metodo : tramaSicovRuidoDesviacion()" + e);
        }
    }

    /**
     *
     * @param prueba
     * @param idRun
     */
    public void tramaSicovRuido(Prueba prueba, Integer idRun, String placa) {
        TramaJsonRuido dataTrama = new TramaJsonRuido();
        System.out.println("--------------------------------------------------");
        System.out.println("----------   tramaSicovRuido         -------------");
        System.out.println("--------------------------------------------------");
        try {
//            for (Prueba prueba : ctxHojaPrueba.getListPruebas())
//            {
            if (prueba.getTipoPrueba().getId() == 7) {
                for (Medida medida : prueba.getMedidaList()) {
                    if (medida.getTipoMedida().getId() == 7005) {
                        dataTrama.setRuidoEscape("" + medida.getValor());
                    }
                }

                dataTrama.setTablaAfectada("medidas");
                dataTrama.setIdRegistro("" + prueba.getId());

                System.out.println("----------------------------------------------------------");
                System.out.println("----------------- Trama json a guardar para RUIDO---------");
                System.out.println("----------------------------------------------------------");
                System.out.println(dataTrama.toString());
                System.out.println("----------------------------------------------------------");
                registrarTramas(prueba, dataTrama.toString(), idRun, 3, placa);
//                    break;
//                }
            }
        } catch (Exception e) {
            System.out.println("Error en el metodo : " + e);
        }
    }

    /**
     *
     * @param prueba
     * @param idRun
     */
    public void tramaSicovFrenos(Prueba prueba, Integer idRun, String placa) {
        System.out.println("--------------------------------------------------");
        System.out.println("----------   tramaSicovFrenos         -------------");
        System.out.println("--------------------------------------------------");
        TramaJsonFrenos dataTrama = new TramaJsonFrenos();

        Float derFuerzaPeso = 0f;
        Float izqFuerzaPeso = 0f;

        try {
//            for (Prueba prueba : ctxHojaPrueba.getListPruebas()) 
//            {
            if (prueba.getTipoPrueba().getId() == 5) {
                for (Medida medida : prueba.getMedidaList()) {
                    if (medida.getTipoMedida().getId() == 5024) {
                        dataTrama.setEficaciaTotal("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5036) {
                        dataTrama.setEficaciaAuxiliar("" + medida.getValor());
                    }

                    //-------------------------------------------------------------
                    //--------------------         EJE 1       --------------------
                    //-------------------------------------------------------------
                    if (medida.getTipoMedida().getId() == 5012) {
                        dataTrama.setFuerzaEje1Izquierdo("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5004) {
                        dataTrama.setPesoEje1Izquierdo("" + medida.getValor());
                        izqFuerzaPeso = izqFuerzaPeso + medida.getValor();
                    }

                    if (medida.getTipoMedida().getId() == 5008) {
                        dataTrama.setFuerzaEje1Derecho("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5000) {
                        dataTrama.setPesoEje1Derecho("" + medida.getValor());
                        derFuerzaPeso = derFuerzaPeso + medida.getValor();
                    }

                    if (medida.getTipoMedida().getId() == 5032) {
                        dataTrama.setEje1Desequilibrio("" + medida.getValor());
                    }

                    //-------------------------------------------------------------
                    //--------------------         EJE 2       --------------------
                    //-------------------------------------------------------------
                    if (medida.getTipoMedida().getId() == 5013) {
                        dataTrama.setFuerzaEje2Izquierdo("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5005) {
                        izqFuerzaPeso = izqFuerzaPeso + medida.getValor();
                        dataTrama.setPesoEje2Izquierdo("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5009) {
                        dataTrama.setFuerzaEje2Derecho("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5001) {
                        dataTrama.setPesoEje2Derecho("" + medida.getValor());
                        derFuerzaPeso = derFuerzaPeso + medida.getValor();
                    }

                    if (medida.getTipoMedida().getId() == 5033) {
                        dataTrama.setEje2Desequilibrio("" + medida.getValor());
                    }

                    //-------------------------------------------------------------
                    //--------------------         EJE 3       --------------------
                    //-------------------------------------------------------------
                    if (medida.getTipoMedida().getId() == 5014) {
                        dataTrama.setFuerzaEje3Izquierdo("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5006) {
                        dataTrama.setPesoEje3Izquierdo("" + medida.getValor());
                        izqFuerzaPeso = izqFuerzaPeso + medida.getValor();
                    }

                    if (medida.getTipoMedida().getId() == 5010) {
                        dataTrama.setFuerzaEje3Derecho("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5002) {
                        dataTrama.setPesoEje3Derecho("" + medida.getValor());
                        derFuerzaPeso = derFuerzaPeso + medida.getValor();
                    }

                    if (medida.getTipoMedida().getId() == 5034) {
                        dataTrama.setEje3Desequilibrio("" + medida.getValor());
                    }

                    //-------------------------------------------------------------
                    //--------------------         EJE 4       --------------------
                    //-------------------------------------------------------------
                    if (medida.getTipoMedida().getId() == 5015) {
                        dataTrama.setFuerzaEje4Izquierdo("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5007) {
                        izqFuerzaPeso = izqFuerzaPeso + medida.getValor();
                        dataTrama.setPesoEje4Izquierdo("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5011) {
                        dataTrama.setFuerzaEje4Derecho("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5003) {
                        dataTrama.setPesoEje4Derecho("" + medida.getValor());
                        derFuerzaPeso = derFuerzaPeso + medida.getValor();
                    }

                    if (medida.getTipoMedida().getId() == 5035) {
                        dataTrama.setEje4Desequilibrio("" + medida.getValor());
                    }

                    //-------------------------------------------------------------
                    //--------------------         EJE 5       --------------------
                    //-------------------------------------------------------------
                    if (medida.getTipoMedida().getId() == 5028) {
                        dataTrama.setFuerzaEje5Izquierdo("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5026) {
                        izqFuerzaPeso = izqFuerzaPeso + medida.getValor();
                        dataTrama.setPesoEje5Izquierdo("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5027) {
                        dataTrama.setFuerzaEje5Derecho("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5025) {
                        dataTrama.setPesoEje5Derecho("" + medida.getValor());
                        derFuerzaPeso = derFuerzaPeso + medida.getValor();
                    }

                    if (medida.getTipoMedida().getId() == 5031) {
                        dataTrama.setEje5Desequilibrio("" + medida.getValor());
                    }

                    //-------------------------------------------------------------
                    //--------------------       auxiliar      --------------------
                    //-------------------------------------------------------------
                    if (medida.getTipoMedida().getId() == 5016 || medida.getTipoMedida().getId() == 5017 || medida.getTipoMedida().getId() == 5018 || medida.getTipoMedida().getId() == 5019 || medida.getTipoMedida().getId() == 5029) {
                        dataTrama.setDerFuerzaAuxiliar("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 5020 || medida.getTipoMedida().getId() == 5021 || medida.getTipoMedida().getId() == 5022 || medida.getTipoMedida().getId() == 5023 || medida.getTipoMedida().getId() == 5030) {
                        dataTrama.setIzqFuerzaAuxiliar("" + medida.getValor());
                    }

                }
                dataTrama.setDerFuerzaPeso("" + derFuerzaPeso);
                dataTrama.setIzqFuerzaPeso("" + izqFuerzaPeso);
                dataTrama.setTablaAfectada("medidas");
                dataTrama.setIdRegistro("" + prueba.getId());

                System.out.println("----------------- Trama json a guardar ---------");
                System.out.println(dataTrama.toString());
                System.out.println("------------------------------------------------");
                registrarTramas(prueba, dataTrama.toString(), idRun, 4, placa);
//                    break;
            }
//            }    
        } catch (Exception e) {
            System.out.println("Error en el metodo : tramaSicovFrenos()" + e);
        }
    }

    /**
     *
     * @param prueba
     * @param idRun
     */
    @SuppressWarnings("unused")
    public void tramaSicovLuces(Prueba prueba, Integer idRun, String placa) {
        System.out.println("--------------------------------------------------");
        System.out.println("----------   tramaSicovLuces         -------------");
        System.out.println("--------------------------------------------------");

        TramaJsonLuces dataTrama = new TramaJsonLuces();
        try {
//            for (Prueba prueba : ctxHojaPrueba.getListPruebas()) 
//            {
            if (prueba.getTipoPrueba().getId() == 2) {
                for (Medida medida : prueba.getMedidaList()) {
                    //--------------------------------------------------------------
                    //-------      SIMULTANIEDA   EXPLOTADORAS  DERECHA         ----
                    //--------------------------------------------------------------

                    TipoMedida tipoMedida = medida.getTipoMedida();
                    if (tipoMedida == null) continue;
                    Integer idTipoMedida = tipoMedida.getId();
                    if (idTipoMedida == null) continue;
                        
                    

                    if (idTipoMedida == 2050 || idTipoMedida == 2051 || idTipoMedida == 2052) {
                        if (medida.getSimult().equalsIgnoreCase("Y")) {
                            dataTrama.setDerExploradorasSimultaneas("S");

                        } else if (medida.getSimult().equalsIgnoreCase("N")) {
                            dataTrama.setDerExploradorasSimultaneas("N");
                        }
                    }

                    if (idTipoMedida == 2050) {
                        dataTrama.setDerExplorardorasValor1("" + medida.getValor());
                    }

                    if (idTipoMedida == 2051) {
                        dataTrama.setDerExplorardorasValor2("" + medida.getValor());
                    }

                    if (idTipoMedida == 2052) {
                        dataTrama.setDerExplorardorasValor2("" + medida.getValor());
                    }

                    //--------------------------------------------------------------
                    //-------    SIMULTANIEDA   EXPLOTADORAS  IZQUIERDA          ---
                    //--------------------------------------------------------------
                    if (idTipoMedida == 2053 || idTipoMedida == 2054 || idTipoMedida == 2055) {
                        if (medida.getSimult().equalsIgnoreCase("Y")) {
                            dataTrama.setIzqExploradorasSimultaneas("S");

                        } else if (medida.getSimult().equalsIgnoreCase("N")) {
                            dataTrama.setIzqExploradorasSimultaneas("N");
                        }
                    }

                    if (idTipoMedida == 2053) {
                        dataTrama.setIzqExplorardorasValor1("" + medida.getValor());
                    }

                    if (idTipoMedida == 2054) {
                        dataTrama.setIzqExplorardorasValor2("" + medida.getValor());
                    }

                    if (idTipoMedida == 2055) {
                        dataTrama.setIzqExplorardorasValor3("" + medida.getValor());
                    }

                    //--------------------------------------------------------------
                    //-------    SIMULTANIEDA  FAROLAS ALTAS  Izquierda           ---
                    //-------------------------------------------------------------- 
                    if (idTipoMedida == 2036 || idTipoMedida == 2033 || idTipoMedida == 2034) {
                        if (medida.getSimult().equalsIgnoreCase("Y")) {
                            dataTrama.setIzqAltasSimultaneas("S");
                        }

                        if (medida.getSimult().equalsIgnoreCase("N")) {
                            dataTrama.setIzqAltasSimultaneas("N");
                        }
                    }

                    if (idTipoMedida == 2036) {
                        dataTrama.setIzqAltaIntesidadValor1("" + medida.getValor());
                    }

                    if (idTipoMedida == 2033) {
                        dataTrama.setIzqAltaIntesidadValor2("" + medida.getValor());
                    }

                    if (idTipoMedida == 2034) {
                        dataTrama.setIzqAltaIntesidadValor3("" + medida.getValor());
                    }

                    //--------------------------------------------------------------
                    //-----    SIMULTANIEDA  FAROLAS ALTAS  derecha           ---
                    //--------------------------------------------------------------
                    if (idTipoMedida == 2032 || idTipoMedida == 2037 || idTipoMedida == 2038) {
                        if (medida.getSimult().equalsIgnoreCase("Y")) {
                            dataTrama.setDerAltasSimultaneas("S");

                        } else if (medida.getSimult().equalsIgnoreCase("N")) {
                            dataTrama.setDerAltasSimultaneas("N");
                        }
                    }

                    if (idTipoMedida == 2032) {
                        dataTrama.setDerAltaIntensidadValor1("" + medida.getValor());
                    }

                    if (idTipoMedida == 2037) {
                        dataTrama.setDerAltaIntensidadValor2("" + medida.getValor());
                    }

                    if (idTipoMedida == 2038) {
                        dataTrama.setDerAltaIntensidadValor3("" + medida.getValor());
                    }

                    //--------------------------------------------------------------
                    //-----    SIMULTANIEDA  FAROLAS BAJAS  DERECHA           ---
                    //--------------------------------------------------------------
                    if (idTipoMedida == 2024 || idTipoMedida == 2025 || idTipoMedida == 2026) {
                        if (medida.getSimult().equalsIgnoreCase("Y")) {
                            dataTrama.setDerBajaSimultaneas("S");

                        } else if (medida.getSimult().equalsIgnoreCase("N")) {
                            dataTrama.setDerBajaSimultaneas("N");
                        }
                    }

                    if (idTipoMedida == 2024) {
                        dataTrama.setDerBajaIntensidadValor1("" + medida.getValor());
                    }

                    if (idTipoMedida == 2025) {
                        dataTrama.setDerBajaIntensidadValor2("" + medida.getValor());
                    }

                    if (idTipoMedida == 2026) {
                        dataTrama.setDerBajaIntensidadValor3("" + medida.getValor());
                    }

                    //--------------------------------------------------------------
                    //-----    SIMULTANIEDA  FAROLAS BAJAS  iZQUIERDAS           ---
                    //--------------------------------------------------------------
                    if (idTipoMedida == 2029 || idTipoMedida == 2030 || idTipoMedida == 2031) {
                        if (medida.getSimult().equalsIgnoreCase("Y")) {
                            dataTrama.setIzqBajaSimultaneas("S");

                        } else if (medida.getSimult().equalsIgnoreCase("N")) {
                            dataTrama.setIzqBajaSimultaneas("N");
                        }
                    }

                    if (idTipoMedida == 2029) {
                        dataTrama.setIzqBajaIntensidadValor2("" + medida.getValor());
                    }

                    if (idTipoMedida == 2030) {
                        dataTrama.setIzqBajaIntensidaValor3("" + medida.getValor());
                    }

                    if (idTipoMedida == 2031) {
                        dataTrama.setIzqBajaIntensidadValor1("" + medida.getValor());
                    }
                    //--------------------------------------------------------------
                    //-----             ANGULOS BAJAS DERECHA                    ---
                    //--------------------------------------------------------------

                    if (idTipoMedida == 2040) {
                        dataTrama.setDerBajaInclinacionValor1("" + medida.getValor());
                    }

                    if (idTipoMedida == 2041) {
                        dataTrama.setDerBajaInclinacionValor2("" + medida.getValor());
                    }

                    if (idTipoMedida == 2042) {
                        dataTrama.setDerBajaInclinacionValor3("" + medida.getValor());
                    }

                    //--------------------------------------------------------------
                    //-----             ANGULOS BAJAS DERECHA                    ---
                    //-------------------------------------------------------------
                    if (idTipoMedida == 2044) {
                        dataTrama.setIzqBajaInclinacionValor1("" + medida.getValor());
                    }

                    if (idTipoMedida == 2045) {
                        dataTrama.setIzqBajaInclinacionValor2("" + medida.getValor());
                    }

                    if (idTipoMedida == 2046) {
                        dataTrama.setIzqBajaInclinacionValor3("" + medida.getValor());
                    }

                    //--------------------------------------------------------------
                    //-----           SUMAROTIA GLOBAL                          ---
                    //-------------------------------------------------------------
                    if (idTipoMedida == 2011) {
                        dataTrama.setSumatoriaIntensidad("" + medida.getValor());
                    }

                }
                dataTrama.setTablaAfectada("medidas");
                dataTrama.setIdRegistro("" + prueba.getId());
                System.out.println("----------------- Trama json a guardar ---------");
                System.out.println(dataTrama.toString());
                System.out.println("------------------------------------------------");
                registrarTramas(prueba, dataTrama.toString(), idRun, 2, placa);
            }else if(prueba.getTipoPrueba().getId() == 2 && prueba == null){
                throw new NullPointerException("Esta prueba no existe");
            }
//            }
        } catch (Exception e) {
            System.out.println("Erro en el metodo :tramaSicovLuces()" + e);
        }
    }

    /**
     *
     * @param prueba
     * @param idRun
     */
    public void tramaSicovGases(Prueba prueba, Integer idRun, String placa, char temperatura, String diametro, int tipoGasolina) {
        System.out.println("--------------------------------------------------");
        System.out.println("----------   tramaSicovGases         -------------");
        System.out.println("--------------------------------------------------");

        TramaJsonGases dataTrama = new TramaJsonGases();
        try {
//            for (Prueba prueba : ctxHojaPrueba.getListPruebas()) 
//            {
            if (prueba.getTipoPrueba().getId() == 8) {
                for (Medida medida : prueba.getMedidaList()) {
                    if (medida.getTipoMedida().getId() == 8031) {
                        dataTrama.setTemperaturaAmbiente("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8005) {
                        dataTrama.setRpmRalenti("" + medida.getValor());
                    }

//                        if (medida.getTipoMedida().getId()==8006)
//                            dataTrama.setTempRalenti(""+medida.getValor());
                    if (medida.getTipoMedida().getId() == 8032) {
                        dataTrama.setHumedadRelativa("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8038) {
                        dataTrama.setVelocidadGobernada0("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8039) {
                        dataTrama.setVelocidadGobernada1("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8040) {
                        dataTrama.setVelocidadGobernada2("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8041) {
                        dataTrama.setVelocidadGobernada3("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8033) {
                        dataTrama.setOpacidad0("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8013) {
                        dataTrama.setOpacidad1("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8014) {
                        dataTrama.setOpacidad2("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8015) {
                        dataTrama.setOpacidad3("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8017) {
                        dataTrama.setValorFinal("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8034) {
                        dataTrama.setTemperaturaInicial("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8037) {
                        dataTrama.setTemperaturaFinal("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8001 || medida.getTipoMedida().getId() == 8018) {
                        dataTrama.setHCRalenti("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8001 || medida.getTipoMedida().getId() == 8018) {
                        dataTrama.setHCRalenti("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8002 || medida.getTipoMedida().getId() == 8020) {
                        dataTrama.setCORalenti("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8003 || medida.getTipoMedida().getId() == 8019) {
                        dataTrama.setCO2Ralenti("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8004 || medida.getTipoMedida().getId() == 8021) {
                        dataTrama.setO2Ralenti("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8011) {
                        dataTrama.setRpmCrucero("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8011) {
                        dataTrama.setRpmCrucero("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8007) {
                        dataTrama.setHCCrucero("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8008) {
                        dataTrama.setCOCrucero("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8009) {
                        dataTrama.setCO2Crucero("" + medida.getValor());
                    }

                    if (medida.getTipoMedida().getId() == 8010) {
                        dataTrama.setO2Crucero("" + medida.getValor());
                    }

//                        if (medida.getTipoMedida().getId() == 9115) 
//                            dataTrama.setTemperaturaPrueba("" + medida.getValor());  
                    if (medida.getTipoMedida().getId() == 8006) {
                        if (temperatura == 'C') {
                            dataTrama.setCatalizador("S");

                        } else {
                            dataTrama.setCatalizador("N");
                            dataTrama.setTemperaturaPrueba("" + medida.getValor());
                        }
                    }

                }

                if (prueba.getComentario() != null && prueba.getComentario().equalsIgnoreCase("DILUCION DE MUESTRA")) {
                    dataTrama.setDilucion("true");
                }

                if (diametro != null) {
                    if (tipoGasolina == 3) {
                        dataTrama.setLTOEStandar(String.valueOf(diametro));
                    }
                }

                dataTrama.setTablaAfectada("medidas");
                dataTrama.setIdRegistro("" + prueba.getId());

                System.out.println("----------------- Trama json a guardar gases ---------");
                System.out.println(dataTrama.toString());
                System.out.println("------------------------------------------------");
                registrarTramas(prueba, dataTrama.toString(), idRun, 1, placa);
//                    break;
            }
//            }
        } catch (Exception e) {
            System.out.println("Erro en el metodo : tramaSicovGases()" + e);
        }
    }

    /**
     *
     * @param prueba
     * @param tramaAuditoriaGases
     * @param idRun
     * @param evenSicov
     */
    public void registrarTramas(Prueba prueba, String tramaAuditoriaGases, Integer idRun, Integer evenSicov, String placa) {

        Log.info("---------------------------------------------------");
        Log.info("--------Registrando trama para auditoria Sicov-----");
        Log.info("---------------------------------------------------");

        EntityManager em = getEntityManager();
        AuditoriaSicov audi = new AuditoriaSicov();
        String[] trm = prueba.getFechaAborto().split(";");
        int nvoAudi = Integer.parseInt(trm[1]);
        Query q;

        while (true) {
            try {
                em.getTransaction().begin();
                audi.setIdAuditoriaSICOV(nvoAudi);
                audi.setIdRevision(prueba.getHojaPruebas().getId());
                if (evenSicov == 7) {
                    audi.setSerialEquipoMedicion(" ");
                } else {
                    if (prueba.getSerialEquipo() == null) {
                        audi.setSerialEquipoMedicion("SERIAL NO ENCONTRADO");
                    } else if(prueba.getSerialEquipo().split("~").length>2){
                        audi.setSerialEquipoMedicion(prueba.getSerialEquipo().split("~")[2]);
                    }
                    else {
                        audi.setSerialEquipoMedicion(prueba.getSerialEquipo());
                    }

                }
                audi.setIpEquipoMedicion(trm[0]);  //direccion ip de la maquina
                audi.setFechaEvento(prueba.getFechaFinal());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(prueba.getFechaFinal()); // Configuramos la fecha que se recibe
                calendar.add(Calendar.MILLISECOND, 75859);
                audi.setFechaRegistroBD(calendar.getTime());
                audi.setTipoOperacion(1);
                audi.setTipoEvento(evenSicov);
                audi.setCodigoProveedor("858");
                audi.setIdRUNTCDA(idRun);
                System.out.println("trama que se va guardar en la tabla auditoria sicov: " + tramaAuditoriaGases);
                audi.setTRAMA(tramaAuditoriaGases);
                System.out.println("Se envio la trama");
                audi.setUsuario(prueba.getUsuarioFor().getNombre());
                audi.setIdentificacionUsuario(prueba.getUsuarioFor().getCedula());

                if (prueba.getObservaciones() != null) {
                    audi.setObservacion(prueba.getObservaciones());
                } else {
                    audi.setObservacion(" ");
                }

                audi.setPlaca(placa);
                Prueba pr = em.find(Prueba.class, prueba.getId());
                pr.setFechaAborto("");
                em.merge(pr);
                em.persist(audi);
                em.getTransaction().commit();
                break;
            } catch (Exception ex1) {
                ex1.printStackTrace();
                System.out.println("Error en el metodo : registrarTramas()" + ex1);
                while (true) {
                    System.out.println("entre while Excepcion");
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    try {
                        Thread.sleep(200);
                        System.out.println("hice roolback " + nvoAudi);
                        em.clear();
                        em.getTransaction().begin();
                        System.out.println("inicie transaccion");
                        q = em.createNativeQuery("SELECT MAX(SEQ_COUNT) from sequence WHERE SEQ_NAME = 'AUD_SICOV' ");
                        nvoAudi = (Integer) q.getSingleResult();
                        System.out.println("id auditoria exite voy a buscar uno nuevo nvoAudi " + nvoAudi);
                        SEQUENCE s = em.find(SEQUENCE.class, "AUD_SICOV");
                        nvoAudi = nvoAudi + 1;
                        System.out.println("SEQUENCE " + nvoAudi);
                        s.setSEQCOUNT(nvoAudi);
                        em.merge(s);
                        em.flush();
                        System.out.println("flush " + nvoAudi);
                        em.getTransaction().commit();
                        nvoAudi = nvoAudi + 1;
                        System.out.println("commit al id nvoId " + nvoAudi);
                        break;
                    } catch (Exception ex) {
                        em.getTransaction().rollback();
                        System.out.println("REGISTRO ESTUVO BLOQUEDO " + ex.getMessage());

                    }
                }
            }
        }
    }

//    public void registrarTramas(Prueba prueba, String tramaAuditoria, Integer idRun, Integer evenSicov, String placa) 
//    {
//
//        Log.info("---------------------------------------------------");
//        Log.info("--------Registrando trama para auditoria Sicov-----");
//        Log.info("---------------------------------------------------");
//
//        EntityManager em = getEntityManager();
//        AuditoriaSicov audi = new AuditoriaSicov();
//
//        try 
//        {
//            em.clear();
//            em.getTransaction().begin();
//            audi.setPlaca(placa);
//            Query q = em.createNativeQuery("SELECT MAX(SEQ_COUNT) from sequence WHERE SEQ_NAME = 'AUD_SICOV' ");
//            audi.setIdAuditoriaSICOV((Integer) q.getSingleResult());
//            audi.setIdRevision(prueba.getHojaPruebas().getId());
//
//            String[] trm = prueba.getFechaAborto().split(";");
//            if (trm.length > 0) {
//                audi.setIpEquipoMedicion(trm[0]);
//            }
//
//            audi.setSerialEquipoMedicion(prueba.getSerialEquipo());
//            audi.setFechaEvento(prueba.getFechaFinal());
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(prueba.getFechaFinal()); // Configuramos la fecha que se recibe
//            calendar.add(Calendar.MILLISECOND, 75859);
//            audi.setFechaRegistroBD(calendar.getTime());
//            audi.setTipoOperacion(1);
//            audi.setTipoEvento(evenSicov);
//            audi.setCodigoProveedor("858");
//            audi.setIdRUNTCDA(idRun);
//            audi.setTRAMA(tramaAuditoria);
//            audi.setUsuario(prueba.getUsuarioFor().getNombre());
//            audi.setIdentificacionUsuario(prueba.getUsuarioFor().getCedula());
//            audi.setObservacion(prueba.getObservaciones());
//            
//            SEQUENCE s = em.find(SEQUENCE.class, "AUD_SICOV");
//            s.setSEQCOUNT((Integer) q.getSingleResult() + 1);
//            em.merge(s);
//            em.persist(audi);
//            em.getTransaction().commit();
//        } catch (Exception ex1) 
//        {
//            System.out.println("Error en el metodo : registrarTramas()" +ex1);
//        }
//    }
    public Prueba findLastByHoja(int idHojaPruebas) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT MAX(p.id) FROM Prueba p WHERE p.hojaPruebas.id = :idHojaPruebas AND p.finalizada = 'N'");
        query.setParameter("idHojaPruebas", idHojaPruebas);
        try {
            Integer idPrueba = (Integer) query.getSingleResult();
            if (idPrueba == null) {
                return null;
            }
            return new PruebaJpaController().find(idPrueba);
        } catch (NoResultException ex) {
            return null;
        } finally {

        }
    }

    public boolean findDefecXProfLabrado(int idPrueba) {
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("SELECT count(id_defecto) FROM defxprueba WHERE defxprueba.id_prueba= ? AND  defxprueba.id_defecto in (10095,10094,14016,15050) ");
        query.setParameter(1, idPrueba);
        try {
            Long n = (Long) query.getSingleResult();
            if (n > 0) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            int p = 0;
            return false;
        } finally {

        }
    }

    public List<PruebaDTO> findPruebas(Integer idHojaPruebas) {
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select h.Id_Pruebas,h.Tipo_prueba_for,h.Aprobada,h.Finalizada from pruebas h where h.hoja_pruebas_for = ?", PruebaDTO.class);
        query.setParameter(1, idHojaPruebas);
        try {
            List<PruebaDTO> idPrueba = (List<PruebaDTO>) query.getResultList();

            return idPrueba;
        } catch (NoResultException ex) {
            return null;
        } finally {

        }
    }

    public static void main(String[] args) {
        new PruebaJpaController().findPruebas(11);
    }

    public Prueba findAprobadoByHoja(int idHojaPruebas) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT MAX(p.id) FROM Prueba p WHERE p.hojaPruebas.id = :idHojaPruebas AND p.aprobado = 'N'");
        query.setParameter("idHojaPruebas", idHojaPruebas);
        try {
            Integer idPrueba = (Integer) query.getSingleResult();
            if (idPrueba == null) {
                return null;
            }
            return new PruebaJpaController().find(idPrueba);
        } catch (NoResultException ex) {
            return null;
        } finally {

        }
    }

    public Integer destroyPruebasFromReinspecciones(Integer idPrueba) throws ClassNotFoundException, IOException, SQLException {
        Connection conn = UtilConexion.obtenerConexion();
        String sql = "DELETE FROM reinspxprueba WHERE id_prueba_for = '" + idPrueba + "'";
        PreparedStatement pstm = conn.prepareStatement(sql);
        return pstm.executeUpdate();
    }

    public Integer destroyPruebasFromDefectos(Integer idPrueba) throws ClassNotFoundException, IOException, SQLException {
        Connection conn = UtilConexion.obtenerConexion();
        String sql = "DELETE FROM defxprueba WHERE id_prueba = '" + idPrueba + "'";
        PreparedStatement pstm = conn.prepareStatement(sql);
        return pstm.executeUpdate();
    }
}

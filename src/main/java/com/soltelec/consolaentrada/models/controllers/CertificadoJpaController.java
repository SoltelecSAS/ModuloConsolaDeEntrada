/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.models.controllers;

import com.soltelec.consolaentrada.models.controllers.conexion.PersistenceController;
import com.soltelec.consolaentrada.models.entities.Certificado;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.utilities.Mensajes;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Gerencia Desarrollo de Soluciones Tecnologicas
 */
public class CertificadoJpaController implements Serializable {

    private EntityManager getEntityManager() {
        return PersistenceController.getEntityManager();
    }

    public List<Certificado> findCertificadoByPlaca(String placa) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery query = em.createQuery("SELECT c FROM Certificado c JOIN c.hojaPruebas.vehiculo v WHERE v.placa = :placa", Certificado.class);
            query.setParameter("placa", placa);
            return query.getResultList();
        } finally {
           
        }
    }
    public String maxCertificado() {
        EntityManager em = getEntityManager();
        Object objConsec;
        try {
            TypedQuery query = em.createQuery("SELECT Max(c.consecutivo) FROM Certificado c ", Certificado.class);           
             objConsec= query.getSingleResult();
           
             if(objConsec instanceof  Long){
                 Long i = (Long)objConsec;
                 return  String.valueOf(i);
             }
              if(objConsec instanceof  Integer){
                 Integer i = (Integer)objConsec;
                 return  String.valueOf(i);
             }           
            
        } finally {
           
        }
        return (String) objConsec;
    }

    public Certificado findCertificadosXHoja(Integer tesSheet) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery query = em.createQuery("SELECT c FROM Certificado c c.hojaPruebas hp WHERE hp.TESTSHEET = :tesSheet", Certificado.class);
            query.setParameter("tesSheet", tesSheet);
            return (Certificado) query.getSingleResult();
        } finally {
           
        }
    }

    public List<Certificado> findCertificadoByFecha(Date dinicial, Date dfinal) {
        EntityManager em = null;

        try {
            em = getEntityManager();
            TypedQuery<Certificado> qCert = em.createQuery("SELECT h FROM HojaPruebas h WHERE h.fechaIngreso BETWEEN :fechainicial and :fechafinal", Certificado.class);
            DateFormat dtfInicial = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
            DateFormat dtfFinal = new SimpleDateFormat("yyyy/MM/dd 23:59:00");
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String fechaInicial = dtfInicial.format(dinicial);
            String fechaFinal = dtfFinal.format(dfinal);
            dinicial = format.parse(fechaInicial);
            dfinal = format.parse(fechaFinal);
            qCert.setParameter("fechainicial", dinicial);
            qCert.setParameter("fechafinal", dfinal);
            List<Certificado> lstCertificado = qCert.getResultList();
            return lstCertificado;
        } catch (ParseException ex) {
            Mensajes.mostrarExcepcion(ex);
            return null;
        } finally {
            
        }
    }

    public void nvoCertificado(Certificado nvoCertificado,HojaPruebas hojaPruebas) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();            
             HojaPruebas ctxHP = em.find(HojaPruebas.class, hojaPruebas.getId());
            ctxHP.setEstadoSICOV(hojaPruebas.getEstadoSICOV());
            ctxHP.setConsecutivoRunt(hojaPruebas.getConsecutivoRunt());
            ctxHP.setCertificados(hojaPruebas.getCertificados());
            em.merge(ctxHP);
            em.persist(nvoCertificado);
            em.getTransaction().commit();
        } finally {
          
        }
    }

}

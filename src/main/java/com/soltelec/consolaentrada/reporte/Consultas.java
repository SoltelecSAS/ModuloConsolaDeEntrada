/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//Test branch
package com.soltelec.consolaentrada.reporte;

import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Clase para juntar las consultas de los datos que se daran para supervisión de
 * donde saco la conexion???? no debo estar conectando y desconectando mas bien
 * usando una conexion
 *
 * @author GerenciaDesarrollo
 */
public class Consultas {

    public String obtenerPruebasFaltantes(Connection cn, long idHojaPrueba) throws SQLException {
        ResultSet rs;
        String strPruebasNoFinalizadas = "select Id_Pruebas,Nombre_tipo_prueba from pruebas as pr "
                + "inner join tipo_prueba as tpr on pr.Tipo_prueba_for = tpr.TESTTYPE "
                + "where pr.Id_Pruebas in ("
                + "select max(id_pruebas) from pruebas as p inner join tipo_prueba as tp "
                + "on p.Tipo_prueba_for = tp.TESTTYPE where p.hoja_pruebas_for = ?  group by p.Tipo_prueba_for) and pr.Finalizada = 'N'"
                + " and pr.hoja_pruebas_for = ?";
        PreparedStatement ps = cn.prepareStatement(strPruebasNoFinalizadas);
        ps.setLong(1, idHojaPrueba);
        ps.setLong(2, idHojaPrueba);
        rs = ps.executeQuery();
        StringBuilder sb2 = new StringBuilder("Faltan las sig pruebas: ");
        while (rs.next()) {
            sb2.append(rs.getString("Nombre_tipo_prueba")).append(" ");
        }
        cn.commit();
        return sb2.toString();
    }

    public String obtenerPruebasReprobadas(Connection cn, long idHojaPruebas) throws SQLException {
        ResultSet rs;
        String strPruebasNoAprobadas = "select Id_Pruebas,Nombre_tipo_prueba from pruebas as pr "
                + "inner join tipo_prueba as tpr on pr.Tipo_prueba_for = tpr.TESTTYPE "
                + "where pr.Id_Pruebas in ("
                + "select max(id_pruebas) from pruebas as p inner join tipo_prueba as tp "
                + "on p.Tipo_prueba_for = tp.TESTTYPE where p.hoja_pruebas_for = ?  group by p.Tipo_prueba_for) and pr.Finalizada = 'Y' and pr.Aprobada = 'N' "
                + " and pr.hoja_pruebas_for = ?";
        PreparedStatement ps = cn.prepareStatement(strPruebasNoAprobadas);
        ps.setLong(1, idHojaPruebas);
        ps.setLong(2, idHojaPruebas);
        rs = ps.executeQuery();
        StringBuilder sb2 = new StringBuilder("Pruebas Reprobadas: ");
        while (rs.next()) {
            sb2.append(rs.getString("Nombre_tipo_prueba")).append(" ");
        }
        return sb2.toString();
    }

    public String obtenerInfoCertificado(Connection cn, long idHojaPrueba) throws SQLException {
        ResultSet rs;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String consulta = "SELECT * FROM certificados WHERE  TESTSHEET = ? AND PRINTED = 'Y' AND ANULED = 'N'";
        PreparedStatement ps = cn.prepareStatement(consulta);
        ps.setLong(1, idHojaPrueba);
        rs = ps.executeQuery();
        StringBuilder sb = new StringBuilder();
        while (rs.next()) {
            sb.append(String.format("%s\t %d \t %s \t", "CERTIFICADO", rs.getInt("CONSECUTIVE"), sdf.format(rs.getDate("PRINTDATE"))));
        }
        return sb.toString();

    }

    public String evaluarPrueba(Connection cn, long idHojaPrueba) throws SQLException {
        ResultSet rs;
//        String strTotal = "select count(d.Tipo_defecto) from pruebas as pr "
//                + "inner join defxprueba as dp on pr.id_pruebas=dp.id_prueba "
//                + "inner join defectos as d on dp.id_defecto = d.CARDEFAULT "
//                + "inner join grupos as g on d.DEFGROUP = g.DEFGROUP "
//                + "inner join sub_grupos as sg on g.DEFGROUP = sg.SCDEFGROUPSUB "
//                + "where pr.id_pruebas in "
//                + "(select max(id_pruebas) from pruebas as p where p.hoja_pruebas_for = ?  AND p.Finalizada = 'Y' group by  p.Tipo_prueba_for ) "
//                + "and  d.Tipo_defecto=? and pr.hoja_pruebas_for = ? and pr.Finalizada = 'Y'";
        String strTotal = "SELECT COUNT(d.Tipo_defecto)\n"
                + "FROM pruebas AS pr\n"
                + "INNER JOIN defxprueba AS dp ON pr.id_pruebas=dp.id_prueba\n"
                + "INNER JOIN defectos AS d ON dp.id_defecto = d.CARDEFAULT\n"
                + "INNER JOIN sub_grupos AS sg ON sg.SCDEFGROUPSUB = d.DEFGROUPSSUB\n"
                + "WHERE pr.id_pruebas IN \n"
                + " (\n"
                + "SELECT MAX(id_pruebas)\n"
                + "FROM pruebas AS p\n"
                + "WHERE p.hoja_pruebas_for = ? AND p.Finalizada = 'Y'\n"
                + "GROUP BY p.Tipo_prueba_for) AND d.Tipo_defecto=? AND pr.hoja_pruebas_for = ? AND pr.Finalizada = 'Y'";
        PreparedStatement consultaTotal = cn.prepareStatement(strTotal);
        consultaTotal.setLong(1, idHojaPrueba);
        consultaTotal.setString(2, "A");
        consultaTotal.setLong(3, idHojaPrueba);
        rs = consultaTotal.executeQuery();
        
        int totalDefA =0;
        if (rs.next()) {
            totalDefA = rs.getInt(1);
            //parametros.put("TotalDefA", String.valueOf(totalDefA));
        }
        
        consultaTotal.clearParameters();
        consultaTotal.setLong(1, idHojaPrueba);
        consultaTotal.setString(2, "B");
        consultaTotal.setLong(3, idHojaPrueba);
        rs = consultaTotal.executeQuery();
        
        int totalDefB =0;
        if (rs.next()) {
            totalDefB = rs.getInt(1);
        }

        String sqlPruebasFinalizadas = "select count(*) from pruebas as pr where pr.Id_pruebas in ("
                + "select max(id_pruebas) from pruebas as p inner join hoja_pruebas as hp on p.hoja_pruebas_for = hp.TESTSHEET "
                + "where hp.TESTSHEET = ? group by p.tipo_prueba_for) and pr.Finalizada = 'Y' and pr.Aprobada = 'Y' and pr.hoja_pruebas_for = ?";
        //Cuenta las pruebas finalizadas y aprobadas numero que debe ser igual al numero de pruebas
        //autorizadas
        PreparedStatement consultaPruebasFinalizadas = cn.prepareStatement(sqlPruebasFinalizadas);
        consultaPruebasFinalizadas.setLong(1, idHojaPrueba);
        consultaPruebasFinalizadas.setLong(2, idHojaPrueba);
        rs = consultaPruebasFinalizadas.executeQuery();
        rs.next();
        int numeroPruebasFinalizadasyAprobadas = rs.getInt(1);//no tiene en cuenta cuando tiene pruebas pendientes
        //Contar las pruebas autorizadas es decir contar los tipos de pruebas autorizadas
        String pruebasAutorizadas = "select count(*) from ( "
                + "select max(id_pruebas) from pruebas as p "
                + "inner join hoja_pruebas as hp on p.hoja_pruebas_for = hp.TESTSHEET "
                + "where hp.TESTSHEET = ? group by p.tipo_prueba_for) as tmp";
        PreparedStatement consultaPruebasAutorizadas = cn.prepareStatement(pruebasAutorizadas);
        consultaPruebasAutorizadas.setLong(1, idHojaPrueba);
        rs = consultaPruebasAutorizadas.executeQuery();
        rs.next();
        int numeroPruebasAutorizadas = rs.getInt(1);//trae el numero de pruebas autorizadas las cuales

        String strPruebasNoFinalizadas = "select count(*) from ("
                + "select max(id_pruebas) from pruebas as p inner join tipo_prueba as tp "
                + "on p.Tipo_prueba_for = tp.TESTTYPE where p.hoja_pruebas_for = ? and p.Finalizada = 'N' group by p.Tipo_prueba_for) as tmp";
        PreparedStatement psNoFinalizadas = cn.prepareStatement(strPruebasNoFinalizadas);
        psNoFinalizadas.setLong(1, idHojaPrueba);
        rs = psNoFinalizadas.executeQuery();
        int numeroPruebasNoFinalizadas = 0;
        if (rs.next()) {
            numeroPruebasNoFinalizadas = rs.getInt(1);
        }
        //numero de pruebas finalizadas menos numero de pruebas pendientes
        numeroPruebasFinalizadasyAprobadas = numeroPruebasFinalizadasyAprobadas - numeroPruebasNoFinalizadas;
        String sql = "SELECT CARTYPE,Modelo,Tiempos_motor,FUELTYPE,SERVICE,CLASS,SPSERVICE from vehiculos as v inner join hoja_pruebas as hp on v.CAR=hp.Vehiculo_for where hp.TESTSHEET = ?";
        PreparedStatement ps = cn.prepareStatement(sql);
        ps.setLong(1, idHojaPrueba);
        ResultSet rs1 = ps.executeQuery();
        rs1.next();//primer registroy

        //PRUEBAS REPROBADAS:
        String strPruebasNoAprobadas = "select count(pr.Id_Pruebas) from pruebas as pr "
                + "where pr.Id_Pruebas in ("
                + "select max(id_pruebas) from pruebas as p inner join tipo_prueba as tp "
                + "on p.Tipo_prueba_for = tp.TESTTYPE where p.hoja_pruebas_for = ?  group by p.Tipo_prueba_for) and pr.Finalizada = 'Y' and pr.Aprobada = 'N' "
                + " and pr.hoja_pruebas_for = ?";

        PreparedStatement psReprobadas = cn.prepareStatement(strPruebasNoAprobadas);
        psReprobadas.setLong(1, idHojaPrueba);
        psReprobadas.setLong(2, idHojaPrueba);
        ResultSet rsReprobadas = psReprobadas.executeQuery();
        int numeroPruebasReprobadas = 0;
        if (rsReprobadas.next()) {
            numeroPruebasReprobadas = rsReprobadas.getInt(1);
        }

        String evaluacion = "reprobada";
        if (rs1.getInt("CARTYPE") == 4 || rs1.getInt("CARTYPE") == 5) {//Motos y MotoCarros 5 pruebas            
                if (totalDefA > 0) {
                    evaluacion = "reprobada";
                } else if (totalDefB < 5) {
                    evaluacion = "aprobada";
                } else {
                    evaluacion = "reprobada";
                }//fin si no tiene defectos tipo A
            if (numeroPruebasNoFinalizadas > 0 && totalDefB < 5 && totalDefA == 0 ) {
                evaluacion = "noFinalizada";
            } 
        } else if (rs1.getInt("CARTYPE") == 3) {//Pesados
            //Pesado no hace suspension 6 preubas obligatorias
            //sinembargo debe haber terminado y aprobado todas las pruebas
            if (numeroPruebasNoFinalizadas > 0) {
                evaluacion = "noFinalizada";
            }
            if (numeroPruebasFinalizadasyAprobadas >= 6 && numeroPruebasFinalizadasyAprobadas == numeroPruebasAutorizadas) {
                if (totalDefA > 0) {
                    evaluacion = "reprobada";
                } else {//si es de servicio publico
                    int servicio = rs1.getInt("SERVICE");
                    if (servicio == 3 || servicio == 1 || servicio == 4) {//particular u oficial o diplomatico
                        if (totalDefB < 10) {
                            evaluacion = "aprobada";
                        } else {
                            evaluacion = "reprobada";
                        }
                    } else if (servicio == 2 || servicio == 5) {//publico 3 especiales 5
                        if (totalDefB < 5) {//con 5 o mas se reprueba
                            evaluacion = "aprobada";
                        } else {
                            evaluacion = "reprobada";
                        }
                    }//end servicio publico

                    int ensenanza = rs1.getInt("CLASS");
                    if (ensenanza == 27) {//clase 27 es clase ensenianza
                        if (totalDefB < 5) {//con 5 o mas se reprueba
                            evaluacion = "aprobada";
                        } else {
                            evaluacion = "reprobada";
                        }
                    }//fin ensenanza

                }//fin else defectos tipoA
            } else//si el numero de pruebas aprobadas no alcanza
             if (numeroPruebasNoFinalizadas > 0) {
                    evaluacion = "noFinalizada";
                } else {
                    evaluacion = "reprobada";
                }//fn else de no terminar un minimo de pruebas
        } else if (rs1.getInt("CARTYPE") == 2) {//4x4
            //1.Inspeccion Sensorial 2.Gases 3.Frenos 4.Luces 5. Suspension
            //6.Foto //7.sonometro 7 pruebas como minimo de ahi hay que mirar si faltan pruebas 18 agosto 6 pruebas minimas
            if (numeroPruebasNoFinalizadas > 0) {
                evaluacion = "noFinalizada";
            }
            if (numeroPruebasFinalizadasyAprobadas >= 6 && numeroPruebasAutorizadas == numeroPruebasFinalizadasyAprobadas) {
                //si es de servicio publico
                if (totalDefA > 0) {
                    evaluacion = "aprobada";
                } else {//si no hay defectos tipo A verficar los defectos tipo B
                    int servicio = rs1.getInt("SERVICE");
                    if (servicio == 3 || servicio == 1 || servicio == 4) {//particular 
                        if (totalDefB < 10) {
                            evaluacion = "aprobada";
                        } else {
                            evaluacion = "reprobada";
                        }
                    } else if (servicio == 2 || servicio == 5) {//publico
                        if (totalDefB < 5) {//con 5 o mas se reprueba
                            evaluacion = "aprobada";
                        } else {
                            evaluacion = "reprobada";
                        }
                    }
                    int ensenanza = rs1.getInt("CLASS");
                    if (ensenanza == 27) {
                        if (totalDefB < 5) {//con 5 o mas se reprueba
                            evaluacion = "aprobada";
                        } else {
                            evaluacion = "reprobada";
                        }
                    }//fin ensenanza

                }//fin else defectos tipoA
            } else if (numeroPruebasNoFinalizadas > 0) {
                evaluacion = "noFinalizada";
            } else {
                evaluacion = "reprobada";
            }

        } else if (rs1.getInt("CARTYPE") == 1 || rs1.getInt("CARTYPE") == 110 || rs1.getInt("CARTYPE") == 109) {//Livianos
            //1.Inspeccion Sensorial 2.Gases 3.Frenos 4.Luces 5. Suspension 6. Desviacion
            //7.Foto //8.sonometro 7 no a todos los livianos se les hace suspension

            if (numeroPruebasNoFinalizadas > 0) {
                evaluacion = "noFinalizada";
            }
            if (numeroPruebasFinalizadasyAprobadas >= 7 && numeroPruebasFinalizadasyAprobadas == numeroPruebasAutorizadas) {
                //si es de servicio publico

                if (totalDefA > 0) {
                    evaluacion = "reprobada";
                } else {//si no hay defectos tipo A
                    int servicio = rs1.getInt("SERVICE");
                    if (servicio == 3 || servicio == 1 || servicio == 4) {//particular
                        if (totalDefB < 10) {
                            evaluacion = "aprobada";
                        } else {
                            evaluacion = "reprobada";
                        }
                    } else if (servicio == 2 || servicio == 5) {//publico 2 , especiales 5
                        if (totalDefB < 5) {//con 5 o mas se reprueba
                            evaluacion = "aprobada";
                        } else {
                            evaluacion = "reprobada";
                        }
                    }//end servicio publico
                    int ensenanza = rs1.getInt("CLASS");
                    if (ensenanza == 27) {
                        if (totalDefB < 5) {//con 5 o mas se reprueba
                            evaluacion = "aprobada";
                        } else {
                            evaluacion = "reprobada";
                        }
                    }//fin ensenanza

                }//fin else defectos tipoA
                if (numeroPruebasReprobadas > 0) {
                    evaluacion = "reprobada";
                }
            } else if (numeroPruebasNoFinalizadas > 0) {
                evaluacion = "noFinalizada";
            } else {
                evaluacion = "reprobada";
            }
        }//fin de livianos
        else if (rs1.getInt("CARTYPE") == 7) {//REMOLQUES

            //verifica que la prueba que se hizo sea de Inspeccion Sensorial
            if (numeroPruebasFinalizadasyAprobadas >= 1 && numeroPruebasFinalizadasyAprobadas == numeroPruebasAutorizadas) {
                if (totalDefA > 0) {
                    evaluacion = "aprobada";
                } else {//si no hay defectos tipo A verficar los defectos tipo B
                    int servicio = rs1.getInt("SERVICE");
                    if (servicio == 3 || servicio == 1 || servicio == 4) {//particular
                        if (totalDefB < 10) {
                            evaluacion = "aprobada";
                        } else {
                            evaluacion = "reprobada";
                        }
                    } else if (servicio == 2 || servicio == 5) {//publico
                        if (totalDefB < 5) {//con 5 o mas se reprueba
                            evaluacion = "aprobada";
                        } else {
                            evaluacion = "reprobada";
                        }
                    }
                    int ensenanza = rs1.getInt("CLASS");
                    if (ensenanza == 27) {
                        if (totalDefB < 5) {//con 5 o mas se reprueba
                            evaluacion = "aprobada";
                        } else {
                            evaluacion = "reprobada";
                        }
                    }//fin ensenanza

                }//fin else defectos tipoA
            } else {

                evaluacion = "reprobada";
            }

        } else {
            evaluacion = "vehiculoInvalido";
        }
        return evaluacion;
    }

    /**
     * Metodo para registrar consecutivo del runt para el Formato de Revision
     *
     * @param consecutivo
     * @param idHojaPrueba
     * @param aprobada
     * @param cn
     * @throws java.sql.SQLException
     */
    public void cerrarRevision(long consecutivo, long idHojaPrueba, boolean aprobada, Connection cn) throws SQLException {
        String strCerrar = "UPDATE hoja_pruebas SET consecutivo_runt = ?,Finalizada = 'Y',Aprobado = ? WHERE TESTSHEET = ?  ";
        PreparedStatement ps = cn.prepareStatement(strCerrar);

        ps.setLong(1, consecutivo);

        if (aprobada) {
            ps.setString(2, "Y");
        } else {
            ps.setString(2, "N");
        }
        ps.setLong(3, idHojaPrueba);
        ps.executeUpdate();
        cn.commit();
    }

    /**
     * Obtiene el estado de la Revision: Finalizada y Aprobada =
     * FinalizadaAprobada Finalizada y Reprobada = FinalizadaReprobada No
     * Finalizada = NoFinalizada Anulada= Anulada
     *
     * @param idHojaPrueba
     * @param cn
     * @return
     * @throws java.sql.SQLException
     */
    public String obtenerEstadoRevision( HojaPruebas ctxHojaPrueba) throws SQLException, Exception {

        String estado = null;
   
            String strFinalizada = ctxHojaPrueba.getFinalizada();
            String strAprobada = ctxHojaPrueba.getAprobado();
            String strAnulado = ctxHojaPrueba.getAnulado();

            if (strAnulado.equalsIgnoreCase("Y")) {
                estado = "Anulada";
            } else if (strFinalizada.equalsIgnoreCase("N")) {
                estado = "NoFinalizada";
            } else if (strAprobada.equalsIgnoreCase("Y")) {
                estado = "FinalizadaAprobada";
            } else if (strAprobada.equalsIgnoreCase("N")) {
                estado = "FinalizadaReprobada";
            }//end aprobada no//end Finalizada si   //fin no anulada        
        return estado;
    }//end of method

    public long obtenerConsecutivo(long idHojaPruebas, Connection cn) throws SQLException, Exception {
        String strConsulta = "SELECT consecutivo_runt FROM hoja_pruebas WHERE TESTSHEET = ?";
        PreparedStatement ps = cn.prepareStatement(strConsulta);
        ps.setLong(1, idHojaPruebas);
        long consecutivoRunt;
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            try {
                consecutivoRunt = rs.getLong("consecutivo_runt");
            } catch (NullPointerException nexc) {
                System.out.println("Consecutivo es nulo");
              
                return -1;
            }
            return consecutivoRunt;
        } else {
            JOptionPane.showMessageDialog(null, "Error obteniendo el consecutivo");
            throw new Exception("error obteniendo el consecutivo");
        }
        

    }

    public void actualizarConsecutivo(long consecutivo, long idHojaPrueba, Connection cn) throws SQLException {
        String strConsulta = "UPDATE hoja_pruebas SET consecutivo_runt = ? WHERE TESTSHEET = ? ";
        PreparedStatement ps = cn.prepareStatement(strConsulta);
        ps.setLong(1, consecutivo);
        ps.setLong(2, idHojaPrueba);
        ps.executeUpdate();
    }

    /**
     * Metodo para obtener el consecutivo runt de la ultima hoja de prueba segun
     * los parametros pasados, en total se manejaran 4 consecutivos: 1. Aprobada
     * = true - Preventiva = true 2. Aprobada = true - Preventiva = false 3.
     * Aprobada = false - Preventiva = true 4. Aprobada = false - Preventiva =
     * false Si la consulta devuelve null se devolveria 0 con el cual el primer
     * consecutivo iniciaria desde 1 ya que al leer el metodo le suma 1 al valor
     * devuelto.
     *
     * @param isAprobada
     * @param isPreventiva
     * @param cn
     * @return consecutivo correspondiente a los parametros, o 0 si no hay
     * consecutivo registrado.
     * @throws SQLException
     */
    public long obtenerSiguienteConsecutivo(boolean isAprobada, boolean isPreventiva, Connection cn) throws SQLException {
        String sql = "SELECT MAX(CONVERT (h.consecutivo_runt, SIGNED)) FROM hoja_pruebas h WHERE h.Aprobado = ? AND h.preventiva = ?";

        PreparedStatement ps = cn.prepareStatement(sql);

        ps.setString(1, isAprobada ? "Y" : "N");
        ps.setString(2, isPreventiva ? "Y" : "N");

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getLong(1);
        } else {
            return 0;
        }

    }//end of method obtenerSiguienteConsecutivo

    public void actualizarConsecutivoRUNT(boolean aprobado, long consecutivo, Connection cn) throws SQLException {
        String strAprobado = "UPDATE cda SET consecutivo_apr = ? WHERE id_cda = 1";
        String strReprobado = "UPDATE cda SET consecutivo_rep = ? WHERE id_cda = 1";
        PreparedStatement ps;
        if (aprobado) {
            ps = cn.prepareStatement(strAprobado);
        } else {
            ps = cn.prepareStatement(strReprobado);
        }

        ps.setLong(1, consecutivo);
        ps.executeUpdate();

    }

    /**
     * Consulta de la tabla certificados el consecutivo asignado por el RUNT
     * para el certificado, NO EL CONSECUTIVO DEL PREIMPRESO.
     *
     * @return
     * @param cn
     * @throws java.sql.SQLException
     */
    public long consultarConsecutivoRUNTCertificados(Connection cn) throws SQLException {

        String consulta = "SELECT consecutivo_cert FROM cda WHERE id_cda=1";
        PreparedStatement ps = cn.prepareStatement(consulta);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getLong(1);
        } else {
            throw new SQLException("No se encuentra el consecutivo para certificados que asigna el RUNT");
        }

    }//end of method consultarConsecutivoRUNTCertificados

    /**
     * Metodo que cambia el consecutivo que asigna el RUNT para los
     * certificados. Este consecutivo se encuentra en la tabla cda con el nombre
     * consecutivo_cert
     *
     * @param consecutivo
     * @param cn
     * @throws SQLException
     */
    public void actualizarConsecutivoRUNTCertificados(long consecutivo, Connection cn) throws SQLException {

        String strSentencia = "UPDATE cda SET consecutivo_cert = ? WHERE id_cda = 1";
        PreparedStatement ps = cn.prepareStatement(strSentencia);
        ps.setLong(1, consecutivo);
        ps.executeUpdate();

    }//end of method 

    /**
     * Metodo que consulta el anterior numero consecutivo del certificado para
     * cuando se deben expedir duplicados.
     *
     * @param idCertificado
     * @param cn
     * @return
     * @throws SQLException
     */
    public long consultarCertificadoRUNTAnteriorCertificado(long idCertificado, Connection cn) throws SQLException {

        String strConsulta = "SELECT consecutivo_runt FROM certificados WHERE CERTIFICATE = ?";
        PreparedStatement ps = cn.prepareStatement(strConsulta);
        ps.setLong(1, idCertificado);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getLong(1);
        } else {
            throw new SQLException("No se encuentra consecutivo del certificado anterior");
        }

    }//end of method

    public void actualizarConsecuivoRUNTdeCertificado(long idCertificado, long consecutivoRUNTNuevo, Connection cn) throws SQLException {

        String strSentencia = "UPDATE certificados SET consecutivo_runt = ? WHERE CERTIFICATE = ?";
        PreparedStatement ps = cn.prepareStatement(strSentencia);
        ps.setLong(1, consecutivoRUNTNuevo);
        ps.setLong(2, idCertificado);
        ps.executeUpdate();

    }

    public int numeroPruebasNoFinalizadas(long idHojaPrueba, Connection cn) throws SQLException {

        String strPruebasNoFinalizadas = "select count(*) from ("
                + "select max(id_pruebas) from pruebas as p inner join tipo_prueba as tp "
                + "on p.Tipo_prueba_for = tp.TESTTYPE where p.hoja_pruebas_for = ? and p.Finalizada = 'N' group by p.Tipo_prueba_for) as tmp";
        PreparedStatement psNoFinalizadas = cn.prepareStatement(strPruebasNoFinalizadas);
        psNoFinalizadas.setLong(1, idHojaPrueba);
        ResultSet rs;
        rs = psNoFinalizadas.executeQuery();
        int numeroPruebasNoFinalizadas = -1;
        if (rs.next()) {
            numeroPruebasNoFinalizadas = rs.getInt(1);
        }
        return numeroPruebasNoFinalizadas;

    }

    public Timestamp obtenerFechaActual(Connection cn) throws SQLException {

        String strConsulta = "SELECT NOW()";
        PreparedStatement ps = cn.prepareStatement(strConsulta);
        ResultSet rs = ps.executeQuery();
        rs.next();
        Timestamp ts = rs.getTimestamp(1);
        return ts;
    }

    public boolean isRevisionPreventiva(HojaPruebas ctxHojaPrueba) throws SQLException { 
        String eve = ctxHojaPrueba.getPreventiva();
        if(ctxHojaPrueba.getPreventiva().equalsIgnoreCase("Y")){
            return true;
        }else{
            return false;
        }   
    }

    public long obtenerSiguienteConsecutivoPreimpreso(Connection cn) throws SQLException {
        long longConsecutivoPreimpreso = -1;
        String strUltimoConsecutivo = "SELECT CONSECUTIVE FROM  certificados order by certificate desc limit 1";
        PreparedStatement psUltimoConsecutivo = cn.prepareStatement(strUltimoConsecutivo);
        ResultSet rsUltimoConsecutivo = psUltimoConsecutivo.executeQuery();

        if (rsUltimoConsecutivo.next()) {
            longConsecutivoPreimpreso = rsUltimoConsecutivo.getLong(1);

        }
        return longConsecutivoPreimpreso;
    }

    public boolean isReinspeccion(long idHojaPrueba, Connection cn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pruebas p WHERE p.hoja_pruebas_for = ? AND p.Tipo_prueba_for = 1 AND p.abortada <> 'A' ";
        PreparedStatement preparedStatement = cn.prepareStatement(sql);
        preparedStatement.setLong(1, idHojaPrueba);
        ResultSet resultSet = preparedStatement.executeQuery();
        int reinspeccion = 0;
        if (resultSet.next()) {
            reinspeccion = resultSet.getInt(1);
        }
        return reinspeccion > 1;
    }

}

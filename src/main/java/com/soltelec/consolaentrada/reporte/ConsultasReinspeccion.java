/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.reporte;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class ConsultasReinspeccion {

    //private Connection cn;
    /**
     * Registra la reinspección en este caso como reprobada debido a que seria
     * por la vuelta del equipo
     *
     * @param idHojaPrueba
     * @param comentario
     * @param consecutivo
     * @param fechaAnterior
     * @param fechaSiguiente
     * @param aprobada
     * @param numeroIntento
     * @param cn
     * @throws java.sql.SQLException
     */
    public void registrarReinspeccion(long idHojaPrueba, String comentario, String consecutivo, Timestamp fechaAnterior, Timestamp fechaSiguiente, String aprobada, int numeroIntento, Connection cn) throws SQLException {

        int numeroReinspeccion = obtenerUltimaReinspeccionAprobada(idHojaPrueba, cn);
        if (numeroReinspeccion > 0) {//no registrar reinspeccion si ya existe una aprobada
            return;
        }

        String strInsertar = "INSERT INTO reinspecciones(aprobada,intento,"
                + "comentarios,hoja_pruebas,fecha_anterior,fecha_siguiente,consecutivo_runt) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement ps = cn.prepareStatement(strInsertar);
        ps.setString(1, aprobada);
        ps.setInt(2, numeroIntento);//por lo pronto
        ps.setString(3, comentario);
        ps.setLong(4, idHojaPrueba);
        ps.setTimestamp(5, fechaAnterior);
        ps.setTimestamp(6, fechaSiguiente);
        ps.setString(7, consecutivo);
        ps.executeUpdate();
        borrarComentario(idHojaPrueba, cn);

    }//

    public void registrarPruebasReinspeccion(List<Integer> listaPruebas, int idReinspeccion, Connection cn) throws SQLException {

        String strInsercion = "INSERT INTO reinspxprueba(id_reinspeccion, id_prueba_for) VALUES (?,?)";
        PreparedStatement ps = cn.prepareStatement(strInsercion);

        for (Integer idPrueba : listaPruebas) {
            ps.clearParameters();
            ps.setInt(1, idReinspeccion);
            ps.setInt(2, idPrueba);
            ps.executeUpdate();
        }

    }

    /**
     * Obtiene la ultima inspeccion asociada a la hoja de prueba.
     *
     * @param testsheet
     * @param cn
     * @return
     * @throws SQLException
     */
    public int obtenerIdReinspeccion(Integer testsheet, Connection cn) throws SQLException {

        String strConsulta = "SELECT MAX(reinspecciones.id_reinspeccion) from reinspecciones where hoja_pruebas = ?";
        PreparedStatement ps = cn.prepareStatement(strConsulta);
        ps.setInt(1, testsheet);
        ResultSet rs = ps.executeQuery();
        int idInspeccion = -1;
        if (rs.next()) {
            idInspeccion = rs.getInt(1);
        } else {

        }
        return idInspeccion;
    }

    public int obtenerIdHojaPruebas(int idReinspeccion, Connection cn) throws SQLException {

        String strConsulta = "SELECT hoja_pruebas FROM reinspecciones WHERE id_reinspeccion = ?";
        PreparedStatement ps = cn.prepareStatement(strConsulta);
        ps.setInt(1, idReinspeccion);
        ResultSet rs = ps.executeQuery();
        int idHojaPruebas = -1;

        if (rs.next()) {
            idHojaPruebas = rs.getInt(1);
        }

        return idHojaPruebas;
    }

    public List<Integer> obtenerPruebasReinspeccion(int hojaPrueba, Connection cn) throws Exception {

        String strConsulta = "SELECT pruebas.Id_Pruebas  FROM pruebas WHERE pruebas.hoja_pruebas_for = ? AND pruebas.Id_Pruebas IN ( SELECT MIN(pruebas.Id_Pruebas) FROM pruebas WHERE pruebas.hoja_pruebas_for = ?  group by pruebas.Tipo_prueba_for)";
        PreparedStatement ps = cn.prepareStatement(strConsulta);
        ps.setInt(1, hojaPrueba);
        ps.setInt(2, hojaPrueba);

        ResultSet rs = ps.executeQuery();
        List<Integer> listaPruebas = new ArrayList<>();

        while (rs.next()) {
            listaPruebas.add(rs.getInt(1));
        }
        return listaPruebas;
    }

    public List<Integer> obtenerReinspecciones(long idHojaPruebas, Connection cn) throws SQLException {

        String consulta = "SELECT id_reinspeccion FROM reinspecciones WHERE hoja_pruebas = ?";
        PreparedStatement ps = cn.prepareStatement(consulta);
        ps.setLong(1, idHojaPruebas);
        ResultSet rs = ps.executeQuery();
        List<Integer> listaReinspecciones = new ArrayList<>();

        while (rs.next()) {

            listaReinspecciones.add(rs.getInt(1));

        }

        return listaReinspecciones;

    }

    public Timestamp obtenerFechaSiguiente(int idReinspeccion, Connection cn) throws SQLException {

        String strConsulta = "SELECT fecha_siguiente FROM reinspecciones WHERE id_reinspeccion = ? ";
        PreparedStatement ps = cn.prepareStatement(strConsulta);
        ps.setInt(1, idReinspeccion);
        ResultSet rs = ps.executeQuery();
        Timestamp timeStamp = null;
        if (rs.next()) {

            timeStamp = rs.getTimestamp(1);
        }
        return timeStamp;

    }

    public int obtenerIntentoAnterior(int idReinspeccion, Connection cn) throws SQLException {
        String strConsulta = "SELECT intento FROM reinspecciones WHERE id_reinspeccion = " + idReinspeccion;
        PreparedStatement ps = cn.prepareStatement(strConsulta);
        ps.setInt(1, idReinspeccion);
        ResultSet rs = ps.executeQuery();

        int numeroIntento = -1;

        if (rs.next()) {
            numeroIntento = rs.getInt(1);
        }

        return numeroIntento;
    }

    public int obtenerUltimaReinspeccionAprobada(long hojaPruebas, Connection cn) throws SQLException {

        int ultimaReinspeccionAprobada = -1;
        String consulta = " SELECT id_reinspeccion FROM reinspecciones WHERE aprobada = 'Y' and hoja_pruebas = ?";
        PreparedStatement ps = cn.prepareStatement(consulta);
        ps.setLong(1, hojaPruebas);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {

            ultimaReinspeccionAprobada = rs.getInt(1);
        }

        return ultimaReinspeccionAprobada;
    }

    public void finalizarHojaPrueba(long idHojaPrueba, Connection cn) throws SQLException {

        String strCerrarHojaPruebas = "UPDATE hoja_pruebas SET Finalizada='Y',Aprobado='Y' WHERE hoja_pruebas.TESTSHEET = ?";
        PreparedStatement psCerrarHojaPruebas = cn.prepareStatement(strCerrarHojaPruebas);
        psCerrarHojaPruebas.setLong(1, idHojaPrueba);
        psCerrarHojaPruebas.executeUpdate();
    }

    private void borrarComentario(long idHojaPrueba, Connection cn) throws SQLException {

        String strBorrar = "UPDATE hoja_pruebas SET Comentarios_cda = ' ' WHERE TESTSHEET = ?";
        PreparedStatement ps = cn.prepareStatement(strBorrar);
        ps.setLong(1, idHojaPrueba);
        ps.executeUpdate();

    }

    public void aumentarConsecutivoHoja(long idHojaPrueba, int numeroIntentos, Connection cn) throws SQLException {

        String strActualizar = "UPDATE hoja_pruebas SET Numero_intentos=? WHERE TESTSHEET=?";
        PreparedStatement ps = cn.prepareStatement(strActualizar);
        ps.setInt(1, numeroIntentos);
        ps.setLong(2, idHojaPrueba);
        ps.executeUpdate();

    }
}

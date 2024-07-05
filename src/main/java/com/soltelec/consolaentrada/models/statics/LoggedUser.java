/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.statics;

/**
 *
 * @author Dany
 */
public class LoggedUser {

    private static Integer idUsuario;
    private static String nick;
    private static Boolean administrador;

    public static Integer getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(Integer idUsuario) {
        LoggedUser.idUsuario = idUsuario;
    }

    public static String getNick() {
        return nick;
    }

    public static void setNick(String nick) {
        LoggedUser.nick = nick;
    }

    public static Boolean isAdministrador() {
        return administrador;
    }

    public static void setAdministrador(Boolean administrador) {
        LoggedUser.administrador = administrador;
    }
    }

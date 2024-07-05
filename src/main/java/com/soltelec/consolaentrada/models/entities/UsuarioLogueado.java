/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

/**
 *
 * @author Dany
 */
public class UsuarioLogueado {
    private static Integer idUsuario;
    private static String nick;

    public static Integer getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(Integer idUsuario) {
        UsuarioLogueado.idUsuario = idUsuario;
    }

    public static String getNick() {
        return nick;
    }

    public static void setNick(String nick) {
        UsuarioLogueado.nick = nick;
    }
}

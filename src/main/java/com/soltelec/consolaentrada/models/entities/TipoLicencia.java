/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.models.entities;

/**
 *
 * @author GerenciaDesarrollo
 */
public enum TipoLicencia {
    Z1("01"),
    Z2("02"),
    Z3("03"),
    Z4("04"),
    Z5("05"),
    Z6("06"),
    A1("A1"),
    A2("A2"),
    A3("A3"),
    B1("B1"),
    B2("B2"),
    B3("B3"),
    C1("C1"),
    C2("C2"),
    C3("C3");

    private final String tipoLicencia;

    private TipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    @Override
    public String toString() {
        return tipoLicencia;
    }
    
}

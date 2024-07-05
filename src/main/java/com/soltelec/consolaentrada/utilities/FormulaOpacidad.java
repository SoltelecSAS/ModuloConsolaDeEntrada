/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.utilities;

import com.soltelec.consolaentrada.models.entities.Vehiculo;

import com.soltelec.consolaentrada.models.controllers.VehiculoJpaController;
import java.util.Objects;


/**
 *
 * @author user
 */
public class FormulaOpacidad {
    
    
    public static Double opacidad(Double opacidad, Double diametro) throws ArithmeticException{
        if (Objects.isNull(diametro)) {
            diametro = 0.430;
        }
        double a = Math.log(1 - (opacidad / 100.0d));
        double K = a / (diametro/1000); 
        return(-1 * K); 
    }
    
    public static String formatearOpacidad(String decimalsPlaces, double valor){
        String dp = "%." + decimalsPlaces + "f";
        return String.format(dp, valor);
    }
    
    public static void main(String[] args){
        try{
        double K = FormulaOpacidad.opacidad(new Double(73.74485473598901),0.430);
        System.out.println("resultado: " + K);
            System.out.println("formateado: " + FormulaOpacidad.formatearOpacidad("8", K));
        }catch(ArithmeticException ar){
            System.out.println("" + ar.getMessage());
        }
    }
    
}

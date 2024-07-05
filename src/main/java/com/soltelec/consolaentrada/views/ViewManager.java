/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.views;

import javax.swing.*;
/**
 *
 * @author Gerencia TIC
 */
public class ViewManager {
    
    private final JFrame frame = new JFrame();
    private final PanelPrincipal pnlPrincipal = new PanelPrincipal();
    private final PanelRevisiones pnlRevisiones = new PanelRevisiones();
    private final PanelImpresionByPlaca pnlImpresionPlaca = new PanelImpresionByPlaca();
    private final PanelImpresionByFecha pnlImpresionFecha = new PanelImpresionByFecha();
    private final PanelReinspecciones pnlReinspecciones = new PanelReinspecciones();
    private final PanelMantFoto pnlMantFoto = new PanelMantFoto();
    private final PanelCertificados pnlCertificados = new PanelCertificados();
    private static ViewManager instance;
    public static Boolean aplicYellow=false;

    private ViewManager() {
    }
    
    public static ViewManager getInstance() {
        if (instance == null) {
            synchronized(ViewManager.class) {
                instance = new ViewManager();
                return instance;
            }
        } else {
            return instance;
        }
    } 
    
    public void iniciar() {
        frame.getContentPane().add(new PanelPrincipal());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setTitle("Consola Entrada 2.4.- Menu Principal");
    }
    
    public void showPrincipal() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnlPrincipal);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setTitle("Consola Entrada 2.4.- Menu Principal");
    }
    
    public void showRevisiones() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnlRevisiones);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setTitle("2.4.- Revisiones");
    }
    
    public void showImpresionesByPlaca() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnlImpresionPlaca);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setTitle("2.4.- Impresiones por placa");
    }
    
    public void showImpresionesByFecha() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnlImpresionFecha);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setTitle("2.4.- Impresiones por fecha");
    }
    
    public void showReinspeccion() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnlReinspecciones);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setTitle("2.4.- Reinspecciones");
    }
    
    public void showFoto() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnlMantFoto);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setTitle("2.4.- Mantenimiento Foto");
    }
    
    public void showCertificados() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnlCertificados);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setTitle("2.4.- Panel Certificados");
    }
    
    public void main(String[] args) {
        ViewManager viewManager = ViewManager.getInstance();
        viewManager.iniciar();
    }
}

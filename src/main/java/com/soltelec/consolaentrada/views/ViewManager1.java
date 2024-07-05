/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.views;

import javax.swing.*;

/**
 *
 * @author Gerencia Desarrollo Tecnologicos
 */
public class ViewManager1 {
    
    private final JFrame frame = new JFrame();
    private final PanelPrincipal1 pnlPrincipal = new PanelPrincipal1();
    private final PanelRevisiones pnlRevisiones = new PanelRevisiones();
    private final PanelImpresionByPlaca pnlImpresionPlaca = new PanelImpresionByPlaca();
    private final PanelImpresionByFecha pnlImpresionFecha = new PanelImpresionByFecha();
    private final PanelReinspecciones pnlReinspecciones = new PanelReinspecciones();
    private static ViewManager1 instance;

    private ViewManager1() {
    }
    
    public static ViewManager1 getInstance() {
        if (instance == null) {
            synchronized(ViewManager1.class) {
                instance = new ViewManager1();
                return instance;
            }
        } else {
            return instance;
        }
    } 
    
    public void iniciar() {
        frame.getContentPane().add(new PanelPrincipal1());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setTitle("Menu Principal");
    }
    
    public void showPrincipal() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnlPrincipal);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setTitle("Menu Principal");
    }
    
    public void showRevisiones() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnlRevisiones);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setTitle("Revisiones");
    }
    
    public void showImpresionesByPlaca() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnlImpresionPlaca);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setTitle("Impresiones por placa");
    }
    
    public void showImpresionesByFecha() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnlImpresionFecha);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setTitle("Impresiones por fecha");
    }
    
    public void showReinspeccion() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnlReinspecciones);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.setTitle("Reinspecciones");
    }
    
    public void main(String[] args) {
        ViewManager1 viewManager = ViewManager1.getInstance();
        viewManager.iniciar();
    }
}

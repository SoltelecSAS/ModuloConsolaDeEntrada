/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.views;

import javax.swing.*;


/**
 *
 * @author Usuario
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                PanelLogin frm_password = new PanelLogin();
                frm_password.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frm_password.setVisible(true);
            }
        });
    }
}

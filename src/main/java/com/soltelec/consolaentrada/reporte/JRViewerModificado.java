/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.reporte;

import java.awt.Component;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author GerenciaDesarrollo
 */
public class JRViewerModificado extends JRViewer {   
    
    public JRViewerModificado(JasperPrint jrPrint) {
         super(jrPrint);
        
    }  
    
    public void setListenerImprimir(ActionListener listener){
        JButton btnImprimir = getPrintButton();
        if (btnImprimir != null) {
            btnImprimir.addActionListener(listener);
        }        
        
    }
    
    private JButton getPrintButton() {
        for (Component comp : this.tlbToolBar.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if ("Print".equals(btn.getToolTipText()) || "Imprimir".equals(btn.getToolTipText())) {
                    return btn;
                }
            }
        }
        return null;
    }
        
}

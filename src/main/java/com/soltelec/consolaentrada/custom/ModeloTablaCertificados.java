/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.custom;

import com.soltelec.consolaentrada.models.entities.Certificado;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dany
 */
public class ModeloTablaCertificados extends AbstractTableModel {

    private List<Certificado> certificados;
    private final String[] nombreColumnas = {"Hoja Pruebas", "Consecutivo", "Fecha Expedicion", "Fecha Expiracion", "Anulado", "Fecha Anulación", "Comentario"};

    public ModeloTablaCertificados() {
        this.certificados = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return certificados.size();
    }

    @Override
    public int getColumnCount() {
        return nombreColumnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Certificado certificado = certificados.get(rowIndex);

        switch (columnIndex) {
            case 0:
//                if (hojaPrueba.getPreventiva().equals("Y")) {
//                    return hojaPrueba.getCon_preventiva();
//                } else {
//                    return hojaPrueba.getCon_hoja_prueba();
//                }
                HojaPruebas hojaPruebas = new HojaPruebas();
                hojaPruebas = certificado.getHojaPruebas();
                return hojaPruebas.getId();
            case 1:
                return certificado.getConsecutivo();
            case 2:
                return sdf.format(certificado.getFechaExpedicion());
            case 3:
                return sdf.format(certificado.getFechaImpresion());
            case 4:
                if (certificado.getAnulado().equals("N")) {
                    return "Activo";
                } else {
                    return "Anulado";
                }
            case 5:
                if(certificado.getFechaAnulacion()==null){
                   return ""; 
                }else
                return sdf.format(certificado.getFechaAnulacion());
                
            case 6:
                return certificado.getComentario();

            default:
                return false;
        }//end of switch
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            default:
                return String.class;
        }
    }

    @Override
    public String getColumnName(int column) {
        return nombreColumnas[column];
    }

    public List<Certificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<Certificado> certificados) {
        if (certificados == null) {
            certificados = new ArrayList<>();
        }

        this.certificados = certificados;
        fireTableDataChanged();
    }

}

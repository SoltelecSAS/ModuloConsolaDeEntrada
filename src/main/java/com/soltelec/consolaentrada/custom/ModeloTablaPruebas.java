/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.custom;

import com.soltelec.consolaentrada.models.entities.EstadoPrueba;
import com.soltelec.consolaentrada.models.entities.Prueba;
import com.soltelec.consolaentrada.views.ViewManager;
import java.awt.Color;
import java.awt.Component;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Modelo de tabla para mostrar los objetos de tabla
 *
 * @author GerenciaDesarrollo
 */
public class ModeloTablaPruebas extends AbstractTableModel {

    private List<Prueba> listPruebas;
    private final ComparadorPruebas comparador = new ComparadorPruebas();
    private final String[] nombreColumnas = {"Nombre Prueba", "Estado", "Nro. serial del equipo ", "Observaciones"};
    public static Integer cntTest = 0;
    public static Integer cntColum = 0;
    public static int posRow = 0;
    public static JTable jTable = null;

    public ModeloTablaPruebas() {
        listPruebas = new ArrayList<>();
    }

    public ModeloTablaPruebas(List<Prueba> listPruebas) {
        this.listPruebas = listPruebas;
        Collections.sort(this.listPruebas, comparador);
        fireTableDataChanged();

    }

    @Override
    public int getRowCount() {
        return listPruebas.size();
    }

    @Override
    public int getColumnCount() {
        return nombreColumnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Prueba prueba = listPruebas.get(rowIndex);
        Object value = null;

        switch (columnIndex) {
            case 0:
                return prueba.getTipoPrueba().getNombre();
            case 1:
                EstadoPrueba estado = EstadoPrueba.INDEFINIDA;
                if (prueba.getAbortado().equals("Y") && prueba.getTipoPrueba().getId() == 8) {
                    estado = EstadoPrueba.ABORTADA;
                }
                if (prueba.getAbortado().equals("Y") && prueba.getTipoPrueba().getId() != 8) {
                    estado = EstadoPrueba.CANCELADA;
                }
                if (prueba.getFinalizada().equals("Y") || prueba.getFinalizada().equalsIgnoreCase("A")) {
                    if (prueba.getAprobado().equals("Y")) {
                        estado = EstadoPrueba.APROBADA;
                    } 
                    if (prueba.getAprobado().equals("N") && !prueba.getAbortado().equals("Y")) {
                        estado = EstadoPrueba.RECHAZADA;
                    }
                    if (prueba.getAbortado().equals("A")) {
                        estado = EstadoPrueba.ANULADA;
                    }
                    return estado;
                } else {
                    estado = EstadoPrueba.PENDIENTE;
                    
                }
                return estado;
            case 2:
                return prueba.getSerialEquipo();
            case 3:
                if (prueba.getAutorizada().equalsIgnoreCase("A") && prueba.getFinalizada().equalsIgnoreCase("N") && ViewManager.aplicYellow == true) {
                    ImageIcon imageOn = new ImageIcon(getClass().getResource("/com/soltelec/consolaentrada/images/bandera.png"));
                    return imageOn;
                }
                if (prueba.getObservaciones() != null) {
                    if (prueba.getTipoPrueba().getId() == 1 && prueba.getObservaciones().indexOf("obs") > 0) {
                        int pos = prueba.getObservaciones().indexOf("obs");
                        return prueba.getObservaciones().substring(pos + 3, prueba.getObservaciones().length());
                    } else {
                        return prueba.getObservaciones();
                    }
                }
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return nombreColumnas[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setListPruebas(List<Prueba> listPruebas) {
        this.listPruebas = listPruebas;
        Collections.sort(this.listPruebas, comparador);
        fireTableDataChanged();
    }

    public List<Prueba> getListPruebas() {
        return listPruebas;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Prueba prueba = null;

        if (columnIndex == -1) {
            return ImageIcon.class;
        }
        if (cntTest >= listPruebas.size() && listPruebas.size() != 0) {
            cntTest = 0;
            cntColum = 0;
        }
        if (listPruebas.size() > 0 && cntTest < listPruebas.size()) {
            prueba = listPruebas.get(cntTest);
            cntColum++;
            if (cntColum == 4) {
                cntTest++;
                cntColum = 0;
            }
        }
        switch (columnIndex) {
            case 3:
                if (listPruebas.size() > 0 && cntTest <= listPruebas.size()) {
                    if (prueba.getAutorizada().equalsIgnoreCase("A") && prueba.getFinalizada().equalsIgnoreCase("N") && ViewManager.aplicYellow == true) {
                        return ImageIcon.class;
                    } else {
                        return String.class;
                    }
                }
            case 1:
                return EstadoPrueba.class;
            default:
                return String.class;
        }

    }
}//end of class ModeloTablaPruebas

class ComparadorPruebas implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof Prueba && o2 instanceof Prueba) {
            Prueba p = (Prueba) o2;
            return ((Prueba) o1).getTipoPrueba().getId().compareTo(p.getTipoPrueba().getId());
        } else {
            throw new IllegalArgumentException();
        }
    }
}

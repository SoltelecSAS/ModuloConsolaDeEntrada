/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.custom;
import com.soltelec.consolaentrada.models.entities.EstadoPrueba;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.controllers.PruebaJpaController;
import com.soltelec.consolaentrada.models.controllers.HojaPruebasJpaController;
import com.soltelec.consolaentrada.models.entities.PruebaDTO;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class ModeloTablaHojasVer extends AbstractTableModel {

    private List<HojaPruebas> listaHojaPruebas;
    private final String[] nombresColumnas = {"Numero", "Fecha", "Estado", "Preventiva"};

    public ModeloTablaHojasVer() {
        listaHojaPruebas = new ArrayList<>();
    }

    public ModeloTablaHojasVer(List<HojaPruebas> listaHojaPruebas) {
        this.listaHojaPruebas = listaHojaPruebas;
    }

    @Override
    public int getRowCount() {
        return listaHojaPruebas.size();
    }

    @Override
    public int getColumnCount() {
        return nombresColumnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        HojaPruebas hojaPrueba = listaHojaPruebas.get(rowIndex);
        HojaPruebasJpaController hoja = new HojaPruebasJpaController();

        switch (columnIndex) {
            case 0:
//                if (hojaPrueba.getPreventiva().equals("Y")) {
//                    return hojaPrueba.getCon_preventiva();
//                } else {
//                    return hojaPrueba.getCon_hoja_prueba();
//                }
                return hojaPrueba.getId();
            case 1:
                return sdf.format(hojaPrueba.getFechaIngreso());
            case 2:

                EstadoPrueba estado;
                

                if (hojaPrueba.getFinalizada().equals("Y")) {
                    if (hojaPrueba.getAprobado().equals("Y")) {
                        estado = EstadoPrueba.APROBADA;
                    } else {
                        estado = EstadoPrueba.RECHAZADA;
                    }

                    if (hojaPrueba.getAnulado().equals("Y")) {
                        estado = EstadoPrueba.ANULADA;
                    }
                } else {
                    estado = EstadoPrueba.PENDIENTE;
                }
                return estado;
            case 3:
                return (hojaPrueba.getPreventiva().equals("Y"));
            default:
                return false;
        }//end of switch

    }

    @Override
    public String getColumnName(int column) {
        return nombresColumnas[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 2:
                return EstadoPrueba.class;
            case 3:
                return Boolean.class;
            default:
                return String.class;
        }
    }

    public void setListaHojaPruebas(List<HojaPruebas> listaHojaPruebas) {
        this.listaHojaPruebas = listaHojaPruebas;
        fireTableDataChanged();
    }

    public List<HojaPruebas> getListaHojaPruebas() {
        if (listaHojaPruebas == null) {
            listaHojaPruebas = new ArrayList<>();
        }
        return listaHojaPruebas;
    }

    public static Object[] removeDuplicates(Object[] A) {
        if (A.length < 2) {
            return A;
        }

        int j = 0;
        int i = 1;

        while (i < A.length) {
            if (A[i].equals(A[j])) {
                i++;
            } else {
                j++;
                A[j] = A[i];
                i++;
            }
        }

        Object[] B = Arrays.copyOf(A, j + 1);

        return B;
    }

    public static EstadoPrueba organizarMayores(Integer idHojaPrueba) {
        PruebaJpaController prueba = new PruebaJpaController();
        HojaPruebasJpaController controller = new HojaPruebasJpaController();
        HojaPruebas hp= controller.find(idHojaPrueba);
//        List<Prueba> pruebas = hp.getListPruebas();
        
        List<PruebaDTO>  pruebas = prueba.findPruebas(idHojaPrueba);
        Collections.sort(pruebas, new PruebaDTO());
        Object[] pruebasArray = pruebas.toArray();

        Object[] pruebasArray2 = pruebas.toArray();

        Object[] arr=removeDuplicates(pruebasArray);
        
        for (int i = 0; i < arr.length; i++) {
            PruebaDTO object = (PruebaDTO) arr[i];
            for (int j = 0; j < pruebasArray2.length; j++) {
                PruebaDTO object1 = (PruebaDTO) pruebasArray2[j];
                if (object1.getTipoPrueba() == object.getTipoPrueba() && object1.getId() > object.getId()) {
                    arr[i] = object1;
                }
            }
        }
        int contPruebasFinalizadas = 0;
        int contPruebasAprobadas = 0;
        int contTotalPruebas = arr.length;

        for (Object object : arr) {
//            System.out.println(object);
            if (((PruebaDTO) object).getFinalizada().equals("Y")) {
                contPruebasFinalizadas++;
            }
            if (((PruebaDTO) object).getAprobado().equals("Y")) {
                contPruebasAprobadas++;
            }
        }
        if (contTotalPruebas == contPruebasFinalizadas) {
            if (contTotalPruebas == contPruebasAprobadas) {
                return EstadoPrueba.APROBADA;
            } else {
                return EstadoPrueba.RECHAZADA;
            }
        } else {
            return EstadoPrueba.PENDIENTE;
        }
    }
}


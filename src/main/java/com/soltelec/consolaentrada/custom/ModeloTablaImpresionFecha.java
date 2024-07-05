/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.custom;

import com.soltelec.consolaentrada.models.controllers.HojaPruebasJpaController;
import com.soltelec.consolaentrada.models.controllers.PruebaJpaController;
import com.soltelec.consolaentrada.models.entities.Certificado;
import com.soltelec.consolaentrada.models.entities.EstadoPrueba;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.Prueba;
import com.soltelec.consolaentrada.models.entities.PruebaDTO;
import com.soltelec.consolaentrada.views.ViewManager;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author GerenciaDesarrollo
 */
public class ModeloTablaImpresionFecha extends AbstractTableModel {

    private List<HojaPruebas> listaHojaPruebas;
    private final String[] nombresColumnas = {"Placa", "Tipo", "Hoja Prueba", "Estado", "Preventiva", "Fecha Ingreso", "Num Certificado", "Edo. Sicov"};

    public ModeloTablaImpresionFecha() {
        listaHojaPruebas = new ArrayList<>();
    }

    public ModeloTablaImpresionFecha(List<HojaPruebas> listaHojaPruebas) {
        this.listaHojaPruebas = listaHojaPruebas;
    }

    @Override
    public int getRowCount() {
        if (listaHojaPruebas != null) {
            return listaHojaPruebas.size();
        } else {
            return 0;
        }

    }

    @Override
    public int getColumnCount() {
        return nombresColumnas.length;
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
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (listaHojaPruebas == null) {
            return null;
        }
        if (listaHojaPruebas.isEmpty()) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        HojaPruebas hojaPrueba = listaHojaPruebas.get(rowIndex);
        HojaPruebasJpaController controller = new HojaPruebasJpaController();
        switch (columnIndex) {
            case 0:
                    return hojaPrueba.getVehiculo().getPlaca();
            case 1:
                return hojaPrueba.getVehiculo().getTipoVehiculo().getNombre();
            case 2:
                if (hojaPrueba.getPreventiva().equals("Y")) {
                    return hojaPrueba.getCon_preventiva();
                } else {
                    return hojaPrueba.getCon_hoja_prueba();
//                    return hojaPrueba.getId();
                }
//                return hojaPrueba.getId();

            case 3:
//                 EstadoPrueba estado;
//                if (hojaPrueba.getFinalizada().equals("Y")) {
//                    if (hojaPrueba.getAprobado().equals("Y")) {
//                        estado = EstadoPrueba.APROBADA;
//                    } else {
//                        estado = EstadoPrueba.REPROBADA;
//                    }
//                    
//                    if (hojaPrueba.getAnulado().equals("Y")) {
//                        estado = EstadoPrueba.ANULADA;
//                    }
//                } else {
//                    estado = EstadoPrueba.PENDIENTE; 
//                }
//                return estado;                
                //modificacion de estados hoja de pruebas error muchas conexiones 
                EstadoPrueba estado;
                String estado0 = (organizarMayores(hojaPrueba)).toString();
                hojaPrueba.setEstado(estado0);
//                try {
//                } catch (Exception e) {
//                }
                return hojaPrueba.getEstado();
            case 4:
                return hojaPrueba.getPreventiva().equals("Y");
            case 5:
                return sdf.format(hojaPrueba.getFechaIngreso());
            case 6:
                List<Certificado> certificados = hojaPrueba.getCertificados();
                if (certificados == null || certificados.isEmpty()) {
                    return "";
                }
                Certificado certificado = certificados.get(certificados.size() - 1);
                return certificado.getConsecutivo() + " " + sdf.format(certificado.getFechaImpresion()) + " " + sdf.format(certificado.getFechaExpedicion());
            default:
                return hojaPrueba.getEstadoSICOV();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return EstadoPrueba.class;
            case 4:
                return Boolean.class;
            case 5:
                return String.class;
            case 6:
                return String.class;
            case 7:
                return String.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }

    public List<HojaPruebas> getListaHojaPruebas() {
        return listaHojaPruebas;
    }

    public void setListaHojaPruebas(List<HojaPruebas> listaHojaPruebas) {
        if (listaHojaPruebas == null) {
            this.listaHojaPruebas = new ArrayList<>();
        }
        this.listaHojaPruebas = listaHojaPruebas;
        fireTableDataChanged();
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

    public static EstadoPrueba organizarMayores(HojaPruebas hp) {
        PruebaDTO pruebaDTO;
        List<PruebaDTO> pruebas = new ArrayList<>();
        for (Prueba prueba : hp.getListPruebas()) {
            pruebaDTO = new PruebaDTO();
            pruebaDTO.setAprobado(prueba.getAprobado());
            pruebaDTO.setFinalizada(prueba.getFinalizada());
            pruebaDTO.setId(prueba.getId());
            pruebaDTO.setTipoPrueba(prueba.getTipoPrueba().getId());
            pruebas.add(pruebaDTO);
        }
        Collections.sort(pruebas, new PruebaDTO());
        Object[] pruebasArray = pruebas.toArray();
        Object[] pruebasArray2 = pruebas.toArray();
        Object[] arr = removeDuplicates(pruebasArray);
        for (int i = 0; i < arr.length; i++) {
            PruebaDTO object = (PruebaDTO) arr[i];
            for (int j = 0; j < pruebasArray2.length; j++) {
                PruebaDTO object1 = (PruebaDTO) pruebasArray2[j];
                if (Objects.equals(object1.getTipoPrueba(), object.getTipoPrueba()) && object1.getId() > object.getId()) {
                    arr[i] = object1;
                }
            }
        }
        int contPruebasFinalizadas = 0;
        int contPruebasAprobadas = 0;
        int contTotalPruebas = arr.length;
        for (Object object : arr) {
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

    public static String organizarMayores(Integer idHojaPrueba, boolean reinspeccion) {
//        PruebaJpaController prueba = new PruebaJpaController();
        HojaPruebasJpaController controller = new HojaPruebasJpaController();
        HojaPruebas hp = controller.find(idHojaPrueba);
        List<Prueba> pruebas;
        if (hp.getIntentos() > 1 && reinspeccion) {
            pruebas = hp.getReinspeccionList().get(0).getPruebaList();
        } else {
            PruebaJpaController jpaController = new PruebaJpaController();
            pruebas = jpaController.findPrimeraPruebasByHoja(idHojaPrueba);
        }
        List<PruebaDTO> pruebas2 = new ArrayList<>();
        PruebaDTO pruebaDTO;
        for (Prueba prueba : pruebas) {
            pruebaDTO = new PruebaDTO();
            pruebaDTO.setAbortado(prueba.getAbortado());
            pruebaDTO.setAprobado(prueba.getAprobado());
            pruebaDTO.setFinalizada(prueba.getFinalizada());
            pruebaDTO.setId(prueba.getId());
            pruebaDTO.setTipoPrueba(prueba.getTipoPrueba().getId());
            pruebaDTO.setComentarioAbortado(prueba.getComentario());
            pruebas2.add(pruebaDTO);
        }

        Collections.sort(pruebas2, new PruebaDTO());
        Object[] pruebasArray = pruebas2.toArray();

        Object[] pruebasArray2 = pruebas2.toArray();

        Object[] arr = removeDuplicates(pruebasArray);

        for (int i = 0; i < arr.length; i++) {
            PruebaDTO object = (PruebaDTO) arr[i];
            for (Object pruebasArray21 : pruebasArray2) {
                PruebaDTO object1 = (PruebaDTO) pruebasArray21;
                if (Objects.equals(object1.getTipoPrueba(), object.getTipoPrueba()) && object1.getId() > object.getId()) {
                    arr[i] = object1;
                }
            }
        }
        int contPruebasFinalizadas = 0;
        int contPruebasAprobadas = 0;
        int contTotalPruebas = arr.length;

        String comentarios = "";
        boolean flag = false;
        for (Object object : arr) {
            if (((PruebaDTO) object).getFinalizada().equals("Y")) {
                if (((PruebaDTO) object).getComentarioAbortado() != null) {
                    if (flag) {
                        comentarios += ((PruebaDTO) object).getComentarioAbortado();
                        flag = true;
                    } else {
                        comentarios += ", " + ((PruebaDTO) object).getComentarioAbortado();
                    }
                }
            }
        }
        return comentarios;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.utilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @version 2.0
 * @author Soltelec Ltda
 */
public class Validaciones {

    /**
     * Metodo para hacer que los campos solo acepten numeros
     *
     * @param e evento del campo que solo aceptara numeros
     */
    public static void soloNumeros(java.awt.event.KeyEvent e) {
        char caracter = e.getKeyChar();
        if (((caracter < '0') || (caracter > '9')) && (caracter != java.awt.event.KeyEvent.VK_BACK_SPACE) && (caracter != '.')) {
            e.consume();
        }
    }

    /*
     * Metodo para comprobar que ningun campo este vacio y sin seleccionar
     * 
     * @param contenedor Es el componente contendor del cual se tomaran los campos         * 
     */
    public static boolean validarCampos(JComponent contenedor) {
        Component[] campos = contenedor.getComponents();
        quitarColor(campos);
        boolean estado = true;
        String mensaje = "";
        //bucle para iterar la variable campos y revisar cada componente
        for (Component campo : campos) {
            //validar si es un JTextField
            if (campo instanceof JTextField) {
                //validar si los campos estan vacios
                if (((JTextField) campo).getText().isEmpty() && ((JTextField) campo).isEnabled() && ((JTextField) campo).isVisible() && campo.getName() != null) {
                    
                    if (!campo.getName().equalsIgnoreCase("Num de Serie") || !campo.getName().equalsIgnoreCase("Codigo Interno") ) {
                        estado = false;
                        ((JTextField) campo).setBackground(Color.YELLOW);
                        mensaje += campo.getName() + ": Esta Vacio\n";
                    }
                }
                //validar si es un ComboBox    
            } else if (campo instanceof JComboBox) {
                //validar si el elemento seleccionado es el indice 0
                if (((JComboBox) campo).getSelectedIndex() == 0 && ((JComboBox) campo).isEnabled() && ((JComboBox) campo).isVisible() && campo.getName() != null) {
                    estado = false;
                    ((JComboBox) campo).setBackground(Color.YELLOW);
                    mensaje += campo.getName() + ": Sin Seleccionar\n";
                }
                //validar si es un JTextArea
            } else if (campo instanceof JTextArea) {
                //validar si los campos estan vacios
                if (((JTextArea) campo).getText().isEmpty() && ((JTextArea) campo).isEnabled() && ((JTextArea) campo).isVisible() && campo.getName() != null) {
                    estado = false;
                    ((JTextArea) campo).setBackground(Color.YELLOW);
                    mensaje += campo.getName() + ": Esta Vacio\n";
                }
            }
        }// fin del bucle for

        if (!estado) {
            Mensajes.mensajeAdvertencia(mensaje);
        }

        return estado;
    }

    /**
     * Metodo para agregar eventos a los campos dependiendo del campo se
     * agregara el evento para poder quitar el color de error segun el campo
     *
     * @param campos los campos a los cuales se les agregara el evento
     * correspondiente
     */
    private static void quitarColor(Component[] campos) {

        for (final Component campo : campos) {
            if (campo instanceof JTextField || campo instanceof JTextArea) {
                ((JTextField) campo).getDocument().addDocumentListener(new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        campo.setBackground(Color.white);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        campo.setBackground(Color.white);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        campo.setBackground(Color.white);
                    }
                });
            }
        }
    }

    /*
     * Metodo para limitar la cantidad de caracteres ingresados en algun determinado campo
     * 
     * @param e Es el que contiene el evento ejercido sobre el componente
     * @param campo Es el campo al cual vamos a limitar
     * @param cantidadPermitida Es un entero con la cantidad de caracteres permitidos
     */
    public static void limitarCaracteres(java.awt.event.KeyEvent e, JTextField campo, int cantidadPermitida) {
        if (campo.getText().length() == cantidadPermitida) {
            e.consume();
        }
    }

    /*
     * Metodo para ordenar los elementos de una Lista
     * 
     * @param lista la lista que se desea ordenar
     * @param propiedad propiedad por la cual se desea ordenar
     */
    public static void ordenarListas(List lista, final String propiedad) {
        Collections.sort(lista, new Comparator<Object>() {

            @Override
            public int compare(Object obj1, Object obj2) {
                Class clase = obj1.getClass();
                String getter = "get" + Character.toUpperCase(propiedad.charAt(0)) + propiedad.substring(1);
                try {
                    Method getPropiedad = clase.getMethod(getter);

                    Object propiedad1 = getPropiedad.invoke(obj1);
                    Object propiedad2 = getPropiedad.invoke(obj2);

                    if (propiedad1 instanceof Comparable && propiedad2 instanceof Comparable) {
                        Comparable prop1 = (Comparable) propiedad1;
                        Comparable prop2 = (Comparable) propiedad2;

                        return prop1.compareTo(prop2);
                    } else if (propiedad1.equals(propiedad2)) {
                        return 0;
                    } else {
                        return 1;
                    }

                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                }
                return 0;
            }
        });
    }

    /*
     * Metodo para que solo muestre las letras en mayusculas
     * 
     * @param e evento accionado por el campo que contiene el texto
     */
    public static void pasarAMayusculas(JComponent contenedor) {
        for (Component campo : contenedor.getComponents()) {
            if (campo instanceof JTextField || campo instanceof JTextArea) {

                campo.addKeyListener(new java.awt.event.KeyAdapter() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                        e.setKeyChar(Character.toUpperCase(e.getKeyChar()));
                    }

                });
            }
        }
    }

    /*
     * Metodo para limpiar componentes comunes
     * 
     * @param contenedor el componente donde se encuentran lo campos a limpiar
     */
    public static void limpiarCampos(JComponent contenedor) {
        Component[] campos = contenedor.getComponents();
        //bucle para iterar la variable campos y revisar cada componente
        for (Component campo : campos) {
            //validar si es un JTextField
            if (campo instanceof JTextField) {
                ((JTextField) campo).setText("");
                //validar si es un ComboBox    
            } else if (campo instanceof JComboBox) {
                ((JComboBox) campo).setSelectedIndex(0);
                //validar si es un JTextArea
            } else if (campo instanceof JTextArea) {
                ((JTextArea) campo).setText("");
            }
        }// fin del bucle for
    }

    /*
     * Metodo para validar que los campos del componente que se pasa como parametro
     * solo acepten numeros esto se hara dinamicamente para no generar mas codigo
     *
     * @param contenedor es el componente que contiene los elementos a validar Ej: JPanel
     */
    public static void validarNumeros(JComponent contenedor) {
        Component[] campos = contenedor.getComponents();

        for (Component campo : campos) {
            if (campo instanceof JTextField) {
                validarCamposNumericos(campo);
            }
        }
    }

    /**
     * metodo para agregar el evento que hace que el componente solo admita
     * numeros
     *
     * @param textField
     */
    public static void validarCamposNumericos(Component textField) {
        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                soloNumeros(evt);
            }
        });
    }

    /**
     * Metodo encargado de redondear las cifras decimales a multiplos de diez
     *
     * @param val
     * @return
     */
    public static Integer redondear(Double val) {
        Integer x = (int) Math.round((val));
        System.out.println(x);
        String valor = String.valueOf(x);
        Integer valor2 = Integer.parseInt(valor.substring(valor.length() - 1));
        if (valor2 != 0) {
            if (valor2 >= 5) {
                return Integer.parseInt(valor.substring(0, valor.length() - 1) + 0) + 10;
            } else {
                return Integer.parseInt(valor.substring(0, valor.length() - 1) + 0);
            }
        } else {
            return x;
        }
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.utilities;

//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Dany
 */
public class GenericExportExcel {

    public GenericExportExcel() {
    }    
    
    /**
     * Metodo apra generar el archivo donde se va a guardar con su correspondiente
     * direccion en el computador
     * @return 
     */
    private FileOutputStream generateDir() {
        JFileChooser jfChoose = new JFileChooser();

        if (jfChoose.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        String directorio = jfChoose.getSelectedFile().getPath();

        String tmp = directorio.substring(directorio.length() - 3);

        if (!tmp.equals("xls")) {
            directorio += ".xls";
        }
        
        FileOutputStream out;
        try {
            out = new FileOutputStream(directorio);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error de Lectura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        return out;
    }
    
    public void exportExcel(JTable table) {   
        FileOutputStream out = generateDir();
        
        if (out == null) {
            return;
        }
        
        HSSFWorkbook wb = new HSSFWorkbook();
        
        addSheet(wb, table);
        try {
            wb.write(out);
            out.close();
            JOptionPane.showMessageDialog(null, "Se Guardo Correctamente");
        } catch (IOException ex) {
            Mensajes.mostrarExcepcion(ex);
        }        
    }
    
    public void exportExcel(List<JTable> tables) {
        FileOutputStream out = generateDir();
        
        if (out == null) {
            return;
        }
        
        HSSFWorkbook wb = new HSSFWorkbook();
        
        for (JTable table: tables) {
            addSheet(wb, table);
        }
        
        try {
            wb.write(out);
            out.close();
            JOptionPane.showMessageDialog(null, "Se Guardo Correctamente");
        } catch (IOException ex) {
            Mensajes.mostrarExcepcion(ex);
            JOptionPane.showMessageDialog(null, "Error al guardar");
        }
    }
    
    /**
     * Metodo para agregar una hoja de calculo dentro de un archivo especifico
     * dandole el formato correspondiente
     * @param out archivo de excel en el cual se agregara la informacion
     * @param table tabla de donde se sacara la informacion de la hoja de calculo
     */
    private void addSheet(HSSFWorkbook wb, JTable table) {
        try {
            TableModel model = table.getModel();
            
            if (model.getRowCount() < 1) {                
                return;
            }
            
            HSSFSheet sheet = wb.createSheet(table.getName());
            HSSFRow row;
            HSSFCell column;

            /**
             * Header Cell Style
             */        
            HSSFCellStyle headerCellStyle = wb.createCellStyle();
            HSSFFont headerFont = wb.createFont();
            headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            headerCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            headerCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            headerCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            headerCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            headerCellStyle.setWrapText(true);

            /**
             * String Cell Style
             */        
            HSSFCellStyle stringCellStyle = wb.createCellStyle();
            stringCellStyle.setWrapText(true);
            stringCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            stringCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            stringCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            stringCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

            /*
             * Add Header to worksheet
             */
            row = sheet.createRow(0);
            for (int colNumber = 0; colNumber < model.getColumnCount(); colNumber++) {            

                column = row.createCell(colNumber);
                column.setCellStyle(headerCellStyle);
                column.setCellValue(model.getColumnName(colNumber));            
            }

            /**
             * Add Data to worksheet
             */
            for (int rowNumber = 0; rowNumber < model.getRowCount(); rowNumber++) {
                row = sheet.createRow(rowNumber+1);

                for (int colNumber = 0; colNumber < model.getColumnCount(); colNumber++) {
                    column = row.createCell(colNumber);
                    column.setCellStyle(stringCellStyle);
                    column.setCellValue((model.getValueAt(rowNumber, colNumber) == null) ? "" : model.getValueAt(rowNumber, colNumber).toString());
                }
            }       

            /**
             * Auto Size All Columns
             */
            for (int colNumber = 0; colNumber < model.getColumnCount(); colNumber++) {
                sheet.autoSizeColumn(colNumber);
            }        
        } catch (HeadlessException e) {
            Mensajes.mostrarExcepcion(e);
        }  
    }
    
    public void exportSameSheet(List<JTable> tables) {
        FileOutputStream out = generateDir();
        int consecutivo = 0;
        int rowTemp = 1;
        
        if (out == null) {
            return;
        }
        
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Reporte");
        HSSFRow row;
        HSSFCell column;
        
        /**
        * Header Cell Style
        */        
        HSSFCellStyle headerCellStyle = wb.createCellStyle();
        HSSFFont headerFont = wb.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerCellStyle.setWrapText(true);

        /**
        * String Cell Style
        */        
        HSSFCellStyle stringCellStyle = wb.createCellStyle();
        stringCellStyle.setWrapText(true);
        stringCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        stringCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        stringCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        stringCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        
        for (JTable table: tables) {
            
            try {
                TableModel model = table.getModel();
                int temp = consecutivo;                 

                /*
                 * Add Header to worksheet
                 */
                row = sheet.getRow(0);
                if (row == null) {
                    row = sheet.createRow(0);
                }
                for (int colNumber = 0; colNumber < model.getColumnCount(); colNumber++) {            

                    column = row.createCell(temp);
                    column.setCellStyle(headerCellStyle);
                    column.setCellValue(model.getColumnName(colNumber));    
                    temp++;
                }
                
                /**
                 * Add Data to worksheet
                 */
                for (int rowNumber = 0; rowNumber < model.getRowCount(); rowNumber++) {
                  
                    if ((row = sheet.getRow(rowNumber+1)) == null) {
                        row = sheet.createRow(rowNumber+1);
                    }

                    temp = consecutivo;
                    
                    for (int colNumber = 0; colNumber < model.getColumnCount(); colNumber++) {
                        column = row.createCell(temp);
                        column.setCellStyle(stringCellStyle);
                        column.setCellValue((model.getValueAt(rowNumber, colNumber) == null) ? "" : model.getValueAt(rowNumber, colNumber).toString());
                        temp++;
                    }
                }       
                
                temp = consecutivo;
                
                /**
                 * Auto Size All Columns
                 */
                for (int colNumber = 0; colNumber < model.getColumnCount(); colNumber++) {
                    sheet.autoSizeColumn(temp);
                    temp++;
                }    
                
                consecutivo += model.getColumnCount();
            } catch (HeadlessException e) {
                Mensajes.mostrarExcepcion(e);
            }           
            
        }
        
        try {
            wb.write(out);
            out.close();
            JOptionPane.showMessageDialog(null, "Se Guardo Correctamente");
        } catch (IOException ex) {
            Mensajes.mostrarExcepcion(ex);
        } 
    }
}

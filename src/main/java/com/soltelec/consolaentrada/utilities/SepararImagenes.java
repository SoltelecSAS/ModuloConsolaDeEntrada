/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soltelec.consolaentrada.utilities;

import com.soltelec.consolaentrada.configuration.Conexion;
import com.soltelec.consolaentrada.models.entities.Fotos;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.Prueba;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Clase encargada de cargar las imagenes para cada revision, ya sea
 * reinspeccion o rtm
 *
 * @fecha 28/03/2016
 * @insidencia SART-21 Solicitudes realizads en las ultimas auditorias ONAC
 * @author GerenciaDesarrollo
 */
public class SepararImagenes {

    private static final Logger LOG = Logger.getLogger(SepararImagenes.class.getName());

    private BufferedImage foto1;
    private BufferedImage foto2;
    private String estadoAProbada="";
    private String  estadoFinalizada="";

    /**
     * Carga la foto en un java.awt.image.BufferedImage, si es una reinpeccion
     * la corta en 4 partes y se estrae la foto que se requiere.
     *
     * @param numeroFoto Indica el numero de la foto que se quiere consultar.
     * @param idHojaPrueba Indicador de la hoja de prueba.
     * @param reinspeccion Indica si la foto es de una reinspeccion.
     */
    public void obtenerImagen(int numeroFoto, HojaPruebas ctxHojaPrueba, boolean reinspeccion,boolean flag) {
        
        BufferedImage image1 = null;
        BufferedImage image2 = null;

        try {
            image2 = null;
            Fotos foto = ctxHojaPrueba.getFotosList().iterator().next();
            System.out.println("trama_foto: "+foto);
            InputStream inpStrFoto = new ByteArrayInputStream(foto.getFoto1());
            image1 = ImageIO.read(inpStrFoto);
            inpStrFoto = new ByteArrayInputStream(foto.getFoto2());
            image2 = ImageIO.read(inpStrFoto);

            if (Utils.obtenerNumeroRevisionFoto(ctxHojaPrueba.getId()) == 4) 
            {
                try 
                {
                    int idPruebaFoto=0;
                    int idHojaPrueba=ctxHojaPrueba.getId();
                    for (Prueba p : ctxHojaPrueba.getListPruebas()) 
                    {
                        if (p.getTipoPrueba().getId()==3) 
                        {
                            idPruebaFoto=p.getTipoPrueba().getId();
                        }
                    }
                    consultarEstadoPruebaVisual(idPruebaFoto,idHojaPrueba);              
                    
                    if (estadoAProbada.equalsIgnoreCase("Y") || estadoFinalizada.equalsIgnoreCase("Y"))
                    {
                        foto1 = split(image1)[numeroFoto];
                        foto2 = split(image2)[numeroFoto];
//                      Ventana a= new Ventana(foto1,foto2); 
                    }else if(flag){
                        foto1 = image1;
                        foto2 = image2;
                    }
                 
                } catch (Exception e) 
                {
                    System.out.println("Error al cargar fotos para reinspeccion");
                }

            } else {
                foto1 = image1;
                foto2 = image2;
            }
        } catch (IOException ex) {
            Logger.getLogger(SepararImagenes.class.getName()).log(Level.SEVERE, null, ex);
        }
         AffineTransform at = AffineTransform.getScaleInstance(2, 2);//matriz de transformacion

    }
    
    
    /**
     * 
     * @param idTipoPrueba
     * @param idHojaPrueba
     * @throws ClassNotFoundException 
     */
    private void consultarEstadoPruebaVisual(int idTipoPrueba,int idHojaPrueba) throws ClassNotFoundException
    {
        try 
        {
            ResultSet rs = null;
            Conexion connConf = Conexion.getInstance();
//            Class.forName(connConf.getDriver());
            Connection connect = DriverManager.getConnection(connConf.getUrl(), connConf.getUsuario(), connConf.getContrasena());
            PreparedStatement pstmt = connect.prepareStatement("SELECT  p.Aprobada, p.Finalizada  FROM  pruebas p  WHERE  p.Tipo_prueba_for="+idTipoPrueba+" AND p.hoja_pruebas_for="+idHojaPrueba+" ORDER BY p.Id_Pruebas DESC LIMIT 1");
            rs = pstmt.executeQuery();
            while (rs.next()) 
            {
                estadoAProbada=rs.getString("Aprobada");
                estadoFinalizada=rs.getString("Finalizada");
            }
            
        } catch (SQLException e) 
        {
            System.out.println("Error en el metodo: consultarEstadoPruebaVisual()" + e.getSQLState() + e.getMessage() + e.getLocalizedMessage());
        }
    }
    
    
    
    public static Image concatenarImagenes(BufferedImage bImg1, BufferedImage bImg2) {

        int anchoImg1 = 640;//trae los dos anchos de las imagenes
        int anchoImg2 = 640;

        int altoImg1 = 480; //trae los dos anchos de las imagenes
        int altoImg2 = 480;

        //Selecciona la mas alta de los dos
        int alto = (bImg1.getHeight() >= bImg2.getHeight() ? bImg1.getHeight() : bImg2.getHeight());

        BufferedImage nuevaImagen = new BufferedImage(anchoImg1 + anchoImg2, altoImg1 + altoImg2, BufferedImage.TYPE_INT_RGB);
        boolean image1Drawn = nuevaImagen.createGraphics().drawImage(bImg1, 0, 0, null);

        if (!image1Drawn) {
            System.out.println("Problemas dibujando la imagen uno");
        }

        boolean image2Drawn = nuevaImagen.createGraphics().drawImage(bImg2, anchoImg1, 0, null);
        if (!image2Drawn) {
            System.out.println("Problemas dibujando la imagen dos");
        }
        //Escala la imagen
        AffineTransform at = AffineTransform.getScaleInstance(0.5, 0.5);//matriz de transformacion
        BufferedImage nuevaImagenEscalada = new BufferedImage((int) ((anchoImg1 + anchoImg2) * 0.5), (int) ((altoImg1 + altoImg2) * 0.5), BufferedImage.TYPE_INT_RGB);
        nuevaImagenEscalada.createGraphics().drawRenderedImage(nuevaImagen, at);
        return nuevaImagenEscalada;
    }

    private BufferedImage[] split(BufferedImage image) {
        try {
            int rows = 2; //You should decide the values for rows and cols variables
            int cols = 2;
            int chunks = rows * cols;
            BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks

            int eve = image.getWidth();
            int rei = image.getHeight();
            int chunkWidth = image.getWidth() / cols; // determines the chunk width and height
            int chunkHeight = image.getHeight() / rows;
            int count = 0;
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < cols; y++) {
                    imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());
                    Graphics2D gr = imgs[count++].createGraphics();
                    gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                    gr.dispose();
                }
            }
            return imgs;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
//        SepararImagenes separarImagenes = new SepararImagenes();
////        separarImagenes.obtenerImagen(0, null, true);
//        ImageIO.write(separarImagenes.getFoto1(), "jpg", new File("Foto1.jpg"));
//        ImageIO.write(separarImagenes.getFoto2(), "jpg", new File("Foto2.jpg"));
    }

    /**
     * @return the foto1
     */
    public BufferedImage getFoto1() {
        return foto1;
    }

    /**
     * @param foto1 the foto1 to set
     */
    public void setFoto1(BufferedImage foto1) {
        this.foto1 = foto1;
    }

    /**
     * @return the foto2
     */
    public BufferedImage getFoto2() {
        return foto2;
    }

    /**
     * @param foto2 the foto2 to set
     */
    public void setFoto2(BufferedImage foto2) {
        this.foto2 = foto2;
    }
}

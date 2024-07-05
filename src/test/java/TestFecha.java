
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public class TestFecha {
    public static void main(String[] args) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fechaInicioResolucion = sdf.parse("2022-08-18 01:30:00");
        Date fechaInicioResolucion0762Tabla27 = sdf.parse("2023-02-07 00:00:00");
        sdf.format(fechaInicioResolucion);
        if(fechaInicioResolucion.after(fechaInicioResolucion0762Tabla27)){
            System.out.println("Mayor");
        }else{
            System.out.println("Menor:" + fechaInicioResolucion);
        }
      
    }
}

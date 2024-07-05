
import com.soltelec.consolaentrada.models.controllers.EventosDao;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.UsuarioLogueado;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SOLTELEC
 */
public class TestEventosSicov {

    /**
     * @param args the command line arguments
     * 
     * @throws java.lang.Exception
     */ 
    public static void main(String[] args) throws Exception {
     //  EventosDao ev = new EventosDao();
       //HojaPruebas hp = new HojaPruebas(58226,"Y",new Date(), new Date(), 4365801, new Date(), 1 );
       //System.out.println(ev.InsertarEvento( "jfm" , "abc123",hp));
       EventosDao ev = new EventosDao();
       HojaPruebas hp = new HojaPruebas(58226,"Y",new Date(), new Date(), 4365801, new Date(), 1 );
       System.out.println(ev.InsertarEvento( "jfm" , "abc123",hp));
       
       
        // TODO code application logic here
    }
    
}

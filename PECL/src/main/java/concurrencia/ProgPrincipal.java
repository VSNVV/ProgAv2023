/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package concurrencia;
import java.util.Formatter;

/**
 *
 * @author victorsanavia
 */
public class ProgPrincipal {

    public ProgPrincipal(){
        Log log = new Log(true);
        Colonia colonia = new Colonia(log);
        Formatter fmt = new Formatter();
        for (int i = 0; i < 20; i++){
            fmt.format("%04d", i);
            String nombre = "HO" + fmt;
            Hormiga h = new Hormiga(colonia, nombre, i, "Obrera");
            h.start();
        }
    }

    
}

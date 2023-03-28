package concurrencia;

import javax.swing.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ZonaDescanso {
    //Atributos de la clase ZonaDescanso
    private Log log;
    private Lock entradaSalida = new ReentrantLock();
    private ListaThreads listaHormigasDescansando;

    //Métodos de la clase ZonaDescanso

    //Método constructor
    public ZonaDescanso(Log log, JTextField jTextFieldHormigasDescansando){
        this.log = log;
        this.listaHormigasDescansando = new ListaThreads(jTextFieldHormigasDescansando);
    }

    //Método para entrar a la zona de descanso
    public void entraZonaDescanso(Hormiga hormiga){
        entradaSalida.lock();
        try{
            //Para entrar, metemos el identificador de la hormiga en el JTextField correspondiente
            getListaHormigasDescansando().meterHormiga(hormiga);
            //Una vez dentro, escribimos el evento en el log
            getLog().escribirEnLog("[Zona Descanso] --> La hormiga " + hormiga.getIdentificador() + " ha entrado a la zona de descanso");

        }finally{
            entradaSalida.unlock();
        }
    }

    //Método para salir de la zona de descanso
    public void saleZonaDescanso(Hormiga hormiga){
        entradaSalida.lock();
        try{
            //Para salir, tenemos que quitar el identificador de la hormiga del JTextField correspondiente
            getListaHormigasDescansando().sacarHormiga(hormiga);
            //Una vez fuera, escribimos el evento en el log
            getLog().escribirEnLog("[Zona Descanso] --> La hormiga " + hormiga.getIdentificador() + " ha salido de la zona de descanso");

        }finally{
            entradaSalida.unlock();
        }
    }

    //Métodos get y set

    public Log getLog() {
        return log;
    }

    public ListaThreads getListaHormigasDescansando() {
        return listaHormigasDescansando;
    }
}

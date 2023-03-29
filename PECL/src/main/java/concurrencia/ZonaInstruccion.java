package concurrencia;

import javax.swing.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ZonaInstruccion {
    //Atributos de la clase ZonaInstruccion
    private Log log;
    private Lock entradaSalida = new ReentrantLock();
    private ListaThreads listaHormigasHaciendoInstruccion;

    //Métodos de la clase ZonaInstruccion

    //Método constructor
    public ZonaInstruccion(Log log, JTextField jTextFieldHormigasHaciendoInstruccion){
        this.listaHormigasHaciendoInstruccion = new ListaThreads(jTextFieldHormigasHaciendoInstruccion);
    }

    //Método para entrar a la zona de instruccion
    public void entraZonaInstruccion(Hormiga hormiga){
        entradaSalida.lock();
        try{
            //Cuando entra una hormiga, mete su ID al JTextField correspondiente mediante el ListaThreads
            getListaHormigasHaciendoInstruccion().meterHormiga(hormiga);
            //Una vez metida, escribe el evento en el log
            getLog().escribirEnLog("[Zona Instruccion] --> La hormiga " + hormiga.getIdentificador() + " ha entrado a la zona de instruccion");

        }finally{
            entradaSalida.unlock();
        }
    }
    //Método para salir de la zona de instruccion
    public void saleZonaInstruccion(Hormiga hormiga){
        entradaSalida.lock();
        try{
            //Para salir, en primer lugar tenemos que quitar su ID del JTextField
            getListaHormigasHaciendoInstruccion().sacarHormiga(hormiga);
            //Una vez fuera, escribe el mensaje en el log
            getLog().escribirEnLog("[Zona Instruccion] --> La hormiga " + hormiga.getIdentificador() + " ha salido de la zona de instruccion");

        }finally{
            entradaSalida.unlock();
        }
    }

    //Método para hacer instruccion
    public void realizaInstruccion(Hormiga hormiga) throws InterruptedException{
        //Hacen instrucción entre 2 y 8 segundos
        getLog().escribirEnLog("[Zona Instruccion] --> La hormiga " + hormiga.getIdentificador() + " esta haciendo instruccion");
        Thread.sleep((int) ((Math.random() + 1) * 4000 + 4000));
        getLog().escribirEnLog("[Zona Instruccion] --> La hormiga " + hormiga.getIdentificador() + " ha terminado instruccion");
    }
    //Métodos get y set

    public Log getLog() {
        return log;
    }

    public Lock getEntradaSalida() {
        return entradaSalida;
    }

    public ListaThreads getListaHormigasHaciendoInstruccion() {
        return listaHormigasHaciendoInstruccion;
    }
}

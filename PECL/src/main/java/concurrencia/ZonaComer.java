package concurrencia;

import javax.swing.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ZonaComer {
    //Atrubutos de la clase ZonaComer
    private Log log;
    private Lock entradaSalida = new ReentrantLock(), elementoComida = new ReentrantLock();
    private int numElementosComida = 0;
    private ListaThreads unidadesElementosComida, listaHormigasLlevandoComida, listaHormigasZonaComer;


    //Métodos de la clase ZonaComer

    //Método constructor
    public ZonaComer(Log log, JTextField jTextFieldUnidadesComidaZonaComer, JTextField jTextFieldHormiasLlevandoComida,
                     JTextField jTextFieldHormigasZonaComer){
        this.log = log;
        this.unidadesElementosComida = new ListaThreads(jTextFieldUnidadesComidaZonaComer);
        this.listaHormigasLlevandoComida = new ListaThreads(jTextFieldHormiasLlevandoComida);
        this.listaHormigasZonaComer = new ListaThreads(jTextFieldHormigasZonaComer);
    }

    //Método para entrar a la zona para comer
    public void entraZonaComer(Hormiga hormiga){
        entradaSalida.lock();
        try{
            //Poner el listathreads para añadir el ID de la hormiga
            getLog().escribirEnLog("[Zona Comer] --> La hormiga " + hormiga.getIdentificador() + " ha entrado a la zona para comer");
        }finally{
            entradaSalida.unlock();
        }
    }

    //Método para salir de la zona de comer
    public void saleZonaComer(Hormiga hormiga){
        entradaSalida.lock();
        try{
            //Poner el listathreads para quitar el JTextField
            getLog().escribirEnLog("[Zona Comer] --> La hormiga " + hormiga.getIdentificador() + " ha salido de la zona para comer");
        }finally{
            entradaSalida.unlock();
        }
    }

    //Método para depositar un elemento de comida
    public void depositaElementoComida(Hormiga hormiga){
        elementoComida.lock();
        try{
            //En depositar un elemento de comida se tarda entre 1 y 2 segundos
            Thread.sleep((int) (Math.random() * 1000 + 1000));
            //Una vez transcurrido el timepo, incrementamos el numero de elementos de comida
            setNumElementosComida(getNumElementosComida() + 1);

        } catch (InterruptedException e) {}
        finally{
            elementoComida.unlock();
        }
    }

    //Métodos get y set
    public Log getLog(){
        return this.log;
    }
    public int getNumElementosComida(){
        return this.numElementosComida;
    }
    public void setNumElementosComida(int numElementosComida){
        this.numElementosComida = numElementosComida;
    }
}

package concurrencia;

import javax.swing.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ZonaComer {
    //Atrubutos de la clase ZonaComer
    private final Log log;
    private final Semaphore semaforoEntradaSalida = new Semaphore(1, true); //Semáforo para entrar y salir
    private final Lock cerrojoElementoComida = new ReentrantLock();
    private final Condition esperaElementoComida = cerrojoElementoComida.newCondition();
    private int numElementosComida = 0, numHormigasZonaComer = 0;
    private final ListaThreads unidadesElementosComida;
    private final ListaThreads listaHormigasZonaComer;

    //Métodos de la clase ZonaComer

    //Método constructor
    public ZonaComer(Log log, JTextField jTextFieldUnidadesComidaZonaComer, JTextField jTextFieldHormigasZonaComer){
        this.log = log;
        this.unidadesElementosComida = new ListaThreads(jTextFieldUnidadesComidaZonaComer);
        this.listaHormigasZonaComer = new ListaThreads(jTextFieldHormigasZonaComer);
    }

    //Método para entrar a la zona para comer
    public void entra(Hormiga hormiga){
        try{
            semaforoEntradaSalida.acquire();
            setNumHormigasZonaComer(getNumHormigasZonaComer() + 1); //Incrementamos el numero de hormigas en la ZonaComer
            getListaHormigasZonaComer().meterHormiga(hormiga);
            getLog().escribirEnLog("[ZONA COMER] --> La hormiga " + hormiga.getIdentificador() + " ha entrado a la zona para comer");
            semaforoEntradaSalida.release();
        }
        catch(InterruptedException ignored){}
    }

    //Método para salir de la zona para comer
    public void sale(Hormiga hormiga){
        try{
            semaforoEntradaSalida.acquire();
            getListaHormigasZonaComer().sacarHormiga(hormiga); //Nos quitamos de la lista
            getLog().escribirEnLog("[ZONA COMER] --> La hormiga " + hormiga.getIdentificador() + " ha salido de la zona para comer"); //Escribimos el evento en el log
            semaforoEntradaSalida.release();
        }
        catch(InterruptedException ignored){}
    }

    //Método para depositar un elemento de comida
    public void depositaElementoComida(Hormiga hormiga){
        try{
            cerrojoElementoComida.lock();
            setNumElementosComida(getNumElementosComida() + 5); //Depositamos los 5 elementos de comida
            getUnidadesElementosComida().insertarNumero(getNumElementosComida());
            getLog().escribirEnLog("[ZONA COMER] --> La hormiga " + hormiga.getIdentificador() + " ha depositado un elemento de comida en la zona para comer");
            for(int i = 0; i < 5; i++){
                esperaElementoComida.signal(); //Como hemos depositado 5 elementos de comida, despertaremos a 5 hormigas para que puedan coger su comida
            }
        }
        finally{
            cerrojoElementoComida.unlock();
        }
    }

    //Método para coger un elemento de comida
    public void cogeElementoComida(Hormiga hormiga) throws InterruptedException{
        try{
            cerrojoElementoComida.lock();
            if(getNumElementosComida() <= 0){
                esperaElementoComida.await(); //Si no hay elementos de comida, tendrá que esperar a que haya uno
            }
            setNumElementosComida(getNumElementosComida() - 1); //Cogemos el elemento de comida
            getUnidadesElementosComida().insertarNumero(getNumElementosComida()); //Imprimios el numero de elementos de comida
            getLog().escribirEnLog("[ZONA COMER] --> La hormiga " + hormiga.getIdentificador() + " ha cogido un elemento de comida de la zona para comer");
        }finally{
            cerrojoElementoComida.unlock();
        }
    }

    //Métodos get y set

    public Log getLog() {
        return log;
    }

    public int getNumElementosComida() {
        return numElementosComida;
    }

    public void setNumElementosComida(int numElementosComida) {
        this.numElementosComida = numElementosComida;
    }

    public int getNumHormigasZonaComer() {
        return numHormigasZonaComer;
    }

    public void setNumHormigasZonaComer(int numHormigasZonaComer) {
        this.numHormigasZonaComer = numHormigasZonaComer;
    }

    public ListaThreads getUnidadesElementosComida() {
        return unidadesElementosComida;
    }

    public ListaThreads getListaHormigasZonaComer() {
        return listaHormigasZonaComer;
    }
}

package concurrencia;

import javax.swing.*;
import java.util.concurrent.Semaphore;

public class ZonaComer {
    //Atrubutos de la clase ZonaComer
    private Log log;
    private Semaphore semaforoEntradaSalida = new Semaphore(1, true); //Semáforo para entrar y salir
    private Semaphore semaforoExclusionMutua = new Semaphore(1); //Semáforo para asegurar exclusion mutua a la hora de recoger y depositar
    private Semaphore semaforoDepositar = new Semaphore(1); //Semaforo para depositar el elemento de comida
    private Semaphore semaforoCoger = new Semaphore(0); //Semaforo para coger el elemento de comida

    private int numElementosComida = 0, numHormigasEsprando = 0, numHormigasZonaComer = 0;
    private boolean hormigaEsperandoAlimento = false;
    private ListaThreads unidadesElementosComida, listaHormigasZonaComer;


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
        catch(InterruptedException ie){}
    }

    //Método para salir de la zona para comer
    public void sale(Hormiga hormiga){
        try{
            semaforoEntradaSalida.acquire();
            setNumHormigasZonaComer(getNumHormigasEsprando() - 1); //Decrementamos el numero de hormigas en la lista de hormigas
            getListaHormigasZonaComer().sacarHormiga(hormiga); //Nos quitamos de la lista
            getLog().escribirEnLog("[ZONA COMER] --> La hormiga " + hormiga.getIdentificador() + " ha salido de la zona para comer"); //Escribimos el evento en el log
            semaforoEntradaSalida.release();
        }
        catch(InterruptedException ie){}
    }

    //Método para depositar un elemento de comida
    public void depositaElementoComida(Hormiga hormiga){
        try{
            semaforoDepositar.acquire();
            semaforoExclusionMutua.acquire();
            setNumElementosComida(getNumElementosComida() + 1); //Incrementamos el numero de elementos de comida
            getUnidadesElementosComida().insertarNumero(getNumElementosComida()); //Imprimimos el numero de elementos de comida
            getLog().escribirEnLog("[ZONA COMER] --> La hormiga " + hormiga.getIdentificador() + " ha depositado un elemento de comida en ZonaComer");
        }catch(InterruptedException ie){}
        finally{
            semaforoExclusionMutua.release();
            semaforoCoger.release();
        }
    }

    //Método para coger un elemento de comida
    public void cogeElementoComida(Hormiga hormiga) throws InterruptedException{
        try{
            semaforoCoger.acquire();
            semaforoExclusionMutua.acquire();
            setNumElementosComida(getNumElementosComida() - 1); //Decrementamos el numero de elementos de comida
            getUnidadesElementosComida().insertarNumero(getNumElementosComida()); //Imprimimos el numero de elementos de comida
            getLog().escribirEnLog("[ZONA COMER] --> La hormiga " + hormiga.getIdentificador() + " ha cogido un elemento de comida en ZonaComer");
        }
        finally{
            semaforoExclusionMutua.release();
            semaforoDepositar.release();
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

    public int getNumHormigasEsprando() {
        return numHormigasEsprando;
    }

    public void setNumHormigasEsprando(int numHormigasEsprando) {
        this.numHormigasEsprando = numHormigasEsprando;
    }

    public int getNumHormigasZonaComer() {
        return numHormigasZonaComer;
    }

    public void setNumHormigasZonaComer(int numHormigasZonaComer) {
        this.numHormigasZonaComer = numHormigasZonaComer;
    }

    public boolean isHormigaEsperandoAlimento() {
        return hormigaEsperandoAlimento;
    }

    public void setHormigaEsperandoAlimento(boolean hormigaEsperandoAlimento) {
        this.hormigaEsperandoAlimento = hormigaEsperandoAlimento;
    }

    public ListaThreads getUnidadesElementosComida() {
        return unidadesElementosComida;
    }

    public void setUnidadesElementosComida(ListaThreads unidadesElementosComida) {
        this.unidadesElementosComida = unidadesElementosComida;
    }

    public ListaThreads getListaHormigasZonaComer() {
        return listaHormigasZonaComer;
    }

    public void setListaHormigasZonaComer(ListaThreads listaHormigasZonaComer) {
        this.listaHormigasZonaComer = listaHormigasZonaComer;
    }
}

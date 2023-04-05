package concurrencia;

import javax.swing.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AlmacenComida {
    //Atributos de AlmacenComida
    private final Log log;
    private int numElementosComida = 0;
    private int numHormigasDentro = 0;
    private int numHormigasEsperando = 0;
    private final Semaphore semaforoEntradaSalida = new Semaphore(10);
    private final Lock recogeElemento = new ReentrantLock();
    private final Lock cerrojoNumElementosComida = new ReentrantLock();
    private final Lock cerrojoHormigaEsperando = new ReentrantLock();
    private final Condition conditionElementoComida = recogeElemento.newCondition();
    private final ListaThreads unidadesElementosComida;
    private final ListaThreads listaHormigasAlmacenComida;

    //Métodos de la clase AlmacenComida

    //Método constructor
    public AlmacenComida(Log log, JTextField jTextFieldUnidadesComidaAlmacen, JTextField jTextFieldHormigasAlmacenComida){
        this.unidadesElementosComida = new ListaThreads(jTextFieldUnidadesComidaAlmacen);
        this.listaHormigasAlmacenComida = new ListaThreads(jTextFieldHormigasAlmacenComida);
        this.log = log;
    }

    //Método para entrar al almacen de comida
    public void entra(HormigaObrera hormiga){
        recogeElemento.lock();
        if(hormiga.getNumIdentificador() % 2 == 0){
            if((getNumHormigasEsperando() > 0) || (getNumElementosComida() <= 0)){
                try{
                    setNumHormigasEsperando(getNumHormigasEsperando() + 1);
                    conditionElementoComida.await();
                }catch(InterruptedException ignored){}
            }
        }
        recogeElemento.unlock();
        try{
            semaforoEntradaSalida.acquire();
            incrementaNumHormigasDentro();
            getListaHormigasAlmacenComida().meterHormiga(hormiga);
            getLog().escribirEnLog("[ALMACEN COMIDA} --> La hormiga " + hormiga.getIdentificador() + " ha entrado al almacen de comida");
        }catch(InterruptedException ignored){}
    }

    //Método para salir del almacen de comida
    public void sale(HormigaObrera hormiga){
        decrementaNumHormigasDentro();
        getListaHormigasAlmacenComida().sacarHormiga(hormiga);
        getLog().escribirEnLog("[ALMACEN COMIDA} --> La hormiga " + hormiga.getIdentificador() + " ha salido del almacen de comida");
        semaforoEntradaSalida.release();
    }

    //Método para que una hormiga deposite un elemento de comida
    public void depositaElementoComida(HormigaObrera hormiga){
        recogeElemento.lock();
        try{
            setNumElementosComida(getNumElementosComida() + 5); //Primero depositamos 5 elementos de comida
            getLog().escribirEnLog("[ALMACEN COMIDA] --> La hormiga " + hormiga.getIdentificador() + " ha depositado 5 elementos de comida en el almacen de comida");
            if(getNumHormigasEsperando() > 0){ //Mirar si hay hormigas esperando elementos de comida
                //Verificamos que hay hormigas esperando
                conditionElementoComida.signal(); //Despierta a la hormiga porque ya hay un elemento de comida presente
                setNumHormigasEsperando(getNumHormigasEsperando() - 1);
            }
            //Si no hay hormigas esperando, no tiene sentido dar signal, ya que no despertaría a nadie
        }
        finally{
            recogeElemento.unlock();
        }
    }

    //Método para que una hormiga recoja un elemento de comida
    public void recogeElementoComida(HormigaObrera hormiga){
        recogeElemento.lock();
        try{
            setNumElementosComida(getNumElementosComida() - 5); //Recogemos un elemento de comida
            getLog().escribirEnLog("[ALMACEN COMIDA] --> La hormiga " + hormiga.getIdentificador() + " ha recogido 5 elementos de comida del almacen de comida");
        }
        finally {
            recogeElemento.unlock();
        }
    }



    //Métodos get y set

    public void incrementaNumHormigasDentro(){
        numHormigasDentro = numHormigasDentro + 1;
    }

    public void decrementaNumHormigasDentro(){
        numHormigasDentro = numHormigasDentro - 1;
    }

    //Variables para realizar lectura y escritua en el numero de elementos de comida en el almacén
    public int getNumElementosComida(){
        cerrojoNumElementosComida.lock();
        try{
            return numElementosComida;
        }
        finally{
            cerrojoNumElementosComida.unlock();
        }
    } //Lectura
    public void setNumElementosComida(int numElementosComida){
        cerrojoNumElementosComida.lock();
        try{
            this.numElementosComida = numElementosComida;
            getUnidadesElementosComida().insertarNumero(getNumElementosComida());
        }
        finally{
            cerrojoNumElementosComida.unlock();
        }

    } //Escritura

    //Métodos para realizar lectura y escritura sobre el numero de hormigas esperando
    public int getNumHormigasEsperando(){
        cerrojoHormigaEsperando.lock();
        try{
            return numHormigasEsperando;
        }
        finally{
            cerrojoHormigaEsperando.unlock();
        }

    } //Lectura
    public void setNumHormigasEsperando(int numHormigasEsperando){
        cerrojoHormigaEsperando.lock();
        try{
            this.numHormigasEsperando = numHormigasEsperando;
        }
        finally{
            cerrojoHormigaEsperando.unlock();
        }
    } //Escritura

    //Métodos get de los ListaThreads
    public ListaThreads getUnidadesElementosComida() {
        return unidadesElementosComida;
    }
    public ListaThreads getListaHormigasAlmacenComida() {
        return listaHormigasAlmacenComida;
    }

    //Método get del log
    public Log getLog() {
        return log;
    }
}

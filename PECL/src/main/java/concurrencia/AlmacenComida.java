package concurrencia;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AlmacenComida {
    //Atributos de AlmacenComida
    private Log log;
    private int numElementosComida = 0;
    private int numHormigasDentro = 0;
    private Lock entradaSalida = new ReentrantLock(), unidadComida = new ReentrantLock();
    private Condition colaEspera = entradaSalida.newCondition(), esperaElementoComida = unidadComida.newCondition();
    private boolean hormigaEsperandoElementoComida;

    //Métodos de la clase AlmacenComida

    //Método constructor
    public AlmacenComida(Log log){

    }
    //Método para entrar al almacen de comida
    public void entraAlmacen(Hormiga hormiga){
        entradaSalida.lock();
        try{
            //Primero debemos comprobar que hay sitio, como máximo pueden haber 10 hormigas
            if (getNumHormigasDentro() == 10){
                //Se verifica que no hay hueco, por tanto tendrá que esperar
                try{
                    colaEspera.await();
                    //Una vez que se le despierta al hilo, podrá entrar
                }catch(InterruptedException ie){}
            }
            setNumHormigasDentro(getNumHormigasDentro() + 1);
            getLog().escribirEnLog("[Almacen Comida] --> La hormiga " + hormiga.getIdentificador() + " ha entrado al almacen de comida.");
        }finally{
            entradaSalida.unlock();
        }
    }

    //Método para salir del almacen de comida
    public void saleAlmacen(Hormiga hormiga){
        entradaSalida.lock();
        try{
            //Para salir del almacen, decrementamos en 1 el numero de hormigas que hay dentro del almacén
            setNumHormigasDentro(getNumHormigasDentro() - 1);
            //Una vez que hayamos decrementado el valor, podremos salir del almacen
            getLog().escribirEnLog("[Almacen Comida] --> La hormiga " + hormiga.getIdentificador() + " ha salido del almacen de comida");
        }finally{
            entradaSalida.unlock();
        }
    }

    //Método para depositar un elemento de comida en el almacen
    public void depositaElementoComida(Hormiga hormiga){
        unidadComida.lock();
        try{
            //Para depositar, se tarda un tiempo aleatorio entre 2 y 4 segundos
            Thread.sleep((int) (Math.random() * 2000 + 2000));
            //Una vez transcurrido el tiempo, añadimos el elemento de comida
            setNumElementosComida(getNumElementosComida() + 1);
            getLog().escribirEnLog("[Almacen Comida] --> La hormiga " + hormiga.getIdentificador() + " ha depositado un elemento de comida");
            //Una vez que han dejado un elemento de comida, despertarán a una hormiga en caso de que esté esperando un elemento de comida
            if (isHormigaEsperandoElementoComida()){
                //Se verifica que hay una hormiga esperando, por tanto se le despierta
                esperaElementoComida.signal();
                //Una vez que se ha despertado, pondremos el booleano a false
                setHormigaEsperandoElementoComida(false);
            }
        } catch (InterruptedException e) {}
        finally{
            unidadComida.unlock();
        }
    }

    //Método para recoger un elemento de comida
    public void recogeElementoComida(Hormiga hormiga){
        unidadComida.lock();
        try{
            //En primer lugar, tenemos que comprobar si hay unidades que recoger
            if (getNumElementosComida() == 0){
                //Se verifica que no hay elementos de comida para recoger, por tanto se tendrá que esperar a que haya uno
                try{
                    setHormigaEsperandoElementoComida(true); //Hay una hormiga esperando un elemento de comida para recoger
                    esperaElementoComida.await();
                }catch(InterruptedException ie){}
            }
            //Se verifica que hay un elemento de comida que recoger, tarda entre 1 y 2 segundos
            Thread.sleep((int) (Math.random() * 2000 + 2000));
            //Una vez transcurrido el tiempo, cogemos el elemento de comida
            setNumElementosComida(getNumElementosComida() - 1);
            getLog().escribirEnLog("[Almacen Comida] --> La hormiga " + hormiga.getIdentificador() + " ha recogido un elemento de comida");

        } catch (InterruptedException e) {}
        finally{
            unidadComida.unlock();
        }
    }

    //Métodos get y set
    public int getNumHormigasDentro(){
        return this.numHormigasDentro;
    }
    public void setNumHormigasDentro(int numHormigasDentro){
        this.numHormigasDentro = numHormigasDentro;
    }
    public int getNumElementosComida(){
        return this.numElementosComida;
    }
    public void setNumElementosComida(int numElementosComida){
        this.numElementosComida = numElementosComida;
    }
    public Log getLog(){
        return this.log;
    }
    public boolean isHormigaEsperandoElementoComida(){
        return this.hormigaEsperandoElementoComida;
    }
    public void setHormigaEsperandoElementoComida(boolean hormigaEsperandoElementoComida){
        this.hormigaEsperandoElementoComida = hormigaEsperandoElementoComida;
    }
}

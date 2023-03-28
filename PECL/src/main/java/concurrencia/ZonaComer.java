package concurrencia;

import javax.swing.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ZonaComer {
    //Atrubutos de la clase ZonaComer
    private Log log;
    private Lock entradaSalida = new ReentrantLock(), elementoComida = new ReentrantLock();
    private Condition esperaAlimento = elementoComida.newCondition();
    private int numElementosComida = 0;
    private boolean hormigaEsperandoAlimento = false;
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
            //Imprimimos el numero en el JTextField
            getUnidadesElementosComida().insertarNumero(getNumElementosComida());
            //Antes de irnos, miraremos si hay alguna hormiga esperando comida
            if(isHormigaEsperandoAlimento()){
                //Se verifica que hay una hormiga esperando alimento, por tanto tenemos que despertarla para que coma
                esperaAlimento.signal();
                //Como no hay hormigas esperando, pondremos el booleano a false
                setHormigaEsperandoAlimento(false);
            }

        } catch (InterruptedException e) {}
        finally{
            elementoComida.unlock();
        }
    }

    //Método para que una hormiga coma
    public void come(Hormiga hormiga){
        try{
            cogeElementoComida(hormiga);
            getLog().escribirEnLog("[Zona Comer] --> La hormiga " + hormiga.getIdentificador() + " esta comiendo");
            //Dependiendo del tipo de hormiga, tarda más o menos en comer
            if((hormiga.getTipo() == "Obrera") || (hormiga.getTipo() == "Soldada")){
                //Las hormigas obreras y soldadas tardan en comer 3 segundos
                Thread.sleep(3000);
            }
            else if(hormiga.getTipo() == "Cria"){
                //Una hormiga cria tarda en comer entre 3 y 5 segundos
                Thread.sleep((int) (((Math.random() + 1) * 3000 ) + 2000));
            }

        }catch(InterruptedException ignored){}

    }

    //Método auxiliar al método comer
    private void cogeElementoComida(Hormiga hormiga){
        elementoComida.lock();
        try{
            //En primer lugar tenemos que comprobar si hay un elemento de comida disponible
            if(getNumElementosComida() == 0){
                //Se verifica que no hay elementos de comida disponibles, por tanto tendrán que esperar a que haya alguno
                //Indicaremos que estamos esperando
                setHormigaEsperandoAlimento(true);
                //Una vez indicado, esperaremos a que un hilo nos despierte
                esperaAlimento.await();
            }
            //En el caso de que no haya que esperar, simplemente cogemos el alimento
            setNumElementosComida(getNumElementosComida() - 1);
        }catch(InterruptedException ignored) {}
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

    public ListaThreads getListaHormigasLlevandoComida() {
        return listaHormigasLlevandoComida;
    }

    public ListaThreads getListaHormigasZonaComer() {
        return listaHormigasZonaComer;
    }

    public ListaThreads getUnidadesElementosComida(){
        return unidadesElementosComida;
    }

    public boolean isHormigaEsperandoAlimento() {
        return hormigaEsperandoAlimento;
    }

    public void setHormigaEsperandoAlimento(boolean hormigaEsperandoAlimento) {
        this.hormigaEsperandoAlimento = hormigaEsperandoAlimento;
    }
}

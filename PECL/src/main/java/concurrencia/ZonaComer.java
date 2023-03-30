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
    private int numElementosComida = 0, numHormigasEsprando = 0;
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
    public void entraZonaComer(Hormiga hormiga){
        entradaSalida.lock();
        try{
            //Para entrar, metemos a la hormiga en el JTextField de la zona para comer
            getListaHormigasZonaComer().meterHormiga(hormiga);
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
            //Para salir, quitaremos a la hormiga del JTextField
            getListaHormigasZonaComer().sacarHormiga(hormiga);
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
            //Una vez transcurrido el timepo, incrementamos el numero de elementos de comida
            setNumElementosComida(getNumElementosComida() + 1);
            //Imprimimos el numero en el JTextField
            getUnidadesElementosComida().insertarNumero(getNumElementosComida());
            //Antes de irnos, miraremos si hay alguna hormiga esperando comida
            if(getNumHormigasEsprando() > 0){
                esperaAlimento.signal();
            }
        }
        finally{
            elementoComida.unlock();
        }
    }

    //Método para que una hormiga coma
    public void come(Hormiga hormiga) throws InterruptedException{
        cogeElementoComida(hormiga);
        getLog().escribirEnLog("[Zona Comer] --> La hormiga " + hormiga.getIdentificador() + " esta comiendo");
        //Dependiendo del tipo de hormiga, tarda más o menos en comer
        if((hormiga.getTipo() == "Obrera") || (hormiga.getTipo() == "Soldada")){
            //Las hormigas obreras y soldadas tardan en comer 3 segundos
            Thread.sleep(3000);
        }
        else if(hormiga.getTipo() == "Cria"){
            //Una hormiga cria tarda en comer entre 3 y 5 segundos
            Thread.sleep((int) (((Math.random() + 1) * 3000) + (5000 - (3000 * 2))));
        }
    }

    //Método auxiliar al método comer, para coger un elemento de comida
    private void cogeElementoComida(Hormiga hormiga) throws InterruptedException{
        elementoComida.lock();
        try{
            //En primer lugar tenemos que comprobar si hay un elemento de comida disponible
            if(getNumElementosComida() <= 0){
                setNumHormigasEsprando(getNumHormigasEsprando() + 1);
                esperaAlimento.await();
                //Ya no estaremos esperando
                setNumHormigasEsprando(getNumHormigasEsprando() - 1);
            }
            //En el caso de que no haya que esperar, simplemente cogemos el alimento
            setNumElementosComida(getNumElementosComida() - 1);
            getUnidadesElementosComida().insertarNumero(getNumElementosComida());
        }
        finally{
            elementoComida.unlock();
        }
    }

    //Métodos get y set
    public Log getLog(){
        return this.log;
    }
    public synchronized int getNumElementosComida(){
        return this.numElementosComida;
    }
    public synchronized void setNumElementosComida(int numElementosComida){
        this.numElementosComida = numElementosComida;
    }

    public ListaThreads getListaHormigasZonaComer() {
        return listaHormigasZonaComer;
    }

    public ListaThreads getUnidadesElementosComida(){
        return unidadesElementosComida;
    }

    public synchronized int getNumHormigasEsprando() {
        return numHormigasEsprando;
    }

    public synchronized void setNumHormigasEsprando(int numHormigasEsprando) {
        this.numHormigasEsprando = numHormigasEsprando;
    }
}

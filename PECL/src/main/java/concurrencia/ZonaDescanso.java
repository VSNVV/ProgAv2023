package concurrencia;

import javax.swing.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ZonaDescanso {
    //Atributos de la clase ZonaDescanso
    private final Log log;
    private final Lock entradaSalida = new ReentrantLock();
    private final ListaThreads listaHormigasDescansando;

    //Métodos de la clase ZonaDescanso

    //Método constructor
    public ZonaDescanso(Log log, JTextField jTextFieldHormigasDescansando){
        this.log = log;
        this.listaHormigasDescansando = new ListaThreads(jTextFieldHormigasDescansando);
    }

    //Método para entrar a la zona de descanso
    public void entra(Hormiga hormiga){
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
    public void sale(Hormiga hormiga) throws InterruptedException{
        entradaSalida.lock();
        try{
            //Para salir, tenemos que quitar el identificador de la hormiga del JTextField correspondiente
            getListaHormigasDescansando().sacarHormiga(hormiga);
            //Una vez fuera, escribimos el evento en el log
            getLog().escribirEnLog("[ZONA DESCANSO] --> La hormiga " + hormiga.getIdentificador() + " ha salido de la zona de descanso");

        }finally{
            entradaSalida.unlock();
        }
    }

    //Método para que una hormiga haga su descanso
    public void realizaDescanso(Hormiga hormiga) throws InterruptedException{
        //Escribimos el evento en el log
        getLog().escribirEnLog("[ZONA DESCANSO] --> La hormiga " + hormiga.getIdentificador() + " esta descansando");
        if (hormiga.getTipo().equals("Soldada")){
            //Una hormiga soldada descansa un tiempo aleatorio entre 2 y 8 segundos
            Thread.sleep((int) (((Math.random() + 1) * 2000) + (8000 - (2000 * 2))));
        }
        else if (hormiga.getTipo().equals("Obrera")){
            //Una hormiga obrera realiza un descanso de 1 segundo
            Thread.sleep(1000);
        }
        else{
            //Una hormiga cria realiza un descanso de 4 segundos
            Thread.sleep(4000);
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

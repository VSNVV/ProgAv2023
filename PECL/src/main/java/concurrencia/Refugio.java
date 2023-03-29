package concurrencia;

import javax.swing.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Refugio {
    //Atributos de la clase Refugio
    private Log log; //Log del sistema concurrente
    private Lock entradaSalida = new ReentrantLock(); //Lock para la entrada y salida de las hormigas cria
    private Lock invasion = new ReentrantLock(); //Lock para gestionar la invasión
    private Condition finInvasion = invasion.newCondition(); //Condition para que las hormigas esperen al fin de la invasion
    private ListaThreads listaHormigasRefugio; //ListaThreads para manejar el JTextField del refugio de la interfaz

    //Métodos de la clase Refugio

    //Método constructor
    public Refugio(Log log, JTextField jTextFieldHormigasRefugio){
        this.log = log;
        this.listaHormigasRefugio = new ListaThreads(jTextFieldHormigasRefugio);
    }

    //Método para que las hormigas entren al refugio
    public void entraRefugio(Hormiga hormiga){
        entradaSalida.lock();
        try{
            //Añadimos a la hormiga a la lista del refugio
            getListaHormigasRefugio().meterHormiga(hormiga);
            //Una vez que esta metida, escribimos el evento en el log
            getLog().escribirEnLog("[Refugio] --> La hormiga " + hormiga.getIdentificador() + " ha entrado al refugio");
        }
        finally{
            entradaSalida.unlock();
        }
    }

    //Método para esperar al fin de la invasion
    public void esperaFinInvasion(Hormiga hormiga){
        invasion.lock();
        try{
            //En primer lugar, tienen que mirar si hay una invasion presente
            if (hormiga.getColonia().isInvasionInsecto()){
                //Se verifica que hay una invasion, por tanto se deberá dormir hasta que se acabe la invasion
                finInvasion.await();
            }
        }
        catch(InterruptedException ignored) {}
        finally{
            invasion.unlock();
        }
    }

    //Método para salir del refugio
    public void saleRefugio(Hormiga hormiga){
        entradaSalida.lock();
        try{
            //Para salir, quitamos al hormiga del JTextField correspondiente
            getListaHormigasRefugio().sacarHormiga(hormiga);
            //Una vez fuera del JTextField, escribimos el evento en el log
            getLog().escribirEnLog("[Refugio] --> La hormiga " + hormiga.getIdentificador() + " ha salido del refugio");
        }
        finally{
            entradaSalida.unlock();
        }
    }

    //Métodos get y set

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public ListaThreads getListaHormigasRefugio() {
        return listaHormigasRefugio;
    }

    public void setListaHormigasRefugio(ListaThreads listaHormigasRefugio) {
        this.listaHormigasRefugio = listaHormigasRefugio;
    }
}

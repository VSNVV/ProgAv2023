package concurrencia;

import javax.swing.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Refugio {
    //Atributos de la clase Refugio
    private Log log; //Log del sistema concurrente
    private boolean activo = false;
    private int numHormigasRefugio = 0;
    private Lock cerrojoRefugio = new ReentrantLock();
    private Condition finInvasion = cerrojoRefugio.newCondition(); //Condition para que las hormigas esperen al fin de la invasion
    private ListaThreads listaHormigasRefugio; //ListaThreads para manejar el JTextField del refugio de la interfaz

    //Métodos de la clase Refugio

    //Método constructor
    public Refugio(Log log, JTextField jTextFieldHormigasRefugio){
        this.log = log;
        this.listaHormigasRefugio = new ListaThreads(jTextFieldHormigasRefugio);
    }

    //Método para que las crias usen el refugio
    public void protegeRefugio(Hormiga hormiga){
        try{
            cerrojoRefugio.lock();
            if(isActivo()){
                hormiga.getPaso().mirar();
                entra(hormiga);
                hormiga.getPaso().mirar();
                try{
                    finInvasion.await();
                    hormiga.getPaso().mirar();
                }catch(InterruptedException ie){}
                sale(hormiga);
            }
        }
        catch(InterruptedException ie){}
        finally{
            cerrojoRefugio.unlock();
        }
    }

    //Método para que las hormigas entren al refugio
    private void entra(Hormiga hormiga){
        //Añadimos a la hormiga a la lista del refugio
        getListaHormigasRefugio().meterHormiga(hormiga);
        setNumHormigasRefugio(getNumHormigasRefugio() + 1);
        //Una vez que esta metida, escribimos el evento en el log
        getLog().escribirEnLog("[REFUGIO] --> La hormiga " + hormiga.getIdentificador() + " ha entrado al refugio");
    }

    //Método para salir del refugio
    private void sale(Hormiga hormiga){
        //Para salir, quitamos al hormiga del JTextField correspondiente
        getListaHormigasRefugio().sacarHormiga(hormiga);
        setNumHormigasRefugio(getNumHormigasRefugio() - 1);
        //Una vez fuera del JTextField, escribimos el evento en el log
        getLog().escribirEnLog("[REFUGIO] --> La hormiga " + hormiga.getIdentificador() + " ha salido del refugio");
        if(getNumHormigasRefugio() == 0){
            setActivo(false);
        }
    }

    //Método para despertar a las hormigas cria despues de la invasion
    public void despiertaRefugio(){
        try{
            cerrojoRefugio.lock();
            setActivo(false); //El refugio no estará activo
            finInvasion.signalAll(); //Despertamos a las crias que están dentro del refugio
        }
        finally{
            cerrojoRefugio.unlock();
        }
    }

    //Métodos get y set

    public Log getLog() {
        return log;
    }

    public ListaThreads getListaHormigasRefugio() {
        return listaHormigasRefugio;
    }

    public boolean isActivo() {
        try{
            cerrojoRefugio.lock();
            return this.activo;
        }finally{
            cerrojoRefugio.unlock();
        }
    }

    public void setActivo(boolean activo){
        this.activo = activo;
    }

    public Lock getCerrojoRefugio() {
        return cerrojoRefugio;
    }

    public int getNumHormigasRefugio() {
        return numHormigasRefugio;
    }

    public void setNumHormigasRefugio(int numHormigasRefugio) {
        this.numHormigasRefugio = numHormigasRefugio;
    }
}

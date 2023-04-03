package concurrencia;

import javax.swing.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Invasion {
    //Atributos de la clase Invasion
    private Log log;
    private int numHormigasSoldado = 0;
    private boolean activa = false;
    private boolean enCurso = false;
    private Lock cerrojoInvasion = new ReentrantLock();
    private Condition conditionInvasion = cerrojoInvasion.newCondition();
    private ListaThreads listaHormigasInvasion;
    //Métodos de la clase Invasion

    //Método constructor
    public Invasion(Log log, JTextField jTextFieldHormigasContraInvasor){
        this.log = log;
        this.listaHormigasInvasion = new ListaThreads(jTextFieldHormigasContraInvasor);
    }

    //Método para unirse a la invasion
    public void realizaInvasion(Hormiga hormiga){
        try{
            cerrojoInvasion.lock();
            if(isActiva() && (!isEnCurso())){
                hormiga.getPaso().mirar();
                entra(hormiga);
                if(activarInvasionEnCurso(hormiga)){
                    setEnCurso(true); //Indicamos que la invasion está en curso
                    getLog().escribirEnLog("[INVASION] --> Las hormigas estan combatiendo al insecto invasor");
                    cerrojoInvasion.unlock();
                    Thread.sleep(20000);
                    hormiga.getPaso().mirar();
                    cerrojoInvasion.lock();
                    getLog().escribirEnLog("[INVASION] --> Las hormigas han vencido al insecto invasor");
                    conditionInvasion.signalAll(); //Despertamos a las hormigas soldadas
                    hormiga.getRefugio().despiertaRefugio(); //Despertamos a las crias pq ya ha terminado la invasion
                }
                else{
                    conditionInvasion.await();
                }
                hormiga.getPaso().mirar();
                sale(hormiga);
            }
        }catch (InterruptedException ie){}
        cerrojoInvasion.unlock();
    }

    //Método para entrar a la invasion
    private void entra(Hormiga hormiga){
        setNumHormigasSoldado(getNumHormigasSoldado() + 1); //Incrementamos el numero de hormigas soldado
        getListaHormigasInvasion().meterHormiga(hormiga); //Metemos el identificador de la hormiga al JTextField
        getLog().escribirEnLog("[INVASION] --> La hormiga " + hormiga.getIdentificador() + " ha entrado a la invasion");
    }

    //Método para comprobar si la invasion deberia estar en curso o no
    private boolean activarInvasionEnCurso(Hormiga hormiga){
        hormiga.getColonia().getEntradaColonia().lock();
        boolean result;
        int numHormigasInvasion = getNumHormigasSoldado();
        int numHormigasSoldadoColonia = hormiga.getColonia().cuentaSoldadas();
        result = numHormigasInvasion == numHormigasSoldadoColonia;
        hormiga.getColonia().getEntradaColonia().unlock();

        return result;
    }

    private void sale(Hormiga hormiga){
        setNumHormigasSoldado(getNumHormigasSoldado() - 1); //Decrementamos el numero de hormigas soldado
        getListaHormigasInvasion().sacarHormiga(hormiga); //Quitamos el identificador de la hormiga del JTextField
        getLog().escribirEnLog("[INVASION] --> La hormiga " + hormiga.getIdentificador() + " ha salido de la invasion");
        if(getNumHormigasSoldado() == 0){
            //Es la última hormiga en salir, por tanto la invasion queda finalizada
            setEnCurso(false); //La invasión no estará en curso
            setActiva(false); //La invasión dejara de estar activa
        }
    }


    //Métodos get y set


    public Log getLog() {
        return log;
    }

    public int getNumHormigasSoldado() {
        return numHormigasSoldado;
    }

    public void setNumHormigasSoldado(int numHormigasSoldado) {
        this.numHormigasSoldado = numHormigasSoldado;
    }

    public synchronized boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isEnCurso() {
        return enCurso;
    }

    public void setEnCurso(boolean enCurso) {
        this.enCurso = enCurso;
    }

    public ListaThreads getListaHormigasInvasion() {
        return listaHormigasInvasion;
    }

    public Lock getCerrojoInvasion() {
        return cerrojoInvasion;
    }
}

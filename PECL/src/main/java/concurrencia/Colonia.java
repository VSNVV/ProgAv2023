package concurrencia;

import javax.swing.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Colonia { //Recurso compartido por todos los hilos
    //Atributos de la clase Colonia
    private int numHormigasSoldado = 0, numHormigasEnInvasion = 0;
    private boolean invasionInsecto = false;
    private Log log; //Log del sistema concurrente
    private Lock entradaColonia = new ReentrantLock(); //Lock del tunel para entrar a la colonia
    private Lock salidaColonia1 = new ReentrantLock(); //Lock del primer tunel de salida de la colonia
    private Lock salidaColonia2 = new ReentrantLock(); //Lock del segundo tunel de salida de la colonia
    private Lock cerrojoInvasion = new ReentrantLock(); //Lock de la invasion del insecto
    private Condition hormigasEsperandoInvasion = cerrojoInvasion.newCondition();
    private AlmacenComida almacenComida; //Almacen de comida de la colonia
    private ZonaComer zonaComer; //Zona para comer de la colonia
    private ZonaInstruccion zonaInstruccion; //Zona de instruccion de la colonia
    private ZonaDescanso zonaDescanso; //Zona de descanso de la colonia

    //Métodos de la clase colonia

    //Método constructor
    public Colonia(Log log, JTextField jTextFieldHormigasBuscandoComida, JTextField jTextFieldHormigasContraInvasor,
                   JTextField jTextFieldHormigasAlmacenComida, JTextField jTextFieldHormiasLlevandoComida,
                   JTextField jTextFieldHormigasHaciendoInstruccion, JTextField jTextFieldUnidadesComidaAlmacen,
                   JTextField jTextFieldUnidadesComidaZonaComer, JTextField jTextFieldHormigasDescansando,
                   JTextField jTextFieldHormigasZonaComer, JTextField jTextFieldHormigasRefugio){
        this.log = log;
        //Crear aqui todas las zonas, para luego en distribuida pasar un solo objeto que tenga toda la parte concurrente
        this.almacenComida = new AlmacenComida(log, jTextFieldUnidadesComidaAlmacen, jTextFieldHormigasAlmacenComida);
        this.zonaComer = new ZonaComer(log, jTextFieldUnidadesComidaZonaComer, jTextFieldHormiasLlevandoComida, jTextFieldHormigasZonaComer);
        this.zonaInstruccion = new ZonaInstruccion(log, jTextFieldHormigasHaciendoInstruccion);
        this.zonaDescanso = new ZonaDescanso(log, jTextFieldHormigasDescansando);

    }

    //Método para entrar a la colonia
    public void entraColonia(Hormiga hormiga){
        entradaColonia.lock();
        try{
            getLog().escribirEnLog("[COLONIA] --> La hormiga " + hormiga.getIdentificador() + " ha entrado a la colonia");
        }finally{
            entradaColonia.unlock();
        }
    }

    //Método para que las hormigas obreras salgan de la colonia
    public void saleColonia(Hormiga hormiga){
        if(salidaColonia1.tryLock()){
            try{
                //Se verifica que el tunel de salida 1 está libre, por tanto puede salir por ese tunel
                getLog().escribirEnLog("[COLONIA] --> La hormiga " + hormiga.getIdentificador() + "ha salido de la colonia por el tunel de salida 1");

            }finally{
                salidaColonia1.unlock();
            }
        }
        else{
            //Como el tunel de salida 1 está ocupado en ese momento, la hormiga intentará salir por el tunel de salida 2
            salidaColonia2.lock();
            try{
                getLog().escribirEnLog("[COLONIA] --> La hormiga " + hormiga.getIdentificador() + "ha salido de la colonia por el tunel de salida 2");
            }finally{
                salidaColonia2.unlock();
            }
        }
    }

    //Método para comprobar su hay una invasion
    public void invasion(Hormiga hormiga){
        cerrojoInvasion.lock();
        try{
            //Una hormiga se suma a la invasión, por tanto incrementamos el numero de hormigas en la invasion
            setNumHormigasEnInvasion(getNumHormigasEnInvasion() + 1);
            //En primer lugar, tenemos que ver si es la ultima hormiga que queda
            if((getNumHormigasEnInvasion()) != (getNumHormigasSoldado())){
                //Verificamos no es la úlitma, por tanto se dormirá
                hormigasEsperandoInvasion.await();
            }
            else{
                //Se verifica que es la última, por tanto salen a la invasion
                //poner booleano para que otras hormigas no se puedan meter durante una invasión
                getLog().escribirEnLog("[Invasion] --> Las hormigas estan repeliendo al insecto invasor");
                //La invasion dura 20 segundos
                Thread.sleep(2000);
                //Una vez finalizada la invasión, despertamos a todos los hilos
            }
        }catch (InterruptedException ingnored){}
        finally{
            cerrojoInvasion.unlock();
        }
    }

    //Métodos get y set
    public Log getLog(){
        return this.log;
    }
    public AlmacenComida getAlmacenComida(){
        return this.almacenComida;
    }
    public ZonaComer getZonaComer(){
        return this.zonaComer;
    }
    public ZonaInstruccion getZonaInstruccion(){
        return this.zonaInstruccion;
    }
    public ZonaDescanso getZonaDescanso(){
        return this.zonaDescanso;
    }

    public boolean isInvasionInsecto() {
        return this.invasionInsecto;
    }

    public void setInvasionInsecto(boolean invasionInsecto){
        this.invasionInsecto = invasionInsecto;
    }

    public int getNumHormigasSoldado() {
        return numHormigasSoldado;
    }

    public void setNumHormigasSoldado(int numHormigasSoldado) {
        this.numHormigasSoldado = numHormigasSoldado;
    }

    public int getNumHormigasEnInvasion() {
        return numHormigasEnInvasion;
    }

    public void setNumHormigasEnInvasion(int numHormigasEnInvasion) {
        this.numHormigasEnInvasion = numHormigasEnInvasion;
    }
}

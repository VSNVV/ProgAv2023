package concurrencia;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Colonia { //Recurso compartido por todos los hilos
    //Atributos de la clase Colonia
    private Log log;
    //todas las zonas y mierdas aqui, se construyen en el constructor
    private Lock entradaColonia = new ReentrantLock(); //Lock del tunel para entrar a la colonia
    private Lock salidaColonia1 = new ReentrantLock(); //Lock del primer tunel de salida de la colonia
    private Lock salidaColonia2 = new ReentrantLock(); //Lock del segundo tunel de salida de la colonia
    private AlmacenComida almacenComida;
    private ZonaComer zonaComer;

    //Métodos de la clase colonia

    //Método constructor
    public Colonia(Log log){
        this.log = log;
        //Crear aqui todas las zonas, para luego en distribuida pasar un solo objeto que tenga toda la parte concurrente
        this.almacenComida = new AlmacenComida(log);
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

}

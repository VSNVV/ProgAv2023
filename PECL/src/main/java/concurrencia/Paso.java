package concurrencia;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Si el booleano vale false (abierto) el proceso puede continuar, pero  si es true(cerrado) el proceso se detiene
public class Paso{
    //Atributos de la clase Paso
    private boolean cerrado = false;
    private final Lock cerrojo = new ReentrantLock();
    private final Condition parar = cerrojo.newCondition();

    //Métodos de la clase Paso

    //Método constructor
    public Paso(){

    }
    //Método que mira si nos tenemos que detener o no
    public void mirar() throws InterruptedException{
        try {
            cerrojo.lock();
            while(isCerrado()) {
                parar.await();
            }
        }
        finally {
            cerrojo.unlock();
        }
    }
    //Método para reanudar la ejecucion de hilos
    public void abrir() {
        try{
            cerrojo.lock();
            setCerrado(false);
            parar.signalAll();
        }
        finally {
            cerrojo.unlock();
        }
    }
    //Métodos para detener la ejecucion de hilos
    public void cerrar() {
        try{
            cerrojo.lock();
            setCerrado(true);
        }
        finally
        {
            cerrojo.unlock();
        }
    }

    public boolean isCerrado() {
        return cerrado;
    }

    public void setCerrado(boolean cerrado) {
        this.cerrado = cerrado;
    }
}

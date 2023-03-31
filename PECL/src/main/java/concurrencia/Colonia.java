package concurrencia;

import javax.swing.*;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Colonia { //Recurso compartido por todos los hilos
    //Atributos de la clase Colonia
    private int numHormigasSoldado = 0, numHormigasEnInvasion = 0;
    private boolean invasionInsecto = false, invasionEnCurso = false;
    private Log log; //Log del sistema concurrente
    private Lock entradaColonia = new ReentrantLock(); //Lock del tunel para entrar a la colonia
    private Lock salidaColonia1 = new ReentrantLock(); //Lock del primer tunel de salida de la colonia
    private Lock salidaColonia2 = new ReentrantLock(); //Lock del segundo tunel de salida de la colonia
    private Lock cerrojoBuscarComida = new ReentrantLock();
    private Lock cerrojoInvasion = new ReentrantLock(); //Lock de la invasion del insecto
    private Condition hormigasEsperandoInvasion = cerrojoInvasion.newCondition();
    private ArrayList<Hormiga> listaHormigas = new ArrayList<Hormiga>();
    private ListaThreads listaHormigasInvasor; //ListaThreads para manejar el JTextField de la invasion
    private ListaThreads listaHormigasBuscandoComida; //ListaThreads para manejar el JTextField de hormigas buscando comida
    private ListaThreads listaHormigasLlevandoComida; //ListaThreads para manejar el JTextField de hormigas llevando comida
    private AlmacenComida almacenComida; //Almacen de comida de la colonia
    private ZonaComer zonaComer; //Zona para comer de la colonia
    private ZonaInstruccion zonaInstruccion; //Zona de instruccion de la colonia
    private ZonaDescanso zonaDescanso; //Zona de descanso de la colonia
    private Refugio refugio;

    //Métodos de la clase colonia

    //Método constructor
    public Colonia(Log log, JTextField jTextFieldHormigasBuscandoComida, JTextField jTextFieldHormigasContraInvasor,
                   JTextField jTextFieldHormigasAlmacenComida, JTextField jTextFieldHormiasLlevandoComida,
                   JTextField jTextFieldHormigasHaciendoInstruccion, JTextField jTextFieldUnidadesComidaAlmacen,
                   JTextField jTextFieldUnidadesComidaZonaComer, JTextField jTextFieldHormigasDescansando,
                   JTextField jTextFieldHormigasZonaComer, JTextField jTextFieldHormigasRefugio){
        this.log = log;
        this.listaHormigasBuscandoComida = new ListaThreads(jTextFieldHormigasBuscandoComida);
        this.listaHormigasInvasor = new ListaThreads(jTextFieldHormigasContraInvasor);
        this.listaHormigasLlevandoComida = new ListaThreads(jTextFieldHormiasLlevandoComida);
        //Crear aqui todas las zonas, para luego en distribuida pasar un solo objeto que tenga toda la parte concurrente
        this.almacenComida = new AlmacenComida(log, jTextFieldUnidadesComidaAlmacen, jTextFieldHormigasAlmacenComida);
        this.zonaComer = new ZonaComer(log, jTextFieldUnidadesComidaZonaComer, jTextFieldHormigasZonaComer);
        this.zonaInstruccion = new ZonaInstruccion(log, jTextFieldHormigasHaciendoInstruccion);
        this.zonaDescanso = new ZonaDescanso(log, jTextFieldHormigasDescansando);
        this.refugio = new Refugio(log, jTextFieldHormigasRefugio);

    }

    //Método para entrar a la colonia
    public void entraColonia(Hormiga hormiga){
        entradaColonia.lock();
        try{
            getLog().escribirEnLog("[COLONIA] --> La hormiga " + hormiga.getIdentificador() + " ha entrado a la colonia");
            //A todas las hormigas que entran les añadimos a un arraylist
            getListaHormigas().add(hormiga);
            if(hormiga.getTipo() == "Soldada"){
                setNumHormigasSoldado(getNumHormigasSoldado() + 1);
            }
            //En el caso de que sea soldada u cria, tiene que mirar si hay una invasion

        }finally{
            entradaColonia.unlock();
        }
    }

    //Método para que las hormigas obreras salgan de la colonia
    public void saleColonia(Hormiga hormiga){
        if(salidaColonia1.tryLock()){
            try{
                //Se verifica que el tunel de salida 1 está libre, por tanto puede salir por ese tunel
                getLog().escribirEnLog("[COLONIA] --> La hormiga " + hormiga.getIdentificador() + " ha salido de la colonia por el tunel de salida 1");

            }finally{
                salidaColonia1.unlock();
            }
        }
        else{
            //Como el tunel de salida 1 está ocupado en ese momento, la hormiga intentará salir por el tunel de salida 2
            salidaColonia2.lock();
            try{
                getLog().escribirEnLog("[COLONIA] --> La hormiga " + hormiga.getIdentificador() + " ha salido de la colonia por el tunel de salida 2");
            }finally{
                salidaColonia2.unlock();
            }
        }
    }

    //Método para buscar comida
    public void buscaComida(Hormiga hormiga){
        //En primer lugar, nos metemos al JTextField de buscando comida
        getListaHormigasBuscandoComida().meterHormiga(hormiga);
        //Una vez metidos, nos saldremos de la colonia
        saleColonia(hormiga);
        //Tardamos 4 segundos en buscar comida
        try{
            Thread.sleep(4000);
        }
        catch(InterruptedException ignored){}
        //Una vez que ya hemos buscado comida volvemos a la colonia
        entraColonia(hormiga);
        //Una vez entra a la colonia, dejará de estar buscando comida
        getListaHormigasBuscandoComida().sacarHormiga(hormiga);
    }

    //Método para comprobar su hay una invasion
    public void invasion(Hormiga hormiga){
        actualizaEstadoInvasion(hormiga);
        cerrojoInvasion.lock();
        try{
            if ((isInvasionInsecto() && (!isInvasionEnCurso()))){
                //Se verica que hay una invasion activa y que la invasion no esta en curso, por tanto se puede unir
                //Una hormiga se suma a la invasión, por tanto incrementamos el numero de hormigas en la invasion
                setNumHormigasEnInvasion(getNumHormigasEnInvasion() + 1);
                //Una vez añadida a la invasion, la metemos en el JTextField
                getListaHormigasInvasor().meterHormiga(hormiga);
                //En primer lugar, tenemos que ver si es la ultima hormiga que queda
                if((getNumHormigasEnInvasion()) != (getNumHormigasSoldado())){
                    //Verificamos no es la úlitma, por tanto se dormirá
                    hormigasEsperandoInvasion.await();
                }
                else{
                    //Se verifica que es la última, por tanto salen a la invasion
                    setInvasionEnCurso(true);
                    //poner booleano para que otras hormigas no se puedan meter durante una invasión
                    getLog().escribirEnLog("[Invasion] --> Las hormigas estan repeliendo al insecto invasor");
                    //La invasion dura 20 segundos
                    Thread.sleep(20000);
                    //Una vez finalizada la invasión, despertamos a todos los hilos y escribimos el evento en el log
                    getLog().escribirEnLog("[Invasion] --> La invasion ha terminado, el insecto invasor ha huido");
                    hormigasEsperandoInvasion.signalAll();
                    getRefugio().indicaFinInvasion();

                }
                //Una vez finalizada la invasion, las hormigas se irán de la invasión
                setNumHormigasEnInvasion(getNumHormigasEnInvasion() - 1);
                //Una vez que se han ido, se saldrán del JTextField de invasion
                getListaHormigasInvasor().sacarHormiga(hormiga);
            }
        }
        catch(InterruptedException ignored){}
        finally{
            cerrojoInvasion.unlock();
        }
    }

    //Método auxiliar a la invasion, para comprobar si hay una invasion o no a las hormigas que entran nuevas
    public void compruebaInvasion(Hormiga hormiga){
        cerrojoInvasion.lock();
        try{
            if((isInvasionInsecto() && !isInvasionEnCurso()) && (hormiga.getTipo() != "Obrera")){
                //Se tiene que ir a la invasion, lanzaremos un InterruptedException
                {
                    if(hormiga.getTipo().equals("Soldada")){
                        invasion(hormiga);
                    }
                    else{
                        getRefugio().entraRefugio(hormiga);
                    }
                }
            }
        }
        finally{
            cerrojoInvasion.unlock();
        }
    }

    //Método auxiliar a la invasion
    public synchronized void actualizaEstadoInvasion(Hormiga hormiga){
        try{
            //Tenemos que ver en que zona está la hormiga, que pueden estar en ZonaComer, ZonaInstruccion o ZonaDescanso
            //ZonaComer
            if(getZonaComer().getListaHormigasZonaComer().getListaHormigas().contains(hormiga)){
                //Está en ZonaComer, por tanto deberán salir del almacen
                getZonaComer().saleZonaComer(hormiga);
            }
            else if(getZonaInstruccion().getListaHormigasHaciendoInstruccion().getListaHormigas().contains(hormiga)){
                //Está haciendo instruccion, por tanto se va de ZonaInstruccion
                getZonaInstruccion().saleZonaInstruccion(hormiga);
            }
            else if(getZonaDescanso().getListaHormigasDescansando().getListaHormigas().contains(hormiga)){
                //Está haciendo un descanso, por tanto saldrán del descanso
                getZonaDescanso().saleZonaDescanso(hormiga);
            }
        }
        catch(InterruptedException ignored){}
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
    public Refugio getRefugio(){
        return this.refugio;
    }
    public ListaThreads getListaHormigasInvasor() {
        return listaHormigasInvasor;
    }
    public ListaThreads getListaHormigasBuscandoComida() {
        return listaHormigasBuscandoComida;
    }
    public ListaThreads getListaHormigasLlevandoComida() {
        return listaHormigasLlevandoComida;
    }
    public ArrayList<Hormiga> getListaHormigas() {
        return listaHormigas;
    }

    public boolean isInvasionInsecto() {
        return invasionInsecto;
    }
    public void setInvasionInsecto(boolean invasionInsecto) {
        this.invasionInsecto = invasionInsecto;
    }
    public boolean isInvasionEnCurso() {
        return invasionEnCurso;
    }
    public void setInvasionEnCurso(boolean invasionEnCurso) {
        this.invasionEnCurso = invasionEnCurso;
    }

    public synchronized int getNumHormigasSoldado() {
        return numHormigasSoldado;
    }
    public synchronized void setNumHormigasSoldado(int numHormigasSoldado) {
        this.numHormigasSoldado = numHormigasSoldado;
    }
    public synchronized int getNumHormigasEnInvasion() {
        return numHormigasEnInvasion;
    }
    public synchronized void setNumHormigasEnInvasion(int numHormigasEnInvasion) {
        this.numHormigasEnInvasion = numHormigasEnInvasion;
    }
}

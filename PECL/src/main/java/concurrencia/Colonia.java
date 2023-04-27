package concurrencia;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Colonia { //Recurso compartido por todos los hilos
    //Atributos de la clase Colonia
    private int numHormigasSoldado = 0;
    private final Log log; //Log del sistema concurrente
    private final Lock entradaColonia = new ReentrantLock(); //Lock del tunel para entrar a la colonia
    private final Lock salidaColonia1 = new ReentrantLock(); //Lock del primer tunel de salida de la colonia
    private final Lock salidaColonia2 = new ReentrantLock(); //Lock del segundo tunel de salida de la colonia
    private final ArrayList<Hormiga> listaHormigas = new ArrayList<>(); //Lista que contiene todas las hormigas que han entrado a la colonia
    private final ListaThreads listaHormigasBuscandoComida; //ListaThreads para manejar el JTextField de hormigas buscando comida
    private final ListaThreads listaHormigasLlevandoComida; //ListaThreads para manejar el JTextField de hormigas llevando comida
    private final AlmacenComida almacenComida; //Almacen de comida de la colonia
    private final ZonaComer zonaComer; //Zona para comer de la colonia
    private final ZonaInstruccion zonaInstruccion; //Zona de instruccion de la colonia
    private final ZonaDescanso zonaDescanso; //Zona de descanso de la colonia
    private final Refugio refugio;
    private final Invasion invasion;
    private final Paso paso;

    //Métodos de la clase colonia

    //Método constructor
    public Colonia(Log log, JTextField jTextFieldHormigasBuscandoComida, JTextField jTextFieldHormigasContraInvasor,
                   JTextField jTextFieldHormigasAlmacenComida, JTextField jTextFieldHormiasLlevandoComida,
                   JTextField jTextFieldHormigasHaciendoInstruccion, JTextField jTextFieldUnidadesComidaAlmacen,
                   JTextField jTextFieldUnidadesComidaZonaComer, JTextField jTextFieldHormigasDescansando,
                   JTextField jTextFieldHormigasZonaComer, JTextField jTextFieldHormigasRefugio){
        this.log = log;
        this.listaHormigasBuscandoComida = new ListaThreads(jTextFieldHormigasBuscandoComida);
        this.listaHormigasLlevandoComida = new ListaThreads(jTextFieldHormiasLlevandoComida);
        //Crear aqui todas las zonas, para luego en distribuida pasar un solo objeto que tenga toda la parte concurrente
        this.almacenComida = new AlmacenComida(log, jTextFieldUnidadesComidaAlmacen, jTextFieldHormigasAlmacenComida);
        this.zonaComer = new ZonaComer(log, jTextFieldUnidadesComidaZonaComer, jTextFieldHormigasZonaComer);
        this.zonaInstruccion = new ZonaInstruccion(log, jTextFieldHormigasHaciendoInstruccion);
        this.zonaDescanso = new ZonaDescanso(log, jTextFieldHormigasDescansando);
        this.refugio = new Refugio(log, jTextFieldHormigasRefugio);
        this.invasion = new Invasion(log, jTextFieldHormigasContraInvasor);
        this.paso = new Paso();

    }

    //Método para entrar a la colonia
    public void entra(Hormiga hormiga){
        entradaColonia.lock();
        try{
            Thread.sleep(100);
        }catch(InterruptedException ignored){}
        if(!getListaHormigas().contains(hormiga)){
            getListaHormigas().add(hormiga); //A todas las hormigas que entran les añadimos a un arraylist
        }
        getLog().escribirEnLog("[COLONIA] --> La hormiga " + hormiga.getIdentificador() + " ha entrado a la colonia");
        if(hormiga.getTipo().equals("Soldada")){
            try{
                hormiga.getPaso().mirar();
            }catch (InterruptedException ignored){}
            setNumHormigasSoldado(getNumHormigasSoldado() + 1);
            entradaColonia.unlock();
            getInvasion().realizaInvasion(hormiga); //Si la invasion no está activa este método serña inutil
        }
        else if(hormiga.getTipo().equals("Cria")){
            entradaColonia.unlock();
            getRefugio().protegeRefugio(hormiga); //Si no está activo, esta función será inutil
        }
        else{
            entradaColonia.unlock();
        }
    }

    //Método para que las hormigas obreras salgan de la colonia
    public void sale(Hormiga hormiga){
        if(salidaColonia1.tryLock()){
            try{
                //Se verifica que el tunel de salida 1 está libre, por tanto puede salir por ese tunel
                try{
                    Thread.sleep(100);
                }catch(InterruptedException ignored){}
                getLog().escribirEnLog("[COLONIA] --> La hormiga " + hormiga.getIdentificador() + " ha salido de la colonia por el tunel de salida 1");

            }finally{
                salidaColonia1.unlock();
            }
        }
        else{
            //Como el tunel de salida 1 está ocupado en ese momento, la hormiga intentará salir por el tunel de salida 2
            salidaColonia2.lock();
            try{
                Thread.sleep(100);
                getLog().escribirEnLog("[COLONIA] --> La hormiga " + hormiga.getIdentificador() + " ha salido de la colonia por el tunel de salida 2");
            }
            catch (InterruptedException ignored) {}

            finally{
                salidaColonia2.unlock();
            }
        }
    }

    //Método auxiliar a la invasion
    public synchronized void actualizaEstadoInvasion(Hormiga hormiga){
        try{
            //Tenemos que ver en que zona está la hormiga, que pueden estar en ZonaComer, ZonaInstruccion o ZonaDescanso
            //ZonaComer
            if(getZonaComer().getListaHormigasZonaComer().getListaHormigas().contains(hormiga)){
                //Está en ZonaComer, por tanto deberán salir del almacen
                getZonaComer().sale(hormiga);
            }
            else if(getZonaInstruccion().getListaHormigasHaciendoInstruccion().getListaHormigas().contains(hormiga)){
                //Está haciendo instruccion, por tanto se va de ZonaInstruccion
                getZonaInstruccion().sale(hormiga);
            }
            else if(getZonaDescanso().getListaHormigasDescansando().getListaHormigas().contains(hormiga)){
                //Está haciendo un descanso, por tanto saldrán del descanso
                getZonaDescanso().sale(hormiga);
            }
        }
        catch(InterruptedException ignored){}
    }

    //Método para generar una invasion
    public void generaInvasion(){
        if (!getInvasion().isActiva() && !getRefugio().isActivo()){
            getInvasion().setActiva(true);
            getRefugio().setActivo(true);
            getLog().escribirEnLog("[INVASION] --> Se ha generado una invasion");
            //Una vez activada darmeos interrupt a todas las hormiga soldado y crias que esten presentes en la colonia
            for(int i = 0; i < getListaHormigas().size() ; i++){
                Hormiga hormigaActual = getListaHormigas().get(i);
                String tipoHormiga = hormigaActual.getTipo();
                if (tipoHormiga.equals("Soldada") || tipoHormiga.equals("Cria")){
                    hormigaActual.interrupt();
                }
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
    public ZonaInstruccion getZonaInstruccion(){
        return this.zonaInstruccion;
    }
    public ZonaDescanso getZonaDescanso(){
        return this.zonaDescanso;
    }
    public Refugio getRefugio(){
        return this.refugio;
    }
    public Invasion getInvasion(){
        return this.invasion;
    }
    public Paso getPaso() {
        return paso;
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

    public int getNumHormigasSoldado() {
        return numHormigasSoldado;
    }
    public void setNumHormigasSoldado(int numHormigasSoldado) {
        this.numHormigasSoldado = numHormigasSoldado;
    }

    public Lock getEntradaColonia() {
        return entradaColonia;
    }
}

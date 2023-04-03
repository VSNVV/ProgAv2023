package distribuida;

import concurrencia.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class GestorInterfaz extends UnicastRemoteObject implements InterfazColonia {
    //Métodos de la clase colonia
    private Colonia colonia;

    //Método constructor
    public GestorInterfaz(Colonia colonia) throws RemoteException {
        this.colonia = colonia;
    }

    //Método que devuelve el numero de hormigas obreras en el exterior de la colonia
    public String numHomrigasObrerasExteriorColonia() throws RemoteException {
        String resultado;
        int num;
        ArrayList<Hormiga> lista = getColonia().getListaHormigasLlevandoComida().getListaHormigas();
        num = lista.size();
        resultado = String.valueOf(num);
        return resultado;
    }

    //Método que devuelve el numero de hormigas obreras dentro de la colonia
    public String numHomrigasObrerasInteriorColonia() throws RemoteException {
        String resultado;
        int num = 0;
        ArrayList<Hormiga> lista = getColonia().getListaHormigas();
        for(int i = 0; i < lista.size(); i++){
            Hormiga hormigaActual = lista.get(i);
            String tipo = hormigaActual.getTipo();
            if(tipo.equals("Obrera")){
                num = num + 1;
            }
        }
        resultado = String.valueOf(num);
        return resultado;
    }

    //Método que devuelve el numero de hormigas soldado haciendo instruccion
    public String numHormigasSoldadoInstruccion() throws RemoteException {
        String resultado;
        int num;
        ZonaInstruccion zonaInstruccion = getColonia().getZonaInstruccion();
        ArrayList<Hormiga> lista = zonaInstruccion.getListaHormigasHaciendoInstruccion().getListaHormigas();
        num = lista.size();
        resultado = String.valueOf(num);
        return resultado;
    }

    //Método que devuelve el numero de hormigas luchando en la invasion
    public String numHormigasSoldadoInvasion() throws RemoteException {
        String resultado;
        int num;
        Invasion invasion = getColonia().getInvasion();
        ArrayList<Hormiga> lista = invasion.getListaHormigasInvasion().getListaHormigas();
        num = lista.size();
        resultado = String.valueOf(num);
        return resultado;
    }

    //Método que devuelve el número de hormigas cría en la zona para comer
    public String numHormigasCriaZonaComer() throws RemoteException {
        String resultado;
        int num = 0;
        ZonaComer zonaComer = getColonia().getZonaComer();
        ArrayList<Hormiga> lista = zonaComer.getListaHormigasZonaComer().getListaHormigas();
        for(int i = 0; i < lista.size(); i++){
            Hormiga hormigaActual = lista.get(i);
            String tipo = hormigaActual.getTipo();
            if(tipo.equals("Cria")){
                num = num + 1;
            }
        }
        resultado = String.valueOf(num);
        return resultado;
    }

    //Método que devuelve el número de hormigas cría en el refugio
    public String numHormigasCriaRefugio() throws RemoteException {
        String resultado;
        int num;
        Refugio refugio = getColonia().getRefugio();
        ArrayList<Hormiga> lista = refugio.getListaHormigasRefugio().getListaHormigas();
        num = lista.size();
        resultado = String.valueOf(num);
        return resultado;
    }

    //Método que genera una invasión
    public void generaInvasion() throws RemoteException {
        getColonia().generaInvasion();
    }

    //Método get de la colonia
    public Colonia getColonia(){
        return this.colonia;
    }
}
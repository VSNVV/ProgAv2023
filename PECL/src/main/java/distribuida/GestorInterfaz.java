package distribuida;

import concurrencia.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class GestorInterfaz extends UnicastRemoteObject implements InterfazColonia {
    //Métodos de la clase colonia
    private final Colonia colonia;

    //Método constructor
    public GestorInterfaz(Colonia colonia) throws RemoteException {
        this.colonia = colonia;
    }

    //Método que devuelve el numero de hormigas obreras en el exterior de la colonia
    public String numHormigasObrerasExteriorColonia() throws RemoteException {
        ArrayList<Hormiga> lista = getColonia().getListaHormigasBuscandoComida().getListaHormigas();
        return cuentaHormigasArraylist(lista);
    }

    //Método que devuelve el numero de hormigas obreras dentro de la colonia
    public String numHormigasObrerasInteriorColonia() throws RemoteException {
        ArrayList<Hormiga> lista = getColonia().getListaHormigas();
        return cuentaHormigasTipoArraylist(lista, "Obrera");
    }

    //Método que devuelve el numero de hormigas soldado haciendo instruccion
    public String numHormigasSoldadoInstruccion() throws RemoteException {
        ZonaInstruccion zonaInstruccion = getColonia().getZonaInstruccion();
        ArrayList<Hormiga> lista = zonaInstruccion.getListaHormigasHaciendoInstruccion().getListaHormigas();
        return cuentaHormigasArraylist(lista);
    }

    //Método que devuelve el numero de hormigas luchando en la invasion
    public String numHormigasSoldadoInvasion() throws RemoteException {
        Invasion invasion = getColonia().getInvasion();
        ArrayList<Hormiga> lista = invasion.getListaHormigasInvasion().getListaHormigas();
        return cuentaHormigasArraylist(lista);
    }

    //Método que devuelve el número de hormigas cría en la zona para comer
    public String numHormigasCriaZonaComer() throws RemoteException {
        ZonaComer zonaComer = getColonia().getZonaComer();
        ArrayList<Hormiga> lista = zonaComer.getListaHormigasZonaComer().getListaHormigas();
        return cuentaHormigasTipoArraylist(lista, "Cria");
    }

    //Método que devuelve el número de hormigas cría en el refugio
    public String numHormigasCriaRefugio() throws RemoteException {
        Refugio refugio = getColonia().getRefugio();
        ArrayList<Hormiga> lista = refugio.getListaHormigasRefugio().getListaHormigas();
        return cuentaHormigasArraylist(lista);
    }

    //Método que genera una invasión
    public void generaInvasion() throws RemoteException {
        getColonia().generaInvasion();
    }

    //Método get de la colonia
    public Colonia getColonia(){
        return this.colonia;
    }

    private String cuentaHormigasArraylist(ArrayList<Hormiga> lista){
        String resultado;
        int num;
        num = lista.size();
        resultado = String.valueOf(num);
        return resultado;
    }

    private String cuentaHormigasTipoArraylist(ArrayList<Hormiga> lista, String tipo){
        int num = 0;
        for (Hormiga hormigaActual : lista) {
            if (hormigaActual.getTipo().equals(tipo)) {
                num = num + 1;
            }
        }
        return String.valueOf(num);
    }
}
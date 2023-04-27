package distribuida;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazColonia extends Remote {
    String numHormigasObrerasExteriorColonia() throws RemoteException;
    String numHormigasObrerasInteriorColonia() throws RemoteException;
    String numHormigasSoldadoInstruccion() throws RemoteException;
    String numHormigasSoldadoInvasion() throws RemoteException;
    String numHormigasCriaZonaComer() throws RemoteException;
    String numHormigasCriaRefugio() throws RemoteException;
    void generaInvasion() throws RemoteException;
    
}

package concurrencia;
import java.util.*;
import javax.swing.JTextField;

/* La clase ListaThreads permite gestionar las listas de threads en los monitores,
con métodos para meter y sacar threads en ella. Cada vez que una lista se modifica,
se imprime su nuevo contenido en el JTextField que toma como parámetro el constructor. */
public class ListaThreads{
    private ArrayList<Hormiga> listaHormigas;
    private JTextField tf;
    
    public ListaThreads(JTextField tf){
        listaHormigas = new ArrayList<Hormiga>();
        this.tf = tf;
    }
    
    public synchronized void meterHormiga(Hormiga hormiga){
        listaHormigas.add(hormiga);
        imprimirHormiga();
    }
    
    public synchronized void sacarHormiga(Hormiga hormiga){
        listaHormigas.remove(hormiga);
        imprimirHormiga();
    }

    public synchronized void insertarNumero(Integer num){
        String texto = String.valueOf(num);
        tf.setText(texto);
    }

    private void imprimirHormiga() {
        String contenido = "";
        for(int i=0; i<listaHormigas.size(); i++)
        {
            contenido = contenido + listaHormigas.get(i).getIdentificador()+" ";
        }
        tf.setText(contenido);
    }

    private void imprimirNumero(){

    }
}
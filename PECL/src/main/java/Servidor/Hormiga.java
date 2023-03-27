package Servidor;

public class Hormiga extends Thread{
    //Atributos de la clase Hormiga
    private String identificador = "";
    private String tipo = "";



    //Métodos de la clase Hormiga

    //Método constructor
    public Hormiga(String indentificador, String tipo){
        this.identificador = indentificador;
        this.tipo = tipo;
    }
}

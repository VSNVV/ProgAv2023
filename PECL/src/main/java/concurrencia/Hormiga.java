package concurrencia;

public class Hormiga extends Thread{
    //Atributos de la clase Hormiga
    private final String identificador;
    private int numIteraciones = 0;
    private final String tipo;
    private final Colonia colonia;
    //Zonas de la colonia (escritas para codigo más legible)
    private final AlmacenComida almacenComida;
    private final ZonaComer zonaComer;
    private final ZonaInstruccion zonaInstruccion;
    private final ZonaDescanso zonaDescanso;
    private final Refugio refugio;
    private final Paso paso;

    //Métodos de la clase Hormiga

    //Método constructor
    public Hormiga(Colonia colonia,  String identificador){
        this.colonia = colonia;
        this.identificador = identificador;
        //El tipo dependerá del ID
        if (identificador.indexOf("O") == 1){
            //La segunda letra es una O de obrera, por tanto esta hormiga será obrera
            this.tipo = "Obrera";
        }
        else if (identificador.indexOf("S") == 1){
            //La segunda letra es una S de soldado, por tanto esta hormiga será soldado
            this.tipo = "Soldada";
        }
        else{
            //Si no es ni obrera ni soldada, tiene que ser cria
            this.tipo = "Cria";
        }
        this.almacenComida = colonia.getAlmacenComida();
        this.zonaComer = colonia.getZonaComer();
        this.zonaInstruccion = colonia.getZonaInstruccion();
        this.zonaDescanso = colonia.getZonaDescanso();
        this.refugio = colonia.getRefugio();
        this.paso = colonia.getPaso();
    }

    //Métodos get y set
    public String getIdentificador(){
        return this.identificador;
    }
    public int getNumIteraciones(){
        return this.numIteraciones;
    }
    public void setNumIteraciones(int numIteraciones){
        this.numIteraciones = numIteraciones;
    }
    public String getTipo(){
        return this.tipo;
    }
    public Colonia getColonia(){
        return this.colonia;
    }

    public AlmacenComida getAlmacenComida() {
        return almacenComida;
    }

    public ZonaComer getZonaComer() {
        return zonaComer;
    }

    public ZonaInstruccion getZonaInstruccion() {
        return zonaInstruccion;
    }

    public ZonaDescanso getZonaDescanso() {
        return zonaDescanso;
    }

    public Refugio getRefugio() {
        return refugio;
    }

    public Paso getPaso() {
        return paso;
    }
}

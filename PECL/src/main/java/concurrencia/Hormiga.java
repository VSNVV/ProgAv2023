package concurrencia;

public class Hormiga extends Thread{
    //Atributos de la clase Hormiga
    private String identificador;
    private int numIdentificador, numIteraciones = 0;
    private String tipo;
    private Colonia colonia;
    private Log log;
    //Zonas de la clonia (escritas para codigo más legible)
    private AlmacenComida almacenComida;
    private ZonaComer zonaComer;
    private ZonaInstruccion zonaInstruccion;
    private ZonaDescanso zonaDescanso;
    private Invasion invasion;
    private Refugio refugio;
    private Paso paso;

    //Métodos de la clase Hormiga

    //Método constructor
    public Hormiga(Colonia colonia, Log log,  String identificador, int numIdentificador){
        this.colonia = colonia;
        this.identificador = identificador;
        this.numIdentificador = numIdentificador;
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
        this.invasion = colonia.getInvasion();
        this.refugio = colonia.getRefugio();
        this.paso = colonia.getPaso();
    }

    //Método run
    public void run(){
        //Todas las hormigas, sin distinción, tienen que entrar a la colonia
        //Según el tipo de hormiga, esta tendrá una rutina u otra
        if (this.getTipo().equals("Obrera")){
            try{
                getPaso().mirar(); //Instruccion para que los hilos revisen si deben quedar parados o no, y recordar donde se quedaron parados
                getColonia().entraColonia(this); //La hormiga entra a la colonia, sea del tipo que sea
                while(true){
                    //Verificamos que la hormiga es de tipo obrera
                    //Las obreras con ID par tienen distinta rutina que las impares
                    if ((getNumIdentificador() % 2) != 0){ //HORMIGA OBRERA IMPAR SALEN FUERA A POR COMIDA
                        getPaso().mirar();
                        getColonia().saleColonia(this);
                        getPaso().mirar();//Sale de la colonia
                        getColonia().getListaHormigasBuscandoComida().meterHormiga(this); //Desde este momento está buscando comida
                        Thread.sleep(4000); //Tarda 4 segundos en recogerlo
                        getPaso().mirar();
                        getColonia().getListaHormigasBuscandoComida().sacarHormiga(this); //Ya no esta buscando comida
                        getPaso().mirar();
                        getColonia().entraColonia(this); //Vuelve a entrar a la colonia
                        getPaso().mirar();
                        getAlmacenComida().entra(this); //Entra al almacen de comida
                        Thread.sleep((int) (((Math.random() + 1) * 2000) + (4000 - (2000 * 2)))); //Tarda entre 2 y 4 segundos en depositar
                        getPaso().mirar();
                        getAlmacenComida().depositaElementoComida(this); //Deposita el elemento de comida
                        getPaso().mirar();
                        getAlmacenComida().sale(this); //Sale del almacén de comida
                        setNumIteraciones(getNumIteraciones() + 1); //Subimos el numero de interrupciones (local a cada hilo)
                    }
                    else{ //HORMIGA OBRERA PAR
                        //Verificamos que la hormiga obrera tiene un identificador par RECOGEN COMIDA
                        getAlmacenComida().entra(this); //Entra en el almacen de comida
                        Thread.sleep((int) (((Math.random() + 1) * 1000) + (2000 - (1000 * 2)))); //Tarda entre 1 y 2 segundos en recoger un elemento de comida
                        getPaso().mirar();
                        getAlmacenComida().recogeElementoComida(this); //Recoge un elemento de comida
                        getPaso().mirar();
                        getAlmacenComida().sale(this); //Sale del almacen de comida
                        getPaso().mirar();
                        getColonia().getListaHormigasLlevandoComida().meterHormiga(this); //Desde este momento esta llevando comida a la zona para comer
                        Thread.sleep((int) (((Math.random() + 1) * 1000) + (3000 - (1000 * 2)))); //Tarda entre 1 y 3 segundos en viajar a la zona para comer
                        getPaso().mirar();
                        getColonia().getListaHormigasLlevandoComida().sacarHormiga(this);//Desde que va a entrar a la zona para comer no esta llevando comida
                        getPaso().mirar();
                        getZonaComer().entra(this); //Entra a la zona de comer
                        Thread.sleep((int) (((Math.random() + 1) * 1000) + (2000 - (1000 * 2)))); //Tarda entre 1 y 2 segundos en depositar un elemento de comida
                        getPaso().mirar();
                        getZonaComer().depositaElementoComida(this); //Deposita un elemento de comida en la zona para comer
                        getPaso().mirar();
                        getZonaComer().sale(this);
                        getPaso().mirar();
                    }
                    if (getNumIteraciones() >= 10){
                        //Primero reiniciaremos las iteraciones a 0
                        setNumIteraciones(0);
                        //Una vez reiniciadas, entramos a la zona para comer
                        getPaso().mirar();
                        getZonaComer().entra(this);
                        getPaso().mirar();
                        //Una vez dentro cogera un elemento de comida
                        getZonaComer().cogeElementoComida(this);
                        getPaso().mirar();
                        Thread.sleep(3000);
                        getPaso().mirar();//Tarda 3 segundos en comer
                        //Una vez que ha comido saldrá de la zona de comer
                        getZonaComer().sale(this);
                        getPaso().mirar();
                        //Una vez fuera, entra a la zona de descanso
                        getZonaDescanso().entra(this);
                        getPaso().mirar();
                        //Una vez dentro de la zona de descanso, realiza el descanso
                        getZonaDescanso().realizaDescanso(this);
                        getPaso().mirar();
                        //Sale de la zona de descanso
                        getColonia().getZonaDescanso().sale(this);
                        getPaso().mirar();
                    }
                }
            }catch(InterruptedException ie){}
        }
        else if (this.getTipo() == "Soldada"){
            try{
                getPaso().mirar(); //Instruccion para que los hilos revisen si deben quedar parados o no, y recordar donde se quedaron parados
                getColonia().entraColonia(this); //La hormiga entra a la colonia, sea del tipo que sea
            }catch(InterruptedException ignored){}
            while(true){
                try{
                    while(true){
                        //Verificamos que la hormiga es de tipo soldada
                        //Una vez dentro de la colonia, se irán a la zona de instrucción
                        getPaso().mirar();
                        getZonaInstruccion().entraZonaInstruccion(this);
                        getPaso().mirar();
                        //Una vez dentro de la zona de instruccion, hacen entre 2 y 8 segundos de instruccion
                        getZonaInstruccion().realizaInstruccion(this);
                        getPaso().mirar();
                        //Una vez que haya hecho la instrucción, saldrá de la zona de instrucción
                        getZonaInstruccion().saleZonaInstruccion(this);
                        getPaso().mirar();
                        //Una vez fuera de la zona de instrucción, se irá a a zona de descanso
                        getZonaDescanso().entra(this);
                        getPaso().mirar();
                        //Una vez dentro de la zona de descanso, procede a descansar
                        getZonaDescanso().realizaDescanso(this);
                        getPaso().mirar();
                        //Una vez que haya realizado el descanso, abandonará la zona de descanso
                        getZonaDescanso().sale(this);
                        getPaso().mirar();
                        //Como se ha completado una iteración, incrementamos en 1 el numero
                        setNumIteraciones(getNumIteraciones() + 1);
                        if (getNumIteraciones() >= 6){
                            //En primer lugar, reiniciaremos el numero de iteraciones a 0
                            setNumIteraciones(0);
                            getPaso().mirar();
                            //Cuando hayan hecho 6 iteraciones, se pasaran por la zona para comer, tardan en comer 3 segundos
                            getZonaComer().entra(this);
                            getPaso().mirar();
                            //Una vez dentro de la zona de comer, nos ponemos a comer
                            getZonaComer().cogeElementoComida(this);
                            Thread.sleep(3000);
                            getPaso().mirar();
                            //Cuando haya comido se va de la zona para comer y repite de nuevo
                            getColonia().getZonaComer().sale(this);
                        }
                    }
                }catch(InterruptedException ie){
                    getColonia().actualizaEstadoInvasion(this);
                    try{
                        getPaso().mirar();
                    }catch(InterruptedException exception){}
                    //Código de la invasion
                    getColonia().getInvasion().realizaInvasion(this);
                }
            }

        }
        else if (this.getTipo() == "Cria"){
            try{
                getPaso().mirar(); //Instruccion para que los hilos revisen si deben quedar parados o no, y recordar donde se quedaron parados
                getColonia().entraColonia(this); //La hormiga entra a la colonia, sea del tipo que sea
                while(true){
                    getPaso().mirar(); //Instruccion para que los hilos revisen si deben quedar parados o no, y recordar donde se quedaron parados
                    //Verificamos que la hormiga es de tipo cría
                    //UNa vez dentro de la colonia, van a la zona de comer
                    getColonia().getZonaComer().entra(this);
                    getPaso().mirar();
                    //Una vez dentro de la zona de comer, cogen un alimento
                    getColonia().getZonaComer().cogeElementoComida(this);
                    Thread.sleep((int) (((Math.random() + 1) * 3000) + (5000 - (3000)))); //Tarda entre 3 y 5 en comer
                    getPaso().mirar();
                    //Una vez que haya comido, sale de la zona de comer
                    getColonia().getZonaComer().sale(this);
                    getPaso().mirar();
                    //Una vez que haya salido de la zona para comer, entran a la zona de descanso
                    getColonia().getZonaDescanso().entra(this);
                    //Una vez dentro de la zona de descanso, descansan 4 segundos
                    getColonia().getZonaDescanso().realizaDescanso(this);
                    getPaso().mirar();
                    //Una vez haya realizado el descanso, sale de la zona de descanso
                    getColonia().getZonaDescanso().sale(this);
                }
            }catch(InterruptedException ie){
                try{
                    getPaso().mirar();
                }catch(InterruptedException exception){}
                //Código de refugio (ya que son crias)
                getColonia().actualizaEstadoInvasion(this);
                //Se protegen en el refugio
                try{
                    getPaso().mirar();
                }catch(InterruptedException exception){}
                getColonia().getRefugio().protegeRefugio(this);
            }
        }
    }

    //Métodos get y set
    public String getIdentificador(){
        return this.identificador;
    }
    public int getNumIdentificador(){
        return this.numIdentificador;
    }
    public int getNumIteraciones(){
        return this.numIteraciones;
    }
    public void setNumIteraciones(int numIteraciones){
        this.numIdentificador = numIteraciones;
    }
    public String getTipo(){
        return this.tipo;
    }
    public Colonia getColonia(){
        return this.colonia;
    }
    public Log getLog(){
        return this.log;
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

    public Invasion getInvasion() {
        return invasion;
    }

    public Refugio getRefugio() {
        return refugio;
    }

    public Paso getPaso() {
        return paso;
    }
}

package concurrencia;

public class Hormiga extends Thread{
    //Atributos de la clase Hormiga
    private String identificador;
    private int numIdentificador, numIteraciones = 0;
    private String tipo;
    private Colonia colonia;
    private Log log;

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
    }

    //Método run
    public void run(){
        getColonia().compruebaInvasion(this);
        //Todas las hormigas, sin distinción, tienen que entrar a la colonia
        getColonia().entraColonia(this);
        //Según el tipo de hormiga, esta tendrá una rutina u otra
        if (this.getTipo() == "Obrera"){
            while (true){
                try{
                    //Verificamos que la hormiga es de tipo obrera
                    //Las obreras con ID par tienen distinta rutina que las impares
                    if ((getNumIdentificador() % 2) != 0){ //HORMIGA OBRERA IMPAR SALEN FUERA A POR COMIDA
                        getColonia().saleColonia(this); //Sale de la colonia
                        getColonia().getListaHormigasBuscandoComida().meterHormiga(this); //Desde este momento está buscando comida
                        Thread.sleep(4000); //Tarda 4 segundos en recogerlo
                        getColonia().getListaHormigasBuscandoComida().sacarHormiga(this); //Ya no esta buscando comida
                        getColonia().entraColonia(this); //Vuelve a entrar a la colonia
                        getColonia().getAlmacenComida().entra(this); //Entra al almacen de comida
                        Thread.sleep((int) (((Math.random() + 1) * 2000) + (4000 - (2000 * 2)))); //Tarda entre 2 y 4 segundos en depositar
                        getColonia().getAlmacenComida().depositaElementoComida(this); //Deposita el elemento de comida
                        getColonia().getAlmacenComida().sale(this); //Sale del almacén de comida
                        setNumIteraciones(getNumIteraciones() + 1); //Subimos el numero de interrupciones (local a cada hilo)
                    }
                    else{ //HORMIGA OBRERA PAR
                        //Verificamos que la hormiga obrera tiene un identificador par RECOGEN COMIDA
                        getColonia().getAlmacenComida().entra(this); //Entra en el almacen de comida
                        Thread.sleep((int) (((Math.random() + 1) * 1000) + (2000 - (1000 * 2)))); //Tarda entre 1 y 2 segundos en recoger un elemento de comida
                        getColonia().getAlmacenComida().recogeElementoComida(this); //Recoge un elemento de comida
                        getColonia().getAlmacenComida().sale(this); //Sale del almacen de comida
                        getColonia().getListaHormigasLlevandoComida().meterHormiga(this); //Desde este momento esta llevando comida a la zona para comer
                        Thread.sleep((int) (((Math.random() + 1) * 1000) + (3000 - (1000 * 2)))); //Tarda entre 1 y 3 segundos en viajar a la zona para comer
                        getColonia().getListaHormigasLlevandoComida().sacarHormiga(this); //Desde que va a entrar a la zona para comer no esta llevando comida
                        getColonia().getZonaComer().entra(this); //Entra a la zona de comer
                        Thread.sleep((int) (((Math.random() + 1) * 1000) + (2000 - (1000 * 2)))); //Tarda entre 1 y 2 segundos en depositar un elemento de comida
                        getColonia().getZonaComer().depositaElementoComida(this); //Deposita un elemento de comida en la zona para comer
                        getColonia().getZonaComer().sale(this);
                    }
                    if (getNumIteraciones() >= 10){
                        //Primero reiniciaremos las iteraciones a 0
                        setNumIteraciones(0);
                        //Una vez reiniciadas, entramos a la zona para comer
                        getColonia().getZonaComer().entra(this);
                        //Una vez dentro cogera un elemento de comida
                        getColonia().getZonaComer().cogeElementoComida(this);
                        Thread.sleep(3000);
                        //Una vez que ha comido saldrá de la zona de comer
                        getColonia().getZonaComer().sale(this);
                        //Una vez fuera, entra a la zona de descanso
                        getColonia().getZonaDescanso().entraZonaDescanso(this);
                        //Una vez dentro de la zona de descanso, realiza el descanso
                        getColonia().getZonaDescanso().realizaDescanso(this);
                        //Sale de la zona de descanso
                        getColonia().getZonaDescanso().saleZonaDescanso(this);
                    }
                }catch(InterruptedException ignored){}
            }
        }
        else if (this.getTipo() == "Soldada"){
            while (true){
                try{
                    //Verificamos que la hormiga es de tipo soldada
                    //Una vez dentro de la colonia, se irán a la zona de instrucción
                    getColonia().getZonaInstruccion().entraZonaInstruccion(this);
                    //Una vez dentro de la zona de instruccion, hacen entre 2 y 8 segundos de instruccion
                    getColonia().getZonaInstruccion().realizaInstruccion(this);
                    //Una vez que haya hecho la instrucción, saldrá de la zona de instrucción
                    getColonia().getZonaInstruccion().saleZonaInstruccion(this);
                    //Una vez fuera de la zona de instrucción, se irá a a zona de descanso
                    getColonia().getZonaDescanso().entraZonaDescanso(this);
                    //Una vez dentro de la zona de descanso, procede a descansar
                    getColonia().getZonaDescanso().realizaDescanso(this);
                    //Una vez que haya realizado el descanso, abandonará la zona de descanso
                    getColonia().getZonaDescanso().saleZonaDescanso(this);
                    //Como se ha completado una iteración, incrementamos en 1 el numero
                    setNumIteraciones(getNumIteraciones() + 1);
                    if (getNumIteraciones() >= 6){
                        //En primer lugar, reiniciaremos el numero de iteraciones a 0
                        setNumIteraciones(0);
                        //Cuando hayan hecho 6 iteraciones, se pasaran por la zona para comer, tardan en comer 3 segundos
                        getColonia().getZonaComer().entra(this);
                        //Una vez dentro de la zona de comer, nos ponemos a comer
                        getColonia().getZonaComer().cogeElementoComida(this);
                        Thread.sleep(3000);
                        //Cuando haya comido se va de la zona para comer y repite de nuevo
                        getColonia().getZonaComer().sale(this);
                    }
                }catch(InterruptedException ie){
                    //Código de la invasion
                    getColonia().invasion(this);
                }
            }
        }
        else if (this.getTipo() == "Cria"){
            while (true){
                try{
                    //Verificamos que la hormiga es de tipo cría
                    //UNa vez dentro de la colonia, van a la zona de comer
                    getColonia().getZonaComer().entra(this);
                    //Una vez dentro de la zona de comer, cogen un alimento
                    getColonia().getZonaComer().cogeElementoComida(this);
                    Thread.sleep((int) (((Math.random() + 1) * 3000) + (5000 - (3000)))); //Tarda entre 3 y 5 en comer
                    //Una vez que haya comido, sale de la zona de comer
                    getColonia().getZonaComer().sale(this);
                    //Una vez que haya salido de la zona para comer, entran a la zona de descanso
                    getColonia().getZonaDescanso().entraZonaDescanso(this);
                    //Una vez dentro de la zona de descanso, descansan 4 segundos
                    getColonia().getZonaDescanso().realizaDescanso(this);
                    //Una vez haya realizado el descanso, sale de la zona de descanso
                    getColonia().getZonaDescanso().saleZonaDescanso(this);
                }
                catch(InterruptedException ie){
                    //Código de refugio (ya que son crias)
                    getColonia().actualizaEstadoInvasion(this);
                    //Entran al refugio
                    getColonia().getRefugio().entraRefugio(this);
                    //Una vez dentro del refugio espera al fin de la invasion
                    getColonia().getRefugio().esperaFinInvasion(this);
                    //Una vez que la invasion haya terminado, podrá salir del refugio, empezando de 0 su actividad
                    getColonia().getRefugio().saleRefugio(this);
                }
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
}

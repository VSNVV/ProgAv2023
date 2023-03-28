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
        //Todas las hormigas, sin distinción, tienen que entrar a la colonia
        getColonia().entraColonia(this);
        //Según el tipo de hormiga, esta tendrá una rutina u otra
        if (this.getTipo() == "Obrera"){
            //Verificamos que la hormiga es de tipo obrera
            //Las obreras con ID par tienen distinta rutina que las impares
            if ((getNumIdentificador() % 2) == 0){
                //Verificamos que la hormiga obrera tiene un identificador par
                while (true){
                    //La hormiga entra al almacén de comida
                    getColonia().getAlmacenComida().entraAlmacen(this);
                    //Una vez dentro del almacén recoge un elemento de comida
                    getColonia().getAlmacenComida().recogeElementoComida(this);
                    //Una vez que ha cogido el elemento de comida, saldra del almacen
                    getColonia().getAlmacenComida().saleAlmacen(this);
                    //Una vez que haya salido del almacen, esta se meterá a la zona para comer, tarda entre 1 y 3 segundos
                    try {
                        Thread.sleep((int) (Math.random() * 1000 + 2000));
                    } catch (InterruptedException e) {}
                    //Una vez transcurrido el tiempo, entramos a la zona para comer
                    getColonia().getZonaComer().entraZonaComer(this);
                    //Una vez dentro de la zona para comer, depositamos el elemento de comida
                    getColonia().getZonaComer().depositaElementoComida(this);
                    //Una vez depositado el elemento de comida, abandonaremos la zona para comer
                    getColonia().getZonaComer().saleZonaComer(this);

                }

            }
            else{
                while (true){
                    //Verificamos que la hormiga obrera tiene un identificador impar
                    //La hormiga sale al exterior en busca de un elemento de comida
                    getColonia().saleColonia(this);
                    //Tarda 4 segundos en coger el elemento de comida
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {}
                    //Una vez que hayan pasado los 4 segundos entrará a la colonia
                    getColonia().entraColonia(this);
                    //Una vez esté dentro de la colonia deposita el elemento de comida en el almacen de comida
                    getColonia().getAlmacenComida().entraAlmacen(this);
                    getColonia().getAlmacenComida().depositaElementoComida(this);
                    //Una vez depositado, sale del almacen de comida
                    getColonia().getAlmacenComida().saleAlmacen(this);
                    //FIN DE RUTINA
                }
            }
        }
        else if (this.getTipo() == "Soldada"){
            while (true){
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
                if (getNumIteraciones() == 6){
                    //Cuando hayan hecho 6 iteraciones, se pasaran por la zona para comer, tardan en comer 3 segundos
                    getColonia().getZonaComer().entraZonaComer(this);
                    //Una vez dentro de la zona de comer, nos ponemos a comer
                    getColonia().getZonaComer().come(this);
                    //Cuando haya comido se va de la zona para comer y repite de nuevo
                    getColonia().getZonaComer().saleZonaComer(this);
                    //Como ya hemos comido, ahora las iteraciones se reiniciarán
                    setNumIteraciones(0);
                }
            }
        }
        else if (this.getTipo() == "Cria"){
            //Verificamos que la hormiga es de tipo cría
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

package concurrencia;

public class HormigaObrera extends Hormiga{
    //Atributos de la clae HormigaObrera
    private final int numIdentificador;


    public HormigaObrera(Colonia colonia, String identificador, int numIdentificador) {
        super(colonia, identificador);
        this.numIdentificador = numIdentificador;
    }

    public void run(){
        try{
            getPaso().mirar(); //Instruccion para que los hilos revisen si deben quedar parados o no, y recordar donde se quedaron parados
            getColonia().entraColonia(this); //La hormiga entra a la colonia, sea del tipo que sea
            while(true){
                //Verificamos que la hormiga es de tipo obrera
                //Las obreras con ID par tienen distinta rutina que las impares
                if ((getNumIdentificador() % 2) != 0){ //HORMIGA OBRERA IMPAR SALEN FUERA A POR COMIDA
                    rutinaHormigaImpar();
                }
                else{ //HORMIGA OBRERA PAR
                    try{
                        rutinaHormigaPar();
                    }catch(InterruptedException ignored){}
                }
                setNumIteraciones(getNumIteraciones() + 1); //Subimos el numero de interrupciones (local a cada hilo)
                if (getNumIteraciones() >= 10){
                    setNumIteraciones(0); //Reiniciamos las iteraciones
                    rutinaDescanso();
                }
            }
        }catch(InterruptedException ignored){}
    }

    private void rutinaHormigaPar() throws InterruptedException{
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

    private void rutinaHormigaImpar() throws InterruptedException{
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
    }

    private void rutinaDescanso() throws InterruptedException{
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

    private int getNumIdentificador(){
        return this.numIdentificador;
    }
}

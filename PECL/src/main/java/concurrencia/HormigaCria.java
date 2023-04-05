package concurrencia;

public class HormigaCria extends Hormiga{
    //Atributos de la clase HormigaCria

    //Métodos de la clase HormigaCria

    //Método constructor
    public HormigaCria(Colonia colonia, String identificador) {
        super(colonia, identificador);
    }

    //Método run
    public void run(){
        try{
            getPaso().mirar(); //Instruccion para que los hilos revisen si deben quedar parados o no, y recordar donde se quedaron parados
            getColonia().entraColonia(this); //La hormiga entra a la colonia, sea del tipo que sea
            while(true){
                rutina();
            }
        }catch(InterruptedException ie){
            try{
                getPaso().mirar();
            }catch(InterruptedException ignored){}
            //Código de refugio (ya que son crias)
            getColonia().actualizaEstadoInvasion(this);
            //Se protegen en el refugio
            try{
                getPaso().mirar();
            }catch(InterruptedException ignored){}
            getColonia().getRefugio().protegeRefugio(this);
        }
    }

    //Método que realiza la rutina de una hormiga cria
    private void rutina() throws InterruptedException{
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
}

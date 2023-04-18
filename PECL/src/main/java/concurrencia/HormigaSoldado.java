package concurrencia;

public class HormigaSoldado extends Hormiga{
    //Atributos de la clase HormigaSoldado

    //Métodos de la clase HormigaSoldado
    public HormigaSoldado(Colonia colonia, String identificador) {
        super(colonia, identificador);
    }

    //Método run
    public void run(){
        try{
            getPaso().mirar(); //Instruccion para que los hilos revisen si deben quedar parados o no, y recordar donde se quedaron parados
            getColonia().entra(this); //La hormiga entra a la colonia, sea del tipo que sea
        }catch(InterruptedException ignored){}
        while(true){
            try{
                while(true){
                    rutinaHormigaSoldada();
                    setNumIteraciones(getNumIteraciones() + 1);
                    if (getNumIteraciones() >= 6){
                        rutinaDescanso();
                    }
                }
            }catch(InterruptedException ie){
                try{
                    getPaso().mirar();
                }catch(InterruptedException ignored){}
                getColonia().actualizaEstadoInvasion(this);
                try{
                    getPaso().mirar();
                }catch(InterruptedException ignored){}
                //Para realizar la invasión, tienen que salir de la colonia
                getColonia().sale(this);
                try{
                    getPaso().mirar();
                }catch(InterruptedException ignored){}
                //Código de la invasion
                getColonia().getInvasion().realizaInvasion(this);
                //Una vez acabada la invasión, tendrán que entrar de nuevo a la colonia
                try{
                    getPaso().mirar();
                }catch(InterruptedException ignored){}
                getColonia().entra(this);
            }
        }
    }

    private void rutinaHormigaSoldada() throws InterruptedException{
        //Verificamos que la hormiga es de tipo soldada
        //Una vez dentro de la colonia, se irán a la zona de instrucción
        getPaso().mirar();
        getZonaInstruccion().entra(this);
        getPaso().mirar();
        //Una vez dentro de la zona de instruccion, hacen entre 2 y 8 segundos de instruccion
        getZonaInstruccion().realizaInstruccion(this);
        getPaso().mirar();
        //Una vez que haya hecho la instrucción, saldrá de la zona de instrucción
        getZonaInstruccion().sale(this);
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
    }

    private void rutinaDescanso() throws InterruptedException{
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

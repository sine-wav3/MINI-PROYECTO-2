import java.util.Random;

public class AcesCoup extends Magica {
    public AcesCoup() {
        super("Aces Coup");
    }

    @Override
    public void activar(Jugador jugador, Jugador oponente) {
        Random rand = new Random();
        int ceroAuno = rand.nextInt(2);
        System.out.println("Se lanzo la moneda");
        if(ceroAuno == 1){
            System.out.println("se activo Aces Coup, roba 2 cartas");
            jugador.robarCarta();
            jugador.robarCarta();
        }else{
            if(ceroAuno == 0){
                System.out.println("se activo Aces Coup, roba 2 cartas");
                oponente.robarCarta();
                oponente.robarCarta();
        }
    }
    }
}

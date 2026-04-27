package modelo.carta;
import modelo.juego.Jugador;

public class ChangeOfHeart extends Magica {
    public ChangeOfHeart(){ 
        super("Change of Heart"); 
    }

    @Override
    public void activar(Jugador jugador, Jugador oponente) {
        // No hace nada sin índice — usar activar(jugador, oponente, idx)
    }

    public void activar(Jugador jugador, Jugador oponente, int idx) {
        if (oponente.getCampo().isEmpty()) return;
        Monstruo m = oponente.getCampo().remove(idx);
        jugador.getCampo().add(m);
    }
}
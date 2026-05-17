package modelo.carta;

import modelo.juego.Jugador;

public class ChangeOfHeart extends Magica {

    public ChangeOfHeart() {
        super("Change of Heart");
    }

    @Override
    public void activar(Jugador jugador, Jugador oponente) {
        if (oponente.getCampo().isEmpty()) return;

        Monstruo m = oponente.getCampo().remove(0);
        jugador.getCampo().add(m);
    }
}
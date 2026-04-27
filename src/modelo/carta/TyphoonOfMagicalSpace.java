package modelo.carta;
import modelo.juego.Jugador;

public class TyphoonOfMagicalSpace extends Magica {
    public TyphoonOfMagicalSpace() {
        super("Typhoon Of Magical Space");
    }

    @Override
    public void activar(Jugador jugador, Jugador oponente) {
        // No hace nada sin índice — usar activar(jugador, oponente, idx)
    }

    public void activar(Jugador jugador, Jugador oponente, int idx) {
        if (oponente.getCampo().isEmpty()) return;
        Monstruo eliminado = oponente.getCampo().remove(idx);
    }
}
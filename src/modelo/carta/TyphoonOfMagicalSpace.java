package modelo.carta;

import modelo.juego.Jugador;

public class TyphoonOfMagicalSpace extends Magica {

    public TyphoonOfMagicalSpace() {
        super("Typhoon Of Magical Space");
    }

    @Override
    public void activar(Jugador jugador, Jugador oponente) {

        if (oponente.getCampo().isEmpty()) {
            return;
        }

        Monstruo eliminado = oponente.getCampo().remove(0);

        System.out.println("🌪️ Destruiste " + eliminado.getNombre());
    }
}
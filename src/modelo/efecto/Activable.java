package modelo.efecto;
import modelo.juego.Jugador;

public interface Activable {
    void activar(Jugador jugador, Jugador oponente);
}

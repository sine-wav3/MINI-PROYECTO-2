package modelo.carta;
import modelo.efecto.Activable;
import modelo.juego.Jugador;

public class TrampaGenerica extends Carta implements Activable {
    
    public TrampaGenerica(String nombre) {
        super(nombre);
    }
    
    @Override
    public void activar(Jugador jugador, Jugador oponente) {
        System.out.println("Trampa activada: " + nombre);
        // efecto simple que daña al oponente
        oponente.recibirDano(300);
    }
}
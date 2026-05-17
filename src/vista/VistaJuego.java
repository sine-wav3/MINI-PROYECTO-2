package vista;

import modelo.juego.Jugador;

public interface VistaJuego {
    void actualizar(Jugador j1, Jugador j2, boolean turnoJ1);
    void mostrarMensaje(String mensaje);
    void mostrarGanador(String nombreGanador);
    int pedirEleccion(String titulo, String[] opciones);
}
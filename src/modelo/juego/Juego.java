package modelo.juego;

import modelo.carta.*;

public class Juego {
    private Jugador j1, j2;
    private boolean turnoJ1;
    private boolean cartaJugadaEsteTurno;

    public Juego(Jugador j1, Jugador j2, boolean turnoJ1) {
        this.j1 = j1;
        this.j2 = j2;
        this.turnoJ1 = turnoJ1;
        this.cartaJugadaEsteTurno = false;
    }

    public Jugador getJugadorActual() {
        return turnoJ1 ? j1 : j2;
    }

    public Jugador getOponente() {
        return turnoJ1 ? j2 : j1;
    }

    public void pasarTurno() {
        turnoJ1 = !turnoJ1;
        cartaJugadaEsteTurno = false;
        for (Monstruo m : getJugadorActual().getCampo()) {
            m.resetCambio();
        }
    }

    public boolean isCartaJugadaEsteTurno() {
        return cartaJugadaEsteTurno;
    }

    public void setCartaJugadaEsteTurno(boolean valor) {
        cartaJugadaEsteTurno = valor;
    }

    public boolean hayGanador() {
        return j1.getLp() <= 0 || j2.getLp() <= 0;
    }

    public Jugador getGanador() {
        if (j1.getLp() <= 0) return j2;
        if (j2.getLp() <= 0) return j1;
        return null;
    }

    public Jugador getJ1() { return j1; }
    public Jugador getJ2() { return j2; }
}
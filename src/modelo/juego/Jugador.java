package modelo.juego;

import java.util.*;
import modelo.carta.Carta;
import modelo.carta.Monstruo;

public class Jugador {
    private String nombre;
    private int lp = 8000;
    private List<Carta> mano = new ArrayList<>();
    private List<Monstruo> campo = new ArrayList<>();
    private Stack<Carta> mazo;

    public Jugador(String nombre, Stack<Carta> mazo) {
        this.nombre = nombre;
        this.mazo = mazo;
    }

    public void robarCarta() {
        if (mazo.isEmpty()) return;
        mano.add(mazo.pop());
    }

    public void recibirDano(int dano) {
        lp -= dano;
        if (lp < 0) lp = 0;
    }

    public String getNombre()          { return nombre; }
    public void setNombre(String n)    { this.nombre = n; }
    public int getLp()                 { return lp; }
    public List<Monstruo> getCampo()   { return campo; }
    public List<Carta> getMano()       { return mano; }
    public Stack<Carta> getMazo()      { return mazo; }
    public boolean isMazoVacio()       { return mazo.isEmpty(); }
}
package modelo.juego;
import java.util.*;

import modelo.carta.Activable;
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

    public void robarCarta(){
        if (mazo.isEmpty()) {
            System.out.println(nombre + "Pierde por quedarse sin cartas en el mazo");
            System.exit(0);
        }
        Carta carta = mazo.pop();
        mano.add(carta);
        System.out.println(nombre + " roba una carta");
    }

    public void mostrarMano(){
        System.out.println("mano de " + nombre + ":");
        for (int i = 0; i < mano.size(); i++){
            System.out.println(i + ". " + mano.get(i).getNombre());
        }
    }

    // jugarCarta sin Scanner — el controlador pasa los índices de sacrificio
    public void jugarCarta(int index, Jugador oponente, List<Integer> sacrificios) {
        if (index < 0 || index >= mano.size()) {
            System.out.println("Índice inválido");
            return;
        }

        Carta carta = mano.remove(index);

        if (carta instanceof Monstruo) {
            Monstruo m = (Monstruo) carta;

            if (m.getNivel() >= 4) {
                int necesarios = m.getNivel() >= 7 ? 2 : 1;
                if (campo.size() < necesarios) {
                    System.out.println("Necesitas " + necesarios + " sacrificio(s) para invocar " + m.getNombre());
                    mano.add(index, carta); // devolver la carta
                    return;
                }

                // sacrificios vienen del controlador, se ordenan de mayor a menor
                // para no alterar índices al remover
                if (sacrificios != null && !sacrificios.isEmpty()) {
                    sacrificios.sort(Collections.reverseOrder());
                    List<Monstruo> sacrificados = new ArrayList<>();
                    for (int idx : sacrificios) {
                        sacrificados.add(campo.remove(idx));
                    }
                    System.out.println("Sacrificaste: " + sacrificados);
                }
            }

            campo.add(m);
            System.out.println(nombre + " invoca " + carta.getNombre());

        } else if (carta instanceof Activable) {
            ((Activable) carta).activar(this, oponente);
            System.out.println(nombre + " activa " + carta.getNombre());
        }
    }

    // métodos auxiliares que usa el controlador para el sacrificio
    public boolean necesitaSacrificio(int index) {
        if (index < 0 || index >= mano.size()) return false;
        Carta carta = mano.get(index);
        if (carta instanceof Monstruo) {
            Monstruo m = (Monstruo) carta;
            if (m.getNivel() >= 4) {
                int necesarios = m.getNivel() >= 7 ? 2 : 1;
                return campo.size() >= necesarios;
            }
        }
        return false;
    }

    public int sacrificiosNecesarios(int index) {
        if (index < 0 || index >= mano.size()) return 0;
        Carta carta = mano.get(index);
        if (carta instanceof Monstruo) {
            Monstruo m = (Monstruo) carta;
            if (m.getNivel() >= 7) return 2;
            if (m.getNivel() >= 4) return 1;
        }
        return 0;
    }

    public void recibirDano(int dano){
        lp -= dano;
        System.out.println(nombre + " recibe " + dano + " Lp");
    }

    public void mostrarCampo() {
        for (int i = 0; i < campo.size(); i++) {
            Monstruo m = campo.get(i);
            System.out.println(i + ". " + m.getNombre() + " [" + (m.isEnAtaque() ? "atk" : "def") + "]");
        }
    }

    public String getNombre(){ return nombre; }
    public int getLp(){ return lp; }
    public List<Monstruo> getCampo(){ return campo; }
    public List<Carta> getMano(){ return mano; }
}
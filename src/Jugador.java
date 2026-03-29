import java.util.*;

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
        System.out.println(nombre + "roba una carta");
    }

    public void mostrarMano(){
        System.out.println("mano de " + nombre + ":");
        for (int i = 0; i < mano.size(); i++){
            System.out.println(i + ". " + mano.get(i).getNombre());
        }
    }

    public void jugarCarta(int index, Jugador oponente){
        if (index < 0 || index >= mano.size()){
            System.out.println("indice invalido");
            return;
        }

        Carta carta = mano.remove(index);

        if(carta instanceof  Monstruo){
            campo.add((Monstruo) carta);
            System.out.println(nombre + " invoca " + carta.getNombre());
        }else if (carta instanceof Activable){
            ((Activable) carta).activar(this, oponente);
            System.out.println(nombre + " activa " + carta.getNombre());
        }
    }

    public void recibirDano(int dano){
        lp -= dano;
        System.out.println(nombre + " recibe " + dano + " Lp");
    }

    public String getNombre(){
        return nombre;
    }
    public int getLp(){
        return lp;
    }
    public List<Monstruo> getCampo(){
        return campo;
    }
    public List<Carta> getMano(){
        return mano;
    }
}

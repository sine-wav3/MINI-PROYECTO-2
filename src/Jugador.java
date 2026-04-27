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
    // se modifica esto para el sistema de sacrificio y se acomode a lo que pide el profe en el documento
    public void jugarCarta(int index, Jugador oponente) {
    if (index < 0 || index >= mano.size()) {
        System.out.println("Índice inválido");
        return;
    }

    Carta carta = mano.remove(index);

    if (carta instanceof Monstruo) {
        Monstruo m = (Monstruo) carta;
        
        // he aqui el famoso sistema de sacrificio, se verifica el nivel del monstruo, si es 4 o mas, se necesitan sacrificios, si es 7 o mas, se necesitan 2 sacrificios, si no se cumplen las condiciones, se devuelve la carta a la mano
        if (m.getNivel() >= 4) {
            int necesarios = m.getNivel() >= 7 ? 2 : 1;
            if (campo.size() < necesarios) {
                System.out.println("Necesitas " + necesarios + " sacrificio(s) para invocar " + m.getNombre());
                mano.add(index, carta); // devolver la carta
                return;
            }
            
            System.out.println("Elige " + necesarios + " monstruo(s) para sacrificar:");
            mostrarCampo();
            
            List<Monstruo> sacrificados = new ArrayList<>();
            Scanner sc = new Scanner(System.in);
            for (int i = 0; i < necesarios; i++) {
                int idx = sc.nextInt();
                sacrificados.add(campo.remove(idx));
            }
            System.out.println("Sacrificaste: " + sacrificados);
        }
        
        campo.add(m);
        System.out.println(nombre + " invoca " + carta.getNombre());
        
    } else if (carta instanceof Activable) {
        ((Activable) carta).activar(this, oponente);
        System.out.println(nombre + " activa " + carta.getNombre());
    }
    }
    //------------------------------------------------------------------ hasta aqui los cambios del sistema de sacrificio, lo demas es acomodar el codigo para que funcione con esto
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

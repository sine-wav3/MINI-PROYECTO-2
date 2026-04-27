package controlador;

import javax.swing.JOptionPane;
import java.util.*;
import modelo.carta.*;
import modelo.juego.Juego;
import modelo.juego.Jugador;
import vista.VentanaYuGiOh;

public class JuegoController {

    private Juego juego;
    private VentanaYuGiOh vista;

    public JuegoController(VentanaYuGiOh vista, String nombreJ1, String nombreJ2) {
        this.vista = vista;
        inicializarJuego(nombreJ1, nombreJ2);
    }

    private void inicializarJuego(String nombreJ1, String nombreJ2) {
        List<Carta> pool = new ArrayList<>();

        String[] nombresMonstruos = {
            "Mago Oscuro", "Dragón Blanco", "Exodia", "Blue-Eyes", "Red-Eyes",
            "Curse of Dragon", "Gaia", "Summoned Skull", "Celtic Guardian", "Dark Magician Girl",
            "Jinzo", "Insect Queen", "Morphing Jar", "Sangan", "Witch of the Black Forest",
            "Kuriboh", "Man-Eater Bug", "Cyber Dragon", "Elemental HERO", "Neo-Spacian",
            "Gravekeeper's", "Vampire Lord", "Zombie Master", "Ryko", "Lyla",
            "Judgment Dragon", "Celestia", "Wulf", "Ehren", "Gorz"
        };

        for (int i = 0; i < 30; i++) {
            int nivel = 1 + (i % 12);
            int atk = 500 + (i * 100);
            int def = 500 + (i * 80);
            pool.add(new Monstruo(nombresMonstruos[i] + " " + (i + 1), atk, def, nivel));
        }

        pool.add(new PotOfGreed());
        pool.add(new PotOfGreed());
        pool.add(new BoostAtk());
        pool.add(new AcesCoup());
        pool.add(new DarkHole());
        pool.add(new Hinotama());
        pool.add(new ChangeOfHeart());
        pool.add(new Raigeki());
        pool.add(new StandarOfCourage());
        pool.add(new TyphoonOfMagicalSpace());

        for (int i = 0; i < 5; i++) pool.add(new MirrorForce());
        for (int i = 0; i < 5; i++) pool.add(new SakuretsuArmor());

        Collections.shuffle(pool);

        Stack<Carta> mazo1 = new Stack<>();
        Stack<Carta> mazo2 = new Stack<>();
        for (int i = 0; i < 25; i++) {
            mazo1.push(pool.remove(0));
            mazo2.push(pool.remove(0));
        }

        Jugador j1 = new Jugador(nombreJ1, mazo1);
        Jugador j2 = new Jugador(nombreJ2, mazo2);

        for (int i = 0; i < 5; i++) {
            j1.robarCarta();
            j2.robarCarta();
        }

        boolean turnoJ1 = new Random().nextBoolean();
        this.juego = new Juego(j1, j2, turnoJ1);
    }

    public void accionRobarJugar() {
        if (juego.hayGanador()) return;

        Jugador actual = juego.getJugadorActual();

        if (juego.isCartaJugadaEsteTurno()) {
            vista.agregarLog("Ya jugaste una carta este turno!");
            return;
        }

        actual.robarCarta();
        vista.agregarLog(actual.getNombre() + " roba una carta.");

        List<Carta> mano = actual.getMano();
        if (mano.isEmpty()) {
            vista.agregarLog("No tienes cartas en mano.");
            return;
        }

        String[] opciones = new String[mano.size()];
        for (int i = 0; i < mano.size(); i++) {
            opciones[i] = i + ". " + mano.get(i).getNombre();
        }

        String seleccion = (String) JOptionPane.showInputDialog(
            vista,
            "Selecciona una carta para jugar:",
            "Mano de " + actual.getNombre(),
            JOptionPane.PLAIN_MESSAGE,
            null,
            opciones,
            opciones[0]
        );

        if (seleccion == null) {
            vista.agregarLog("Ninguna carta jugada.");
            vista.actualizarVista();
            return;
        }

        int idx = Integer.parseInt(seleccion.split("\\.")[0].trim());
        Carta cartaElegida = mano.get(idx);

        // --- Cartas que requieren elegir un monstruo enemigo ---
        if (cartaElegida instanceof ChangeOfHeart || cartaElegida instanceof TyphoonOfMagicalSpace) {

            if (juego.getOponente().getCampo().isEmpty()) {
                vista.agregarLog("No hay monstruos enemigos en campo para activar " + cartaElegida.getNombre() + ".");
                vista.actualizarVista();
                return;
            }

            List<Monstruo> campoEnemigo = juego.getOponente().getCampo();
            String[] opcionesEnemigo = new String[campoEnemigo.size()];
            for (int i = 0; i < campoEnemigo.size(); i++) {
                opcionesEnemigo[i] = i + ". " + campoEnemigo.get(i).getNombre();
            }

            String selEnemigo = (String) JOptionPane.showInputDialog(
                vista,
                "Elige un monstruo enemigo:",
                cartaElegida.getNombre(),
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcionesEnemigo,
                opcionesEnemigo[0]
            );

            if (selEnemigo == null) {
                vista.agregarLog("Acción cancelada.");
                vista.actualizarVista();
                return;
            }

            int idxEnemigo = Integer.parseInt(selEnemigo.split("\\.")[0].trim());

            if (cartaElegida instanceof ChangeOfHeart c) {
                c.activar(actual, juego.getOponente(), idxEnemigo);
                vista.agregarLog(actual.getNombre() + " roba el monstruo: " + campoEnemigo.get(idxEnemigo) + " con Change of Heart.");
            } else if (cartaElegida instanceof TyphoonOfMagicalSpace t) {
                String nombreDestruido = campoEnemigo.get(idxEnemigo).getNombre();
                t.activar(actual, juego.getOponente(), idxEnemigo);
                vista.agregarLog("🌪️ Typhoon Of Magical Space destruye a: " + nombreDestruido);
            }

            actual.getMano().remove(idx);
            juego.setCartaJugadaEsteTurno(true);
            vista.agregarLog(actual.getNombre() + " juega: " + cartaElegida.getNombre());
            verificarGanador();
            vista.actualizarVista();
            return;
        }

        // --- Resto de cartas (flujo normal) ---
        List<Integer> sacrificios = new ArrayList<>();
        int cantSacrificios = actual.sacrificiosNecesarios(idx);

        if (cantSacrificios > 0 && actual.necesitaSacrificio(idx)) {
            List<Monstruo> campo = actual.getCampo();
            String[] opcionesCampo = new String[campo.size()];
            for (int i = 0; i < campo.size(); i++) {
                opcionesCampo[i] = i + ". " + campo.get(i).getNombre()
                    + " [" + (campo.get(i).isEnAtaque() ? "ATK" : "DEF") + "]";
            }

            for (int s = 0; s < cantSacrificios; s++) {
                String selSacrificio = (String) JOptionPane.showInputDialog(
                    vista,
                    "Elige monstruo a sacrificar (" + (s + 1) + "/" + cantSacrificios + "):",
                    "Sacrificio",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcionesCampo,
                    opcionesCampo[0]
                );

                if (selSacrificio == null) {
                    vista.agregarLog("Sacrificio cancelado.");
                    vista.actualizarVista();
                    return;
                }

                int idxSac = Integer.parseInt(selSacrificio.split("\\.")[0].trim());
                sacrificios.add(idxSac);
                vista.agregarLog("Sacrificado: " + campo.get(idxSac).getNombre());
            }
        }

        actual.jugarCarta(idx, juego.getOponente(), sacrificios);
        juego.setCartaJugadaEsteTurno(true);
        vista.agregarLog(actual.getNombre() + " juega: " + seleccion);

        verificarGanador();
        vista.actualizarVista();
    }

    public void accionAtacar() {
        if (juego.hayGanador()) return;

        Jugador atacante = juego.getJugadorActual();
        Jugador defensor = juego.getOponente();

        if (atacante.getCampo().isEmpty()) {
            vista.agregarLog("No tienes monstruos para atacar.");
            return;
        }

        List<Monstruo> campoAtacante = atacante.getCampo();
        List<String> opcionesAtkList = new ArrayList<>();
        List<Integer> indicesValidos = new ArrayList<>();

        for (int i = 0; i < campoAtacante.size(); i++) {
            Monstruo m = campoAtacante.get(i);
            if (m.puedeAtacar()) {
                opcionesAtkList.add(i + ". " + m.getNombre() + " ATK: " + m.getAtk());
                indicesValidos.add(i);
            }
        }

        if (opcionesAtkList.isEmpty()) {
            vista.agregarLog("Todos tus monstruos ya atacaron este turno.");
            return;
        }

        String[] opcionesAtk = opcionesAtkList.toArray(new String[0]);

        String selAtacante = (String) JOptionPane.showInputDialog(
            vista,
            "Elige tu monstruo atacante:",
            "Atacar",
            JOptionPane.PLAIN_MESSAGE,
            null,
            opcionesAtk,
            opcionesAtk[0]
        );

        if (selAtacante == null) return;

        int idxAtk = Integer.parseInt(selAtacante.split("\\.")[0].trim());
        Monstruo mAtacante = campoAtacante.get(idxAtk);

        if (defensor.getCampo().isEmpty()) {
            defensor.recibirDano(mAtacante.getAtk());
            mAtacante.marcarAtaque();
            vista.agregarLog("Ataque directo! " + mAtacante.getNombre()
                + " inflige " + mAtacante.getAtk() + " de dano a " + defensor.getNombre());
        } else {
            List<Monstruo> campoDefensor = defensor.getCampo();
            String[] opcionesDef = new String[campoDefensor.size()];
            for (int i = 0; i < campoDefensor.size(); i++) {
                Monstruo m = campoDefensor.get(i);
                opcionesDef[i] = i + ". " + m.getNombre()
                    + (m.isEnAtaque() ? " ATK: " + m.getAtk() : " DEF: " + m.getDef());
            }

            String selDefensor = (String) JOptionPane.showInputDialog(
                vista,
                "Elige el monstruo a atacar:",
                "Atacar",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcionesDef,
                opcionesDef[0]
            );

            if (selDefensor == null) return;

            int idxDef = Integer.parseInt(selDefensor.split("\\.")[0].trim());
            Monstruo mDefensor = campoDefensor.get(idxDef);

            if (mDefensor.isEnAtaque()) {
                if (mAtacante.getAtk() > mDefensor.getAtk()) {
                    int diff = mAtacante.getAtk() - mDefensor.getAtk();
                    defensor.getCampo().remove(idxDef);
                    defensor.recibirDano(diff);
                    vista.agregarLog(mDefensor.getNombre() + " destruido. "
                        + defensor.getNombre() + " recibe " + diff + " de dano.");
                } else if (mAtacante.getAtk() < mDefensor.getAtk()) {
                    int diff = mDefensor.getAtk() - mAtacante.getAtk();
                    atacante.getCampo().remove(idxAtk);
                    atacante.recibirDano(diff);
                    vista.agregarLog(mAtacante.getNombre() + " destruido. "
                        + atacante.getNombre() + " recibe " + diff + " de dano.");
                } else {
                    defensor.getCampo().remove(idxDef);
                    atacante.getCampo().remove(idxAtk);
                    vista.agregarLog("Empate! Ambos monstruos destruidos.");
                }
            } else {
                if (mAtacante.getAtk() > mDefensor.getDef()) {
                    defensor.getCampo().remove(idxDef);
                    vista.agregarLog(mDefensor.getNombre() + " destruido en defensa. Sin dano a LP.");
                } else {
                    vista.agregarLog("Ataque bloqueado. " + mDefensor.getNombre() + " resiste.");
                }
            }

            mAtacante.marcarAtaque();
        }

        verificarGanador();
        vista.actualizarVista();
    }

    public void accionPasarTurno() {
        if (juego.hayGanador()) return;
        juego.pasarTurno();
        vista.agregarLog("Turno pasado. Ahora es turno de " + juego.getJugadorActual().getNombre());
        vista.actualizarVista();
    }

    public Jugador getJ1() { return juego.getJ1(); }
    public Jugador getJ2() { return juego.getJ2(); }
    public Juego getJuego() { return juego; }

    private void verificarGanador() {
        if (juego.hayGanador()) {
            Jugador ganador = juego.getGanador();
            JOptionPane.showMessageDialog(
                vista,
                "El destino ha hablado! " + ganador.getNombre() + " GANA EL DUELO!\n\"Confia en el corazon de las cartas\"",
                "Yu-Gi-Oh! - Fin del Duelo",
                JOptionPane.INFORMATION_MESSAGE
            );
            System.exit(0);
        }
    }
}
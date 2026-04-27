import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import javax.swing.*;

public class VentanaYuGiOh extends JFrame {
    private Jugador j1, j2;
    private boolean turnoJ1;
    private JTextArea areaLog;
    private JLabel lpJ1, lpJ2;
    private JPanel panelCampoJ1, panelCampoJ2;
    
    public VentanaYuGiOh() {
        //configuracion normal de la ventana
        setTitle("Yu-Gi-Oh! - Duelo de Cartas");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana
        setLayout(new BorderLayout());
        
        // Aquí se inicializa el juego
        inicializarJuego();
        
        // se crea la interfaz gráfica
        crearInterfaz();
        
        // se actualiza la vista para mostrar el estado inicial
        actualizarVista();
    }
    
    private void inicializarJuego() {
        // Esto es lo mismo que estaba en juego.main pero adaptado a esta clase
        Scanner sc = new Scanner(System.in);
        
        // se muestan ventanitas para pedir los nombres y asi 
        String n1 = JOptionPane.showInputDialog(this, "Nombre del Jugador 1:");
        String n2 = JOptionPane.showInputDialog(this, "Nombre del Jugador 2:");
        
        if (n1 == null || n1.trim().isEmpty()) n1 = "Yugi";
        if (n2 == null || n2.trim().isEmpty()) n2 = "Kaiba";
        
        // crea las 50 cartas (30 monstruos, 10 mágicas, 10 trampas)
        List<Carta> pool = new ArrayList<>();
        
        // 30 monstruos
        for (int i = 0; i < 30; i++) {
            pool.add(new Monstruo("Monstruo " + (i+1), 1000 + i * 50, 1000 + i * 30, 1 + (i % 12)));
        }
        
        // 10 magicas que ya estaban 
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
        
        // 10 trampas
        for (int i = 0; i < 10; i++) {
            pool.add(new TrampaGenerica("Trampa " + (i+1)));
        }
        
        Collections.shuffle(pool);
        
        Stack<Carta> mazo1 = new Stack<>();
        Stack<Carta> mazo2 = new Stack<>();
        
        for (int i = 0; i < 25; i++) {
            mazo1.push(pool.remove(0));
            mazo2.push(pool.remove(0));
        }
        
        j1 = new Jugador(n1, mazo1);
        j2 = new Jugador(n2, mazo2);
        
        // Robar 5 cartas iniciales
        for (int i = 0; i < 5; i++) {
            j1.robarCarta();
            j2.robarCarta();
        }
        
        // Aqui alguno empieza (al azar)
        turnoJ1 = new Random().nextBoolean();
    }
    
    private void crearInterfaz() {
        // Panel superior 
        JPanel panelLP = new JPanel(new GridLayout(1, 2));
        lpJ1 = new JLabel("", SwingConstants.CENTER);
        lpJ2 = new JLabel("", SwingConstants.CENTER);
        lpJ1.setFont(new Font("Monospaced", Font.BOLD, 16));
        lpJ2.setFont(new Font("Monospaced", Font.BOLD, 16));
        panelLP.add(lpJ1);
        panelLP.add(lpJ2);
        add(panelLP, BorderLayout.NORTH);
        
        // Panel central con campos de batalla
        JPanel panelCentral = new JPanel(new GridLayout(2, 1));
        panelCampoJ1 = new JPanel(new FlowLayout());
        panelCampoJ2 = new JPanel(new FlowLayout());
        panelCampoJ1.setBorder(BorderFactory.createTitledBorder("Campo de " + j1.getNombre()));
        panelCampoJ2.setBorder(BorderFactory.createTitledBorder("Campo de " + j2.getNombre()));
        panelCentral.add(panelCampoJ1);
        panelCentral.add(panelCampoJ2);
        add(panelCentral, BorderLayout.CENTER);
        
        //area de log 
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setBackground(Color.BLACK);
        areaLog.setForeground(Color.GREEN);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setPreferredSize(new Dimension(900, 150));
        add(scrollLog, BorderLayout.SOUTH);
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnRobarJugar = new JButton("Robar / Jugar Carta");
        JButton btnAtacar = new JButton("Atacar");
        JButton btnPasar = new JButton("Pasar Turno");
        
        btnRobarJugar.addActionListener(e -> accionRobarJugar());
        btnAtacar.addActionListener(e -> accionAtacar());
        btnPasar.addActionListener(e -> accionPasarTurno());
        
        panelBotones.add(btnRobarJugar);
        panelBotones.add(btnAtacar);
        panelBotones.add(btnPasar);
        add(panelBotones, BorderLayout.EAST);
    }
    
    private void accionRobarJugar() {
        // aqui va lo de robar carta y luego mostrar la mano para seleccionar una carta a jugar
        agregarLog("Turno de " + getJugadorActual().getNombre());
        getJugadorActual().robarCarta();
        // Mostrar mano y seleccionar carta...
        actualizarVista();
    }
    
    private void accionAtacar() {
        agregarLog("Fase de ataque");
        // logica del ataque
        actualizarVista();
    }
    
    private void accionPasarTurno() {
        turnoJ1 = !turnoJ1;
        agregarLog("Turno pasado. Ahora es turno de " + getJugadorActual().getNombre());
        actualizarVista();
    }
    
    private Jugador getJugadorActual() {
        return turnoJ1 ? j1 : j2;
    }
    
    private void agregarLog(String msg) {
        areaLog.append(msg + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
    
    private void actualizarVista() {
        lpJ1.setText(j1.getNombre() + " LP: " + j1.getLp());
        lpJ2.setText(j2.getNombre() + " LP: " + j2.getLp());
        
        // actualiza campos de monstruos
        panelCampoJ1.removeAll();
        for (Monstruo m : j1.getCampo()) {
            panelCampoJ1.add(new JLabel(m.getNombre() + " [" + (m.isEnAtaque() ? "⚔️" : "🛡️") + "]"));
        }
        
        panelCampoJ2.removeAll();
        for (Monstruo m : j2.getCampo()) {
            panelCampoJ2.add(new JLabel(m.getNombre() + " [" + (m.isEnAtaque() ? "⚔️" : "🛡️") + "]"));
        }
        
        panelCampoJ1.revalidate();
        panelCampoJ1.repaint();
        panelCampoJ2.revalidate();
        panelCampoJ2.repaint();
        
        // Verificar si alguien gano o q
        if (j1.getLp() <= 0) {
            JOptionPane.showMessageDialog(this, j2.getNombre() + " GANA EL DUELO!", "Yu-Gi-Oh!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else if (j2.getLp() <= 0) {
            JOptionPane.showMessageDialog(this, j1.getNombre() + " GANA EL DUELO!", "Yu-Gi-Oh!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
}

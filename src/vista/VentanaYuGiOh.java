package vista;

import java.awt.*;
import javax.swing.*;

import controlador.JuegoController;
import modelo.carta.Carta;
import modelo.carta.Monstruo;
import modelo.juego.Jugador;

public class VentanaYuGiOh extends JFrame {

    private JuegoController controller;
    private JTextArea areaLog;
    private JLabel lpJ1, lpJ2;
    private JPanel panelCampoJ1, panelCampoJ2;
    private JPanel panelMano;

    public VentanaYuGiOh() {
        String n1 = JOptionPane.showInputDialog(null, "Nombre del Jugador 1:");
        String n2 = JOptionPane.showInputDialog(null, "Nombre del Jugador 2:");

        if (n1 == null || n1.trim().isEmpty()) n1 = "Yugi";
        if (n2 == null || n2.trim().isEmpty()) n2 = "Kaiba";

        controller = new JuegoController(this, n1, n2);

        setTitle("Yu-Gi-Oh! - Duelo de Cartas");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        crearInterfaz();
        actualizarVista();
    }

    private void crearInterfaz() {
        // Panel LP
        JPanel panelLP = new JPanel(new GridLayout(1, 2));
        lpJ1 = new JLabel("", SwingConstants.CENTER);
        lpJ2 = new JLabel("", SwingConstants.CENTER);
        lpJ1.setFont(new Font("Monospaced", Font.BOLD, 16));
        lpJ2.setFont(new Font("Monospaced", Font.BOLD, 16));
        panelLP.add(lpJ1);
        panelLP.add(lpJ2);

        // Panel mano
        panelMano = new JPanel(new FlowLayout());
        panelMano.setBorder(BorderFactory.createTitledBorder("Cartas en mano"));
        panelMano.setPreferredSize(new Dimension(900, 100));

        // Panel superior agrupa LP + mano
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelLP, BorderLayout.NORTH);
        panelSuperior.add(panelMano, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        // Campos de batalla
        JPanel panelCentral = new JPanel(new GridLayout(2, 1));
        panelCampoJ1 = new JPanel(new FlowLayout());
        panelCampoJ2 = new JPanel(new FlowLayout());
        panelCampoJ1.setBorder(BorderFactory.createTitledBorder(
            "Campo de " + controller.getJ1().getNombre()));
        panelCampoJ2.setBorder(BorderFactory.createTitledBorder(
            "Campo de " + controller.getJ2().getNombre()));
        panelCentral.add(panelCampoJ1);
        panelCentral.add(panelCampoJ2);
        add(panelCentral, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnRobarJugar = new JButton("Robar / Jugar Carta");
        JButton btnAtacar = new JButton("Atacar");
        JButton btnPasar = new JButton("Pasar Turno");

        btnRobarJugar.addActionListener(e -> controller.accionRobarJugar());
        btnAtacar.addActionListener(e -> controller.accionAtacar());
        btnPasar.addActionListener(e -> controller.accionPasarTurno());

        panelBotones.add(btnRobarJugar);
        panelBotones.add(btnAtacar);
        panelBotones.add(btnPasar);
        add(panelBotones, BorderLayout.EAST);

        // Log
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setBackground(Color.BLACK);
        areaLog.setForeground(Color.GREEN);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setPreferredSize(new Dimension(900, 150));
        add(scrollLog, BorderLayout.SOUTH);
    }

    public void actualizarVista() {
        Jugador j1 = controller.getJ1();
        Jugador j2 = controller.getJ2();
        Jugador actual = controller.getJuego().getJugadorActual();

        // LP
        lpJ1.setText(j1.getNombre() + " LP: " + j1.getLp());
        lpJ2.setText(j2.getNombre() + " LP: " + j2.getLp());

        // Campo J1
        panelCampoJ1.removeAll();
        for (Monstruo m : j1.getCampo()) {
            JLabel lbl = new JLabel(m.getNombre()
                + " [" + (m.isEnAtaque() ? "ATK: " + m.getAtk() : "DEF: " + m.getDef()) + "]");
            lbl.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
            panelCampoJ1.add(lbl);
        }

        // Campo J2
        panelCampoJ2.removeAll();
        for (Monstruo m : j2.getCampo()) {
            JLabel lbl = new JLabel(m.getNombre()
                + " [" + (m.isEnAtaque() ? "ATK: " + m.getAtk() : "DEF: " + m.getDef()) + "]");
            lbl.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            panelCampoJ2.add(lbl);
        }

        // Mano del jugador actual
        panelMano.removeAll();
        panelMano.setBorder(BorderFactory.createTitledBorder(
            "Mano de " + actual.getNombre()));
        for (Carta c : actual.getMano()) {
            JLabel lbl = new JLabel(c.getNombre());
            lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            lbl.setOpaque(true);
            lbl.setBackground(new Color(230, 230, 180));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            panelMano.add(lbl);
        }

        panelCampoJ1.revalidate();
        panelCampoJ1.repaint();
        panelCampoJ2.revalidate();
        panelCampoJ2.repaint();
        panelMano.revalidate();
        panelMano.repaint();
    }

    public void agregarLog(String msg) {
        areaLog.append(msg + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
}
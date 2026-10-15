package vistas;

import utiles.EstilosRPG;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.Timer;

public class PanelNavegacion extends JPanel {

    private JButton btnIdentidad;
    private JButton btnStats;
    private JButton btnHabilidades;
    private JButton btnInventario;
    private JButton btnFicha;
    private JLabel labelHora;
    private MainFrame frame;

    public PanelNavegacion(MainFrame frame) {
        this.frame = frame;

        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        setBackground(EstilosRPG.COLOR_FONDO_NAV);
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, EstilosRPG.COLOR_DORADO));

        btnIdentidad = new JButton("Personaje");
        btnStats = new JButton("Atributos");
        btnHabilidades = new JButton("Habilidades");
        btnInventario = new JButton("Inventario");
        btnFicha = new JButton("Ficha");

        EstilosRPG.estilizarBotonNav(btnIdentidad);
        EstilosRPG.estilizarBotonNav(btnStats);
        EstilosRPG.estilizarBotonNav(btnHabilidades);
        EstilosRPG.estilizarBotonNav(btnInventario);
        EstilosRPG.estilizarBotonNav(btnFicha);


        btnIdentidad.addActionListener(e -> frame.cambiarPantalla("Identidad"));
        btnStats.addActionListener(e -> frame.cambiarPantalla("Stats"));
        btnHabilidades.addActionListener(e -> frame.cambiarPantalla("Habilidades"));
        btnInventario.addActionListener(e -> frame.cambiarPantalla("Inventario"));
        btnFicha.addActionListener(e -> frame.cambiarPantalla("FichaFinal"));

        add(btnIdentidad);
        add(btnStats);
        add(btnHabilidades);
        add(btnInventario);
        add(btnFicha);
        labelHora = new JLabel();
        labelHora.setFont(new Font("Monospaced", Font.BOLD, 14)); // Letra retro tipo consola
        labelHora.setForeground(EstilosRPG.COLOR_DORADO); // Usa el dorado de tus estilos
        labelHora.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0)); // Espacio a la izquierda para separarlo de los botones
        add(labelHora);

        // Configurar el formato y el Timer de actualización por segundo
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("HH:mm:ss");
        Timer timerHora = new Timer(1000, e -> {
            labelHora.setText("[" + LocalDateTime.now().format(formateador) + "]");
        });
        timerHora.start();
        actualizarEstado(0);
    }

    public void actualizarEstado(int progreso) {
        btnIdentidad.setEnabled(true);
        btnStats.setEnabled(progreso >= 1);
        btnHabilidades.setEnabled(progreso >= 2);
        btnInventario.setEnabled(progreso >= 3);
        btnFicha.setEnabled(progreso >= 4);
    }
}
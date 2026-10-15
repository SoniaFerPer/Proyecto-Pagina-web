package vistas;

import modelo.Personaje;
import utiles.EstilosRPG;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contenedorPrincipal;
    private JPanel panelCreador;
    private JPanel pantallasCreador;
    private Personaje nuevoPersonaje;
    private int progreso = 0;
    private PanelNavegacion panelNavegacion;
    private PanelFichaFinal panelFichaFinal;

    public MainFrame() {
        setTitle("Forja de Heroes");
        setMinimumSize(new Dimension(1400, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setJMenuBar(crearBarraMenu());
        cargarIcono();

        cardLayout = new CardLayout();
        contenedorPrincipal = new JPanel(cardLayout);
        nuevoPersonaje = new Personaje();

        JPanel menu = new PanelMenu(this);
        construirPanelCreador();

        contenedorPrincipal.add(menu, "Menu");
        contenedorPrincipal.add(panelCreador, "Creador");

        add(contenedorPrincipal);

        panelNavegacion.actualizarEstado(progreso);
        cardLayout.show(contenedorPrincipal, "Menu");
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void construirPanelCreador() {
        panelCreador = new JPanel(new BorderLayout());
        panelNavegacion = new PanelNavegacion(this);
        panelFichaFinal = new PanelFichaFinal(this, nuevoPersonaje);

        pantallasCreador = new JPanel(cardLayout);
        pantallasCreador.add(crearPantallaTransparente(new PanelIdentidad(this, nuevoPersonaje)), "Identidad");
        pantallasCreador.add(crearPantallaTransparente(new PanelStats(this, nuevoPersonaje)), "Stats");
        pantallasCreador.add(crearPantallaTransparente(new PanelHabilidades(this, nuevoPersonaje)), "Habilidades");
        pantallasCreador.add(crearPantallaTransparente(new PanelInventario(this, nuevoPersonaje)), "Inventario");
        pantallasCreador.add(crearPantallaTransparente(panelFichaFinal), "FichaFinal");

        panelCreador.add(panelNavegacion, BorderLayout.NORTH);
        panelCreador.add(pantallasCreador, BorderLayout.CENTER);
    }

    private JMenuBar crearBarraMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu archivo = new JMenu("Archivo");
        JMenuItem volverMenu = new JMenuItem("Volver al menu");
        volverMenu.addActionListener(e -> cambiarPantalla("Menu"));
        archivo.add(volverMenu);

        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(e -> System.exit(0));
        archivo.add(salir);

        JMenu ayuda = new JMenu("Ayuda");
        JMenuItem acercaDe = new JMenuItem("Acerca de");
        acercaDe.addActionListener(e -> mostrarAyuda());
        ayuda.add(acercaDe);

        barra.add(archivo);
        barra.add(ayuda);

        return barra;
    }

    private void cargarIcono() {
        java.net.URL url = getClass().getResource("/img/default.png");
        if (url != null) {
            setIconImage(new ImageIcon(url).getImage());
        }
    }

    private JPanel crearPantallaTransparente(JPanel panelInterno) {
        EstilosRPG.PanelConFondoDefault panelConFondo = new EstilosRPG.PanelConFondoDefault();
        panelConFondo.add(panelInterno, BorderLayout.CENTER);
        panelInterno.setOpaque(false);

        return panelConFondo;
    }

    public void cambiarPantalla(String pantalla) {
        if (pantalla.equals("Menu")) {
            cardLayout.show(contenedorPrincipal, "Menu");
            return;
        }
        if (pantalla.equals("Creador")) {
            pantalla = "Identidad";
        }
        cardLayout.show(contenedorPrincipal, "Creador");
        CardLayout cl = (CardLayout) pantallasCreador.getLayout();
        int nivelRequerido = switch (pantalla) {
            case "Identidad" -> 0;
            case "Stats" -> 1;
            case "Habilidades" -> 2;
            case "Inventario" -> 3;
            case "FichaFinal" -> 4;
            default -> 0;
        };
        if (nivelRequerido > progreso) {
            JOptionPane.showMessageDialog(this, "Debes completar la pantalla anterior primero.");
            return;
        }
        cl.show(pantallasCreador, pantalla);

        if (pantalla.equals("FichaFinal")) {
            panelFichaFinal.actualizarCampos();
        }
    }

    public void completarPantalla(String pantalla) {
        int nivel = switch (pantalla) {
            case "Identidad" -> 1;
            case "Stats" -> 2;
            case "Habilidades" -> 3;
            case "Inventario" -> 4;
            default -> progreso;
        };
        if (nivel > progreso) {
            progreso = nivel;
            panelNavegacion.actualizarEstado(progreso);
        }
    }

//    public Personaje getNuevoPersonaje() {
//        return nuevoPersonaje;
//    }

    public PanelFichaFinal getPanelFichaFinal() {
        return panelFichaFinal;
    }

    public void reiniciarCreador() {
        contenedorPrincipal.remove(panelCreador);
        nuevoPersonaje = new Personaje();
        progreso = 0;
        construirPanelCreador();
        contenedorPrincipal.add(panelCreador, "Creador");
        contenedorPrincipal.revalidate();
        contenedorPrincipal.repaint();
        panelNavegacion.actualizarEstado(progreso);
        cambiarPantalla("Menu");
    }

    public void iniciarNuevoPersonaje() {
        reiniciarCreador();
        cambiarPantalla("Identidad");
    }

    public void cargarPersonajeDesdeBD(int idPersonaje) {
        Personaje cargado = Personaje.cargarPorId(idPersonaje);

        if (cargado == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el personaje seleccionado.");
            return;
        }

        contenedorPrincipal.remove(panelCreador);
        nuevoPersonaje = cargado;
        progreso = 4;
        construirPanelCreador();
        contenedorPrincipal.add(panelCreador, "Creador");
        panelNavegacion.actualizarEstado(progreso);
        panelFichaFinal.actualizarCampos();
        contenedorPrincipal.revalidate();
        contenedorPrincipal.repaint();
        cambiarPantalla("FichaFinal");
    }

    public void mostrarAyuda() {
        JOptionPane.showMessageDialog(
                this,
                """
                Forja de Heroes

                1. Crea un personaje completando Identidad, Atributos, Habilidades e Inventario.
                2. La ficha final resume todo antes de guardarlo.
                3. Cargar personaje abre una ficha guardada desde MySQL.
                4. Eliminar personaje borra un registro tras pedir confirmacion.

                Version 1.0
                """,
                "Ayuda",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}

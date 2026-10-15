package vistas;

import database.Conexiondb;
import utiles.EstilosRPG;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelMenu extends EstilosRPG.PanelConFondo {

    private final MainFrame frame;

    public PanelMenu(MainFrame frame) {
        super(EstilosRPG.RUTA_FONDO_MENU);
        this.frame = frame;

        JPanel panelBotones = new JPanel();
        EstilosRPG.hacerTransparente(panelBotones);
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        panelBotones.add(crearBoton("CREAR PERSONAJE", e -> frame.iniciarNuevoPersonaje()));
        panelBotones.add(crearBoton("CARGAR PERSONAJE", e -> cargarPersonaje()));
        panelBotones.add(crearBoton("ELIMINAR PERSONAJE", e -> eliminarPersonaje()));
        panelBotones.add(crearBoton("SALIR", e -> System.exit(0)));

        add(panelBotones, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener accion) {
        JButton btn = new JButton(texto);
        EstilosRPG.estilizarBotonRPG(btn);

        btn.setPreferredSize(new Dimension(220, 50));
        btn.addActionListener(accion);
        return btn;
    }

    private void cargarPersonaje() {
        SeleccionPersonaje seleccion = pedirPersonaje("Cargar personaje");

        if (seleccion == null) {
            return;
        }

        frame.cargarPersonajeDesdeBD(seleccion.id());
    }

    private void eliminarPersonaje() {
        SeleccionPersonaje seleccion = pedirPersonaje("Eliminar personaje");

        if (seleccion == null) {
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "Seguro que quieres eliminar a " + seleccion.nombre() + "?",
                "Confirmar borrado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM personajes WHERE id_personaje = ?";

        try (Connection con = Conexiondb.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, seleccion.id());
            int borrados = ps.executeUpdate();

            if (borrados > 0) {
                JOptionPane.showMessageDialog(this, "Personaje eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontro el personaje seleccionado.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error eliminando personaje: " + ex.getMessage());
        }
    }

    private SeleccionPersonaje pedirPersonaje(String titulo) {
        String sql = "SELECT id_personaje, nombre FROM personajes ORDER BY nombre";

        try (Connection con = Conexiondb.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<String> nombres = new ArrayList<>();
            Map<String, Integer> mapa = new HashMap<>();

            while (rs.next()) {
                String etiqueta = rs.getString("nombre") + " (ID: " + rs.getInt("id_personaje") + ")";
                nombres.add(etiqueta);
                mapa.put(etiqueta, rs.getInt("id_personaje"));
            }

            if (nombres.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay personajes guardados.");
                return null;
            }

            String seleccionado = (String) JOptionPane.showInputDialog(
                    this,
                    "Selecciona un personaje:",
                    titulo,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    nombres.toArray(),
                    nombres.get(0)
            );

            if (seleccionado == null) {
                return null;
            }

            return new SeleccionPersonaje(mapa.get(seleccionado), seleccionado);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando personajes: " + ex.getMessage());
            return null;
        }
    }

    private record SeleccionPersonaje(int id, String nombre) {
    }
}

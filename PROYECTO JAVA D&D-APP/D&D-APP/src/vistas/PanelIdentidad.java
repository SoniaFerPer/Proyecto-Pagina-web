package vistas;

import database.Conexiondb;
import modelo.Personaje;
import utiles.EstilosRPG;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.*;

public class PanelIdentidad extends JPanel {

    private MainFrame frame;
    private Personaje p;

    private JTextField txtNombre;
    private JComboBox<String> comboRaza, comboClase;

    private JLabel lblTitulo;
    private JLabel lblImagen;
    private JTextArea txtDescripcion;
    private JLabel lblBonificaciones;

    private static final Dimension IMG_SIZE = new Dimension(900, 750);
    private static final Dimension DESC_SIZE = new Dimension(420, 130);

    public PanelIdentidad(MainFrame frame, Personaje p) {

        this.frame = frame;
        this.p = p;

        setLayout(new BorderLayout(20, 20));
        setOpaque(false);

        JPanel panelControles = new JPanel(new GridLayout(8, 1, 10, 10));
        panelControles.setBackground(new Color(0, 0, 0, 0));
        panelControles.setOpaque(false);
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNombre = new JTextField(15);
        txtNombre.setPreferredSize(new Dimension(450, 35));
        txtNombre.setBackground(EstilosRPG.WHITE);
        txtNombre.setFont(EstilosRPG.FUENTE_NEGRITA);

        String[] razas = {"Humano", "Enano", "Mediano", "Elfo", "Draconido", "Tiefling"};
        String[] clases = {"Guerrero", "Picaro", "Mago", "Clerigo", "Druida", "Explorador"};

        comboRaza = new JComboBox<>(razas);
        comboClase = new JComboBox<>(clases);

        comboRaza.setPreferredSize(new Dimension(450, 35));
        comboClase.setPreferredSize(new Dimension(450, 35));

        EstilosRPG.estilizarComboBox(comboRaza);
        EstilosRPG.estilizarComboBox(comboClase);

        panelControles.add(EstilosRPG.crearLabelFormulario("Nombre del Personaje:"));
        panelControles.add(txtNombre);
        panelControles.add(EstilosRPG.crearLabelFormulario("Raza:"));
        panelControles.add(comboRaza);
        panelControles.add(EstilosRPG.crearLabelFormulario("Clase:"));
        panelControles.add(comboClase);

        JPanel panelVisual = new JPanel(new BorderLayout(10, 10));
        panelVisual.setOpaque(false);
        panelVisual.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTitulo = new JLabel(" ", SwingConstants.CENTER);
        lblTitulo.setForeground(EstilosRPG.COLOR_DORADO);
        lblTitulo.setFont(EstilosRPG.FUENTE_NEGRITA);

        lblImagen = new JLabel("[Arte del Héroe]", SwingConstants.CENTER);
        lblImagen.setPreferredSize(IMG_SIZE);
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.TOP);

        txtDescripcion = EstilosRPG.crearAreaTextoPergamino();
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(txtDescripcion);
        scroll.setPreferredSize(DESC_SIZE);

        lblBonificaciones = new JLabel();
        lblBonificaciones.setForeground(EstilosRPG.COLOR_DORADO);
        lblBonificaciones.setFont(new Font("Serif", Font.PLAIN, 13));
        lblBonificaciones.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);

        panelInferior.add(scroll, BorderLayout.CENTER);
        panelInferior.add(lblBonificaciones, BorderLayout.SOUTH);

        panelVisual.add(lblTitulo, BorderLayout.NORTH);
        panelVisual.add(lblImagen, BorderLayout.CENTER);
        panelVisual.add(panelInferior, BorderLayout.SOUTH);

        // Botón siguiente
        JButton btnSiguiente = new JButton("SIGUIENTE");
        EstilosRPG.estilizarBotonDorado(btnSiguiente);
        btnSiguiente.setPreferredSize(new Dimension(300, 45));
        btnSiguiente.addActionListener(e -> validarYContinuar());

        //GUARDAR PARA FINAL
        txtNombre.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarEstadoBoton(); }
            public void removeUpdate(DocumentEvent e) { actualizarEstadoBoton(); }
            public void changedUpdate(DocumentEvent e) { actualizarEstadoBoton(); }

            private void actualizarEstadoBoton() {
                btnSiguiente.setEnabled(!txtNombre.getText().trim().isEmpty());
            }
        });

        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.add(btnSiguiente);

        add(panelControles, BorderLayout.WEST);
        add(panelVisual, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        comboRaza.addActionListener(e -> actualizarCombinacion());
        comboClase.addActionListener(e -> actualizarCombinacion());

        actualizarCombinacion();
    }


    private void actualizarCombinacion() {

        int idRazaSel = comboRaza.getSelectedIndex() + 1;
        int idClaseSel = comboClase.getSelectedIndex() + 1;

        try (Connection con = Conexiondb.getConexion();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT titulo, descripcion, imagen FROM combinaciones WHERE id_raza=? AND id_clase=?")) {

            ps.setInt(1, idRazaSel);
            ps.setInt(2, idClaseSel);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lblTitulo.setText(rs.getString("titulo"));
                txtDescripcion.setText(rs.getString("descripcion"));

                String ruta = "/img/" + idRazaSel + "_" + idClaseSel + ".png";
                java.net.URL imgURL = getClass().getResource(ruta);

                if (imgURL != null) {
                    ImageIcon iconOriginal = new ImageIcon(imgURL);

                    // Obtiene el tamaño real del archivo (1122 x 1402)
                    int anchoOriginal = iconOriginal.getIconWidth();
                    int altoOriginal = iconOriginal.getIconHeight();

                    // Divide por 1.6 de forma precisa y convierte a entero
                    int nuevoAncho = (int) (anchoOriginal / 1.285); // 1122 / 1.6 = 701
                    int nuevoAlto = (int) (altoOriginal / 1.285);   // 1402 / 1.6 = 876

                    // Escala la imagen manteniendo la proporción perfecta
                    Image imgEscalada = iconOriginal.getImage().getScaledInstance(
                            nuevoAncho,
                            nuevoAlto,
                            Image.SCALE_SMOOTH
                    );

                    // Guardamos la imagen escalada en un ImageIcon definitivo
                    ImageIcon iconFinal = new ImageIcon(imgEscalada);

                    // Bloquea la alineación arriba (TOP) y a la izquierda (LEFT)
                    lblImagen.setHorizontalAlignment(SwingConstants.LEFT);
                    lblImagen.setVerticalAlignment(SwingConstants.TOP);

                    // Asigna la imagen limpia al JLabel
                    lblImagen.setIcon(iconFinal);
                    lblImagen.setText("");

                    // Mantiene las dimensiones de tu contenedor para que recorte el sobrante
                    lblImagen.setPreferredSize(IMG_SIZE);
                    lblImagen.setSize(IMG_SIZE);
                }

                actualizarBonificaciones();

            } else {
                lblTitulo.setText("Sin combinación");
                txtDescripcion.setText("No encontrada en base de datos.");
            }

        } catch (Exception e) {
            txtDescripcion.setText("Error en base de datos.");
        }
    }

    private void actualizarBonificaciones() {

        int idRazaSel = comboRaza.getSelectedIndex() + 1;
        int idClaseSel = comboClase.getSelectedIndex() + 1;

        StringBuilder sb = new StringBuilder();

        try (Connection con = Conexiondb.getConexion()) {

            // =========================
            // RAZA (1 LÍNEA)
            // =========================
            sb.append("RAZA: ");

            if (comboRaza.getSelectedItem().toString().equalsIgnoreCase("Humano")) {

                sb.append("+1 a todas las bonificaciones");

            } else {

                String sqlRaza =
                        "SELECT a.nombre, rb.valor " +
                                "FROM raza_bonificaciones rb " +
                                "JOIN atributos a ON rb.id_atributo = a.id_atributo " +
                                "WHERE rb.id_raza = ?";

                PreparedStatement psR = con.prepareStatement(sqlRaza);
                psR.setInt(1, idRazaSel);

                ResultSet rsR = psR.executeQuery();

                boolean first = true;

                while (rsR.next()) {

                    if (!first) sb.append(", ");
                    first = false;

                    sb.append("+")
                            .append(rsR.getInt("valor"))
                            .append(" ")
                            .append(rsR.getString("nombre"));
                }
            }

            sb.append("\n");

            // =========================
            // CLASE (1 LÍNEA)
            // =========================
            sb.append("CLASE: ");

            String sqlClase =
                    "SELECT a.nombre, cb.valor " +
                            "FROM clase_bonificaciones cb " +
                            "JOIN atributos a ON cb.id_atributo = a.id_atributo " +
                            "WHERE cb.id_clase = ?";

            PreparedStatement psC = con.prepareStatement(sqlClase);
            psC.setInt(1, idClaseSel);

            ResultSet rsC = psC.executeQuery();

            boolean first = true;

            while (rsC.next()) {

                if (!first) sb.append(", ");
                first = false;

                sb.append("+")
                        .append(rsC.getInt("valor"))
                        .append(" ")
                        .append(rsC.getString("nombre"));
            }

            lblBonificaciones.setText("<html>" + sb.toString() + "</html>");

        } catch (Exception e) {
            lblBonificaciones.setText("Error cargando bonificaciones");
        }
    }

    private void validarYContinuar() {

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "¡La crónica exige un nombre!");
            return;
        }

        p.setNombre(nombre);
        p.setIdRaza(comboRaza.getSelectedIndex() + 1);
        p.setIdClase(comboClase.getSelectedIndex() + 1);
        p.cargarRazaClase();

        frame.completarPantalla("Identidad");
        frame.cambiarPantalla("Stats");
    }
}

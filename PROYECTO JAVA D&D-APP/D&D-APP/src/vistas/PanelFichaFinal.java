package vistas;

import database.Conexiondb;
import modelo.Personaje;
import utiles.EstilosRPG;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanelFichaFinal extends JPanel {

    private final MainFrame frame;
    private final Personaje p;

    private static final int ORO_INICIAL = 120;

    public PanelFichaFinal(MainFrame frame, Personaje p) {
        this.frame = frame;
        this.p = p;

        setOpaque(false);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        construirVista();
    }

    private void construirVista() {
        removeAll();
        add(crearCentro(), BorderLayout.CENTER);
        add(crearBotonera(), BorderLayout.SOUTH);
    }

    private JPanel crearCentro() {
        recalcularStats();

        JPanel root = new JPanel(new GridBagLayout());
        root.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.58;
        root.add(crearFichaCompleta(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.42;
        gbc.insets = new Insets(0, 15, 0, 0);
        root.add(crearImagen(), gbc);

        return root;
    }

    private JPanel crearFichaCompleta() {
        JPanel panel = panelBase("FICHA DEL PERSONAJE");
        panel.add(crearScrollLista(fichaComoLista()));

        return panel;
    }

    private JPanel crearStats() {
        JPanel panel = panelBase("ESTADISTICAS");

        recalcularStats();

        panel.add(label("Nombre: " + safe(p.getNombre())));
        panel.add(label("Raza: " + safe(p.getNombreRazaTxt())));
        panel.add(label("Clase: " + safe(p.getNombreClaseTxt())));
        panel.add(label("HP: " + p.getVidaMaxima()));
        panel.add(label("MP: " + p.getManaMaximo()));

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        panel.add(label("FUE: " + p.getFuerza()));
        panel.add(label("DES: " + p.getDestreza()));
        panel.add(label("CON: " + p.getConstitucion()));
        panel.add(label("INT: " + p.getInteligencia()));
        panel.add(label("SAB: " + p.getSabiduria()));
        panel.add(label("CAR: " + p.getCarisma()));

        return panel;
    }

    private JPanel crearImagen() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        panel.add(titulo("PERSONAJE"), BorderLayout.NORTH);
        panel.add(new ImagenPersonajePanel(cargarImagen()), BorderLayout.CENTER);

        return panel;
    }

    private Image cargarImagen() {
        try {
            String path = "/img/" + p.getIdRaza() + "_" + p.getIdClase() + ".png";
            URL url = getClass().getResource(path);

            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                return icon.getImage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> fichaComoLista() {
        List<String> elementos = new ArrayList<>();

        elementos.add("Nombre: " + safe(p.getNombre()));
        elementos.add("Raza: " + safe(p.getNombreRazaTxt()));
        elementos.add("Clase: " + safe(p.getNombreClaseTxt()));
        elementos.add("HP: " + p.getVidaMaxima());
        elementos.add("MP: " + p.getManaMaximo());
        elementos.add("Fuerza: " + p.getFuerza());
        elementos.add("Destreza: " + p.getDestreza());
        elementos.add("Constitución: " + p.getConstitucion());
        elementos.add("Inteligencia: " + p.getInteligencia());
        elementos.add("Sabiduría: " + p.getSabiduria());
        elementos.add("Carisma: " + p.getCarisma());

        for (String habilidad : separarPorComas(p.getHabilidadesTxt())) {
            elementos.add("Habilidad: " + habilidad);
        }

        elementos.addAll(inventarioComoLista());

        return elementos;
    }

    private JPanel crearHabilidades() {
        JPanel panel = panelBase("HABILIDADES");
        panel.add(crearScrollLista(separarPorComas(p.getHabilidadesTxt())));

        return panel;
    }

    private JPanel crearInventario() {
        JPanel panel = panelBase("INVENTARIO");
        panel.add(crearScrollLista(inventarioComoLista()));

        return panel;
    }

    //GUARDAR VOLVER AL MENU
    private JPanel crearBotonera() {
        JButton btnGuardar = new JButton(p.getIdPersonaje() > 0 ? "VOLVER AL MENU" : "GUARDAR PERSONAJE");
        EstilosRPG.estilizarBotonDorado(btnGuardar);
        btnGuardar.setPreferredSize(new Dimension(300, 45));
        btnGuardar.addActionListener(e -> {
            if (p.getIdPersonaje() > 0) {
                frame.reiniciarCreador();
            } else {
                guardarTodo();
            }
        });

        JPanel south = new JPanel();
        south.setOpaque(false);
        south.add(btnGuardar);

        return south;
    }

    private void recalcularStats() {
        p.cargarRazaClase();
        p.recalcularStats();
        if (p.getOro() <= 0) {
            p.setOro(ORO_INICIAL);
        }
    }

    public void actualizarCampos() {
        construirVista();
        revalidate();
        repaint();
    }

    private void guardarTodo() {
        if (!personajeCompleto()) {
            JOptionPane.showMessageDialog(this, "Completa todos los pasos antes de guardar el personaje.");
            return;
        }

        try (Connection con = Conexiondb.getConexion()) {
            Personaje.asegurarColumnaHabilidades(con);

            if (p.getIdPersonaje() > 0) {
                actualizarPersonaje(con);
                guardarAtributos(con, p.getIdPersonaje());
                JOptionPane.showMessageDialog(this, "Personaje actualizado correctamente");
            } else {
                int idPersonaje = insertarPersonaje(con);
                p.setIdPersonaje(idPersonaje);
                guardarAtributos(con, idPersonaje);
                JOptionPane.showMessageDialog(this, "Personaje guardado correctamente");
            }

            frame.reiniciarCreador();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error guardando personaje: " + e.getMessage());
        }
    }

    private boolean personajeCompleto() {
        return p.getNombre() != null && !p.getNombre().isBlank()
                && p.getIdRaza() > 0
                && p.getIdClase() > 0
                && p.getHabilidadesTxt() != null && !p.getHabilidadesTxt().isBlank()
                && p.getInventario() != null && !p.getInventario().isBlank();
    }

    private int insertarPersonaje(Connection con) throws SQLException {
        String sql = """
                    INSERT INTO personajes
                    (nombre, id_raza, id_clase, experiencia,
                     vida_actual, mana_actual, oro, inventario, habilidades)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            rellenarPersonajeStatement(ps);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("No se pudo obtener el ID generado.");
    }

    private void actualizarPersonaje(Connection con) throws SQLException {
        String sql = """
                    UPDATE personajes
                    SET nombre = ?, id_raza = ?, id_clase = ?, experiencia = ?,
                        vida_actual = ?, mana_actual = ?, oro = ?, inventario = ?, habilidades = ?
                    WHERE id_personaje = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            rellenarPersonajeStatement(ps);
            ps.setInt(11, p.getIdPersonaje());
            ps.executeUpdate();
        }
    }

    private void rellenarPersonajeStatement(PreparedStatement ps) throws SQLException {
        ps.setString(1, p.getNombre());
        ps.setInt(2, p.getIdRaza());
        ps.setInt(3, p.getIdClase());
        ps.setInt(4, p.getExperiencia());
        ps.setInt(5, p.getVidaMaxima());
        ps.setInt(6, p.getManaMaximo());
        ps.setInt(7, p.getOro());
        ps.setString(8, p.getInventario());
        ps.setString(9, p.getHabilidadesTxt());
    }

    private void guardarAtributos(Connection con, int idPersonaje) throws SQLException {
        String sql = """
                    INSERT INTO personaje_atributos
                    (id_personaje, id_atributo, valor)
                    VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE valor = VALUES(valor)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPersonaje);

            ps.setInt(2, 1);
            ps.setInt(3, p.getFuerza());
            ps.addBatch();
            ps.setInt(2, 2);
            ps.setInt(3, p.getDestreza());
            ps.addBatch();
            ps.setInt(2, 3);
            ps.setInt(3, p.getConstitucion());
            ps.addBatch();
            ps.setInt(2, 4);
            ps.setInt(3, p.getInteligencia());
            ps.addBatch();
            ps.setInt(2, 5);
            ps.setInt(3, p.getSabiduria());
            ps.addBatch();
            ps.setInt(2, 6);
            ps.setInt(3, p.getCarisma());
            ps.addBatch();

            ps.executeBatch();
        }
    }

    private JPanel panelBase(String titulo) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        panel.add(titulo(titulo));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        return panel;
    }

    private JLabel titulo(String texto) {
        JLabel t = new JLabel(texto, SwingConstants.CENTER);
        t.setForeground(EstilosRPG.COLOR_DORADO);
        t.setFont(EstilosRPG.FUENTE_TITULO_MEDIANO);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);
        return t;
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(EstilosRPG.COLOR_GRISACEO);
        l.setFont(EstilosRPG.FUENTE_NEGRITA);
        return l;
    }

    private JScrollPane crearScrollLista(List<String> elementos) {
        JPanel lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        if (elementos.isEmpty()) {
            elementos.add("N/A");
        }

        for (String elemento : elementos) {
            JEditorPane item = new JEditorPane("text/html", itemHtml(elemento));
            item.setEditable(false);
            item.setOpaque(false);
            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            item.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            lista.add(item);
        }

        JScrollPane scroll = EstilosRPG.crearScrollEstilizado(lista);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private String itemHtml(String texto) {
        String clave = texto;
        String valor = "";
        int separador = texto.indexOf(":");

        if (separador >= 0) {
            clave = texto.substring(0, separador + 1);
            valor = texto.substring(separador + 1).trim();
        }

        return """
                <html>
                  <body style='margin:0; font-family:Serif; font-size:14px; color:white;'>
                    <span style='color:#d4af37; font-weight:bold;'>- %s</span>
                    <span style='color:white;'> %s</span>
                  </body>
                </html>
                """.formatted(escapeHtml(clave), escapeHtml(valor));
    }

    private String escapeHtml(String texto) {
        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private List<String> separarPorComas(String texto) {
        List<String> elementos = new ArrayList<>();

        if (texto == null || texto.isBlank()) {
            return elementos;
        }

        for (String parte : texto.split(",")) {
            String limpia = parte.trim();
            if (!limpia.isEmpty()) {
                elementos.add(limpia);
            }
        }

        return elementos;
    }

    private List<String> inventarioComoLista() {
        List<String> elementos = new ArrayList<>();
        elementos.add("Oro: " + p.getOro());

        String inventario = p.getInventario();
        if (inventario == null || inventario.isBlank()) {
            return elementos;
        }

        for (String bloque : inventario.split("\\|")) {
            String limpio = bloque.trim();

            if (limpio.startsWith("Objetos:")) {
                String objetos = limpio.substring("Objetos:".length()).trim();
                for (String objeto : separarPorComas(objetos)) {
                    elementos.add("Objeto: " + objeto);
                }
            } else if (!limpio.isEmpty()) {
                elementos.add(limpio);
            }
        }

        return elementos;
    }

    private static class ImagenPersonajePanel extends JPanel {
        private final Image imagen;

        public ImagenPersonajePanel(Image imagen) {
            this.imagen = imagen;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (imagen == null) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Serif", Font.BOLD, 18));
                FontMetrics fm = g.getFontMetrics();
                String texto = "SIN IMAGEN";
                g.drawString(texto, (getWidth() - fm.stringWidth(texto)) / 2, getHeight() / 2);
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int iw = imagen.getWidth(this);
            int ih = imagen.getHeight(this);

            if (iw <= 0 || ih <= 0 || getWidth() <= 0 || getHeight() <= 0) {
                g2.dispose();
                return;
            }

            double escala = Math.min((double) getWidth() / iw, (double) getHeight() / ih);
            int ancho = (int) (iw * escala);
            int alto = (int) (ih * escala);
            int x = (getWidth() - ancho) / 2;
            int y = (getHeight() - alto) / 2;

            g2.drawImage(imagen, x, y, ancho, alto, this);
            g2.dispose();
        }
    }

    private String safe(String s) {
        return (s == null || s.isBlank()) ? "N/A" : s;
    }
}

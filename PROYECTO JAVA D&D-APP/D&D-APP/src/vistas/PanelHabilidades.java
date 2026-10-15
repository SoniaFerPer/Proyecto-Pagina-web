package vistas;

import database.Conexiondb;
import modelo.Personaje;
import utiles.EstilosRPG;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PanelHabilidades extends JPanel {

    private MainFrame frame;
    private Personaje p;
    private List<HabilidadItem> habilidades = new ArrayList<>();
    private JLabel lblContador;

    private static final int MAX = 6;

    public PanelHabilidades(MainFrame frame, Personaje p) {
        this.frame = frame;
        this.p = p;

        setLayout(new BorderLayout(20, 20));
        setOpaque(false);

        lblContador = new JLabel("Habilidades: 0 / " + MAX, SwingConstants.CENTER);
        lblContador.setFont(EstilosRPG.FUENTE_TITULO_GRANDE);
        lblContador.setForeground(EstilosRPG.COLOR_DORADO);
        lblContador.setBorder(BorderFactory.createEmptyBorder(12, 10, 8, 10));
        add(lblContador, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 18, 14));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(4, 30, 14, 30));

        cargar(grid);
        JScrollPane scroll = EstilosRPG.crearScrollEstilizado(grid);
        add(scroll, BorderLayout.CENTER);

        //BOTON SIGUIENTE
        JButton btn = new JButton("SIGUIENTE");
        EstilosRPG.estilizarBotonDorado(btn);
        btn.setPreferredSize(new Dimension(300, 45));
        btn.addActionListener(e -> continuar());

        JPanel south = new JPanel();
        south.setOpaque(false);
        south.add(btn);
        add(south, BorderLayout.SOUTH);
    }

    private void cargar(JPanel grid) {
        try (
                Connection con = Conexiondb.getConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT nombre, descripcion FROM habilidades")
        ) {
            while (rs.next()) {
                HabilidadItem item = new HabilidadItem(
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );

                habilidades.add(item);
                grid.add(item);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando habilidades");
            e.printStackTrace();
        }
    }

    private String normalizarNombre(String nombre) {
        return corregirTexto(nombre)
                .toLowerCase(Locale.ROOT)
                .replace("\u00e1", "a")
                .replace("\u00e9", "e")
                .replace("\u00ed", "i")
                .replace("\u00f3", "o")
                .replace("\u00fa", "u")
                .replace("\u00f1", "n")
                .replace(" ", "_");
    }

    private String corregirTexto(String texto) {
        if (texto == null) {
            return "";
        }

        String corregido = texto;

        for (int i = 0; i < 2 && pareceMojibake(corregido); i++) {
            corregido = new String(
                    corregido.getBytes(StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8
            );
        }

        return corregido;
    }

    private boolean pareceMojibake(String texto) {
        return texto.contains("\u00c3")
                || texto.contains("\u00c2")
                || texto.contains("\u00e2");
    }

    private void actualizarBloqueo() {
        long seleccionadas = habilidades.stream()
                .filter(h -> h.selected)
                .count();

        boolean bloquear = seleccionadas >= MAX;

        for (HabilidadItem h : habilidades) {
            if (!h.selected) {
                h.setEnabled(!bloquear);
                h.setBloqueado(bloquear);
            }
        }
    }

    private void actualizarContador() {
        long c = habilidades.stream()
                .filter(h -> h.selected)
                .count();

        lblContador.setText("Habilidades: " + c + " / " + MAX);
    }

    private class HabilidadItem extends JPanel {

        private boolean selected = false;
        private boolean bloqueado = false;
        private String nombre;
        private Image backgroundImage;

        private Border normal = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosRPG.COLOR_DORADO, 1),
                BorderFactory.createEmptyBorder(5, 9, 5, 9)
        );

        private Border glow = BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        BorderFactory.createLineBorder(EstilosRPG.COLOR_DORADO, 3)
                ),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
        );

        public HabilidadItem(String nombre, String desc) {
            this.nombre = corregirTexto(nombre);
            String descripcion = corregirTexto(desc);

            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(520, 88));
            setMinimumSize(new Dimension(430, 88));
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(normal);

            java.net.URL url = getClass().getResource("/img/" + normalizarNombre(nombre) + ".png");
            if (url != null) {
                backgroundImage = new ImageIcon(url).getImage();
            }

            JLabel lblNombre = new JLabel(this.nombre);
            lblNombre.setForeground(EstilosRPG.COLOR_DORADO);
            lblNombre.setFont(EstilosRPG.FUENTE_NEGRITA);
            lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblNombre.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

            JTextArea txtDesc = new JTextArea(descripcion);
            txtDesc.setEditable(false);
            txtDesc.setOpaque(false);
            txtDesc.setLineWrap(true);
            txtDesc.setWrapStyleWord(true);
            txtDesc.setFocusable(false);
            txtDesc.setForeground(Color.WHITE);
            txtDesc.setFont(EstilosRPG.FUENTE_NORMAL);
            txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
            txtDesc.setBorder(BorderFactory.createEmptyBorder());
            txtDesc.setMargin(new Insets(0, 0, 0, 0));

            JPanel texto = new JPanel();
            texto.setOpaque(false);
            texto.setLayout(new BoxLayout(texto, BoxLayout.Y_AXIS));
            texto.setBorder(BorderFactory.createEmptyBorder(4, 155, 4, 12));
            texto.add(lblNombre);
            texto.add(Box.createRigidArea(new Dimension(0, 2)));
            texto.add(txtDesc);
            add(texto, BorderLayout.CENTER);

            java.awt.event.MouseAdapter click = new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (bloqueado && !selected) {
                        return;
                    }

                    toggle();
                }
            };

            addMouseListener(click);
            lblNombre.addMouseListener(click);
            txtDesc.addMouseListener(click);
            texto.addMouseListener(click);
        }

        private void toggle() {
            selected = !selected;
            setBorder(selected ? glow : normal);

            actualizarContador();
            actualizarBloqueo();

            repaint();
        }

        public void setBloqueado(boolean b) {
            bloqueado = b;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            if (backgroundImage != null) {
                int iw = backgroundImage.getWidth(this);
                int ih = backgroundImage.getHeight(this);

                if (iw > 0 && ih > 0) {
                    double scale = Math.max((double) getWidth() / iw, (double) getHeight() / ih);
                    int nw = (int) (iw * scale);
                    int nh = (int) (ih * scale);
                    int x = (getWidth() - nw) / 2;
                    int y = (getHeight() - nh) / 2;
                    g2.drawImage(backgroundImage, x, y, nw, nh, this);
                }
            }

            g2.setColor(selected ? new Color(60, 48, 20, 95) : new Color(0, 0, 0, 55));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            if (bloqueado && !selected) {
                g2.setColor(new Color(0, 0, 0, 150));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            g2.dispose();
        }
    }

    private void continuar() {
        long seleccionadas = habilidades.stream()
                .filter(h -> h.selected)
                .count();

        if (seleccionadas != MAX) {
            JOptionPane.showMessageDialog(this, "Debes elegir exactamente " + MAX + " habilidades.");
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (HabilidadItem h : habilidades) {
            if (h.selected) {
                sb.append(h.nombre).append(", ");
            }
        }

        p.setHabilidadesTxt(sb.substring(0, sb.length() - 2));

        frame.completarPantalla("Habilidades");
        frame.cambiarPantalla("Inventario");
    }
}

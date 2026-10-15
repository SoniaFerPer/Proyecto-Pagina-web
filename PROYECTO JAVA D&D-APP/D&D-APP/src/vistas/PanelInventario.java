package vistas;

import database.Conexiondb;
import modelo.Personaje;
import utiles.EstilosRPG;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class PanelInventario extends JPanel {

    private MainFrame frame;
    private Personaje p;

    private ButtonGroup grupoArmas = new ButtonGroup();
    private ButtonGroup grupoArmaduras = new ButtonGroup();

    private ArrayList<ItemCard> armas = new ArrayList<>();
    private ArrayList<ItemCard> armaduras = new ArrayList<>();
    private ArrayList<ItemCard> objetos = new ArrayList<>();

    public PanelInventario(MainFrame frame, Personaje p) {

        this.frame = frame;
        this.p = p;

        setOpaque(false);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelCentral(), BorderLayout.CENTER);

        JButton btnFinalizar = new JButton("Ver Resumen Final");
        EstilosRPG.estilizarBotonDorado(btnFinalizar);
        btnFinalizar.setPreferredSize(new Dimension(300, 45));
        btnFinalizar.addActionListener(e -> validarYGuardar());

        JPanel sur = new JPanel();
        sur.setOpaque(false);
        sur.add(btnFinalizar);

        add(sur, BorderLayout.SOUTH);
    }

    // =====================================================
    // CENTRAL
    // =====================================================
    private JPanel crearPanelCentral() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 18, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        panel.add(crearColumnaArmas());
        panel.add(crearColumnaArmaduras());
        panel.add(crearColumnaObjetos());

        return panel;
    }

    // =====================================================
    // COLUMNA ARMAS
    // =====================================================
    private JPanel crearColumnaArmas() {

        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(false);

        root.add(titulo("ARMAS"), BorderLayout.NORTH);

        JPanel list = crearLista();

        try (Connection con = Conexiondb.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT nombre FROM inventario WHERE tipo='ARMA'")) {

            while (rs.next()) {
                ItemCard card = new ItemCard(rs.getString("nombre"), grupoArmas, "ARMA");
                armas.add(card);

                list.add(card);
                list.add(Box.createRigidArea(new Dimension(0, 5)));
            }

        } catch (Exception e) {
            list.add(error("Error armas"));
        }

        root.add(EstilosRPG.crearScrollEstilizado(list), BorderLayout.CENTER);

        return root;
    }

    // =====================================================
    // COLUMNA ARMADURAS
    // =====================================================
    private JPanel crearColumnaArmaduras() {

        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(false);

        root.add(titulo("ARMADURAS"), BorderLayout.NORTH);

        JPanel list = crearLista();

        try (Connection con = Conexiondb.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT nombre FROM inventario WHERE tipo='ARMADURA'")) {

            while (rs.next()) {
                ItemCard card = new ItemCard(rs.getString("nombre"), grupoArmaduras, "ARMADURA");
                armaduras.add(card);

                list.add(card);
                list.add(Box.createRigidArea(new Dimension(0, 5)));
            }

        } catch (Exception e) {
            list.add(error("Error armaduras"));
        }

        root.add(EstilosRPG.crearScrollEstilizado(list), BorderLayout.CENTER);

        return root;
    }

    // =====================================================
    // COLUMNA OBJETOS
    // =====================================================
    private JPanel crearColumnaObjetos() {

        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(false);

        root.add(titulo("OBJETOS"), BorderLayout.NORTH);

        JPanel list = crearLista();

        try (Connection con = Conexiondb.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT nombre FROM inventario WHERE tipo IN ('POCION','OBJETO_MAGICO','HERRAMIENTA','MISCELANEO')")) {

            while (rs.next()) {
                ItemCard card = new ItemCard(rs.getString("nombre"), null, "OBJETO");
                objetos.add(card);

                list.add(card);
                list.add(Box.createRigidArea(new Dimension(0, 5)));
            }

        } catch (Exception e) {
            list.add(error("Error objetos"));
        }

        root.add(EstilosRPG.crearScrollEstilizado(list), BorderLayout.CENTER);

        return root;
    }

    // =====================================================
    // TÍTULO FIJO (NO SCROLL)
    // =====================================================
    private JLabel titulo(String t) {

        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setFont(EstilosRPG.FUENTE_TITULO_GRANDE);
        l.setForeground(EstilosRPG.COLOR_DORADO);
        l.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        return l;
    }

    private JPanel crearLista() {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        return list;
    }

    private JLabel error(String msg) {
        JLabel l = new JLabel(msg);
        l.setForeground(Color.RED);
        return l;
    }

    // =====================================================
    // ITEM CARD
    // =====================================================
    private class ItemCard extends JPanel {

        private String nombre;
        private String tipo;
        private JRadioButton radio;

        private Color base;
        private Color bg;

        public ItemCard(String nombre, ButtonGroup grupo, String tipo) {

            this.nombre = nombre;
            this.tipo = tipo;

            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(360, 48));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            setMinimumSize(new Dimension(320, 48));
            setOpaque(false);

            switch (tipo) {
                case "ARMA":
                    base = new Color(180, 0, 0);
                    bg = new Color(87, 5, 5, 160);
                    break;
                case "ARMADURA":
                    base = new Color(0, 120, 200);
                    bg = new Color(10, 40, 70, 179);
                    break;
                case "OBJETO":
                    base = new Color(74, 207, 38, 255);
                    bg = new Color(49, 92, 39, 158);
                    break;
            }

            radio = new JRadioButton(nombre);
            radio.setOpaque(false);
            radio.setForeground(Color.WHITE);
            radio.setFont(EstilosRPG.FUENTE_NEGRITA);

            EstilosRPG.hacerTransparente(radio);

            if (grupo != null) grupo.add(radio);

            add(radio, BorderLayout.CENTER);

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    radio.setSelected(true);
                    repaint();
                }
            });

            radio.addActionListener(e -> {

                if (!tipo.equals("OBJETO")) {
                    return;
                }

                if (radio.isSelected()) {

                    int count = contarObjetosSeleccionados();

                    if (count > 5) {

                        radio.setSelected(false);

                        JOptionPane.showMessageDialog(
                                PanelInventario.this,
                                "🎒 Solo puedes seleccionar un máximo de 5 objetos."
                        );
                    }
                }

                repaint();
            });
        }
        private int contarObjetosSeleccionados() {

            int count = 0;

            for (ItemCard c : objetos) {
                if (c.isSelected()) {
                    count++;
                }
            }

            return count;
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

            if (radio.isSelected()) {
                g2.setColor(EstilosRPG.COLOR_DORADO);
            } else {
                g2.setColor(base);
            }

            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

            super.paintComponent(g);
        }

        public boolean isSelected() {
            return radio.isSelected();
        }

        public String getNombre() {
            return nombre;
        }
    }

    // VALIDACIÓN
    private void validarYGuardar() {
        String arma = obtenerSeleccion(armas);
        String armadura = obtenerSeleccion(armaduras);
        // VALIDAR ARMA

        if (arma == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "⚔ Debes seleccionar un ARMA antes de continuar."
            );
            return;
        }


        // VALIDAR ARMADURA
        if (armadura == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "🛡 Debes seleccionar una ARMADURA antes de continuar."
            );
            return;
        }

        // VALIDAR OBJETOS (MAX 5)
        int count = 0;

        for (ItemCard c : objetos) {
            if (c.isSelected()) {
                count++;
            }
        }

        if (count == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "🎒 Debes seleccionar al menos 1 OBJETO (máximo 5)."
            );
            return;
        }

        if (count > 5) {
            JOptionPane.showMessageDialog(
                    this,
                    "🎒 Solo puedes llevar un máximo de 5 objetos."
            );
            return;
        }

        // CONSTRUIR RESULTADO
        StringBuilder sb = new StringBuilder();

        sb.append("Arma: ").append(arma).append(" | ");
        sb.append("Armadura: ").append(armadura).append(" | Objetos: ");

        int added = 0;

        for (ItemCard c : objetos) {
            if (c.isSelected() && added < 5) {
                sb.append(c.getNombre()).append(", ");
                added++;
            }
        }

        String result = sb.toString();

        if (result.endsWith(", ")) {
            result = result.substring(0, result.length() - 2);
        }

        p.setInventario(result);

        frame.getPanelFichaFinal().actualizarCampos();
        frame.completarPantalla("Inventario");
        frame.cambiarPantalla("FichaFinal");
    }

    private String obtenerSeleccion(ArrayList<ItemCard> lista) {
        for (ItemCard c : lista) {
            if (c.isSelected()) return c.getNombre();
        }
        return null;
    }
}

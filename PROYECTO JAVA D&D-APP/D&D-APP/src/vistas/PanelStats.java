package vistas;

import database.Conexiondb;
import modelo.Personaje;
import utiles.EstilosRPG;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PanelStats extends JPanel {

    private MainFrame frame;
    private Personaje p;
    private int bolsaPuntos = 0;
    private boolean mulliganUsado = false;
    private JLabel lblBolsa;
    private JSpinner[] spinners = new JSpinner[6];
    private int[] valoresBase = new int[6];
    private String[] nombresStats = {"Fuerza", "Destreza", "Constitución", "Inteligencia", "Sabiduría", "Carisma"};
    private String[] descripcionesStats = {
            "Determina el poder físico y daño cuerpo a cuerpo.",
            "Afecta agilidad, reflejos y precisión.",
            "Representa resistencia física y vida.",
            "Mide conocimiento y capacidad mágica.",
            "Influye percepción e intuición.",
            "Determina presencia y capacidad social."
    };
    private JLabel[] lblBonos = new JLabel[6];
    private JButton btnMulligan;

    public PanelStats(MainFrame frame, Personaje p) {
        this.frame = frame;
        this.p = p;

        setLayout(new BorderLayout(15, 15));
        setBackground(EstilosRPG.COLOR_FONDO_NAV);

        // Panel superior: zona de dados
        JPanel panelDados = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        panelDados.setBackground(new Color(0, 0, 0, 100));  // Fondo semitransparente
        panelDados.setOpaque(false); //tiene que ser false para que no salga doble botón efecto espejo
        panelDados.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, EstilosRPG.COLOR_DORADO));

        JButton btnDados = new JButton("Lanzar Dados");
        btnMulligan = new JButton("Tirada de Salvación");
        btnMulligan.setEnabled(false);
        lblBolsa = new JLabel("Puntos Disponibles: 0");
        lblBolsa.setFont(EstilosRPG.FUENTE_TITULO_MEDIANO);
        lblBolsa.setForeground(EstilosRPG.COLOR_DORADO);

        EstilosRPG.estilizarBotonDorado(btnDados);
        EstilosRPG.estilizarBotonDorado(btnMulligan);

        panelDados.add(btnDados);
        panelDados.add(btnMulligan);
        panelDados.add(lblBolsa);
        add(panelDados, BorderLayout.NORTH);

        // Panel central: GridBagLayout en lugar de BoxLayout
        GridBagLayout layoutStats = new GridBagLayout();
        layoutStats.columnWeights = new double[]{1.0, 1.0, 1.0};
        JPanel panelStats = new JPanel(layoutStats);
        panelStats.setBackground(new Color(0, 0, 0, 0));  // Fondo transparente
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createEmptyBorder(25, 45, 25, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 18, 18, 18);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        // Cabeceras
        String[] cabeceras = {"ATRIBUTO", "BONO", "VALOR"};
        for (int i = 0; i < cabeceras.length; i++) {
            gbc.gridx = i;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 0.35;
            JLabel lblCabecera = new JLabel(cabeceras[i]);
            lblCabecera.setFont(EstilosRPG.FUENTE_TITULO_GRANDE);
            lblCabecera.setForeground(EstilosRPG.COLOR_DORADO);
            lblCabecera.setHorizontalAlignment(SwingConstants.CENTER);
            lblCabecera.setVerticalAlignment(SwingConstants.CENTER);
            panelStats.add(lblCabecera, gbc);
        }

        // Filas de atributos
        for (int i = 0; i < 6; i++) {
            gbc.insets = new Insets(8, 18, 8, 18);
            gbc.weighty = 1.0;
            // Nombre del atributo
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.weightx = 1.0;
            JLabel lblNombre = new JLabel(nombresStats[i]);
            lblNombre.setToolTipText(descripcionesStats[i]);
            lblNombre.setFont(EstilosRPG.FUENTE_NEGRITA_GRANDE);
            lblNombre.setForeground(EstilosRPG.WHITE);
            lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
            lblNombre.setVerticalAlignment(SwingConstants.CENTER);
            panelStats.add(lblNombre, gbc);

            //COLOR DE LOS MENSAJES PUSH-UP
            UIManager.put("ToolTip.background", EstilosRPG.COLOR_FONDO_NAV);
            UIManager.put("ToolTip.foreground", EstilosRPG.COLOR_DORADO);
            UIManager.put("ToolTip.font", EstilosRPG.FUENTE_NORMAL);

            // Bono
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            lblBonos[i] = new JLabel("+0");
            lblBonos[i].setFont(EstilosRPG.FUENTE_NEGRITA_GRANDE);
            lblBonos[i].setForeground(EstilosRPG.WHITE);
            lblBonos[i].setHorizontalAlignment(SwingConstants.CENTER);
            lblBonos[i].setVerticalAlignment(SwingConstants.CENTER);
            JPanel contenedorBono = new JPanel(new BorderLayout());
            contenedorBono.setOpaque(false);
            contenedorBono.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 45));
            contenedorBono.add(lblBonos[i], BorderLayout.CENTER);
            panelStats.add(contenedorBono, gbc);

            // Spinner
            gbc.gridx = 2;
            gbc.weightx = 1.0;
            //DIMENSION SPINNER
            spinners[i] = new JSpinner(new SpinnerNumberModel(10, 10, 50, 1));
            spinners[i].setEnabled(false);
            Dimension spinnerSize = new Dimension(185, 46);

            spinners[i].setPreferredSize(spinnerSize);
            spinners[i].setMinimumSize(spinnerSize);
            spinners[i].setMaximumSize(spinnerSize);

            // Aplicar estilo dorado al spinner
            EstilosRPG.estilizarSpinnerDeshabilitado(spinners[i]);
            aplicarColorSpinner(spinners[i], EstilosRPG.WHITE);

            JPanel contenedorSpinner = new JPanel(new GridBagLayout());
            contenedorSpinner.setOpaque(false);
            contenedorSpinner.add(spinners[i]);
            panelStats.add(contenedorSpinner, gbc);

            final int index = i;
            spinners[i].addChangeListener(e -> evaluarCambioPuntos(index));
        }

        JScrollPane scrollPane = new JScrollPane(panelStats);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Botón siguiente
        JButton btnSig = new JButton("SIGUIENTE");
        EstilosRPG.estilizarBotonDorado(btnSig);
        btnSig.setPreferredSize(new Dimension(300, 45));
        btnSig.addActionListener(e -> avanzarPantalla());

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(0, 0, 0, 0));  // Fondo transparente
        panelBoton.setOpaque(true);
        panelBoton.add(btnSig);
        add(panelBoton, BorderLayout.SOUTH);

        // Eventos de dados
        btnDados.addActionListener(e -> {
            ejecutarTiradaDados();
            btnDados.setEnabled(false);
            habilitarSpinners(true);
            // Aplicar estilo dorado habilitado a los spinners
            for (JSpinner spinner : spinners) {
                EstilosRPG.estilizarSpinner(spinner);
                aplicarColorSpinner(spinner, EstilosRPG.WHITE);
            }
        });

        btnMulligan.addActionListener(e -> {
            mulliganUsado = true;
            btnMulligan.setEnabled(false);
            ejecutarTiradaDados();
            // Aplicar estilo dorado habilitado a los spinners de la tirada de salvacion
            for (JSpinner spinner : spinners) {
                EstilosRPG.estilizarSpinner(spinner);
                aplicarColorSpinner(spinner, EstilosRPG.WHITE);
            }
        });
    }

    private void aplicarColorSpinner(JSpinner spinner, Color colorTexto) {
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setForeground(colorTexto);
        editor.getTextField().setFont(EstilosRPG.FUENTE_NEGRITA_GRANDE);
    }

    private void ejecutarTiradaDados() {
        bolsaPuntos = (int) (Math.random() * 31) + 10;
        lblBolsa.setText("Puntos Disponibles: " + bolsaPuntos);

        if (bolsaPuntos <= 20 && !mulliganUsado) {
            btnMulligan.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Tirada mediocre (" + bolsaPuntos + " pts). ¡Tienes una segunda oportunidad!");
        } else {
            btnMulligan.setEnabled(false);
            if (!mulliganUsado) {
                JOptionPane.showMessageDialog(this, "¡Buena tirada! (" + bolsaPuntos + " pts).");
            }
        }

        int[] bonosCalculados = new int[6];

        // Bonos de raza
        String sqlRaza =
                "SELECT a.nombre, rb.valor " +
                        "FROM raza_bonificaciones rb " +
                        "JOIN atributos a ON rb.id_atributo = a.id_atributo " +
                        "WHERE rb.id_raza = ?";
        try (Connection con = Conexiondb.getConexion(); PreparedStatement ps = con.prepareStatement(sqlRaza)) {
            ps.setInt(1, p.getIdRaza());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String att = rs.getString("nombre");
                    int val = rs.getInt("valor");
                    //lee atributos y constitucion y sabiduria como tienen tilde hay que agragarlos a la condicion
                    for (int i = 0; i < 6; i++) {

                        if (
                                nombresStats[i].equalsIgnoreCase(att)
                                        || (i == 2 && att.equalsIgnoreCase("Constitucion"))
                                        || (i == 4 && att.equalsIgnoreCase("Sabiduria"))
                        ) {

                            bonosCalculados[i] += val;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Bonos de clase
        String sqlClase =
                "SELECT a.nombre, cb.valor " +
                        "FROM clase_bonificaciones cb " +
                        "JOIN atributos a ON cb.id_atributo = a.id_atributo " +
                        "WHERE cb.id_clase = ?";
        try (Connection con = Conexiondb.getConexion(); PreparedStatement ps = con.prepareStatement(sqlClase)) {
            ps.setInt(1, p.getIdClase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String att = rs.getString("nombre");
                    int val = rs.getInt("valor");
                    for (int i = 0; i < 6; i++) {
                        if (nombresStats[i].equalsIgnoreCase(att) || (i == 2 && att.equalsIgnoreCase("Constitucion"))) {
                            bonosCalculados[i] += val;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Aplicamos los modelos a los spinners
        for (int i = 0; i < 6; i++) {
            int baseConBonos = 10 + bonosCalculados[i];
            valoresBase[i] = baseConBonos;
            String signo = bonosCalculados[i] >= 0 ? "+" : "";
            lblBonos[i].setText(signo + bonosCalculados[i]);
            spinners[i].setModel(new SpinnerNumberModel(baseConBonos, baseConBonos, baseConBonos + 30, 1));
        }

        //importante para que los bonos se queden limpios
        revalidate();
        repaint();
    }

    private void evaluarCambioPuntos(int index) {
        if (bolsaPuntos == 0) return;

        int usados = 0;
        for (int i = 0; i < 6; i++) {
            usados += ((int) spinners[i].getValue() - valoresBase[i]);
        }
        int restantes = bolsaPuntos - usados;
        if (restantes < 0) {
            spinners[index].setValue((int) spinners[index].getValue() - 1);
        } else {
            lblBolsa.setText("Puntos Disponibles: " + restantes);
        }
    }

    private void habilitarSpinners(boolean b) {
        for (JSpinner s : spinners) {
            if (s != null) s.setEnabled(b);
        }
    }

    private void avanzarPantalla() {
        if (bolsaPuntos == 0) {
            JOptionPane.showMessageDialog(this, "Sin el rugido de los dados, no hay poder. ¡Lanza los dados, aventurero!");
            return;
        }

        int usados = 0;
        for (int i = 0; i < 6; i++) {
            usados += ((int) spinners[i].getValue() - valoresBase[i]);
        }

        if (bolsaPuntos - usados != 0) {
            JOptionPane.showMessageDialog(this, "El destino exige equilibrio. Debes consagrar cada punto otorgado por los dioses.");
            return;
        }

        p.setFuerza((int) spinners[0].getValue());
        p.setDestreza((int) spinners[1].getValue());
        p.setConstitucion((int) spinners[2].getValue());
        p.setInteligencia((int) spinners[3].getValue());
        p.setSabiduria((int) spinners[4].getValue());
        p.setCarisma((int) spinners[5].getValue());

        frame.completarPantalla("Stats");
        frame.cambiarPantalla("Habilidades");
    }
}

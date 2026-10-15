package utiles;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;

public class EstilosRPG {

    // -- COLORES PRINCIPALES --
    public static final Color COLOR_DORADO = new Color(212, 175, 55);
    public static final Color COLOR_ROJO_RPG = new Color(90, 30, 30);
    public static final Color WHITE = new Color(239, 237, 230);
    public static final Color COLOR_GRISACEO = new Color(202, 190, 182);
    public static final Color COLOR_TEXTO_OSCURO = new Color(40, 30, 20);
    public static final Color COLOR_FONDO_NAV = new Color(35, 35, 35);
    public static final Color COLOR_BORDE_MARRON = new Color(62, 39, 35);

    // Color para texto de botones deshabilitados
    public static final Color COLOR_BOTON_DESHABILITADO_TEXTO = COLOR_GRISACEO;

    //-- FUENTES --
    public static final Font FUENTE_TITULO_GRANDE = new Font("Georgia", Font.BOLD, 45);
    public static final Font FUENTE_TITULO_MEDIANO = new Font("Georgia", Font.BOLD, 20);
    public static final Font FUENTE_NORMAL = new Font("Georgia", Font.PLAIN, 18);
    public static final Font FUENTE_NEGRITA = new Font("Georgia", Font.BOLD, 16);
    public static final Font FUENTE_NEGRITA_GRANDE = new Font("Georgia", Font.BOLD, 25);

    //SIGUIENTE/CARGAR Y CREAR/SALIR
    public static final Font FUENTE_BOTON_GRANDE = new Font("Georgia", Font.BOLD, 16);

    // -- BORDES --
    public static final Border BORDE_DORADO = BorderFactory.createLineBorder(COLOR_DORADO, 2);
    public static final Border BORDE_MARRON = BorderFactory.createLineBorder(COLOR_BORDE_MARRON, 2);

    // -- RUTAS IMÁGENES --
    public static final String RUTA_FONDO_MENU = "/img/fondo.png";
    public static final String RUTA_FONDO_DEFAULT = "/img/default.png";

    // -- MÉTODOS DE ESTILO --
        // -- BOTONES ACCION, NAVETGACION, ESPECIALES --
    public static void estilizarBotonRPG(JButton boton) {
        boton.setBackground(COLOR_DORADO);
        boton.setForeground(COLOR_FONDO_NAV);
        boton.setBorder(BORDE_MARRON);
        //Elimina el molesto recuadro de puntos discontinuos que Swing dibuja por defecto alrededor del texto cuando haces clic en un botón.
        boton.setFocusPainted(false);
        boton.setFont(FUENTE_BOTON_GRANDE);
        //Cambia el puntero del ratón por una "mano interactiva" al pasar por encima, mejorando la experiencia del usuario (UX).
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Asegura que el color de fondo personalizado se pinte correctamente y no sea ignorado por el sistema operativo.
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);

        // Cambia el UI para controlar el color cuando está deshabilitado
        boton.setUI(new MetalButtonUI() {
            @Override
            protected Color getDisabledTextColor() {
                return COLOR_BOTON_DESHABILITADO_TEXTO;
            }
        });
    }

    public static void estilizarBotonNav(JButton boton) {
        boton.setBackground(COLOR_ROJO_RPG);
        boton.setForeground(COLOR_DORADO);
        boton.setFont(FUENTE_NEGRITA);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        boton.setUI(new MetalButtonUI() {
            @Override
            protected Color getDisabledTextColor() {
                return COLOR_BOTON_DESHABILITADO_TEXTO;
            }
        });
    }

    public static void estilizarBotonDorado(JButton boton) {
        boton.setBackground(COLOR_DORADO);
        boton.setForeground(COLOR_TEXTO_OSCURO);
        boton.setFont(FUENTE_NEGRITA);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BORDE_MARRON,
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        boton.setUI(new MetalButtonUI() {
            @Override
            protected Color getDisabledTextColor() {
                return COLOR_BOTON_DESHABILITADO_TEXTO;
            }
        });
    }

    //TITULO DE RAZA, CLASE Y NOMBRE DE PERSONAJE
    public static JLabel crearLabelFormulario(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(COLOR_DORADO);
        label.setFont(FUENTE_NEGRITA_GRANDE);
        return label;
    }

    // Métodos para hacer transparente FONDO COMPONENTES
    public static void hacerTransparente(JPanel panel) {
        panel.setOpaque(false);
    }

    public static void hacerTransparente(JCheckBox checkBox) {
        checkBox.setOpaque(false);
    }

    public static void hacerTransparente(JRadioButton radioButton) {
        radioButton.setOpaque(false);
    }

    public static void hacerTransparente(JLabel label) {
        label.setOpaque(false);
    }

    public static void hacerTransparente(JComponent componente) {
        componente.setOpaque(false);
    }

    //AREA DE TEXTO MULTILINEA
    public static JTextArea crearAreaTextoPergamino() {
        JTextArea area = new JTextArea();
        area.setFont(FUENTE_NORMAL);
        //Bloquea el componente para que el usuario pueda leer las descripciones pero no modificarlas.
        area.setEditable(false);

        area.setBackground(WHITE);
        //Activa el ajuste de línea automático. Si una palabra no cabe al final de la línea, la salta por completo a la siguiente en lugar de romperla a la mitad.
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        return area;
    }

    // * CLASE PANELMENU CON FONDO
    // * Sirve para crear un panel que dibuja automáticamente una imagen como fondo de pantalla.
    // * La imagen se estira o encoge sola para adaptarse siempre al tamaño de la ventana.
    // */
    public static class PanelConFondo extends JPanel {
        private Image imagenFondo;

        public PanelConFondo(String rutaImagen) {
            try {
                // Intenta cargar la imagen desde los recursos del proyecto
                imagenFondo = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
            } catch (Exception e) {
                imagenFondo = null;
            }
            setLayout(new BorderLayout());
        }

        // Metodo que dibuja los componentes visuales en la pantalla
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Borra lo que había antes para evitar errores visuales
            if (imagenFondo != null) {
                // Dibuja la imagen estirándola desde la esquina (0,0) hasta el ancho y alto total del panel
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    /**
     * PANEL CON FONDO DEFAULT RESTO VENTANAS
     * Hereda de 'PanelConFondo' para reutilizar su lógica, pero automatiza la carga de la imagen.
     * Evita tener que escribir la ruta del archivo cada vez que creas una ventana nueva.
     */
    public static class PanelConFondoDefault extends PanelConFondo {
        public PanelConFondoDefault() {
            super(RUTA_FONDO_DEFAULT);
        }
    }

    //ESTILO SPINNERS = ATRIBUTOS AÑADIR
    public static void estilizarSpinner(JSpinner spinner) {
        spinner.setBackground(COLOR_DORADO);
        spinner.setForeground(COLOR_FONDO_NAV);
        spinner.setFont(FUENTE_NEGRITA);

        // Personalizar el editor (campo de texto interno)
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setBackground(COLOR_FONDO_NAV);
        editor.getTextField().setForeground(COLOR_DORADO);
        editor.getTextField().setHorizontalAlignment(JTextField.CENTER);
        editor.getTextField().setFont(FUENTE_NEGRITA);

        // Personalizar los botones de flecha arriba/abajo
        for (Component comp : spinner.getComponents()) {
            if (comp instanceof JButton) {
                comp.setBackground(COLOR_ROJO_RPG);
                comp.setForeground(COLOR_FONDO_NAV);
            }
        }
    }

    //ESTILO SPINNERS DESHABILITADO
    public static void estilizarSpinnerDeshabilitado(JSpinner spinner) {
        spinner.setBackground(new Color(80, 70, 65));
        spinner.setForeground(COLOR_GRISACEO);
        spinner.setFont(FUENTE_NEGRITA);

        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setBackground(new Color(80, 70, 65));
        editor.getTextField().setForeground(COLOR_GRISACEO);
        editor.getTextField().setHorizontalAlignment(JTextField.CENTER);
        editor.getTextField().setFont(FUENTE_NEGRITA);
    }

    //DESPLEGABLES RAZA Y CLASE
    public static void estilizarComboBox(JComboBox<?> combo) {
        combo.setBackground(COLOR_FONDO_NAV);
        combo.setForeground(COLOR_DORADO);
        combo.setFont(FUENTE_NORMAL);
        combo.setBorder(BORDE_DORADO);

        // Controla el estado: si el elemento está seleccionado por el ratón, lo pinta con fondo dorado y texto oscuro; si no, mantiene el fondo oscuro y texto dorado.
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? COLOR_DORADO : COLOR_FONDO_NAV);
                setForeground(isSelected ? COLOR_TEXTO_OSCURO : COLOR_DORADO);
                setFont(FUENTE_NORMAL);
                return this;
            }
        });
    }
    //BARRA DE DESPLAZAMIENTO (SCROLLS)
    public static JScrollPane crearScrollEstilizado(JPanel panel) {
        JScrollPane sp = new JScrollPane(panel);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        Color fondo = COLOR_FONDO_NAV;
        Color linea = COLOR_DORADO;
        sp.setBackground(fondo);
        sp.getViewport().setBackground(fondo);
        JScrollBar vertical = sp.getVerticalScrollBar();
        //CAPTURA LA BARRA VERTICAL Y LA PINTA
        vertical.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            //COLORES
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = linea;
                this.trackColor = fondo;
            }
            // DESHABILITA LAS FECHAS DE LA BARRA
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(0, 0);
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(0, 0);
                    }
                };
            }
        });

        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return sp;
    }
}
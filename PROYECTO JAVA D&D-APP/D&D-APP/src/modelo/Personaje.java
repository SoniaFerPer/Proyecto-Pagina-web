package modelo;

import database.Conexiondb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Personaje {
    /**
     * Clase que representa un personaje del juego.
     * Se encarga de:
     * - Almacenar datos del personaje.
     * - Cargar información desde la base de datos.
     * - Guardar personajes nuevos.
     * - Gestionar atributos, raza, clase e imágenes.
     */

    private int idPersonaje;
    private String nombre;
    private int idRaza;
    private int idClase;
    private int experiencia = 0;
    private int vidaMaxima;
    private int manaMaximo;
    private int oro = 120; // oro base fijo RPG
    private String imagen;

    // ATRIBUTOS
    private int fuerza;
    private int destreza;
    private int constitucion;
    private int inteligencia;
    private int sabiduria;
    private int carisma;

    // TEXTOS / UI
    private String inventario;
    private String habilidadesTxt;

    private String nombreRazaTxt;
    private String nombreClaseTxt;

    // IMÁGENES
    private String imagenRaza;
    private String imagenClase;

    // HABILIDADES REALES
    private List<String> habilidades = new ArrayList<>();

    public Personaje() {}

    /**
     * Recalcula la vida y el maná máximos
     * a partir de los atributos actuales.
     */
    public void recalcularStats() {
        this.vidaMaxima = 100 + (constitucion * 10);
        this.manaMaximo = 50 + (inteligencia * 8);
    }

    /**
     * CARGA DE ATRIBUTOS:
     * Obtiene desde la base de datos todos los
     * atributos del personaje y los asigna
     * a sus variables correspondientes.
     */
    public void cargarAtributosDesdeBD() {

        String sql = """
            SELECT a.nombre, pa.valor
            FROM personaje_atributos pa
            JOIN atributos a ON pa.id_atributo = a.id_atributo
            WHERE pa.id_personaje = ?
        """;

        try (Connection con = Conexiondb.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, this.idPersonaje);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Indica qué personaje se quiere consultar
                String nombre = rs.getString("nombre").trim().toUpperCase();
                int valor = rs.getInt("valor");
                // Asigna el valor al atributo correcto
                switch (nombre) {
                    case "FUERZA" -> fuerza = valor;
                    case "DESTREZA" -> destreza = valor;
                    case "CONSTITUCIÓN" -> constitucion = valor;
                    case "INTELIGENCIA" -> inteligencia = valor;
                    case "SABIDURÍA" -> sabiduria = valor;
                    case "CARISMA" -> carisma = valor;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * Convierte un nombre de atributo a un formato uniforme:
//     * - Mayúsculas
//     * - Sin acentos
//     * Esto evita errores al comparar cadenas.
//     */
//    private static String normalizarAtributo(String nombre) {
//        if (nombre == null) {
//            return "";
//        }
//
//        return nombre
//                .trim()
//                .toUpperCase()
//                .replace("Ó", "O")
//                .replace("Í", "I")
//                .replace("Ú", "U")
//                .replace("Á", "A")
//                .replace("É", "E");
//    }

    /**
     * Carga un personaje completo desde la BD
     * usando su identificador.
     *
     * @param idPersonaje ID del personaje.
     * @return Objeto Personaje o null si no existe.
     */
    public static Personaje cargarPorId(int idPersonaje) {
        String sql = """
            SELECT id_personaje, nombre, id_raza, id_clase, nivel, experiencia,
                   vida_actual, mana_actual, oro, inventario, habilidades
            FROM personajes
            WHERE id_personaje = ?
        """;

        try (Connection con = Conexiondb.getConexion()) {
            // Comprueba que exista la columna habilidades
            asegurarColumnaHabilidades(con);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idPersonaje);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Personaje p = new Personaje();
                    // Rellena el objeto con los datos recuperados
                    p.setIdPersonaje(rs.getInt("id_personaje"));
                    p.setNombre(rs.getString("nombre"));
                    p.setIdRaza(rs.getInt("id_raza"));
                    p.setIdClase(rs.getInt("id_clase"));
                    p.setExperiencia(rs.getInt("experiencia"));
                    p.setOro(rs.getInt("oro"));
                    p.setInventario(rs.getString("inventario"));
                    p.setHabilidadesTxt(rs.getString("habilidades"));
                    // Carga información adicional
                    p.cargarAtributosDesdeBD();
                    p.cargarRazaClase();
                    p.recalcularStats();

                    return p;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Comprueba si existe la columna "habilidades".
     * Si no existe, la crea automáticamente.
     */
    public static void asegurarColumnaHabilidades(Connection con) throws SQLException {
        DatabaseMetaData meta = con.getMetaData();

        try (ResultSet rs = meta.getColumns(null, null, "personajes", "habilidades")) {
            if (rs.next()) {
                return;
            }
        }

        try (Statement st = con.createStatement()) {
            st.executeUpdate("ALTER TABLE personajes ADD COLUMN habilidades TEXT");
        }
    }

    /**
     * RAZA + CLASE + IMAGEN
     * Obtiene desde la BD:
     * - Nombre + IMAGEN de raza
     * - Nombre + IMAGEN de clase
     */
    public void cargarRazaClase() {

        String sqlRaza = "SELECT nombre, imagen FROM razas WHERE id_raza = ?";
        String sqlClase = "SELECT nombre, imagen FROM clases WHERE id_clase = ?";

        try (Connection con = Conexiondb.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlRaza)) {

            ps.setInt(1, idRaza);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nombreRazaTxt = rs.getString("nombre");
                imagenRaza = rs.getString("imagen");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection con = Conexiondb.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlClase)) {

            ps.setInt(1, idClase);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nombreClaseTxt = rs.getString("nombre");
                imagenClase = rs.getString("imagen");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  GUARDADO PERSONAJE EN BD
     * Inserta un personaje nuevo en la BD.
     * @return true si se guardó correctamente.
     */
    public boolean guardarTodo() {

        String sql = """
            INSERT INTO personajes
            (nombre, id_raza, id_clase, experiencia,
             vida_actual, mana_actual, oro, inventario, habilidades)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = Conexiondb.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            asegurarColumnaHabilidades(con);
            ps.setString(1, nombre);
            ps.setInt(2, idRaza);
            ps.setInt(3, idClase);
            ps.setInt(4, experiencia);
            ps.setInt(5, vidaMaxima);
            ps.setInt(6, manaMaximo);
            ps.setInt(7, oro);
            ps.setString(8, inventario);
            ps.setString(9, habilidadesTxt);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // HELPERS INVENTARIO
    // =========================
    public void setInventario(String inventario) {
        this.inventario = inventario;
    }

    // =========================
    // GETTERS / SETTERS
    // =========================

    public int getIdPersonaje() { return idPersonaje; }
    public void setIdPersonaje(int idPersonaje) { this.idPersonaje = idPersonaje; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getIdRaza() { return idRaza; }
    public void setIdRaza(int idRaza) { this.idRaza = idRaza; }

    public int getIdClase() { return idClase; }
    public void setIdClase(int idClase) { this.idClase = idClase; }

    public int getExperiencia() { return experiencia; }
    public void setExperiencia(int experiencia) { this.experiencia = experiencia; }

    public int getVidaMaxima() { return vidaMaxima; }
    public int getManaMaximo() { return manaMaximo; }

    public int getOro() { return oro; }
    public void setOro(int oro) { this.oro = oro; }

    public int getFuerza() { return fuerza; }
    public int getDestreza() { return destreza; }
    public int getConstitucion() { return constitucion; }
    public int getInteligencia() { return inteligencia; }
    public int getSabiduria() { return sabiduria; }
    public int getCarisma() { return carisma; }
    public void setFuerza(int fuerza) {this.fuerza = fuerza;}
    public void setDestreza(int destreza) {this.destreza = destreza; }
    public void setConstitucion(int constitucion) {this.constitucion = constitucion;}
    public void setInteligencia(int inteligencia) {this.inteligencia = inteligencia;}
    public void setSabiduria(int sabiduria) { this.sabiduria = sabiduria;}
    public void setCarisma(int carisma) {this.carisma = carisma;}
    public String getInventario() { return inventario; }

    public String getHabilidadesTxt() { return habilidadesTxt; }
    public void setHabilidadesTxt(String habilidadesTxt) { this.habilidadesTxt = habilidadesTxt; }

    public String getNombreRazaTxt() { return nombreRazaTxt; }
    public String getNombreClaseTxt() { return nombreClaseTxt; }

    public String getImagenRaza() { return imagenRaza; }
    public String getImagenClase() { return imagenClase; }
}

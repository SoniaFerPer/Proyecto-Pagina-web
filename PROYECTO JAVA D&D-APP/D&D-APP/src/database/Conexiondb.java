package database;

import java.sql.*;

public class Conexiondb {
        private static final String URL ="jdbc:mysql://localhost:33060/dnd_app?useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true";
        private static final String USER ="root";
        private static final String PASS ="admin";

        public static Connection getConexion() {
            Connection con = null;
                try {
                    con = DriverManager.getConnection(URL, USER, PASS);

                } catch (SQLException e) {
                    System.out.println("Driver de MySQL no encontrado en el proyecto." + e.getMessage());
                }

            return con;
        }
}







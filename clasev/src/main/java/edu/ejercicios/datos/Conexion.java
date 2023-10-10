package edu.ejercicios.datos;

import java.sql.*;

public class Conexion {
    //se crea la conexion a la base de datos
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/p2a?useSSL=false&serverTimezone=UTC", "root", "Alay2003@");
    }
    //cierra la conexion al resultset
    public static void close(ResultSet rs){
        try {
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
    //cierra la conexion al statement
    public static void close(PreparedStatement stmt){
        try {
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }

    //cierra la conexion a la base de datos
    public static void close(Connection conn){
        try {
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }

}
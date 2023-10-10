package edu.ejercicios.datos;

import edu.ejercicios.domain.*;
import java.security.*;
import java.sql.*;

public class usuariodao {
    private Connection conexiontrans;

    public usuariodao() {
        // Constructor en blanco
    }

    // Consultas SQL predefinidas
    private static final String SQLINSERT = "INSERT INTO usuario(username, password) VALUES (?, ?)";
    private static final String SQLVALUSER = "SELECT * FROM usuario WHERE username = ? AND password = ?";

    public usuariodao(Connection conexiontrans) {
        this.conexiontrans = conexiontrans;
    }

    // Método para insertar un usuario en la base de datos
    public int insert(usuario usuario) throws SQLException, NoSuchAlgorithmException {
        Connection cone = null;
        PreparedStatement dec = null;
        int filas = 0;

        try {
            // Obtenemos una conexión si no se proporciona una transacción
            cone = this.conexiontrans != null ? this.conexiontrans : Conexion.getConnection();

            // Preparar la consulta SQL de inserción
            dec = cone.prepareStatement(SQLINSERT);
            dec.setString(1, usuario.getNombreu());
            dec.setString(2, ocultarpass(usuario.getPass())); // Encriptamos la contraseña con el método ya creado

            System.out.println("Ejecutando el query: " + SQLINSERT);
            filas = dec.executeUpdate(); // Ejecutar la inserción
            System.out.println("Registros afectados: " + filas);

        } finally {
            // Cerramos los recursos
            Conexion.close(dec);
            if (this.conexiontrans == null) {
                Conexion.close(cone);
            }
        }

        return filas;
    }

    // Método para validar un usuario en la base de datos
    public boolean validar(String nombreu, String Pass) throws SQLException, NoSuchAlgorithmException {
        Connection cone = null;
        PreparedStatement dec = null;
        ResultSet rs = null;
        boolean valido = false;

        try {
            // Obtenemos una conexión si no se proporciona una transacción
            cone = this.conexiontrans != null ? this.conexiontrans : Conexion.getConnection();

            // Preparar la consulta SQL para validar al usuario
            dec = cone.prepareStatement(SQLVALUSER);
            dec.setString(1, nombreu);
            dec.setString(2, ocultarpass(Pass));

            // Ejecutamos la consulta y verificamos si existe el usuario
            rs = dec.executeQuery();
            valido = rs.next();

        } finally {
            // Cerramos los recursos
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (dec != null) {
                    dec.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (this.conexiontrans == null) {
                try {
                    if (cone != null) {
                        cone.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return valido;
    }

    // Método para encriptar la contraseña utilizando el algoritmo MD5
    private String ocultarpass(String pass) throws NoSuchAlgorithmException {
        try {
            MessageDigest mess = MessageDigest.getInstance("MD5");
            mess.update(pass.getBytes());
            byte[] digest = mess.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return pass;
        }
    }
}
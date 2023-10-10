package edu.ejercicios.test;

import edu.ejercicios.datos.*;
import edu.ejercicios.domain.Persona;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ManejoPersonas {

    // Este método se utiliza para realizar operaciones con la base de datos.
    public static void con() {
        Connection conexion = null;
        try {
            conexion = Conexion.getConnection(); // Obtenemos una conexión a la base de datos.
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false); // Deshabilitamos el modo de autocommit para gestionar transacciones.
            }
            PersonaJDBC personaJDBC = new PersonaJDBC(conexion); // Creamos un objeto para manejar operaciones con personas.

            // Recorremos y mostramos las personas en la base de datos.
            for (Persona persona : personaJDBC.select()) {
                System.out.println("Persona = " + persona);
            }

            conexion.commit(); // Realizamos un commit para confirmar los cambios en la base de datos.
            System.out.println("Se ha hecho Commit de la transaccion");

        } catch (SQLException var6) {
            var6.printStackTrace(System.out);
            System.out.println("Entramos al rollback");

            try {
                conexion.rollback(); // Si ocurre un error, realizamos un rollback para deshacer los cambios.
            } catch (SQLException var5) {
                var5.printStackTrace(System.out);
            }
        }
    }

    public static void main(String[] args) {
        Connection conexion = null;
        con(); // Llamamos al método "con" para mostrar las personas antes de realizar una inserción.
        try {
            conexion = Conexion.getConnection(); // Obtenemos una conexión a la base de datos.
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false); // Deshabilitamos el modo de autocommit para gestionar transacciones.
            }

            PersonaJDBC personaJdbc = new PersonaJDBC(conexion); // Creamos un objeto para manejar operaciones con personas.

            // Creamos una nueva persona y la insertamos en la base de datos.
            Persona nuevaPersona = new Persona();
            nuevaPersona.setNombre("Angel");
            nuevaPersona.setApellido("Medina");
            personaJdbc.insert(nuevaPersona);

            conexion.commit(); // Realizamos un commit para confirmar los cambios en la base de datos.
            System.out.println("Se ha hecho commit de la transaccion");

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Entramos al rollback");
            try {
                if (conexion != null) {
                    conexion.rollback(); // Si ocurre un error, realizamos un rollback para deshacer los cambios.
                }
            } catch (SQLException ex1) {
                ex1.printStackTrace(System.out);
            }
        } finally {
            Conexion.close(conexion); // Cerramos la conexión a la base de datos.
        }
    }
}

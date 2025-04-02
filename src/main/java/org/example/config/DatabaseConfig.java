package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:sqlite:database.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQLite não carregado!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initDatabase() {
        String sql = "DROP TABLE IF EXISTS users;\n" + // Linha nova para limpeza
                "CREATE TABLE IF NOT EXISTS users (\n" +
                "id INTEGER PRIMARY KEY,\n" +
                "name TEXT NOT NULL,\n" +
                "email TEXT UNIQUE NOT NULL,\n" +
                "password TEXT NOT NULL)";

        try (Connection conn = getConnection()) {
            conn.createStatement().executeUpdate(sql);
            System.out.println("✅ Tabela 'users' criada com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao criar tabela", e);
        }
    }
}
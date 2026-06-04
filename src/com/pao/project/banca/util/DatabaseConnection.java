package com.pao.project.banca.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Collectors;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("Fisierul db.properties nu exista!");
            }
            properties.load(inputStream);

            String url = properties.getProperty("db.url");
            this.connection = DriverManager.getConnection(url);
            System.out.println("Conexiunea la baza de date a fost initializata");
            initializeazaBazaDeDateDacaEsteGoala();

        } catch (IOException e) {
            throw new RuntimeException("Eroare la citirea fisierului de proprietati: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la conectarea la baza de date: " + e.getMessage(), e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        try {
            if (instance == null || instance.getConnection().isClosed()) {
                instance = new DatabaseConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la verificarea starii conexiunii: " + e.getMessage());
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initializeazaBazaDeDateDacaEsteGoala() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet rs = metaData.getTables(null, null, "clienti", null)) {
                if (rs.next()) {
                    System.out.println("Structura bazei de date exista deja. Datele sunt pastrate.");
                    return;
                }
            }

            System.out.println("Baza de date este goala. Se executa schema.sql...");
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("schema.sql")) {
                if (is == null) {
                    throw new RuntimeException("Fisierul schema.sql nu a fost gasit in resources!");
                }

                String script;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    script = reader.lines().collect(Collectors.joining("\n"));
                }
                String[] comenzi = script.split(";");
                try (Statement stmt = connection.createStatement()) {
                    for (String comanda : comenzi) {
                        String comandaCurata = comanda.trim();
                        if (!comandaCurata.isEmpty()) {
                            stmt.execute(comandaCurata);
                        }
                    }
                    System.out.println("Schema bazei de date a fost initializata cu succes!");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Eroare la initializarea automata a tabelelor: " + e.getMessage(), e);
        }
    }
}
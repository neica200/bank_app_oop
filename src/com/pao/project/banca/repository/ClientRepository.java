package com.pao.project.banca.repository;

import com.pao.project.banca.model.Client;
import com.pao.project.banca.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, String> {

    private final Connection connection;

    public ClientRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Client entity) {
        String sql = "INSERT INTO clienti (id, nume, prenume, email) VALUES (?, ?, ?, ?)";

        // try-with-resources asigura inchiderea automata a statement-ului
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getId());
            ps.setString(2, entity.getNume());
            ps.setString(3, entity.getPrenume());
            ps.setString(4, entity.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea clientului: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Client> findById(String id) {
        String sql = "SELECT * FROM clienti WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client(
                            rs.getString("nume"),
                            rs.getString("prenume"),
                            rs.getString("email")
                    );
                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea clientului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        List<Client> clienti = new ArrayList<>();
        String sql = "SELECT * FROM clienti";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clienti.add(new Client(
                        rs.getString("nume"),
                        rs.getString("prenume"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea clientilor: " + e.getMessage(), e);
        }
        return clienti;
    }

    @Override
    public void update(Client entity) {
        String sql = "UPDATE clienti SET nume = ?, prenume = ?, email = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getPrenume());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea clientului: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM clienti WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea clientului: " + e.getMessage(), e);
        }
    }
}
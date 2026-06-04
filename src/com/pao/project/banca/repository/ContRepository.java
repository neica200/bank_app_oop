package com.pao.project.banca.repository;

import com.pao.project.banca.model.*;
import com.pao.project.banca.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContRepository implements Repository<Cont, IBAN> {

    private final Connection connection;

    public ContRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Cont entity) {
        String sql = "INSERT INTO conturi (iban, tip, sold, titular_id, limita_overdraft, rata_dobanda) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getIBAN().toString());
            ps.setDouble(3, entity.getSold());
            ps.setString(4, entity.getTitular().getId());

            if (entity instanceof ContCurent) {
                ps.setString(2, "CURENT");
                ps.setDouble(5, ((ContCurent) entity).getLimitaOverdraft());
                ps.setNull(6, Types.DOUBLE);
            } else if (entity instanceof ContEconomii) {
                ps.setString(2, "ECONOMII");
                ps.setNull(5, Types.DOUBLE);
                ps.setDouble(6, ((ContEconomii) entity).getRataDobanda());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea contului: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cont> findById(IBAN iban) {
        String sql = "SELECT c.*, cl.nume, cl.prenume, cl.email FROM conturi c " +
                "JOIN clienti cl ON c.titular_id = cl.id WHERE c.iban = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, iban.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Client titular = new Client(
                            rs.getString("titular_id"),
                            rs.getString("nume"),
                            rs.getString("prenume"),
                            rs.getString("email")
                    );

                    String tip = rs.getString("tip");
                    Cont cont = null;

                    if ("CURENT".equals(tip)) {
                        cont = new ContCurent(iban, titular, rs.getDouble("limita_overdraft"));
                    } else if ("ECONOMII".equals(tip)) {
                        cont = new ContEconomii(iban, titular, rs.getDouble("rata_dobanda"));
                    }

                    if (cont != null) {
                        cont.depune(rs.getDouble("sold"));
                        return Optional.of(cont);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea contului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Cont> findAll() {
        List<Cont> conturi = new ArrayList<>();
        String sql = "SELECT c.*, cl.nume, cl.prenume, cl.email FROM conturi c " +
                "JOIN clienti cl ON c.titular_id = cl.id";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Client titular = new Client(
                        rs.getString("titular_id"),
                        rs.getString("nume"),
                        rs.getString("prenume"),
                        rs.getString("email")
                );

                IBAN iban = new IBAN(rs.getString("iban"));
                String tip = rs.getString("tip");
                Cont cont = null;

                if ("CURENT".equals(tip)) {
                    cont = new ContCurent(iban, titular, rs.getDouble("limita_overdraft"));
                } else if ("ECONOMII".equals(tip)) {
                    cont = new ContEconomii(iban, titular, rs.getDouble("rata_dobanda"));
                }

                if (cont != null) {
                    cont.depune(rs.getDouble("sold"));
                    conturi.add(cont);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea conturilor: " + e.getMessage(), e);
        }
        return conturi;
    }

    @Override
    public void update(Cont entity) {
        String sql = "UPDATE conturi SET sold = ? WHERE iban = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, entity.getSold());
            ps.setString(2, entity.getIBAN().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea contului: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(IBAN iban) {
        String sql = "DELETE FROM conturi WHERE iban = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, iban.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea contului: " + e.getMessage(), e);
        }
    }
}
package com.pao.project.banca.repository;

import com.pao.project.banca.model.Card;
import com.pao.project.banca.model.IBAN;
import com.pao.project.banca.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepository implements Repository<Card, String> {

    private final Connection connection;

    public CardRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Card entity) {
        String sql = "INSERT INTO carduri (numar_card, pin, iban_asociat, este_blocat, limita_zilnica, data_expirarii) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getNumarCard());
            ps.setString(2, entity.getPin());
            ps.setString(3, entity.getIbanAsociat().toString());
            ps.setBoolean(4, entity.isEsteBlocat());
            ps.setDouble(5, entity.getLimitaZilnica());
            ps.setString(6, entity.getDataExpirarii().toString()); // SQLite stocheaza datele ca text (YYYY-MM-DD)
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea cardului: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Card> findById(String numarCard) {
        String sql = "SELECT * FROM carduri WHERE numar_card = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, numarCard);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    IBAN iban = new IBAN(rs.getString("iban_asociat"));

                    // Folosim noul constructor complet
                    Card card = new Card(
                            rs.getString("numar_card"),
                            rs.getString("pin"),
                            iban,
                            rs.getBoolean("este_blocat"),
                            rs.getDouble("limita_zilnica"),
                            LocalDate.parse(rs.getString("data_expirarii"))
                    );

                    return Optional.of(card);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea cardului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Card> findAll() {
        List<Card> carduri = new ArrayList<>();
        String sql = "SELECT * FROM carduri";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                IBAN iban = new IBAN(rs.getString("iban_asociat"));

                // Folosim noul constructor complet
                Card card = new Card(
                        rs.getString("numar_card"),
                        rs.getString("pin"),
                        iban,
                        rs.getBoolean("este_blocat"),
                        rs.getDouble("limita_zilnica"),
                        LocalDate.parse(rs.getString("data_expirarii"))
                );

                carduri.add(card);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea cardurilor: " + e.getMessage(), e);
        }
        return carduri;
    }

    @Override
    public void update(Card entity) {
        String sql = "UPDATE carduri SET pin = ?, este_blocat = ?, limita_zilnica = ? WHERE numar_card = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getPin());
            ps.setBoolean(2, entity.isEsteBlocat());
            ps.setDouble(3, entity.getLimitaZilnica());
            ps.setString(4, entity.getNumarCard());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea cardului: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String numarCard) {
        String sql = "DELETE FROM carduri WHERE numar_card = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, numarCard);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea cardului: " + e.getMessage(), e);
        }
    }
}
package com.pao.project.banca.repository;

import com.pao.project.banca.model.*;
import com.pao.project.banca.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TranzactieRepository implements Repository<Tranzactie, String> {

    private final Connection connection;

    public TranzactieRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Tranzactie entity) {
        String sql = "INSERT INTO tranzactii (id, tip, suma, valuta, data_executie, iban_sursa, iban_destinatie, comerciant, swift, comision) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getId());
            ps.setDouble(3, entity.getSuma());
            ps.setString(4, entity.getCurrency());
            ps.setString(5, entity.getTimestamp().toString());

            ps.setString(6, "RO-SURSA-GENERIC");

            if (entity instanceof Plata) {
                ps.setString(2, "PLATA");
                ps.setNull(7, Types.VARCHAR);
                ps.setString(8, ((Plata) entity).getComerciant());
                ps.setNull(9, Types.VARCHAR);
                ps.setNull(10, Types.DOUBLE);
            } else if (entity instanceof TransferIntraBancar) {
                ps.setString(2, "INTRA");
                ps.setString(7, ((TransferIntraBancar) entity).getReceiverIBAN().toString()); // Uniformizat cu getter-ul din clasa ta
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.VARCHAR);
                ps.setNull(10, Types.DOUBLE);
            } else if (entity instanceof TransferInterBancar) {
                ps.setString(2, "INTER");
                ps.setString(7, ((TransferInterBancar) entity).getReceiverIBAN().toString()); // Uniformizat cu getter-ul din clasa ta
                ps.setNull(8, Types.VARCHAR);
                ps.setString(9, ((TransferInterBancar) entity).getSwiftCode());
                ps.setDouble(10, ((TransferInterBancar) entity).getComision());
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea tranzactiei: " + e.getMessage(), e);
        }
    }

    //interogare join
    public List<Tranzactie> findAllByIban(IBAN iban) {
        List<Tranzactie> tranzactii = new ArrayList<>();
        String sql = "SELECT t.* FROM tranzactii t " +
                "JOIN conturi c ON t.iban_sursa = c.iban WHERE t.iban_sursa = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, iban.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    double suma = rs.getDouble("suma");
                    String currency = rs.getString("valuta");
                    LocalDateTime timestamp = LocalDateTime.parse(rs.getString("data_executie"));
                    String tip = rs.getString("tip");

                    Tranzactie t = null;
                    if ("PLATA".equals(tip)) {
                        t = new Plata(id, suma, currency, timestamp, rs.getString("comerciant"));
                    } else if ("INTRA".equals(tip)) {
                        t = new TransferIntraBancar(id, suma, currency, timestamp, new IBAN(rs.getString("iban_destinatie")));
                    } else if ("INTER".equals(tip)) {
                        t = new TransferInterBancar(id, suma, currency, timestamp, new IBAN(rs.getString("iban_destinatie")), rs.getString("swift"), rs.getDouble("comision"));
                    }

                    if (t != null) tranzactii.add(t);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la extragerea istoricului prin JOIN: " + e.getMessage(), e);
        }
        return tranzactii;
    }


    public void afiseazaToatePlatileCuDetaliiClient() {
        String sql = "SELECT t.id, t.suma, t.comerciant, cl.nume, cl.prenume FROM tranzactii t " +
                "JOIN conturi c ON t.iban_sursa = c.iban " +
                "JOIN clienti cl ON c.titular_id = cl.id WHERE t.tip = 'PLATA'";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("\nRAPORT PLATI CLIENTI");
            while (rs.next()) {
                System.out.println("Client: " + rs.getString("nume") + " " + rs.getString("prenume") +
                        " | Suma: " + rs.getDouble("suma") + " RON la comerciantul: " + rs.getString("comerciant"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea avansata JOIN : " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Tranzactie> findById(String id) { return Optional.empty(); }

    @Override
    public List<Tranzactie> findAll() { return new ArrayList<>(); }

    @Override
    public void update(Tranzactie entity) {}

    @Override
    public void delete(String id) {}
}
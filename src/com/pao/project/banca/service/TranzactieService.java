package com.pao.project.banca.service;

import com.pao.project.banca.model.*;
import com.pao.project.banca.exception.*;
import com.pao.project.banca.repository.*;
import com.pao.project.banca.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TranzactieService {
    private static TranzactieService instance;


    private final ContService contService = ContService.getInstance();
    private final CardService cardService = CardService.getInstance();

    private final ContRepository contRepository = new ContRepository();
    private final TranzactieRepository tranzactieRepository = new TranzactieRepository();

    private TranzactieService() {}

    public static synchronized TranzactieService getInstance() {
        if (instance == null) {
            instance = new TranzactieService();
        }
        return instance;
    }


    //tranzactie jdbc
    public void executaTransfer(IBAN sursaIban, IBAN destIban, double suma, String swift) throws Exception {
        AuditService.getInstance().logActiune("executa_transfer_bancar");

        Connection connection = DatabaseConnection.getInstance().getConnection();
        Cont sursa = contService.gasesteCont(sursaIban);
        Tranzactie t;

        try {
            Cont destIntern = contService.gasesteCont(destIban);
            sursa.retrage(suma);
            destIntern.depune(suma);
            t = new TransferIntraBancar(suma, "RON", destIban);
            destIntern.adaugaTranzactie(t);

            try {
                connection.setAutoCommit(false);
                contRepository.update(sursa);
                contRepository.update(destIntern);
                tranzactieRepository.save(t);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (EntitateNegasitaException e) {
            if (swift == null || swift.isEmpty()) {
                throw new Exception("Codul SWIFT este obligatoriu pentru transferuri externe!");
            }
            double comision = 5.0;
            sursa.retrage(suma + comision);
            t = new TransferInterBancar(suma, "RON", destIban, swift, comision);

            // Pentru transfer extern, actualizare doar contul sursa
            try {
                connection.setAutoCommit(false);
                contRepository.update(sursa);
                tranzactieRepository.save(t);
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        }

        sursa.adaugaTranzactie(t);
        t.execute();
    }

    public void platesteCuCard(String numarCard, double suma, String comerciant) throws Exception {
        AuditService.getInstance().logActiune("plata_cu_card");

        Card card = cardService.gasesteCard(numarCard);

        if (card.isEsteBlocat()) {
            throw new Exception("Tranzactie refuzata: Cardul " + numarCard + " este blocat!");
        }

        Cont cont = contService.gasesteCont(card.getIbanAsociat());

        cont.retrage(suma);

        Tranzactie p = new Plata(suma, "RON", comerciant);
        cont.adaugaTranzactie(p);

        Connection connection = DatabaseConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            contRepository.update(cont);
            tranzactieRepository.save(p);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }

        p.execute();
    }

    public void proceseazaDobanda(IBAN iban) throws Exception {
        AuditService.getInstance().logActiune("proceseaza_dobanda");
        Cont cont = contService.gasesteCont(iban);
        if (cont instanceof ContEconomii) {
            ((ContEconomii) cont).aplicaDobanda();

            contRepository.update(cont);
        } else {
            throw new Exception("Acest tip de cont nu suporta dobanda!");
        }
    }

    //interogare avansata cu Join
    public List<Tranzactie> obtineIstoricCont(IBAN iban) {
        AuditService.getInstance().logActiune("vizualizare_istoric_db");
        return tranzactieRepository.findAllByIban(iban);
    }

    //3 tabele join
    public void afiseazaRaportPlati() {
        AuditService.getInstance().logActiune("raport_global_plati");
        tranzactieRepository.afiseazaToatePlatileCuDetaliiClient();
    }
}
package com.pao.project.banca.service;
import com.pao.project.banca.model.*;
import com.pao.project.banca.exception.*;

public class TranzactieService {
    private static TranzactieService instance;
    private final ContService contService = ContService.getInstance();
    private final CardService cardService = CardService.getInstance();

    private TranzactieService() {}

    public static TranzactieService getInstance() {
        if (instance == null) {
            instance = new TranzactieService();
        }
        return instance;
    }

    public void executaTransfer(IBAN sursaIban, IBAN destIban, double suma, String swift) throws Exception {
        Cont sursa = contService.gasesteCont(sursaIban);
        Tranzactie t;

        try {
            Cont destIntern = contService.gasesteCont(destIban);
            sursa.retrage(suma);
            destIntern.depune(suma);
            t = new TransferIntraBancar(suma, "RON", destIban);
            destIntern.adaugaTranzactie(t);
        }catch (EntitateNegasitaException e){
            if (swift == null || swift.isEmpty()) {
                throw new Exception("Codul SWIFT este obligatoriu pentru transferuri externe!");
            }
            double comision = 5.0;
            sursa.retrage(suma + comision);
            t = new TransferInterBancar(suma, "RON", destIban, swift, comision);
        }

        sursa.adaugaTranzactie(t);
        t.execute();


    }

    public void platesteCuCard(String numarCard, double suma, String comerciant) throws Exception {
        // 1. Luam cardul din CardService
        Card card = cardService.gasesteCard(numarCard);

        if (card.isEsteBlocat()) {
            throw new Exception("Tranzactie refuzata: Cardul " + numarCard + " este blocat!");
        }

        // 2. Luam contul din ContService folosind legatura card -> IBAN
        Cont cont = contService.gasesteCont(card.getIbanAsociat());

        // 3. Executam plata
        cont.retrage(suma);

        // 4. Cream obiectul Plata (Nivel 1 in ierarhie)
        Tranzactie p = new Plata(suma, "RON", comerciant);
        cont.adaugaTranzactie(p);
        p.execute();
    }

    public void proceseazaDobanda(IBAN iban) throws Exception {
        Cont cont = contService.gasesteCont(iban);

        if (cont instanceof ContEconomii) {
            ((ContEconomii) cont).aplicaDobanda();
        } else {
            throw new Exception("Acest tip de cont nu suporta dobanda!");
        }
    }
}

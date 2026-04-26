package com.pao.project.banca.service;
import com.pao.project.banca.model.*;
import com.pao.project.banca.exception.*;

import java.util.*;

public class ContService {
    private static ContService instance;
    private Map<IBAN, Cont> conturi;


    private ContService() {
        conturi = new HashMap<>();
    }

    public static ContService getInstance() {
        if (instance == null) {
            instance = new ContService();
        }
        return instance;
    }

    public void adaugaCont(Cont cont) {
        this.conturi.put(cont.getIBAN(), cont);
    }

    public void stergeCont(IBAN iban) {
        this.conturi.remove(iban);
        System.out.println("Contul " + iban + " a fost sters cu succes.");
    }

    public void deschideContCurent(Client titular, double overdraft) {
        IBAN iban = new IBAN("RO" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        Cont c = new ContCurent(iban, titular, overdraft);
        conturi.put(iban, c);
    }

    public void deschideContEconomii(Client titular, double dobanda) {
        IBAN iban = new IBAN("RO" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        Cont c = new ContEconomii(iban, titular, dobanda);
        conturi.put(iban, c);
    }


    public Cont gasesteCont(IBAN iban) throws EntitateNegasitaException {
        Cont cont = conturi.get(iban);
        if (cont == null) {
            throw new EntitateNegasitaException("Contul cu IBAN-ul " + iban + " nu exista!");
        }
        return cont;
    }

    public List<Cont> listeazaToateConturile() {
        return new ArrayList<>(conturi.values());
    }
}

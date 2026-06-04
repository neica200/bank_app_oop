package com.pao.project.banca.service;

import com.pao.project.banca.model.*;
import com.pao.project.banca.exception.*;
import com.pao.project.banca.repository.ContRepository;

import java.util.*;

public class ContService {
    private static ContService instance;
    private final ContRepository contRepository = new ContRepository();

    private ContService() {}

    public static synchronized ContService getInstance() {
        if (instance == null) {
            instance = new ContService();
        }
        return instance;
    }

    public void adaugaCont(Cont cont) {
        AuditService.getInstance().logActiune("adauga_cont_existent");

        contRepository.save(cont);
    }

    public void stergeCont(IBAN iban) throws EntitateNegasitaException {
        AuditService.getInstance().logActiune("sterge_cont");
        gasesteCont(iban);
        contRepository.delete(iban);
        System.out.println("Contul " + iban + " a fost sters cu succes din DB.");
    }

    public ContCurent deschideContCurent(Client titular, double overdraft) {
        AuditService.getInstance().logActiune("deschide_cont_curent");
        IBAN iban = new IBAN("RO" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        ContCurent c = new ContCurent(iban, titular, overdraft);
        contRepository.save(c);
        return c;
    }

    public ContEconomii deschideContEconomii(Client titular, double dobanda) {
        AuditService.getInstance().logActiune("deschide_cont_economii");
        IBAN iban = new IBAN("RO" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        ContEconomii c = new ContEconomii(iban, titular, dobanda);
        contRepository.save(c);
        return c;
    }

    public Cont gasesteCont(IBAN iban) throws EntitateNegasitaException {
        AuditService.getInstance().logActiune("cauta_cont_by_iban");

        return contRepository.findById(iban)
                .orElseThrow(() -> new EntitateNegasitaException("Contul cu IBAN-ul " + iban + " nu exista!"));
    }

    public void actualizeazaCont(Cont cont) {
        // Audit opțional dacă vrei
        AuditService.getInstance().logActiune("actualizeaza_cont");
        contRepository.update(cont);
    }

    public List<Cont> listeazaToateConturile() {
        AuditService.getInstance().logActiune("listeaza_toate_conturile");
        return contRepository.findAll();
    }
}
package com.pao.project.banca.service;

import com.pao.project.banca.model.Client;
import com.pao.project.banca.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

public class ClientService {
    private static ClientService instance;
    private final ClientRepository clientRepository = new ClientRepository();

    private ClientService() {}

    public static synchronized ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public void adaugaClient(Client c) {
        AuditService.getInstance().logActiune("adauga_client");
        clientRepository.save(c);
        System.out.println("Client adaugat in DB: " + c.getNume());
    }

    public void stergeClient(String id) {
        AuditService.getInstance().logActiune("sterge_client");

        clientRepository.delete(id);
        System.out.println("Client sters din DB: " + id);
    }

    public List<Client> listeazaToti() {
        AuditService.getInstance().logActiune("listeaza_toti_clientii");

        return clientRepository.findAll();
    }

    public Optional<Client> cautaDupaId(String id) {
        AuditService.getInstance().logActiune("cauta_client_by_id");
        return clientRepository.findById(id);
    }
}
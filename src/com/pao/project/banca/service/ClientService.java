package com.pao.project.banca.service;
import com.pao.project.banca.model.Client;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientService {
    private static ClientService instance;
    private Map<String,Client> clienti;

    private ClientService() {
        this.clienti = new HashMap<>();
    }

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public void adaugaClient(Client c) {
        clienti.put(c.getId(), c);
        System.out.println("Client adaugat: " + c.getNume());
    }

    public void stergeClient(String id) {
        clienti.remove(id);
        System.out.println("Client sters: " + id);
    }

    public List<Client> listeazaToti() {
        return new ArrayList<>(clienti.values());
    }

    public Optional<Client> cautaDupaId(String id) {
        return Optional.ofNullable(clienti.get(id));
    }
}

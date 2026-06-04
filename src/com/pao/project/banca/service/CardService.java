package com.pao.project.banca.service;

import com.pao.project.banca.model.Card;
import com.pao.project.banca.exception.EntitateNegasitaException;
import com.pao.project.banca.repository.CardRepository;
import java.util.Optional;

public class CardService {
    private static CardService instance;
    private final CardRepository cardRepository = new CardRepository();

    private CardService() {}

    public static synchronized CardService getInstance() {
        if (instance == null) {
            instance = new CardService();
        }
        return instance;
    }

    public void inregistreazaCard(Card card) {
        AuditService.getInstance().logActiune("inregistreaza_card");
        cardRepository.save(card);
    }

    public Card gasesteCard(String numarCard) throws EntitateNegasitaException {
        AuditService.getInstance().logActiune("cauta_card");
        return cardRepository.findById(numarCard)
                .orElseThrow(() -> new EntitateNegasitaException("Cardul cu numarul " + numarCard + " nu a fost gasit!"));
    }

    public void blocheazaCard(String numarCard) throws EntitateNegasitaException {
        AuditService.getInstance().logActiune("blocheaza_card");

        Card card = gasesteCard(numarCard);
        card.setEsteBlocat(true);
        cardRepository.update(card);
        System.out.println("Cardul " + numarCard + " a fost blocat cu succes.");
    }

    public void stergeCard(String numarCard) throws EntitateNegasitaException {
        AuditService.getInstance().logActiune("sterge_card");
        gasesteCard(numarCard);
        cardRepository.delete(numarCard);
        System.out.println("Cardul " + numarCard + " a fost sters cu succes.");
    }
}
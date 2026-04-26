package com.pao.project.banca.service;
import com.pao.project.banca.model.Card;
import com.pao.project.banca.exception.EntitateNegasitaException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CardService {
    private static CardService instance;
    private Map<String, Card> carduri;

    private CardService() {
        this.carduri = new HashMap<>();
    }

    public static CardService getInstance() {
        if (instance == null) instance = new CardService();
        return instance;
    }

    public void inregistreazaCard(Card card) {
        carduri.put(card.getNumarCard(), card);
    }

    public Card gasesteCard(String numarCard) throws EntitateNegasitaException {
        return Optional.ofNullable(carduri.get(numarCard))
                .orElseThrow(() -> new EntitateNegasitaException("Cardul cu numarul " + numarCard + " nu a fost gasit!"));
    }

    public void blocheazaCard(String numarCard) throws EntitateNegasitaException {
        Card card = gasesteCard(numarCard);
        card.setEsteBlocat(true);
        System.out.println("Cardul " + numarCard + " a fost blocat cu succes.");
    }

    public void stergeCard(String numarCard) throws EntitateNegasitaException {
        carduri.remove(numarCard);
        System.out.println("Cardul " + numarCard + " a fost sters cu succes.");

    }

}

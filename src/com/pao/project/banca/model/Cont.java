package com.pao.project.banca.model;
import java.util.*;
import com.pao.project.banca.exception.*;

public abstract class Cont {
    private final IBAN iban;
    private double sold;
    private Client titular;
    protected List<Tranzactie> istoricTranzactii;
    private List<Card> carduri;

    public Cont(IBAN iban, Client titular) {
        this.iban = iban;
        this.titular = titular;
        this.sold = 0;
        this.istoricTranzactii = new ArrayList<>();
        this.carduri = new ArrayList<>();
    }

    public IBAN getIBAN() {
        return iban;
    }

    public Client getTitular() {
        return titular;
    }

    public double getSold() {
        return sold;
    }

    public List<Tranzactie> getIstoricTranzactii() {
        return new ArrayList<>(this.istoricTranzactii);
    }

    public List<Card> getCarduri() {
        return new ArrayList<>(this.carduri);
    }
    public void setSold(double sold) {
        this.sold = sold;
    }

    public void depune(double suma){
        this.sold += suma;
        System.out.println("Depunere reusita. Sold nou: " + sold);
    }

    public void retrage(double suma) throws FonduriInsuficienteException{
        if(suma > this.sold) {
            throw new FonduriInsuficienteException("Fonduri insuficiente pentru IBAN: " + iban);
        }
        this.sold -= suma;
    }

    public void ataseazaCard(Card card){
        this.carduri.add(card);
        System.out.println("Cardul " + card.getNumarCard() + " a fost atasat contului " + iban);
    }

    public void adaugaTranzactie(Tranzactie tranzactie) {
        this.istoricTranzactii.add(tranzactie);
    }

    @Override
    public String toString() {
        return "IBAN: " + iban + " | Sold: " + sold + " RON";
    }

}

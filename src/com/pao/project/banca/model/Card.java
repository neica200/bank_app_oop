package com.pao.project.banca.model;
import java.time.LocalDate;

public class Card {
    private final String numarCard;
    private String pin;
    private final IBAN ibanAsociat;
    private boolean esteBlocat;
    private double limitaZilnica;
    private LocalDate dataExpirarii;

    public Card(String numarCard,String pin, IBAN ibanAsociat){
        this.numarCard = numarCard;
        this.pin = pin;
        this.ibanAsociat = ibanAsociat;
        this.esteBlocat = false;
        this.limitaZilnica = 5000.0;
        this.dataExpirarii = LocalDate.now().plusYears(4);
    }

    public  String getNumarCard() {
        return numarCard;
    }

    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isEsteBlocat() {
        return esteBlocat;
    }
    public void setEsteBlocat(boolean esteBlocat) {
        this.esteBlocat = esteBlocat;
    }

    public double getLimitaZilnica() {
        return limitaZilnica;
    }

    public void setLimitaZilnica(double limitaZilnica) {
        this.limitaZilnica = limitaZilnica;
    }

    public LocalDate getDataExpirarii() {
        return dataExpirarii;
    }

    public IBAN getIbanAsociat() {
        return ibanAsociat;
    }
    @Override
    public String toString() {
        return "Card: " + numarCard + " | Status: " + (esteBlocat ? "BLOCAT" : "ACTIV") + " | Expira: " + dataExpirarii;
    }
}

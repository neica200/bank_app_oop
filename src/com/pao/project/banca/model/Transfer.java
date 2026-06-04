package com.pao.project.banca.model;

import java.time.LocalDateTime;

public abstract class Transfer extends Tranzactie {
    protected IBAN receiverIBAN;

    // 1. Constructorul tau existent din Etapa I
    public Transfer(double suma, String currency, IBAN receiverIBAN) {
        super(suma, currency);
        this.receiverIBAN = receiverIBAN;
    }

    // 2. CONSTRUCTOR NOU: Propaga datele din DB catre clasa de baza Tranzactie
    public Transfer(String id, double suma, String currency, LocalDateTime timestamp, IBAN receiverIBAN) {
        super(id, suma, currency, timestamp);
        this.receiverIBAN = receiverIBAN;
    }

    public IBAN getReceiverIBAN() {
        return receiverIBAN;
    }

    // Adaugam si varianta asta de getter pentru a preveni erorile de scriere din clasele copil
    public IBAN getReceiverIban() {
        return receiverIBAN;
    }

    @Override
    public String toString(){
        return super.toString() + " | Destinatar: " + receiverIBAN;
    }
}
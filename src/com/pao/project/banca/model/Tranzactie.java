package com.pao.project.banca.model;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Tranzactie {
    protected String id;
    protected double suma;
    protected String currency;
    protected LocalDateTime timestamp;

    public Tranzactie(double suma, String currency) {
        this.id = UUID.randomUUID().toString();
        this.suma = suma;
        this.currency = currency;
        this.timestamp = LocalDateTime.now();
    }

    public abstract void execute();

    public String getId() {
        return id;
    }
    public double getSuma() {
        return suma;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Tranzactia cu id-ul " + id + " | Suma: " + suma + " " + currency + " | Data: " + timestamp;

    }
}

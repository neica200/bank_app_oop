package com.pao.project.banca.model;

import java.time.LocalDateTime;

public class Plata extends Tranzactie{
    private String comerciant;

    public Plata(double suma, String currency, String comerciant) {
        super(suma, currency);
        this.comerciant = comerciant;
    }

    public Plata(String id, double suma, String currency, LocalDateTime timestamp, String comerciant) {
        super(id, suma, currency, timestamp); // Trimite cele 4 argumente catre parintele Tranzactie
        this.comerciant = comerciant;
    }

    public String getComerciant() {
        return comerciant;
    }

    public void setComerciant(String comerciant) {
        this.comerciant = comerciant;
    }

    @Override
    public void execute() {
        System.out.println("Plata de " + suma + " " + currency + " efectuata la: " + comerciant);
    }

    @Override
    public String toString()
    {
        return super.toString() + " | Comerciant: " + comerciant;
    }


}

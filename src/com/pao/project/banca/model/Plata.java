package com.pao.project.banca.model;

public class Plata extends Tranzactie{
    private String comerciant;

    public Plata(double suma, String currency, String comerciant) {
        super(suma, currency);
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

package com.pao.project.banca.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class TransferInterBancar extends Transfer {
    private String swiftCode;
    private double comision;

    // 1. Constructorul tau existent din Etapa I
    public TransferInterBancar(double amount, String currency, IBAN receiverIban, String swiftCode, double comision){
        super(amount, currency, receiverIban);
        this.swiftCode = swiftCode;
        this.comision = comision;
    }

    // 2. CONSTRUCTOR NOU: Folosit de TranzactieRepository pentru incarcarea din DB
    public TransferInterBancar(String id, double amount, String currency, LocalDateTime timestamp, IBAN receiverIban, String swiftCode, double comision) {
        // Apelam constructorul parintelui care stie sa seteze ID-ul si data veche
        super(id, amount, currency, timestamp, receiverIban);
        this.swiftCode = swiftCode;
        this.comision = comision;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public double getComision() {
        return comision;
    }

    @Override
    public void execute() {
        // receiverIBAN corectat conform numelui din clasa parinte sau getter-ului disponibil
        System.out.println("Se executa transferul interbancar catre: " + getReceiverIban() + " (SWIFT: " + swiftCode + ")");
    }

    @Override
    public String toString() {
        return super.toString() + " | SWIFT: " + swiftCode + " | Comision: " + comision;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof TransferInterBancar)) return false;
        TransferInterBancar other = (TransferInterBancar)obj;
        return Objects.equals(getId(), other.getId()); // Folosim getId() deoarece id este privat/protejat in clasa de baza
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
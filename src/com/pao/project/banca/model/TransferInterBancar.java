package com.pao.project.banca.model;

import java.util.Objects;

public class TransferInterBancar extends Transfer{
    private String swiftCode;
    private double comision;

    public TransferInterBancar(double amount,String currency,IBAN receiverIban, String swiftCode, double comision){
        super(amount,currency,receiverIban);
        this.swiftCode = swiftCode;
        this.comision = comision;
    }

    @Override
    public void execute() {
        // Aici va veni logica in Etapa Serviciilor:
        // 1. Verifica sold cont sursa
        // 2. Scade suma + taxa
        // 3. Trimite catre sistemul interbancar
        System.out.println("Se executa transferul interbancar catre: " + receiverIBAN + " (SWIFT: " + swiftCode + ")");
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
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

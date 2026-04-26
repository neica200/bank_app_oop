package com.pao.project.banca.model;

public class TransferIntraBancar extends Transfer{
    public TransferIntraBancar(double suma, String currency,IBAN receiverIBAN) {
        super(suma,currency,receiverIBAN);
    }

    @Override
    public void execute() {
        System.out.println("Transfer intern procesat instant catre: " + receiverIBAN);
    }
}

package com.pao.project.banca.model;

import java.time.LocalDateTime;

public class TransferIntraBancar extends Transfer {


    public TransferIntraBancar(double suma, String currency, IBAN receiverIBAN) {
        super(suma, currency, receiverIBAN);
    }

    public TransferIntraBancar(String id, double suma, String currency, LocalDateTime timestamp, IBAN receiverIBAN) {
        super(id, suma, currency, timestamp, receiverIBAN);
    }

    @Override
    public void execute() {
        System.out.println("Transfer intern procesat instant catre: " + receiverIBAN);
    }
}
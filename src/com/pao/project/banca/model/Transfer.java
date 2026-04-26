package com.pao.project.banca.model;

public abstract class Transfer extends Tranzactie{
    protected IBAN receiverIBAN;

    public Transfer(double suma,String currency, IBAN receiverIBAN) {
        super(suma,currency);
        this.receiverIBAN = receiverIBAN;
    }

    public IBAN getReceiverIBAN() {
        return receiverIBAN;
    }

    @Override
    public String toString(){
        return super.toString() + " | Destinatar: " + receiverIBAN;
    }
}

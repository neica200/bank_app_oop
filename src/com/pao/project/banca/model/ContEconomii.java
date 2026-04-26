package com.pao.project.banca.model;

public class ContEconomii extends Cont{
    private double rataDobanda;

    public ContEconomii(IBAN iban, Client titular,double rataDobanda) {
        super(iban,titular);
        this.rataDobanda = rataDobanda;
    }

    public void aplicaDobanda(){
        double dobanda = this.getSold() * (rataDobanda/100);
        this.setSold(dobanda);
        System.out.println("S-a aplicat dobanda de  " + dobanda +  " pentru contul " + this.getIBAN());
    }

    public double getRataDobanda() {
        return rataDobanda;
    }

    public void setRataDobanda(double rataDobanda) {
        this.rataDobanda = rataDobanda;
    }

    @Override
    public String toString() {
        return super.toString() + " | Tip: Economii | Rata Dobanda: " + rataDobanda;
    }
}

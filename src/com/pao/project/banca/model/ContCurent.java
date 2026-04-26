package com.pao.project.banca.model;

public class ContCurent extends Cont{
    private double limitaOverdraft;

    public ContCurent(IBAN iban, Client titular,double limitaOverdraft) {
        super(iban, titular);
        this.limitaOverdraft = limitaOverdraft;
    }

    public double getLimitaOverdraft() {
        return limitaOverdraft;
    }

    public void setLimitaOverdraft(double limitaOverdraft) {
        this.limitaOverdraft = limitaOverdraft;
    }

    @Override
    public String toString() {
        return super.toString() + "| Tip: Curent | LimitaOverdraft: " + limitaOverdraft;
    }


}

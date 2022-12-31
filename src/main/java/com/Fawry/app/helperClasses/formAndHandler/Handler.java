package com.Fawry.app.helperClasses.formAndHandler;

public abstract class Handler {
    private double amount = 0.0;
    private boolean supportsCash = true;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isSupportsCash() {
        return supportsCash;
    }

    public void setSupportsCash(boolean supportsCash) {
        this.supportsCash = supportsCash;
    }

}

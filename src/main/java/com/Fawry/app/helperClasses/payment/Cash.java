package com.Fawry.app.helperClasses.payment;

public class Cash implements Payment {
    @Override
    public boolean pay(double amount) {
        return true;
    }
}

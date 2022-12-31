package com.Fawry.app.helperClasses.formAndHandler;

import java.util.Scanner;

public class MobileRecharge extends Handler {

    private String number;

    public MobileRecharge(double amount, String number) {
        setSupportsCash(true);
        setAmount(amount);
        setNumber(number);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}

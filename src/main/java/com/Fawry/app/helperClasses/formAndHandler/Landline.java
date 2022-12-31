package com.Fawry.app.helperClasses.formAndHandler;

import java.util.Scanner;

public class Landline extends Handler {
    private String landlineNumber;

    public Landline(double amount, String landlineNumber) {
        setSupportsCash(true);
        setAmount(amount);
        setLandlineNumber(landlineNumber);
    }

    public String getLandlineNumber() {
        return landlineNumber;
    }

    public void setLandlineNumber(String landlineNumber) {
        this.landlineNumber = landlineNumber;
    }

}

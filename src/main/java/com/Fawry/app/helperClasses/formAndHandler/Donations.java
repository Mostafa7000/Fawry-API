package com.Fawry.app.helperClasses.formAndHandler;

import java.util.Scanner;

public class Donations extends Handler {

    private String dest = "";

    public Donations(double amount, String dest) {
        setSupportsCash(false);
        this.dest= dest;
        this.setAmount(amount);
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}

package com.Fawry.app.helperClasses.payment;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Card implements Payment {
    private String cardNumber;
    private String holderName;
    private String cvv;

    public Card(String cardNumber, String holderName, String cvv) {
        initialize(cardNumber, holderName, cvv);
    }

    public void initialize(String cardNumber, String holderName, String cvv) {
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.cvv = cvv;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public boolean pay(double amount) {
        return contactBank();
    }

    public boolean contactBank() {
        return true;
    }
}

package com.Fawry.app.helperClasses.payment;

public class PaymentFactory {
    public static final int CARD = 1;
    public static final int WALLET = 2;
    public static final int CASH = 3;

    public static Payment createPayment(int choice) {
        return switch (choice) {
            case CARD -> new Card();
            case WALLET -> new Wallet();
            case CASH -> new Cash();
            default -> null;
        };
    }
}

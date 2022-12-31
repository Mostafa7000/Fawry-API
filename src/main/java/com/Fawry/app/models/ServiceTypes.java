package com.Fawry.app.models;

public enum ServiceTypes {
    INTERNET_PAYMENT("Internet Payment"),
    MOBILE_RECHARGE("Mobile Recharge"),
    LANDLINE("Landline"),
    DONATIONS("Donations");

    private final String databaseAcronym;

    ServiceTypes(String databaseAcronym) {
        this.databaseAcronym = databaseAcronym;
    }

    public static ServiceTypes getEnumType(String type) {
        return switch (type) {
            case "Internet Payment" -> INTERNET_PAYMENT;
            case "Mobile Recharge" -> MOBILE_RECHARGE;
            case "Landline" -> LANDLINE;
            case "Donations" -> DONATIONS;
            default -> null;
        };
    }

    public String getDatabaseAcronym() {
        return databaseAcronym;
    }
}

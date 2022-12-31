package com.Fawry.app.models;

public class Transaction {
    private int id = 0;
    private double amount = 0.0;

    // refund String can be null
    private String refund = "";
    private String userEmail = null;
    private int serviceId = 0;

    public Transaction(int id, int serviceId, String userEmail, double amount, String refund) {
        this.id = id;
        this.amount = amount;
        this.refund = refund;
        this.userEmail = userEmail;
        this.serviceId = serviceId;
    }

    public Transaction() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

}

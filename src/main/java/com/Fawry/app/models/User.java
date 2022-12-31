package com.Fawry.app.models;

public class User {
    private String email = null;
    private String password = null;
    private String name = null;
    private double balance = 0.0;
    private double discount = 0.0;
    private boolean admin = false;

    public User(String email, String password, String name, double balance, double discount, boolean admin) {
        this.email = email.toLowerCase();
        this.password = password;
        this.name = name;
        this.balance = balance;
        this.discount = discount;
        this.admin = admin;
    }

    public User(String email, String password, String name) {
        this.name = name;
        this.email = email.toLowerCase();
        this.password = password;
    }

    public User() {

    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public boolean equals(User anotherUser) {
        return this.getName().equalsIgnoreCase(anotherUser.getName());
    }
}

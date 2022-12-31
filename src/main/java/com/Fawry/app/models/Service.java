package com.Fawry.app.models;

public class Service {
    private int id = 0;
    private ServiceTypes serviceType = null;
    private String provider = null;
    private double discount = 0.0;
    private double price = 0.0;

    public Service(int id, ServiceTypes serviceType, String provider, double price, double discount) {
        this.id = id;
        this.serviceType = serviceType;
        this.provider = provider;
        this.price = price;
        this.discount = discount;
    }

    public Service() {

    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double amount) {
        this.price = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ServiceTypes getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypes serviceType) {
        this.serviceType = serviceType;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}

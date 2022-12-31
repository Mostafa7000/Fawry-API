package com.Fawry.app.helperClasses;

import com.Fawry.app.helperClasses.formAndHandler.*;
import com.Fawry.app.models.Service;
import com.Fawry.app.models.ServicesData;
import com.Fawry.app.models.Transaction;
import com.Fawry.app.models.TransactionsData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Services {

    private final ServicesData servicesData;
    private List<Service> allServices;

    public Services() {
        try {
            servicesData = new ServicesData();
            allServices = servicesData.index();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Service> getAllServices() {
        updateServices();
        return allServices;
    }

    public void updateServices() {
        try {
            allServices = servicesData.index();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Service> searchServices(String word) {
        var result = new ArrayList<Service>();
        var caseLess = word.toLowerCase();
        for (var s : getAllServices()) {
            if (s.getServiceType().getDatabaseAcronym().toLowerCase().contains(caseLess) || s.getProvider().toLowerCase().contains(caseLess))
                result.add(s);
        }
        return result;
    }

    public Form createForm(Service service, Map<String, String> mp) {
        Handler handler;
        handler = switch (service.getServiceType()) {
            case MOBILE_RECHARGE -> new MobileRecharge(service.getPrice(), mp.get("number"));
            case INTERNET_PAYMENT -> new InternetPayment(service.getPrice(), mp.get("number"));
            case LANDLINE -> new Landline(service.getPrice(), mp.get("number"));
            case DONATIONS -> new Donations(Double.parseDouble(mp.get("amount")), mp.get("destination"));
        };
        return new Form(service, handler);
    }

    public boolean requestRefund(int id, String email, TransactionsData transactions) throws SQLException {
        List<Transaction> myTransactions;
        myTransactions = transactions.showMyTransactions(email);

        for (var transaction : myTransactions)
            if (id == transaction.getId()) {
                return transactions.requestRefund(id);
            }
        return false;
    }
}

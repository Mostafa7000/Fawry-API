package com.Fawry.app.boundaries.homeBoundary;

import com.Fawry.app.helperClasses.Services;
import com.Fawry.app.models.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public abstract class HomeBoundary {
    Scanner scanner = new Scanner(System.in);
    Services servicesManager = new Services();
    TransactionsData transactions = new TransactionsData();
    User user;
    ServicesData servicesData = new ServicesData();
    UsersData users = new UsersData();

    protected HomeBoundary() throws SQLException {
    }

    abstract void onCreate() throws Exception;

    abstract void displayMenu() throws Exception;

    protected void displayAllTransactions(List<Transaction> transactions) {
        if (transactions.size() > 0) {
            System.out.printf("%-3s %-11s %-25s %-8s %-8s %n", "id", "service id", "user email", "amount", "refund");
            System.out.println("----------------------------------------------------------");
        } else {
            System.out.println("There are no refund requests!");
        }
        for (var transaction : transactions) {
            System.out.printf(Locale.US, "%-3s %-11s %-25s %-8.2f %-8s %n", transaction.getId(),
                    transaction.getServiceId(), transaction.getUserEmail(), transaction.getAmount(), transaction.getRefund());
        }
    }

    protected void displayAllServices(List<Service> allServices) {
        if (allServices.size() > 0) {
            System.out.printf("%-3s %-20s %-30s %-8s %-3s %n", "id", "service type", "provider", "price", "discount");
            System.out.println("----------------------------------------------------------------------------");
        }
        for (var service : allServices) {
            System.out.printf(Locale.US, "%-3s %-20s %-30s %-8.2f %-3.2f %n", service.getId(),
                    service.getServiceType(), service.getProvider(), service.getPrice(), service.getDiscount());
        }
    }

    protected void displayAllUsers(List<User> allUsers) {
        if (allUsers.size() > 0) {
            System.out.printf("%-30s %-20s %-10s %-3s %n", "email", "name", "balance", "discount");
            System.out.println("----------------------------------------------------------------------------");
        }
        for (var user : allUsers) {
            System.out.printf(Locale.US, "%-30s %-20s %-10s %-3s %n", user.getEmail(),
                    user.getName(), user.getBalance(), user.getDiscount());
        }
    }
}

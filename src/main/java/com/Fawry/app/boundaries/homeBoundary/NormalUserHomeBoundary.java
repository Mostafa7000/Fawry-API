package com.Fawry.app.boundaries.homeBoundary;

import com.Fawry.app.helperClasses.payment.*;
import com.Fawry.app.models.Service;
import com.Fawry.app.models.Transaction;
import com.Fawry.app.models.User;
import com.Fawry.app.models.UsersData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class NormalUserHomeBoundary extends HomeBoundary {

    public NormalUserHomeBoundary(User user) throws Exception {
        super();
        this.user = user;
        onCreate();
    }

    public static void main(String[] args) throws SQLException {
        var users = new UsersData();
        var user = users.show("youssef@gmail.com").get(0);
        try {
            var x = new NormalUserHomeBoundary(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    final void onCreate() throws Exception {
        displayMenu();
    }

    final void displayMenu() throws Exception {
        int selection;

        do {
            updateUser();
            System.out.println("Select an option");
            System.out.println("1. View all available services");
            System.out.println("2. Search for a service");
            System.out.println("3. Request a refund");
            System.out.println("4. Add funds to wallet");
            System.out.println("5. View discounted services");
            System.out.println("6. Logout");

            selection = scanner.nextInt();
        } while (userSelectionsFactory(selection));
    }

    private boolean userSelectionsFactory(int selection) throws Exception {
        switch (selection) {
            case 1 -> {
                displayAllServices(servicesManager.getAllServices());
                Service chosenService = selectService();
                handlePayment(chosenService);
            }
            case 2 -> {
                var services = searchForService();
                if (services.size() > 0) {
                    displayAllServices(services);
                    Service chosenService = selectService();
                    handlePayment(chosenService);
                } else
                    System.out.println("No services found");
            }
            case 3 -> {
                requestRefund();
            }
            case 4 -> {
                addToWallet();
            }
            case 5 -> {
                listDiscounts();
            }
            case 6 -> {
                throw new Exception("Logging out...");
            }
            default -> {
                System.out.println("Wrong input, try again...");
                return true;
            }
        }
        return true;
    }

    private List<Service> searchForService() {
        System.out.println("Enter search word: ");
        var word = scanner.next();
        return servicesManager.searchServices(word);
    }

    private Service selectService() {
        do {
            System.out.println("Enter service id: ");
            int id = scanner.nextInt();
            for (var service : servicesManager.getAllServices())
                if (id == service.getId())
                    return service;
        } while (true);
    }



    private void listDiscounts() {
        List<Service> res = new ArrayList<>();
        for (var service : servicesManager.getAllServices()) {
            if (service.getDiscount() > 0) {
                res.add(service);
            }
        }
        if (res.size() > 0)
            displayAllServices(res);
        else
            System.out.println("There isn't any discounts right now");
    }

    private void handlePayment(Service service) {
        var form = servicesManager.createForm(service);
        System.out.printf(Locale.US, "Due amount after discounts: $%.2f %n", ServicePay.calculateDueAmount(user, form));
        int choice;
        do {
            System.out.println("Available payment methods: ");
            System.out.println("1. Credit Card");
            System.out.printf(Locale.US, "2. Wallet: $%.2f %n", user.getBalance());
            if (form.getHandler().isSupportsCash())
                System.out.println("3. Cash");
            System.out.println("Choice: ");
            choice = scanner.nextInt();
        } while ((choice != 1 && choice != 2 && choice != 3) || (choice == 3 && !form.getHandler().isSupportsCash()));
        Payment payMethod = PaymentFactory.createPayment(choice);
        switch (choice) {
            case 1 -> {
                initializeCreditCards((Card) payMethod);
            }
            case 2 -> {
                ((Wallet) payMethod).initialize(user);
            }
        }
        ServicePay payHandler = new ServicePay(user, form, payMethod);
        try {
            payHandler.handlePayment();
            printDecorated("Payment have completed successfully", '-');
        } catch (Exception e) {
            printDecorated("Couldn't complete payment: " + e.getMessage(), '*');
        }
    }

    private void initializeCreditCards(Card card) {
        String number, holder, cvv;
        System.out.println("Enter card Number (16 digits): ");
        number = scanner.next();
        while (!Pattern.matches("\\d{16}", number)) {
            System.out.println("Please enter a valid credit card number (16 digits): ");
            number = scanner.next();
        }

        System.out.println("Enter the name on the card: ");
        holder = scanner.next();
        while (!Pattern.matches("[a-zA-Z\\s]+", holder)) {
            System.out.println("Please enter a valid name: ");
            holder = scanner.next();
        }

        System.out.println("Enter the 3 digits (CVV) at the back of your card: ");
        cvv = scanner.next();
        while (!Pattern.matches("\\d{3}", cvv)) {
            System.out.println("Please enter valid CVV number: ");
            cvv = scanner.next();
        }
        card.initialize(number, holder, cvv);
    }

    private void addToWallet() {
        System.out.println("Enter the amount you would like to add: ");
        double amount = scanner.nextDouble();
        Payment payMethod = PaymentFactory.createPayment(PaymentFactory.CARD);
        initializeCreditCards((Card) payMethod);
        try {
            payMethod.pay(amount);
            users.updateBalance(user.getEmail(), user.getBalance() + amount);
            // register a new transaction for wallet recharging
            transactions.create(new Transaction(0,0, user.getEmail(), amount,""));
            printDecorated("Funds have been added successfully", '-');
        } catch (Exception e) {
            printDecorated("Couldn't complete payment: " + e.getMessage(), '*');
        }
    }

    private void printDecorated(String st, char decore) {
        int sz = st.length();
        for (int i = 0; i < sz + 4; i++)
            System.out.print(decore);
        System.out.println();
        System.out.println(decore + " " + st + " " + decore);
        for (int i = 0; i < sz + 4; i++)
            System.out.print(decore);
        System.out.println();
    }

    private void updateUser() throws SQLException {
        this.user = users.show(user.getEmail()).get(0);
    }
}

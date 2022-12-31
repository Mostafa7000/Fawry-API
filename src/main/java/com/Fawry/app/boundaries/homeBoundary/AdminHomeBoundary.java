package com.Fawry.app.boundaries.homeBoundary;

import com.Fawry.app.models.Transaction;
import com.Fawry.app.models.User;

import java.sql.SQLException;
import java.util.List;

public class AdminHomeBoundary extends HomeBoundary {
    User user;

    public AdminHomeBoundary(User user) throws Exception {
        this.user = user;
        onCreate();
    }

    final void onCreate() throws Exception {
        displayMenu();
    }

    final void displayMenu() throws Exception {
        do {
            System.out.println("Select an option");
            System.out.println("1. Accept or Reject refund");
            System.out.println("2. Apply a discount");
            System.out.println("3. Logout");

            String selection = scanner.next();

            switch (selection) {
                case "1" -> acceptOrRejectRefund();
                case "2" -> applyDiscount();
                case "3" -> throw new Exception("Logging out...");
                default -> System.out.println("Wrong input, try again...");
            }
        } while (true);
    }

    private void applyDiscount() throws SQLException {
        do {
            System.out.println("Select an option");
            System.out.println("1. Apply discount on a service");
            System.out.println("2. Apply discount on specific user");
            System.out.println("3. Return to previous menu");

            String selection = scanner.next();

            switch (selection) {
                case "1" -> applyDiscountOnService();
                case "2" -> applyUserDiscount();
                case "3" -> {
                    System.out.println("Returning to previous menu...");
                    return;
                }
                default -> System.out.println("Wrong input, try again...");
            }
        } while (true);
    }

    private void applyUserDiscount() throws SQLException {

        var allUsers = users.index();
        displayAllUsers(allUsers);
        System.out.println("Enter the e-mail of user:");
        String mail = scanner.next();

        while (users.show(mail).size() == 0) {
            System.out.println("Such email not found....");
            System.out.println("1.to renter e-mail:");
            System.out.println("2.to return to previous menu");

            String selection = scanner.next();

            if (selection.equals("1")) {
                System.out.println("Enter the e-mail of user:");
                mail = scanner.next();
            } else if (selection.equals("2")) {
                System.out.println("Returning to previous menu...");
                return;
            } else {
                System.out.println("Wrong input, try again...");
            }
        }

        System.out.println("Enter discount amount in range [0,1] (e.g., 35% = 0.35)");

        String d = scanner.next();
        double discount = Double.parseDouble(d);

        while (discount > 1 || discount < 0) {
            System.out.println("Enter valid discount: ");
            discount = scanner.nextDouble();
        }

        users.setDiscount(mail, discount);
        System.out.println("Discount added!");
    }

    private void applyDiscountOnService() throws SQLException {
        displayAllServices(servicesData.index());

        System.out.println("Choose Service ID:");

        int selectedID = scanner.nextInt();

        while (servicesData.show(selectedID).size() == 0) {

            System.out.println("Invalid id");
            System.out.println("1.Enter Id again");
            System.out.println("2.Return previous menu");

            String selection = scanner.next();

            if (selection.equals("1")) {
                selectedID = scanner.nextInt();
            } else if (selection.equals("2")) {
                System.out.println("Returning to previous menu...");
                return;
            } else {
                System.out.println("wrong input...");
                return;
            }
        }

        System.out.println("Enter discount amount in range [0,1] (e.g., 35% = 0.35)");

        String d = scanner.next();
        double discount = Double.parseDouble(d);

        while (discount > 1 || discount < 0) {
            System.out.println("Enter valid discount");
            discount = scanner.nextDouble();
        }
        System.out.println("Discount added!");
        servicesData.setDiscount(selectedID, discount);
    }

    private void acceptOrRejectRefund() throws Exception {

        var refunds = transactions.showRefundRequests();
        displayAllTransactions(refunds);

        do {
            System.out.println("Select an option");
            System.out.println("1. Accept a refund");
            System.out.println("2. Reject a refund");
            System.out.println("3. Return to previous menu");

            String selection = scanner.next();

            switch (selection) {
                case "1" -> {
                    acceptRefund(refunds);
                }
                case "2" -> {
                    rejectRefund(refunds);
                }
                case "3" -> {
                    System.out.println("Returning to previous menu...");
                    return;
                }
                default -> System.out.println("Wrong input, try again...");
            }
        } while (true);
    }

    private void rejectRefund(List<Transaction> refunds) throws SQLException {
        if (refunds.size() == 0) {
            return;
        }

        System.out.println("Select the transaction ID: ");

        int selectedID = scanner.nextInt();

        if (transactions.approveOrReject(selectedID, false))
            System.out.println("Refund Is Rejected!");
        else
            System.out.println("ID not found");

    }

    private void acceptRefund(List<Transaction> refunds) throws SQLException {
        if (refunds.size() == 0) {
            return;
        }

        System.out.println("Select the transaction ID: ");

        int selectedID = scanner.nextInt();

        if (transactions.approveOrReject(selectedID, true)) {
            System.out.println("Refund Is Accepted");

            String email = transactions.show(selectedID).get(0).getUserEmail();

            double amount = transactions.show(selectedID).get(0).getAmount();

            double currentBalance = users.show(email).get(0).getBalance();

            users.updateBalance(email, currentBalance + amount);
        } else {
            System.out.println("ID not found");
        }
    }
}

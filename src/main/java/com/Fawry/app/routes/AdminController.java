package com.Fawry.app.routes;

import com.Fawry.app.custom.Response;
import com.Fawry.app.models.ServicesData;
import com.Fawry.app.models.Transaction;
import com.Fawry.app.models.TransactionsData;
import com.Fawry.app.models.UsersData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    UsersData usersData;
    TransactionsData transactionsData;
    ServicesData servicesData;

    AdminController() throws SQLException {
        usersData = new UsersData();
        transactionsData = new TransactionsData();
        servicesData = new ServicesData();
    }

    @GetMapping("/discounts/service/{id}/amount/{amount}")
    public Response<Void> addServiceDiscount(@PathVariable int id, @PathVariable double amount) throws SQLException {
        var res = new Response<Void>();
        if (servicesData.show(id).size() == 0) {
            res.setStatus(false);
            res.setMessage("Service not found");
            return res;
        }
        if (amount > 1.0 || amount < 0.0) {
            res.setStatus(false);
            res.setMessage("Invalid discount, discount must rely between 0.0 and 1.0");
            return res;
        }
        servicesData.setDiscount(id, amount);
        res.setStatus(true);
        res.setMessage("Added discount to service successfully");
        return res;
    }

    @GetMapping("/discounts/user/{email}/amount/{amount}")
    public Response<Void> addUserDiscount(@PathVariable String email, @PathVariable double amount) throws SQLException {
        var res = new Response<Void>();
        if (usersData.show(email).size() == 0) {
            res.setStatus(false);
            res.setMessage("User not found");
            return res;
        }
        if (amount > 1.0 || amount < 0.0) {
            res.setStatus(false);
            res.setMessage("Invalid discount, discount must rely between 0.0 and 1.0");
            return res;
        }
        usersData.setDiscount(email, amount);
        res.setStatus(true);
        res.setMessage("Added discount to user successfully");
        return res;
    }

    @GetMapping("/transactions/payment")
    public List<Transaction> paymentTransactions() throws SQLException {
        var res = new ArrayList<Transaction>();
        for (var transaction : transactionsData.index()) {
            if (!transaction.getRefund().equals("refunded") && transaction.getServiceId() != 0)
                res.add(transaction);
        }
        return res;
    }

    @GetMapping("/transactions/wallet")
    public List<Transaction> addToWalletTransactions() throws SQLException {
        var res = new ArrayList<Transaction>();
        for (var transaction : transactionsData.index()) {
            if (transaction.getServiceId() == 0)
                res.add(transaction);
        }
        return res;
    }

    @GetMapping("/transactions/refund/refunded")
    public List<Transaction> refundTransactions() throws SQLException {
        var res = new ArrayList<Transaction>();
        for (var transaction : transactionsData.index()) {
            if (transaction.getRefund().equals("refunded"))
                res.add(transaction);
        }
        return res;
    }

    @GetMapping("/transactions/refund/pending")
    public List<Transaction> pendingRefundTransactions() throws SQLException {
        var res = new ArrayList<Transaction>();
        for (var transaction : transactionsData.index()) {
            if (transaction.getRefund().equals("pending"))
                res.add(transaction);
        }
        return res;
    }

    @GetMapping("/transactions/{id}/refund/approve")
    public Response<Void> approveRefund(@PathVariable int id) throws SQLException {
        var res = new Response<Void>();
        if (transactionsData.show(id).size() == 0) {
            res.setStatus(false);
            res.setMessage("wrong transaction ID");
            return res;
        }
        if (!transactionsData.show(id).get(0).getRefund().equals("pending")) {
            res.setStatus(false);
            res.setMessage("No refund request found");
            return res;
        }
        // issue refund transaction
        transactionsData.approveOrReject(id, true);
        String email = transactionsData.show(id).get(0).getUserEmail();
        double amount = transactionsData.show(id).get(0).getAmount();
        double currentBalance = usersData.show(email).get(0).getBalance();
        // update user balance
        usersData.updateBalance(email, currentBalance + amount);

        res.setStatus(true);
        res.setMessage("Transaction refunded successfully");
        return res;
    }

    @GetMapping("/transactions/{id}/refund/reject")
    public Response<Void> rejectRefund(@PathVariable int id) throws SQLException {
        var res = new Response<Void>();
        // check that the transaction exists
        if (transactionsData.show(id).size() == 0) {
            res.setStatus(false);
            res.setMessage("wrong transaction ID");
            return res;
        }
        // check that transaction is refundable
        if (!transactionsData.show(id).get(0).getRefund().equals("pending")) {
            res.setStatus(false);
            res.setMessage("No refund request found");
            return res;
        }
        // issue refund transaction
        transactionsData.approveOrReject(id, false);
        res.setStatus(true);
        res.setMessage("refund rejected successfully");
        return res;
    }
}

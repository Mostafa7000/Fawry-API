package com.Fawry.app.routes;

import com.Fawry.app.custom.Response;
import com.Fawry.app.helperClasses.Services;
import com.Fawry.app.models.Transaction;
import com.Fawry.app.models.TransactionsData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    TransactionsData transactionsData;
    Services services;


    public TransactionsController() throws SQLException {
        this.transactionsData = new TransactionsData();
        this.services = new Services();
    }

    @GetMapping("/user/{email}")
    public List<Transaction> myTransactions(@PathVariable String email) throws SQLException {
        return transactionsData.showMyTransactions(email);
    }

    @GetMapping("/user/{email}/refund/{id}")
    public Response<Void> submitRefundReq(@PathVariable int id, @PathVariable String email) throws SQLException {
        Response<Void> res = new Response<>();
        if (!transactionsData.show(id).get(0).getUserEmail().equals(email)) {
            res.setStatus(false);
            res.setMessage("User didn't issue such transaction");
            return res;
        }
        if (services.requestRefund(id, email, transactionsData)) {
            res.setStatus(true);
            res.setMessage("Refund requested successfully");
        } else {
            res.setStatus(false);
            res.setMessage("Couldn't ask for refund");
        }
        return res;
    }


}

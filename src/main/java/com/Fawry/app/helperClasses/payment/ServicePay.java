package com.Fawry.app.helperClasses.payment;

import com.Fawry.app.helperClasses.formAndHandler.Form;
import com.Fawry.app.models.Transaction;
import com.Fawry.app.models.TransactionsData;
import com.Fawry.app.models.User;

import java.sql.SQLException;

public class ServicePay {
    User user;
    Form form;
    Payment payMethod;

    public ServicePay(User user, Form form, Payment payMethod) {
        this.user = user;
        this.form = form;
        this.payMethod = payMethod;
    }

    public static double calculateDueAmount(User user, Form form) {
        double amountToBeDiscounted = (user.getDiscount() + form.getService().getDiscount()) * form.getHandler().getAmount();
        return form.getHandler().getAmount() - amountToBeDiscounted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Payment getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Payment payMethod) {
        this.payMethod = payMethod;
    }

    public boolean handlePayment() throws Exception {
        var amountToBePaid = ServicePay.calculateDueAmount(user, form);
        if (!payMethod.pay(amountToBePaid))
            return false;
        recordTransaction(amountToBePaid);
        return true;
    }

    public void recordTransaction(double amount) throws SQLException {
        // id and refund values are not necessary for recording a transaction
        // hence they can take any arbitrary values
        Transaction t = new Transaction(0, form.getService().getId(), user.getEmail(), amount, "");
        TransactionsData transactionsData = new TransactionsData();
        transactionsData.create(t);
    }
}

package com.Fawry.app.routes;

import com.Fawry.app.custom.Response;
import com.Fawry.app.helperClasses.Services;
import com.Fawry.app.helperClasses.formAndHandler.Form;
import com.Fawry.app.helperClasses.payment.*;
import com.Fawry.app.models.PayRequest;
import com.Fawry.app.models.ServicesData;
import com.Fawry.app.models.User;
import com.Fawry.app.models.UsersData;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Locale;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/payment/service/{id}/user/{email}")
public class PaymentController {
    ServicesData servicesData;
    UsersData usersData;
    Services services;

    PaymentController() throws SQLException {
        servicesData = new ServicesData();
        usersData = new UsersData();
        services = new Services();
    }
 //payment with card
    @PostMapping("/card")
    public Response<Void> payWithCard(@RequestBody PayRequest req, @PathVariable int id, @PathVariable String email) throws Exception {
        Response<Void> res = new Response<>();
        if (servicesData.show(id).size() == 0) {
            res.setStatus(false);
            res.setMessage("Service Not Available");
            return res;
        }
        if (!usersData.checkUserExistence(email)) {
            res.setStatus(false);
            res.setMessage("User Not Found");
            return res;
        }
        var form = services.createForm(servicesData.show(id).get(0), req.getHandler());
        var user = usersData.show(email).get(0);
        // check if Card info is valid
        if (!validateCard((Card) req.getCard())) {
            res.setStatus(false);
            res.setMessage("Bad Card Information, please check card Info and try again.");
            return res;
        }
        Payment payMethodObj = req.getCard();
        return handlePayment(user, form, payMethodObj);
    }
 //payment with wallet
    @PostMapping("/wallet")
    public Response<Void> payWithWallet(@RequestBody PayRequest req, @PathVariable int id, @PathVariable String email) throws Exception {
        Response<Void> res = new Response<>();
        if (servicesData.show(id).size() == 0) {
            res.setStatus(false);
            res.setMessage("Service Not Available");
            return res;
        }
        if (!usersData.checkUserExistence(email)) {
            res.setStatus(false);
            res.setMessage("User Not Found");
            return res;
        }
        var form = services.createForm(servicesData.show(id).get(0), req.getHandler());
        var user = usersData.show(email).get(0);
        Payment payMethodObj = PaymentFactory.createPayment(PaymentFactory.WALLET);
        ((Wallet) payMethodObj).initialize(user);
        return handlePayment(user, form, payMethodObj);
    }
//payment with cash
    @PostMapping("/cash")
    public Response<Void> payWithCash(@RequestBody PayRequest req, @PathVariable int id, @PathVariable String email) throws Exception {
        Response<Void> res = new Response<>();
        if (servicesData.show(id).size() == 0) {
            res.setStatus(false);
            res.setMessage("Service Not Available");
            return res;
        }
        if (!usersData.checkUserExistence(email)) {
            res.setStatus(false);
            res.setMessage("User Not Found");
            return res;
        }
        var form = services.createForm(servicesData.show(id).get(0), req.getHandler());
        var user = usersData.show(email).get(0);
        // check if this kind of service supports cash
        if (!form.getHandler().isSupportsCash()) {
            res.setStatus(false);
            res.setMessage("Cash payment is not supported");
            return res;
        }
        Payment payMethodObj = PaymentFactory.createPayment(PaymentFactory.CASH);
        return handlePayment(user, form, payMethodObj);
    }

    private boolean validateCard(Card card) {
        return Pattern.matches("^(\\d{4}[\\s-]?){3}\\d{4}$", card.getCardNumber()) && Pattern.matches("[a-zA-Z\\s]+", card.getHolderName()) && Pattern.matches("\\d{3}", card.getCvv());
    }

    private Response<Void> handlePayment(User user, Form form, Payment payMethodObj) throws Exception {
        var res = new Response<Void>();
        ServicePay payHandler = new ServicePay(user, form, payMethodObj);
        double amount = ServicePay.calculateDueAmount(user, form);
        if (!payHandler.handlePayment()) {
            res.setStatus(false);
            res.setMessage("An error happened while making payment");
        } else {
            res.setStatus(true);
            res.setMessage(String.format(Locale.US, "Paid %.2f successfully", amount));
        }
        return res;
    }
}

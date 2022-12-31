package com.Fawry.app.routes;

import com.Fawry.app.custom.Response;
import com.Fawry.app.helperClasses.Services;
import com.Fawry.app.helperClasses.payment.Card;
import com.Fawry.app.helperClasses.payment.Payment;
import com.Fawry.app.helperClasses.payment.PaymentFactory;
import com.Fawry.app.helperClasses.payment.ServicePay;
import com.Fawry.app.models.PayRequest;
import com.Fawry.app.models.ServicesData;
import com.Fawry.app.models.UsersData;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Locale;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/payment/{id}/{email}")
public class PaymentController {
    ServicesData servicesData;
    UsersData usersData;
    Services services;

    PaymentController() throws SQLException {
        servicesData = new ServicesData();
        usersData = new UsersData();
        services = new Services();
    }

    @PostMapping("/card")
    public Response<Void> payWithCard(@RequestBody PayRequest req, @PathVariable int id, @PathVariable String email) throws Exception {
        return getVoidResponse(req, id, email, PaymentFactory.CARD);
    }

    @PostMapping("/wallet")
    public Response<Void> payWithWallet(@RequestBody PayRequest req, @PathVariable int id, @PathVariable String email) throws Exception {
        return getVoidResponse(req, id, email, PaymentFactory.WALLET);
    }

    @PostMapping("/cash")
    public Response<Void> payWithCash(@RequestBody PayRequest req, @PathVariable int id, @PathVariable String email) throws Exception {
        return getVoidResponse(req, id, email, PaymentFactory.CASH);
    }

    private Response<Void> getVoidResponse(@RequestBody PayRequest req, @PathVariable int id, @PathVariable String email, int payMethod) throws Exception {
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
        if (!form.getHandler().isSupportsCash() && payMethod == PaymentFactory.CASH) {
            res.setStatus(false);
            res.setMessage("Cash payment is not supported");
            return res;
        }
        // check if Card info is valid
        if (payMethod == PaymentFactory.CARD && !validateCard((Card) req.getCard())) {
            res.setStatus(false);
            res.setMessage("Bad Card Information, please check card Info and try again.");
            return res;
        }
        // Initializing Payment method
        Payment payMethodObj;
        if(payMethod!=PaymentFactory.CARD)
            payMethodObj = PaymentFactory.createPayment(payMethod);
        else
            payMethodObj= req.getCard();

        // Handle Payment
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

    private boolean validateCard(Card card) {
        return Pattern.matches("\\d{16}", card.getCardNumber()) && Pattern.matches("[a-zA-Z\\s]+", card.getHolderName()) && Pattern.matches("\\d{3}", card.getCvv());
    }
}

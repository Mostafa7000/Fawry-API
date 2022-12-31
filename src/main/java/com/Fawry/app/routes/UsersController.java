package com.Fawry.app.routes;


import com.Fawry.app.custom.Response;
import com.Fawry.app.helperClasses.Services;
import com.Fawry.app.helperClasses.payment.Card;
import com.Fawry.app.models.PayRequest;
import com.Fawry.app.models.User;
import com.Fawry.app.models.UsersData;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
public class UsersController {

    UsersData users;
    Services services;

    public UsersController() throws SQLException {
        users = new UsersData();
        services = new Services();
    }


    @PostMapping("/signin")
    public Response<User> authenticate(@RequestBody Map<String, String> mp) throws SQLException {
        var res = new Response<User>();
        String email = mp.get("email");
        String password = mp.get("password");
        // check that user exists
        if (users.checkUserExistence(email)) {
            // check that password is correct for that user
            if (users.authenticate(email, password)) {
                res.setStatus(true);
                res.setMessage("successful authentication");
                res.setPayload(users.show(email).get(0));
                return res;
            }
        }
        res.setStatus(false);
        res.setMessage("Unsuccessful authentication");
        return res;

    }

    @PostMapping("/signup")
    public Response<Void> create(@RequestBody Map<String, String> mp) throws SQLException {
        var res = new Response<Void>();
        String email = mp.get("email"), password = mp.get("password"), username = mp.get("username");
        if (users.checkUserExistence(email)) {
            res.setStatus(false);
            res.setMessage("User already exists");
            return res;
        }
        if (!Pattern.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email) || !Pattern.matches("[a-zA-Z\\s]+", username)) {
            res.setStatus(false);
            res.setMessage("Invalid email or name, try again");
        } else {
            var newUser = new User(email, password, username);
            users.create(newUser);
            res.setStatus(true);
            res.setMessage("Account created successfully");
        }
        return res;
    }

    @PostMapping("/{email}/wallet/add")
    public Response<Void> rechargeWallet(@PathVariable String email, @RequestBody PayRequest req) throws Exception {
        return services.addToWallet(users, users.show(email).get(0), (Card) req.getCard(), Double.parseDouble(req.getHandler().get("amount")));
    }

}

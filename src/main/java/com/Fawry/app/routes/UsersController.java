package com.Fawry.app.routes;


import com.Fawry.app.custom.Response;
import com.Fawry.app.models.User;
import com.Fawry.app.models.UsersData;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController{

    UsersData users;

    public UsersController() throws SQLException {
        var users= new UsersData();
    }


    @PostMapping("/signin")
    public Response<User> authenticate(@RequestBody Map<String, String> mp) throws SQLException {
        var res= new Response<User>();
        String email= mp.get("email");
        String password= mp.get("password");
        // check that user exists
        if(users.checkUserExistence(email)){
            // check that password is correct for that user
            if (users.authenticate(email, password)){
                res.setStatus(true);
                res.setMessage("successful authentication");
                res.setObject(users.show(email).get(0));
                return res;
            }
        }
            res.setStatus(false);
            res.setMessage("Unsuccessful authentication");
            return res;

    }

    @PostMapping("/signup")
    public Response<Void> create(@RequestBody Map<String, String> mp) throws SQLException {
        var res= new Response<Void>();
        String email= mp.get("email"), password= mp.get("password"), username= mp.get("username");
        if(users.checkUserExistence(email)){
            res.setStatus(false);
            res.setMessage("User already exists");
            return res;
        }
        else{
            var newUser= new User(email, password, username);
            users.create(newUser);
            res.setStatus(true);
            res.setMessage("Account created successfully");
            return res;
        }
    }
}

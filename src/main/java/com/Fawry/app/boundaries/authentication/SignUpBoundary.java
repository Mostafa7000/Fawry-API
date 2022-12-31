package com.Fawry.app.boundaries.authentication;


import com.Fawry.app.models.User;

import java.util.regex.Pattern;

public class SignUpBoundary extends Authentication {

    public SignUpBoundary() throws Exception {
        onCreate();
    }

    final void onCreate() throws Exception {
        inputUserData();
    }

    public void inputUserData() throws Exception {
        String email, userName, password;
        do {
            System.out.println("Enter your email address: ");
            email = scanner.next();
            while (!Pattern.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
                System.out.println("please enter a valid email address");
                email = scanner.next();
            }
            System.out.println("Enter a user name: ");
            userName = scanner.next();
            while (!Pattern.matches("[a-zA-Z\\s]+", userName)) {
                System.out.println("Please enter a valid user name: ");
                userName = scanner.next();
            }
            System.out.println("Please choose a password: ");
            password = scanner.next();
            signUp(userName, email, password);
        } while (true);
    }

    public void signUp(String name, String email, String password) throws Exception {

        if (data.checkUserExistence(email)) {
            System.out.println("This email already exist!");
            handleTryAgainInput();
        } else {
            data.create(new User(email, password, name, 0, 0, false));
            throw new Exception("Sign up is successfully!");
        }
    }

    final void handleTryAgainInput() throws Exception {
        do {
            System.out.println("Select option");
            System.out.println("1. Sign up again");
            System.out.println("2. Return to previous menu");

            String selection = scanner.next();

            if (selection.equals("1")) {
                break;
            }
            if (selection.equals("2")) {
                throw new Exception("Switching to previous menu...");
            } else {
                System.out.println("Please select a valid input");
            }
        } while (true);
    }
}

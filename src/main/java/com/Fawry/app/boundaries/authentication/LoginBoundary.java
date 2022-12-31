package com.Fawry.app.boundaries.authentication;

import com.Fawry.app.boundaries.homeBoundary.HomeBoundaryFactory;
import com.Fawry.app.models.User;

import java.sql.SQLException;

public class LoginBoundary extends Authentication {

    public LoginBoundary() throws Exception {
        onCreate();
    }

    final void onCreate() throws Exception {
        inputUserData();
    }

    final void inputUserData() throws Exception {
        String email, password;

        do {
            System.out.println("Enter email");
            email = scanner.next();
            System.out.println("Enter password");
            password = scanner.next();

            User user = validateUserInput(email, password);

            if (user == null) {
                System.out.println("Invalid username or password!");
                handleTryAgainInput();
            } else {
                System.out.println("Successfully logged in!");
                initializeHomeBoundary(user);
            }
        } while (true);
    }

    final void initializeHomeBoundary(User user) throws Exception {
        HomeBoundaryFactory homeBoundaryFactory = new HomeBoundaryFactory();
        homeBoundaryFactory.getHomeBoundary(user);
    }

    final void handleTryAgainInput() throws Exception {
        do {
            System.out.println("Select option");
            System.out.println("1. Sign in again");
            System.out.println("2. Return to previous menu");

            String selection = scanner.next();

            if (selection.equals("1")) {
                return;
            }
            if (selection.equals("2")) {
                throw new Exception("Returning to previous menu...");
            } else {
                System.out.println("Please select a valid input");
            }
        } while (true);
    }

    User validateUserInput(String email, String password) throws SQLException {

        if (data.authenticate(email, password))
            return data.show(email).get(0);

        return null;
    }
}

package com.Fawry.app.boundaries;

import com.Fawry.app.boundaries.authentication.AuthenticationFactory;


import java.util.Scanner;

public class BoundaryProducer {
    public BoundaryProducer() throws Exception {
        displayAuthenticationMenu();
    }

    void displayAuthenticationMenu() throws Exception {
        do {
            System.out.println("Select option");
            System.out.println("1. Login");
            System.out.println("2. SignUp");
            System.out.println("3. Terminate");

            Scanner scanner = new Scanner(System.in);
            String selection = scanner.next();

            if (selection.equals("1") || selection.equals("2")) {
                try {
                    AuthenticationFactory authenticationFactory = new AuthenticationFactory();
                    authenticationFactory.getAuthentication(selection);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (selection.equals("3")) {
                throw new Exception("Program Terminates...");
            } else {
                System.out.println("Please select a valid input");
            }
        } while (true);
    }
}
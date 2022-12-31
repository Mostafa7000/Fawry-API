package com.Fawry.app.boundaries.authentication;


public class AuthenticationFactory {

    public Authentication getAuthentication(String selection) throws Exception {
        if (selection.equals("1")) {
            return new LoginBoundary();
        } else if (selection.equals("2")) {
            return new SignUpBoundary();
        }
        return null;
    }
}

package com.Fawry.app.boundaries.authentication;

import com.Fawry.app.models.UsersData;

import java.sql.SQLException;
import java.util.Scanner;

public abstract class Authentication {
    Scanner scanner = new Scanner(System.in);
    UsersData data = new UsersData();

    protected Authentication() throws SQLException {
    }

    abstract void onCreate() throws Exception;

    abstract void inputUserData() throws Exception;

    abstract void handleTryAgainInput() throws Exception;
}
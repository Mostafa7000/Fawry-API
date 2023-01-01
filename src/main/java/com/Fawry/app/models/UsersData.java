package com.Fawry.app.models;

import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class UsersData {

    private final Connection conn;
    private String st;

    public UsersData() throws SQLException {
        String dbURL = "jdbc:sqlite:./database/fawry.db";
        this.conn = DriverManager.getConnection(dbURL);
//        this.st= conn.createStatement();
    }

    /**
     * @param pass The password to hash
     * @return a string representing sha256 encryption of the password
     */
    private String sha256(String pass) {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedHash = digest.digest(pass.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte hash : encodedHash) {
            String hex = Integer.toHexString(0xff & hash);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private List<User> mapUser(ResultSet rs) throws SQLException {
        List<User> result = new ArrayList<>();
        while (rs.next()) {
            User myUser = new User();
            myUser.setEmail(rs.getString("email"));
            myUser.setPassword(rs.getString("password"));
            myUser.setName(rs.getString("name"));
            myUser.setBalance(rs.getDouble("balance"));
            myUser.setDiscount(rs.getDouble("discount"));
            myUser.setAdmin(rs.getInt("admin") == 1);

            result.add(myUser);
        }
        return result;
    }

    public List<User> index() throws SQLException {
        String query = """
                SELECT *
                FROM users""";
        var st = conn.createStatement();
        var rs = st.executeQuery(query);
        return mapUser(rs);
    }

    public int create(User aUser) throws SQLException {
        String query = """
                INSERT INTO users (email, password, name, balance, discount, admin)
                VALUES (?, ?, ?, ?, ?, ?)""";
        PreparedStatement st = conn.prepareStatement(query);
        st.setString(1, aUser.getEmail().toLowerCase());
        st.setString(2, sha256(aUser.getPassword()));
        st.setString(3, aUser.getName());
        st.setDouble(4, aUser.getBalance());
        st.setDouble(5, aUser.getDiscount());
        st.setInt(6, aUser.isAdmin() ? 1 : 0);

        return st.executeUpdate();
    }

    public List<User> show(String email) throws SQLException {
        String query = """
                SELECT *
                FROM users
                WHERE email=?""";
        PreparedStatement st = conn.prepareStatement(query);
        st.setString(1, email.toLowerCase());
        ResultSet rs = st.executeQuery();

        return mapUser(rs);
    }

    public boolean authenticate(String email, String password) throws SQLException {
        var myUser = show(email.toLowerCase());
        if (myUser.size() > 0) {
            return myUser.get(0).getPassword().equals(sha256(password));
        }
        return false;
    }

    public boolean checkUserExistence(String email) throws SQLException {
        var myUser = show(email.toLowerCase());
        return myUser.size() > 0;
    }

    public boolean updateBalance(String email, double amount) throws SQLException {
        var user = show(email.toLowerCase());
        if (user.size() > 0) {
            String query = """
                    UPDATE users
                    SET balance=?
                    WHERE email=?""";
            var st = conn.prepareStatement(query);
            st.setString(2, email.toLowerCase());
            st.setDouble(1, amount);

            return st.executeUpdate() == 1;
        }
        return false;
    }

    public int setDiscount(String user_email, double discount) throws SQLException {
        String query = """
                UPDATE users
                SET discount=?
                WHERE email=?""";
        PreparedStatement st = conn.prepareStatement(query);
        st.setDouble(1, discount);
        st.setString(2, user_email);

        return st.executeUpdate();
    }


}

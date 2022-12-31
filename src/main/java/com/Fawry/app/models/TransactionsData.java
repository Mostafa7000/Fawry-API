package com.Fawry.app.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionsData {

    private final Connection conn;

    public TransactionsData() throws SQLException {
        String dbURL = "jdbc:sqlite:./database/fawry.db";
        this.conn = DriverManager.getConnection(dbURL);
    }

    private List<Transaction> mapResult(ResultSet rs) throws SQLException {
        var result = new ArrayList<Transaction>();
        while (rs.next()) {
            var aTransaction = new Transaction();

            aTransaction.setId(rs.getInt("id"));
            aTransaction.setServiceId(rs.getInt("service_id"));
            aTransaction.setUserEmail(rs.getString("user_email"));
            aTransaction.setAmount(rs.getDouble("amount"));
            aTransaction.setRefund((rs.getString("refund") != null) ? rs.getString("refund") : "");

            result.add(aTransaction);
        }
        return result;
    }

    public List<Transaction> index() throws SQLException {
        String query = """
                SELECT *
                FROM transactions;""";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        return mapResult(rs);
    }

    public List<Transaction> show(int id) throws SQLException {
        var query = """
                SELECT *
                FROM transactions
                WHERE id=?""";

        PreparedStatement st = conn.prepareStatement(query);
        st.setInt(1, id);
        var rs = st.executeQuery();
        return mapResult(rs);
    }

    public int create(Transaction aTransaction) throws SQLException {
        String query = """
                INSERT INTO transactions (service_id, user_email, amount)
                VALUES (?, ?, ?)""";

        PreparedStatement st = conn.prepareStatement(query);
        st.setInt(1, aTransaction.getServiceId());
        st.setString(2, aTransaction.getUserEmail());
        st.setDouble(3, aTransaction.getAmount());

        return st.executeUpdate();
    }

    public List<Transaction> showMyTransactions(String email) throws SQLException {
        String query = """
                SELECT *
                FROM transactions
                WHERE user_email=?;""";
        var st = conn.prepareStatement(query);
        st.setString(1, email);
        var rs = st.executeQuery();
        return mapResult(rs);
    }

    public List<Transaction> showRefundRequests() throws SQLException {
        String query = """
                SELECT *
                FROM transactions
                WHERE refund='pending';""";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        return mapResult(rs);
    }

    public boolean requestRefund(int id) throws SQLException {
        var transaction = show(id);
        if (transaction.size() > 0 && !transaction.get(0).getRefund().equals("refunded")) {
            String query = """
                    UPDATE transactions
                    SET refund=?
                    WHERE id=?""";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, "pending");
            st.setInt(2, id);

            return st.executeUpdate() == 1;
        }
        return false;
    }

    public boolean approveOrReject(int id, boolean approve) throws SQLException {
        var transaction = show(id);
        if (transaction.size() > 0 && transaction.get(0).getRefund().equals("pending")) {
            String query = """
                    UPDATE transactions
                    SET refund=?
                    WHERE id=?""";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(2, id);
            if (approve)
                st.setString(1, "refunded");
            else
                st.setString(1, "rejected");

            return st.executeUpdate() == 1;
        }
        return false;
    }
}

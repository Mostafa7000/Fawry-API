package com.Fawry.app.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicesData {
    private final Connection conn;


    public ServicesData() throws SQLException {
        String dbURL = "jdbc:sqlite:./database/fawry.db";
        this.conn = DriverManager.getConnection(dbURL);
    }

    /**
     * Helper function to map the result of the query to a list of objects
     *
     * @param rs the result of the query
     * @return an array list of objects mapping to database table (services)
     */
    private List<Service> mapResult(ResultSet rs) throws SQLException {
        var result = new ArrayList<Service>();
        while (rs.next()) {
            var aService = new Service();

            aService.setId(rs.getInt("id"));
            aService.setServiceType(ServiceTypes.getEnumType(rs.getString("service_type")));
            aService.setProvider(rs.getString("provider"));
            aService.setPrice(rs.getDouble("price"));
            aService.setDiscount(rs.getDouble("discount"));

            result.add(aService);
        }
        return result;
    }

    public List<Service> index() throws SQLException {
        String query = """
                SELECT *
                FROM services;""";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        return mapResult(rs);
    }

    public List<Service> show(int id) throws SQLException {
        var query = """
                SELECT *
                FROM services
                WHERE id=?""";

        PreparedStatement st = conn.prepareStatement(query);
        st.setInt(1, id);
        var rs = st.executeQuery();
        return mapResult(rs);
    }

    public int create(Service aService) throws SQLException {
        String query = """
                INSERT INTO services (id, service_type, provider, price, discount)
                VALUES (?, ?, ?, ?, ?)""";
        PreparedStatement st = conn.prepareStatement(query);
        st.setInt(1, aService.getId());
        st.setString(2, aService.getServiceType().getDatabaseAcronym());
        st.setString(3, aService.getProvider());
        st.setDouble(4, aService.getPrice());
        st.setDouble(5, aService.getDiscount());

        return st.executeUpdate();
    }

    public int setDiscount(int id, double discount) throws SQLException {
        String query = """
                UPDATE services
                SET discount=?
                WHERE id=?""";
        PreparedStatement st = conn.prepareStatement(query);
        st.setDouble(1, discount);
        st.setInt(2, id);

        return st.executeUpdate();
    }
}

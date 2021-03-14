package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Main;
import model.Customer;
import util.DBQuery;
import util.Session;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDao implements Dao<Customer> {

    /**
     * This method queries the customers table and returns all records
     *
     * @return
     * @throws SQLException
     */
    @Override
    public ObservableList<Customer> getAll() throws SQLException {

        // Create observable list
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM customers";
        DBQuery.setPreparedStatement(Main.conn, selectStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();

        // Execute statement and capture results
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // Create result object
        while (rs.next()) {
            Customer row = new Customer(
                    rs.getString("Customer_Name"),
                    rs.getString("Address"),
                    rs.getString("Postal_Code"),
                    rs.getString("Phone"),
                    Session.toLocal(rs.getTimestamp("Create_Date")),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Division_ID"));
            row.setCustomerId(rs.getInt("Customer_ID"));
            row.setLastUpdated(Session.toLocal(rs.getTimestamp("Last_Update")));
            allCustomers.add(row);
        }
        return allCustomers;
    }

    /**
     * This method queries the customers table and returns all records that match the provided customer ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public Customer getById(int id) throws SQLException {

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM customers WHERE Customer_ID = ?";
        DBQuery.setPreparedStatement(Main.conn, selectStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setInt(1, id);

        // Execute statement and capture results
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // Create result object
        rs.next();
        if (rs.isLast()) {
            Customer customer = new Customer(
                    rs.getString("Customer_Name"),
                    rs.getString("Address"),
                    rs.getString("Postal_Code"),
                    rs.getString("Phone"),
                    Session.toLocal(rs.getTimestamp("Create_Date")),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Division_ID"));
            customer.setCustomerId(rs.getInt("Customer_ID"));
            customer.setLastUpdated(Session.toLocal(rs.getTimestamp("Last_Update")));
            return customer;
        }
        else
            return null;
    }

    /**
     * This method uses the provided customer object to create a new record in the customers table
     *
     * @param customer
     * @throws SQLException
     */
    @Override
    public void create(Customer customer) throws SQLException {

        // Set create statement string and prepared statement
        String createStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Updated_By, Division_ID)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        DBQuery.setPreparedStatement(Main.conn, createStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, customer.getCustomerName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setTimestamp(5, Session.toUTC(customer.getCreatedDate()));
        ps.setString(6, customer.getCreatedBy());
        ps.setString(7, customer.getLastUpdatedBy());
        ps.setInt(8, customer.getDivisionId());

        // Execute statement
        ps.execute();

        // Confirm execution
        if (ps.getUpdateCount() > 0)
            System.out.println(ps.getUpdateCount() + " rows affected!");
        else
            System.out.println("No changes made!");
    }

    /**
     * This method uses the provided customer object to update the matching record in the customers table
     *
     * @param customer
     * @throws SQLException
     */
    @Override
    public void update(Customer customer) throws SQLException {

        // Set create statement string and prepared statement
        String updateStatement = "UPDATE customers SET " +
                "Customer_Name = ?, " +     // Param 1
                "Address = ?, " +           // Param 2
                "Postal_Code = ?, " +       // Param 3
                "Phone = ?, " +             // Param 4
                "Last_Update = ?, " +       // Param 5
                "Last_Updated_By = ?, " +   // Param 6
                "Division_ID = ? " +       // Param 7
                "WHERE Customer_ID = ?";    // Param 8
        DBQuery.setPreparedStatement(Main.conn, updateStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, customer.getCustomerName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setTimestamp(5, Session.toUTC(customer.getLastUpdated()));
        ps.setString(6, customer.getLastUpdatedBy());
        ps.setInt(7, customer.getDivisionId());
        ps.setInt(8, customer.getCustomerId());

        // Execute statement
        ps.execute();

        // Confirm execution
        if (ps.getUpdateCount() > 0)
            System.out.println(ps.getUpdateCount() + " rows affected!");
        else
            System.out.println("No changes made!");
    }

    /**
     * This method uses the provided customer object to delete the matching record from the customers table
     *
     * @param customer
     * @throws SQLException
     */
    @Override
    public void delete(Customer customer) throws SQLException {

        // Set create statement string and prepared statement
        String deleteStatement = "DELETE FROM customers WHERE Customer_ID = ?";
        DBQuery.setPreparedStatement(Main.conn, deleteStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setInt(1, customer.getCustomerId());

        // Execute statement
        ps.execute();

        // Confirm execution
        if (ps.getUpdateCount() > 0)
            System.out.println(ps.getUpdateCount() + " rows affected!");
        else
            System.out.println("No changes made!");
    }

    /**
     * This method queries the customers table to obtain the next auto increment ID value
     *
     * @return
     * @throws SQLException
     */
    public int getNextId() throws SQLException {

        // Set create statement string and prepared statement
        String selectStatement = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'WJ081aW' AND TABLE_NAME = 'customers'";
        DBQuery.setPreparedStatement(Main.conn, selectStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();

        // Execute statement and capture results
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // Create result object
        rs.next();
        if (rs.isLast()) {
            return rs.getInt("AUTO_INCREMENT");
        }
        else
            return -1;
    }
}

package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Main;
import model.Contact;
import util.DBQuery;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDao implements Dao<Contact> {

    /**
     * This function queries the contacts table and returns all records
     *
     * @return
     * @throws SQLException
     */
    @Override
    public ObservableList<Contact> getAll() throws SQLException {

        // Create observable list
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM contacts";
        DBQuery.setPreparedStatement(Main.conn, selectStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();

        // Execute statement and capture results
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // Create result object
        while (rs.next()) {
            Contact row = new Contact(
                    rs.getString("Contact_Name"),
                    rs.getString("Email"));
            row.setContactId(rs.getInt("Contact_ID"));
            allContacts.add(row);
        }
        return allContacts;
    }

    /**
     * This method queries the contacts table and returns all records that match the provided contact ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public Contact getById(int id) throws SQLException {

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM contacts WHERE Contact_ID = ?";
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
            Contact contact = new Contact(
                    rs.getString("Contact_Name"),
                    rs.getString("Email"));
            contact.setContactId(rs.getInt("Contact_ID"));
            return contact;
        }
        else
            return null;
    }

    /**
     * The methods below are inherited by the DAO interface
     * They are not used
     *
     * @param t
     * @throws SQLException
     */
    // Unused methods
    @Override
    public void create(Contact t) throws SQLException {}

    @Override
    public void update(Contact t) throws SQLException {}

    @Override
    public void delete(Contact t) throws SQLException {}
}

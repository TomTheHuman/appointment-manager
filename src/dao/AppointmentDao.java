package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Main;
import model.Appointment;
import util.DBQuery;
import util.Session;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

public class AppointmentDao implements Dao<Appointment> {

    /**
     * This method queries the database for all items from the appointments table
     *
     * @return allAppointments
     * @throws SQLException
     */
    @Override
    public ObservableList<Appointment> getAll() throws SQLException {

        // Create observable list
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM appointments";
        DBQuery.setPreparedStatement(Main.conn, selectStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();

        // Execute statement and capture results
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // Create result object
        while (rs.next()) {
            Appointment row = new Appointment(
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getString("Location"),
                    rs.getString("Type"),
                    Session.toLocal(rs.getTimestamp("Start")),
                    Session.toLocal(rs.getTimestamp("End")),
                    Session.toLocal(rs.getTimestamp("Create_Date")),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"),
                    rs.getInt("Contact_ID"));
            row.setAppointmentId(rs.getInt("Appointment_ID"));
            row.setLastUpdated(Session.toLocal(rs.getTimestamp("Last_Update")));
            allAppointments.add(row);
        }
        return allAppointments;
    }

    /**
     * This method queries the database for all appointments that match the provided appointment ID
     *
     * @param id
     * @return result
     * @throws SQLException
     */
    @Override
    public Appointment getById(int id) throws SQLException {

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM appointments WHERE Appointment_ID = ?";
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
            Appointment result = new Appointment(
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getString("Location"),
                    rs.getString("Type"),
                    Session.toLocal(rs.getTimestamp("Start")),
                    Session.toLocal(rs.getTimestamp("End")),
                    Session.toLocal(rs.getTimestamp("Create_Date")),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"),
                    rs.getInt("Contact_ID"));
            result.setAppointmentId(rs.getInt("Appointment_ID"));
            result.setLastUpdated(Session.toLocal(rs.getTimestamp("Last_Update")));
            return result;
        }
        else
            return null;
    }

    /**
     * This method queries the database and returns all appointments that match the provided customer ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public ObservableList<Appointment> getAllByCustomerId(int id) throws SQLException {

        // Create observable list
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM appointments WHERE Customer_ID = ?";
        DBQuery.setPreparedStatement(Main.conn, selectStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setInt(1, id);

        // Execute statement and capture results
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // Create result object
        while (rs.next()) {
            Appointment row = new Appointment(
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getString("Location"),
                    rs.getString("Type"),
                    Session.toLocal(rs.getTimestamp("Start")),
                    Session.toLocal(rs.getTimestamp("End")),
                    Session.toLocal(rs.getTimestamp("Create_Date")),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"),
                    rs.getInt("Contact_ID"));
            row.setAppointmentId(rs.getInt("Appointment_ID"));
            row.setLastUpdated(Session.toLocal(rs.getTimestamp("Last_Update")));
            allAppointments.add(row);
        }
        return allAppointments;
    }

    /**
     * This method returns report data with a sum of all appointments by type and month
     *
     * @return
     * @throws SQLException
     */
    public Stack appsByMonthType() throws SQLException {

        // Create rows array
        Stack appRows = new Stack();

        // Set create statement string and prepared statement
        String selectStatement = "SELECT Type, MONTH(Start) AS Month_Num, MONTHNAME(Start) AS Month, COUNT(Customer_ID) AS Total FROM appointments GROUP BY Type, Month, Month_Num ORDER BY Type, Month_Num";
        DBQuery.setPreparedStatement(Main.conn, selectStatement);

        // Set and execute prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // Create result object
        appRows.push("TYPE            | MONTH        | TOTAL");

        while (rs.next()) {
            String TYPE = rs.getString("Type");
            String MONTH = rs.getString("Month");
            int TOTAL = rs.getInt("Total");

            String row = String.format("%-15s | %-12s | %-5s", TYPE, MONTH, TOTAL);
            appRows.add(row);
        }
        return appRows;
    }

    /**
     * This method queries the database for appointments that match the provided contact ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public ObservableList<Appointment> appSchedByContact(int id) throws SQLException {

        // Create rows array
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM appointments WHERE Contact_ID = ? ORDER BY Start";
        DBQuery.setPreparedStatement(Main.conn, selectStatement);

        // Set and execute prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setInt(1, id);
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            Appointment row = new Appointment(
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getString("Location"),
                    rs.getString("Type"),
                    Session.toLocal(rs.getTimestamp("Start")),
                    Session.toLocal(rs.getTimestamp("End")),
                    Session.toLocal(rs.getTimestamp("Create_Date")),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"),
                    rs.getInt("Contact_ID"));
            row.setAppointmentId(rs.getInt("Appointment_ID"));
            row.setLastUpdated(Session.toLocal(rs.getTimestamp("Last_Update")));
            appointments.add(row);
        }
        return appointments;
    }

    /**
     * This method queries the database for all appointments that match the provided user name
     *
     * @param username
     * @return
     * @throws SQLException
     */
    public ObservableList<Appointment> appSchedByUser(String username) throws SQLException {

        // Create rows array
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM appointments WHERE Created_By = ? ORDER BY Start";
        DBQuery.setPreparedStatement(Main.conn, selectStatement);

        // Set and execute prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, username);
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            Appointment row = new Appointment(
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getString("Location"),
                    rs.getString("Type"),
                    Session.toLocal(rs.getTimestamp("Start")),
                    Session.toLocal(rs.getTimestamp("End")),
                    Session.toLocal(rs.getTimestamp("Create_Date")),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"),
                    rs.getInt("Contact_ID"));
            row.setAppointmentId(rs.getInt("Appointment_ID"));
            row.setLastUpdated(Session.toLocal(rs.getTimestamp("Last_Update")));
            appointments.add(row);
        }
        return appointments;
    }

    /**
     * This method uses the provided appointment object to insert the data into the appointments table
     *
     * @param app
     * @throws SQLException
     */
    @Override
    public void create(Appointment app) throws SQLException {

        // Set create statement string and prepared statement
        String createStatement = "INSERT INTO appointments(Title, Description, Location, Type, " +
                "Start, End, Create_Date, Created_By, Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        DBQuery.setPreparedStatement(Main.conn, createStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, app.getTitle());
        ps.setString(2, app.getDescription());
        ps.setString(3, app.getLocation());
        ps.setString(4, app.getType());
        ps.setTimestamp(5, Session.toUTC(app.getStartDate()));
        ps.setTimestamp(6, Session.toUTC(app.getEndDate()));
        ps.setTimestamp(7, Session.toUTC(app.getCreatedDate()));
        ps.setString(8, app.getCreatedBy());
        ps.setString(9, app.getLastUpdatedBy());
        ps.setInt(10, app.getCustomerId());
        ps.setInt(11, app.getUserId());
        ps.setInt(12, app.getContactId());

        // Execute statement
        ps.execute();

        // Confirm execution
        if (ps.getUpdateCount() > 0)
            System.out.println(ps.getUpdateCount() + " rows affected!");
        else
            System.out.println("No changes made!");
    }

    /**
     * This method uses the provided appointment object to update the tuple with the same appointment ID
     *
     * @param app
     * @throws SQLException
     */
    @Override
    public void update(Appointment app) throws SQLException {

        // Set create statement string and prepared statement
        String updateStatement = "UPDATE appointments SET " +
                "Title = ?, " +             // Param 1
                "Description = ?, " +       // Param 2
                "Location = ?, " +          // Param 3
                "Type = ?, " +              // Param 4
                "Start = ?, " +             // Param 5
                "End = ?, " +               // Param 6
                "Last_Updated_By = ?, " +   // Param 7
                "Last_Update = ?, " +       // Param 8
                "Customer_ID = ?, " +       // Param 9
                "User_ID = ?, " +           // Param 10
                "Contact_ID = ? " +         // Param 11
                "WHERE Appointment_ID = ?"; // Param 12
        DBQuery.setPreparedStatement(Main.conn, updateStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, app.getTitle());
        ps.setString(2, app.getDescription());
        ps.setString(3, app.getLocation());
        ps.setString(4, app.getType());
        ps.setTimestamp(5, Session.toUTC(app.getStartDate()));
        ps.setTimestamp(6, Session.toUTC(app.getEndDate()));
        ps.setString(7, app.getLastUpdatedBy());
        ps.setTimestamp(8, Session.toUTC(app.getLastUpdated()));
        ps.setInt(9, app.getCustomerId());
        ps.setInt(10, app.getUserId());
        ps.setInt(11, app.getContactId());
        ps.setInt(12, app.getAppointmentId());

        // Execute statement
        ps.execute();

        // Confirm execution
        if (ps.getUpdateCount() > 0)
            System.out.println(ps.getUpdateCount() + " rows affected!");
        else
            System.out.println("No changes made!");
    }

    /**
     * This method queries the database and deletes the appointment that match the provided appointment ID
     *
     * @param app
     * @throws SQLException
     */
    @Override
    public void delete(Appointment app) throws SQLException {

        // Set create statement string and prepared statement
        String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";
        DBQuery.setPreparedStatement(Main.conn, deleteStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setInt(1, app.getAppointmentId());

        // Execute statement
        ps.execute();

        // Confirm execution
        if (ps.getUpdateCount() > 0)
            System.out.println(ps.getUpdateCount() + " rows affected!");
        else
            System.out.println("No changes made!");
    }

    /**
     * This method deletes all appointments that match the provided customer ID
     *
     * @param id
     * @throws SQLException
     */
    public void deleteAllFor(int id) throws SQLException {

        // Set create statement string and prepared statement
        String deleteStatement = "DELETE FROM appointments WHERE Customer_ID = ?";
        DBQuery.setPreparedStatement(Main.conn, deleteStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setInt(1, id);

        // Execute statement
        ps.execute();

        // Confirm execution
        if (ps.getUpdateCount() > 0)
            System.out.println(ps.getUpdateCount() + " rows affected!");
        else
            System.out.println("No changes made!");
    }

    /**
     * This method queries the appointments table to find out what the next auto increment ID value will be
     *
     * @return
     * @throws SQLException
     */
    public int getNextId() throws SQLException {

        // Set create statement string and prepared statement
        String selectStatement = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'WJ081aW' AND TABLE_NAME = 'appointments'";
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

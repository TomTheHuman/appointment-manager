package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Main;
import model.User;
import util.DBQuery;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao implements Dao<User> {

    /**
     * This method queries the users table and returns all records
     *
     * @return
     * @throws SQLException
     */
    @Override
    public ObservableList<User> getAll() throws SQLException {

        // Create observable list
        ObservableList<User> allUsers = FXCollections.observableArrayList();

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM users";
        DBQuery.setPreparedStatement(Main.conn, selectStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();

        // Execute statement and capture results
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // Create result object
        while (rs.next()) {
            User row = new User(
                    rs.getString("User_Name"),
                    rs.getString("Password"),
                    rs.getDate("Create_Date"),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"));
            row.setUserId(rs.getInt("User_ID"));
            allUsers.add(row);
        }
        return allUsers;
    }

    /**
     * This method queries the users table and returns the record that matches the provided user ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public User getById(int id) throws SQLException {

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM users WHERE User_ID = ?";
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
            User user = new User(
                    rs.getString("User_Name"),
                    rs.getString("Password"),
                    rs.getDate("Create_Date"),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"));
            user.setUserId(rs.getInt("User_ID"));
            return user;
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
    public void create(User t) throws SQLException {}

    @Override
    public void update(User t) throws SQLException {}

    @Override
    public void delete(User t) throws SQLException {}

}

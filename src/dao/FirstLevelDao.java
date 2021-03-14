package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Main;
import model.FirstLevelDiv;
import util.DBQuery;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDao implements Dao<FirstLevelDiv> {

    /**
     * This method queries the first level divisions table and returns all records
     *
     * @return
     * @throws SQLException
     */
    @Override
    public ObservableList<FirstLevelDiv> getAll() throws SQLException {

        // Create observable list
        ObservableList<FirstLevelDiv> allFirstLevelDivs = FXCollections.observableArrayList();

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM first_level_divisions";
        DBQuery.setPreparedStatement(Main.conn, selectStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();

        // Execute statement and capture results
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // Create result object
        while (rs.next()) {
            FirstLevelDiv row = new FirstLevelDiv(
                    rs.getString("Division"),
                    rs.getDate("Create_Date"),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("COUNTRY_ID"));
            row.setDivisionId(rs.getInt("Division_ID"));
            row.setLastUpdated(rs.getTimestamp("Last_Update"));
            allFirstLevelDivs.add(row);
        }
        return allFirstLevelDivs;
    }

    /**
     * This method queries the first level divisions table and returns the record that matches the provided ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public FirstLevelDiv getById(int id) throws SQLException {

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
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
            FirstLevelDiv fLevel = new FirstLevelDiv(
                    rs.getString("Division"),
                    rs.getDate("Create_Date"),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"),
                    rs.getInt("COUNTRY_ID"));
            fLevel.setDivisionId(rs.getInt("Division_ID"));
            fLevel.setLastUpdated(rs.getTimestamp("Last_Update"));
            return fLevel;
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
    public void create(FirstLevelDiv t) throws SQLException {}

    @Override
    public void update(FirstLevelDiv t) throws SQLException {}

    @Override
    public void delete(FirstLevelDiv t) throws SQLException {}

}

package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Main;
import model.Country;
import util.DBQuery;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDao implements Dao<Country> {

    /**
     * This method queries the country table and returns all records
     *
     * @return
     * @throws SQLException
     */
    @Override
    public ObservableList<Country> getAll() throws SQLException {

        // Create observable list
        ObservableList<Country> allCountries = FXCollections.observableArrayList();

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM countries";
        DBQuery.setPreparedStatement(Main.conn, selectStatement);

        // Set prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();

        // Execute statement and capture results
        ps.execute();
        ResultSet rs = ps.getResultSet();

        // Create result object
        while (rs.next()) {
            Country row = new Country(
                    rs.getString("Country"),
                    rs.getDate("Create_Date"),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"));
            row.setCountryId(rs.getInt("Country_ID"));
            row.setLastUpdated(rs.getTimestamp("Last_Update"));
            allCountries.add(row);
        }
        return allCountries;
    }

    /**
     * This method queries the country table and returns all records that match the provided country ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public Country getById(int id) throws SQLException {

        // Set create statement string and prepared statement
        String selectStatement = "SELECT * FROM countries WHERE Country_ID = ?";
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
            Country country = new Country(
                    rs.getString("Country"),
                    rs.getDate("Create_Date"),
                    rs.getString("Created_By"),
                    rs.getString("Last_Updated_By"));
            country.setCountryId(rs.getInt("Country_ID"));
            country.setLastUpdated(rs.getTimestamp("Last_Update"));
            return country;
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
    public void create(Country t) throws SQLException {}

    @Override
    public void update(Country t) throws SQLException {}

    @Override
    public void delete(Country t) throws SQLException {}

}

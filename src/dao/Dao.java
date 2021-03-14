package dao;

import javafx.collections.ObservableList;
import java.sql.SQLException;

public interface Dao<Type> {

    /**
     * This method returns an observable list of all results
     *
     * @return
     * @throws SQLException
     */
    // Get all objects
    ObservableList<Type> getAll() throws SQLException;

    /**
     * This method returns an object that matches the provided ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    // Get object by ID
    Type getById(int id) throws SQLException;

    /**
     * This method creates a record in the database
     *
     * @param t
     * @throws SQLException
     */
    // Create object
    void create(Type t) throws SQLException;

    /**
     * This method updates a record in the database
     *
     * @param t
     * @throws SQLException
     */
    // Update object
    void update(Type t) throws SQLException;

    /**
     * This method deletes a record in the database
     *
     * @param t
     * @throws SQLException
     */
    // Delete object
    void delete(Type t) throws SQLException;

}

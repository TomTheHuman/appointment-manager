package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBQuery {

    private static PreparedStatement statement; // Statement reference

    // Create statement object

    /**
     * This method sets the prepared statement to query
     *
     * @param conn
     * @param sqlStatement
     * @throws SQLException
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        statement = conn.prepareStatement(sqlStatement);
    }

    // Return Statement object

    /**
     * This method returns the prepared statement
     *
     * @return
     */
    public static PreparedStatement getPreparedStatement() {
        return statement;
    }

}

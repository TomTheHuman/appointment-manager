package model;

import java.sql.Date;
import java.sql.Timestamp;

public class User {

    // Variables
    private int userId;
    private String username;
    private String password;
    private Date createdDate;
    private String createdBy;
    private Timestamp lastUpdated;
    private String lastUpdatedBy;

    // Constructor

    /**
     *
     * @param username
     * @param password
     * @param createdDate
     * @param createdBy
     * @param lastUpdatedBy
     */
    public User(String username, String password, Date createdDate, String createdBy, String lastUpdatedBy) {
        this.username = username;
        this.password = password;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    // Getters

    /**
     *
     * @return the user ID
     */
     public int getUserId() { return this.userId; }

     /**
     *
     * @return the user name
     */
     public String getUsername() { return this.username; }

     /**
     *
     * @return the password
     */
     public String getPassword() { return this.password; }

     /**
     *
     * @return the created date
     */
     public Date getCreatedDate() { return this.createdDate; }

     /**
     *
     * @return the created by
     */
     public String getCreatedBy() { return this.createdBy; }

     /**
     *
     * @return the last updated date
     */
     public Timestamp getLastUpdated() { return this.lastUpdated; }

     /**
     *
     * @return the last updated by
     */
     public String getLastUpdatedBy() { return this.lastUpdatedBy; }

     // Setters
     /**
     *
     * @param userId the user ID to set
     */
     public void setUserId(int userId) { this.userId = userId; }

     /**
     *
     * @param username the user name to set
     */
     public void setUsername(String username) { this.username = username; }

     /**
     *
     * @param password the password to set
     */
     public void setPassword(String password) { this.password = password; }

     /**
     *
     * @param createdDate the created date to set
     */
     public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

     /**
     *
     * @param createdBy the created by to set
     */
     public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

     /**
     *
     * @param lastUpdated the last updated to set
     */
     public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }

     /**
     *
     * @param lastUpdatedBy the created by to set
     */
     public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }

     // Helpers
     /**
     * Returns user name for combo boxes
      *
     * @return
     */
    @Override
    public String toString() {
        return (username);
    }
}

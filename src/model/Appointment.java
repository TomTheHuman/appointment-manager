package model;

import dao.ContactDao;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Appointment {

    // Variables

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp lastUpdated;
    private String lastUpdatedBy;
    private int customerId;
    private int userId;
    private int contactId;

    // Constructor
    /**
     *
     * @param title
     * @param description
     * @param location
     * @param type
     * @param startDate
     * @param endDate
     * @param createdDate
     * @param createdBy
     * @param lastUpdatedBy
     * @param customerId
     * @param userId
     * @param contactId
     */
    public Appointment(String title, String description, String location, String type, Timestamp startDate, Timestamp endDate,
                       Timestamp createdDate, String createdBy, String lastUpdatedBy, int customerId, int userId, int contactId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    // Setters
    /**
     *
     * @param id id to set
     */
    public void setAppointmentId(int id) { this.appointmentId = id; }

    /**
     *
     * @param title title to set
     */
    public void setTitle(String title) { this.title = title; }

    /**
     *
     * @param description description to set
     */
    public void setDescription(String description) { this.description = description; }

    /**
     *
     * @param location location to set
     */
    public void setLocation(String location) { this.location = location; }

    /**
     *
     * @param type type to set
     */
    public void setType(String type) { this.type = type; }

    /**
     *
     * @param startDate start date to set
     */
    public void setStartDate(Timestamp startDate) { this.startDate = startDate; }

    /**
     *
     * @param endDate end date to set
     */
    public void setEndDate(Timestamp endDate) { this.endDate = endDate; }

    /**
     *
     * @param createdDate created date to set
     */
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }

    /**
     *
     * @param createdBy created by to set
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    /**
     *
     * @param lastUpdated last updated to set
     */
    public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }

    /**
     *
     * @param lastUpdatedBy last updated by to set
     */
    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }

    /**
     *
     * @param customerId customer ID to set
     */
     public void setCustomerId(int customerId) { this.customerId = customerId; }

     /**
     *
     * @param userId user ID to set
     */
     public void setUserId(int userId) { this.userId = userId; }

     /**
     *
     * @param contactId contact ID to set
     */
    public void setContactId(int contactId) { this.contactId = contactId; }


    // Getters
    /**
     *
     * @return the appointment ID
     */
     public int getAppointmentId() { return this.appointmentId; }

     /**
     *
     * @return the title
     */
     public String getTitle() { return this.title; }

     /**
     *
     * @return the description
     */
     public String getDescription() { return this.description; }

     /**
     *
     * @return the location
     */
     public String getLocation() { return this.location; }

     /**
     *
     * @return the type
     */
     public String getType() { return this.type; }

     /**
     *
     * @return the start date
     */
     public Timestamp getStartDate() { return this.startDate; }

     /**
     *
     * @return the end date
     */
     public Timestamp getEndDate() { return this.endDate; }

     /**
     *
     * @return the created date
     */
     public Timestamp getCreatedDate() { return this.createdDate; }

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

     /**
     *
     * @return the customer ID
     */
     public int getCustomerId() { return this.customerId; }

     /**
     *
     * @return the user ID
     */
     public int getUserId() { return this.userId; }

     /**
     *
     * @return the contact ID
     */
     public int getContactId() { return this.contactId; }

     // Helper Getters
     /**
     * Returns the appropriate contact name for the local contact ID
     *
     * @return
     * @throws SQLException
     */
    public String getContact() throws SQLException {
        // Create contact DAO instance
        ContactDao contact = new ContactDao();
        // Retrieve and return contact name from ID
        return contact.getById(this.contactId).getContactName();
    }

}

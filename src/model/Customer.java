package model;

import dao.FirstLevelDao;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Customer {

    // Variables
    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp lastUpdated;
    private String lastUpdatedBy;
    private int divisionId;

    // Constructor

    /**
     *
     * @param customerName
     * @param address
     * @param postalCode
     * @param phone
     * @param createdDate
     * @param createdBy
     * @param lastUpdatedBy
     * @param divisionId
     */
    public Customer(String customerName, String address, String postalCode, String phone, Timestamp createdDate, String createdBy, String lastUpdatedBy, int divisionId) {
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
    }

    // Setters

    /**
     *
     * @param customerId customer ID to set
     */
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    /**
     *
     * @param customerName customer name to set
     */
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    /**
     *
     * @param address address to set
     */
    public void setAddress(String address) { this.address = address; }

    /**
     *
     * @param postalCode postal code to set
     */
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    /**
     *
     * @param phone phone to set
     */
    public void setPhone(String phone) { this.phone = phone; }

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
     * @param divisionId division ID to set
     */
    public void setDivisionId(int divisionId) { this.divisionId = divisionId; }


    // Getters

    /**
     *
     * @return the customer ID
     */
    public int getCustomerId() { return this.customerId; }

    /**
     *
     * @return the customer name
     */
    public String getCustomerName() { return this.customerName; }

    /**
     *
     * @return the address
     */
    public String getAddress() { return this.address; }

    /**
     *
     * @return the postal code
     */
    public String getPostalCode() { return this.postalCode; }

    /**
     *
     * @return the phone
     */
    public String getPhone() { return this.phone; }

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
     * @return the division ID
     */
    public int getDivisionId() { return this.divisionId; }

    // Helpers

    /**
     * Returns the customer name string for combo boxes
     *
     * @return
     */
    @Override
    public String toString() {
        return (customerName);
    }

    /**
     * Returns the division name using local division ID
     *
     * @return
     * @throws SQLException
     */
    public String getDivision() throws SQLException {
        // Create first level division DAO instance
        FirstLevelDao flDao = new FirstLevelDao();
        // Retrieve and return first level div name
        return flDao.getById(this.divisionId).getDivision();
    }
}

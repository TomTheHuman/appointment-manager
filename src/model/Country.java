package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Country {

    // Variables
    private int countryId;
    private String country;
    private Date createdDate;
    private String createdBy;
    private Timestamp lastUpdated;
    private String lastUpdatedBy;

    // Constructor

    /**
     *
     * @param country
     * @param createdDate
     * @param createdBy
     * @param lastUpdatedBy
     */
    public Country(String country, Date createdDate, String createdBy, String lastUpdatedBy) {
        this.country = country;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    // Getters

    /**
     *
     * @return the country ID
     */
    public int getCountryId() { return this.countryId; }

    /**
     *
     * @return the country
     */
    public String getCountry() { return this.country; }

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
     * @param countryId the country ID to set
     */
    public void setCountryId(int countryId) { this.countryId = countryId; }

    /**
     *
     * @param country the country to set
     */
    public void setCountry(String country) { this.country = country; }

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
     * @param lastUpdatedBy the last updated by to set
     */
    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }

    // Helpers
    /**
     * Returns country string for combo boxes
     *
     * @return
     */
    @Override
    public String toString() {
        return (country);
    }

}

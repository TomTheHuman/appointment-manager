package model;

import java.sql.Date;
import java.sql.Timestamp;

public class FirstLevelDiv {

    // Variables
    private int divisionId;
    private String division;
    private Date createdDate;
    private String createdBy;
    private Timestamp lastUpdated;
    private String lastUpdatedBy;
    private int countryId;

    // Constructor

    /**
     *
     * @param division
     * @param createdDate
     * @param createdBy
     * @param lastUpdatedBy
     * @param countryId
     */
    public FirstLevelDiv(String division, Date createdDate, String createdBy, String lastUpdatedBy, int countryId) {
        this.division = division;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    // Getters
    /**
     *
     * @return the division ID
     */
     public int getDivisionId() { return this.divisionId; }

     /**
     *
     * @return the division
     */
     public String getDivision() { return this.division; }

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
     * @return the last updated
     */
     public Timestamp getLastUpdated() { return this.lastUpdated; }

     /**
     *
     * @return the last updated by
     */
     public String getLastUpdatedBy() { return this.lastUpdatedBy; }

     /**
     *
     * @return the country ID
     */
     public int getCountryId() { return this. countryId; }


     // Setters
     /**
     *
     * @param divisionId division ID to set
     */
     public void setDivisionId(int divisionId) { this.divisionId = divisionId; }

     /**
     *
     * @param division division to set
     */
     public void setDivision(String division) { this.division = division; }

     /**
     *
     * @param createdDate created date to set
     */
     public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

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
     * @param countryId country ID to set
     */
     public void setCountryId(int countryId) { this.countryId = countryId; }

     // Helpers
     /**
     * Returns the division for combo boxes
     *
     * @return
     */
    @Override
    public String toString() {
        return (division);
    }

}

package model;

public class Contact {

    // Variables
    private int contactId;
    private String contactName;
    private String email;

    // Constructor

    /**
     *
     * @param contactName
     * @param email
     */
    public Contact(String contactName, String email) {
        this.contactName = contactName;
        this.email = email;
    }

    // Getters

    /**
     *
     * @return the contact ID
     */
    public int getContactId() { return this.contactId; }

    /**
     *
     * @return the contact name
     */
    public String getContactName() { return this.contactName; }

    /**
     *
     * @return the email
     */
    public String getEmail() { return this.email; }

    // Setters

    /**
     *
     * @param contactId contact ID to set
     */
    public void setContactId(int contactId) { this.contactId = contactId; }

    /**
     *
     * @param contactName contact name to set
     */
    public void setContactName(String contactName) { this.contactName = contactName; }

    /**
     *
     * @param email email to set
     */
    public void setEmail(String email) { this.email = email; }

    // Helpers

    /**
     *
     * @return contactName string for combo box items
     */
    @Override
    public String toString() {
        return (contactName);
    }
}

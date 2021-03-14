# Appointment Management System
This application provides the user with an easy way to manage appointments and customers within their organization. 
Tools are provided to allow for creating, modifying, and deleting both customers and appointments in a powerful yet simple system.

## Developer Details
Author: Thomas Shaw
Email: tshaw85@wgu.edu
Version: v1.0.0
Date: 11/28/2020

## Additional Details
IDE: IntelliJ IDEA 2020.2.4 (Community Edition)
IDE Build: #IC-202.8194.7
Java Runtime Version: Java SE 11.0.9+11-b944.49 amd64
JavaFX Version: JavaFX-SDK-11.0.2

## Directions
### Logging In:
After launching the application, you will be prompted to login with the login form.
- Enter your user ID in the appropriate field
- Enter your password in the appropriate field
- Click the Login button to proceed

If your username or password are incorrect, you will receive an error. Please double check your information
and try again if this occurs.

------------------------------------------------------------------------------------
### Managing Customers:
The customers table displays all customers and their information in the database in one easy to view location.

#### Add a Customer
- Click the add button under the customers table
- Provide a customer name, address, postal code, phone number, country and state/province
(Do not include first level division or country data in address field)
(Format phone number as (000) 000-0000)
- Click the save button to save your new customer

You will see new customers on the customer table when you return to the main form.

#### Modify a Customer
- Select a customer and click the modify button under the customer table
(If you do not select a customer, an error will appear. Select a customer and try again if this occurs.)
- Modify any of the available fields on the form
- Click the save button to save your changes

You will see your changes in the customer table when you return to the main form.

#### Delete a Customer
(You cannot delete a customer with appointments. An error will appear if you attempt this. Please see instructions below for managing customer's appointments.)
- Select a customer and click the delete button
(If you do not select a customer, an error will appear. Select a customer and try again if this occurs.)
- You will be asked to confirm your deletion
- Click the OK button to confirm your deletion

The customer will be removed from the table immediately.

#### Viewing/Deleting Customer Appointments
- Select a customer from the table
- Click the View Customer's Appointments button
This page will display all appointments for the selected customer.

To delete a single appointment, select the appointment, click Delete Selected Appointment and confirm your action when prompted.

To delete all appointments for the customer, click Delete All Appointments and confirm your action when prompted. 

Deletions will take effect immediately.

------------------------------------------------------------------------------------
### Managing Appointments:
The appointments table displays all appointments in the system for the current week, or current month. To change the filter, click either the "By Week" or "By Month" radio button.

#### Add an Appointment
- Click the add button under the appointments table
- Provide an appointment title, description, location, contact, type, start date/time, end date/time, customer, and user
(Start and End date/times must be in the format MM-DD-YYYY 00:00 AM)
- Click the save button to save your new appointment

You will see new appointments on the appointments table when you return to the main form.

#### Modify an Appointment
- Select an appointment and click the modify button under the appointments table
(If you do not select an appointment, an error will appear. Select an appointment and try again if this occurs.)
- Modify any of the available fields on the form
- Click the save button to save your changes

You will see your changes in the appointments table when you return to the main form.

#### Delete an Appointment
- Select an appointment and click the delete button
(If you do not select an appointment, an error will appear. Select an appointment and try again if this occurs.)
- You will be asked to confirm your deletion
- Click the OK button to confirm your deletion

The appointment will be removed from the table immediately.

------------------------------------------------------------------------------------
### Reports:
Report provide insightful information regarding customers and appointments in the system.

#### Run a Report:
- Click the drop-down menu below the appointments table
- Select a report from the list
- Click the Run Report button to view the report

#### Report Details:
##### Customer Appointment Totals by Type & Month
This report breaks down the total number of appointments across all customers, users and contacts, grouped by appointment type and month.

##### Appointments Schedule by Contact
This report provides an appointments schedule for the selected contact.
- Click the drop-down menu above the table
- Select a contact to view their appointments schedule

##### Appointments by Created-By User (Additional Report)
This report provides a list of appointments created by the selected user.
- Click the drop-down menu above the table
- Select a user to view their created appointments


### Exit
To exit the program, return to the main form and click exit.


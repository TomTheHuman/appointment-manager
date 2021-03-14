package controller;

import dao.AppointmentDao;
import dao.ContactDao;
import dao.CustomerDao;
import dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;
import util.Session;
import util.Validate;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ModifyAppointment implements Initializable {

    /**
     * Stage variable for navigation
     */
    Stage stage;

    /**
     * Scene variable for navigation
     */
    Parent scene;

    /**
     * Text field for appointment ID
     */
    @FXML
    private TextField textFieldAppointmentId;

    /**
     * Label for title
      */
    @FXML
    private Label labelTitle;

    /**
     * Text field for title
     */
    @FXML
    private TextField textFieldTitle;

    /**
     * Label for description
     */
    @FXML
    private Label labelDescription;

    /**
     * Text field for description
     */
    @FXML
    private TextField textFieldDescription;

    /**
     * Label for location
     */
    @FXML
    private Label labelLocation;

    /**
     * Text field for location
     */
    @FXML
    private TextField textFieldLocation;

    /**
     * Label for contact
     */
    @FXML
    private Label labelContact;

    /**
     * Combo box for contact
     */
    @FXML
    private ComboBox<Contact> comboContact;

    /**
     * Label for type
     */
    @FXML
    private Label labelType;

    /**
     * Text field for type
     */
    @FXML
    private TextField textFieldType;

    /**
     * Label for start date/time
     */
    @FXML
    private Label labelStartDate;

    /**
     * Text field for start date/time
     */
    @FXML
    private TextField textFieldStartDate;

    /**
     * Label for end date/time
     */
    @FXML
    private Label labelEndDate;

    /**
     * Text field for end date/time
     */
    @FXML
    private TextField textFieldEndDate;

    /**
     * Label for customer
     */
    @FXML
    private Label labelCustomer;

    /**
     * Label for user ID
     */
    @FXML
    private Label labelUserId;

    /**
     * Combo box for customer ID
     */
    @FXML
    private ComboBox<Customer> comboCustomerId;

    /**
     * Combo box for user ID
     */
    @FXML
    private ComboBox<User> comboUserId;

    /**
     * AppointmentDao instance for data access
     */
    private AppointmentDao appDao;

    /**
     * Appointment variable for storing current appointment
     */
    private Appointment appointment;

    /**
     * ContactDao instance for data access
     */
    private ContactDao contactDao;

    /**
     * CustomerDao instance for data access
     */
    private CustomerDao custDao;

    /**
     * UserDao instance for data access
     */
    private UserDao userDao;

    /**
     * This method closes the current window and returns to the main form
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        goToMain(event);
    }

    /**
     * This method validates all fields before updating the local appointment variable's elements
     * and updating the appointment in the database
     *
     * @param event
     * @throws IOException
     * @throws SQLException
     * @throws ParseException
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException, SQLException, ParseException {

        TextField[] TFArgs = {textFieldTitle, textFieldDescription, textFieldLocation, textFieldType, textFieldStartDate, textFieldEndDate};
        Label[] TLArgs = {labelTitle, labelDescription, labelLocation, labelType, labelStartDate, labelEndDate};
        ComboBox[] CBArgs = {comboContact, comboCustomerId, comboUserId};
        Label[] CLArgs = {labelContact, labelCustomer, labelUserId};
        Boolean isValid = Validate.validated(TFArgs, TLArgs, CBArgs, CLArgs);

        if (isValid) {
            // Set date format
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy h:mm aa");

            // Set start date
            java.util.Date date = format.parse(textFieldStartDate.getText());
            Timestamp start = new Timestamp(date.getTime());

            // Set end date
            date = format.parse(textFieldEndDate.getText());
            Timestamp end = new Timestamp(date.getTime());

            // Get current time
            date = new Date();
            Timestamp updated = new Timestamp(date.getTime());

            appointment.setTitle(String.valueOf(textFieldTitle.getText()));
            appointment.setDescription(String.valueOf(textFieldDescription.getText()));
            appointment.setLocation(String.valueOf(textFieldLocation.getText()));
            appointment.setContactId(comboContact.getSelectionModel().getSelectedItem().getContactId());
            appointment.setType(String.valueOf(textFieldType.getText()));
            appointment.setStartDate(start);
            appointment.setEndDate(end);
            appointment.setCustomerId(comboCustomerId.getSelectionModel().getSelectedItem().getCustomerId());
            appointment.setUserId(comboUserId.getSelectionModel().getSelectedItem().getUserId());
            appointment.setLastUpdated(updated);
            appointment.setLastUpdatedBy(Session.getCurrentUser().getUsername());
            appDao.update(appointment);

            goToMain(event);
        }
    }

    /**
     * This method returns to the main form
     *
     * @param event
     * @throws IOException
     */
    public void goToMain(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method takes the appointment object passed in from a different form
     * and sets local fields to the appropriate data using the object's elements
     *
     * @param app
     * @throws SQLException
     * @throws ParseException
     */
    public void selectAppointment(Appointment app) throws SQLException, ParseException {

        appointment = app;

        Date date = new Date();
        date.setTime(app.getStartDate().getTime());
        String start = new SimpleDateFormat("MM-dd-yyyy h:mm aa").format(date);

        date.setTime(app.getEndDate().getTime());
        String end = new SimpleDateFormat("MM-dd-yyyy h:mm aa").format(date);

        textFieldAppointmentId.setText(String.valueOf(app.getAppointmentId()));
        textFieldTitle.setText(String.valueOf(app.getTitle()));
        textFieldDescription.setText(String.valueOf(app.getDescription()));
        textFieldLocation.setText(String.valueOf(app.getLocation()));
        comboContact.getSelectionModel().select(contactDao.getById(app.getContactId()));
        textFieldType.setText(String.valueOf(app.getType()));
        textFieldStartDate.setText(start);
        textFieldEndDate.setText(end);
        comboCustomerId.getSelectionModel().select(custDao.getById(app.getCustomerId()));
        comboUserId.getSelectionModel().select(userDao.getById(app.getUserId()));
    }

    /**
     * This method initializes combo boxes and variables for use with the form
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize local variables
        appDao = new AppointmentDao();

        // Create instances
        contactDao = new ContactDao();
        custDao = new CustomerDao();
        userDao = new UserDao();

        // Set combo box items
        try {
            comboContact.setItems(contactDao.getAll());
            comboCustomerId.setItems(custDao.getAll());
            comboUserId.setItems(userDao.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

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

public class AddAppointment implements Initializable {

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
     * Combo box for contacts
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
     * Label for start date
     */
    @FXML
    private Label labelStartDate;

    /**
     * Text field for start date
     */
    @FXML
    private TextField textFieldStartDate;

    /**
     * Label for end date
     */
    @FXML
    private Label labelEndDate;

    /**
     * Text field for end date
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
     * Combo box for customers
     */
    @FXML
    private ComboBox<Customer> comboCustomerId;

    /**
     * Combo box for users
     */
    @FXML
    private ComboBox<User> comboUserId;

    /**
     * AppointmentDao instance for data access
     */
    private AppointmentDao appDao;

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
     * This method confirms all fields are valid, creates a new appointment object
     * and saves it to the appointments table in the database
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
            Timestamp created = new Timestamp(date.getTime());

            int custId = comboCustomerId.getSelectionModel().getSelectedItem().getCustomerId();

            // Validate timestamps
            if (Validate.inHours(start, end) && !Validate.overlaps(start, end, custId)) {

                Appointment newApp = new Appointment(
                        textFieldTitle.getText(),
                        textFieldDescription.getText(),
                        textFieldLocation.getText(),
                        textFieldType.getText(),
                        start,
                        end,
                        created,
                        Session.getCurrentUser().getUsername(),
                        Session.getCurrentUser().getUsername(),
                        comboCustomerId.getSelectionModel().getSelectedItem().getCustomerId(),
                        comboUserId.getSelectionModel().getSelectedItem().getUserId(),
                        comboContact.getSelectionModel().getSelectedItem().getContactId());
                appDao.create(newApp);

                goToMain(event);
            }
        }
    }

    /**
     * This method navigates to the main form
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
     * This method initializes combo boxes and variables for use in the form
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initialize local variables
        appDao = new AppointmentDao();

        // Fill ID field with incremented value
        try {
            textFieldAppointmentId.setText(String.valueOf(appDao.getNextId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create instances
        ContactDao contactDao = new ContactDao();
        CustomerDao custDao = new CustomerDao();
        UserDao userDao = new UserDao();

        // Set combo box items
        try {
            comboUserId.getSelectionModel().select(Session.getCurrentUser());
            comboContact.setItems(contactDao.getAll());
            comboCustomerId.setItems(custDao.getAll());
            comboUserId.setItems(userDao.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

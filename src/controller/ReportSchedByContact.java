package controller;

import dao.AppointmentDao;
import dao.ContactDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class ReportSchedByContact implements Initializable {

    /**
     * Stage variable for navigation
     */
    Stage stage;

    /**
     * Scene variable for navigation
      */
    Parent scene;

    /**
     * Table view for report
     */
    @FXML
    private TableView<Appointment> tableViewReport;

    /**
     * Table column for appointment ID
     */
    @FXML
    private TableColumn<Appointment, Integer> appIdCol;

    /**
     * Table column for title
     */
    @FXML
    private TableColumn<Appointment, String> appTitleCol;

    /**
     * Table column for type
     */
    @FXML
    private TableColumn<Appointment, String> appTypeCol;

    /**
     * Table column for description
     */
    @FXML
    private TableColumn<Appointment, String> appDescriptionCol;

    /**
     * Table column for start date/time
     */
    @FXML
    private TableColumn<Appointment, Timestamp> appStartCol;

    /**
     * Table column for end date/time
     */
    @FXML
    private TableColumn<Appointment, Timestamp> appEndCol;

    /**
     * Table column for customer ID
     */
    @FXML
    private TableColumn<Appointment, Integer> appCustomerIdCol;

    /**
     * Combo box for contacts
     */
    @FXML
    private ComboBox<Contact> comboContact;

    /**
     * AppointmentDao instance for data access
     */
    private AppointmentDao appDao;

    /**
     * ContactDao instance for data access
     */
    private ContactDao contactDao;

    /**
     * This method closes the current window and returns to the main form
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method is triggered when a selection is made from the contacts combo box
     * It sets the table view items to those with a matching contact ID
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    void onActionGenerate(ActionEvent event) throws SQLException {
        tableViewReport.setItems(appDao.appSchedByContact(comboContact.getSelectionModel().getSelectedItem().getContactId()));
    }

    /**
     * This method initializes data access objects and combox boxes for use with the form
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDao = new AppointmentDao();
        contactDao = new ContactDao();

        try {
            comboContact.setItems(contactDao.getAll());
            setTableItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sets the table columns to their appropriate data
     * Lambda functions are used to display dates in a friendly format
     *
     * @throws SQLException
     */
    public void setTableItems() throws SQLException {

        // Configure columns
        appIdCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        appTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        appDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        appStartCol.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        appStartCol.setCellFactory(column -> {
            TableCell<Appointment, Timestamp> formattedCell = new TableCell<Appointment, Timestamp>() {
                SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy h:mm aa");

                @Override
                protected void updateItem(Timestamp date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty) {
                        setText(null);
                    }
                    else {
                        setText(format.format(date));
                    }
                }
            };
            return formattedCell;
        });
        appEndCol.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        appEndCol.setCellFactory(column -> {
            TableCell<Appointment, Timestamp> formattedCell = new TableCell<Appointment, Timestamp>() {
                SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy h:mm aa");

                @Override
                protected void updateItem(Timestamp date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty) {
                        setText(null);
                    }
                    else {
                        setText(format.format(date));
                    }
                }
            };
            return formattedCell;
        });
        appCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
    }
}

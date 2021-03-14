package controller;

import dao.AppointmentDao;
import javafx.collections.transformation.FilteredList;
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
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class ViewAppointments implements Initializable {

    /**
     * Stage variable for navigation
     */
    Stage stage;

    /**
     * Scene variable for navigation
     */
    Parent scene;

    /**
     * Label for name
     */
    @FXML
    private Label labelSelectedName;

    /**
     * Table view for appointments
     */
    @FXML
    private TableView<Appointment> tableViewAppointments;

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
     * Table column for description
     */
    @FXML
    private TableColumn<Appointment, String> appDescriptionCol;

    /**
     * Table column for location
     */
    @FXML
    private TableColumn<Appointment, String> appLocationCol;

    /**
     * Table column for contact
     */
    @FXML
    private TableColumn<Appointment, String> appContactCol;

    /**
     * Table column for type
     */
    @FXML
    private TableColumn<Appointment, String> appTypeCol;

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
     * Table column for customer
     */
    @FXML
    private TableColumn<Appointment, String> appCustCol;

    /**
     * AppointmentDao instance for data access
     */
    private AppointmentDao appDao;

    /**
     * Customer object to store current customer
     */
    private static Customer customer;

    /**
     * Filtered list for appointments
     */
    private static FilteredList<Appointment> appointments;

    /**
     * This method closes the current window and returns to the main form
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionClose(ActionEvent event) throws IOException {
        goToMain(event);
    }

    /**
     * This method confirms the users actions and if confirmed, deletes all appointments for the selected customer
     * If te user does not confirm their actions, nothing occurs
     *
     * @param event
     */
    @FXML
    void onActionDeleteAll(ActionEvent event) {
        try {
            // Display confirmation message before deleting
            Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirm.setTitle("Confirm Deletion");
            alertConfirm.setContentText("Are you sure you want to delete ALL appointments for " + customer.getCustomerName() + "?");
            alertConfirm.showAndWait();

            // Check for user confirmation response
            if (alertConfirm.getResult().getText().equals("OK")) {

                // Delete items from table and retrieve new list
                appDao.deleteAllFor(customer.getCustomerId());
                setFilteredLists();

                // Confirm deletion
                Alert alertDeleted = new Alert(Alert.AlertType.WARNING);
                alertDeleted.setTitle("Item Deleted");
                alertDeleted.setContentText("Appointment has been deleted!");
                alertDeleted.showAndWait();
            }
        } catch (NullPointerException | SQLException e) {
            System.out.println(e.getMessage());
            Alert alertProblem = new Alert(Alert.AlertType.ERROR);
            alertProblem.setTitle("Error!");
            alertProblem.setContentText("No item was selected from appointments table!");
            alertProblem.showAndWait();
        }
    }

    /**
     * This method confirms the user's actions before proceeding to delete the selected appointment
     * If the item was deleted successfully, a prompt appears with confirmation of deletion including appointment details
     * If no item was selected, an error appears
     *
     * @param event
     */
    @FXML
    void onActionDeleteSelected(ActionEvent event) {
        try {
            // Display confirmation message before deleting
            Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirm.setTitle("Confirm Deletion");
            alertConfirm.setContentText("Are you sure you want to delete appointment '" + tableViewAppointments.getSelectionModel().getSelectedItem().getTitle() + "'?");
            alertConfirm.showAndWait();

            // Check for user confirmation response
            if (alertConfirm.getResult().getText().equals("OK")) {

                // Save appointment details for message
                String appId = String.valueOf(tableViewAppointments.getSelectionModel().getSelectedItem().getAppointmentId());
                String appType = String.valueOf(tableViewAppointments.getSelectionModel().getSelectedItem().getType());

                // Delete items from table and retrieve new list
                appDao.delete(tableViewAppointments.getSelectionModel().getSelectedItem());
                setFilteredLists();

                // Confirm deletion
                Alert alertDeleted = new Alert(Alert.AlertType.WARNING);
                alertDeleted.setTitle("Item Deleted");
                alertDeleted.setContentText("Appointment has been deleted!\n\n" +
                        "Appointment ID: " + appId + "\n" +
                        "Appointment Type: " + appType);
                alertDeleted.showAndWait();
            }
        } catch (NullPointerException | SQLException e) {
            Alert alertProblem = new Alert(Alert.AlertType.ERROR);
            alertProblem.setTitle("Error!");
            alertProblem.setContentText("No item was selected from appointments table!");
            alertProblem.showAndWait();
        }
    }

    /**
     * This method initializes a data access object for use with the form
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Create appointment DAO object instance
        appDao = new AppointmentDao();
    }

    /**
     * This method takes the customer passed in from a previous form and uses it to
     * set form data
     *
     * @param cust
     * @throws SQLException
     * @throws ParseException
     */
    public void selectCustomer(Customer cust) throws SQLException, ParseException {
        // Set local customer object
        customer = cust;

        try {
            setFilteredLists();
            setAppTableItems();
            labelSelectedName.setText(String.valueOf(customer.getCustomerName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sets the table columns to their appropriate data
     * Lambda functions are uses to display dates in a friendly format
     *
     * @throws SQLException
     */
    public void setAppTableItems() throws SQLException {

        // Configure columns
        appIdCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        appTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        appDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        appLocationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        appContactCol.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
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
        appCustCol.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
    }

    /**
     * This method filters the list of appointments to those assigned to the selected customer
     * A lambda function is used to set the predicate
     *
     * @throws SQLException
     */
    public void setFilteredLists() throws SQLException {
        appointments = new FilteredList<>(appDao.getAll(), b -> true);
        appointments.setPredicate(appointment -> {
            if (appointment.getCustomerId() == customer.getCustomerId()) {
                return true;
            }
            else
                return false;
        });
        tableViewAppointments.setItems(appointments);
    }

    /**
     * This function returns to the main form
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

}

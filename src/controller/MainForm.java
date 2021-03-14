package controller;

import dao.AppointmentDao;
import dao.CustomerDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import util.DBConnection;
import util.Session;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainForm implements Initializable {

    /**
     * Stage variable for navigation
     */
    Stage stage;

    /**
     * Scene variable for navigation
     */
    Parent scene;

    /**
     * Table view for customers
     */
    @FXML
    private TableView<Customer> tableViewCustomers;

    /**
     * Table column for customer ID
     */
    @FXML
    private TableColumn<Customer, Integer> custIdCol;

    /**
     * Table column for customer name
     */
    @FXML
    private TableColumn<Customer, String> custNameCol;

    /**
     * Table column for address
     */
    @FXML
    private TableColumn<Customer, String> custAddressCol;

    /**
     * Table column for postal code
     */
    @FXML
    private TableColumn<Customer, String> custPostalCodeCol;

    /**
     * Table column for phone number
     */
    @FXML
    private TableColumn<Customer, String> custPhoneCol;

    /**
     * Table column for created date/time
     */
    @FXML
    private TableColumn<Customer, Timestamp> custCreatedCol;

    /**
     * Table column for created by name
     */
    @FXML
    private TableColumn<Customer, String> custCreatedByCol;

    /**
     * Table column for last update date/time
     */
    @FXML
    private TableColumn<Customer, Timestamp> custLastUpdatedCol;

    /**
     * Table column for last updated by name
     */
    @FXML
    private TableColumn<Customer, String> custLastUpdatedByCol;

    /**
     * Table column for first level division
     */
    @FXML
    private TableColumn<Customer, String> custFirstLevelDivCol;

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
     * Radio button for filtering by week
     */
    @FXML
    private RadioButton radioByWeek;

    /**
     * Combo box for reports
     */
    @FXML
    private ComboBox<String> comboReports;

    /**
     * Button for running selected report
     */
    @FXML
    private Button buttonRunReport;

    /**
     * AppointmentDao instance for data access
     */
    private AppointmentDao appDao;

    /**
     * CustomerDao instance for data access
     */
    private CustomerDao custDao;

    /**
     * Filtered list for appointments by week
     */
    private static FilteredList<Appointment> appointmentsWeek;

    /**
     * Filtered list for appointments by month
     */
    private static FilteredList<Appointment> appointmentsMonth;

    /**
     * This method opens the add appointment form when the associated button is pressed
     *
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void onActionAddAppointment(ActionEvent event) throws IOException, SQLException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/AddAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method opens the add customer form when the associated button is pressed
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method checks to see if an item is selected
     * If an item is selected, the item is deleted with a confirmation message
     * If an item is not selected, no item is deleted and an error appears
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    void onActionDeleteAppointment(ActionEvent event) throws SQLException {
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
                setTableViews();

                // Confirm deletion
                Alert alertDeleted = new Alert(Alert.AlertType.WARNING);
                alertDeleted.setTitle("Item Deleted");
                alertDeleted.setContentText("Appointment has been deleted!\n\n" +
                        "Appointment ID: " + appId + "\n" +
                        "Appointment Type: " + appType);
                alertDeleted.showAndWait();
            }
        } catch (NullPointerException e) {
            Alert alertProblem = new Alert(Alert.AlertType.ERROR);
            alertProblem.setTitle("Error!");
            alertProblem.setContentText("No item was selected from appointments table!");
            alertProblem.showAndWait();
        }
    }

    /**
     * This method checks to see if an item is selected
     * If an item is selected, the item is deleted with a confirmation message
     * If an item is not selected, no item is deleted and an error appears
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    void onActionDeleteCustomer(ActionEvent event) throws SQLException {
        try {
            if (appDao.getAllByCustomerId(tableViewCustomers.getSelectionModel().getSelectedItem().getCustomerId()).isEmpty()) {

                // Display confirmation message before deleting
                Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirm.setTitle("Confirm Deletion");
                alertConfirm.setContentText("Are you sure you want to delete customer '" + tableViewCustomers.getSelectionModel().getSelectedItem().getCustomerName() + "'?");
                alertConfirm.showAndWait();

                // Check for user confirmation response
                if (alertConfirm.getResult().getText().equals("OK")) {

                    // Delete items from table and retrieve new list
                    custDao.delete(tableViewCustomers.getSelectionModel().getSelectedItem());
                    setCustomerTableItems();

                    // Confirm deletion
                    Alert alertDeleted = new Alert(Alert.AlertType.WARNING);
                    alertDeleted.setTitle("Item Deleted");
                    alertDeleted.setContentText("Customer has been deleted!");
                    alertDeleted.showAndWait();
                }
            }
            else {
                Alert alertProblem = new Alert(Alert.AlertType.ERROR);
                alertProblem.setTitle("Error!");
                alertProblem.setContentText("Cannot delete customer with appointments!\n");
                alertProblem.showAndWait();
            }
        } catch (NullPointerException e) {
            Alert alertProblem = new Alert(Alert.AlertType.ERROR);
            alertProblem.setTitle("Error!");
            alertProblem.setContentText("No item was selected from customer table!");
            alertProblem.showAndWait();
        }
    }

    /**
     * This method closes the application
     * Open files and DB connections are closed before exit
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionExit(ActionEvent event) throws IOException {
        Session.closeFiles();
        DBConnection.closeConnection();
        System.exit(0);
    }

    /**
     * This method passes the selected table item to the modify appointment form
     * If no item is selected, an error appears
     *
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void onActionModifyAppointment(ActionEvent event) throws IOException, SQLException {
        try {
            // Set form to be loaded
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/ModifyAppointment.fxml"));
            loader.load();

            // Set controller reference & pass part to modify
            ModifyAppointment modAppointmentController = loader.getController();
            modAppointmentController.selectAppointment(tableViewAppointments.getSelectionModel().getSelectedItem());

            // Navigate to Modify Part window
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException | SQLException | ParseException e) {
            Alert alertProblem = new Alert(Alert.AlertType.ERROR);
            alertProblem.setTitle("Error!");
            alertProblem.setContentText("No item was selected from appointments table!");
            alertProblem.showAndWait();
        }
    }

    /**
     * This method passes the selected table item to the modify customer form
     * If no item is selected, an error appears
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionModifyCustomer(ActionEvent event) throws IOException {
        try {
            // Set form to be loaded
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/ModifyCustomer.fxml"));
            loader.load();

            // Set controller reference & pass part to modify
            ModifyCustomer modCustomerController = loader.getController();
            modCustomerController.selectCustomer(tableViewCustomers.getSelectionModel().getSelectedItem());

            // Navigate to Modify Part window
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException | SQLException e) {
            Alert alertProblem = new Alert(Alert.AlertType.ERROR);
            alertProblem.setTitle("Error!");
            alertProblem.setContentText("No item was selected from customers table!");
            alertProblem.showAndWait();
        }
    }

    /**
     * This method opens the view appointments form filtered to the selected customer
     * If no customer is selected, and error appears
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionViewApps(ActionEvent event) throws IOException {
        try {
            // Set form to be loaded
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/ViewAppointments.fxml"));
            loader.load();

            // Set controller reference & pass part to modify
            ViewAppointments viewAppointmentController = loader.getController();
            viewAppointmentController.selectCustomer(tableViewCustomers.getSelectionModel().getSelectedItem());

            // Navigate to Modify Part window
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException | SQLException | ParseException e) {
            Alert alertProblem = new Alert(Alert.AlertType.ERROR);
            alertProblem.setTitle("Error!");
            alertProblem.setContentText("No item was selected from customers table!");
            alertProblem.showAndWait();
        }
    }

    /**
     * This method is triggered when the by-week or by-month radio buttons are selected
     * It calls setTableViews to filters the appointments table
     *
     * @param event
     */
    @FXML
    void onActionFilterApps(ActionEvent event) {
        setTableViews();
    }

    /**
     * This method is called when a selection is made from the reports combo box
     * If a selection is made, the run report button is enabled
     *
     * @param event
     */
    @FXML
    void onActionEnableRun(ActionEvent event) {
        if (comboReports.getSelectionModel().getSelectedItem() != null) {
            buttonRunReport.setDisable(false);
        }
    }

    /**
     * This method opens the appropriate report form when a report is selected and the
     * run report button is selected
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionRunReport(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        if (comboReports.getSelectionModel().getSelectedItem().equals("Customer Appointment Totals by Type & Month")) {
            scene = FXMLLoader.load(getClass().getResource("../view/ReportCABMT.fxml"));
        }
        else if (comboReports.getSelectionModel().getSelectedItem().equals("Appointments Schedule by Contact")) {
            scene = FXMLLoader.load(getClass().getResource("../view/ReportSchedByContact.fxml"));
        }
        else {
            scene = FXMLLoader.load(getClass().getResource("../view/ReportAppsCreatedBy.fxml"));
        }
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method initializes all tables, combo boxes, and variables for use with the form
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Create appointment DAO object instance
        appDao = new AppointmentDao();
        custDao = new CustomerDao();

        try {
            setFilteredLists();
            setAppTableItems();
            setCustomerTableItems();
            setTableViews();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObservableList<String> reports =
                FXCollections.observableArrayList(
                        "Customer Appointment Totals by Type & Month",
                        "Appointments Schedule by Contact",
                        "Appointments by Created-By User"
                );
        comboReports.setItems(reports);
    }

    /**
     * This method sets the appropriate appointments table view (by-week or by-month)
     * based on the selected radio button
     */
    public void setTableViews() {
        if (radioByWeek.isSelected()) {
            tableViewAppointments.setItems(appointmentsWeek);
        } else {
            tableViewAppointments.setItems(appointmentsMonth);
        }
    }

    /**
     * This method assigns the appropriate appointments table column to data using value factories
     * Appropriate formatting occurs to display friendly dates
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
     * This method assigns the appropriate customers table column to data using value factories
     * Appropriate formatting occurs to display friendly dates
     *
     * @throws SQLException
     */
    public void setCustomerTableItems() throws SQLException {

        // Create customer DAO object instance
        CustomerDao custDao = new CustomerDao();

        // Retrieve observable list of appointments
        tableViewCustomers.setItems(custDao.getAll());

        // Configure columns
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        custPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        custCreatedCol.setCellValueFactory(new PropertyValueFactory<>("CreatedDate"));
        custCreatedByCol.setCellValueFactory(new PropertyValueFactory<>("CreatedBy"));
        custCreatedCol.setCellFactory(column -> {
            TableCell<Customer, Timestamp> formattedCell = new TableCell<Customer, Timestamp>() {
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
        custLastUpdatedCol.setCellValueFactory(new PropertyValueFactory<>("LastUpdated"));
        custLastUpdatedCol.setCellFactory(column -> {
            TableCell<Customer, Timestamp> formattedCell = new TableCell<Customer, Timestamp>() {
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
        custLastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<>("LastUpdatedBy"));

        custFirstLevelDivCol.setCellValueFactory(new PropertyValueFactory<>("Division"));
    }

    /**
     * This method sets the filtered list variables using lambda functions on setPredicate
     *
     * @throws SQLException
     */
    public void setFilteredLists() throws SQLException {

        appointmentsWeek = new FilteredList<>(appDao.getAll(), b -> true);
        appointmentsMonth = new FilteredList<>(appDao.getAll(), b -> true);

        // Create weekly filtered list
        LocalDate afterDate = LocalDate.now().minusDays((LocalDate.now().getDayOfWeek().getValue() % 7) - 1);
        LocalDate beforeDate = afterDate.minusDays(-8);

        appointmentsWeek.setPredicate(appointment -> {

            if (appointment.getStartDate().after(Date.valueOf(afterDate)) && appointment.getStartDate().before(Date.valueOf(beforeDate))) {
                return true;
            }
            else
                return false;
        });

        // Create monthly filtered list
        int currentMonthVal = LocalDate.now().getMonthValue();
        int currentYearVal = LocalDate.now().getYear() - 1900;

        appointmentsMonth.setPredicate(appointment -> {

            if (appointment.getStartDate().getMonth() + 1 == currentMonthVal) {
                if (appointment.getStartDate().getYear() == currentYearVal) {
                    return true;
                }
                else
                    return false;
            }
            else
                return false;
        });
    }
}

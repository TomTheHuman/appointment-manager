package controller;

import dao.AppointmentDao;
import dao.UserDao;
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
import model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class ReportAppsCreatedBy implements Initializable {

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
     * Combo box for users
     */
    @FXML
    private ComboBox<User> comboContact;

    /**
     * AppointmentDao instance for data access
     */
    private AppointmentDao appDao;

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
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method is called when a selection is made from the user combo box
     * It sets the table view items to appointments created by the user selected
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    void onActionGenerate(ActionEvent event) throws SQLException {
        tableViewReport.setItems(appDao.appSchedByUser(comboContact.getSelectionModel().getSelectedItem().getUsername()));
    }

    /**
     * This method initializes combo boxes and data access objects for use with the form
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appDao = new AppointmentDao();
        userDao = new UserDao();

        try {
            comboContact.setItems(userDao.getAll());
            setTableItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method sets table columns to the appropriate data using value factories
     * Lambdas are used to display friendly date formats
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

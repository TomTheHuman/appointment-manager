package controller;

import dao.CountryDao;
import dao.CustomerDao;
import dao.FirstLevelDao;
import javafx.collections.transformation.FilteredList;
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
import model.Country;
import model.Customer;
import model.FirstLevelDiv;
import util.Session;
import util.Validate;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;

public class ModifyCustomer implements Initializable {

    /**
     * Stage variable for navigation
     */
    Stage stage;

    /**
     * Scene variable for navigation
     */
    Parent scene;

    /**
     * Text field for customer ID
     */
    @FXML
    private TextField textFieldCustomerId;

    /**
     * Label for customer name
     */
    @FXML
    private Label labelName;

    /**
     * Text field for customer name
     */
    @FXML
    private TextField textFieldName;

    /**
     * Label for address
     */
    @FXML
    private Label labelAddress;

    /**
     * Text field for address
     */
    @FXML
    private TextField textFieldAddress;

    /**
     * Label for postal code
     */
    @FXML
    private Label labelPostalCode;

    /**
     * Text field for postal code
     */
    @FXML
    private TextField textFieldPostalCode;

    /**
     * Label for phone number
     */
    @FXML
    private Label labelPhoneNumber;

    /**
     * Text field for phone number
     */
    @FXML
    private TextField textFieldPhoneNumber;

    /**
     * Label for state/province
     */
    @FXML
    private Label labelStateProvince;

    /**
     * Label for country
     */
    @FXML
    private Label labelCountry;

    /**
     * Combo box for state/province
     */
    @FXML
    private ComboBox<FirstLevelDiv> comboStateProvince;

    /**
     * Combo box for country
     */
    @FXML
    private ComboBox<Country> comboCountry;

    /**
     * CustomerDao instance for data access
     */
    private CustomerDao custDao;

    /**
     * Customer variable for storing current customer
     */
    private Customer customer;

    /**
     * Filtered list for first level divisions
     */
    private FilteredList<FirstLevelDiv> divisions;

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
     * This method validates all fields before updating the local customer variable's elements
     * and using it to update the database
     *
     * @param event
     * @throws IOException
     * @throws SQLException
     * @throws ParseException
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException, SQLException, ParseException {

        TextField[] TFArgs = {textFieldName, textFieldAddress, textFieldPostalCode, textFieldPhoneNumber};
        Label[] TLArgs = {labelName, labelAddress, labelPostalCode, labelPhoneNumber};
        ComboBox[] CBArgs = {comboStateProvince, comboCountry};
        Label[] CLArgs = {labelStateProvince, labelCountry};
        Boolean isValid = Validate.validated(TFArgs, TLArgs, CBArgs, CLArgs);

        if (isValid) {
            // Get current time
            Date date = new Date();
            Timestamp updated = new Timestamp(date.getTime());

            customer.setCustomerName(textFieldName.getText());
            customer.setAddress(textFieldAddress.getText());
            customer.setPostalCode(textFieldPostalCode.getText());
            customer.setPhone(textFieldPhoneNumber.getText());
            customer.setLastUpdated(updated);
            customer.setLastUpdatedBy(Session.getCurrentUser().getUsername());
            customer.setDivisionId(comboStateProvince.getSelectionModel().getSelectedItem().getDivisionId());
            custDao.update(customer);
            goToMain(event);
        }
    }

    /**
     * This method is triggered when a selection is made from the country combo box
     * It calls setFilteredLists to filter the first level divisions comobo box
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    void onActionFilter(ActionEvent event) throws SQLException {
        setFilteredLists();
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
     * This method takes the customer object passed in and saves it to a local variable
     * before using its elements to populate form fields
     *
     * @param cust
     * @throws SQLException
     */
    public void selectCustomer(Customer cust) throws SQLException {

        customer = cust;
        FirstLevelDao flDao = new FirstLevelDao();
        CountryDao countryDao = new CountryDao();

        textFieldCustomerId.setText(String.valueOf(cust.getCustomerId()));
        textFieldName.setText(String.valueOf(cust.getCustomerName()));
        textFieldAddress.setText(String.valueOf(cust.getAddress()));
        textFieldPostalCode.setText(String.valueOf(cust.getPostalCode()));
        textFieldPhoneNumber.setText(String.valueOf(cust.getPhone()));
        comboStateProvince.getSelectionModel().select(flDao.getById(cust.getDivisionId()));
        comboCountry.getSelectionModel().select(countryDao.getById(flDao.getById(cust.getDivisionId()).getCountryId()));
        setFilteredLists();
    }

    /**
     * This method initializes combo boxes, variables, and data access objects for use in the form
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initialize local variables
        custDao = new CustomerDao();

        // Fill ID field with incremented value
        try {
            textFieldCustomerId.setText(String.valueOf(custDao.getNextId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create instances
        FirstLevelDao flDao = new FirstLevelDao();
        CountryDao countryDao = new CountryDao();

        // Set combo box items
        try {
            comboCountry.setItems(countryDao.getAll());
            setFilteredLists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method filters the first level divisions combo box list based on the
     * country selected in the country combo box
     *
     * @throws SQLException
     */
    public void setFilteredLists() throws SQLException {
        FirstLevelDao flDao = new FirstLevelDao();
        divisions = new FilteredList<>(flDao.getAll(), b -> true);
        divisions.setPredicate(div -> {

            if (comboCountry.getSelectionModel().getSelectedItem() == null) {
                return true;
            } else if (comboCountry.getSelectionModel().getSelectedItem().getCountryId() == div.getCountryId())
                return true;
            else {
                return false;
            }
        });
        comboStateProvince.setItems(divisions);
    }
}

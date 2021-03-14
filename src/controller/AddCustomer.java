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

public class AddCustomer implements Initializable {

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
     * Label for name
     */
    @FXML
    private Label labelName;

    /**
     * Text field for name
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
     * FilteredList for first level divisions
     */
    private static FilteredList<FirstLevelDiv> divisions;

    /**
     * This method closes the current form and returns to the main form
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        goToMain(event);
    }

    /**
     * This method validates all fields before creating a customer object and
     * saving it to the customers table in the database
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
            Timestamp created = new Timestamp(date.getTime());

            Customer newCust = new Customer(
                    textFieldName.getText(),
                    textFieldAddress.getText(),
                    textFieldPostalCode.getText(),
                    textFieldPhoneNumber.getText(),
                    created,
                    Session.getCurrentUser().getUsername(),
                    Session.getCurrentUser().getUsername(),
                    comboStateProvince.getSelectionModel().getSelectedItem().getDivisionId());
            custDao.create(newCust);
            goToMain(event);
        }

    }

    /**
     * This method is triggered when a country is selected from the combo box
     * It filters the first level divisions combo box based on country ID selected
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
     * This method intializes combox boxes and variables for use in the form
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
            setFilteredLists();
            comboCountry.setItems(countryDao.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method filters the first level divisions list by country ID using
     * the selected country in the country combo box
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

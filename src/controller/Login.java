package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.Session;
import util.Validate;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Login implements Initializable {

    /**
     * Stage variable for navigation
     */
    Stage stage;

    /**
     * Scene variable for navigation
     */
    Parent scene;

    /**
     * Label for zone ID
     */
    @FXML
    private Label labelZoneId;

    /**
     * Label for header
     */
    @FXML
    private Label labelHeader;

    /**
     * Text field for user ID
     */
    @FXML
    private TextField textFieldUserID;

    /**
     * Text field for password
     */
    @FXML
    private TextField textFieldPassword;

    /**
     * Label for user ID
     */
    @FXML
    private Label labelUserID;

    /**
     * Label for password
     */
    @FXML
    private Label labelPassword;

    /**
     * Button for loging in
     */
    @FXML
    private Button buttonLogin;

    /**
     * User input is validated and authenticated before proceeding with logging in
     * and navigating to main form
     *
     * @param event
     * @throws IOException
     * @throws SQLException
     * @throws ParseException
     */
    @FXML
    void onActionLogin(ActionEvent event) throws IOException, SQLException, ParseException {

        TextField[] textArgs = {textFieldUserID, textFieldPassword};

        if (Validate.loginValidated(textArgs)) {
            if (Session.authenticated(Integer.valueOf(textFieldUserID.getText()), textFieldPassword.getText())) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("../view/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

                try {
                    Session.appAlerts();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Labels, variables, and log files are initialized while accounting for English/French region settings
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Session.initWriters();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        if (Session.getRb() != null) {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                labelHeader.setText(Session.getRb().getString("Appointment Manager"));
                labelUserID.setText(Session.getRb().getString("User ID"));
                labelPassword.setText(Session.getRb().getString("Password"));
                buttonLogin.setText(Session.getRb().getString("Login"));
            }
        }
        labelZoneId.setText(String.valueOf(Session.getZoneId()));
    }
}

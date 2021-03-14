package controller;

import dao.AppointmentDao;
import dao.CustomerDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ResourceBundle;

public class ReportCABMT implements Initializable {

    /**
     * Stage variable for navigation
     */
    Stage stage;

    /**
     * Scene variable for navigation
     */
    Parent scene;

    /**
     * Text area for report
     */
    @FXML
    private TextArea textAreaReport;

    /**
     * AppointmentDao instance for data access
     */
    private AppointmentDao appDao;

    /**
     * CustomerDao instance for data access
     */
    private CustomerDao custDao;

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
     * This method initializes data access objects and sets the report context to be displayed
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appDao = new AppointmentDao();
        custDao = new CustomerDao();

        String reportContent = "";
        Iterator repIterator = null;
        try {
            repIterator = appDao.appsByMonthType().iterator();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while(repIterator.hasNext()) {
            reportContent += (repIterator.next() + "\n");
        }
        textAreaReport.setText(reportContent);
    }
}

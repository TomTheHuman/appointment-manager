package util;

import dao.AppointmentDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Appointment;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Locale;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {

    /**
     * Stack to hold errors during validation
     */
    private static Stack errors = new Stack();

    /**
     * Default text field style
     */
    private static String resetStyle = "-fx-border-color: none;";

    /**
     * Error text field style
     */
    private static String errorStyle = "-fx-border-color: red; -fx-border-width: 2px";

    /**
     * @param fields fields to reset style
     */
    public static void resetStyles(TextField[] fields) {
        for (int i = 0; i < fields.length; i++) {
            fields[i].setStyle(resetStyle);
        }
    }

    /**
     * @param boxes boxes to reset style
     */
    public static void resetStyles(ComboBox[] boxes) {
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].setStyle(resetStyle);
        }
    }

    /**
     * @param fieldInput fieldInput to check if empty
     * @param fieldLabel fieldLabel to use in error message
     * @return true or false if field is empty
     */
    public static boolean isEmpty(TextField fieldInput, Label fieldLabel) {
        if (fieldInput.getLength() == 0) {
            errors.push(fieldLabel.getText() + " cannot be blank!");
            fieldInput.setStyle(errorStyle);
            return true;
        }
        else { return false; }
    }

    /**
     *
     * @param fieldInput
     * @param fieldLabel
     * @return true or false if drop down is not selected
     */
    public static boolean dropDownsSelected(ComboBox fieldInput, Label fieldLabel) {
        if (fieldInput.getSelectionModel().getSelectedItem() == null) {
            errors.push(fieldLabel.getText() + " must have a selection!");
            fieldInput.setStyle(errorStyle);
            return false;
        }
        else { return true; }
    }

    /**
     *
     * @param format
     * @param fieldInput
     * @param fieldLabel
     * @return true or false if date formatted correctly
     */
    public static boolean dateDateFormatted(SimpleDateFormat format, TextField fieldInput, Label fieldLabel) {
        try {
            format.parse(fieldInput.getText());
            return true;
        } catch (ParseException e) {
            errors.push(fieldLabel.getText() + " field must be in format: MM-DD-YYYY 00:00 AM");
            fieldInput.setStyle(errorStyle);
            return false;
        }
    }

    /**
     *
     * @param fieldInput
     * @param fieldLabel
     * @return true or false if phone number formatted correctly
     */
    public static boolean phoneNumberFormatted(TextField fieldInput, Label fieldLabel) {

            // Look for (000) 000-0000 phone number format
            Pattern phoneFormat = Pattern.compile("\\(\\d{3}\\)\\s\\d{3}-\\d{4}");
            Matcher phoneMatcher = phoneFormat.matcher(fieldInput.getText());

            if (phoneMatcher.matches()) {
                return true;
            }
            else {
                errors.push(fieldLabel.getText() + " field must be in format: (000) 000-0000");
                fieldInput.setStyle(errorStyle);
                return false;
            }
    }

    /**
     * Validated checks to make sure all appropriate input validation steps are passed before any further action is completed
     *
     * @param TFields text fields to check for validity
     * @param TLabels text field labels to use in error messages
     * @param CBoxes combo boxes to check for validity
     * @param CLabels combo box labels to use in error messages
     * @return isValid
     */
    public static Boolean validated(TextField[] TFields, Label[] TLabels, ComboBox[] CBoxes, Label[] CLabels) {

        // Initialize variables and fields
        errors.clear();
        Boolean isValid = true;
        resetStyles(TFields);
        resetStyles(CBoxes);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy H:mm aa");

        // Verify no fields are empty
        for (int i = 0; i < TFields.length; i++) {
            if (isEmpty(TFields[i], TLabels[i])) { isValid = false; }
        }

        // Verify drop-downs are selected
        for (int i = 0; i < CBoxes.length; i++) {
            // Check drop-downs
            if (!dropDownsSelected(CBoxes[i], CLabels[i])) {
                isValid = false;
            }
        }

        // Verify dates and phone numbers are in correct format
        if (isValid) {
            for (int i = 0; i < TFields.length; i++) {
                if (TLabels[i].getText().contains("Start Date") || TLabels[i].getText().contains("End Date")) {
                    if (!dateDateFormatted(format, TFields[i], TLabels[i]) ) { isValid = false; }
                }
                if (TLabels[i].getText().contains("Phone Number")) {
                    if (!phoneNumberFormatted(TFields[i], TLabels[i]) ) { isValid = false; }
                }
            }
        }
        if (!isValid) { printErrors(); }

        return isValid;
    }

    /**
     *
     * @param textFields
     * @return true or false if user ID and password fields are not blank
     */
    public static Boolean loginValidated(TextField[] textFields) {
        errors.clear();
        Boolean isValid = true;
        resetStyles(textFields);

        for (int i=0; i < textFields.length; i++) {
            if (textFields[i].getLength() == 0) {
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    errors.push(Session.getRb().getString((textFields[i].getId().contains("UserID") ? "User ID" : "Password")) + " " + Session.getRb().getString("cannot be blank!"));
                }
                else
                    errors.push((textFields[i].getId().contains("UserID") ? "User ID" : "Password") + " cannot be blank!");
                textFields[i].setStyle(errorStyle);
                if (isValid) {
                    isValid = false;
                }
            }
        }

        if (isValid) {
            return true;
        } else {
            String errorContent = "";
            Iterator errIterator = errors.iterator();
            while(errIterator.hasNext()) {
                errorContent += (errIterator.next() + "\n");
            }

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle((Locale.getDefault().getLanguage().equals("fr") ? Session.getRb().getString("Validation Error") : "Validation Error"));
            alert.setContentText(errorContent);
            alert.showAndWait();
            errors.clear();
            return false;
        }

    }

    /**
     *
     * @param startTS
     * @param endTS
     * @return true or false if the timestamps passed in are within business hours
     */
    public static Boolean inHours(Timestamp startTS, Timestamp endTS) {

        // Set local begin and end day time variables
        LocalTime startDay = LocalTime.of(8, 0, 0, 0);
        LocalTime endDay = LocalTime.of(22, 0, 0, 0);

        // Convert timestamp to EST
        Timestamp startEST = Session.toEST(startTS);
        LocalDateTime startTime = startEST.toLocalDateTime();

        Timestamp endEST = Session.toEST(endTS);
        LocalDateTime endTime = endEST.toLocalDateTime();

        // Check if within business hours
        if ((startTime.toLocalTime().isAfter(startDay) || startTime.toLocalTime().equals(startDay)) && (endTime.toLocalTime().isBefore(endDay) || endTime.toLocalTime().equals(endDay))) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Appointment outside of business hours!\nBusiness Hours: 8:00AM - 10:00 PM EST");
            alert.showAndWait();
            return false;
        }
    }

    /**
     *
     * @param startTS
     * @param endTS
     * @param custId
     * @return true or false if the timestamps passed overlap any other appointments for the provided customer
     * @throws SQLException
     */
    public static Boolean overlaps(Timestamp startTS, Timestamp endTS, int custId) throws SQLException {

        // Declare variables
        AppointmentDao appDao = new AppointmentDao();
        Boolean overlaps = false;

        // Create observable list
        ObservableList<Appointment> custAppointments = FXCollections.observableArrayList();
        custAppointments = appDao.getAllByCustomerId(custId);

        // Convert timestamps to LocalDateTime
        LocalDateTime startTime = startTS.toLocalDateTime();
        LocalDateTime endTime = endTS.toLocalDateTime();

        // Check appointments
        for (Appointment app : custAppointments) {

            // Convert appointment times to LocalDateTime
            LocalDateTime appStartTime = app.getStartDate().toLocalDateTime();
            LocalDateTime appEndTime = app.getEndDate().toLocalDateTime();

            if (startTime.isAfter(appStartTime) && startTime.isBefore(appEndTime)) {
                if (!overlaps) { overlaps = true; }
            }
            else if (endTime.isAfter(appStartTime) && endTime.isBefore(appEndTime)) {
                if (!overlaps) { overlaps = true; }
            }
            else if (startTime.equals(appStartTime) || endTime.equals(appEndTime)) {
                if (!overlaps) { overlaps = true; }
            }
        }

        // Handle overlaps
        if (overlaps) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Cannot create overlapping appointments for customer!");
            alert.showAndWait();
        }

        // Return value
        return overlaps;
    }

    /**
     * Create an error dialog window with all stored error messages printed
     * on separate lines.
     */
    public static void printErrors() {
        String errorContent = "";
        Iterator errIterator = errors.iterator();
        while(errIterator.hasNext()) {
            errorContent += (errIterator.next() + "\n");
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setContentText(errorContent);
        alert.showAndWait();
        errors.clear();
    }
}

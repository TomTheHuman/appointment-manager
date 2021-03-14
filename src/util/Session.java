package util;

import dao.AppointmentDao;
import dao.UserDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Appointment;
import model.User;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class Session {

    // Local variables
    private static User currentUser;
    private static UserDao userDao;
    private static ZoneId zoneId;
    private static ResourceBundle rb;
    private static FileWriter fw;
    private static PrintWriter pw;

    // Setters

    /**
     * Initializes the zone ID
     */
    public static void initZoneId() {
        zoneId = ZoneId.of(TimeZone.getDefault().getID());
    }

    /**
     * Initializes the resource bundle
     */
    public static void initResourceBundle() {
        rb = ResourceBundle.getBundle("resources.Lang", Locale.getDefault());
    }

    /**
     * Initializes the file/print writers
     *
     * @throws IOException
     */
    public static void initWriters() throws IOException {
        fw = new FileWriter("login_activity.txt", true);
        pw = new PrintWriter(fw);
    }

    // Getters

    /**
     *
     * @return the current user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     *
     * @return the zone ID
     */
    public static ZoneId getZoneId() { return zoneId; }

    /**
     *
     * @return the resource bundle
     */
    public static ResourceBundle getRb() { return rb; }

    // Methods
    /**
     * This method takes the provided user ID and password and authenticates the user session
     * If the user does not exist an error appears
     * If the password does not match and error appears
     * If the user exists ans the password exists, a true value is passed back
     *
     * @param uId
     * @param pass
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public static boolean authenticated(int uId, String pass) throws SQLException, ParseException {

        // Local variables
        userDao = new UserDao();
        User tempUser = userDao.getById(uId);

        // If username exists
        if (tempUser != null) {
            if (tempUser.getPassword().equals(pass)) {
                currentUser = tempUser;
                System.out.println("Current user is " + currentUser.getUsername());
                logActivity(uId, "AUTHENTICATION SUCCESSFUL");
                return true;
            }
            else {
                Alert alertProblem = new Alert(Alert.AlertType.ERROR);
                alertProblem.setTitle("Error!");
                if (rb != null) {
                    if (Locale.getDefault().getLanguage().equals("fr")) {
                        alertProblem.setContentText(rb.getString("Incorrect password!"));
                    } else {
                        alertProblem.setContentText("Incorrect password!");
                    }
                }
                alertProblem.showAndWait();
                logActivity(uId, "INCORRECT PASSWORD");
                return false;
            }
        }
        else {
            Alert alertProblem = new Alert(Alert.AlertType.ERROR);
            alertProblem.setTitle("Error!");
            if (rb != null) {
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    alertProblem.setContentText(rb.getString("Username does not exist!"));
                } else {
                    alertProblem.setContentText("Username does not exist!");
                }
            }
            alertProblem.showAndWait();
            logActivity(uId, "INVALID USER ID");
            return false;
        }
    }

    /**
     * This method logs user login activity
     * Failed login attempts are recorded with appropriate status
     * Successful login attempts are recorded with appropriate status
     *
     * @param USER_ID
     * @param LOGIN_STATUS
     * @throws ParseException
     */
    public static void logActivity(int USER_ID, String LOGIN_STATUS) throws ParseException {

        // Get current timestamp
        Date date = new Date();
        Timestamp now = new Timestamp(date.getTime());
        now = Session.toUTC(now);
        date = new Date(now.getTime());
        String DATE_TIME = new SimpleDateFormat("MM-dd-yyyy hh:mm aa").format(date);

        // Write to file
        pw.printf("LOGIN @ DATE_TIME=%-18s (UTC) USER_ID=%-8s LOGIN_STATUS=%-30s\n", DATE_TIME, USER_ID, LOGIN_STATUS);
    }

    /**
     * This method closes both the file and print writer objects
     *
     * @throws IOException
     */
    public static void closeFiles() throws IOException {
        pw.close();
        fw.close();
    }

    /**
     * This method converts a timestamp to UTC
     *
     * @param localDateTime
     * @return
     */
    public static Timestamp toUTC(Timestamp localDateTime) {

        LocalDate localDate = localDateTime.toLocalDateTime().toLocalDate();
        LocalTime localTime = localDateTime.toLocalDateTime().toLocalTime();
        ZonedDateTime localZDT = ZonedDateTime.of(localDate, localTime, zoneId);

        Instant utcInstant = localZDT.toInstant();
        ZonedDateTime utcZDT = utcInstant.atZone(ZoneId.of("UTC"));
        Timestamp utcTS = Timestamp.valueOf(utcZDT.toLocalDateTime());

        return utcTS;
    }

    /**
     * This method converts a timestamp to EST
     *
     * @param localDateTime
     * @return
     */
    public static Timestamp toEST(Timestamp localDateTime) {

        LocalDate localDate = localDateTime.toLocalDateTime().toLocalDate();
        LocalTime localTime = localDateTime.toLocalDateTime().toLocalTime();
        ZonedDateTime localZDT = ZonedDateTime.of(localDate, localTime, zoneId);

        Instant instant = localZDT.toInstant();
        ZonedDateTime estZDT = instant.atZone(ZoneId.of("-05:00"));
        Timestamp estTS = Timestamp.valueOf(estZDT.toLocalDateTime());

        return estTS;
    }

    /**
     * This method converts a timestamp to the local time zone
     *
     * @param utcDateTime
     * @return
     */
    public static Timestamp toLocal(Timestamp utcDateTime) {

        LocalDate utcDate = utcDateTime.toLocalDateTime().toLocalDate();
        LocalTime utcTime = utcDateTime.toLocalDateTime().toLocalTime();
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcZDT = ZonedDateTime.of(utcDate, utcTime, utcZoneId);

        Instant utcInstant = utcZDT.toInstant();
        ZonedDateTime localZDT = utcInstant.atZone(zoneId);
        Timestamp localTS = Timestamp.valueOf(localZDT.toLocalDateTime());

        return localTS;
    }

    /**
     * This method alerts the user if there is an appointment within 15 minutes of their login
     * A notice appears to let the user know no appointments are within 15 minutes if none are found
     *
     * @throws SQLException
     */
    public static void appAlerts() throws SQLException {

        // Declare variables
        AppointmentDao appDao = new AppointmentDao();
        LocalTime current = LocalTime.now();
        Boolean noApps = true;

        // Create observable list
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        appointments = appDao.getAll();

        // Check for appointments within 15 minutes of login
        for (Appointment app : appointments) {

            // Get difference between current time and appointment start time
            long diff = ChronoUnit.MINUTES.between(current, app.getStartDate().toLocalDateTime().toLocalTime());
            System.out.println(diff);

            // Format dates
            Date date = new Date();
            date.setTime(app.getStartDate().getTime());
            String start = new SimpleDateFormat("MM-dd-yyyy h:mm aa").format(date);
            date.setTime(app.getEndDate().getTime());
            String end = new SimpleDateFormat("MM-dd-yyyy h:mm aa").format(date);

            if (diff >= 0 && diff <= 15) {
                if (noApps) { noApps = false; }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Reminder");
                alert.setContentText("You have an appointment coming up!\n\n" +
                        "Appointment ID: " + app.getAppointmentId() + "\n" +
                        "Appointment: " + app.getTitle() + "\n" +
                        "Start Date/Time: " + start + "\n" +
                        "End Date/Time: " + end);
                alert.showAndWait();
            }
        }
        if (noApps) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reminder");
            alert.setContentText("You have no appointments coming up!");
            alert.showAndWait();
        }
    }
}

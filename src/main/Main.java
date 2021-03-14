package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import util.DBConnection;
import util.Session;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.MissingResourceException;

public class Main extends Application {

    public static Connection conn;

    @Override
    public void start(@NotNull Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
        primaryStage.setTitle("Appointment Management System");
        primaryStage.setScene(new Scene(root, 450, 300));
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException, IOException {

        Session.initZoneId();
        try {
            Session.initResourceBundle();
        } catch (MissingResourceException e) {
            System.out.println(e.getMessage());
        }
        conn = DBConnection.startConnection();
        launch(args);
        Session.closeFiles();
        DBConnection.closeConnection();
    }
}

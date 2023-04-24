package eu.fiit.cookingmanager.cookingmanager.utils;

import eu.fiit.cookingmanager.cookingmanager.CookingManager;
import eu.fiit.cookingmanager.cookingmanager.controller.HomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import io.github.cdimascio.dotenv.Dotenv;

public class DBUtils {

    private final String driver = "org.postgresql.Driver";
    private Connection conn = null;
    private Dotenv env = null;
    private String connURL = null;


    public void DBController() {
        this.env = Dotenv.configure()
                //.directory("C:\\Users\\42191\\OneDrive\\Dokumenty\\Škola\\Škola 6. semester\\VAVA\\vava\\CookingManager\\src\\main\\java\\eu\\fiit\\cookingmanager\\cookingmanager\\")
                .directory("C:\\Users\\ahlad\\Desktop\\LS 2023\\VAVA\\VAVA_PROJECT\\vava\\CookingManager\\src\\main\\java\\eu\\fiit\\cookingmanager\\cookingmanager\\")
                .filename("env")
                .load();
        this.connURL = String.format("jdbc:postgresql://%s/%s", this.env.get("DB_HOST"), this.env.get("DB_NAME"));

    }

    public Connection dbConnect() {
        DBController();
        try {
            Class.forName(this.driver);
        }
        catch(ClassNotFoundException classNotFoundException) {
            System.err.println("Could not set database driver");
        }

        try {
            this.conn = DriverManager.getConnection(this.connURL, this.env.get("DB_USER"), this.env.get("DB_PASS"));

        }
        catch (SQLException sqlException) {
            //System.err.println("Could not establish database connection \nDBUSER: " + this.env.get("DB_USER") + "\nDBPASS: " + this.env.get("DB_PASS") + "\nURL: " + this.connURL.toString());
            sqlException.printStackTrace();

        }

        return this.conn;
    }

    public static void dbDisconnect(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
        catch(SQLException sqlException) {
            System.err.println("Error while trying to disconnect database");
        }

    }



    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, ResourceBundle resourceBundle) {
        Parent root = null;

        if (username != null) {
            try{
                FXMLLoader loader = new FXMLLoader(CookingManager.class.getResource(fxmlFile), resourceBundle);
                root = loader.load();
                HomeController homeController = loader.getController();
                homeController.setUserInformation(username);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try{
                root = FXMLLoader.load(CookingManager.class.getResource(fxmlFile), resourceBundle);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

}






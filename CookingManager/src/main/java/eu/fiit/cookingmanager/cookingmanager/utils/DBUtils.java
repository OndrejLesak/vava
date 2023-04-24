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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



public class DBUtils {
    private Connection conn = null;
    private Dotenv env = null;
    private String connURL = null;

    private static final Logger logger = LogManager.getLogger(DBUtils.class);


    public void DBController() {
        this.env = Dotenv.load();
        this.connURL = String.format("jdbc:postgresql://%s/%s", this.env.get("DB_HOST"), this.env.get("DB_NAME"));
    }


    public Connection dbConnect() {
        DBController();
        try {
            String driver = "org.postgresql.Driver";
            Class.forName(driver);
        }
        catch(ClassNotFoundException classNotFoundException) {
            logger.error(DBUtils.class.getName() + " || " + classNotFoundException.getMessage());
        }

        try {
            this.conn = DriverManager.getConnection(this.connURL, this.env.get("DB_USER"), this.env.get("DB_PASS"));

        }
        catch (SQLException sqlException) {
            logger.error(DBUtils.class.getName() + " || " + sqlException.getMessage());
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
            logger.error(DBUtils.class.getName() + " || " + sqlException.getMessage());

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
                logger.error(DBUtils.class.getName() + " || " + e.getMessage());
            }
        }
        else{
            try{
                root = FXMLLoader.load(CookingManager.class.getResource(fxmlFile), resourceBundle);
            } catch (IOException e) {
                logger.error(DBUtils.class.getName() + " || " + e.getMessage());
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);

        assert root != null;
        stage.setScene(new Scene(root));
        stage.show();
    }

}






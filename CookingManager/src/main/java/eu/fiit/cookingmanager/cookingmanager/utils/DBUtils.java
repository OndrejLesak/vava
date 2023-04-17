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

public class DBUtils {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username) {
        Parent root = null;

        if (username != null) {
            try{
                FXMLLoader loader = new FXMLLoader(CookingManager.class.getResource(fxmlFile));
                root = loader.load();
                HomeController homeController = loader.getController();
                homeController.setUserInformation(username);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try{
                root = FXMLLoader.load(CookingManager.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
}

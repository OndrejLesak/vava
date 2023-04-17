package eu.fiit.cookingmanager.cookingmanager;

import eu.fiit.cookingmanager.cookingmanager.controller.DBController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.kordamp.bootstrapfx.BootstrapFX;

public class CookingManager extends Application {



    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Log In!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        //new DBController();
    }

    public static void main(String[] args) {
        launch();
    }
}
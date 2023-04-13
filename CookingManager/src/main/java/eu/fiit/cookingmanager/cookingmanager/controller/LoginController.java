package eu.fiit.cookingmanager.cookingmanager.controller;


import eu.fiit.cookingmanager.cookingmanager.CookingManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private Button button;


    @FXML
    private void handleLogin()  {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username and password are required");
            messageLabel.setVisible(true);
        } else if (username.equals("admin") && password.equals("password")) {
            messageLabel.setStyle("-fx-text-fill: green");
            messageLabel.setVisible(true);
        } else {
            messageLabel.setText("Invalid username or password");
            messageLabel.setVisible(true);
        }

        try {
            Parent root = FXMLLoader.load(CookingManager.class.getResource("register.fxml"));

            Stage stage = (Stage) button.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

package eu.fiit.cookingmanager.cookingmanager.controller;
import eu.fiit.cookingmanager.cookingmanager.utils.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML private TextField tf_username;
    @FXML private PasswordField pf_password;
    @FXML private Button btn_signup;
    @FXML private Button btn_login;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //tu sa logguje, ejkejej čekuješ db ci tam je taky juzer
                DBUtils.changeScene(actionEvent, "home.fxml", "Cooking Manager", "juzernejm");
            }

        });

        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "register.fxml", "Sign Up!", null);
            }
        });

    }
}

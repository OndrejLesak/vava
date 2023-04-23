package eu.fiit.cookingmanager.cookingmanager.controller;

import eu.fiit.cookingmanager.cookingmanager.utils.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button btn_loggout;

    @FXML
    private Button btn_addRecipe;

    @FXML
    private Label lbl_name;

    @FXML
    private VBox v_box;
    public String username ;

    public void setUserInformation(String username){lbl_name.setText(username);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_loggout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", "Log In!", null);
            }
        });

        btn_addRecipe.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "addRecipe.fxml", "Add recipe",null );
            }
        });

    }



}

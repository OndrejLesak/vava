package eu.fiit.cookingmanager.cookingmanager.controller;


import eu.fiit.cookingmanager.cookingmanager.utils.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddRecipeController implements Initializable {

    @FXML
    private Button btn_xmlFile;
    @FXML
    private Button btn_logout;
    @FXML
    private Button btn_back_home;
    @FXML
    private Label lbl_xmlFile;
    @FXML
    private Button btn_discard;
    FileChooser fileChooser = new FileChooser();
    File selectedFile = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_xmlFile.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.xml"));
                selectedFile = fileChooser.showOpenDialog(stage);
                if(selectedFile != null){
                    lbl_xmlFile.setText(selectedFile.getName());
                }
            }
        });

        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", resourceBundle.getString("login_title"), null, resourceBundle);
            }
        });

        btn_back_home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "home.fxml", resourceBundle.getString("cooking_manager"), null, resourceBundle);
            }
        });

        btn_discard.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "home.fxml", "Cooking Manager", GlobalVariableUser.getValue(), resourceBundle);
            }
        });

    }



}

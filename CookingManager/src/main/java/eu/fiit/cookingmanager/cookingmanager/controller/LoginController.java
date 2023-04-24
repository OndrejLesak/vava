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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML private TextField tf_username;
    @FXML private PasswordField pf_password;
    @FXML private Button btn_signup;
    @FXML private Button btn_login;
    @FXML private Label lbl_error;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean isAuthenticated = false;

                try {
                    Connection conn = new DBUtils().dbConnect();

                    String query = "SELECT * FROM public.account WHERE login=(?)";
                    PreparedStatement pstmt = conn.prepareStatement(query);

                    pstmt.setString(1, tf_username.getText());
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        GlobalVariableUser.setValue(tf_username.getText());
                        String password = rs.getString("password");
                        if (password.equals(pf_password.getText())) {
                            isAuthenticated = true;
                            break;
                        }
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (isAuthenticated) {
                    DBUtils.changeScene(actionEvent, "home.fxml", resourceBundle.getString("cooking_manager"), tf_username.getText(), resourceBundle);
                } else {
                    lbl_error.setText(resourceBundle.getString("credentials_mismatch"));
                }
            }
        });

        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "register.fxml", resourceBundle.getString("sign_up_title"), null, resourceBundle);
            }
        });

    }
}

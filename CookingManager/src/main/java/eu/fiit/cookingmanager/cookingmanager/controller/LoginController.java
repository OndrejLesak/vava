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
                //tu sa logguje, ejkejej čekuješ db ci tam je taky juzer
                //if (juzer exists) {



                try {
                    Connection conn = new DBUtils().dbConnect();

                    String query = "SELECT * FROM public.account WHERE login=(?)";
                    PreparedStatement pstmt = conn.prepareStatement(query);

                    pstmt.setString(1, tf_username.getText());
                    ResultSet rs = pstmt.executeQuery();

                    while(rs.next()){
                        String password = rs.getString("password");
                        if (password.equals(pf_password.getText())){
                            DBUtils.changeScene(actionEvent, "home.fxml", "Cooking Manager", tf_username.getText());
                        }
                        else{
                            lbl_error.setText("These credentials are incorrect!");
                        }
                    }
                    lbl_error.setText("These credentials are incorrect!");


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

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

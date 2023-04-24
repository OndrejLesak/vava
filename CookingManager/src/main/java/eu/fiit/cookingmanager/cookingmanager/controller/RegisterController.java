package eu.fiit.cookingmanager.cookingmanager.controller;

import eu.fiit.cookingmanager.cookingmanager.utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML private TextField tf_username;
    @FXML private PasswordField pf_password;
    @FXML private PasswordField pf_confpassword;
    @FXML private TextField tf_name;
    @FXML private TextField tf_surname;
    @FXML private TextField tf_email;
    @FXML private Button btn_signup;
    @FXML private Button btn_login;
    @FXML private Label lbl_error;
    @FXML private ChoiceBox chbox_type;

    private final static Logger logger = LogManager.getLogger(RegisterController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        chbox_type.setItems(FXCollections.observableArrayList("Guest", "Chef")
        );
        chbox_type.setValue("Guest");



        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (!pf_password.getText().equals(pf_confpassword.getText())) {
                    lbl_error.setText(resourceBundle.getString("password_notmatch"));
                } else if (tf_name.getText().equals("")  || tf_surname.getText().equals("")  || tf_email.getText().equals("") || tf_username.getText().equals("") || pf_password.getText().equals("")) {
                    lbl_error.setText(resourceBundle.getString("fields_notfilled"));
                }

                try {
                    Connection conn = new DBUtils().dbConnect();

                    String query = "SELECT * FROM public.user WHERE email=(?)";

                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, tf_email.getText().trim());
                    ResultSet rs = pstmt.executeQuery();

                    if (rs != null){
                        lbl_error.setText(resourceBundle.getString("email_existing"));
                    }

                    else {
                        query = "SELECT * FROM public.account WHERE login=(?)";
                        pstmt = conn.prepareStatement(query);
                        pstmt.setString(1, tf_username.getText());
                        rs = pstmt.executeQuery();

                        if (rs != null){
                            lbl_error.setText(resourceBundle.getString("user_existing"));
                        }

                        else {
                            // login pass name surname email
                            query = "INSERT INTO public.user(id, name, surname, email, user_type_id) VALUES(nextval('user_id_seq'), ?, ?, ?, ?)";
                            pstmt = conn.prepareStatement(query);

                            //pstmt.setInt(1, 8);
                            pstmt.setString(1, tf_name.getText());
                            pstmt.setString(2, tf_surname.getText());
                            pstmt.setString(3, tf_email.getText());
                            if (chbox_type.getValue() == "Guest") {
                                pstmt.setInt(4, 1);  //1 je normalny guest
                            } else if (chbox_type.getValue() == "Chef") {
                                pstmt.setInt(4, 2);  //2 je sefkuchar
                            }

                            pstmt.executeUpdate();

                            query = "SELECT * FROM public.user WHERE email=(?)";
                            pstmt = conn.prepareStatement(query);
                            pstmt.setString(1, tf_email.getText());
                            rs = pstmt.executeQuery();

                            rs.next();
                            int user_id = rs.getInt("id");
                            GlobalVariableUser.setUser(user_id, conn);

                            query = "INSERT INTO public.account(id, user_id, login, password) VALUES(nextval('account_id_seq'), ?, ?, ?)";
                            pstmt = conn.prepareStatement(query);

                            //pstmt.setInt(1, 2);
                            pstmt.setInt(1, user_id);
                            pstmt.setString(2, tf_username.getText());
                            pstmt.setString(3, pf_password.getText());

                            pstmt.executeUpdate();

                            DBUtils.dbDisconnect(conn);
                            DBUtils.changeScene(actionEvent, "home.fxml", resourceBundle.getString("cooking_manager"), tf_username.getText(), resourceBundle);
                        }

                    }

                    // login pass name surname email
                    query = "INSERT INTO public.user(name, surname, email, user_type_id) VALUES(?, ?, ?, ?)";
                    pstmt = conn.prepareStatement(query);

                    //pstmt.setInt(1, 8);
                    pstmt.setString(1, tf_name.getText().trim());
                    pstmt.setString(2, tf_surname.getText().trim());
                    pstmt.setString(3, tf_email.getText().trim());
                    pstmt.setInt(4, 1);  //1 je normalny guest

                    pstmt.executeUpdate();

                    query = "SELECT * FROM public.user WHERE email=(?)";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, tf_email.getText().trim());
                    rs = pstmt.executeQuery();

                    rs.next();
                    int user_id = rs.getInt("id");

                    query = "INSERT INTO public.account(user_id, login, password) VALUES(?, ?, ?)";
                    pstmt = conn.prepareStatement(query);

                    //pstmt.setInt(1, 2);
                    pstmt.setInt(1, user_id);
                    pstmt.setString(2, tf_username.getText().trim());
                    pstmt.setString(3, pf_password.getText().trim());

                    pstmt.executeUpdate();

                    DBUtils.dbDisconnect(conn);
                    DBUtils.changeScene(actionEvent, "home.fxml", resourceBundle.getString("cooking_manager"), tf_username.getText(), resourceBundle);

                } catch (SQLException e) {
                    logger.error(RegisterController.class.getName() + " || " + e.getMessage());

                }
            }
        });


        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", resourceBundle.getString("login_title"), null, resourceBundle);
            }
        });

    }
}

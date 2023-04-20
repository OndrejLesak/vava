package eu.fiit.cookingmanager.cookingmanager.controller;

import eu.fiit.cookingmanager.cookingmanager.utils.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private TextField tf_username;
    @FXML private PasswordField pf_password;
    @FXML private PasswordField pf_confpassword;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_surname;
    @FXML
    private TextField tf_email;
    @FXML private Button btn_signup;
    @FXML private Button btn_login;
    @FXML private Label lbl_error;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (!pf_password.getText().equals(pf_confpassword.getText())) {
                    lbl_error.setText("Passwords doesn't match!");
                } else if (tf_name.getText().equals("")  || tf_surname.getText().equals("")  || tf_email.getText().equals("") || tf_username.getText().equals("") || pf_password.getText().equals("")) {
                    lbl_error.setText("Please fill all fields!");
                }

                try {
                    Connection conn = new DBUtils().dbConnect();

                    String query = "SELECT * FROM public.user WHERE email=(?)";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, tf_email.getText());
                    ResultSet rs = pstmt.executeQuery();

                    if (rs != null){
                        System.out.println(rs);
                        lbl_error.setText("User with this email already exists!");
                    }

                    query = "SELECT * FROM public.account WHERE username=(?)";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, tf_username.getText());
                    rs = pstmt.executeQuery();

                    if (rs != null){
                        System.out.println(rs);
                        lbl_error.setText("User with this username already exists!");
                    }

                    // login pass name surname email
                    query = "INSERT INTO public.user(name, surname, email, user_type_id) VALUES(?, ?, ?, ?)";
                    pstmt = conn.prepareStatement(query);

                    //pstmt.setInt(1, 8);
                    pstmt.setString(1, tf_name.getText());
                    pstmt.setString(2, tf_surname.getText());
                    pstmt.setString(3, tf_email.getText());
                    pstmt.setInt(4, 1);  //1 je normalny guest

                    pstmt.executeUpdate();

                    query = "SELECT * FROM public.user WHERE email=(?)";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, tf_email.getText());
                    rs = pstmt.executeQuery();

                    rs.next();
                    int user_id = rs.getInt("id");

                    query = "INSERT INTO public.account(user_id, login, password) VALUES(?, ?, ?)";
                    pstmt = conn.prepareStatement(query);

                    //pstmt.setInt(1, 2);
                    pstmt.setInt(1, user_id);
                    pstmt.setString(2, tf_username.getText());
                    pstmt.setString(3, pf_password.getText());

                    pstmt.executeUpdate();

                    DBUtils.dbDisconnect(conn);
                    DBUtils.changeScene(actionEvent, "home.fxml", "Cooking Manager", tf_username.getText());

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", "Log In!", null);
            }

        });

    }
}

package eu.fiit.cookingmanager.cookingmanager.controller;

import eu.fiit.cookingmanager.cookingmanager.utils.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RecipeController implements Initializable {

    private static int food_id;

    @FXML Label lbl_recipeName;
    @FXML Label lbl_recipeType;
    @FXML Label lbl_recipeTime;
    @FXML Label lbl_steps;
    @FXML Button btn_back;
    @FXML Button btn_logout;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Connection conn = new DBUtils().dbConnect();


        try {
            String query = "SELECT * FROM public.recipe WHERE id=(?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, food_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lbl_recipeName.setText(rs.getString("name"));
                lbl_recipeTime.setText(String.valueOf(rs.getInt("time_to_cook")));
                lbl_steps.setText(rs.getString("process"));
                int food_type = rs.getInt("food_type_id");

                query = "SELECT * FROM public.food_type WHERE id=(?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, food_type);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    lbl_recipeType.setText(rs.getString("type"));
                }



            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", resourceBundle.getString("login_title"),  resourceBundle);
            }
        });

        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "home.fxml", resourceBundle.getString("cooking_manager"), resourceBundle);
            }
        });


    }

    public static void setRecipe(int id){
        food_id = id;
    }
}

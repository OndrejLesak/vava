package eu.fiit.cookingmanager.cookingmanager.controller;

import eu.fiit.cookingmanager.cookingmanager.utils.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RecipeController implements Initializable {

    private static int recipe_id;

    @FXML Label txt_recipeName;
    @FXML Label txt_recipeType;
    @FXML Label txt_recipeTime;
    @FXML TextArea txt_steps;
    @FXML Button btn_back;
    @FXML Button btn_logout;
    @FXML Button btn_createList;
    @FXML Button btn_save;
    @FXML Button btn_delete;

    private final static Logger logger = LogManager.getLogger(RecipeController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Connection conn = new DBUtils().dbConnect();

        try {
            String query = "SELECT * FROM public.recipe WHERE id=(?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, recipe_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                txt_recipeName.setText(rs.getString("name").trim());
                txt_recipeTime.setText(String.valueOf(rs.getInt("time_to_cook")).trim());
                txt_steps.setText(rs.getString("process").trim());
                int food_type = rs.getInt("food_type_id");

                query = "SELECT * FROM public.food_type WHERE id=(?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, food_type);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    txt_recipeType.setText(rs.getString("type").trim());
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.info("User logged out of the application");
                DBUtils.changeScene(actionEvent, "login.fxml", resourceBundle.getString("login_title"), resourceBundle);
            }
        });

        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.info("User requested to go back to the home screen");
                DBUtils.changeScene(actionEvent, "home.fxml", resourceBundle.getString("cooking_manager"), resourceBundle);
            }
        });

        btn_createList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //stiahnut shoppping list
            }
        });

        btn_delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //vymazat recipe ako admin
                //recipe, ingredient_recipe, plan
                logger.info(String.format("Administrator requested to delete recipe number %d", recipe_id));
                try {
                    String query = "SELECT * FROM public.plan WHERE recipe_id=(?)";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, recipe_id);
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        query = "DELETE FROM public.plan WHERE recipe_id=(?)";
                        pstmt = conn.prepareStatement(query);
                        pstmt.setInt(1, recipe_id);
                        pstmt.executeUpdate();
                    }

                    query = "SELECT * FROM public.ingredient_recipe WHERE recipe_id=(?)";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, recipe_id);
                    rs = pstmt.executeQuery();

                    while (rs.next()) {
                        query = "DELETE FROM public.ingredient_recipe WHERE recipe_id=(?)";
                        pstmt = conn.prepareStatement(query);
                        pstmt.setInt(1, recipe_id);
                        pstmt.executeUpdate();
                    }

                    query = "DELETE FROM public.recipe WHERE id=(?)";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, recipe_id);
                    pstmt.executeUpdate();
                    DBUtils.changeScene(actionEvent, "home.fxml", resourceBundle.getString("cooking_manager"), resourceBundle);

                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }

            }
        });

    }

    public static void setRecipe(int id){
        recipe_id = id;
    }
}

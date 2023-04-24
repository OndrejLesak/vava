package eu.fiit.cookingmanager.cookingmanager.controller;

import eu.fiit.cookingmanager.cookingmanager.utils.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button btn_loggout;

    @FXML
    private Button btn_addRecipe;

    @FXML
    private Label lbl_name;

    @FXML
    private VBox recipeList;

    public String username ;

    public void setUserInformation(String username){lbl_name.setText(username);
    }

    private final static Logger logger = LogManager.getLogger(HomeController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_loggout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", resourceBundle.getString("login_title"), null, resourceBundle);
            }
        });

        btn_addRecipe.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "addRecipe.fxml", resourceBundle.getString("add_recipe_title"), null, resourceBundle);
            }
        });

        loadRecipes();
    }


    public void loadRecipes() {
        DBUtils dbUtils = new DBUtils();

        try {
            Connection conn = dbUtils.dbConnect();

            String query = "SELECT r.name, r.time_to_cook, ft.type FROM recipe r" +
                    " JOIN food_type ft ON ft.id = r.food_type_id";

            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Pane recipePanel = new Pane();
                    recipePanel.setStyle("-fx-background-color: #fff; -fx-padding: 20px;");

                    recipePanel.setOnMouseClicked(e -> {

                    });

                Text recipeName = new Text(rs.getString("name"));
                    recipeName.setStyle("-fx-font-size: 23px; -fx-padding: 20px");
                    recipeName.setX(20.0);
                    recipeName.setY(40.0);

                Text recipeType = new Text(rs.getString("type"));
                Text timeToCook = new Text(String.valueOf(rs.getInt("time_to_cook")));

                recipePanel.getChildren().add(recipeName);
                recipeList.getChildren().add(recipePanel);


            }
        }
        catch (SQLException e) {
            logger.error(HomeController.class.getName() + " || " + e.getMessage());
        }

    }

}

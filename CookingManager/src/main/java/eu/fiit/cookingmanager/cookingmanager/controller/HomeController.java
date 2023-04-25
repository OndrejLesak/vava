package eu.fiit.cookingmanager.cookingmanager.controller;

import eu.fiit.cookingmanager.cookingmanager.repository.entity.Recipe;
import eu.fiit.cookingmanager.cookingmanager.utils.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML private Button btn_loggout;
    @FXML private Button btn_addRecipe;
    @FXML private Label lbl_name;
    @FXML private VBox recipeList;

    @FXML
    private ScrollPane recipeScroll;

    public String username ;

    private final static Logger logger = LogManager.getLogger(HomeController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recipeScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //recipeScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        recipeScroll.setStyle("-fx-background-color: transparent");
        lbl_name.setText(GlobalVariableUser.getName());

        if (GlobalVariableUser.getType() == 1){
            btn_addRecipe.setVisible(false);
        }
        new RecipeController();


        btn_loggout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", resourceBundle.getString("login_title"),  resourceBundle);
            }
        });

        btn_addRecipe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "addRecipe.fxml", resourceBundle.getString("add_recipe_title"), resourceBundle);
            }
        });

        loadRecipes(resourceBundle);
    }


    public void loadRecipes(ResourceBundle resourceBundle) {
        DBUtils dbUtils = new DBUtils();
        HashMap<String, Recipe> recipes = new HashMap<>();

        try {
            Connection conn = dbUtils.dbConnect();

            String query = "SELECT r.id, r.name, r.account_id, r.time_to_cook, ft.type FROM recipe r" +
                    " JOIN food_type ft ON ft.id = r.food_type_id";

            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            // load data to hash-map for quicker search time
            while (rs.next()) {
                Recipe recipe = new Recipe();
                    recipe.setId(rs.getInt("id"));
                    recipe.setName(rs.getString("name"));
                    recipe.setTimeToCook(rs.getInt("time_to_cook"));
                    recipe.setFoodType(rs.getString("type"));

                recipes.put(rs.getString("name"), recipe);
            }

            // print recipes
            for (String recipeKey : recipes.keySet()) {
                Recipe recipe = recipes.get(recipeKey);

                Pane recipePanel = new Pane();
                recipePanel.setStyle("-fx-background-color: #fff; -fx-padding: 20px;");

                // onClick event handler for recipes (opens recipe detail)
                recipePanel.setOnMouseClicked(e -> {
                    RecipeController.setRecipe(recipe.getId());
                    DBUtils.changeScene(e, "recipe.fxml", resourceBundle.getString("cooking_manager"), resourceBundle);
                });

              Text recipeName = new Text(recipe.getName());
                  recipeName.setStyle("-fx-font: normal bold 23px 'sans-serif'");
                  recipeName.setX(20.0);
                  recipeName.setY(40.0);

              Text recipeType = new Text("Food type: " + recipe.getFoodType());
                  recipeType.setStyle("-fx-font: normal 18px 'sans-serif'");
                  recipeType.setX(20.0);
                  recipeType.setY(70.0);

              Text timeToCook = new Text("Time to cook: " + recipe.getTimeToCook() + " min.");
                  timeToCook.setStyle("-fx-font: normal 18px 'sans-serif'");
                  timeToCook.setX(20.0);
                  timeToCook.setY(100.0);

              recipePanel.getChildren().addAll(recipeName, recipeType, timeToCook); // add component to the Pane component

              recipeList.getChildren().add(recipePanel); // add new item to the Vbox
          }
        }
        catch (SQLException | NullPointerException e) {
            logger.error(HomeController.class.getName() + " || " + e.getMessage());
        }

    }

}

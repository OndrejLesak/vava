package eu.fiit.cookingmanager.cookingmanager.controller;



import eu.fiit.cookingmanager.cookingmanager.utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Arrays;

public class AddRecipeController implements Initializable {

    @FXML public VBox vbox_ingredients;
    @FXML private Button btn_xmlFile;
    @FXML private Button btn_logout;
    @FXML private Button btn_back_home;
    @FXML private Button btn_adding;
    @FXML private Label lbl_xmlFile;
    @FXML private TextField inputName;
    @FXML private TextField inputTime;
    @FXML private TextArea inputSteps;
    @FXML private Button btn_recipe;
    @FXML private ChoiceBox choiceType;
    @FXML private Button btn_discard;
    FileChooser fileChooser = new FileChooser();
    File selectedFile = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {








        vbox_ingredients.setSpacing(10);
        vbox_ingredients.setPadding(new Insets(10));

        Object[][] arr_ing = {};
        try{//Get information about all ingredients and fill into array
            Connection conn = new DBUtils().dbConnect();
            String query = "Select * from public.ingredient";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();




            while (rs.next()) {
                //System.out.println("while");
                int ing_id = rs.getInt("id");

                String ing_name = rs.getString("name");

                String ing_uom = rs.getString("uom");

                int ing_weight = 0;
                //String ing_weight = rs.getString("type"); weight tam nieje v DB


                Object arrNew[][] = new Object[arr_ing.length + 1][3];


                for (int i = 0; i < arr_ing.length; i++) {
                    for (int j = 0; j < 3; j++) {
                        arrNew[i][j] = arr_ing[i][j];
                    }
                }


                arrNew[arrNew.length -1] = new Object[] {ing_id, ing_name, ing_uom,ing_weight};
                arr_ing = arrNew.clone();

                //System.out.println(arr_ing[0][1]);
                //System.out.println(arr_ing[0]);
            }
            DBUtils.dbDisconnect(conn);


            //System.out.println(arr_ing[1]);
            choiceType.setItems(FXCollections.observableArrayList(arr_ing)
            );
            choiceType.setValue("");
        }
        catch (Exception e) {

            lbl_xmlFile.setText("Database disconnected");
            lbl_xmlFile.setTextFill(Color.color(1,0,0));
            e.printStackTrace();

        }

        String [] arr = {};
        try{//Get information about all food types and fill the panel
        Connection conn = new DBUtils().dbConnect();
        String query = "Select type from public.food_type";
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();


        //robiš že hladáš types s DB
            while (rs.next()) {

                String food_type_element = rs.getString("type");

                String arrNew[] = new String[arr.length + 1];
                int i;
                for(i = 0; i < arr.length; i++) {
                    arrNew[i] = arr[i];
                }
                arrNew[i] = food_type_element;
                arr = arrNew.clone();
            }
            DBUtils.dbDisconnect(conn);

        choiceType.setItems(FXCollections.observableArrayList(arr)
        );
        choiceType.setValue("");
        }
         catch (Exception e) {

             lbl_xmlFile.setText("Database disconnected");
             lbl_xmlFile.setTextFill(Color.color(1,0,0));
             e.printStackTrace();

        }


        btn_xmlFile.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.xml"));
                selectedFile = fileChooser.showOpenDialog(stage);
                if(selectedFile != null){

                    //System.out.println(selectedFile.getAbsolutePath());
                    inputName.setText("");
                    inputTime.setText("");
                    inputSteps.setText("");
                    choiceType.setValue("");

                    try {
                        // Create a new DocumentBuilderFactory and DocumentBuilder
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();

                        // Parse the XML file into a Document object
                        Document document = builder.parse(selectedFile.getAbsolutePath());
                        // Get the root element of the XML document
                        Element root = document.getDocumentElement();

                        // Get a list of all the "recept" elements
                        NodeList receptList = root.getElementsByTagName("recept");
                        if (receptList.getLength() == 1){
                            // Loop through each "recept" element and print its name, type, time, ingredients, and steps
                            for (int i = 0; i < receptList.getLength(); i++) {
                                org.w3c.dom.Node receptNode = receptList.item(i);
                                if (receptNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                    Element receptElement = (Element) receptNode;
                                    String name = receptElement.getElementsByTagName("name").item(0).getTextContent();
                                    String type = receptElement.getElementsByTagName("type").item(0).getTextContent();
                                    String time = receptElement.getElementsByTagName("time").item(0).getTextContent();
                                     NodeList ingredientList = receptElement.getElementsByTagName("ingredient");
                                    String steps = receptElement.getElementsByTagName("steps").item(0).getTextContent();

                                    //System.out.println("Name: " + name);
                                    //System.out.println("Type: " + type);
                                    //System.out.println("Time: " + time);
                                    //System.out.println("Ingredients:");
                                    for (int j = 0; j < ingredientList.getLength(); j++) {
                                        org.w3c.dom.Node ingredientNode = ingredientList.item(j);
                                        if (ingredientNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                            Element ingredientElement = (Element) ingredientNode;
                                            String ingName = ingredientElement.getElementsByTagName("ing_name").item(0).getTextContent();
                                            int ingAmount = Integer.parseInt(ingredientElement.getElementsByTagName("ing_amount").item(0).getTextContent());
                                            System.out.println("- " + ingName + ": " + ingAmount);
                                        }
                                    }
                                    //System.out.println("Steps: " + steps);
                                    //System.out.println();





                                    Connection conn = new DBUtils().dbConnect();
                                    String query = "Select * from public.food_type where type=(?)";

                                    PreparedStatement pstmt = conn.prepareStatement(query);

                                    pstmt.setString(1, type);

                                    ResultSet rs = pstmt.executeQuery();

                                    boolean is_food_type = rs.next();
                                    DBUtils.dbDisconnect(conn);

                                    if( is_food_type){
                                        //System.out.println("Hello this food is here");
                                        inputName.setText(name);
                                        inputTime.setText(time);
                                        inputSteps.setText(steps);
                                        choiceType.setValue(type);
                                        lbl_xmlFile.setText(selectedFile.getName());
                                        lbl_xmlFile.setTextFill(Color.color(0,0,0));
                                     }
                                    else
                                    {
                                        lbl_xmlFile.setText("Invalid food type");
                                        lbl_xmlFile.setTextFill(Color.color(1,0,0));

                                    }

                                }
                            }

                        }
                        else {
                            lbl_xmlFile.setText("Invalid XML file");
                            lbl_xmlFile.setTextFill(Color.color(1,0,0));
                        }


                    } catch (Exception e) {
                        lbl_xmlFile.setText("Invalid XML file");
                        lbl_xmlFile.setTextFill(Color.color(1,0,0));
                        e.printStackTrace();

                    }

                }
            }
        });



        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", resourceBundle.getString("login_title"), resourceBundle);
            }
        });

        btn_back_home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "home.fxml", resourceBundle.getString("cooking_manager"), resourceBundle);
            }
        });

        btn_discard.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "home.fxml", "Cooking Manager", resourceBundle);
            }
        });


        btn_recipe.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "home.fxml", "Cooking Manager", resourceBundle);
            }
        });

        btn_adding.setOnAction(event -> addNewChoiceBox());

        /*btn_adding.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                //DBUtils.changeScene(actionEvent, "login.fxml", resourceBundle.getString("login_title"), resourceBundle);
                System.out.println("adding");
            }
        });
        */

    }

    private void addNewChoiceBox() {

        HBox hbox = new HBox();
        hbox.setSpacing(10);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Flour", "Sugar", "Butter", "Salt");

        // Create a delete button for the new ChoiceBox
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            vbox_ingredients.getChildren().remove(hbox);
        });

        hbox.getChildren().addAll(choiceBox, deleteButton);

        // Add the HBox to the VBox layout
        vbox_ingredients.getChildren().add(hbox);
    }




}

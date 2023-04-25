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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddRecipeController implements Initializable {

    @FXML private Button btn_xmlFile;
    @FXML private Button btn_logout;
    @FXML private Button btn_back_home;
    @FXML private Label lbl_xmlFile;

    @FXML
    private Button btn_recipe;
    @FXML private Button btn_discard;
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

                    System.out.println(selectedFile.getAbsolutePath());

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
                                    int time = Integer.parseInt(receptElement.getElementsByTagName("time").item(0).getTextContent());
                                    NodeList ingredientList = receptElement.getElementsByTagName("ingredient");
                                    String steps = receptElement.getElementsByTagName("steps").item(0).getTextContent();

                                    System.out.println("Name: " + name);
                                    System.out.println("Type: " + type);
                                    System.out.println("Time: " + time);
                                    System.out.println("Ingredients:");
                                    for (int j = 0; j < ingredientList.getLength(); j++) {
                                        org.w3c.dom.Node ingredientNode = ingredientList.item(j);
                                        if (ingredientNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                            Element ingredientElement = (Element) ingredientNode;
                                            String ingName = ingredientElement.getElementsByTagName("ing_name").item(0).getTextContent();
                                            int ingAmount = Integer.parseInt(ingredientElement.getElementsByTagName("ing_amount").item(0).getTextContent());
                                            System.out.println("- " + ingName + ": " + ingAmount);
                                        }
                                    }
                                    System.out.println("Steps: " + steps);
                                    System.out.println();


                                }
                            }
                            lbl_xmlFile.setText(selectedFile.getName());
                        }
                        else {
                            lbl_xmlFile.setText("Invalid XML file");
                        }


                    } catch (Exception e) {
                        lbl_xmlFile.setText("Invalid XML file");
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
                DBUtils.changeScene(actionEvent, "home.fxml", "Cooking Manager", GlobalVariableUser.getName(), resourceBundle);
            }
        });
    }



}

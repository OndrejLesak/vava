package eu.fiit.cookingmanager.cookingmanager.controller;

import eu.fiit.cookingmanager.cookingmanager.utils.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
public class GlobalVariableUser {

    private static String value;

    public static void setValue(String newValue) {
        value = newValue;
    }

    public static String getValue() {
        return value;
    }
}

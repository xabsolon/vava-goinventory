package com.example.vavagoinventory.Employee;

import com.example.vavagoinventory.ApplicationController;
import com.example.vavagoinventory.FunctionsController;
import com.example.vavagoinventory.Log;
import com.example.vavagoinventory.Login.FadingIntroController;
import com.example.vavagoinventory.Employee.Storage.Product;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMainPageController extends ApplicationController implements Initializable {

    public Log log = new Log();

    public static ObservableList<Product> productObservableList;

    @FXML
    private Button logOutButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logOutButton.setOnAction(this::onLogOutButtonClick);
    }

    private void onLogOutButtonClick(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource("Login.fxml"));
        try {
            FunctionsController.changeScene(
                    FunctionsController.getStageFromEvent(actionEvent), loader, "GoInventory");
        } catch (IOException e) {
            log.Exceptions("Failed to load login screen",e);
        }
        log.userLogout("ferino");
    }
}

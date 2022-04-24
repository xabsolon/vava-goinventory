package com.example.vavagoinventory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private Button closeButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void closeButtonClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void changePasswordClicked(ActionEvent actionEvent) { //TODO: implement this

    }
    public void ChangeLanguage(ActionEvent actionEvent) { //TODO: implement this

    }

}

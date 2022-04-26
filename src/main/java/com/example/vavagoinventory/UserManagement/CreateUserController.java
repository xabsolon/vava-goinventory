package com.example.vavagoinventory.UserManagement;

import com.example.vavagoinventory.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateUserController implements Initializable {
    @FXML
    private Button confirmCreateButton;

    @FXML
    private Button cancelCreateButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label positionLabel;

    @FXML
    private Label createUserLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmCreateButton.textProperty().bind(I18N.createStringBinding("confirmButtonLabel"));
        cancelCreateButton.textProperty().bind(I18N.createStringBinding("cancelButtonLabel"));
        usernameLabel.textProperty().bind(I18N.createStringBinding("usernameLabel"));
        passwordLabel.textProperty().bind(I18N.createStringBinding("passwordLabel"));
        positionLabel.textProperty().bind(I18N.createStringBinding("positionLabel"));
        createUserLabel.textProperty().bind(I18N.createStringBinding("createUserLabel"));
    }
}

package com.example.vavagoinventory.UserManagement;

import com.example.vavagoinventory.Utils.I18N;
import com.example.vavagoinventory.Utils.FunctionsController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.exception.DataAccessException;

import java.net.URL;
import java.util.ResourceBundle;

public class EditUserController implements Initializable {

    private UserManagementController userManagementController;

    private User selected;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField positionField;

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
    private Label editUserLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmCreateButton.textProperty().bind(I18N.createStringBinding("confirmButtonLabel"));
        cancelCreateButton.textProperty().bind(I18N.createStringBinding("cancelButtonLabel"));
        usernameLabel.textProperty().bind(I18N.createStringBinding("usernameLabel"));
        passwordLabel.textProperty().bind(I18N.createStringBinding("passwordLabel"));
        positionLabel.textProperty().bind(I18N.createStringBinding("positionLabel"));
        editUserLabel.textProperty().bind(I18N.createStringBinding("editUserLabel"));

    }

    public void injectUserManagementController(UserManagementController userManagementController) {
        this.userManagementController = userManagementController;
        selected = userManagementController.getLastSelected();
        usernameField.setText(selected.getUsername());
        passwordField.setText(selected.getPassword());
        positionField.setText(selected.getPosition());
    }


    @FXML
    private void onClickCreate() {
        User user;
        if (!positionField.getText().equals("owner")
                && !positionField.getText().equals("user")
                && !positionField.getText().equals("logistics")) {
            FunctionsController.showErrorAlert(I18N.get("invalidPosition"));
            return;
        }

        user = new User.UserBuilder()
                .username(usernameField.getText())
                .password(passwordField.getText())
                .position(positionField.getText())
                .id(selected.getId())
                .build();

        try {
            UserManagementController.UserQuery.editQuery(user);
        }
        catch (DataAccessException e) {
            FunctionsController.showErrorAlert(I18N.get("emailUnique"));
            return;
        }

        selected.setUsername(user.getUsername());
        selected.setPassword(user.getPassword());
        selected.setPosition(user.getPosition());
        userManagementController.updateTable();

        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onClickCancel() {
        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }
}

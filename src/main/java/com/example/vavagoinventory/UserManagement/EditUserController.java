package com.example.vavagoinventory.UserManagement;

import com.example.vavagoinventory.FunctionsController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.exception.DataAccessException;

import java.net.URL;
import java.nio.Buffer;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
            FunctionsController.showErrorAlert("Invalid position");
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
            FunctionsController.showErrorAlert("Cannot edit order, email must be unique");
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

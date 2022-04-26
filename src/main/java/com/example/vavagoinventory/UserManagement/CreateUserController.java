package com.example.vavagoinventory.UserManagement;

import com.example.vavagoinventory.Utils.FunctionsController;
import com.example.vavagoinventory.Utils.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.exception.DataAccessException;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static org.jooq.codegen.maven.goinventory.Tables.ORDERS;
import static org.jooq.codegen.maven.goinventory.Tables.PRODUCTS;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.select;

public class CreateUserController implements Initializable {

    private UserManagementController userManagementController;

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

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField positionField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmCreateButton.textProperty().bind(I18N.createStringBinding("confirmButtonLabel"));
        cancelCreateButton.textProperty().bind(I18N.createStringBinding("cancelButtonLabel"));
        usernameLabel.textProperty().bind(I18N.createStringBinding("usernameLabel"));
        passwordLabel.textProperty().bind(I18N.createStringBinding("passwordLabel"));
        positionLabel.textProperty().bind(I18N.createStringBinding("positionLabel"));
        createUserLabel.textProperty().bind(I18N.createStringBinding("createUserLabel"));

    }

    public void injectUserManagementController(UserManagementController userManagementController) {
        this.userManagementController = userManagementController;
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
                .build();
        try {
            UserManagementController.UserQuery.insertQuery(user);
        }
        catch (DataAccessException e) {
            FunctionsController.showErrorAlert(I18N.get("emailUnique"));
            return;
        }
        userManagementController.addUser(user);

        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onClickCancel() {
        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }
}

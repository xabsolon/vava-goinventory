package com.example.vavagoinventory.UserManagement;

import com.example.vavagoinventory.DatabaseContextSingleton;
import com.example.vavagoinventory.FunctionsController;
import com.example.vavagoinventory.I18N;
import com.example.vavagoinventory.Orders.Order;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;

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
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField positionField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
            FunctionsController.showErrorAlert("Invalid position");
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
            FunctionsController.showErrorAlert("Cannot create order, email must be unique");
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

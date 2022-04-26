package com.example.vavagoinventory.PageControllers;

import com.example.vavagoinventory.Utils.DatabaseContextSingleton;
import com.example.vavagoinventory.Utils.FunctionsController;
import com.example.vavagoinventory.Utils.UserSingleton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.goinventory.tables.Users;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private PasswordField oldPassword;

    @FXML
    private PasswordField newPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void onClickCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onClickConfirm() {
        System.out.println(UserSingleton.getInstance().getUser().getName());
        DSLContext create = DatabaseContextSingleton.getContext();
        int success = create.update(Users.USERS)
                .set(Users.USERS.PASSWORD, newPassword.getText())
                .where(Users.USERS.EMAIL.eq(UserSingleton.getInstance().getUser().getEmail())
                        .and(Users.USERS.PASSWORD.eq(oldPassword.getText())))
                .execute();
        if (success == 0) {
            FunctionsController.showErrorAlert("Cannot change password");
        }
        else {
            FunctionsController.showConfirmationAlert("Password successfully changed");
        }

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}

package com.example.vavagoinventory;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.synedra.validatorfx.Validator;
import org.jooq.Record;
import org.jooq.codegen.maven.goinventory.tables.Users;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController implements Initializable {
    @FXML
    private BorderPane loginBorderPane;

    @FXML
    private Rectangle exitButton;

    @FXML
    private TextField emailField;

    @FXML
    private Text forgotpassButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginLabel;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private ImageView logoView;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signupButton;

    private final Validator validator = new Validator();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupFormValidators();
    }

    private void setupFormValidators() {
        validator.createCheck()
                .dependsOn("email", emailField.textProperty())
                .withMethod(c -> {
                    String email = c.get("email");
                    String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(email);
                    if (!matcher.matches()) {
                        c.error("Email is not valid.");
                    }
                })
                .decorates(emailField);
        validator.createCheck()
                .dependsOn("password", passwordField.textProperty())
                .withMethod(c -> {
                    String password = c.get("password");
                    if (password.length() < 5) {
                        c.error("Password must be minimum of 5 characters long.");
                    }
                })
                .decorates(passwordField);
    }

    @FXML
    private void exitButtonClicked() {
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Are you sure you want to exit ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else if (result.get() == ButtonType.CANCEL) {
            alert.close();
        }
    }

    @FXML
    private void signInButtonClicked(Event event) {
        if(!validator.validate()) {
            FunctionsController.showErrorAlert(validator.createStringBinding().get());
            return;
        }

        Record user = FunctionsController.maybeGetUserFromDatabase(emailField.getText(), passwordField.getText());

        if(user == null) {
            FunctionsController.showErrorAlert("Invalid email or password");
            return;
        }

        String position = user.get(Users.USERS.POSSITION);
        String page = position.equalsIgnoreCase("user") ? "EmployeeMainPage.fxml" : "OwnerMainPage.fxml";
        Stage stage = FunctionsController.getStageFromEvent(event);
        FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource(page));
        try {
            FunctionsController.changeScene(stage, loader, "GoInventory");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package com.example.vavagoinventory;

import com.example.vavagoinventory.Exceptions.WrongUserNameOrPasswordException;
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
import org.jooq.codegen.maven.goinventory.tables.records.UsersRecord;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.vavagoinventory.FunctionsController.log;

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

    @FXML
    public Button langEnButton;

    @FXML
    public Button langSkButton;

    private final Validator validator = new Validator();


    WrongUserNameOrPasswordException wunpException = new WrongUserNameOrPasswordException();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (I18N.getLocale().getLanguage().equals("en")) {
            Locale.setDefault(new Locale("en"));
        }
        setupFormValidators();
        setupTranslation();
    }

    private void setupTranslation() {
        loginLabel.textProperty().bind(I18N.createStringBinding("signIn"));
        passwordField.promptTextProperty().bind(I18N.createStringBinding("passwordPlaceholder"));
        forgotpassButton.textProperty().bind(I18N.createStringBinding("forgotPassword"));
        loginButton.textProperty().bind(I18N.createStringBinding("signInButton"));
        signupButton.textProperty().bind(I18N.createStringBinding("signUpButton"));
        signupButton.textProperty().bind(I18N.createStringBinding("signUpButton"));
        langEnButton.setOnAction((evt) -> {
            I18N.setLocale(new Locale("en"));
            Locale.setDefault(new Locale("en"));
        });
        langSkButton.setOnAction((evt) -> {
            I18N.setLocale(new Locale("sk"));
            Locale.setDefault(new Locale("sk"));
        });
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
                        c.error(I18N.get("emailInvalid"));
                        // neni to nutne tu, tu je len priklad ako to budeme pouzivat
                        try {
                            throw wunpException;
                        } catch (WrongUserNameOrPasswordException e) {
                            log.Exceptions("Wrong email", e);
                        }
                    }
                })
                .decorates(emailField);
        validator.createCheck()
                .dependsOn("password", passwordField.textProperty())
                .withMethod(c -> {
                    String password = c.get("password");
                    if (password.length() < 5) {
                        c.error(I18N.get("passwordInvalid"));
                    }
                })
                .decorates(passwordField);
    }

    @FXML
    private void exitButtonClicked() {
        FunctionsController.showExitAlert("Are you sure you want to exit?", "Exit");
    }

    @FXML
    private void signInButtonClicked(Event event) {
        if(!validator.validate()) {
            FunctionsController.showErrorAlert(validator.createStringBinding().get());
            return;
        }

        UsersRecord user = FunctionsController.maybeGetUserFromDatabase(emailField.getText(), passwordField.getText());

        if(user == null) {
            FunctionsController.showErrorAlert(I18N.get("wrongCredentials"));
            return;
        }
        String page = "MainPage.fxml";
        Stage stage = FunctionsController.getStageFromEvent(event);
        FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource(page));
        log.login(user.getName());

        UserSingleton.getInstance().setUser(user);

        try {
            FunctionsController.changeScene(stage, loader, "GoInventory");
        } catch (IOException e) {
            e.printStackTrace();
            log.Exceptions("Failed to load login screen",e);
        }
    }
}

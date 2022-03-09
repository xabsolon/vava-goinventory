package com.example.vavagoinventory;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private BorderPane LoginBorderPane;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
        //docasny kod, aby sa dalo dostat k employee a owner main pageom ktore som vytvoril (Jozo)
        //kto bude robit login system a prepojenie s db tak to potom nahradte svojim kodom

        if (String.valueOf(emailField.getCharacters()).equals("employee@example.com")) {
            try {
                Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(EmployeeMainPageController.class.getResource("EmployeeMainPage.fxml"));
                FunctionsController.changeScene(stage, loader, "Login page");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (String.valueOf(emailField.getCharacters()).equals("owner@example.com")) {
            try {
                Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(OwnerMainPageController.class.getResource("OwnerMainPage.fxml"));
                FunctionsController.changeScene(stage, loader, "Login page");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

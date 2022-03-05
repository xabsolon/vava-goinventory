package com.example.vavagoinventory;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FadingIntroController implements Initializable {
    @FXML
    private AnchorPane IntroAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::fadeOut);
    }

    private void fadeOut() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(2500));
        fadeTransition.setNode(IntroAnchorPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);


        fadeTransition.setOnFinished(actionEvent -> {
            try {
                Stage stage = (Stage) IntroAnchorPane.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource("Login.fxml"));
                FunctionsController.changeScene(stage, loader, "Login page");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fadeTransition.play();
    }
}
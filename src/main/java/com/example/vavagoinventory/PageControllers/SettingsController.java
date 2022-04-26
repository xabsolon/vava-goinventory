package com.example.vavagoinventory.PageControllers;

import com.example.vavagoinventory.ApplicationController;
import com.example.vavagoinventory.Utils.I18N;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private Button closeButton;
    @FXML
    private ToggleButton skButton;
    @FXML
    private ToggleButton enButton;
    @FXML
    private Button changePassword;
    @FXML
    private Label settingsLabel;
    @FXML
    private Label languageLabel;

    ToggleGroup group = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        languageLabel.textProperty().bind(I18N.createStringBinding("languageLabel"));
        closeButton.textProperty().bind(I18N.createStringBinding("closeButton"));
        settingsLabel.textProperty().bind(I18N.createStringBinding("settingsLabel"));
        changePassword.textProperty().bind(I18N.createStringBinding("changePassword"));

        skButton.setToggleGroup(group);
        enButton.setToggleGroup(group);

        if (Locale.getDefault().getLanguage().equals("sk")) {
            skButton.setSelected(true);
        } else {
            enButton.setSelected(true);
        }

        skButton.setOnAction((evt) -> I18N.setLocale(new Locale("sk")));
        enButton.setOnAction((evt) -> I18N.setLocale(new Locale("en")));
    }

    public void closeButtonClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void changePasswordClicked(ActionEvent actionEvent) throws IOException { //TODO: implement this
        Stage newstage = new Stage();
        FXMLLoader loader = new FXMLLoader(ApplicationController.class.getResource("ChangePassword.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        newstage.showAndWait();
    }

    public void ChangeLanguage(ActionEvent actionEvent) { //TODO: implement this

    }

}

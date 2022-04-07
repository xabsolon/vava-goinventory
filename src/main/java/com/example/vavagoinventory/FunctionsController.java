package com.example.vavagoinventory;

import com.example.vavagoinventory.DBconnector.DatabaseContextSingleton;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jooq.Record;
import org.jooq.User;
import org.jooq.codegen.maven.goinventory.tables.Users;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class FunctionsController {

    public static void showConfirmationAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showErrorAlert(String text) {
        Alert alert;
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(I18N.get("error"));
        alert.setContentText(text);
        alert.showAndWait();
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }

    }

    public static Record maybeGetUserFromDatabase(String email, String password) {
        return DatabaseContextSingleton.getContext().select()
                .from(Users.USERS)
                .where(Users.USERS.EMAIL.eq(email).and(Users.USERS.PASSWORD.eq(password)))
                .fetchOne();
    }

    public static Stage getStageFromEvent(Event event) {
        return (Stage) ((Node) event.getTarget()).getScene().getWindow();
    }

    public static void changeScene(Stage stage, FXMLLoader loader, String title) throws IOException {
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.centerOnScreen();
        stage.show();
    }
    public static void openWindow(URL page) throws Exception {
        Stage newstage = new Stage();
        Parent root = FXMLLoader.load(page);
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        newstage.showAndWait();
    }

}

package com.example.vavagoinventory;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.jooq.Record;
import org.jooq.codegen.maven.goinventory.tables.Users;

import java.io.IOException;

public class FunctionsController {

    public static void showErrorAlert(String text) {
        Alert alert;
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static Record maybeGetUserFromDatabase(String email, String password) {
        Record user = DatabaseContextSingleton.getContext().select()
                .from(Users.USERS)
                .where(Users.USERS.EMAIL.eq(email).and(Users.USERS.PASSWORD.eq(password)))
                .fetchOne();
        return user;
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

}
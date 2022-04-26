package com.example.vavagoinventory;

import com.example.vavagoinventory.Utils.DatabaseContextSingleton;
import com.example.vavagoinventory.Utils.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("FadingIntro.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Fading!");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        DatabaseContextSingleton.init();
        setupLogger();
    }
    private void setupLogger() {
        try {
            FileHandler fh = new FileHandler("src/logs/manage.log", true);
            Log.LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        // Disable JOOQ annoying logger
        LogManager.getLogManager().reset();
        launch();
    }
}
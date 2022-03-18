package com.example.vavagoinventory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMainPageController implements Initializable {
    public Log log = new Log();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void exitButtonClicked() {
        log.userLogout("ferino");
        System.exit(0);
    }
}

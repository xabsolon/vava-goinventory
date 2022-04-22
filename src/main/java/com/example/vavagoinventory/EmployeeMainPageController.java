package com.example.vavagoinventory;

import com.example.vavagoinventory.Storage.Product;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.jooq.DSLContext;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Products;
import org.jooq.codegen.maven.goinventory.tables.records.ProductsRecord;

public class EmployeeMainPageController extends ApplicationController implements Initializable {

    public Log log = new Log();

    public static ObservableList<Product> productObservableList;

    @FXML
    private Button logOutButton;

    @FXML
    private TextField storageSearchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logOutButton.setOnAction(this::onLogOutButtonClick);
    }

    @FXML
    private void onLogOutButtonClick(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource("Login.fxml"));
        try {
            FunctionsController.changeScene(
                    FunctionsController.getStageFromEvent(actionEvent), loader, "GoInventory");
        } catch (IOException e) {
            log.Exceptions("Failed to load login screen", e);
        }
        log.userLogout("ferino");
    }

    public void exitButtonClicked(MouseEvent mouseEvent) {
        System.exit(0);
    }
    public void settingsButtonClicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource("Settings.fxml"));
        try {
            FunctionsController.changeScene(
                    FunctionsController.getStageFromEvent(actionEvent), loader, "GoInventory");
        } catch (IOException e) {
            log.Exceptions("Failed to load settings screen", e);
        }
    }

    //TODO fix this, make this function work for all search fields to prevent repeated code
/*
    public void searchStorage(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            productObservableList.clear();
            DSLContext dslContext = DatabaseContextSingleton.getContext();
            dslContext.selectFrom(Products.PRODUCTS).fetch().stream().filter(p -> p.getName().toLowerCase().contains(storageSearchField.getText().toLowerCase())).
                    forEach(p -> productObservableList.add(Product product = new Product.ProductBuilder()
                            .name(p.getName())
                            .quantity(p.getQuantity())
                            .sellingPrice(p.getSellingprice())
                            .build()));
        }
    }

 */
}
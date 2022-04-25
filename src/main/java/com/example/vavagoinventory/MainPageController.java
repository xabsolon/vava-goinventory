package com.example.vavagoinventory;

import com.example.vavagoinventory.Storage.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Products;
import org.jooq.codegen.maven.goinventory.tables.records.ProductsRecord;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainPageController extends ApplicationController implements Initializable { //TODO get user to this stage from login page

    public Log log = new Log();

    public static ObservableList<Product> productObservableList = FXCollections.observableArrayList();

    @FXML
    private Button logOutButton;

    @FXML
    private TextField storageSearchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logOutButton.setOnAction(this::onLogOutButtonClick);
        System.out.println(UserSingleton.getInstance().getUser().getName());
        super.init();
    }

    @FXML
    private void onLogOutButtonClick(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource("Login.fxml"));
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out ?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            try {
                FunctionsController.changeScene(
                        FunctionsController.getStageFromEvent(actionEvent), loader, "GoInventory");
            } catch (IOException e) {
                log.Exceptions("Failed to load login screen", e);
            }
            log.userLogout();
            UserSingleton.getInstance().setUser(null);
        }
    }

    public void exitButtonClicked(MouseEvent mouseEvent) {
        FunctionsController.showExitAlert("Are you sure you want to exit ?", "Exit");
    }

    public void settingsButtonClicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource("Settings.fxml"));
        try {
            FunctionsController.openWindow("Settings.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            log.Exceptions("Failed to load settings screen", e);
        }
    }

    public void searchStorage(KeyEvent event) {
        String searchQuery = storageSearchField.getText();
        if (event.getCode() == KeyCode.ENTER) {
            productObservableList.clear();
            DSLContext dslContext = DatabaseContextSingleton.getContext();
            Result<ProductsRecord> result = dslContext
                .selectFrom(Products.PRODUCTS)
                .where(Products.PRODUCTS.NAME.likeIgnoreCase("%" + searchQuery + "%"))
                .fetch();

            result.forEach(p -> {
                Product product = new Product.ProductBuilder()
                        .name(p.getName())
                        .quantity(p.getQuantity())
                        .sellingPrice(p.getSellingprice())
                        .build();
                productObservableList.add(product);
            });
            System.out.println(productObservableList);
        }
    }


}
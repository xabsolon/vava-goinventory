package com.example.vavagoinventory.Storage;

import com.example.vavagoinventory.ApplicationController;
import com.example.vavagoinventory.DatabaseContextSingleton;
import com.example.vavagoinventory.FunctionsController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Products;
import org.jooq.codegen.maven.goinventory.tables.records.ProductsRecord;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class AddProductController extends ApplicationController implements Initializable {

    private ApplicationController applicationController;

    private Product selected;

    @FXML
    private AnchorPane addProductPane;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<?> choiceBox;

    @FXML
    private TextField quantityField;

    ArrayList<Product> products = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void injectApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
        selected = applicationController.getLastSelectedProduct();
    }

    @FXML
    void onClickCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickConfirm() {
        if (quantityField.getText().isEmpty()) {
            FunctionsController.showErrorAlert("Please enter a quantity");
        } else if (!FunctionsController.isNumeric(quantityField.getText())) {
            FunctionsController.showErrorAlert("Please enter a valid quantity");
        } else {
            DSLContext create = DatabaseContextSingleton.getContext();
            create.update(Products.PRODUCTS)
                    .set(Products.PRODUCTS.QUANTITY, Products.PRODUCTS.QUANTITY.plus(Integer.parseInt(quantityField.getText())))
                    .where(Products.PRODUCTS.P_ID.eq(selected.getId()))
                    .execute();
            FunctionsController.log.ProductAdded((String) selected.getName(), Integer.parseInt(quantityField.getText()));

            selected.setQuantity(selected.getQuantity() + Integer.parseInt(quantityField.getText()));
            applicationController.updateTable();
            Stage stage = (Stage) addProductPane.getScene().getWindow();
            stage.close();
        }
    }

}

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
        ObservableList data = FXCollections.observableArrayList();
        Platform.runLater(() -> {
            DSLContext create = DatabaseContextSingleton.getContext();
            Result<ProductsRecord> result = create.selectFrom(Products.PRODUCTS).groupBy(Products.PRODUCTS.NAME).fetch();
            result.forEach(p -> {
                Product product = new Product.ProductBuilder()
                        .id(p.getPId())
                        .name(p.getName())
                        .build();
                products.add(product);
                data.add(product.getName());
            });
        });
        choiceBox.setItems(data);
    }

    @FXML
    void onClickCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickConfirm() {
        if (choiceBox.getSelectionModel().isEmpty()) {
            FunctionsController.showErrorAlert("Please select a product");
        } else if (quantityField.getText().isEmpty()) {
            FunctionsController.showErrorAlert("Please enter a quantity");
        } else if (!FunctionsController.isNumeric(quantityField.getText())) {
            FunctionsController.showErrorAlert("Please enter a valid quantity");
        } else {
            DSLContext create = DatabaseContextSingleton.getContext();
            Result<ProductsRecord> result = create.selectFrom(Products.PRODUCTS).fetch();

            AtomicInteger quantityTemp = new AtomicInteger();
            result.forEach(p -> {
                if (p.getName().equals(choiceBox.getValue())) {
                    quantityTemp.set(p.getQuantity());
                }
            });

            quantityTemp.addAndGet(Integer.parseInt(quantityField.getText()));
            create.update(Products.PRODUCTS).set(Products.PRODUCTS.QUANTITY, quantityTemp.get()).where(Products.PRODUCTS.NAME.eq((String) choiceBox.getValue())).execute();
            FunctionsController.log.ProductAdded((String) choiceBox.getValue(), Integer.parseInt(quantityField.getText()));
            Stage stage = (Stage) addProductPane.getScene().getWindow();
            stage.close();
        }
    }

}

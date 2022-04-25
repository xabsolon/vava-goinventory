package com.example.vavagoinventory.Storage;

import com.example.vavagoinventory.ApplicationController;
import com.example.vavagoinventory.DatabaseContextSingleton;
import com.example.vavagoinventory.FunctionsController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Products;
import org.jooq.codegen.maven.goinventory.tables.records.ProductsRecord;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditProductController extends ApplicationController implements Initializable {

    private ApplicationController applicationController;

    private Product selected;

    @FXML
    private AnchorPane confirmEditStoragePane;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField nameField;

    @FXML
    void onClickCancel() {
        Stage stage = (Stage) priceField.getScene().getWindow();
        stage.close();
    }

    public void injectApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
        selected = applicationController.getLastSelectedProduct();
        priceField.setText(Double.toString(selected.getSellingPrice()));
        quantityField.setText(Integer.toString(selected.getQuantity()));
        nameField.setText(selected.getName());
    }

    static boolean isProductChanged = false;

    ArrayList<Product> products = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void onClickEdit() throws SQLException {
        double sellingPrice;

        String name = nameField.getText();
        if (name == null) {
            FunctionsController.showErrorAlert("Name field is empty");
        } else {
            String quantity = quantityField.getText();
            if (quantity.isEmpty()) {
                FunctionsController.showErrorAlert("Quantity field is empty");
            } else if (!FunctionsController.isNumeric(quantity)) {
                FunctionsController.showErrorAlert("Quantity field is not a number");
            } else {
                String price = priceField.getText();
                if (price.isEmpty()) {
                    FunctionsController.showErrorAlert("Price field is empty");
                } else if (!FunctionsController.isNumeric(price.replace(",", "."))) {
                    FunctionsController.showErrorAlert("Price field is not a number");
                } else {
                    sellingPrice = Double.parseDouble(price.replace(",", "."));
                    DSLContext create = DatabaseContextSingleton.getContext();
                    int success = create.update(Products.PRODUCTS)
                            .set(Products.PRODUCTS.NAME, name)
                            .set(Products.PRODUCTS.QUANTITY, Integer.parseInt(quantity))
                            .set(Products.PRODUCTS.SELLINGPRICE, (int) sellingPrice)
                            .where(Products.PRODUCTS.P_ID.eq(selected.getId()))
                            .execute();

                    if(success == 0) {
                        FunctionsController.showErrorAlert("Failed to update product");
                    } else {
                        selected.setName(name);
                        selected.setQuantity(Integer.parseInt(quantity));
                        selected.setSellingPrice(Double.parseDouble(price));
                        FunctionsController.showConfirmationAlert("Product update successfuly");
                        FunctionsController.log.ProductEdited(name);
                    }
                    isProductChanged = true;
                    applicationController.updateTable();
                    Stage stage = (Stage) confirmEditStoragePane.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }
}



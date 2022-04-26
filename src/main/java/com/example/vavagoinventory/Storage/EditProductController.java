package com.example.vavagoinventory.Storage;

import com.example.vavagoinventory.ApplicationController;
import com.example.vavagoinventory.DatabaseContextSingleton;
import com.example.vavagoinventory.FunctionsController;
import com.example.vavagoinventory.I18N;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
    private Button cancelEditButton;

    @FXML
    private Button confirmEditButton;

    @FXML
    private Label priceLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField nameField;

    @FXML
    private Label editProductLabel;

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
        nameLabel.textProperty().bind(I18N.createStringBinding("nameLabel"));
        priceLabel.textProperty().bind(I18N.createStringBinding("priceLabel"));
        quantityLabel.textProperty().bind(I18N.createStringBinding("quantityLabel"));
        confirmEditButton.textProperty().bind(I18N.createStringBinding("confirmButtonLabel"));
        cancelEditButton.textProperty().bind(I18N.createStringBinding("cancelButtonLabel"));
        editProductLabel.textProperty().bind(I18N.createStringBinding("editProductLabel"));
    }

    @FXML
    void onClickEdit() throws SQLException {
        double sellingPrice;

        String name = nameField.getText();
        if (name == null) {
            FunctionsController.showErrorAlert(I18N.get("emptyNameField"));
        } else {
            String quantity = quantityField.getText();
            if (quantity.isEmpty()) {
                FunctionsController.showErrorAlert(I18N.get("emptyQuantityField"));
            } else if (!FunctionsController.isNumeric(quantity)) {
                FunctionsController.showErrorAlert(I18N.get("invalidQuantityField"));
            } else {
                String price = priceField.getText();
                if (price.isEmpty()) {
                    FunctionsController.showErrorAlert(I18N.get("emptyPriceField"));
                } else if (!FunctionsController.isNumeric(price.replace(",", "."))) {
                    FunctionsController.showErrorAlert(I18N.get("invalidPriceField"));
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
                        FunctionsController.showErrorAlert(I18N.get("updateFailed"));
                    } else {
                        selected.setName(name);
                        selected.setQuantity(Integer.parseInt(quantity));
                        selected.setSellingPrice(Double.parseDouble(price));
                        FunctionsController.showConfirmationAlert(I18N.get("updateSuccess"));
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



package com.example.vavagoinventory.Storage;

import com.example.vavagoinventory.ApplicationController;
import com.example.vavagoinventory.Utils.DatabaseContextSingleton;
import com.example.vavagoinventory.Utils.FunctionsController;
import com.example.vavagoinventory.Utils.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.goinventory.tables.Products;
import org.jooq.codegen.maven.goinventory.tables.records.ProductsRecord;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddProductController extends ApplicationController implements Initializable {

    private ApplicationController applicationController;

    private Product selected;

    @FXML
    private AnchorPane addProductPane;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label addProductLabel;

    @FXML
    private ComboBox<?> choiceBox;

    @FXML
    private TextField quantityField;

    ArrayList<Product> products = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.textProperty().bind(I18N.createStringBinding("cancelButtonLabel"));
        confirmButton.textProperty().bind(I18N.createStringBinding("confirmButtonLabel"));
        addProductLabel.textProperty().bind(I18N.createStringBinding("addProductLabel"));
        quantityLabel.textProperty().bind(I18N.createStringBinding("quantityLabel"));
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
            FunctionsController.showErrorAlert(I18N.get("enterQuantity"));
        } else if (!FunctionsController.isNumeric(quantityField.getText())) {
            FunctionsController.showErrorAlert(I18N.get("invalidQuantity"));
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

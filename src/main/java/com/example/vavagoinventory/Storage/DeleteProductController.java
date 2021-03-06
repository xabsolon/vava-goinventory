package com.example.vavagoinventory.Storage;

import com.example.vavagoinventory.Utils.DatabaseContextSingleton;
import com.example.vavagoinventory.Utils.FunctionsController;
import com.example.vavagoinventory.Utils.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.goinventory.tables.Products;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteProductController implements Initializable {

    @FXML
    private Button cancelDeleteButton;

    @FXML
    private Button confirmDeleteButton;

    @FXML
    private Label deleteProductLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField productNameField;

    public void onClickDelete() {
        if (productNameField.getText().isEmpty()) {
            FunctionsController.showErrorAlert("Please enter a product name");
        } else {
            DSLContext create = DatabaseContextSingleton.getContext();
            int success = create.deleteFrom(Products.PRODUCTS).where(Products.PRODUCTS.NAME.eq(productNameField.getText())).execute();

            if(success == 0) {
                FunctionsController.showErrorAlert("Product with name " + productNameField.getText() + " does not exist.");
            } else {
                FunctionsController.showConfirmationAlert("Product Deleted Successfully");
                FunctionsController.log.ProductDeleted(productNameField.getText());
            }

            Stage stage = (Stage) cancelDeleteButton.getScene().getWindow();
            stage.close();
        }
    }

    public void onClickCancel() {
        Stage stage = (Stage) cancelDeleteButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.textProperty().bind(I18N.createStringBinding("nameLabel"));
        deleteProductLabel.textProperty().bind(I18N.createStringBinding("deleteProductLabel"));
        confirmDeleteButton.textProperty().bind(I18N.createStringBinding("confirmDeleteButton"));
        cancelDeleteButton.textProperty().bind(I18N.createStringBinding("cancelDeleteButton"));
    }
}

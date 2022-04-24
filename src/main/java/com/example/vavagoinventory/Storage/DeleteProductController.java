package com.example.vavagoinventory.Storage;

import com.example.vavagoinventory.DatabaseContextSingleton;
import com.example.vavagoinventory.FunctionsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.goinventory.tables.Products;

public class DeleteProductController {

    @FXML
    private Button cancelDeleteButton;
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
}

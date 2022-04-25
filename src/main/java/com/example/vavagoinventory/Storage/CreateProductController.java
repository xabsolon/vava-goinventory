package com.example.vavagoinventory.Storage;

import com.example.vavagoinventory.ApplicationController;
import com.example.vavagoinventory.DatabaseContextSingleton;
import com.example.vavagoinventory.FunctionsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.goinventory.tables.Products;
import org.jooq.codegen.maven.goinventory.tables.records.ProductsRecord;

import java.util.Comparator;

import static com.example.vavagoinventory.ApplicationController.productsObservableList;

public class CreateProductController {

    private ApplicationController applicationController;

    @FXML
    private Button cancelCreateButton;

    @FXML
    private TextField productNameField;

    public void injectApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @FXML
    void onClickCancel() {
        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickConfirm() {

        if (productNameField.getText().isEmpty()) {
            FunctionsController.showErrorAlert("Please enter a product name");
        } else if (productsObservableList.stream().anyMatch(product -> product.getName().equals(productNameField.getText()))) {
            FunctionsController.showErrorAlert("Product already exists");
        } else {

            DSLContext create = DatabaseContextSingleton.getContext();
            ProductsRecord productRecord = create.newRecord(Products.PRODUCTS);
            productRecord.setName(productNameField.getText());
            productRecord.store();
            int id = productRecord.getPId();

            Product product = new Product.ProductBuilder()
                    .name(productNameField.getText())
                    .quantity(0)
                    .sellingPrice(0)
                    .id(id)
                    .build();
            productsObservableList.add(product);

            FunctionsController.showConfirmationAlert("Product created successfully");
            FunctionsController.log.ProductCreated(product.getName());
            Comparator<Product> productComparator = Comparator.comparing(Product::getQuantity);
            productsObservableList.sort(productComparator);

            applicationController.addProduct(product);

            Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
            stage.close();
        }
    }

}

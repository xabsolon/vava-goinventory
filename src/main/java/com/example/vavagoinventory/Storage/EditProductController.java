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

    @FXML
    private AnchorPane confirmEditStoragePane;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private ComboBox<?> nameField;

    @FXML
    void onClickCancel() {
        Stage stage = (Stage) priceField.getScene().getWindow();
        stage.close();
    }

    static boolean isProductChanged = false;

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
        nameField.setItems(data);
    }

    @FXML
    void onClickEdit() throws SQLException {
        double sellingPrice;

        Object name = nameField.getValue();
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
                            .set(Products.PRODUCTS.NAME, (String) name)
                            .set(Products.PRODUCTS.QUANTITY, Integer.parseInt(quantity))
                            .set(Products.PRODUCTS.SELLINGPRICE, (int) sellingPrice)
                            .execute();

                    if(success == 0) {
                        FunctionsController.showErrorAlert("Failed to update project");
                    } else {
                        FunctionsController.showConfirmationAlert("Product update successfuly");
                        FunctionsController.log.ProductEdited((String) name);
                    }
                    isProductChanged = true;
                    Stage stage = (Stage) confirmEditStoragePane.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }
}



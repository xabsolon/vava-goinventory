package com.example.vavagoinventory.Employee.Storage;
import com.example.vavagoinventory.DBconnector.DatabaseConnection;
import com.example.vavagoinventory.FunctionsController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

import static com.example.vavagoinventory.ApplicationController.productsObservableList;

public class CreateProductController implements Initializable {

    @FXML
    private Button confirmCreateButton;

    @FXML
    private Button cancelCreateButton;

    @FXML
    private TextField productNameField;

    DatabaseConnection connectivity = new DatabaseConnection();
    Connection connection = connectivity.getConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void onClickCancel(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickConfirm(javafx.event.ActionEvent actionEvent) throws SQLException {
        //toto som tu len docasne pridal aby sa to dalo skompilovat lebo inac to nejde a v tej novej verzii sa ten list
        //neda naimportovat
        //List<Product> productsObservableList = null;

        if (productNameField.getText().isEmpty()) {
            FunctionsController.showErrorAlert("Please enter a product name");
        } else if (productsObservableList.stream().anyMatch(product -> product.getName().equals(productNameField.getText()))) {
            FunctionsController.showErrorAlert("Product already exists");
        } else {
            Statement statement = connection.createStatement();
            String insert = "INSERT INTO products (name) VALUES ('" + productNameField.getText() + "')";
            statement.executeLargeUpdate(insert);

            Product product = new Product.ProductBuilder()
                    .name(productNameField.getText())
                    .quantity(0)
                    .sellingPrice(0)
                    .build();
            productsObservableList.add(product);

            //FunctionsController.showConfirmationAlert("Product created successfully");

            Comparator<Product> productComparator = Comparator.comparing(Product::getQuantity);
            Collections.sort(productsObservableList, productComparator);

            connection.close();

            Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
            stage.close();
        }
    }

}

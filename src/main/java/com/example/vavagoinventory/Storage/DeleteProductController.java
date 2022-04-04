package com.example.vavagoinventory.Storage;

import com.example.vavagoinventory.DatabaseConnection;

import com.example.vavagoinventory.FunctionsController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static com.example.vavagoinventory.EmployeeMainPageController.productsObservableList;

public class DeleteProductController implements Initializable {

    @FXML
    private Button cancelDeleteButton, confirmDeleteButton;
    @FXML
    private TextField productNameField;

    static boolean isProductDeleted = true;

    DatabaseConnection connectivity = new DatabaseConnection();
    Connection connection = connectivity.getConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onClickDelete(javafx.event.ActionEvent event) throws SQLException {
        if (productNameField.getText().isEmpty()) {
            FunctionsController.showErrorAlert("Please enter a product name");
        }else {
            Statement statement = connection.createStatement();
            String select = "SELECT p_id,name,quantity,sellingPrice FROM products ";
            ResultSet result = connection.prepareStatement(select).executeQuery();
            isProductDeleted = true;
            if (isProductDeleted) {
                String delete = "DELETE FROM products WHERE name = '" + productNameField.getText() + "'";
                statement.executeLargeUpdate(delete);

                connection.close();

                productsObservableList.forEach(product -> {
                    String product_name = product.getName();
                    if (product_name.equals(productNameField.getText())) {
                        productsObservableList.remove(product);
                        FunctionsController.showConfirmationAlert("Product Deleted Successfully");
                        Stage stage = (Stage) cancelDeleteButton.getScene().getWindow();
                        stage.close();
                    }
                });
            } else {
            Stage stage = (Stage) cancelDeleteButton.getScene().getWindow();
            stage.close();
            }

        }
    }

    public void onClickCancel(javafx.event.ActionEvent event) {
        isProductDeleted = false;
        Stage stage = (Stage) cancelDeleteButton.getScene().getWindow();
        stage.close();

    }
}

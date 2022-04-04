package com.example.vavagoinventory.Storage;

import com.example.vavagoinventory.ApplicationController;
import com.example.vavagoinventory.FunctionsController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) priceField.getScene().getWindow();
        stage.close();
    }

    public int quantityTemp;
    static boolean isProductChanged = false;

    DatabaseConnection connectivity = new DatabaseConnection();
    Connection connection = connectivity.getConnection();

    ArrayList<Product> products = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList data = FXCollections.observableArrayList();
        Platform.runLater(() -> {
            String select = "SELECT p_id, name FROM products GROUP BY name";
            try {
                ResultSet result = connection.prepareStatement(select).executeQuery();
                while (result.next()) {
                    Product product = new Product.ProductBuilder()
                            .id(result.getInt(1))
                            .name(result.getString(2))
                            .build();
                    products.add(product);
                    data.add(product.getName());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        nameField.setItems(data);
    }

    @FXML
    void onClickEdit(ActionEvent event) throws SQLException {
        Double sellingPrice = null;

        if (nameField.getValue() == null) {
            FunctionsController.showErrorAlert("Name field is empty");
        } else if (quantityField.getText().isEmpty()) {
            FunctionsController.showErrorAlert("Quantity field is empty");
        } else if (FunctionsController.isNumeric(quantityField.getText()) == false) {
            FunctionsController.showErrorAlert("Quantity field is not a number");
        } else if (priceField.getText().isEmpty()) {
            FunctionsController.showErrorAlert("Price field is empty");
        } else if (!FunctionsController.isNumeric(priceField.getText().replace(",", "."))) {
            FunctionsController.showErrorAlert("Price field is not a number");
        } else {
            sellingPrice = Double.parseDouble(priceField.getText().replace(",", "."));
            String update = "UPDATE products SET name = '" + nameField.getValue() + "',quantity =" + Integer.parseInt(quantityField.getText()) + ",sellingPrice =" + sellingPrice
                    + " WHERE name = '" + nameField.getValue() + "'";

            Statement statement = connection.createStatement();
            statement.executeLargeUpdate(update);
            System.out.println("Product updated successfully");
            isProductChanged = true;
            Stage stage = (Stage) confirmEditStoragePane.getScene().getWindow();
            stage.close();

        }
    }
}


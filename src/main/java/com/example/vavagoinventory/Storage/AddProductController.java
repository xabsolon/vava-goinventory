package com.example.vavagoinventory.Storage;


public class AddProductController {

import com.example.vavagoinventory.ApplicationController;
import com.example.vavagoinventory.FunctionsController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

public class AddProductController extends ApplicationController implements Initializable {

    @FXML
    private AnchorPane addProductPane;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<?> choiceBox;

    @FXML
    private TextField quantityField;

    ArrayList<Product> products = new ArrayList<>();

    DatabaseConnection connectivity = new DatabaseConnection();
    Connection connection = connectivity.getConnection();

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
        choiceBox.setItems(data);
    }

    @FXML
    void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickConfirm(ActionEvent event) throws SQLException {
        Double pricePerUnit = null;

        if (choiceBox.getSelectionModel().isEmpty()) {
            FunctionsController.showErrorAlert("Please select a product");
        } else if (quantityField.getText().isEmpty()) {
            FunctionsController.showErrorAlert("Please enter a quantity");
        } else if (!FunctionsController.isNumeric(quantityField.getText())) {
            FunctionsController.showErrorAlert("Please enter a valid quantity");
        } else {
            //updating products quantity
            Statement statement = connection.createStatement();
            String select = "SELECT * FROM products";
            ResultSet resultSet = connection.prepareStatement(select).executeQuery();

            int quantityTemp = 0;
            while (resultSet.next()) {
                Product product = new Product.ProductBuilder()
                        .id(resultSet.getInt(1))
                        .name(resultSet.getString(2))
                        .quantity(resultSet.getInt(3))
                        .sellingPrice(resultSet.getDouble(4))
                        .build();

                if (product.getName().equals(choiceBox.getValue())) {
                    quantityTemp = product.getQuantity();
                    break;
                }
            }
            quantityTemp += Integer.parseInt(quantityField.getText());
            String update = "UPDATE products SET quantity = " + quantityTemp + " WHERE name = '" + choiceBox.getValue() + "'";
            statement.executeLargeUpdate(update);
            Stage stage = (Stage) addProductPane.getScene().getWindow();
            stage.close();
        }
    }

}

package com.example.vavagoinventory;

import com.example.vavagoinventory.Storage.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmployeeMainPageController implements Initializable {
    public Log log = new Log();

    public static DatabaseConnection connectivity = new DatabaseConnection();   //connectivity static so we can make calls from anywhere
    public static Connection connection = connectivity.getConnection();

    public static ObservableList<Product> productsObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public static class StorageQuery {

        public static ArrayList<Product> products = new ArrayList<>();

        // Getting storage info from database
        public static void getQuery() {
            String select = "SELECT p_id,name,quantity,sellingPrice FROM products GROUP BY name ORDER BY quantity";
            ResultSet result;
            try {
                result = DatabaseConnection.connection.prepareStatement(select).executeQuery();

                while (result.next()) {
                    Product product = new Product.ProductBuilder()
                            .id(result.getInt(1))
                            .name(result.getString(2))
                            .quantity(result.getInt(3))
                            .sellingPrice(result.getDouble(4))
                            .build();
                    products.add(product);
                }

                productsObservableList = FXCollections.observableArrayList();
                productsObservableList.addAll(products);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickCreateProduct() throws Exception {
        EmployeeMainPageController.StorageQuery query = new StorageQuery();
        query.getQuery();
        Stage newstage = new Stage();
        Parent root = FXMLLoader.load(EmployeeMainPageController.class.getResource("CreateProduct.fxml"));
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        newstage.showAndWait();
    }

    public void onClickDeleteProduct() throws Exception {
        EmployeeMainPageController.StorageQuery query = new StorageQuery();
        query.getQuery();
        Stage newstage = new Stage();
        Parent root = FXMLLoader.load(EmployeeMainPageController.class.getResource("DeleteProduct.fxml"));
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        newstage.showAndWait();
    }

    @FXML
    private void exitButtonClicked() {
        log.userLogout("ferino");
        System.exit(0);
    }
}

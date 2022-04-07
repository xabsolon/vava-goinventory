package com.example.vavagoinventory;

import com.example.vavagoinventory.Employee.EmployeeMainPageController;
import com.example.vavagoinventory.Employee.Storage.DatabaseConnection;
import com.example.vavagoinventory.Employee.Storage.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ApplicationController {

    public static DatabaseConnection connectivity = new DatabaseConnection();
    public static Connection connection = connectivity.getConnection();

    public static ObservableList<Product> productsObservableList;

    public static Product product = new Product();
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public static class StorageQuery {

        public static ArrayList<Product> products = new ArrayList<>();

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
        StorageQuery.getQuery();
        Stage newstage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(EmployeeMainPageController.class.getResource("CreateProduct.fxml")));
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        newstage.showAndWait();
    }

    public void onClickDeleteProduct() throws Exception {
        StorageQuery.getQuery();
        Stage newstage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ApplicationController.class.getResource("DeleteProduct.fxml")));
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        newstage.showAndWait();
    }

    public void onClickAddProduct() throws Exception {
        StorageQuery.getQuery();
        Stage newstage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ApplicationController.class.getResource("AddProduct.fxml")));
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        newstage.showAndWait();
    }

    public void onClickEditProduct() throws Exception {
        ApplicationController.StorageQuery query = new ApplicationController.StorageQuery();
        query.getQuery();
        Stage newstage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ApplicationController.class.getResource("EditProduct.fxml")));
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        newstage.showAndWait();
    }
}

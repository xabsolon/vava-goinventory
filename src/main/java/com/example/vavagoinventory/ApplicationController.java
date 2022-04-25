package com.example.vavagoinventory;

import com.example.vavagoinventory.Storage.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Products;
import org.jooq.codegen.maven.goinventory.tables.records.ProductsRecord;

import java.util.ArrayList;
import java.util.Objects;

public class ApplicationController {

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
            DSLContext create = DatabaseContextSingleton.getContext();
            Result<ProductsRecord> result1 = create.selectFrom(Products.PRODUCTS).groupBy(Products.PRODUCTS.NAME).orderBy(Products.PRODUCTS.QUANTITY).fetch();
            result1.forEach(p -> {
                Product product = new Product.ProductBuilder()
                        .id(p.getPId())
                        .name(p.getName())
                        .quantity(p.getQuantity())
                        .sellingPrice(p.getSellingprice())
                        .build();
                products.add(product);
            });

            productsObservableList = FXCollections.observableArrayList();
            productsObservableList.addAll(products);
        }
    }

    public void onClickCreateProduct() throws Exception {
        ApplicationController.StorageQuery query = new ApplicationController.StorageQuery();
        query.getQuery();
        Stage newstage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(MainPageController.class.getResource("CreateProduct.fxml")));
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        newstage.showAndWait();
    }

    public void onClickDeleteProduct() throws Exception {
        ApplicationController.StorageQuery query = new ApplicationController.StorageQuery();
        query.getQuery();
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
        ApplicationController.StorageQuery query = new ApplicationController.StorageQuery();
        query.getQuery();
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

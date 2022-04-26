package com.example.vavagoinventory;

import com.example.vavagoinventory.PageControllers.MainPageController;
import com.example.vavagoinventory.Storage.AddProductController;
import com.example.vavagoinventory.Storage.CreateProductController;
import com.example.vavagoinventory.Storage.EditProductController;
import com.example.vavagoinventory.Storage.Product;
import com.example.vavagoinventory.Utils.DatabaseContextSingleton;
import com.example.vavagoinventory.Utils.FunctionsController;
import com.example.vavagoinventory.Utils.I18N;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Products;
import org.jooq.codegen.maven.goinventory.tables.records.ProductsRecord;

import java.util.ArrayList;

public class ApplicationController {

    @FXML
    private CreateProductController createProductController;

    @FXML
    private EditProductController editProductController;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    private ObservableList<Product> products;

    private Product lastSelectedProduct;

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
            products.clear();
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

        public static boolean deleteQuery(Product product) {
            DSLContext create = DatabaseContextSingleton.getContext();
            try {
                create.deleteFrom(Products.PRODUCTS).where(Products.PRODUCTS.NAME.eq(product.getName())).execute();
            }
            catch (org.jooq.exception.DataAccessException e) {
                FunctionsController.showErrorAlert(I18N.get("CannotDeleteProduct"));
                return false;
            }
            return true;
        }

    }

    public void init() {
        StorageQuery.getQuery();
        products = FXCollections.observableArrayList(StorageQuery.products);
        idColumn.setCellValueFactory(products -> new SimpleIntegerProperty(products.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(products -> new SimpleStringProperty(products.getValue().getName()));
        quantityColumn.setCellValueFactory(products -> new SimpleIntegerProperty(products.getValue().getQuantity()).asObject());
        priceColumn.setCellValueFactory(products -> new SimpleDoubleProperty(products.getValue().getSellingPrice()).asObject());
        productsTable.setItems(products);
    }

    public void updateTable() {
        productsTable.refresh();
    }

    private void saveLastSelectedProduct() {
        lastSelectedProduct = productsTable.getSelectionModel().getSelectedItem();
    }

    public Product getLastSelectedProduct() {
        return lastSelectedProduct;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void onClickCreateProduct() throws Exception {
        StorageQuery.getQuery();
        Stage newstage = new Stage();
        FXMLLoader loader = new FXMLLoader(ApplicationController.class.getResource("CreateProduct.fxml"));
        Parent root = loader.load();
        CreateProductController createProductController = loader.getController();
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        createProductController.injectApplicationController(this);
        newstage.showAndWait();
    }

    public void onClickDeleteProduct() throws Exception {
        saveLastSelectedProduct();
        if (lastSelectedProduct == null) {
            FunctionsController.showErrorAlert(I18N.get("NoProductSelected"));
            return;
        }
        if (StorageQuery.deleteQuery(lastSelectedProduct)) {
            products.remove(lastSelectedProduct);
        }
    }

    public void onClickAddProduct() throws Exception {
        saveLastSelectedProduct();
        if (lastSelectedProduct == null) {
            FunctionsController.showErrorAlert(I18N.get("NoProductSelected"));
            return;
        }
        StorageQuery.getQuery();
        Stage newstage = new Stage();
        FXMLLoader loader = new FXMLLoader(ApplicationController.class.getResource("AddProduct.fxml"));
        Parent root = loader.load();
        AddProductController addProductController = loader.getController();
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        addProductController.injectApplicationController(this);
        newstage.showAndWait();
    }

    public void onClickEditProduct() throws Exception {
        saveLastSelectedProduct();
        if (lastSelectedProduct == null) {
            FunctionsController.showErrorAlert(I18N.get("NoProductSelected"));
            return;
        }
        StorageQuery.getQuery();
        Stage newstage = new Stage();
        FXMLLoader loader = new FXMLLoader(ApplicationController.class.getResource("EditProduct.fxml"));
        Parent root = loader.load();
        EditProductController editProductController = loader.getController();
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        editProductController.injectApplicationController(this);
        newstage.showAndWait();
    }

    public void onClickRefresh() {
        StorageQuery.getQuery();
        products = FXCollections.observableArrayList(StorageQuery.products);
        productsTable.setItems(products);
        updateTable();
    }

}

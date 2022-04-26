package com.example.vavagoinventory.PageControllers;

import com.example.vavagoinventory.ApplicationController;
import com.example.vavagoinventory.Storage.Product;
import com.example.vavagoinventory.Utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Products;
import org.jooq.codegen.maven.goinventory.tables.records.ProductsRecord;
import org.jooq.codegen.maven.goinventory.tables.records.UsersRecord;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainPageController extends ApplicationController implements Initializable { //TODO get user to this stage from login page

    public Log log = new Log();

    public static ObservableList<Product> productObservableList = FXCollections.observableArrayList();

    @FXML
    public Tab employeesTab;

    @FXML
    public Tab storageTab;

    @FXML
    public Tab ordersTab;

    @FXML
    public Tab ordersHistoryTab;

    @FXML
    private Button logOutButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Label homeTabLabel;

    @FXML
    private Label employeesTabLabel;

    @FXML
    private Label storageTabLabel;

    @FXML
    private Label ordersTabLabel;

    @FXML
    private Label historyTabLabel;

    @FXML
    private Label homeLabel;

    @FXML
    private Label employeesLabel;

    @FXML
    private Label storageLabel;

    @FXML
    private Label ordersLabel;

    @FXML
    private Label historyLabel;

    @FXML
    private Button createProductButton;

    @FXML
    private Button editProductButton;

    @FXML
    private Button deleteProductButton;

    @FXML
    private Button addProductButton;

    @FXML
    private Button refreshButton;

    @FXML
    private TableView productsTable;

    @FXML
    private TableColumn idColumn;

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TableColumn quantityColumn;

    @FXML
    private TableColumn priceColumn;

    @FXML
    private TextField storageSearchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.disableTabsByRole();
        this.disableButtonsByRole();
        homeTabLabel.textProperty().bind(I18N.createStringBinding("homeTabLabel"));
        employeesTabLabel.textProperty().bind(I18N.createStringBinding("employeesTabLabel"));
        storageTabLabel.textProperty().bind(I18N.createStringBinding("storageTabLabel"));
        ordersTabLabel.textProperty().bind(I18N.createStringBinding("ordersTabLabel"));
        historyTabLabel.textProperty().bind(I18N.createStringBinding("historyTabLabel"));
        logOutButton.textProperty().bind(I18N.createStringBinding("logoutButtonLabel"));
        settingsButton.textProperty().bind(I18N.createStringBinding("settingsButtonLabel"));
        homeLabel.textProperty().bind(I18N.createStringBinding("homeLabel"));
        employeesLabel.textProperty().bind(I18N.createStringBinding("employeesLabel"));
        storageLabel.textProperty().bind(I18N.createStringBinding("storageLabel"));
        ordersLabel.textProperty().bind(I18N.createStringBinding("ordersLabel"));
        historyLabel.textProperty().bind(I18N.createStringBinding("historyLabel"));
        createProductButton.textProperty().bind(I18N.createStringBinding("createProductButtonLabel"));
        editProductButton.textProperty().bind(I18N.createStringBinding("editProductButtonLabel"));
        deleteProductButton.textProperty().bind(I18N.createStringBinding("deleteProductButtonLabel"));
        addProductButton.textProperty().bind(I18N.createStringBinding("addProductButtonLabel"));
        refreshButton.textProperty().bind(I18N.createStringBinding("refreshButtonLabel"));
        idColumn.textProperty().bind(I18N.createStringBinding("idColumnLabel"));
        nameColumn.textProperty().bind(I18N.createStringBinding("nameColumnLabel"));
        quantityColumn.textProperty().bind(I18N.createStringBinding("quantityColumnLabel"));
        priceColumn.textProperty().bind(I18N.createStringBinding("priceColumnLabel"));

        logOutButton.setOnAction(this::onLogOutButtonClick);
        System.out.println(UserSingleton.getInstance().getUser().getName());
        super.init();
    }

    private void disableAllTabs() {
        employeesTab.setDisable(true);
        storageTab.setDisable(true);
        ordersTab.setDisable(true);
        ordersHistoryTab.setDisable(true);
    }

    private void disableTabsByRole() {
        UsersRecord user = UserSingleton.getInstance().getUser();
        String position = user.getPossition();

        disableAllTabs();

        if(position.equals("owner")) {
            employeesTab.setDisable(false);
            storageTab.setDisable(false);
            ordersTab.setDisable(false);
            ordersHistoryTab.setDisable(false);
        } else if(position.equals("user")) {
            storageTab.setDisable(false);
            ordersTab.setDisable(false);
        } else if(position.equals("logistics")) {
            storageTab.setDisable(false);
            ordersTab.setDisable(false);
            ordersHistoryTab.setDisable(false);
        }
    }

    private void disableAllButtons() {
        createProductButton.setDisable(true);
        storageSearchField.setDisable(true);
        editProductButton.setDisable(true);
        deleteProductButton.setDisable(true);
        addProductButton.setDisable(true);
    }

    private void disableButtonsByRole() {
        UsersRecord user = UserSingleton.getInstance().getUser();
        String position = user.getPossition();

        disableAllButtons();

        if(position.equals("owner") || position.equals("user")) {
            createProductButton.setDisable(false);
            storageSearchField.setDisable(false);
            editProductButton.setDisable(false);
            deleteProductButton.setDisable(false);
            addProductButton.setDisable(false);
        } else if(position.equals("logistics")) {
            storageSearchField.setDisable(false);
        }
    }

    @FXML
    private void onLogOutButtonClick(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(ApplicationController.class.getResource("Login.fxml"));
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(I18N.get("logoutLabel"));
        alert.setHeaderText(null);
        alert.setContentText(I18N.get("logoutMessage"));
        alert.getButtonTypes().set(1, new ButtonType(I18N.get("cancelAlertLabel"), ButtonBar.ButtonData.CANCEL_CLOSE));
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            try {
                FunctionsController.changeScene(
                        FunctionsController.getStageFromEvent(actionEvent), loader, "GoInventory");
            } catch (IOException e) {
                log.Exceptions(I18N.get("failedToLoadLoginPage"), e);
            }
            log.userLogout();
            UserSingleton.getInstance().setUser(null);
        }
    }

    public void exitButtonClicked(MouseEvent mouseEvent) {
        FunctionsController.showExitAlert(I18N.get("exitMessage"), I18N.get("exitLabel"), I18N.get("cancelAlertLabel"));
    }

    public void settingsButtonClicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(ApplicationController.class.getResource("Settings.fxml"));
        try {
            FunctionsController.openWindow("Settings.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            log.Exceptions("Failed to load settings screen", e);
        }
    }

    public void searchStorage(KeyEvent event) {
        String searchQuery = storageSearchField.getText();
        if (event.getCode() == KeyCode.ENTER) {
            productObservableList.clear();
            DSLContext dslContext = DatabaseContextSingleton.getContext();
            Result<ProductsRecord> result = dslContext
                .selectFrom(Products.PRODUCTS)
                .where(Products.PRODUCTS.NAME.likeIgnoreCase("%" + searchQuery + "%"))
                .fetch();

            result.forEach(p -> {
                Product product = new Product.ProductBuilder()
                        .name(p.getName())
                        .quantity(p.getQuantity())
                        .sellingPrice(p.getSellingprice())
                        .build();
                productObservableList.add(product);
            });
            System.out.println(productObservableList);
        }
    }


}
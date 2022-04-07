package com.example.vavagoinventory.Employee;

import com.example.vavagoinventory.ApplicationController;
import com.example.vavagoinventory.Employee.Orders.Order;
import com.example.vavagoinventory.Employee.Storage.AddProductController;
import com.example.vavagoinventory.FunctionsController;
import com.example.vavagoinventory.Log;
import com.example.vavagoinventory.Login.FadingIntroController;
import com.example.vavagoinventory.Employee.Storage.Product;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.vavagoinventory.FunctionsController.openWindow;

public class EmployeeMainPageController extends ApplicationController implements Initializable {
    @FXML
    private Button switchToHistoryButton;
    @FXML
    private Button addOrdersButton;
    @FXML
    private Pane ordersHistoryPane;
    @FXML
    private HBox orderHistoryTableHeader;
    @FXML
    private ListView ordersHistoryListView;
    @FXML
    private TextField searchOrderHistoryTextfield;
    @FXML
    private Button switchToOrdersButton;
    @FXML
    private Pane storagePane;
    @FXML
    private HBox storageTableHeader;
    @FXML
    private TextField searchStorageTextfield;
    @FXML
    private ListView storageListView;
    @FXML
    private Button addProductToStorage;
    @FXML
    private Button createProductButton;
    @FXML
    private TextField searchOrderTextfield;
    @FXML
    private ListView ordersListView;
    @FXML
    private Button logOutButton;
    @FXML
    private Pane ordersPane;
    @FXML
    private HBox orderTableHeader;

    public Log log = new Log();

    public static ObservableList<Product> productObservableList;
    public static ObservableList<Order> orderObservableList;

    private FunctionsController functionsController = new FunctionsController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logOutButton.setOnAction(this::onLogOutButtonClick);
    }

    @FXML
    private void onClickAddOrder(ActionEvent actionEvent) throws Exception {
    // add order
    }

    @FXML
    private void onClickAddProductToStorage(Event event) throws Exception {
        openWindow(AddProductController.class.getResource("AddProduct.fxml"));
        productsObservableList.sort(Comparator.comparing(Product::getQuantity));
    }

    private void onLogOutButtonClick(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource("Login.fxml"));
        try {
            FunctionsController.changeScene(
                    FunctionsController.getStageFromEvent(actionEvent), loader, "GoInventory");
        } catch (IOException e) {
            log.Exceptions("Failed to load login screen",e);
        }
        log.userLogout("ferino");
    }
}

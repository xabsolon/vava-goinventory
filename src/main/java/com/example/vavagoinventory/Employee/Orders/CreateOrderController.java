package com.example.vavagoinventory.Employee.Orders;

import com.example.vavagoinventory.FunctionsController;
import com.example.vavagoinventory.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateOrderController implements Initializable { //work in progress

    private OrdersController ordersController;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private Button cancelCreateButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void injectOrdersController(OrdersController ordersController) {
        this.ordersController = ordersController;
    }

    @FXML
    public void onClickCancel() {
        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onClickCreate() {
        Order order;
        try {
            order = new Order.OrderBuilder()
                    .o_id(0)
                    .p_id(Integer.parseInt(productNameField.getText()))
                    .quantity(Integer.parseInt(quantityField.getText()))
                    .build();
        } catch (NumberFormatException e) {
            FunctionsController.showErrorAlert(I18N.get("OrderCreationError"));
            return;
        }
        OrdersController.OrderQuery.insertQuery(order);
        System.out.println(ordersController);
        //ordersController.addOrder(order);
        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }
}
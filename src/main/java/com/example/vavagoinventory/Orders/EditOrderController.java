package com.example.vavagoinventory.Orders;

import com.example.vavagoinventory.Utils.FunctionsController;
import com.example.vavagoinventory.Utils.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.exception.DataAccessException;

import java.net.URL;
import java.util.ResourceBundle;

public class EditOrderController implements Initializable {

    private OrdersController ordersController;

    private Order selectedOrder;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField productNameField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Label editOrderLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label nameLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancelButton.textProperty().bind(I18N.createStringBinding("cancelButtonLabel"));
        confirmButton.textProperty().bind(I18N.createStringBinding("confirmButtonLabel"));
        editOrderLabel.textProperty().bind(I18N.createStringBinding("editOrderLabel"));
        quantityLabel.textProperty().bind(I18N.createStringBinding("quantityLabel"));
        nameLabel.textProperty().bind(I18N.createStringBinding("nameLabel"));
    }

    public void injectOrdersController(OrdersController ordersController) {
        this.ordersController = ordersController;
        selectedOrder = ordersController.getLastSelectedOrder();
        quantityField.setText(String.valueOf(selectedOrder.getQuantity()));
        productNameField.setText(selectedOrder.getProductName());
    }

    @FXML
    private void onClickCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onClickConfirm() {
        Order order;
        try {
            order = new Order.OrderBuilder()
                    .productName(productNameField.getText())
                    .quantity(Integer.parseInt(quantityField.getText()))
                    .o_id(selectedOrder.getO_id())
                    .build();
        } catch (NumberFormatException e) {
            FunctionsController.showErrorAlert(I18N.get("OrderCreationError"));
            return;
        }
        try {
            OrdersController.OrderQuery.editQuery(order);
        }
        catch (DataAccessException e) {
            FunctionsController.showErrorAlert(I18N.get("OrderCreationErrorSQL"));
            return;
        }
        FunctionsController.log.OrderEdited(order.getProductName(), order.getQuantity());
        selectedOrder.setProductName(order.getProductName());
        selectedOrder.setP_id(order.getP_id());
        selectedOrder.setQuantity(order.getQuantity());
        ordersController.updateTable();
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

}

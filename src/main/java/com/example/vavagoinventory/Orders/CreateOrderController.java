package com.example.vavagoinventory.Orders;

import com.example.vavagoinventory.DatabaseContextSingleton;
import com.example.vavagoinventory.FunctionsController;
import com.example.vavagoinventory.I18N;
import com.example.vavagoinventory.UserSingleton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.jooq.codegen.maven.goinventory.Tables.ORDERS;
import static org.jooq.codegen.maven.goinventory.Tables.USERS;

public class CreateOrderController implements Initializable {

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
                    .productName(productNameField.getText())
                    .quantity(Integer.parseInt(quantityField.getText()))
                    .build();
        } catch (NumberFormatException e) {
            FunctionsController.showErrorAlert(I18N.get("OrderCreationError"));
            return;
        }
        try {
            OrdersController.OrderQuery.insertQuery(order);
            FunctionsController.log.OrderCreated(order.getProductName(),order.getQuantity());
        }
        catch (DataAccessException e) {
            FunctionsController.showErrorAlert(I18N.get("OrderCreationErrorSQL"));
            return;
        }
        ordersController.addOrder(order);
        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }

}

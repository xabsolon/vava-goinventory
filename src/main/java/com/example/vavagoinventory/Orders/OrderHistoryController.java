package com.example.vavagoinventory.Orders;

import com.example.vavagoinventory.DatabaseContextSingleton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Orders;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.jooq.codegen.maven.goinventory.Tables.ORDERS;
import static org.jooq.codegen.maven.goinventory.Tables.PRODUCTS;

public class OrderHistoryController implements Initializable {

    private ObservableList<Order> orders;

    @FXML
    private TableView<Order> tableView;

    @FXML
    private TableColumn<Order, Integer> colID;

    @FXML
    private TableColumn<Order, String> colProduct;

    @FXML
    private TableColumn<Order, Integer> colQuantity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        query();
        colID.setCellValueFactory(orders -> new SimpleIntegerProperty(orders.getValue().getO_id()).asObject());
        colProduct.setCellValueFactory(orders -> new SimpleStringProperty(orders.getValue().getProductName()));
        colQuantity.setCellValueFactory(orders -> new SimpleIntegerProperty(orders.getValue().getQuantity()).asObject());
        tableView.setItems(orders);
    }

    private void query() {
        ArrayList<Order> orders = new ArrayList<Order>();
        DSLContext create = DatabaseContextSingleton.getContext();
        Result<Record4<String, Integer, Integer, Integer>> result = create.select(
                ORDERS.products().NAME,
                ORDERS.O_ID,
                ORDERS.P_ID,
                ORDERS.QUANTITY).from(Orders.ORDERS).fetch();
        for (Record r:result) {
            Order order = new Order.OrderBuilder()
                    .o_id(r.get(ORDERS.O_ID))
                    .p_id(r.get(ORDERS.P_ID))
                    .productName(r.get(PRODUCTS.NAME))
                    .quantity(r.get(ORDERS.QUANTITY))
                    .build();
            orders.add(order);
        }
        this.orders = FXCollections.observableArrayList(orders);
    }

    @FXML
    private void onClickRefresh() {
        OrdersController.OrderQuery.getQuery();
        orders = FXCollections.observableArrayList(OrdersController.OrderQuery.orders);
        tableView.setItems(orders);
        tableView.refresh();
    }
}

package com.example.vavagoinventory.Orders;

import com.example.vavagoinventory.DatabaseContextSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Orders;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.jooq.codegen.maven.goinventory.Tables.ORDERS;

public class OrdersController implements Initializable {

    ObservableList<Order> orders;

    @FXML
    private Button closeButton;

    @FXML
    private TableView<Order> tableView;

    @FXML
    private TableColumn<Order, Integer> colID;

    @FXML
    private TableColumn<Order, Integer> colProduct;

    @FXML
    private TableColumn<Order, Integer> colQuantity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OrderQuery.getQuery();
        orders = FXCollections.observableArrayList(OrderQuery.orders);
        colID.setCellValueFactory(new PropertyValueFactory<Order, Integer>("o_id"));
        colProduct.setCellValueFactory(new PropertyValueFactory<Order, Integer>("p_id"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantity"));
        tableView.setItems(orders);
    }

    private static class OrderQuery {
        public static ArrayList<Order> orders = new ArrayList<>();

        public static void getQuery() {
            orders.clear();
            DSLContext create = DatabaseContextSingleton.getContext();
            Result<Record> result = create.select().from(Orders.ORDERS).fetch();
            for (Record r:result) {
                Order order = new Order.OrderBuilder()
                        .o_id(r.get(ORDERS.O_ID))
                        .p_id(r.get(ORDERS.P_ID))
                        .quantity(r.get(ORDERS.QUANTITY))
                        .build();
                orders.add(order);
            }
            System.out.println(orders);
        }

        public static void deleteQuery(int o_id) {
            Integer id = o_id;
            DSLContext create = DatabaseContextSingleton.getContext();
            create.delete(ORDERS).where(ORDERS.O_ID.eq(id)).execute();
        }
    }

    @FXML
    private void onClickClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onClickDelete() {
        Order order = tableView.getSelectionModel().getSelectedItem();
        orders.remove(order);
        OrderQuery.deleteQuery(order.getO_id());
    }


}

package com.example.vavagoinventory.Orders;

import com.example.vavagoinventory.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jooq.*;
import org.jooq.codegen.maven.goinventory.tables.Orders;
import org.jooq.conf.ParamType;
import org.jooq.exception.DataAccessException;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

//import static com.sun.javafx.scene.control.skin.Utils.getResource;
import static org.jooq.codegen.maven.goinventory.Tables.ORDERS;
import static org.jooq.codegen.maven.goinventory.Tables.PRODUCTS;
import static org.jooq.impl.DSL.*;

public class OrdersController implements Initializable {

    private ObservableList<Order> orders;

    private Order lastSelectedOrder;

    @FXML
    private CreateOrderController createOrderController;

    @FXML
    private EditOrderController editOrderController;

    @FXML
    private Button closeButton;

    @FXML
    private TableView<Order> tableView;

    @FXML
    private TableColumn<Order, Integer> colID;

    @FXML
    private TableColumn<Order, String> colProduct;

    @FXML
    private TableColumn<Order, Integer> colQuantity;

    public static class OrderQuery {
        public static ArrayList<Order> orders = new ArrayList<>();

        public static void getQuery() {
            orders.clear();
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
            System.out.println(orders);
        }

        public static void insertQuery(Order order) {
            String name = order.getProductName();
            Integer quantity = order.getQuantity();
            DSLContext create = DatabaseContextSingleton.getContext();
            Record record = create.insertInto(ORDERS)
                    .set(ORDERS.P_ID, select(PRODUCTS.P_ID).from(PRODUCTS).where(PRODUCTS.NAME.eq(inline(name))))
                    .set(ORDERS.QUANTITY, inline(quantity))
                    .returningResult(ORDERS.O_ID, ORDERS.P_ID)
                    .fetchOne();
            order.setO_id(record.get(ORDERS.O_ID));
            order.setP_id(record.get(ORDERS.P_ID));
        }

        public static void deleteQuery(int o_id) {
            Integer id = (Integer) o_id;
            System.out.println(id);
            DSLContext create = DatabaseContextSingleton.getContext();
            create.delete(ORDERS).where(ORDERS.O_ID.eq(id)).execute();
            //does not work for some reason, id in query gets replaced by a question mark
        }

        public static void editQuery(Order order) {
            String name = order.getProductName();
            Integer quantity = order.getQuantity();
            Integer id = order.getO_id();
            DSLContext create = DatabaseContextSingleton.getContext();
            create.update(ORDERS)
                    .set(ORDERS.P_ID, select(PRODUCTS.P_ID).from(PRODUCTS).where(PRODUCTS.NAME.eq(inline(name))))
                    .set(ORDERS.QUANTITY, inline(quantity))
                    .where(ORDERS.O_ID.eq(inline(id)))
                    .execute();
            Record record = create.select(ORDERS.P_ID).from(ORDERS).where(ORDERS.O_ID.eq(inline(id))).fetchOne();
            order.setP_id(record.get(ORDERS.P_ID));
        }

        public static void completeQuery(Order order) {
            Integer quantity = order.getQuantity();
            Integer p_id = order.getP_id();
            DSLContext create = DatabaseContextSingleton.getContext();
            int result = create.update(PRODUCTS)
                    .set(PRODUCTS.QUANTITY, PRODUCTS.QUANTITY.sub(quantity))
                    .where(PRODUCTS.QUANTITY.ge(quantity), PRODUCTS.P_ID.eq(p_id))
                    .execute();
            if (result != 0) {
                order.setP_id(-1);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OrderQuery.getQuery();
        orders = FXCollections.observableArrayList(OrderQuery.orders);
        colID.setCellValueFactory(orders -> new SimpleIntegerProperty(orders.getValue().getO_id()).asObject());
        colProduct.setCellValueFactory(orders -> new SimpleStringProperty(orders.getValue().getProductName()));
        colQuantity.setCellValueFactory(orders -> new SimpleIntegerProperty(orders.getValue().getQuantity()).asObject());
        tableView.setItems(orders);
        System.out.println(orders);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public Order getLastSelectedOrder() {
        return lastSelectedOrder;
    }

    public void updateTable() {
        tableView.refresh();
    }

    private void saveCurrentlySelectedOrder() {
        lastSelectedOrder = tableView.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void onClickCreate() throws IOException {
        Stage newstage = new Stage();
        FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource("CreateOrder.fxml"));
        Parent root = loader.load();
        createOrderController = loader.getController();
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        createOrderController.injectOrdersController(this);
        newstage.showAndWait();
    }

    @FXML
    private void onClickDelete() {
        saveCurrentlySelectedOrder();
        if (lastSelectedOrder == null) {
            FunctionsController.showErrorAlert(I18N.get("NoOrderSelected"));
            return;
        }
        orders.remove(lastSelectedOrder);
        OrderQuery.deleteQuery(lastSelectedOrder.getO_id());
    }

    @FXML
    private void onClickComplete() {
        saveCurrentlySelectedOrder();
        if (lastSelectedOrder == null) {
            FunctionsController.showErrorAlert(I18N.get("NoOrderSelected"));
            return;
        }
        OrderQuery.completeQuery(lastSelectedOrder);
        if (lastSelectedOrder.getP_id() == -1) {
            orders.remove(lastSelectedOrder);
            OrderQuery.deleteQuery(lastSelectedOrder.getO_id());
            FunctionsController.showConfirmationAlert(I18N.get("OrderCompleted"));
        }
        else {
            FunctionsController.showErrorAlert(I18N.get("NotEnoughProducts"));
        }
    }

    @FXML
    private void onClickEdit() throws IOException{
        saveCurrentlySelectedOrder();
        if (lastSelectedOrder == null) {
            FunctionsController.showErrorAlert(I18N.get("NoOrderSelected"));
            return;
        }
        Stage newstage = new Stage();
        FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource("EditOrder.fxml"));
        Parent root = loader.load();
        editOrderController = loader.getController();
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        editOrderController.injectOrdersController(this);
        newstage.showAndWait();
    }

    @FXML
    private void onClickRefresh() {
        OrderQuery.getQuery();
        orders = FXCollections.observableArrayList(OrderQuery.orders);
        tableView.setItems(orders);
        updateTable();
    }
}

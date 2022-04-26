package com.example.vavagoinventory.Orders;

import com.example.vavagoinventory.DatabaseContextSingleton;
import com.example.vavagoinventory.FadingIntroController;
import com.example.vavagoinventory.FunctionsController;
import com.example.vavagoinventory.I18N;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jooq.*;
import org.jooq.codegen.maven.goinventory.tables.Orders;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.jooq.codegen.maven.goinventory.tables.Users;
import org.jooq.codegen.maven.goinventory.tables.records.OrdersRecord;
import org.jooq.codegen.maven.goinventory.tables.records.UsersRecord;
import org.jooq.exception.DataAccessException;

import static org.jooq.codegen.maven.goinventory.Tables.*;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.select;

public class OrdersController implements Initializable {

    private ObservableList<Order> orders;

    private Order lastSelectedOrder;

    @FXML
    private Button createButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button completeButton;

    @FXML
    private Button importButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button refreshButton;

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

        public static void insertQueryWithId(Order order) {
            DSLContext create = DatabaseContextSingleton.getContext();
            OrdersRecord orderRecord = create.newRecord(Orders.ORDERS);
            orderRecord.setOId(order.getO_id());
            orderRecord.setPId(order.getP_id());
            orderRecord.setQuantity(order.getQuantity());
            orderRecord.store();
        }

        public static void deleteQuery(int o_id) {
            Integer id = (Integer) o_id;
            DSLContext create = DatabaseContextSingleton.getContext();
            create.delete(ORDERS).where(ORDERS.O_ID.eq(id)).execute();
        }

        public static void addRecord(Order order) {
            String name = order.getProductName();
            Integer quantity = order.getQuantity();
            DSLContext create = DatabaseContextSingleton.getContext();
            create.insertInto(ORDERHISTORY)
                    .set(ORDERHISTORY.PRODUCT, name)
                    .set(ORDERHISTORY.QUANTITY, inline(quantity))
                    .execute();
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
        createButton.textProperty().bind(I18N.createStringBinding("createOrderButtonLabel"));
        editButton.textProperty().bind(I18N.createStringBinding("editOrderButtonLabel"));
        deleteButton.textProperty().bind(I18N.createStringBinding("deleteOrderButtonLabel"));
        completeButton.textProperty().bind(I18N.createStringBinding("completeOrderButtonLabel"));
        importButton.textProperty().bind(I18N.createStringBinding("importXMLButtonLabel"));
        exportButton.textProperty().bind(I18N.createStringBinding("exportXMLButtonLabel"));
        colID.textProperty().bind(I18N.createStringBinding("idColumnLabel"));
        colProduct.textProperty().bind(I18N.createStringBinding("nameColumnLabel"));
        colQuantity.textProperty().bind(I18N.createStringBinding("quantityColumnLabel"));
        refreshButton.textProperty().bind(I18N.createStringBinding("refreshButtonLabel"));
        OrderQuery.getQuery();
        orders = FXCollections.observableArrayList(OrderQuery.orders);
        colID.setCellValueFactory(orders -> new SimpleIntegerProperty(orders.getValue().getO_id()).asObject());
        colProduct.setCellValueFactory(orders -> new SimpleStringProperty(orders.getValue().getProductName()));
        colQuantity.setCellValueFactory(orders -> new SimpleIntegerProperty(orders.getValue().getQuantity()).asObject());
        tableView.setItems(orders);
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
        FunctionsController.log.OrderDeleted(lastSelectedOrder.getProductName(), lastSelectedOrder.getQuantity());
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
            OrderQuery.addRecord(lastSelectedOrder);
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

    @FXML
    private void onClickImportFromXml(ActionEvent actionEvent) {
        Stage stage = FunctionsController.getStageFromEvent(actionEvent);
        OrdersXmlSerializer.importFromXmlFile(stage);
        onClickRefresh();
    }

    @FXML
    private void onClickExportToXml(ActionEvent actionEvent) {
        Stage stage = FunctionsController.getStageFromEvent(actionEvent);
        OrdersXmlSerializer.exportToXmlFile(orders, stage);
    }
}

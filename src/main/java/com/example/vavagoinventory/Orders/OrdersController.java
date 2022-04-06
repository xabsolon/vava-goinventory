package com.example.vavagoinventory.Orders;

import com.example.vavagoinventory.DatabaseContextSingleton;
import com.example.vavagoinventory.EmployeeMainPageController;
import com.example.vavagoinventory.FadingIntroController;
import javafx.beans.property.SimpleIntegerProperty;
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
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Orders;
import org.jooq.conf.ParamType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//import static com.sun.javafx.scene.control.skin.Utils.getResource;
import static org.jooq.codegen.maven.goinventory.Tables.ORDERS;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.val;

public class OrdersController implements Initializable {

    private ObservableList<Order> orders;

    @FXML
    private CreateOrderController createOrderController;

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

    public static class OrderQuery {
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

        public static void insertQuery(Order order) {
            Integer p_id = order.getP_id();
            Integer quantity = order.getQuantity();
            System.out.println(p_id + " " + quantity);
            DSLContext create = DatabaseContextSingleton.getContext();
            Record record = create.insertInto(ORDERS)
                    .set(ORDERS.P_ID, inline(p_id))
                    .set(ORDERS.QUANTITY, inline(quantity))
                    .returningResult(ORDERS.O_ID)
                    .fetchOne();
            order.setO_id(record.get(ORDERS.O_ID));
        }

        public static void deleteQuery(int o_id) {
            Integer id = (Integer) o_id;
            System.out.println(id);
            DSLContext create = DatabaseContextSingleton.getContext();
            create.delete(ORDERS).where(ORDERS.O_ID.eq(id)).execute();
            //does not work for some reason, id in query gets replaced by a question mark
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OrderQuery.getQuery();
        orders = FXCollections.observableArrayList(OrderQuery.orders);
        colID.setCellValueFactory(orders -> new SimpleIntegerProperty(orders.getValue().getO_id()).asObject());
        colProduct.setCellValueFactory(orders -> new SimpleIntegerProperty(orders.getValue().getP_id()).asObject());
        colQuantity.setCellValueFactory(orders -> new SimpleIntegerProperty(orders.getValue().getQuantity()).asObject());
        tableView.setItems(orders);
        System.out.println(orders);
    }

    public void addOrder(Order order) {
        orders.add(order);
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

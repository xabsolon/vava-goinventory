package com.example.vavagoinventory.Employee.Orders;

import com.example.vavagoinventory.DBconnector.DatabaseContextSingleton;
import com.example.vavagoinventory.Login.FadingIntroController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Orders;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//import static com.sun.javafx.scene.control.skin.Utils.getResource;
import static org.jooq.codegen.maven.goinventory.Tables.ORDERS;

public class OrdersController implements Initializable {

    @FXML
    private CreateOrderController createOrderController;

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
            DSLContext create = DatabaseContextSingleton.getContext();
            Record record = create.insertInto(ORDERS, ORDERS.P_ID, ORDERS.QUANTITY)
                    .values(p_id, quantity)
                    .returningResult(ORDERS.O_ID)
                    .fetchOne();
            order.setO_id(record.get(ORDERS.O_ID));
        }

        public static void updateQuery(Order order) {
            Integer o_id = order.getO_id();
            Integer p_id = order.getP_id();
            Integer quantity = order.getQuantity();
            DSLContext create = DatabaseContextSingleton.getContext();
            create.update(ORDERS)
                    .set(ORDERS.P_ID, p_id)
                    .set(ORDERS.QUANTITY, quantity)
                    .where(ORDERS.O_ID.eq(o_id))
                    .execute();
        }

        public static void deleteQuery(Order order) {
            Integer o_id = order.getO_id();
            DSLContext create = DatabaseContextSingleton.getContext();
            create.delete(ORDERS)
                    .where(ORDERS.O_ID.eq(o_id))
                    .execute();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    /*
    public void addOrder(Order order) {
        orders.add(order);
    }
    */
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
    /*
    @FXML
    private void onClickDelete() {
        Order order = tableView.getSelectionModel().getSelectedItem();
        //orders.remove(order);
        OrderQuery.deleteQuery(order);
    }
    */
}

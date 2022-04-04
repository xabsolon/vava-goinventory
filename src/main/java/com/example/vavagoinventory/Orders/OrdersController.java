package com.example.vavagoinventory.Orders;

import com.example.vavagoinventory.DatabaseContextSingleton;
import com.example.vavagoinventory.EmployeeMainPageController;
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
import org.jooq.impl.QOM;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//import static com.sun.javafx.scene.control.skin.Utils.getResource;
import static org.jooq.codegen.maven.goinventory.Tables.ORDERS;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OrderQuery.getQuery();
        orders = FXCollections.observableArrayList(OrderQuery.orders);
        colID.setCellValueFactory(new PropertyValueFactory<Order, Integer>("o_id"));
        colProduct.setCellValueFactory(new PropertyValueFactory<Order, Integer>("p_id"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantity"));
        tableView.setItems(orders);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    @FXML
    private void onClickCreate() throws IOException {
        Stage newstage = new Stage();
        Parent root = FXMLLoader.load(OrdersController.class.getResource("Orders.fxml"));
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        newstage.showAndWait();
        createOrderController.injectOrdersController(this);
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

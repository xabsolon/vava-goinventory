package com.example.vavagoinventory.UserManagement;

import com.example.vavagoinventory.DatabaseContextSingleton;
import com.example.vavagoinventory.Orders.Order;
import com.example.vavagoinventory.Orders.OrdersController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Orders;
import org.jooq.codegen.maven.goinventory.tables.Users;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.jooq.codegen.maven.goinventory.Tables.*;

public class UserManagementController implements Initializable {

    private ObservableList<User> users;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> colLogin;

    @FXML
    private TableColumn<User, String> colPassword;

    @FXML
    private TableColumn<User, String> colPosition;

    public static class UserQuery {
        public static ArrayList<User> users = new ArrayList<>();

        public static void getQuery() {
            users.clear();
            DSLContext create = DatabaseContextSingleton.getContext();
            Result<Record4<String, Integer, String, String>> result = create.select(
                    USERS.EMAIL,
                    USERS.U_ID,
                    USERS.PASSWORD,
                    USERS.POSSITION).from(Users.USERS).fetch();
            for (Record r:result) {
                User user = new User.UserBuilder()
                        .id(r.get(USERS.U_ID))
                        .username(r.get(USERS.EMAIL))
                        .password(r.get(USERS.PASSWORD))
                        .position(r.get(USERS.POSSITION))
                        .build();
                users.add(user);
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserQuery.getQuery();
        users = FXCollections.observableArrayList(UserQuery.users);
        colLogin.setCellValueFactory(users -> new SimpleStringProperty(users.getValue().getUsername()));
        colPassword.setCellValueFactory(users -> new SimpleStringProperty(users.getValue().getPassword()));
        colPosition.setCellValueFactory(users -> new SimpleStringProperty(users.getValue().getPosition()));
        tableView.setItems(users);
    }

    @FXML
    private void onClickCreate() {

    }

    @FXML
    private void onClickEdit() {

    }

    @FXML
    private void onClickDelete() {

    }

    @FXML
    private void onClickRefresh() {

    }
}

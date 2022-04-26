package com.example.vavagoinventory.UserManagement;

import com.example.vavagoinventory.DatabaseContextSingleton;
import com.example.vavagoinventory.FadingIntroController;
import com.example.vavagoinventory.FunctionsController;
import com.example.vavagoinventory.I18N;
import com.example.vavagoinventory.Orders.Order;
import com.example.vavagoinventory.Orders.OrdersController;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Orders;
import org.jooq.codegen.maven.goinventory.tables.Users;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.jooq.codegen.maven.goinventory.Tables.*;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.select;

public class UserManagementController implements Initializable {

    private ObservableList<User> users;

    private User lastSelected;

    @FXML
    private TableView<User> tableView;

    @FXML
    private Button createUserButton;

    @FXML
    private Button editUserButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button refreshButton;

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

        public static void insertQuery(User user) {
            String name = user.getUsername();
            String password = user.getPassword();
            String position = user.getPosition();
            DSLContext create = DatabaseContextSingleton.getContext();
            Record record = create.insertInto(USERS)
                    .set(USERS.EMAIL, name)
                    .set(USERS.PASSWORD, password)
                    .set(USERS.POSSITION, position)
                    .set(USERS.SURNAME, name)
                    .set(USERS.NAME, name)
                    .returningResult(USERS.U_ID)
                    .fetchOne();
            user.setId(record.get(USERS.U_ID));
        }

        public static void editQuery(User user) {
            String name = user.getUsername();
            String password = user.getPassword();
            String position = user.getPosition();
            Integer id = user.getId();
            System.out.println(id);
            DSLContext create = DatabaseContextSingleton.getContext();
            create.update(USERS)
                    .set(USERS.EMAIL, name)
                    .set(USERS.PASSWORD, password)
                    .set(USERS.POSSITION, position)
                    .set(USERS.SURNAME, name)
                    .set(USERS.NAME, name)
                    .where(USERS.U_ID.eq(id))
                    .execute();
        }

        public static void deleteQuery(User user) {
            Integer id = user.getId();
            DSLContext create = DatabaseContextSingleton.getContext();
            create.delete(USERS).where(USERS.U_ID.eq(id)).execute();
        }

    }

    public void addUser(User user) {
        users.add(user);
    }

    public User getLastSelected() {
        return lastSelected;
    }

    public void updateTable() {
        tableView.refresh();
    }

    private void saveLastSelectedUser() {
        lastSelected = tableView.getSelectionModel().getSelectedItem();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshButton.textProperty().bind(I18N.createStringBinding("refreshButtonLabel"));
        createUserButton.textProperty().bind(I18N.createStringBinding("createUserButtonLabel"));
        editUserButton.textProperty().bind(I18N.createStringBinding("editUserButtonLabel"));
        deleteUserButton.textProperty().bind(I18N.createStringBinding("deleteUserButtonLabel"));
        colLogin.textProperty().bind(I18N.createStringBinding("colLoginLabel"));
        colPassword.textProperty().bind(I18N.createStringBinding("colPasswordLabel"));
        colPosition.textProperty().bind(I18N.createStringBinding("colPositionLabel"));
        UserQuery.getQuery();
        users = FXCollections.observableArrayList(UserQuery.users);
        colLogin.setCellValueFactory(users -> new SimpleStringProperty(users.getValue().getUsername()));
        colPassword.setCellValueFactory(users -> new SimpleStringProperty(users.getValue().getPassword()));
        colPosition.setCellValueFactory(users -> new SimpleStringProperty(users.getValue().getPosition()));
        tableView.setItems(users);
    }

    @FXML
    private void onClickCreate() throws IOException {
        Stage newstage = new Stage();
        FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource("CreateUser.fxml"));
        Parent root = loader.load();
        CreateUserController createUserController = loader.getController();
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        createUserController.injectUserManagementController(this);
        newstage.showAndWait();
    }

    @FXML
    private void onClickEdit() throws IOException {
        saveLastSelectedUser();
        if (lastSelected == null) {
            FunctionsController.showErrorAlert("No user selected");
            return;
        }
        Stage newstage = new Stage();
        FXMLLoader loader = new FXMLLoader(FadingIntroController.class.getResource("EditUser.fxml"));
        Parent root = loader.load();
        EditUserController createUserController = loader.getController();
        Scene scene = new Scene(root);
        newstage.setScene(scene);
        newstage.setResizable(false);
        newstage.initStyle(StageStyle.TRANSPARENT);
        newstage.initModality(Modality.APPLICATION_MODAL);
        createUserController.injectUserManagementController(this);
        newstage.showAndWait();
    }

    @FXML
    private void onClickDelete() {
        saveLastSelectedUser();
        if (lastSelected == null) {
            FunctionsController.showErrorAlert("No user selected");
            return;
        }
        UserQuery.deleteQuery(lastSelected);
        users.remove(lastSelected);
    }

    @FXML
    private void onClickRefresh() {
        UserQuery.getQuery();
        users = FXCollections.observableArrayList(UserQuery.users);
        tableView.setItems(users);
        updateTable();
    }
}

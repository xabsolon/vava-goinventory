package com.example.vavagoinventory.Orders;

import com.example.vavagoinventory.Utils.FunctionsController;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jooq.exception.DataAccessException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OrdersXmlSerializer {

    public static void exportToXmlFile(List<Order> orders, Stage stage) {
        XmlMapper mapper = new XmlMapper();
        OrdersWrapper ordersWrapper = new OrdersWrapper(orders);
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
            File file = fileChooser.showSaveDialog(stage);
            mapper.writeValue(file, ordersWrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void importFromXmlFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(stage);
        XmlMapper mapper = new XmlMapper();
        mapper.registerModule(new GuavaModule());

        OrdersWrapper ordersWrapper;
        try {
            ordersWrapper = mapper.readValue(file, OrdersWrapper.class);
        } catch (IOException e) {
            FunctionsController.showErrorAlert("Failed to parse XML.");
            e.printStackTrace();
            return;
        }

        DataAccessException exception = null;
        for(Order order : ordersWrapper.getOrders()) {
            try {
                OrdersController.OrderQuery.insertQueryWithId(order);
            } catch (DataAccessException e) {
                exception = e;
            }
        }

        if(exception != null) {
            FunctionsController.showErrorAlert("Orders with existing ID's were skipped.");
            exception.printStackTrace();
        }
    }

}

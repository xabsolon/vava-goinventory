module com.example.vavagoinventory {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires org.jooq;
    requires mail;
    requires jdk.scripting.nashorn;

    opens com.example.vavagoinventory to javafx.fxml;
    opens com.example.vavagoinventory.Employee.Storage to javafx.fxml;
    exports com.example.vavagoinventory;
    exports com.example.vavagoinventory.Employee.Storage;
    exports org.jooq.codegen.maven.goinventory.tables.records;
    opens com.example.vavagoinventory.Employee.Orders to javafx.fxml;
    exports com.example.vavagoinventory.Owner;
    opens com.example.vavagoinventory.Owner to javafx.fxml;
    exports com.example.vavagoinventory.Login;
    opens com.example.vavagoinventory.Login to javafx.fxml;
    exports com.example.vavagoinventory.DBconnector;
    opens com.example.vavagoinventory.DBconnector to javafx.fxml;
    exports com.example.vavagoinventory.Employee;
    opens com.example.vavagoinventory.Employee to javafx.fxml;
}
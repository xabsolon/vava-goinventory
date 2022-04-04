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
    requires java.mail;
    requires jdk.scripting.nashorn;

    opens com.example.vavagoinventory to javafx.fxml;
    opens com.example.vavagoinventory.Storage to javafx.fxml;
    exports com.example.vavagoinventory;
    exports com.example.vavagoinventory.Storage;
    exports org.jooq.codegen.maven.goinventory.tables.records;
}
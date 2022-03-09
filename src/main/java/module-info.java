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

    opens com.example.vavagoinventory to javafx.fxml;
    exports com.example.vavagoinventory;
    exports org.jooq.codegen.maven.goinventory.tables.records;
}
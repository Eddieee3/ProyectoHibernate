module com.desarrollo.proyectohibernate {
    requires javafx.controls;
    requires javafx.fxml;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;

    opens com.desarrollo.proyectohibernate to javafx.fxml;
    opens com.desarrollo.proyectohibernate.controller to javafx.fxml;
    // Hibernate necesita acceso por reflexión a las entidades
    opens com.desarrollo.proyectohibernate.model to org.hibernate.orm.core, javafx.base;

    exports com.desarrollo.proyectohibernate;
    exports com.desarrollo.proyectohibernate.controller;
    exports com.desarrollo.proyectohibernate.model;
    exports com.desarrollo.proyectohibernate.dao;
    exports com.desarrollo.proyectohibernate.database;
}

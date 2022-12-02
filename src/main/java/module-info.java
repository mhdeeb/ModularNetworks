module com.mat.modularservers {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.controlsfx.controls;
    requires org.jetbrains.annotations;
    requires javafx.web;
    requires org.jsoup;
    requires javafx.graphics;
    requires jdk.jsobject;
    requires java.desktop;


    opens com.mat.modularservers.controller to javafx.fxml;
    opens com.mat.modularservers.fxml;
    opens com.mat.modularservers.html;
    opens com.mat.modularservers.images;
    opens com.mat.modularservers.sounds;
    opens com.mat.modularservers.style;
    exports com.mat.modularservers.client;
    exports com.mat.modularservers.controller;
    exports com.mat.modularservers.gui;
    exports com.mat.modularservers.main;
    exports com.mat.modularservers.module;
    exports com.mat.modularservers.server;
    exports com.mat.modularservers.test;
    exports com.mat.modularservers.util;

}
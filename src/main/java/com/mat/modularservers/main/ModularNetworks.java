package com.mat.modularservers.main;


import com.mat.modularservers.util.ResourcePath;
import com.mat.modularservers.util.TimeUtil;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Objects;

public class ModularNetworks extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainStage.init(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
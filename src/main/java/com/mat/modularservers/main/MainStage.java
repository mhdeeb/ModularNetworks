package com.mat.modularservers.main;

import com.mat.modularservers.util.ResourcePath;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainStage {
    public static void init(Stage mainStage) throws IOException {
        FXMLLoader mainView = new FXMLLoader(MainStage.class.getResource(ResourcePath.MAIN_VIEW));
        Scene mainScene = new Scene(mainView.load(), 505, 500);
        mainStage.setTitle("Modular Networks");
        mainStage.getIcons().add(new Image(Objects.requireNonNull(MainStage.class.getResourceAsStream(ResourcePath.DATA_SERVER_PNG))));
        mainStage.setScene(mainScene);
        mainStage.setOnCloseRequest(windowEvent -> System.exit(0));
        mainStage.setResizable(false);
        mainStage.show();
    }
}

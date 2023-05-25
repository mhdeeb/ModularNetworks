package com.mat.modularservers.main;


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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/mat/modularservers/fxml/chat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 505, 500);
        stage.setTitle("Modular Networks");
        stage.getIcons().add(new Image(Objects.requireNonNull(ModularNetworks.class.getResourceAsStream("/com/mat/modularservers/images/data-server.png"))));
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
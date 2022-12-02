package com.mat.modularservers.gui;

import com.mat.modularservers.util.ExceptionUtil;
import com.mat.modularservers.util.JavaFxUtil;
import com.mat.modularservers.util.LinkedFixedList;
import com.mat.modularservers.util.MessageFlag;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Prompt {
    public FileOutputStream outputStream;
    private String name;
    private long progress, bytesPerSecond;
    private DownloadPopup downloadPop;
    private CompletePopup completePop;
    private final LinkedFixedList<Long> speedList = new LinkedFixedList<>(60);

    public Prompt() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            speedList.fixedInsert(bytesPerSecond);
            bytesPerSecond = 0;
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void write(byte[] bytes) throws IOException {
        outputStream.write(bytes);
        updateProgress(bytes.length);
    }

    public String getName() {
        return name;
    }

    public void updateProgress(int progress) {
        long total = speedList.sum();
        float speed = (float) total / speedList.size();
        this.bytesPerSecond += progress;
        this.progress += progress;
        downloadPop.update(speed, this.progress);
    }

    public void complete() throws IOException {
        downloadPop.close();
        completePop.pop();
        ExceptionUtil.ignoreExceptions(outputStream::close);
    }

    public MessageFlag pop(String fileName, long dataSize, String source) throws IOException {
        String path = StartPopup.setDownloadLocation(fileName, String.valueOf(dataSize), source);
        if (path != null) {
            File file = new File(path);
            name = file.getName();
            outputStream = new FileOutputStream(file);
            downloadPop = new DownloadPopup(path, dataSize);
            downloadPop.pop();
            completePop = new CompletePopup(path, dataSize, source);
            return MessageFlag.RESPONSE_ACCEPT;
        } else {
            return MessageFlag.RESPONSE_REJECT;
        }
    }

    public class DownloadPopup {
        private final String path;
        private final long dataSize;
        private final Stage stage = new Stage();
        private AnchorPane anchorPane;

        public DownloadPopup(String path, long dataSize) {
            this.path = path;
            this.dataSize = dataSize;
        }

        public void pop() throws IOException {
            downloadPopup(path, String.valueOf(dataSize));
        }

        public void downloadPopup(String path, String fileSize) throws IOException {
            stage.setTitle("Downloading " + new File(path).getName() + " (0%)");
            stage.setResizable(false);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/mat/modularservers/fxml/download-view.fxml"));
            anchorPane = fxmlLoader.load();
            ((Label) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(0)).getChildren().get(1)).setText(path);
            ((Label) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(1)).getChildren().get(1)).setText(fileSize);
            ((Button) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(8)).getChildren().get(0)).setOnAction(e -> {

            });
            ((Button) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(8)).getChildren().get(2)).setOnAction(e -> {

                stage.close();
            });
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.show();
        }

        public void close() {
            stage.close();
        }

        public void update(float speed, long progress) {
            stage.setTitle("Downloading " + new File(path).getName() + " (" + progress * 100 / dataSize + "%)");
            ((Label) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(2)).getChildren().get(1)).setText(String.valueOf(progress));
            ((Label) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(3)).getChildren().get(1)).setText(String.valueOf(speed));
            ((Label) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(4)).getChildren().get(1)).setText(String.valueOf((dataSize - progress) / speed));
            ((ProgressBar) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(6)).setProgress((double) progress / dataSize);
        }
    }

    public class StartPopup {

        public static String setDownloadLocation(String filename, String fileSize, String source) {
            try {
                AtomicReference<File> file = new AtomicReference<>();
                AtomicBoolean atomicBoolean = new AtomicBoolean(true);
                Stage stage = new Stage();
                stage.setTitle("Select Download Location");
                stage.setResizable(false);
                FXMLLoader fxmlLoader = new FXMLLoader(StartPopup.class.getResource("/com/mat/modularservers/fxml/start-view.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                ((Label) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(0)).getChildren().get(1)).setText(filename);
                ((Label) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(1)).getChildren().get(1)).setText(fileSize);
                ((Label) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(2)).getChildren().get(1)).setText(source);
                ((TextField) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(4)).getChildren().get(1)).setText(System.getProperty("user.dir") + "\\" + filename);
                ((Button) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(6)).getChildren().get(2)).setOnAction(e -> stage.close());
                ((Button) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(4)).getChildren().get(3)).setOnAction(e -> {
                    file.set(JavaFxUtil.setDownloadLocation(((Label) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(0)).getChildren().get(1)).getText()));
                    if (file.get() != null)
                        ((TextField) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(4)).getChildren().get(1)).setText(file.get().getAbsolutePath());
                });
                ((Button) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(6)).getChildren().get(0)).setOnAction(e -> {
                    stage.close();
                    atomicBoolean.set(false);
                });
                Scene scene = new Scene(anchorPane);
                stage.setScene(scene);
                stage.show();
                if (atomicBoolean.get())
                    return file.get().getAbsolutePath();
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public class CompletePopup {
        private final String path, fileName, source;
        private final long dataSize;

        public CompletePopup(String path, long dataSize, String source) {
            this.path = path;
            this.dataSize = dataSize;
            fileName = new File(path).getName();
            this.source = source;
        }

        public void pop() throws IOException {
            completePopup(path, fileName, String.valueOf(dataSize), source);
        }

        public void completePopup(String fileLocation, String fileName, String fileSize, String source) throws IOException {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/mat/modularservers/fxml/complete-view.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            ((Button) ((HBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(1)).getChildren().get(2)).setOnAction(e -> stage.close());
            ((Label) ((HBox) ((VBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).getChildren().get(1)).setText(fileLocation);
            ((Label) ((HBox) ((VBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(0)).getChildren().get(1)).getChildren().get(1)).setText(fileName);
            ((Label) ((HBox) ((VBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(0)).getChildren().get(2)).getChildren().get(1)).setText(fileSize);
            ((Label) ((HBox) ((VBox) ((VBox) anchorPane.getChildren().get(0)).getChildren().get(0)).getChildren().get(3)).getChildren().get(1)).setText(source);
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.setTitle("Download Complete");
            stage.setResizable(false);
            stage.show();
        }
    }
}

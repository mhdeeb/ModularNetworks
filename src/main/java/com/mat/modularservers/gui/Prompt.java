package com.mat.modularservers.gui;

import com.mat.modularservers.controller.CompleteController;
import com.mat.modularservers.controller.DownloadController;
import com.mat.modularservers.controller.SignInController;
import com.mat.modularservers.controller.StartController;
import com.mat.modularservers.util.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
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
    private final LinkedFixedList<Long> speedList = new LinkedFixedList<>(5);

    public Prompt() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            speedList.fixedInsert(bytesPerSecond);
            bytesPerSecond = 0;
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void write(byte[] bytes) throws IOException {
        outputStream.write(bytes);
        Platform.runLater(() -> updateProgress(bytes.length));
    }

    public String getName() {
        return name;
    }

    public void updateProgress(int progress) {
        this.bytesPerSecond += progress;
        this.progress += progress;
        downloadPop.update((float) speedList.sum() / speedList.size(), this.progress);
    }

    public void complete() {
        ExceptionUtil.ignoreExceptions(() -> {
            outputStream.close();
            Platform.runLater(() -> {
                downloadPop.close();
                ExceptionUtil.ignoreExceptions(() -> completePop.pop());
            });
        });
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

    public static class DownloadPopup {
        private final String path;
        private final long dataSize;
        private final Stage stage = new Stage();
        private DownloadController downloadController;

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
            FXMLLoader downloadView = new FXMLLoader(getClass().getResource(ResourcePath.DOWNLOAD_VIEW));
            AnchorPane anchorPane = downloadView.load();
            downloadController = downloadView.getController();
            downloadController.fileLocation.setText(path);
            downloadController.fileSize.setText(StringUtil.dataFormat(Double.parseDouble(fileSize)));
            downloadController.pause.setOnAction(e -> {
            });
            downloadController.cancel.setOnAction(e -> {
                stage.close();
            });
            stage.setScene(new Scene(anchorPane));
            stage.show();
        }

        public void close() {
            stage.close();
        }

        public void update(float speed, long progress) {
            stage.setTitle("Downloading " + new File(path).getName() + " (" + progress * 100 / dataSize + "%)");
            downloadController.downloaded.setText(StringUtil.dataFormat(progress));
            downloadController.transferRate.setText(StringUtil.dataFormat(speed) + "/sec");
            downloadController.timeLeft.setText(String.format("%d sec", (int) ((dataSize - progress) / speed)));
            downloadController.progressBar.setProgress((double) progress / dataSize);
        }
    }

    public static class StartPopup {

        public static String setDownloadLocation(String filename, String fileSize, String source) {
            try {
                AtomicReference<File> file = new AtomicReference<>();
                AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                Stage stage = new Stage();
                stage.setTitle("Select Download Location");
                stage.setResizable(false);
                FXMLLoader startView = new FXMLLoader(StartPopup.class.getResource(ResourcePath.START_VIEW));
                AnchorPane anchorPane = startView.load();
                StartController startController = startView.getController();
                startController.fileName.setText(filename);
                startController.fileSize.setText(StringUtil.dataFormat(Double.parseDouble(fileSize)));
                startController.source.setText(source);
                startController.fileName.setText(filename);
                startController.saveAs.setText(System.getProperty("user.dir") + "\\" + filename);
                file.set(Paths.get(startController.saveAs.getText()).toFile());
                startController.cancel.setOnAction(e -> stage.close());
                startController.browse.setOnAction(e -> {
                    file.set(JavaFxUtil.setDownloadLocation(startController.saveAs.getText()));
                    if (file.get() != null) startController.saveAs.setText(file.get().getAbsolutePath());
                });
                startController.startDownload.setOnAction(e -> {
                    atomicBoolean.set(true);
                    stage.close();
                });
                stage.setScene(new Scene(anchorPane));
                stage.showAndWait();
                if (atomicBoolean.get()) return file.get().getAbsolutePath();
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class CompletePopup {
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
            FXMLLoader completeView = new FXMLLoader(getClass().getResource(ResourcePath.COMPLETE_VIEW));
            AnchorPane anchorPane = completeView.load();
            CompleteController completeController = completeView.getController();
            completeController.openFile.setOnAction(e -> {
                ExceptionUtil.ignoreExceptions(() -> Desktop.getDesktop().open(new File(completeController.fileLocation.getText())));
                stage.close();
            });
            completeController.openLocation.setOnAction(e -> {
                ExceptionUtil.ignoreExceptions(() -> Desktop.getDesktop().open(new File(StringUtil.subString(completeController.fileLocation.getText(), "\\", 0, -2))));
                stage.close();
            });
            completeController.cancel.setOnAction(e -> stage.close());
            completeController.fileLocation.setText(fileLocation);
            completeController.fileName.setText(fileName);
            completeController.fileSize.setText(StringUtil.dataFormat(Double.parseDouble(fileSize)));
            completeController.source.setText(source);
            stage.setScene(new Scene(anchorPane));
            stage.setTitle("Download Complete");
            stage.setResizable(false);
            stage.showAndWait();
        }
    }

    public static Credentials login(String message) throws IOException {
        Stage stage = new Stage();
        FXMLLoader signInView = new FXMLLoader(Prompt.class.getResource(ResourcePath.SIGN_IN_VIEW));
        AnchorPane signInAnchorPane = signInView.load();
        SignInController signInController = signInView.getController();
        signInController.message.setText(message);
        signInController.getSignIn().setOnAction(e -> {
            signInController.credentials = new Credentials(signInController.username.getText(), signInController.password.getText());
            stage.close();
        });
        signInController.signUp.setOnMouseClicked(e -> {
            try {
                FXMLLoader signUpView = new FXMLLoader(Prompt.class.getResource(ResourcePath.SIGN_UP_VIEW));
                AnchorPane signUpAnchorPane = signUpView.load();
                SignInController signUpController = signInView.getController();
                signUpController.message.setText(message);
                Scene signUpScene = new Scene(signUpAnchorPane);
                stage.setScene(signUpScene);
                stage.setTitle("Signup");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        Scene signInScene = new Scene(signInAnchorPane);
        stage.setScene(signInScene);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.initModality(Modality.NONE);
        stage.showAndWait();
        return signInController.getCredentials();
    }

}

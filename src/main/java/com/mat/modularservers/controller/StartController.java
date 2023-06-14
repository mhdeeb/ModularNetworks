package com.mat.modularservers.controller;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class StartController {
    public Button browse;
    public Label fileName;
    public Label fileSize;
    public Label source;
    public TextField saveAs;
    public Button startDownload;
    public Button cancel;

    public void initialize() {
        Platform.runLater(browse::requestFocus);
    }

}

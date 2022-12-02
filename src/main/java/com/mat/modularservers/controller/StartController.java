package com.mat.modularservers.controller;

import javafx.application.Platform;
import javafx.scene.control.Button;

public class StartController {
    public Button browse;

    public void initialize() {
        Platform.runLater(browse::requestFocus);
    }
}

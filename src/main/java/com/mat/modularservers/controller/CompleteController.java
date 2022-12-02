package com.mat.modularservers.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.File;
import java.io.IOException;

;

public class CompleteController {

    public Label fileLocation;

    public void openLocation(ActionEvent actionEvent) throws IOException {
        Desktop.getDesktop().open(new File(fileLocation.getText()));
    }
}

package com.mat.modularservers.controller;

import com.mat.modularservers.util.TimeUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class ServerController {
    public Label date;
    public ScrollPane debuggerScrollPane;
    public TextArea debuggerTextArea;

    @FXML
    public void initialize() {
        debuggerTextArea.textProperty().addListener(e -> debuggerScrollPane.setVvalue(1));
        date.textProperty().bind(TimeUtil.getTimeProperty());
    }
}

package com.mat.modularservers.controller;

import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ChatController {

    @FXML
    TextField textField;
    @FXML
    TextArea textArea;
    @FXML
    Button button1;

    @FXML
    public void focusChange(KeyEvent keyEvent) {
        textField.requestFocus();
        keyEvent.consume();
    }

    public void close(Event event) {

    }

    public void initialize() {
//        Utility.suggestible(textField);
        button1.disableProperty().bind(Bindings.createBooleanBinding(()-> textField.textProperty().getValue().length() == 0,textField.textProperty()));
    }
}

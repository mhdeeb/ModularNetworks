package com.mat.modularservers.controller;

import com.mat.modularservers.util.Credentials;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

public class SignInController {
    public TextField username;
    public TextField password;
    public Button signIn;
    public Label signUp;
    public CheckBox remember;
    public Credentials credentials;
    public Label message;

    public void initialize() {
        signIn.disableProperty().bind(username.textProperty().isEmpty().or(password.textProperty().isEmpty()));
    }
    public void selectSignUp(MouseEvent mouseEvent) {
        signUp.setTextFill(Paint.valueOf("blue"));
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public TextField getUsername() {
        return username;
    }

    public TextField getPassword() {
        return password;
    }

    public Button getSignIn() {
        return signIn;
    }

    public Label getSignUp() {
        return signUp;
    }

    public CheckBox getRemember() {
        return remember;
    }

    public void deselectSignUp(MouseEvent mouseEvent) {
        signUp.setTextFill(Paint.valueOf("black"));
    }

}

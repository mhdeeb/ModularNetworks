package com.mat.modularservers.module;

import com.mat.modularservers.util.Locker;
import javafx.scene.control.TextField;

import java.util.Scanner;

public class Input<input> {
    private final input in;
    private String text;
    private final Locker locker = new Locker();

    public Input(input in) {
        this.in = in;
        if (in instanceof TextField textField)
            textField.setOnAction(e -> {
                text = textField.getText();
                textField.setText("");
                locker.unlock();
            });
    }

    public String read() throws InterruptedException {
        if (in instanceof Scanner scanner) {
            text = scanner.nextLine();
            return text;
        } else if (in instanceof TextField) {
            locker.lock();
            locker.breakPoint();
            return text;
        } else return text;
    }
}

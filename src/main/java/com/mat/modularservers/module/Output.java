package com.mat.modularservers.module;

import javafx.scene.control.TextArea;

import java.io.PrintStream;

public class Output<output> {
    output out;

    public Output(output out) {
        this.out = out;
    }

    public void write(String message) {
        if (out instanceof TextArea textArea) textArea.appendText(message);
        else if (out instanceof PrintStream printStream) {
            printStream.print(message);
        }
    }
}

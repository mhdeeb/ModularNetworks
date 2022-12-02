package com.mat.modularservers.module;

public class IOBundle<in, out> {
    Input<in> input;
    Output<out> output;

    public IOBundle(Input<in> input, Output<out> output) {
        this.input = input;
        this.output = output;
    }

    public void write(String message) {
        output.write(message);
    }

    public String read() throws InterruptedException {
        return input.read();
    }
}

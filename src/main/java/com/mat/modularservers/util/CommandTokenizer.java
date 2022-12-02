package com.mat.modularservers.util;

public class CommandTokenizer {
    public final Command command;
    public final String[] tokens;
    public final int size;
    public final String content;

    public CommandTokenizer(String message) {
        content = message;
        tokens = message.split(" ");
        size = tokens.length;
        command = Command.fromString(tokens[0]);
    }

    public boolean isCommand() {
        return command != Command.NULL;
    }
}

package com.mat.modularservers.util;

import org.jetbrains.annotations.NotNull;

public enum Command {
    KICK("\\kick"),
    BAN("\\ban"),
    NULL(""),
    IP_BAN("\\ipban");

    private final String command;

    Command(final String command) {
        this.command = command;
    }

    public static @NotNull Command fromString(String command) {
        for (Command c : Command.values()) if (c.toString().equals(command)) return c;
        return NULL;
    }

    public String toString() {
        return command;
    }
}

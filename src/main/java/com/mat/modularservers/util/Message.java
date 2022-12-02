package com.mat.modularservers.util;

import com.mat.modularservers.module.SocketWrapper;

public class Message {
    private final MessageFlag flag;
    private final String message;

    public MessageFlag getFlag() {
        return flag;
    }

    public String getMessage() {
        return message;
    }

    public SocketWrapper getSocket() {
        return socket;
    }

    private final SocketWrapper socket;

    public Message(MessageFlag flag, String message, SocketWrapper socket) {
        this.flag = flag;
        this.message = message;
        this.socket = socket;
    }

    public Message(MessageFlag flag, String message) {
        this(flag, message, null);
    }
}

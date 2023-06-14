package com.mat.modularservers.module;

import com.mat.modularservers.util.*;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

public class SocketWrapper {
    private final Socket socket;
    private String name;
    private boolean running;
    private final Logger logger = new Logger("Socket", LoggerType.ERROR);
    private final TransferManager transferManager;
    private ServerTier serverTier = ServerTier.USER;
    private Credentials credentials = Credentials.DEFAULT;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private Status status = Status.PENDING;
    private final String socketAddress;

    //TODO: changeName & Register.
    public SocketWrapper(Socket socket, ArrayBlockingQueue<Message> messageQueue) throws IOException {
        this.socket = socket;
        name = socket.getInetAddress().getHostName();
        socketAddress = socket.getInetAddress().getHostAddress();
        running = true;
        transferManager = new TransferManager(messageQueue, this, name, logger);
    }

    public void stop() {
        running = false;
        transferManager.close();
        ExceptionUtil.ignoreExceptions(socket::close);
    }

    public void setTier(ServerTier tier) {
        serverTier = tier;
    }

    public ServerTier getTier() {
        return serverTier;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public String getSocketAddress() {
        return socketAddress;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void login(Credentials credentials) throws InterruptedException {
        setCredentials(credentials);
        transferManager.login(credentials.username(), credentials.password());
    }

    public void setSoTimeOut(int soTimeOut) throws SocketException {
        socket.setSoTimeout(soTimeOut);
    }

    public void register(Credentials credentials) throws InterruptedException {
        setCredentials(credentials);
        transferManager.register(credentials.username(), credentials.password());
    }

    public void sendFlag(MessageFlag flag) {
        ExceptionUtil.logExceptions(() -> transferManager.flag(flag), logger);
    }

    public void sendMessage(String message) {
        ExceptionUtil.logExceptions(() -> transferManager.message(message), logger);
    }

    public void sendMessage(String message, MessageFlag flag) {
        ExceptionUtil.logExceptions(() -> transferManager.sendString(message, flag), logger);
    }

    public void sendFile(String path) {
        transferManager.file(path);
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void changeName() throws InterruptedException {
        transferManager.changeName(name);
    }

    public boolean isOn() {
        return running;
    }


    @Override
    public String toString() {
        return '[' + name + ", " + credentials + ", " + serverTier + ']';
    }
}

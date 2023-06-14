package com.mat.modularservers.module;

import com.mat.modularservers.gui.Prompt;
import com.mat.modularservers.util.*;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;

//TODO: Fix Pause and Close
//TODO: Send file button
//TODO: uploader popup
public class TransferManager {
    private final ArrayBlockingQueue<Message> messages;
    private final String name;
    private final SynchronousQueue<Boolean> responder = new SynchronousQueue<>();
    private boolean running = true;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final Logger logger;
    private final Locker locker = new Locker();
    private final ArrayBlockingQueue<byte[]> codes = new ArrayBlockingQueue<>(1024);
    private final SocketWrapper socket;
    private Prompt prompt;

    public TransferManager(ArrayBlockingQueue<Message> messages, SocketWrapper socket, String name, Logger logger) throws IOException {
        this.messages = messages;
        this.socket = socket;
        this.inputStream = new DataInputStream(new BufferedInputStream(socket.getSocket().getInputStream()));
        this.outputStream = new DataOutputStream(socket.getSocket().getOutputStream());
        this.logger = logger;
        this.name = name;
        decode();
        sender();
    }


    public void decode() {
        new Thread(() -> {
            try {
                while (running) {
                    ExceptionUtil.logException(() -> {
                        MessageFlag flag = MessageFlag.fromByte(inputStream.readByte());
                        switch (flag) {
                            case STRING_0, NAME_CHANGE, LOGIN_REQUEST, LOGIN_FAILED, REGISTER_FAILED, LOGIN_SUCCEEDED, BAN_REPLY -> {
                                byte[] buffer = new byte[inputStream.readInt()];
                                inputStream.readFully(buffer);
                                messages.put(new Message(flag, new String(buffer), socket));
                            }
                            case DATA_HEADER -> {
                                long dataSize = inputStream.readLong();
                                byte[] buffer = new byte[inputStream.readInt()];
                                inputStream.readFully(buffer);
                                String fileName = new String(buffer);
                                prompt = new Prompt();
                                Platform.runLater(()-> ExceptionUtil.logExceptions(()->flag(prompt.pop(fileName, dataSize, name)), logger));
                            }
                            case DATA -> {
                                byte[] buffer = new byte[inputStream.readInt()];
                                inputStream.readFully(buffer);
                                Platform.runLater(()-> ExceptionUtil.logExceptions(()->prompt.write(buffer), logger));
                            }
                            case DATA_END -> Platform.runLater(()-> ExceptionUtil.logExceptions(()->prompt.complete(), logger));
                            case QUIT -> {
                                messages.put(new Message(flag, null, socket));
                                close();
                            }
                            case RESPONSE_ACCEPT -> responder.put(true);
                            case RESPONSE_REJECT -> responder.put(false);
                            case REQUEST_PAUSE -> pause();
                            case REQUEST_RESUME -> resume();
                        }
                    }, logger, InterruptedException.class);
                }
            } catch (Exception e) {
                logger.logln(e);
                ExceptionUtil.logExceptions(() -> messages.put(new Message(MessageFlag.DISCONNECT, null, socket)), logger);
                close();
            }
        }).start();
    }

    public void close() {
        running = false;
    }

    public void pause() {
        locker.lock();
    }

    public void resume() {
        locker.unlock();
    }

    public void sendString(String message, MessageFlag flag) throws InterruptedException {
        codes.put(ArrayUtil.concatArray(flag.toByte(), ByteUtil.intToBytes(message.length()), message.getBytes()));
    }

    public void changeName(String newName) throws InterruptedException {
        sendString(newName, MessageFlag.NAME_CHANGE);
    }

    public void flag(MessageFlag messageFlag) throws InterruptedException {
        codes.put(new byte[]{messageFlag.toByte()});
    }

    public void message(String message) throws InterruptedException {
        sendString(message, MessageFlag.STRING_0);
    }

    public void file(String path) {
        new Thread(() -> ExceptionUtil.logExceptions(() -> {
            FileData fileData = new FileData(path);
            codes.put(ArrayUtil.concatArray(MessageFlag.DATA_HEADER.toByte(), ByteUtil.longToBytes(fileData.size), ByteUtil.intToBytes(fileData.name.length()), fileData.name.getBytes()));
            if (responder.take()) {
                DataInputStream fileStream = new DataInputStream(new FileInputStream(fileData.file));
                byte[] buffer = new byte[8 * 1024];
                int count;
                while ((count = fileStream.read(buffer)) > 0) {
                    codes.put(ArrayUtil.concatArray(MessageFlag.DATA.toByte(), ByteUtil.intToBytes(count), Arrays.copyOf(buffer, count)));
                    locker.breakPoint();
                }
                flag(MessageFlag.DATA_END);
                fileStream.close();
            }
        }, logger)).start();
    }

    void sender() {
        new Thread(() -> {
            while (running) {
                try {
                    outputStream.write(codes.take());
                } catch (IOException e) {
                    logger.logln(e);
                    close();
                } catch (InterruptedException e) {
                    logger.logln(e);
                }
            }
        }).start();
    }

    public void login(String name, String password) throws InterruptedException {
        sendString(name + " " + password, MessageFlag.LOGIN_REQUEST);
    }

    public void register(String username, String password) throws InterruptedException {
        sendString(username + " " + password, MessageFlag.REGISTER_REQUEST);
    }
}

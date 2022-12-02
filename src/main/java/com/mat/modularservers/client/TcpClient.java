package com.mat.modularservers.client;


import com.mat.modularservers.module.IOBundle;
import com.mat.modularservers.module.Logger;
import com.mat.modularservers.module.SocketWrapper;
import com.mat.modularservers.util.*;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

//TODO: FIX LOGIN PROMPT.
public class TcpClient {
    private final IOBundle ioBundle;
    private final SocketWrapper socket;
    private final Logger logger = new Logger("Client", LoggerType.MESSAGE);
    private final ArrayBlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(1024);

    public TcpClient(String address, int port, IOBundle ioBundle, int attempts, int repeatInterval) throws Exception {
        socket = new SocketWrapper(connect(address, port, attempts, repeatInterval), messageQueue);
        this.ioBundle = ioBundle;
        println("Connected to " + address + " on port " + port);
        read();
        write();
    }

    private Socket connect(String address, int port, int attempts, int interval) throws ConnectException {
        int c = 0;
        while (c++ < attempts) try {
            return new Socket(address, port);
        } catch (IOException ioe) {
            logger.logln(ioe.getMessage());
            try {
                Thread.sleep(interval);
            } catch (InterruptedException ie) {
                logger.logln(ie);
            }
        }
        throw new ConnectException("Connection refused: connect");
    }

    private void read() {
        new Thread(() -> ExceptionUtil.logExceptions(() -> {
            while (socket.isOn()) {
                Message message = messageQueue.take();
                switch (message.getFlag()) {
                    case LOGIN_SUCCEEDED -> {
                        socket.setStatus(Status.CONNECTED);
                        println(message.getMessage());
                    }
                    case STRING_0 -> println(message.getMessage());
                    case QUIT -> {
                        println("Server Closed.");
                        socket.stop();
                    }
                    case DISCONNECT -> {
                        println("Server Disconnected.");
                        socket.stop();
                    }
                    case BAN_REPLY -> {
                        print("You are banned from this server.");
                        socket.stop();
                    }
                    case LOGIN_REQUEST, LOGIN_FAILED, REGISTER_FAILED, REGISTER_SUCCEEDED -> {
                        print("Input 1 for login and 2 for register");
                        switch (ioBundle.read()) {
                            case "1" -> {
                                print("Enter Username: ");
                                String user = ioBundle.read();
                                print("Enter Password: ");
                                String pass = ioBundle.read();
                                socket.login(new Credentials(user, pass));
                            }
                            case "2" -> {
                                print("Enter Username: ");
                                String user = ioBundle.read();
                                print("Enter Password: ");
                                String pass = ioBundle.read();
                                socket.register(new Credentials(user, pass));
                            }
                        }
                    }
                }
            }
        }, logger)).start();
    }

    private void write() {
        new Thread(() -> ExceptionUtil.logExceptions(() -> {
            while (socket.isOn()) socket.sendMessage(ioBundle.read());
        }, logger)).start();
    }

    private void print(String message) {
        ioBundle.write(message);
    }

    private void println(String message) {
        print(message + '\n');
    }
}

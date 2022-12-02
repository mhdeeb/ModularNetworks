package com.mat.modularservers.server;

import com.mat.modularservers.module.*;
import com.mat.modularservers.util.*;

import java.io.IOException;
import java.net.ServerSocket;

//TODO: Fix login requests.
//TODO: Fix Input/Output/IOBundle.
public class TcpServer {
    private final ServerSocket server;
    private final ClientList clients = new ClientList();
    private final Logger logger = new Logger("Server", LoggerType.ERROR), chat = new Logger("Chat", LoggerType.MESSAGE);
    private boolean state;

    public TcpServer(int port) {
        try {
            server = new ServerSocket(port);
            state = true;
            listen();
            readClients();
//            clients.setForceLogin(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMessageDisplay(Output output) {
        chat.setDisplay(output);
        logger.logln("Chat logger online...");
    }

    public void setErrorDisplay(Output output) {
        logger.setDisplay(output);
        logger.logln("Server logger online...");
    }

    public void attachInput(Input input) {
        new Thread(() -> ExceptionUtil.logExceptions(() -> {
            while (state) {
                String message = input.read();
                CommandTokenizer ct = new CommandTokenizer(message);
                switch (ct.command) {
                    case KICK -> {
                        if (ct.size == 2) {
                            if (clients.kick(ct.tokens[1])) {
                                broadcast(ct.tokens[1] + " was kicked for the server.");
                                chat.logln(ct.tokens[1] + " was kicked for the server.");
                            } else chat.logln(ct.tokens[1] + " does not exist.");
                        }
                    }
                    case BAN -> {
                        if (ct.size == 2) {
                            if (clients.ban(ct.tokens[1], false)) {
                                broadcast(ct.tokens[1] + " was banned for the server.");
                                chat.logln(ct.tokens[1] + " was banned for the server.");
                            } else chat.logln(ct.tokens[1] + " does not exist.");
                        }
                    }
                    case IP_BAN -> {
                        if (ct.size == 2) {
                            if (clients.ban(ct.tokens[1], true)) {
                                broadcast(ct.tokens[1] + " was ip banned for the server.");
                                chat.logln(ct.tokens[1] + " was ip banned for the server.");
                            } else chat.logln(ct.tokens[1] + " does not exist.");
                        }
                    }
                    default -> {
                        chat.logln("Server: " + message);
                        broadcast("Server: " + message);
                    }
                }
            }
        }, logger)).start();
    }

    public void close() {
        state = false;
        clients.close();
        ExceptionUtil.ignoreExceptions(server::close);
        logger.logln("Server Closed.");
    }

    private void listen() {
        new Thread(() -> {
            while (state) {
                ExceptionUtil.logExceptions(() -> {
                    SocketWrapper client = new SocketWrapper(server.accept(), clients.getMessageQueue());
                    logger.logln(client.getName() + " connected.");
                    if (clients.join(client)) broadcast(client.getName() + " has joined.", client);
                    else if (client.isOn()) {
                        logger.logln(client.getName() + " is in pending list.");
                    } else {
                        logger.logln(client.getName() + " was kicked out.");
                    }
//                    client.sendFile("C:\\Users\\moham\\Desktop\\-\\processing-3.5.4-windows64.zip");
                }, logger);
            }
        }).start();
    }

    private void readClients() {
        new Thread(() -> {
            while (state) {
                ExceptionUtil.logExceptions(() -> {
                    Message message;
                    if ((message = clients.getMessage()) != null) {
                        SocketWrapper client = message.getSocket();
                        if (client.getStatus() == Status.CONNECTED)
                            switch (message.getFlag()) {
                                case STRING_0 -> {
                                    chat.logln(String.format("\b[%s] %s: %s", client.getSocketAddress(), client.getName(), message.getMessage()));
                                    broadcast(client.getName() + ": " + message.getMessage());
                                }
                                case NAME_CHANGE -> {
                                    broadcast(client.getName() + " was changed to " + message.getMessage());
                                    client.setName(message.getMessage());
                                }
                                case QUIT -> {
                                    chat.logln(client.getName() + " has left.");
                                    broadcast(client.getName() + " has left.");
                                    clients.remove(client);
                                }
                                case DISCONNECT -> {
                                    chat.logln(client.getName() + " has disconnected.");
                                    broadcast(client.getName() + " has disconnected.");
                                    clients.remove(client);
                                }
                            }
                        else switch (message.getFlag()) {
                            case LOGIN_REQUEST -> {
                                String[] credentials = message.getMessage().split(" ");
                                client.setCredentials(new Credentials(credentials[0], credentials[1]));
                                if (clients.join(client)) {
                                    broadcast(client.getName() + " has joined.");
                                    logger.logln(client.getName() + " logged in successfully.");
                                }
                            }
                            case REGISTER_REQUEST -> {
                                String[] credentials = message.getMessage().split(" ");
                                client.setCredentials(new Credentials(credentials[0], credentials[1]));
                                clients.register(client);
                            }
                        }
                    }
                }, logger);
            }
        }).start();
    }

    void broadcast(String message) {
        broadcast(message, null);
    }

    void broadcast(String message, SocketWrapper exclusion) {
        clients.forEach(client -> {
            if (!client.equals(exclusion)) ExceptionUtil.logExceptions(() -> client.sendMessage(message), logger);
        });
    }
}

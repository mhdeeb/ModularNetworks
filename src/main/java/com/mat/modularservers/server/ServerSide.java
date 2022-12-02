//package com.assets.server;
//
//import com.mat.modularservers.module.SocketWrapper;
//import javafx.beans.property.StringProperty;
//
//import java.io.IOException;
//import java.net.Socket;
//import java.net.ServerSocket;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//public class ServerSide {
//    private final ServerSocket server;
//    private final ConcurrentHashMap<String, SocketWrapper> clients;
//    private final ConcurrentHashMap<String, String> names;
//    private boolean state;
//    private final StringProperty textArea, textField;
//    private final ScheduledExecutorService reader;
//
//    public ServerSide(String port, StringProperty[] stringProperty) throws IOException {
//        textArea = stringProperty[0];
//        textField = stringProperty[1];
//        clients = new ConcurrentHashMap<>();
//        names = new ConcurrentHashMap<>();
//        server = new ServerSocket(Integer.parseInt(port));
//        state = true;
//        listen().start();
//        reader = readClients();
//        addString("Listening...");
//    }
//
//    void close() throws IOException {
//        state = false;
//        reader.shutdown();
//        server.close();
//        clients.clear();
//        addString("Server Closed.");
//    }
//
//    void broadcast(String message) {
//        addString(message);
//        clients.forEach((k, v) -> {
//            try {
//                v.output().writeUTF(message);
//            } catch (IOException e) {
//                addString("Failed to reach " + v.name() + ".");
//                try {
//                    release(k, v);
//                    broadcast(v.name() + " has disconnect.");
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        });
//    }
//
//    Thread listen() {
//        return new Thread(() -> {
//            while (state) {
//                try {
//                    Socket socket = server.accept();
//                    String name = socket.toString();
//                    clients.put(name, new SClient(socket));
//                    names.put(clients.get(name).name(), name);
//                    broadcast(clients.get(name).name() + " has joined the lobby.");
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//    }
//
//    ScheduledExecutorService readClients() {
//        ScheduledExecutorService reader = Executors.newSingleThreadScheduledExecutor();
//        reader.scheduleAtFixedRate(() -> {
//            if (!state) {
//                reader.shutdown();
//            }
//            clients.forEach((k, v) -> {
//                try {
//                    if (v.input().available() > 0) {
//                        String in = v.input().readUTF();
//                        String[] words = in.split(" ");
//                        if (words[0].equals("\\setName")) {
//                            try {
//                                changeName(k, v, words[1]);
//                            } catch (ArrayIndexOutOfBoundsException e) {
//                                v.output().writeUTF("Error: \\setName is missing \"name\" parameter.");
//                            }
//                        } else if (words[0].equals("\\quit")) {
//                            release(k, v);
//                            broadcast(v.name() + " has logged off.");
//                        } else {
//                            broadcast(v.name() + ": " + in);
//                        }
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }, 0, 100, TimeUnit.MILLISECONDS);
//        return reader;
//    }
//
//    void listClients() {
//        addString("Clients:");
//        clients.forEach((k, v) -> addString(v.name() + ": " + v.socket().getInetAddress().toString()));
//    }
//
//    void release(String k, SClient v) throws IOException {
//        names.remove(v.name());
//        v.close();
//        clients.remove(k);
//    }
//
//    void changeName(String k, SClient v, String newName) {
//        broadcast(v.name() + " was changed to " + newName);
//        names.remove(v.name());
//        names.put(newName, k);
//        v.setName(newName);
//    }
//
//    void command() {
//        if (!textField.get().isBlank()) {
//            String message = textField.get();
//            String[] words = message.split(" ");
//            if (message.equals("\\quit")) {
//                try {
//                    server.close();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            } else if (message.equals("\\list")) {
//                listClients();
//            } else {
//                broadcast("Server: " + message);
//            }
//            textField.setValue("");
//        }
//    }
//
//    void addString(String message) {
//        textArea.setValue(textArea.getValue() + message + "\n");
//    }
//}

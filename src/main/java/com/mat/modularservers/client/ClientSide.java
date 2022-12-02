//package com.mat.modularservers.client;
//
//import javafx.beans.property.StringProperty;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.net.SocketTimeoutException;
//
//public class ClientSide implements Runnable {
//    private final Socket socket;
//    private DataOutputStream out;
//    private DataInputStream in;
//    private final StringProperty textArea;
//    private final StringProperty textField;
//    private boolean running = true;
//    InetSocketAddress socketAddress;
//    int timeout;
//
//    ClientSide(String address, int port, StringProperty[] stringProperty, int timeout) {
//        this.timeout = timeout;
//        textArea = stringProperty[0];
//        textField = stringProperty[1];
//        socketAddress = new InetSocketAddress(address, port);
//        socket = new Socket();
//    }
//
//    boolean connect() {
//        try {
//            socket.connect(socketAddress, timeout);
//            socket.setSoTimeout(100);
//            addString("Connected to " + socketAddress.getAddress() + " on port " + socketAddress.getPort());
//            addString("If you want to change your name use (\\setName \"name\") command");
//            out = new DataOutputStream(socket.getOutputStream());
//            in = new DataInputStream(socket.getInputStream());
//            return true;
//        } catch (IOException e) {
//            Debugger.println(e.getMessage());
//            return false;
//        }
//    }
//
//    void write() {
//        try {
//            if (!textField.getValue().isBlank()) {
//                out.writeUTF(textField.getValue());
//                textField.setValue("");
//            }
//        } catch (IOException e) {
//            Debugger.println(e);
//        }
//    }
//
//    void leave() {
//        try {
//            running = false;
//            socket.close();
//        } catch (IOException e) {
//            Debugger.println(e);
//        }
//    }
//
//    void addString(String message) {
//        textArea.setValue(textArea.getValue() + message + "\n");
//    }
//
//    @Override
//    public void run() {
//        if (connect()) {
//            while (running) {
//                try {
//                    addString(in.readUTF());
//                } catch (SocketTimeoutException ignored) {
//                } catch (IOException e) {
//                    addString("Connection lost.");
//                    leave();
//                }
//            }
//        }
//    }
//}

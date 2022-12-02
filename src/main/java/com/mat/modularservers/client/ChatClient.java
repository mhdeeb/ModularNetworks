//package com.mat.modularservers.client;
//
//
//import javafx.fxml.FXMLLoader;
//import javafx.scene.control.*;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.text.TextFlow;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.*;
//import java.util.Objects;
//
//public class ChatClient implements Runnable {
//    private int timeout;
//    private TextArea textArea;
//    private TextField textField;
//    private DataOutputStream out;
//    private DataInputStream in;
//    private boolean running = true;
//    private Tab tab;
//    private InetSocketAddress socketAddress;
//    private Socket tcpSocket;
//
//    ChatClient(String name, String address, int port, int timeout) {
//        try {
//            tab = ((TabPane)FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("fxml/chat-view.fxml")))).getTabs().get(0);
//            tab.setText(name);
//            textArea = textArea(tab);
//            textField = textField(tab);
//            textField.setOnAction(actionEvent -> write());
//            tab.setOnCloseRequest(event -> leave());
//            button(tab).onActionProperty().bind(textField(tab).onActionProperty());
//            this.timeout = timeout;
//            socketAddress = new InetSocketAddress(address, port);
//            tcpSocket = new Socket();
//        } catch (IOException e) {
//            Debugger.println(e.getMessage());
//        }
//    }
//
//    boolean connect() {
//        try {
//            tcpSocket.connect(socketAddress, timeout);
//            tcpSocket.setSoTimeout(timeout);
//            out = new DataOutputStream(tcpSocket.getOutputStream());
//            in = new DataInputStream(tcpSocket.getInputStream());
//            addString("Connected to " + socketAddress.getAddress() + " on port " + socketAddress.getPort());
//            addString("If you want to change your name use (\\setName \"name\") command");
//            return true;
//        } catch (IOException e) {
//            Debugger.println(e.getMessage());
//            return false;
//        }
//    }
//
//    void write() {
//        if (!textField.getText().isBlank()) {
//            try {
//                out.writeUTF(textField.getText());
//                textField.setText("");
//            } catch (IOException e) {
//                Debugger.println(e);
//            }
//        }
//    }
//
//    void leave() {
//        try {
//            running = false;
//            tcpSocket.close();
//        } catch (IOException e) {
//            Debugger.println(e);
//        }
//
//    }
//
//    @Override
//    public void run() {
//        if (connect()) {
//            try {
//                while (running) addString(in.readUTF());
//            } catch (SocketTimeoutException ignored) {
//            } catch (IOException e) {
//                addString("Connection lost.");
//                leave();
//            }
//        }
//    }
//
//    private TextArea textArea(Tab tab) {
//        return ((TextArea) ((TextFlow) ((AnchorPane) ((ScrollPane) ((BorderPane) tab.getContent()).getCenter()).getContent()).getChildren().get(0)).getChildren().get(1));
//    }
//
//    private TextField textField(Tab tab) {
//        return ((TextField) ((HBox) ((BorderPane) tab.getContent()).getBottom()).getChildren().get(1));
//    }
//
//    private Button button(Tab tab) {
//        return (Button) ((HBox) ((BorderPane) tab.getContent()).getBottom()).getChildren().get(0);
//    }
//
//    void addString(String message) {
//        textArea.appendText(message + "\n");
//    }
//
//    public Tab tab() {
//        return tab;
//    }
//}

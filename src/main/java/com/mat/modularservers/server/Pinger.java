//package com.mat.modularservers.server;
//
//
//import com.mat.modularservers.gui.Sounds;
//import com.mat.modularservers.util.Debugger;
//import javafx.application.Platform;
//import javafx.beans.binding.Bindings;
//import javafx.beans.property.IntegerProperty;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.control.*;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.paint.Color;
//import javafx.scene.text.TextFlow;
//import org.jetbrains.annotations.Nullable;
//
//import java.io.IOException;
//import java.net.*;
//import java.nio.charset.StandardCharsets;
//import java.util.Objects;
//
//public class Pinger implements Runnable {
//    private Tab tab;
//    private final InetSocketAddress socketAddress;
//    private DatagramSocket udpSocket;
//    private byte[] udpBuffer;
//    boolean starter;
//    boolean running = true, connected = false;
//    int timeOut, attempts, usedAttempts = 0;
//    String tabName;
//    IntegerProperty notifications = new SimpleIntegerProperty();
//    Label label;
//
//    public Pinger(String name, String address, int port, int timeout, int attempts) {
//        this.attempts = attempts;
//        timeOut = timeout;
//        starter = true;
//        socketAddress = new InetSocketAddress(address, port);
//        rest(name, timeout, port);
//    }
//
//    public Pinger(String name, int port, int timeout, int attempts) {
//        this.attempts = attempts;
//        starter = false;
//        socketAddress = new InetSocketAddress(port);
//        rest(name, timeout, port);
//    }
//
//    private void rest(String name, int timeout, int port) {
//        try {
//            Debugger.println("[" + name + "] Creating Module...");
//            tab = ((TabPane) FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("fxml/chat-view.fxml")))).getTabs().get(0);
//            tabName = name;
//            tab.setText(name);
//            tab.setId(name);
//            tab.setOnSelectionChanged(event -> {
//                if (((Tab) event.getTarget()).isSelected()) notifications.setValue(0);
//            });
//            tab.textProperty().bind(Bindings.createStringBinding(() -> {
//                if (notifications.getValue() != 0) {
//                    return tabName + "(" + notifications.getValue() + ")";
//                } else {
//                    return tabName;
//                }
//            }, notifications));
//            label = label(tab);
//            textArea = textArea(tab);
//            textField = textField(tab);
//            textField.setOnAction(actionEvent -> write());
//            tab.setOnCloseRequest(event -> leave());
//            button(tab).onActionProperty().bind(textField.onActionProperty());
//            button(tab).disableProperty().bind(Bindings.createBooleanBinding(()-> textField.textProperty().getValue().length() == 0,textField.textProperty()));
//            if (starter) {
//                udpSocket = new DatagramSocket();
//            } else {
//                udpSocket = new DatagramSocket(port);
//            }
//            udpSocket.setSoTimeout(timeout);
//        } catch (IOException e) {
//            Debugger.println("[" + tabName + "] " + e.getMessage());
//        }
//    }
//
//    private void write() {
//        if (!textField.getText().isBlank()) {
//            addString("you: " + textField.getText());
//            udpBuffer = textField.getText().getBytes(StandardCharsets.UTF_8);
//            textField.setText("");
//        }
//    }
//
//    private void leave() {
//        running = false;
//        udpSocket.close();
//        Platform.runLater(() -> {
//            if (tab.getTabPane() != null) tab.getTabPane().getTabs().remove(tab);
//        });
//        connected = false;
//    }
//
//    @Override
//    public void run() {
//        String array;
//        while (running) {
//            long t1 = java.lang.System.nanoTime();
//            array = starter ? ping(udpBuffer) : respond(udpBuffer);
//            long t2 = java.lang.System.nanoTime();
//            udpBuffer = null;
//            Platform.runLater(() -> {
//                int ms = (int) ((t2 - t1) * 0.000001);
//                if (ms > 200) {
//                    label.setTextFill(Color.color(164. / 255, 6. / 255, 6. / 255));
//                } else if (ms > 80) {
//                    label.setTextFill(Color.color(215. / 255, 95. / 255, 5. / 255));
//                } else {
//                    label.setTextFill(Color.color(5. / 255, 151. / 255, 5. / 255));
//                }
//                label.setText(ms + " ms");
//            });
//            if (array != null) {
//                Platform.runLater(() -> {
//                    if (!tab.isSelected()) notifications.setValue(notifications.getValue() + 1);
//                    Sounds.beep();
//                });
//                addString("them: " + array);
//            }
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    private InlineCssTextArea textArea(Tab tab) {
//        return ((InlineCssTextArea) ((TextFlow) ((AnchorPane) ((ScrollPane) ((BorderPane) tab.getContent()).getCenter()).getContent()).getChildren().get(0)).getChildren().get(0));
//    }
//
//    private InlineCssTextField textField(Tab tab) {
//        return ((InlineCssTextField) ((HBox) ((BorderPane) tab.getContent()).getBottom()).getChildren().get(1));
//    }
//
//    private Button button(Tab tab) {
//        return (Button) ((HBox) ((BorderPane) tab.getContent()).getBottom()).getChildren().get(0);
//    }
//
//    private void addString(String message) {
//        Platform.runLater(()->textArea.appendText(message + "\n"));
//    }
//
//    public Tab tab() {
//        return tab;
//    }
//
//    private String ping(byte @Nullable [] message) {
//        try {
//            if (message == null) {
//                udpSocket.send(new DatagramPacket(new byte[]{}, 0, 0, socketAddress));
//            } else {
//                udpSocket.send(new DatagramPacket(message, 0, message.length, socketAddress));
//            }
//            byte[] bytes = new byte[512];
//            DatagramPacket datagramPacket = new DatagramPacket(bytes, 0, bytes.length);
//            udpSocket.receive(datagramPacket);
//            if (!connected) {
//                connected = true;
//                textField.disableProperty().setValue(false);
//                Debugger.println("[" + tabName + "] Connected.");
//            }
//            if (datagramPacket.getLength() == 0) return null;
//            else return new String(datagramPacket.getData(), StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            Debugger.println("[" + tabName + "] " + e.getMessage());
//            if (usedAttempts++ == attempts) leave();
//            return null;
//        }
//    }
//
//    private String respond(byte @Nullable [] message) {
//        byte[] bytes = new byte[500];
//        DatagramPacket datagramPacket = new DatagramPacket(bytes, 0, bytes.length);
//        try {
//            udpSocket.receive(datagramPacket);
//            if (!connected) {
//                connected = true;
//                textField.disableProperty().setValue(false);
//                Debugger.println("[" + tabName + "] Connected.");
//            }
//            if (message == null) {
//                udpSocket.send(new DatagramPacket(new byte[]{}, 0, 0, datagramPacket.getSocketAddress()));
//            } else {
//                udpSocket.send(new DatagramPacket(message, 0, message.length, datagramPacket.getSocketAddress()));
//            }
//            if (datagramPacket.getLength() == 0) {
//                return null;
//            } else {
//                return new String(datagramPacket.getData(), StandardCharsets.UTF_8);
//            }
//        } catch (IOException e) {
//            Debugger.println("[" + tabName + "] " + e.getMessage());
//            if (++usedAttempts == attempts) leave();
//            return null;
//        }
//    }
//
//    public Label label(Tab tab) {
//        return (Label) ((AnchorPane) ((ScrollPane) ((BorderPane) tab.getContent()).getCenter()).getContent()).getChildren().get(1);
//    }
//}

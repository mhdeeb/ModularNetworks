// TODO: Message header + user names
// TODO: Data
// TODO: Android
// TODO: Add bluetooth
// TODO: Encryption
// TODO: Fragment messages

package com.mat.modularservers.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Objects;
public class MainController {
    @FXML
    TabPane tabs;

    @FXML
    public void initialize() throws IOException {
        Tab tab = ((TabPane) FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/com/mat/modularservers/fxml/add-view.fxml")))).getTabs().get(0);
        tabs.getTabs().add(tab);
    }

//    public void createNetwork() {
//        try {
//            if (chatSwitch.isSelected()) {
//                tab = createChatTab();
//                if (udpSwitch.isSelected()) {
//                    UdpListener udpListener = new UdpListener(Integer.parseInt(port.getText()), getTextOfTab(tab));
//                    tab.setOnCloseRequest(event -> udpListener.end());
//                } else {
//                    if (clientSwitch.isSelected()) {
//                        ClientSide clientSide = new ClientSide(address.getText() + ":" + port.getText(), getTextOfTab(tab), Integer.parseInt(timeout.getText()));
//                        getTextFieldOfTab(tab).setOnAction(actionEvent -> clientSide.write());
//                        tab.setOnCloseRequest(event -> clientSide.leave());
//                        new Thread(clientSide).start();
//                    } else {
//                        ServerSide serverSide = new ServerSide(port.getText(), getTextOfTab(tab));
//                        tab.setOnCloseRequest(event -> {
//                            try {
//                                serverSide.close();
//                            } catch (IOException e) {
//                                Debugger.println(e);
//                            }
//                        });
//                        getTextFieldOfTab(tab).setOnAction(actionEvent -> serverSide.command());
//                    }
//                }
//                getButtonOfTab(tab).onActionProperty().bind(getTextFieldOfTab(tab).onActionProperty());
//            } else {
//                if (udpSwitch.isSelected()) {
//                    if (clientSwitch.isSelected()) {
//                        Debugger.println("udp, client, data");
//                    } else {
//                        Debugger.println("udp, server, data");
//                    }
//                } else {
//                    if (clientSwitch.isSelected()) {
//                        Debugger.println("tcp, client, data");
//                    } else {
//                        Debugger.println("tcp, server, data");
//                    }
//                }
//            }
//            tabs.getTabs().add(tabs.getTabs().size() - 1, tab);
//            tabs.getSelectionModel().select(tabs.getTabs().size() - 2);
//        } catch (Exception e) {
//        }
//    }
//
//    Tab createChatTab() {
//        try {
//            Tab tab = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("chat-view.fxml")));
//            openTabs++;
//            tab.setText(name.getText());
//            tab.setId("tab" + openTabs);
//            tab0.setText("+");
//            name.setText("");
//            getButtonOfTab(tab).onActionProperty().bind(getTextFieldOfTab(tab).onActionProperty());
//            return tab;
//        } catch (Exception e) {
//            Debugger.println(e);
//            return null;
//        }
//    }
//
//    StringProperty[] getTextOfTab(Tab tab) {
//        return new StringProperty[]{((TextArea) ((TextFlow) ((AnchorPane) ((ScrollPane) ((BorderPane) tab.getContent()).getCenter()).getContent()).getChildren().get(0)).getChildren().get(0)).textProperty(), ((TextField) ((HBox) ((BorderPane) tab.getContent()).getBottom()).getChildren().get(1)).textProperty()};
//    }
//
//    TextField getTextFieldOfTab(Tab tab) {
//        return (TextField) ((HBox) ((BorderPane) tab.getContent()).getBottom()).getChildren().get(1);
//    }
//
//    Button getButtonOfTab(Tab tab) {
//        return (Button) ((HBox) ((BorderPane) tab.getContent()).getBottom()).getChildren().get(0);
//    }
}
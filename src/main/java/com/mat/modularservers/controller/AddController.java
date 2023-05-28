package com.mat.modularservers.controller;

import com.mat.modularservers.server.Pinger;
import com.mat.modularservers.util.Debugger;
import com.mat.modularservers.util.Utility;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.controlsfx.control.ToggleSwitch;

import java.io.IOException;


public class AddController {
    public Tab tab0;
    public Label addressL, date;
    public TextField name, address, port, attempts, timeout;
    public TextArea debuggerTextArea;
    public Button button;
    public ToggleSwitch dataSwitch, clientSwitch, udpSwitch;
    public ScrollPane debuggerScrollPane;

    @FXML
    public void initialize() {
//        Debugger.attach(new Output<>(debuggerTextArea));
        debuggerTextArea.textProperty().addListener(e -> debuggerScrollPane.setVvalue(1));
        date.textProperty().bind(Utility.getTimeProperty());
        address.disableProperty().bind(clientSwitch.selectedProperty().not());
        addressL.disableProperty().bind(clientSwitch.selectedProperty().not());
        button.disableProperty().bind(Bindings.createBooleanBinding(() -> name.textProperty().getValue().isEmpty() || (address.textProperty().getValue().isEmpty() && !address.disableProperty().getValue()) || port.textProperty().getValue().isEmpty(), name.textProperty(), address.textProperty(), address.disableProperty(), port.textProperty()));
        tab0.textProperty().bind(Bindings.createStringBinding(() -> (name.getText().isBlank()) ? "+" : name.getText(), name.textProperty()));
    }

    public void traverse(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        javafx.collections.ObservableList<javafx.scene.Node> peers = node.getParent().getChildrenUnmodifiable().filtered(element -> !element.isDisabled() && element.isFocusTraversable());
        if (peers.size() > 1) peers.get((peers.indexOf(node) + 1) % peers.size()).requestFocus();
    }

    public void createModule(ActionEvent actionEvent) throws IOException, InterruptedException {
        if (!udpSwitch.isSelected() && clientSwitch.isSelected() && !dataSwitch.isSelected()) {
//            TcpClient chatClient = new TcpClient(name.getText(), address.getText(), Integer.parseInt(port.getText()), Integer.parseInt(timeout.getText()));
//            new Thread(chatClient).start();
//            parent().getTabs().add(parent().getTabs().size() - 1, chatClient.tab());
//            parent().getSelectionModel().select(chatClient.tab());
        } else if (udpSwitch.isSelected() && clientSwitch.isSelected() && !dataSwitch.isSelected()) {
            Pinger pinger = new Pinger(name.getText(), address.getText(), Integer.parseInt(port.getText()), Integer.parseInt(timeout.getText()), Integer.parseInt(attempts.getText()));
            new Thread(pinger).start();
            parent().getTabs().add(parent().getTabs().size() - 1, pinger.tab());
            parent().getSelectionModel().select(pinger.tab());
        } else if (udpSwitch.isSelected() && !clientSwitch.isSelected() && !dataSwitch.isSelected()) {
            Pinger pinger = new Pinger(name.getText(), Integer.parseInt(port.getText()), Integer.parseInt(timeout.getText()), Integer.parseInt(attempts.getText()));
            new Thread(pinger).start();
            parent().getTabs().add(parent().getTabs().size() - 1, pinger.tab());
            parent().getSelectionModel().select(pinger.tab());
        } else if (!udpSwitch.isSelected() && !clientSwitch.isSelected() && !dataSwitch.isSelected()) {
//            TcpServer chatServer = new TcpServer(Integer.parseInt(port.getText()));
//            parent().getTabs().add(parent().getTabs().size() - 1, chatClient.tab());
//            parent().getSelectionModel().select(chatClient.tab());
        }
        name.setText("");

    }

    private TabPane parent() {
        return tab0.getTabPane();
    }
}
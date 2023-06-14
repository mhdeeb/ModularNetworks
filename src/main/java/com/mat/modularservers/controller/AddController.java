package com.mat.modularservers.controller;

import com.mat.modularservers.client.DataTunnelClient;
import com.mat.modularservers.client.TcpClient;
import com.mat.modularservers.module.IOBundle;
import com.mat.modularservers.module.Input;
import com.mat.modularservers.module.Output;
import com.mat.modularservers.server.DataTunnelHost;
import com.mat.modularservers.server.TcpServer;
import com.mat.modularservers.util.JavaFxUtil;
import com.mat.modularservers.util.ResourcePath;
import com.mat.modularservers.util.TimeUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


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
        date.textProperty().bind(TimeUtil.getTimeProperty());
        debuggerTextArea.textProperty().bind(TimeUtil.getLogsProperty());
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

    public void createModule(ActionEvent actionEvent) throws Exception {
        if (clientSwitch.isSelected() && !dataSwitch.isSelected()) {
            Tab tab = addLoadingTab(name.getText());
            new Thread(() -> {
                try {
                    FXMLLoader chatView = new FXMLLoader(getClass().getResource(ResourcePath.CHAT_VIEW));
                    Tab chatTab = ((TabPane) chatView.load()).getTabs().get(0);
                    ChatController chatController = chatView.getController();
                    TcpClient client = new TcpClient(address.getText(), Integer.parseInt(port.getText()), new IOBundle<>(new Input<>(chatController.getTextField()), new Output<>(chatController.getTextArea())), Integer.parseInt(timeout.getText()), Integer.parseInt(attempts.getText()));
                    Platform.runLater(() -> {
                        tab.setContent(chatTab.getContent());
                        tab.setOnClosed(e -> client.close());
                    });
                } catch (Exception e) {
                    parent().getTabs().remove(tab);
                }
            }).start();
        } else if (clientSwitch.isSelected() && dataSwitch.isSelected()) {
            FXMLLoader dataView = new FXMLLoader(getClass().getResource(ResourcePath.DATA_VIEW));
            Tab dataTab = ((TabPane) dataView.load()).getTabs().get(0);
            dataTab.setText(name.getText());
            DataController dataController = dataView.getController();
            dataController.host.setText(address.getText());
            dataController.port.setText(port.getText());
            try {
                DataTunnelClient client = new DataTunnelClient(dataController.host.getText(), Integer.parseInt(dataController.port.getText()));
                dataController.send.setOnAction(e -> client.send(dataController.loc.getText()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            Platform.runLater(() -> {
                try {
                    AtomicReference<File> file = new AtomicReference<>();
                    dataController.browse.setOnAction(e -> {
                        file.set(JavaFxUtil.setDownloadLocation());
                        if (file.get() != null) dataController.loc.setText(file.get().getAbsolutePath());
                    });
                    parent().getTabs().add(parent().getTabs().size() - 1, dataTab);
                    parent().getSelectionModel().select(dataTab);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else if (!clientSwitch.isSelected() && dataSwitch.isSelected()) {
            FXMLLoader dataView = new FXMLLoader(getClass().getResource(ResourcePath.DATA_VIEW));
            Tab dataTab = ((TabPane) dataView.load()).getTabs().get(0);
            dataTab.setText(name.getText());
            DataController dataController = dataView.getController();
            dataController.host.setText("localhost");
            dataController.port.setText(port.getText());
            Platform.runLater(() -> {
                try {
                    new Thread(() -> {
                        DataTunnelHost client = null;
                        try {
                            client = new DataTunnelHost(Integer.parseInt(port.getText()));
                            DataTunnelHost finalClient1 = client;
                            dataTab.setOnClosed(e -> finalClient1.close());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        DataTunnelHost finalClient = client;
                        dataController.send.setOnAction(e -> {
                            finalClient.send(dataController.loc.getText());
                        });
                    }).start();
                    AtomicReference<File> file = new AtomicReference<>();
                    dataController.browse.setOnAction(e -> {
                        file.set(JavaFxUtil.setDownloadLocation());
                        if (file.get() != null) dataController.loc.setText(file.get().getAbsolutePath());
                    });
                    parent().getTabs().add(parent().getTabs().size() - 1, dataTab);
                    parent().getSelectionModel().select(dataTab);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else if (!clientSwitch.isSelected() && !dataSwitch.isSelected()) {
            FXMLLoader chatView = new FXMLLoader(getClass().getResource(ResourcePath.CHAT_VIEW));
            Tab chatTab = ((TabPane) chatView.load()).getTabs().get(0);
            ChatController controller = chatView.getController();
            TcpServer tcpServer = new TcpServer(Integer.parseInt(port.getText()));
            tcpServer.setErrorDisplay(new Output<>(controller.getTextArea()));
            tcpServer.setMessageDisplay(new Output<>(controller.getTextArea()));
            tcpServer.attachInput(new Input<>(controller.getTextField()));
            chatTab.setText(name.getText());
            parent().getTabs().add(parent().getTabs().size() - 1, chatTab);
            parent().getSelectionModel().select(chatTab);
            chatTab.setOnClosed(event -> tcpServer.close());
        }
        name.setText("");
    }

    private Tab addLoadingTab(String name) throws IOException {
        FXMLLoader chatTab = new FXMLLoader(getClass().getResource(ResourcePath.LOADING_VIEW));
        Tab tab = ((TabPane) chatTab.load()).getTabs().get(0);
        parent().getTabs().add(parent().getTabs().size() - 1, tab);
        parent().getSelectionModel().select(tab);
        tab.setText(name);
        return tab;
    }

    private TabPane parent() {
        return tab0.getTabPane();
    }
}
package com.mat.modularservers.gui;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

public class HtmlCustomEditor extends HTMLEditor {
    static void init() {
        if (initialized) return;
        initialized = true;
        htmlButton = new Button("</>");
        htmlButton.setOnAction(e -> {
            chosen = 1 - chosen;
            children.set(2, nodes[chosen]);
        });
        ((ToolBar) children.get(1)).getItems().add(htmlButton);
    }

    static ObservableList<Node> children;
    static boolean initialized = false;
    static int chosen = 0;
    static Button htmlButton;
    static TextArea textArea = new TextArea();
    static WebView webView;
    static Node[] nodes;

    HtmlCustomEditor() {
        super();
        children = ((GridPane) getChildren().get(0)).getChildren();
        webView = (WebView) children.get(2);
        nodes  = new Node[]{webView, textArea};
    }
}

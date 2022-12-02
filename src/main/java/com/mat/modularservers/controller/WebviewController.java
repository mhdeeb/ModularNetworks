package com.mat.modularservers.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;

import java.util.Objects;

public class WebviewController {
    public WebView webview;
    public SplitPane splitPane1, splitPane2;
    public HTMLEditor editor;
    public TextArea textArea;
    public VBox rawEditor;
    Button button = new Button("</>");
    public Button htmlButton, sender;
    public CheckBox sendOnEnter;
    String emptyMessage;

    @FXML
    public void initialize() {
        webview.getEngine().load(Objects.requireNonNull(this.getClass().getResource("html/main.html")).toString());
        button.setTooltip(new Tooltip("Show raw HTML script"));
        htmlButton.setTooltip(new Tooltip("Show HTML editor"));
        htmlButton.setOnAction(e -> {
            editor.setHtmlText(textArea.getText());
            splitPane2.getItems().remove(rawEditor);
            splitPane2.getItems().add(editor);
        });
        splitPane2.getItems().remove(rawEditor);
        button.setOnAction(e -> {
            textArea.setText(prettify(editor.getHtmlText()));
            splitPane2.getItems().remove(editor);
            splitPane2.getItems().add(rawEditor);
        });
        getToolBar(editor).getItems().add(button);
        editor.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER) && !(e.isShiftDown() == sendOnEnter.isSelected())) {
                sender.fire();
            }
        });
        sender.setOnAction(e -> {
            String cleaned = clean(editor.getHtmlText());
            if (hasContent(cleaned)) {
                send(cleaned, webview);
            }
        });
        emptyMessage = editor.getHtmlText();
        splitPane1.setDividerPosition(0, 0.6);
    }

    private boolean hasContent(String cleaned) {
        return cleaned.contains("src") || Jsoup.parse(cleaned).hasText();
    }

    private ToolBar getToolBar(HTMLEditor editor) {
        return ((ToolBar) ((GridPane) editor.getChildrenUnmodifiable().get(0)).getChildren().get(0));
    }

    private String prettify(String html) {
        org.jsoup.nodes.Document document = Jsoup.parse(html);
        document.outputSettings().indentAmount(4);
        return document.body().html();
    }

    private String clean(String html) {
        return Jsoup.parse(html).body().html().replace("\n", "");
    }

    void send(String message, WebView webView) {
        String script = """
                div = document.createElement("div");
                div.setAttribute("class", "chat");
                div.innerHTML = "%s";
                document.body.appendChild(div);
                """.formatted(message.replace("\"", "\\\""));
        webView.getEngine().executeScript(script);
        editor.setHtmlText("");
        textArea.setText("");
        requestFocus(editor);
    }

    public void requestFocus(HTMLEditor editor) {
        WebView webView = (WebView) ((GridPane) editor.getChildrenUnmodifiable().get(0)).getChildren().get(2);
        String script = """
                const body = document.body;
                let range;
                body.focus();
                range = document.createRange();
                range.selectNodeContents(body);
                range.collapse(false);
                const sel = window.getSelection();
                sel.removeAllRanges();
                sel.addRange(range);
                """;
        Platform.runLater(() -> {
            try {
                webView.getEngine().executeScript(script);
            } catch (netscape.javascript.JSException e) {
                e.printStackTrace();
            }
        });
    }
}

//package com.mat.modularservers.util;
//
//import org.jsoup.safety.Cleaner;
//import org.jsoup.safety.Safelist;
//import org.w3c.dom.Document;
//
//import javax.xml.transform.*;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//import java.io.*;
//import java.util.Arrays;
//
//public class Utility {
//    private static final Transformer transformer;
//
//    private static final Cleaner cleaner = new Cleaner(Safelist.simpleText());
//
//    private static String written;
//    private static final String[] suggestions = {"\\list", "\\setName", "\\ping", "\\setBate5"};
//
//    static {
//        try {
//            transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
////            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
//        } catch (TransformerConfigurationException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static String printDocument(Document html) throws TransformerException {
//        StringWriter writer = new StringWriter();
//        Utility.transformer().transform(new DOMSource(html.getElementsByTagName("body").item(0)), new StreamResult(writer));
//        String[] out = writer.toString().split("\n");
//        return String.join("\n", Arrays.copyOfRange(out, 1, out.length - 1));
//    }
//
//    static Transformer transformer() {
//        return transformer;
//    }
//
//    public static Cleaner cleaner() {
//        return cleaner;
//    }
//
//    //    static public InlineCssTextField suggestible(InlineCssTextField textField) {
////        textField.textProperty().
////        ContextMenu contextMenu = new ContextMenu();
////        contextMenu.
////        contextMenu.setOnAction(e -> textField.setText(((MenuItem)e.getTarget()).getText()));
////        textField.setContextMenu(contextMenu);
////        textField.setOnKeyTyped(keyEvent -> {
////            written = written.equals("\b") ? truncate(written, 1) : written + keyEvent.getCharacter();
////            if (selected != null && !selected.isEmpty()) {
////                int written = textField.getLength();
////                textField.setText(selected);
////                textField.setStyle(written, selected.length(), "-fx-fill:grey");
////            }
////
////            List<MenuItem> menuItems = Arrays.stream(suggestions).map(suggestion -> {
////                MenuItem menuItem = new MenuItem(suggestion);
////                menuItem.setOnAction(e -> {
////                    textField.setText(menuItem.getText() + " ");
////                    textField.displaceCaret(textField.getText().length());
////                });
////                return menuItem;
////            }).filter(menuItem -> menuItem.getText().startsWith(textField.getText())).toList();
////            contextMenu.getItems().setAll(menuItems);
////            Bounds boundsInScreen = textField.localToScreen(textField.getBoundsInLocal());
////            if (!(textField.getText().isEmpty() || contextMenu.getItems().isEmpty()))
////                contextMenu.show(textField, boundsInScreen.getMinX(), boundsInScreen.getMaxY());
////            else contextMenu.hide();
////        });
////        return textField;
////    }
//
//}

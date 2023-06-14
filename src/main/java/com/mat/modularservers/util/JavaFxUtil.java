package com.mat.modularservers.util;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.File;
import java.util.List;

public class JavaFxUtil {
    public static File getDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        directoryChooser.setTitle("Select Directory");
        return directoryChooser.showDialog(new Popup());
    }

    public static File getFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Select File To Upload");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wave", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("Executable Files", "*.exe", "*.jar"),
                new FileChooser.ExtensionFilter("Compressed Files", "*.zip", "*.gz", "*.rar", "*.7z")
        );
        return fileChooser.showOpenDialog(new Popup());
    }

    public static List<File> getFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Select Files To Upload");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wave", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("Executable Files", "*.exe", "*.jar"),
                new FileChooser.ExtensionFilter("Compressed Files", "*.zip", "*.gz", "*.rar", "*.7z")
        );
        return fileChooser.showOpenMultipleDialog(new Popup());
    }

    public static List<File> getCompressedFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Select Compressed Files To Upload");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Compressed Files", "*.zip", "*.gz", "*.rar", "*.7z")
        );
        return fileChooser.showOpenMultipleDialog(new Popup());
    }

    public static File getCompressedFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Select Compressed File To Upload");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Compressed Files", "*.zip", "*.gz", "*.rar", "*.7z")
        );
        return fileChooser.showOpenDialog(new Popup());
    }

    public static File setDownloadLocation(String fileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setInitialFileName(fileName);
        fileChooser.setTitle("Select Download Location");
        String[] splitDots = fileName.split("\\.");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File", "*." + splitDots[splitDots.length - 1]));
        return fileChooser.showSaveDialog(new Popup());
    }
        public static File setDownloadLocation() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Select Download Location");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All", "*.*"));
        return fileChooser.showOpenDialog(new Popup());
    }
}

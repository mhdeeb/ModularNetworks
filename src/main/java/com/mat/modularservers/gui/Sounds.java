package com.mat.modularservers.gui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sounds {
    static Media beep = new Media(new File("src/main/resources/com/assets/server/beep.mp3").toURI().toString());

    public static void beep() {
        new MediaPlayer(beep).play();
    }
}

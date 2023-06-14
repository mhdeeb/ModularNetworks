package com.mat.modularservers.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private static final StringProperty time = new SimpleStringProperty(getDate());
    private static final StringProperty logs = new SimpleStringProperty();


    static {
        new Thread(() -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                logs.setValue(LoggerBoard.getErrorLogs());
                time.setValue(getDate());
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }).start();
    }

    static public String getDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    static public String getHour() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    static public StringProperty getTimeProperty() {
        return time;
    }

    static public StringProperty getLogsProperty() {
        return logs;
    }

    static public Timeline execute(EventHandler<ActionEvent> f, int frequency, int limit) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(frequency), f));
        timeline.setCycleCount(limit);
        timeline.play();
        return timeline;
    }
}

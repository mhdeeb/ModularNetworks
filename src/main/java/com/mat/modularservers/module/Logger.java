package com.mat.modularservers.module;

import com.mat.modularservers.util.FileUtil;
import com.mat.modularservers.util.LoggerBoard;
import com.mat.modularservers.util.LoggerType;
import com.mat.modularservers.util.TimeUtil;

public class Logger {
    private final StringBuilder log = new StringBuilder();
    private Output display = null;
    private boolean hasDisplay = false;
    private String name = "default";
    private LoggerType type;

    public Logger(LoggerType type) {
        this.type = type;
        LoggerBoard.append(this);
    }

    public Logger(Output output, LoggerType type) {
        this(type);
        hasDisplay = true;
        display = output;
    }

    public Logger(String name, LoggerType type) {
        this(type);
        this.name = name;
    }

    public Logger(String name, Output output, LoggerType type) {
        this(output, type);
        this.name = name;
    }

    public void log(String message) {
        message = String.format("[%s][%s][%s] %s", TimeUtil.getHour(), name, Thread.currentThread().getName(), message);
        if (display != null) display.write(message);
        log.append(message);
    }

    public void log(Exception stackTraceElements) {
        log(stackTraceElements.toString());
        for (StackTraceElement message : stackTraceElements.getStackTrace()) {
            log(message.toString());
        }
    }

    public void logln(String message) {
        log(message + '\n');
    }

    public void logln(Exception stackTraceElements) {
        log(stackTraceElements.toString() + '\n');
        for (StackTraceElement message : stackTraceElements.getStackTrace()) {
            log(message.toString() + '\n');
        }
    }

    public String getLog() {
        return log.toString();
    }

    public void clearLog() {
        log.setLength(0);
    }

    public boolean hasDisplay() {
        return hasDisplay;
    }

    public Output getDisplay() {
        return display;
    }

    public void setDisplay(Output display) {
        if (display != null) {
            hasDisplay = true;
            this.display = display;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoggerType getType() {
        return type;
    }

    public void setType(LoggerType type) {
        this.type = type;
    }

    public void saveToFile(String path) {
        FileUtil.stringToFile(getLog(), path);
    }
}

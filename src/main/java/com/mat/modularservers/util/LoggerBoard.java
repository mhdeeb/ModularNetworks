package com.mat.modularservers.util;

import com.mat.modularservers.module.Logger;
import com.mat.modularservers.module.Output;

import java.util.ArrayList;

public class LoggerBoard {

    private static Logger globalLogger;

    private static final ArrayList<Logger> loggers = new ArrayList<>();

    public static void append(Logger logger) {
        loggers.add(logger);
    }

    public static String getLogs(LoggerType loggerType) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Logger logger : loggers) {
            if (logger.getType() == loggerType)
                stringBuilder.append(logger.getLog());
        }
        return stringBuilder.toString();
    }

    public static String getLog(String loggerName) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Logger logger : loggers) {
            if (logger.getName().equals(loggerName))
                stringBuilder.append(logger.getLog());
        }
        return stringBuilder.toString();
    }

    public static String getAllLogs() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Logger logger : loggers) {
            stringBuilder.append(logger.getLog());
        }
        return stringBuilder.toString();
    }

    public static String getErrorLogs() {
        return getLogs(LoggerType.ERROR);
    }

    public static String getMessageLogs() {
        return getLogs(LoggerType.MESSAGE);
    }

    public static String getGlobalLogs() {
        return getLogs(LoggerType.GLOBAL);
    }

    public static String getDefaultLogs() {
        return getLogs(LoggerType.DEFAULT);
    }

    public static String getLoggerNames() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Logger logger : loggers) {
            stringBuilder.append(String.format("[%s]%s\n", logger.getType(), logger.getName()));
        }
        return stringBuilder.toString();
    }

    public static void saveToFile(String path, LoggerType loggerType) {
        FileUtil.stringToFile(getLogs(loggerType), path);
    }

    public static void saveToFile(String path) {
        FileUtil.stringToFile(getAllLogs(), path);
    }

    public static int size() {
        return loggers.size();
    }

    public static Logger getGlobalLogger() {
        if (globalLogger == null) globalLogger = new Logger("Global", LoggerType.GLOBAL);
        return globalLogger;
    }

}

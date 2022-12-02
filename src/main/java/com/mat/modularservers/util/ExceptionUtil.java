package com.mat.modularservers.util;

import com.mat.modularservers.module.Logger;

public class ExceptionUtil {
    public static <E extends Exception> void ignoreException(RunnableExc r, Class<E> exceptionClass) throws Exception {
        try {
            r.run();
        } catch (Exception e) {
            if (exceptionClass.isInstance(e)) return;
            throw e;
        }
    }

    public static void ignoreExceptions(RunnableExc r) {
        try {
            r.run();
        } catch (Exception ignored) {
        }
    }

    public static <E extends Exception> void logException(RunnableExc r, Logger logger, Class<E> exceptionClass) throws Exception {
        try {
            r.run();
        } catch (Exception e) {
            if (exceptionClass.isInstance(e)) {
                logger.logln(e);
                return;
            }
            throw e;
        }
    }

    public static void logExceptions(RunnableExc r, Logger logger) {
        try {
            r.run();
        } catch (Exception e) {
            logger.logln(e);
        }
    }

    @FunctionalInterface
    public interface RunnableExc {
        void run() throws Exception;
    }
}

package com.mat.modularservers.module;

import com.mat.modularservers.gui.Prompt;
import com.mat.modularservers.util.*;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class DataManager {
    private final SynchronousQueue<Boolean> responder = new SynchronousQueue<>();
    private boolean running = true;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final Logger logger = new Logger(LoggerType.ERROR);
    private final Locker locker = new Locker();
    private final ArrayBlockingQueue<byte[]> codes = new ArrayBlockingQueue<>(1024);
    private final Socket socket;
    private Prompt prompt;

    public DataManager(Socket socket) throws IOException {
        logger.setDisplay(new Output<>(System.out));
        this.socket = socket;
        this.inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        decode();
        sender();
    }

    public void close() {
        running = false;
    }

    public void decode() {
        new Thread(() -> {
            try {
                while (running) {
                    ExceptionUtil.logException(() -> {
                        MessageFlag flag = MessageFlag.fromByte(inputStream.readByte());
                        switch (flag) {
                            case DATA_HEADER -> {
                                long dataSize = inputStream.readLong();
                                byte[] buffer = new byte[inputStream.readInt()];
                                inputStream.readFully(buffer);
                                String fileName = new String(buffer);
                                prompt = new Prompt();
                                Platform.runLater(() -> ExceptionUtil.logExceptions(() -> codes.put(new byte[]{prompt.pop(fileName, dataSize, socket.getInetAddress().getHostName()).toByte()}), logger));
                            }
                            case DATA -> {
                                byte[] buffer = new byte[inputStream.readInt()];
                                inputStream.readFully(buffer);
                                ExceptionUtil.logExceptions(() -> prompt.write(buffer), logger);
                            }
                            case DATA_END ->prompt.complete();
                            case QUIT -> close();
                            case RESPONSE_ACCEPT -> responder.put(true);
                            case RESPONSE_REJECT -> responder.put(false);
                            case REQUEST_PAUSE -> locker.lock();
                            case REQUEST_RESUME -> locker.unlock();
                            case REQUEST_CLOSE -> locker.setBreak(true);
                        }
                    }, logger, InterruptedException.class);
                }
            } catch (Exception e) {
                logger.logln(e);
                close();
            }
        }).start();
    }

    public void file(String path) {
        new Thread(() -> ExceptionUtil.logExceptions(() -> {
            FileData fileData = new FileData(path);
            codes.put(ArrayUtil.concatArray(MessageFlag.DATA_HEADER.toByte(), ByteUtil.longToBytes(fileData.size), ByteUtil.intToBytes(fileData.name.length()), fileData.name.getBytes()));
            if (responder.take()) {
                DataInputStream fileStream = new DataInputStream(new FileInputStream(fileData.file));
                byte[] buffer = new byte[8 * 1024];
                int count;
                while ((count = fileStream.read(buffer)) > 0) {
                    codes.put(ArrayUtil.concatArray(MessageFlag.DATA.toByte(), ByteUtil.intToBytes(count), Arrays.copyOf(buffer, count)));
                    if (locker.breakPoint()) {
                        locker.setBreak(false);
                        break;
                    }
                }
                codes.put(new byte[]{MessageFlag.DATA_END.toByte()});
                fileStream.close();
                System.gc();
            }
        }, logger)).start();
    }

    private void sender() {
        new Thread(() -> {
            while (running) {
                try {
                    ExceptionUtil.logException(() -> outputStream.write(codes.take()), logger, InterruptedException.class);
                } catch (Exception e) {
                    logger.logln(e);
                    close();
                }
            }
        }).start();
    }

    public Logger getLogger() {
        return logger;
    }
}

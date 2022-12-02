package com.mat.modularservers.test;

import com.mat.modularservers.module.Input;
import com.mat.modularservers.module.Output;
import com.mat.modularservers.server.TcpServer;
import com.mat.modularservers.util.Locker;
import com.mat.modularservers.util.LoggerBoard;

import java.util.Scanner;


public class ServerProgram {
    public static Locker locker = new Locker();

    public static void main(String[] args) {
        locker.lock();
        LoggerBoard.getGlobalLogger().setDisplay(new Output<>(System.out));
        TcpServer tcpServer = new TcpServer(5000);
        tcpServer.setErrorDisplay(new Output<>(System.out));
        tcpServer.setMessageDisplay(new Output<>(System.out));
        tcpServer.attachInput(new Input<>(new Scanner(System.in)));
        try {
            locker.breakPoint();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        tcpServer.close();
    }
}

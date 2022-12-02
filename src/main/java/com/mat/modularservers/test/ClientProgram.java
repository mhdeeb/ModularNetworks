package com.mat.modularservers.test;

import com.mat.modularservers.client.TcpClient;
import com.mat.modularservers.module.IOBundle;
import com.mat.modularservers.module.Input;
import com.mat.modularservers.module.Output;
import com.mat.modularservers.util.LoggerBoard;

import java.io.IOException;
import java.util.Scanner;

public class ClientProgram {
    public static void main(String[] args) throws Exception {
        LoggerBoard.getGlobalLogger().setDisplay(new Output<>(System.out));
        new TcpClient("localhost", 5000, new IOBundle<>(new Input<>(new Scanner(System.in)), new Output<>(System.out)), 10, 1000);
    }
}

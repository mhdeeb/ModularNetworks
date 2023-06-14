package com.mat.modularservers.server;

import com.mat.modularservers.module.DataManager;

import java.io.IOException;
import java.net.ServerSocket;

public class DataTunnelHost {
    private final DataManager client;

    public DataTunnelHost(int port) throws IOException {
        client = new DataManager(new ServerSocket(port).accept());
    }

    public void send(String path) {
        client.file(path);
    }

    public void close() {
        client.close();
    }
}

package com.mat.modularservers.client;

import com.mat.modularservers.module.DataManager;

import java.io.IOException;
import java.net.Socket;

public class DataTunnelClient {
    private final DataManager host;

    public DataTunnelClient(String address, int port) throws IOException {
        host = new DataManager(new Socket(address, port));
    }

    public void send(String path) {
        host.file(path);
    }

    public void close() {
        host.close();
    }
}

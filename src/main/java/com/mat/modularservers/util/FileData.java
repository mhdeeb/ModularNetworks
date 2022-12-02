package com.mat.modularservers.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class FileData {
    public final File file;
    public final String path;
    public final String name;
    public final long size;

    public FileData(String path) throws IOException {
        this.path = path;
        file = new File(path);
        name = file.getName();
        size = Files.size(file.toPath());

    }


}

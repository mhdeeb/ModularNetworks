package com.mat.modularservers.util;

import java.util.HashMap;

public class FileMap {
    private final HashMap<String, String> map;
    private final String path;

    public FileMap(String path) {
        this.path = path;
        map = FileUtil.fileToMap(path);
    }

    public void put(String key, String value) {
        map.put(key, value);
        FileUtil.mapToFile(path, map);
    }

    public void remove(String key) {
        map.remove(key);
        FileUtil.mapToFile(path, map);
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public Object get(String key) {
        return map.get(key);
    }
}

package com.mat.modularservers.util;

import java.util.Set;

public class FileSet {
    private final Set<String> set;
    private final String path;

    public FileSet(String path) {
        this.path = path;
        set = FileUtil.fileToSet(path);
    }

    public void add(String element) {
        set.add(element);
        FileUtil.setToFile(path, set);
    }

    public void remove(String element) {
        set.remove(element);
        FileUtil.setToFile(path, set);
    }

    public boolean contains(String key) {
        return set.contains(key);
    }
}
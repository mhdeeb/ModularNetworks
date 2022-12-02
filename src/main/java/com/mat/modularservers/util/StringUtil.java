package com.mat.modularservers.util;

public class StringUtil {
    static String truncate(String string, int length) {
        if (string == null || string.length() < length) return "";
        else return string.substring(0, string.length() - length);
    }
}

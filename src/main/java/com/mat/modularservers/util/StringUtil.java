package com.mat.modularservers.util;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class StringUtil {
    public static String truncate(String string, int length) {
        if (string == null || string.length() < length) return "";
        else return string.substring(0, string.length() - length);
    }

    public static String subString(String string, String separator, int start, int end) {
        var slices = string.split(Pattern.quote(separator));
        if (end < 0) end += slices.length + 1;
        if (slices.length >= end && end > start && start >= 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = start; i < end; i++) sb.append(slices[i]).append(separator);
            sb.delete(sb.length() - separator.length(), sb.length());
            return sb.toString();
        } else return string;
    }

//    public static LinkedList<String> split(String string, String separator) {
//        var slices = new LinkedList<String>();
//        if (string.length() < separator.length()) {
//            slices.add(string);
//            return slices;
//        } else if (string.equals(separator)) {
//            slices.add("");
//            return slices;
//        }
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < string.length(); i++) {
//            String substring = string.substring(i, i + separator.length());
//            if (substring.equals(separator)) {
//                slices.add(sb.toString());
//                sb.delete(0, sb.length());
//            } else sb.append(substring);
//        }
//        return slices;
//    }

    public static String dataFormat(double bytes) {
        long GIGA = 1_000_000_000, MEGA = 1_000_000, KILO = 1_000;
        String string;
        if (bytes > GIGA) {
            bytes /= GIGA;
            string = "GB";
        } else if (bytes > MEGA) {
            bytes /= MEGA;
            string = "MB";
        } else if (bytes > KILO) {
            bytes /= KILO;
            string = "KB";
        } else {
            string = "B";
        }
        return String.format("%.2f %s", bytes, string);
    }
}

package com.mat.modularservers.util;

public class ArrayUtil {
    public static byte[] concatArray(byte b, byte[]... arrays) {
        int size = 0, k = 1;
        for (int i = 0; i < arrays.length; i++) size += arrays[i].length;
        byte[] bytes = new byte[1 + size];
        bytes[0] = b;
        for (int i = 0; i < arrays.length; i++) for (int j = 0; j < arrays[i].length; j++) bytes[k++] = arrays[i][j];
        return bytes;
    }

    public static byte[] concatArray(byte[]... arrays) {
        int size = 0, k = 0;
        for (int i = 0; i < arrays.length; i++) size += arrays[i].length;
        byte[] bytes = new byte[size];
        for (int i = 0; i < arrays.length; i++) for (int j = 0; j < arrays[i].length; j++) bytes[k++] = arrays[i][j];
        return bytes;
    }

    static final String brackets = "{}[]()";
    public static String recursiveToString(Iterable<?> iterable) {
        return recursiveToStringHelper(iterable, 1);
    }
    public static String recursiveToStringHelper(Iterable<?> iterable, int level) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(brackets.charAt(2*(level-1))).append('\n');
        for (Object i : iterable) {
            if (i instanceof Iterable<?> it) {
                stringBuilder.append("\t".repeat(level)).append(recursiveToStringHelper(it, level+1)).append(',').append('\n');
            } else {
                stringBuilder.append("\t".repeat(level)).append(i).append(',').append('\n');
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-2).append("\t".repeat(level-1)).append(brackets.charAt(2*(level-1)+1));
        return stringBuilder.toString();
    }
}

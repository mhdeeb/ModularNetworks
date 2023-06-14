package com.mat.modularservers.util;

import java.util.Random;

public class RandomUtil {
    public static Random random = new Random();

    public static String string(int minSize, int maxSize) {
        if (minSize > maxSize && maxSize < 0) throw new IllegalArgumentException("Size boundary error");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < random.nextInt(minSize, maxSize + 1); i++) sb.append((char) random.nextInt(33, 127));
        return sb.toString();
    }
}

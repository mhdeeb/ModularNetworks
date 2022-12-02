package com.mat.modularservers.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileUtil {

    public static void compress(String source, String target) throws IOException {
        try (GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(target))) {
            Files.copy(Paths.get(source), gos);
        }
    }

    public static void decompress(String source, String target) throws IOException {
        try (GZIPInputStream gis = new GZIPInputStream(new FileInputStream(source))) {
            Files.copy(gis, Paths.get(target));
        }
    }

    public static String fileToString(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (Exception e) {
            LoggerBoard.getGlobalLogger().logln(e);
            return null;
        }
    }

    public static void stringToFile(String text, String path) {
        try (var out = new PrintStream(new FileOutputStream(path))) {
            out.print(text);
        } catch (Exception e) {
            LoggerBoard.getGlobalLogger().logln(e);
        }
    }

    public static Set<String> fileToSet(String path) {
        try (var file = new Scanner(new FileInputStream(path))) {
            Set<String> set = Collections.synchronizedSet(new HashSet<>());
            while (file.hasNext()) set.add(file.nextLine());
            return set;
        } catch (FileNotFoundException f) {
            LoggerBoard.getGlobalLogger().logln(f);
            ExceptionUtil.ignoreExceptions(() -> new File(path).createNewFile());
        } catch (Exception e) {
            LoggerBoard.getGlobalLogger().logln(e);
        }
        return Collections.synchronizedSet(new HashSet<>());
    }

    public static void setToFile(String path, Set<String> set) {
        try (var file = new PrintStream(new FileOutputStream(path))) {
            set.forEach(file::println);
        } catch (Exception e) {
            LoggerBoard.getGlobalLogger().logln(e);
        }
    }

    public static HashMap<String, String> fileToMap(String path) {
        try (var file = new Scanner(new FileInputStream(path))) {
            HashMap<String, String> hashMap = new HashMap<>();
            String line;
            String[] tokens;
            while (file.hasNext()) {
                line = file.nextLine();
                tokens = line.split(" ");
                hashMap.put(tokens[0], tokens[1]);
            }
            return hashMap;
        } catch (FileNotFoundException f) {
            LoggerBoard.getGlobalLogger().logln(f);
            ExceptionUtil.ignoreExceptions(() -> new File(path).createNewFile());
        } catch (Exception e) {
            LoggerBoard.getGlobalLogger().logln(e);
        }
        return new HashMap<>();
    }

    public static void mapToFile(String path, HashMap<String, String> map) {
        try (var file = new PrintStream(new FileOutputStream(path))) {
            map.forEach((key, value) -> file.println(key + " " + value));
        } catch (Exception e) {
            LoggerBoard.getGlobalLogger().logln(e);
        }
    }
}

package com.mygit.index;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Index {

    private static final Path INDEX_FILE = Paths.get(".mygit/index");

    public static Map<String, String> load() throws IOException {
        Map<String, String> entries = new TreeMap<>();
        if(!Files.exists(INDEX_FILE)) {
            return entries;
        }

        for (String line : Files.readAllLines(INDEX_FILE)) {
            if (line.isBlank()) continue;
            String[] parts = line.split(" ", 2);
            if (parts.length == 2) {
                entries.put(parts[1], parts[0]);
            }
        }
        return entries;
    }

    public static void save(Map<String, String> entries) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            sb.append(entry.getValue()).append(" ").append(entry.getKey()).append("\n");
        }
        Files.writeString(INDEX_FILE, sb.toString());
    }
}
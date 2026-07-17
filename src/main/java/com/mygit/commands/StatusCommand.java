package com.mygit.commands;

import com.mygit.index.Index;
import com.mygit.objects.Utils.HashUtils;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class StatusCommand {
    public static void run() throws IOException {
        Map<String, String> index = Index.load();

        List<Path> workingFiles = Files.walk(Paths.get("."))
                .filter(Files::isRegularFile)
                .filter(p -> !p.toString().contains(".mygit"))
                .filter(p -> !p.toString().contains("target"))
                .collect(Collectors.toList());

        Set<String> seen = new HashSet<>();

        for (Path path : workingFiles) {
            String relPath = path.toString();
            // Normalize the path for comparison
            relPath = normalizePath(relPath);
            seen.add(relPath);

            byte[] content = Files.readAllBytes(path);
            String currentHash = hashBlobContent(content);

            if (!index.containsKey(relPath)) {
                System.out.println("Untracked: " + relPath);
            } else if (!index.get(relPath).equals(currentHash)) {
                System.out.println("Modified (unstaged): " + relPath);
            } else {
                System.out.println("Staged: " + relPath);
            }
        }

        for (String path : index.keySet()) {
            if (!seen.contains(path)) {
                System.out.println("Deleted: " + path);
            }
        }
    }

    private static String normalizePath(String path) {
        // Convert backslashes to forward slashes
        path = path.replace("\\", "/");
        // Remove leading ./
        if (path.startsWith("./")) {
            path = path.substring(2);
        }
        return path;
    }

    private static String hashBlobContent(byte[] content) {
        byte[] header = ("blob " + content.length + "\0").getBytes();
        byte[] full = new byte[header.length + content.length];
        System.arraycopy(header, 0, full, 0, header.length);
        System.arraycopy(content, 0, full, header.length, content.length);
        return HashUtils.sha1Hex(full);
    }
}
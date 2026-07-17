package com.mygit.objects;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Tree {

    // Recursively snapshots a directory, returns its hash
    public static String write(Path dir) throws IOException {
        List<Path> entries = Files.list(dir)
                .filter(p -> !p.getFileName().toString().equals(".mygit"))
                .filter(p -> !p.getFileName().toString().equals("target")) // skip Maven build output
                .sorted()
                .collect(Collectors.toList());

        StringBuilder content = new StringBuilder();

        for (Path entry : entries) {
            String name = entry.getFileName().toString();
            if (Files.isDirectory(entry)) {
                String subHash = write(entry); // recurse into subfolder
                content.append("tree ").append(subHash).append(" ").append(name).append("\n");
            } else {
                byte[] fileContent = Files.readAllBytes(entry);
                String hash = GitObject.write("blob", fileContent);
                content.append("blob ").append(hash).append(" ").append(name).append("\n");
            }
        }

        return GitObject.write("tree", content.toString().getBytes());
    }

    public static String writeFromIndex(Map<String, String> index) throws IOException {
        return buildTree(index, "");
    }

    private static String buildTree(Map<String, String> index, String prefix) throws IOException {
        Map<String, String> directBlobs = new TreeMap<>();
        Map<String, Map<String, String>> subDirs = new TreeMap<>();

        for( var e : index.entrySet()) {
            String path = e.getKey();
            if(!path.startsWith(prefix)) continue;
            String remaining = path.substring(prefix.length());
            if(remaining.contains("/")) {
                String subDir = remaining.substring(0, remaining.indexOf("/"));
                subDirs.computeIfAbsent(subDir, k -> new TreeMap<>()).put(path, e.getValue());
            } else {
                directBlobs.put(remaining, e.getValue());
            }
        }

        StringBuilder content = new StringBuilder();
        for (var e: directBlobs.entrySet()) {
            content.append("blob ").append(e.getValue()).append(" ").append(e.getKey()).append("\n");
        }
        for (String dirName : subDirs.keySet()) {
            content.append("tree ").append(buildTree(index, prefix + dirName + "/")).append(" ").append(dirName).append("\n");
        }
        return GitObject.write("tree", content.toString().getBytes());
    }

    public static void restore(String treeHash, Path targetDir) throws IOException {
        String text = new String(GitObject.read(treeHash));
        if(text.isBlank()) return; // empty tree

        for (String line : text.split("\n")) {
            if (line.isBlank()) continue;
            String[] parts = line.split(" ", 3);
            String type = parts[0];
            String hash = parts[1];
            String name = parts[2];


            var entryPath = targetDir.resolve(name);
            if (type.equals("blob")) {
                Files.write(entryPath, GitObject.read(hash));
            } else {
                Files.createDirectories(entryPath);
                restore(hash, entryPath);
            }
        }
    }
}
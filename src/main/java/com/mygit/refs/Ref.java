package com.mygit.refs;

import java.io.IOException;
import java.nio.file.*;

public class Ref {
    public static String getHeadCommit() throws IOException {
        String head = Files.readString(Paths.get(".mygit/HEAD")).trim();
        String refPath = head.substring(5).trim();
        Path full = Paths.get(".mygit", refPath);
        if (!Files.exists(full)) return null;
        return Files.readString(full).trim();
    }

    public static void updateHead(String commitHash) throws IOException {
        String head = Files.readString(Paths.get(".mygit/HEAD")).trim();
        String refPath = head.substring(5).trim();
        Path full = Paths.get(".mygit", refPath);
        Files.createDirectories(full.getParent());
        Files.writeString(full, commitHash + "\n");
    }
}
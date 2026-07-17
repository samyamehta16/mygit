package com.mygit.commands;

import com.mygit.objects.GitObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HashObjectCommand {
    public static void run(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }

        byte[] content = Files.readAllBytes(path);
        String hash = GitObject.write("blob", content);
        System.out.println(hash);
    }
}

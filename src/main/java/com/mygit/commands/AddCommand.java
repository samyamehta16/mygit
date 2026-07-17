package com.mygit.commands;

import com.mygit.index.Index;
import com.mygit.objects.GitObject;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

public class AddCommand {
    public static void run(String filePath) throws IOException {
        // Normalize path: remove leading ./ or .\ and use forward slashes
        String normalizedPath = normalizePath(filePath);
        
        byte[] content = Files.readAllBytes(Paths.get(filePath));
        String hash = GitObject.write("blob", content);

        Map<String, String> index = Index.load();
        index.put(normalizedPath, hash);
        Index.save(index);

        System.out.println("Added " + normalizedPath + " to the index.");
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
}
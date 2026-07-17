package com.mygit.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InitCommand {
    public static void run() throws IOException {
        Path gitDir = Paths.get(".mygit");
        Files.createDirectories(gitDir.resolve("objects"));
        Files.createDirectories(gitDir.resolve("refs/heads"));
        Files.writeString(gitDir.resolve("HEAD"), "ref: refs/heads/main\n");
        System.out.println("Initialized empty MyGit repository in " + gitDir.toAbsolutePath());
    }
}

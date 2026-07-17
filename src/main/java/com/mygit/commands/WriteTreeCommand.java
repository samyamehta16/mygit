package com.mygit.commands;

import com.mygit.objects.Tree;
import java.io.IOException;
import java.nio.file.Paths;

public class WriteTreeCommand {
    public static void run() throws IOException {
        String hash = Tree.write(Paths.get("."));
        System.out.println(hash);
    }
}
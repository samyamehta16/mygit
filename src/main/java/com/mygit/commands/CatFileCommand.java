package com.mygit.commands;

import com.mygit.objects.GitObject;
import java.io.IOException;

public class CatFileCommand {
    public static void run(String hash) throws IOException {
        byte[] content = GitObject.read(hash);
        System.out.print(new String(content));
    }
}

package com.mygit.commands;

import com.mygit.diff.Diff;
import com.mygit.index.Index;
import com.mygit.objects.GitObject;
import java.io.IOException;
import java.nio.file.*;

public class DiffCommand {
    // Compares the STAGED version (what's in the index) vs the CURRENT working file
    public static void run(String filePath) throws IOException {
        var index = Index.load();
        if (!index.containsKey(filePath)) {
            System.out.println(filePath + " is not staged — nothing to compare against.");
            return;
        }

        String stagedHash = index.get(filePath);
        String stagedContent = new String(GitObject.read(stagedHash));
        String workingContent = Files.readString(Paths.get(filePath));

        String[] stagedLines = stagedContent.split("\n", -1);
        String[] workingLines = workingContent.split("\n", -1);

        var diff = Diff.diffLines(stagedLines, workingLines);
        for (String line : diff) {
            System.out.println(line);
        }
    }
}
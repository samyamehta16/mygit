package com.mygit.commands;

import com.mygit.objects.GitObject;
import com.mygit.objects.Tree;
import java.io.IOException;
import java.nio.file.*;

public class CheckoutCommand {
    public static void run(String branchName) throws IOException {
        Path branchPath = Paths.get(".mygit/refs/heads", branchName);
        if(!Files.exists(branchPath)) {
            System.out.println("Branch '" + branchName + "' does not exist.");
            return;
        }

        String commitHash = Files.readString(branchPath).trim();
        String commitText = new String(GitObject.read(commitHash));
        String treeHash = commitText.split("\n")[0].substring(5);

        Tree.restore(treeHash, Paths.get("."));
        Files.writeString(Paths.get(".mygit/HEAD"), "ref: refs/heads/" + branchName + "\n");
        System.out.println("Switched to branch '" + branchName + "'");
    }
}
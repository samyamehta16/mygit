package com.mygit.commands;

import com.mygit.refs.Ref;
import java.io.IOException;
import java.nio.file.*;

public class BranchCommand {
    public static void run(String branchName) throws IOException {
        String current = Ref.getHeadCommit();
        if (current == null) {
            System.out.println("No commits yet. Cannot create a branch.");
            return;
        }

        Files.writeString(Paths.get(".mygit/refs/heads/" + branchName), current + "\n");
        System.out.println("Branch '" + branchName + "' created at commit " + current);
    }
}
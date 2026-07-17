package com.mygit;

import com.mygit.commands.AddCommand;
import com.mygit.commands.CatFileCommand;
import com.mygit.commands.CommitCommand;
import com.mygit.commands.HashObjectCommand;
import com.mygit.commands.InitCommand;
import com.mygit.commands.LogCommand;
import com.mygit.commands.StatusCommand;
import com.mygit.commands.WriteTreeCommand;
import com.mygit.commands.BranchCommand;
import com.mygit.commands.CheckoutCommand;
import com.mygit.commands.DiffCommand;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: init | hash-object <file> | cat-file -p <hash> | add <file> | status | commit -m <message> | log");
            return;
        }

        switch (args[0]) {
            case "init" -> InitCommand.run();
            case "hash-object" -> {
                if (args.length < 2) {
                    System.out.println("Usage: hash-object <file>");
                    return;
                }
                HashObjectCommand.run(args[1]);
            }
            case "cat-file" -> {
                if (args.length < 3 || !"-p".equals(args[1])) {
                    System.out.println("Usage: cat-file -p <hash>");
                    return;
                }
                CatFileCommand.run(args[2]);
            }
            case "write-tree" -> WriteTreeCommand.run();
            case "add" -> {
                if (args.length < 2) {
                    System.out.println("Usage: add <file>");
                    return;
                }
                AddCommand.run(args[1]);
            }
            case "status" -> StatusCommand.run();
            case "commit" -> {
                if (args.length < 3 || !"-m".equals(args[1])) {
                    System.out.println("Usage: commit -m <message>");
                    return;
                }
                CommitCommand.run(args[2]);
            }
            case "log" -> LogCommand.run();
            case "branch" -> {
                if (args.length < 2) {
                    System.out.println("Usage: branch <branch-name>");
                    return;
                }
                BranchCommand.run(args[1]);
            }
            case "checkout" -> {
                if (args.length < 2) {
                    System.out.println("Usage: checkout <branch-name>");
                    return;
                }
                CheckoutCommand.run(args[1]);
            }
            case "diff" -> {
                if (args.length < 2) {
                    System.out.println("Usage: diff <file>");
                    return;
                }
                DiffCommand.run(args[1]);
            }
            default -> System.out.println("Unknown command: " + args[0]);
        }
    }
}

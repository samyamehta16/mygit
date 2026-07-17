package com.mygit.commands;

import com.mygit.objects.GitObject;
import com.mygit.refs.Ref;
import java.io.IOException;
import java.util.Arrays;

public class LogCommand {
    public static void run() throws IOException {
        String current = Ref.getHeadCommit();
        if (current == null) {
            System.out.println("No commits yet.");
            return;
        }

        while (current != null) {
            String text = new String(GitObject.read(current));
            String[] lines = text.split("\n");
            String parent = null;
            int msgStartIndex = 0;

            for (int i = 0; i < lines.length; i++) {
                if (lines[i].startsWith("parent ")) parent = lines[i].substring(7);
                else if (lines[i].isEmpty()) msgStartIndex = i + 1; break;
            }

            String message = String.join("\n", Arrays.copyOfRange(lines, msgStartIndex, lines.length)).trim();

            System.out.println("Commit: " + current);
            System.out.println("Message: " + message + "\n");
            current = parent;
        }
    }
}
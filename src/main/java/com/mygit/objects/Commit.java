package com.mygit.objects;

import java.io.IOException;

public class Commit {
    public static String write(String treeHash, String parentHash, String message) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("tree ").append(treeHash).append("\n");
        if (parentHash != null && !parentHash.isEmpty()) {
            sb.append("parent ").append(parentHash).append("\n");
        }
        sb.append("\n").append(message).append("\n");

        return GitObject.write("commit", sb.toString().getBytes());
    }
}
package com.mygit.objects;

import com.mygit.objects.Utils.HashUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class GitObject {

    public static String write(String type, byte[] content) throws IOException {
        byte[] header = (type + " " + content.length + "\0").getBytes();
        byte[] full = concat(header, content);

        String hash = HashUtils.sha1Hex(full);
        byte[] compressed = deflate(full);

        Path dir = Paths.get(".mygit", "objects", hash.substring(0, 2));
        Files.createDirectories(dir);
        Path file = dir.resolve(hash.substring(2));
        Files.write(file, compressed);

        return hash;
    }

    public static byte[] read(String hash) throws IOException {
        Path file = Paths.get(".mygit", "objects", hash.substring(0, 2), hash.substring(2));
        if (!Files.exists(file)) {
            throw new IOException("Object not found: " + hash);
        }

        byte[] compressed = Files.readAllBytes(file);
        byte[] full = inflate(compressed);

        int nullIndex = 0;
        while (nullIndex < full.length && full[nullIndex] != 0) {
            nullIndex++;
        }
        if (nullIndex >= full.length) {
            throw new IOException("Invalid object format for: " + hash);
        }

        int contentLength = full.length - nullIndex - 1;
        byte[] content = new byte[contentLength];
        System.arraycopy(full, nullIndex + 1, content, 0, contentLength);
        return content;
    }

    private static byte[] concat(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private static byte[] deflate(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        return outputStream.toByteArray();
    }

    private static byte[] inflate(byte[] data) throws IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            try {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            } catch (Exception e) {
                throw new IOException("Failed to inflate data", e);
            }
        }
        return outputStream.toByteArray();
    }
}

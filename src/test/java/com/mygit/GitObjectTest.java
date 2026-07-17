package com.mygit;

import com.mygit.objects.GitObject;
import com.mygit.diff.Diff;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GitObjectTest {

    @BeforeEach
    void setup() throws IOException {
        Files.createDirectories(Paths.get(".mygit/objects"));
    }

    @Test
    void sameContentProducesSameHash() throws IOException {
        String h1 = GitObject.write("blob", "hello".getBytes());
        String h2 = GitObject.write("blob", "hello".getBytes());
        assertEquals(h1, h2);
    }

    @Test
    void differentContentProducesDifferentHash() throws IOException {
        String h1 = GitObject.write("blob", "hello".getBytes());
        String h2 = GitObject.write("blob", "world".getBytes());
        assertNotEquals(h1, h2);
    }

    @Test
    void writeThenReadReturnsOriginalContent() throws IOException {
        String hash = GitObject.write("blob", "roundtrip test".getBytes());
        byte[] content = GitObject.read(hash);
        assertEquals("roundtrip test", new String(content));
    }

    @Test
    void diffDetectsAddedLine() {
        String[] a = {"line one"};
        String[] b = {"line one", "line two"};
        List<String> diff = Diff.diffLines(a, b);
        assertTrue(diff.contains("+ line two"));
    }
}

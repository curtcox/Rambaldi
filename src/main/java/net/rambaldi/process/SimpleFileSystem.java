package net.rambaldi.process;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A simple implementation of FileSystem.
 * It uses the underlying OS filesystem, rooted at a given directory.
 */
public final class SimpleFileSystem
     implements FileSystem, Serializable
{
    private final java.nio.file.Path root;

    public SimpleFileSystem(java.nio.file.Path root) {
        this.root = root;
    }

    @Override
    public void deleteRecursive(Path path) throws IOException {
        deleteRecursive(file(path));
    }

    private File file(Path path) {
        return null;
    }

    /**
     * By default File#delete fails for non-empty directories, it works like "rm".
     * We need something a little more brutal - this does the equivalent of "rm -r"
     * @param path Root File Path
     */
    private static void deleteRecursive(File path) {
        if (!path.exists()) {
            return;
        }
        if (path.isDirectory()){
            for (File f : path.listFiles()){
                deleteRecursive(f);
            }
        }
    }

    @Override
    public void createDirectories(Path path) throws IOException {
        Files.createDirectories(path(path));
    }

    @Override
    public void write(Path path, byte[] bytes) throws IOException {
        Files.write(path(path),bytes);
    }

    @Override
    public byte[] readAllBytes(Path path) throws IOException {
        return Files.readAllBytes(path(path));
    }

    private java.nio.file.Path path(Path path) {
        return null;
    }
}

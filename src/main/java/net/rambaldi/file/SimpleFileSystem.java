package net.rambaldi.file;

import net.rambaldi.file.FileSystem;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * A simple implementation of FileSystem.
 * It uses the underlying OS filesystem, rooted at a given directory.
 */
public final class SimpleFileSystem
     implements FileSystem, Serializable
{
    private final transient Path root;

    public SimpleFileSystem(RelativePath dir) {
        this(fromCurrent(dir));
    }

    public SimpleFileSystem() {
        this(Paths.get(""));
    }

    public SimpleFileSystem(Path root) {
        this.root = requireNonNull(root);
    }

    @Override
    public void deleteRecursive(RelativePath path) throws IOException {
        deleteRecursive(file(path));
    }

    private File file(RelativePath path) {
        return fromRoot(path).toFile();
    }

    /**
     * By default File#delete fails for non-empty directories, it works like "rm".
     * We need something a little more brutal - this does the equivalent of "rm -r"
     * @param path Root File RelativePath
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
    public void createDirectories(RelativePath path) throws IOException {
        Files.createDirectories(fromRoot(path));
    }

    @Override
    public void write(RelativePath path, byte[] bytes) throws IOException {
        Files.write(fromRoot(path),bytes);
    }

    @Override
    public byte[] readAllBytes(RelativePath path) throws IOException {
        return Files.readAllBytes(fromRoot(path));
    }

    /**
     * Return the path relative to the root directory.
     */
    private Path fromRoot(FileSystem.RelativePath relative) {
        Path path = root;
        for (String element : relative.elements()) {
            path = path.resolve(element);
        }
        return path;
    }

    /**
     * Return the path relative to the current directory.
     */
    public static Path fromCurrent(FileSystem.RelativePath path) {
        if (path.elements().isEmpty()) {
            return Paths.get("");
        }
        String first = path.elements().get(0);
        String[] rest = path.elements().subList(1,path.elements().size())
                .toArray(new String[0]);
        return Paths.get(first,rest);
    }
}

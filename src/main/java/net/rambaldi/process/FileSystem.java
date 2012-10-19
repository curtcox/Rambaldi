package net.rambaldi.process;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * How we need to interact with the file system.
 */
public interface FileSystem {

    /**
     * The path to a resource on a FileSystem.
     */
    interface Path {
        Path resolve(String canonicalName);
    }

    /**
     * Delete everything under this path.
     */
    void deleteRecursive(Path path) throws IOException;

    /**
     * Create as many directories as needed to create this path.
     */
    void createDirectories(Path path) throws IOException;

    /**
     * Write the given bytes to this path.
     */
    void write(Path path, byte[] bytes) throws IOException;

    /**
     * Return the contents at this path.
     */
    byte[] readAllBytes(Path path) throws IOException;

}

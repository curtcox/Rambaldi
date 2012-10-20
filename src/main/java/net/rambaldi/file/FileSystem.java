package net.rambaldi.file;

import net.rambaldi.time.Immutable;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * How we need to interact with the file system.
 */
public interface FileSystem {

    /**
     * The path to a resource on a FileSystem.
     * This is conceptually similar to a java.nio.file.Path.
     */
    interface RelativePath extends Immutable, Serializable {
        RelativePath resolve(String canonicalName);

        List<String> elements();

    }

    /**
     * Delete everything under this path.
     */
    void deleteRecursive(RelativePath path) throws IOException;

    /**
     * Create as many directories as needed to create this path.
     */
    void createDirectories(RelativePath path) throws IOException;

    /**
     * Write the given bytes to this path.
     */
    void write(RelativePath path, byte[] bytes) throws IOException;

    /**
     * Return the contents at this path.
     */
    byte[] readAllBytes(RelativePath path) throws IOException;

}

package net.rambaldi.process;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * How we need to interact with the file system.
 */
public interface FileSystem {

    void deleteRecursive(Path path) throws IOException;

    void createDirectories(Path path) throws IOException;

    void write(Path path, byte[] bytes) throws IOException;

    byte[] readAllBytes(Path path) throws IOException;

}

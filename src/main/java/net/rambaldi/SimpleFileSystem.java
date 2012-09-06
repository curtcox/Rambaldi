package net.rambaldi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public final class SimpleFileSystem
     implements FileSystem
{
    @Override
    public void deleteRecursive(Path path) throws IOException {
        deleteRecursive(path.toFile());
    }
    /**
     * By default File#delete fails for non-empty directories, it works like "rm".
     * We need something a little more brutual - this does the equivalent of "rm -r"
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
    }

    @Override
    public void write(Path path, byte[] bytes) throws IOException {
    }

    @Override
    public byte[] readAllBytes(Path path) throws IOException {
        return new byte[0];
    }
}

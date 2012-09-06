package net.rambaldi;

import java.io.IOException;
import java.nio.file.Path;

public class FakeFileSystem
    implements FileSystem
{
    @Override
    public void deleteRecursive(Path path) throws IOException {
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

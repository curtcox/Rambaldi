package net.rambaldi.process;

import net.rambaldi.process.FileSystem;

import java.io.IOException;
import java.nio.file.Path;

public class FakeFileSystem
    implements FileSystem
{
    public Path deleteRecursiveCalledWith;
    public Path createDirectoriesCalledWith;

    @Override public void deleteRecursive(Path path) throws IOException { deleteRecursiveCalledWith = path; }
    @Override public void createDirectories(Path path) throws IOException { createDirectoriesCalledWith = path; }
    @Override public void write(Path path, byte[] bytes) throws IOException {    }
    @Override public byte[] readAllBytes(Path path) throws IOException { return new byte[0]; }
}

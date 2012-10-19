package net.rambaldi.file;

import net.rambaldi.file.FileSystem;

import java.io.IOException;

public class FakeFileSystem
    implements FileSystem
{
    public RelativePath deleteRecursiveCalledWith;
    public RelativePath createDirectoriesCalledWith;

    @Override public void deleteRecursive(RelativePath path) throws IOException { deleteRecursiveCalledWith = path; }
    @Override public void createDirectories(RelativePath path) throws IOException { createDirectoriesCalledWith = path; }
    @Override public void write(RelativePath path, byte[] bytes) throws IOException {    }
    @Override public byte[] readAllBytes(RelativePath path) throws IOException { return new byte[0]; }
}

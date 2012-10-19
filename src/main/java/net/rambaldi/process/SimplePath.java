package net.rambaldi.process;

public final class SimplePath
    implements FileSystem.Path
{
    public SimplePath(String tempDir) {

    }

    @Override
    public FileSystem.Path resolve(String canonicalName) {
        return null;
    }
}

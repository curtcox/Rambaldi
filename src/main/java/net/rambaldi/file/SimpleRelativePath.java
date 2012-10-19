package net.rambaldi.file;

import static net.rambaldi.file.FileSystem.RelativePath;

/**
 * A simple implementation of RelativePath.
 */
public final class SimpleRelativePath
    implements RelativePath
{
    public SimpleRelativePath(String tempDir) {

    }

    @Override
    public RelativePath resolve(String canonicalName) {
        return null;
    }
}

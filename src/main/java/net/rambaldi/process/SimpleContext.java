package net.rambaldi.process;

import net.rambaldi.file.FileSystem;
import net.rambaldi.file.SimpleFileSystem;

import java.io.Serializable;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

/**
 * A simple implementation of Context.
 * @author Curt
 */
public final class SimpleContext
    implements Context, Serializable
{

    private Serializable state;
    private final FileSystem fileSystem;

    public SimpleContext(FileSystem fileSystem) {
        this.fileSystem = requireNonNull(fileSystem);
    }

    public SimpleContext() {
        this(new SimpleFileSystem(Paths.get("")));
    }

    @Override
    public Entity getEntity(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Serializable getState() {
        return state;
    }

    @Override
    public void setState(Serializable state) {
        this.state = state;
    }

    @Override
    public FileSystem getFileSystem() {
        return fileSystem;
    }
}

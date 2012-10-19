package net.rambaldi.process;

import java.io.Serializable;
import java.util.Objects;

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
        this(new SimpleFileSystem(null));
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

    public FileSystem getFileSystem() {
        return fileSystem;
    }
}

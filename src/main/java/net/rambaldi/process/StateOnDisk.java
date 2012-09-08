package net.rambaldi.process;

import java.io.IOException;
import java.nio.file.Path;
import static java.util.Objects.*;

/**
 * Persistent state on disk.
 */
public final class StateOnDisk {

    public final Path path;
    private final FileSystem fileSystem;
    private final ObjectStore store;
    private RequestProcessor processor;

    public StateOnDisk(Path dir, IO io, FileSystem fileSystem) {
        this(dir,new SimpleObjectStore(dir,io,fileSystem),fileSystem);
    }

    public StateOnDisk(Path dir, ObjectStore store, FileSystem fileSystem) {
        path = requireNonNull(dir);
        this.store = requireNonNull(store);
        this.fileSystem = requireNonNull(fileSystem);
    }

    public void delete() throws IOException {
        fileSystem.deleteRecursive(path);
    }

    public void setProcessor(RequestProcessor processor) {
        this.processor = processor;
    }

    public void persist() throws IOException {
        fileSystem.createDirectories(path);
        store.write(RequestProcessor.class, processor);
    }

    public void load() {
        processor = store.read(RequestProcessor.class);
    }

    public RequestProcessor getProcessor() {
        return processor;
    }
}

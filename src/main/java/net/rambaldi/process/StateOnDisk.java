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
    private RequestProcessor requestProcessor;

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

    public void setRequestProcessor(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    public void persist() throws IOException {
        fileSystem.createDirectories(path);
        store.write(RequestProcessor.class, requestProcessor);
    }

    public void load() {
        requestProcessor = store.read(RequestProcessor.class);
    }

    public RequestProcessor getRequestProcessor() {
        return requestProcessor;
    }
}

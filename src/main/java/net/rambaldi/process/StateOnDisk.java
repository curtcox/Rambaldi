package net.rambaldi.process;

import net.rambaldi.file.FileSystem;

import java.io.IOException;

import static java.util.Objects.*;

/**
 * Persistent state on disk.
 */
public final class StateOnDisk {

    public final FileSystem.RelativePath path;
    private final FileSystem fileSystem;
    private final ObjectStore store;
    private RequestProcessor requestProcessor;

    public StateOnDisk(FileSystem.RelativePath dir, IO io, FileSystem fileSystem) {
        this(dir,new SimpleObjectStore(dir,io,fileSystem),fileSystem);
    }

    public StateOnDisk(FileSystem.RelativePath dir, ObjectStore store, FileSystem fileSystem) {
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

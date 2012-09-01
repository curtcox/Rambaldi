package net.rambaldi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Persistent state on disk.
 */
public final class StateOnDisk {

    public final Path path;
    private final ObjectStore store;
    private RequestProcessor processor;

    public StateOnDisk(Path dir, IO io) {
        this(dir,new SimpleObjectStore(dir,io));
    }
    public StateOnDisk(Path dir, ObjectStore store) {
        path = Objects.requireNonNull(dir);
        this.store = Objects.requireNonNull(store);
    }

    public void delete() throws IOException {
        FileUtils.deleteRecursive(path.toFile());
        Files.delete(path);
    }

    public void setProcessor(RequestProcessor processor) {
        this.processor = processor;
    }

    public void persist() throws IOException {
        Files.createDirectories(path);
        store.write(RequestProcessor.class, processor);
    }

    public void load() {
        processor = store.read(RequestProcessor.class);
    }

    public RequestProcessor getProcessor() {
        return processor;
    }
}

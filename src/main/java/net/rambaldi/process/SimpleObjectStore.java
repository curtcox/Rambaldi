package net.rambaldi.process;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import static java.util.Objects.*;

public final class SimpleObjectStore implements ObjectStore {

    private final FileSystem.Path path;
    private final IO io;
    private final FileSystem fileSystem;

    public SimpleObjectStore(FileSystem.Path path, IO io, FileSystem fileSystem) {
        this.path = requireNonNull(path);
        this.io = requireNonNull(io);
        this.fileSystem = requireNonNull(fileSystem);
    }

    @Override
    public <T extends Serializable> void write(Class<T> type, T t) throws SerializationException {
        try {
            fileSystem.write(path(type), io.serialize(t));
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T read(Class<T> type) throws DeserializationException {
        try {
            return type.cast(io.deserialize(fileSystem.readAllBytes(path(type))));
        } catch (IOException e) {
            throw new DeserializationException(e);
        }
    }

    private FileSystem.Path path(Class type) {
        return path.resolve(type.getCanonicalName());
    }
}

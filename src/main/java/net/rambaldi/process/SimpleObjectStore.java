package net.rambaldi.process;

import net.rambaldi.file.FileSystem;

import java.io.IOException;
import java.io.Serializable;

import static java.util.Objects.*;

public final class SimpleObjectStore implements ObjectStore {

    private final FileSystem.RelativePath path;
    private final IO io;
    private final FileSystem fileSystem;

    public SimpleObjectStore(FileSystem.RelativePath path, IO io, FileSystem fileSystem) {
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

    private FileSystem.RelativePath path(Class type) {
        return path.resolve(type.getCanonicalName());
    }
}

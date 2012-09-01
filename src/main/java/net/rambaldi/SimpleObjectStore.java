package net.rambaldi;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.util.Objects.*;

public class SimpleObjectStore implements ObjectStore {

    private final Path path;
    private final IO io;

    public SimpleObjectStore(Path path, IO io) {
        this.path = requireNonNull(path);
        this.io = requireNonNull(io);
    }

    @Override
    public <T extends Serializable> void write(Class<T> type, T t) throws SerializationException {
        try {
            Files.write(path(type), io.serialize(t));
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T read(Class<T> type) throws DeserializationException {
        try {
            return type.cast(io.deserialize(Files.readAllBytes(path(type))));
        } catch (IOException e) {
            throw new DeserializationException(e);
        }
    }

    private Path path(Class type) {
        return path.resolve(type.getCanonicalName());
    }
}

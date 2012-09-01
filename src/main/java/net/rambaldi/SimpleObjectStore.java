package net.rambaldi;

import java.io.Serializable;
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
    public <T extends Serializable> void write(Class<T> clazz, T t) throws SerializationException {
    }

    @Override
    public <T> T read(Class<T> t) throws DeserializationException {
        return null;
    }
}

package net.rambaldi;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleObjectStoreTest {

    Path path = Paths.get("temp");
    IO io = new SimpleIO();

    @Test(expected = NullPointerException.class)
    public void constructor_requires_path() {
        path = null;
        new SimpleObjectStore(path,io);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_io() {
        io = null;
        new SimpleObjectStore(path,io);
    }

}

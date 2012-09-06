package net.rambaldi;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleObjectStoreTest {

    Path path = Paths.get("temp");
    IO io = new SimpleIO();
    FileSystem fileSystem = new FakeFileSystem();

    @Test(expected = NullPointerException.class)
    public void constructor_requires_path() {
        new SimpleObjectStore(null,io,fileSystem);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_io() {
        new SimpleObjectStore(path,null,fileSystem);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_filesystem() {
        new SimpleObjectStore(path,io,null);
    }

}

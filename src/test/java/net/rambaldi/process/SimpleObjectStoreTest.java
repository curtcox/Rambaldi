package net.rambaldi.process;

import net.rambaldi.file.FakeFileSystem;
import net.rambaldi.file.FileSystem;
import org.junit.Test;

public class SimpleObjectStoreTest {

    FileSystem.RelativePath path = null;//Paths.get("temp");
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

package net.rambaldi.file;

import net.rambaldi.file.SimpleFileSystem;
import org.junit.Test;

import java.nio.file.Paths;

public class SimpleFileSystemTest {

    @Test
    public void can_create() {
        new SimpleFileSystem(Paths.get(""));
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_root() {
        new SimpleFileSystem(null);
    }

}

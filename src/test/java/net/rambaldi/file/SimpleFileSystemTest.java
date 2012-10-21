package net.rambaldi.file;

import net.rambaldi.file.SimpleFileSystem;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleFileSystemTest {

    @Test
    public void can_create() {
        new SimpleFileSystem(Paths.get(""));
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_root() {
        new SimpleFileSystem(null);
    }

    @Test(expected = NullPointerException.class)
    public void path_requires_path() {
        SimpleFileSystem.path(null);
    }

    @Test
    public void path_produces_path_that_contains_relative_path_name() {
        assertTrue(SimpleFileSystem.path(new SimpleRelativePath("stuff")).toString().contains("stuff"));
    }

    @Test
    public void path_produces_current_directory_for_empty_relative_path() {
        assertEquals(Paths.get(""),SimpleFileSystem.path(new SimpleRelativePath()));
    }

    @Test
    public void path_produces_file_in_current_directory_for_single_item_relative_path() {
        assertEquals(Paths.get("file.txt"),SimpleFileSystem.path(new SimpleRelativePath("file.txt")));
    }

}

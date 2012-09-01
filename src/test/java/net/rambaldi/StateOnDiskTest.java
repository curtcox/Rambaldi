package net.rambaldi;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.*;

public class StateOnDiskTest {

    Path path = Paths.get("dir");
    ObjectStore store = new FakeObjectStore();
    StateOnDisk state = new StateOnDisk(path,store);

    @Before
    public void Before() throws Exception {
        Files.deleteIfExists(path);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_path() {
        new StateOnDisk(null, store);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_store() {
        new StateOnDisk(path, (IO) null);
    }

    @Test
    public void path_uses_path_from_constructor() {
        StateOnDisk state = new StateOnDisk(path,store);
        assertEquals(path,state.path);
    }

    @Test
    public void persist_creates_path_on_disk() throws Exception{
        assertFalse(Files.exists(path));

        state.persist();

        assertTrue(Files.exists(path));
        assertTrue(Files.isDirectory(path));
    }

    @Test
    public void getProcessor_return_processor_set() {
        EchoProcessor processor = new EchoProcessor();

        state.setProcessor(processor);

        assertEquals(processor, state.getProcessor());
    }

    @Test
    public void persist_preserves_path_on_disk() throws Exception{
        assertFalse(Files.exists(path));
        StateOnDisk state1 = new StateOnDisk(path,store);
        EchoProcessor processor = new EchoProcessor();
        state1.setProcessor(processor);
        state1.persist();
        StateOnDisk state2 = new StateOnDisk(path,store);
        state2.load();
        assertEquals(processor, state2.getProcessor());
    }

    @Test
    public void delete_removes_dir() throws Exception {
        assertFalse(Files.exists(path));
        StateOnDisk state = new StateOnDisk(path,store);
        state.persist();
        assertTrue(Files.exists(path));
        state.delete();
        assertFalse(Files.exists(path));
    }
}

package net.rambaldi.process;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.*;

public class StateOnDiskTest {

    Path             path = Paths.get("dir");

    FakeObjectStore     store = new FakeObjectStore();
    FakeFileSystem fileSystem = new FakeFileSystem();
    FakeIO                 io = new FakeIO();

    StateOnDisk     state = new StateOnDisk(path,store,fileSystem);

    @Before
    public void Before() throws Exception {
        Files.deleteIfExists(path);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_path() {
        new StateOnDisk(null, store,fileSystem);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_store() {
        new StateOnDisk(path, (ObjectStore) null, fileSystem);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_fileSystem() {
        new StateOnDisk(path, store, null);
    }

    @Test
    public void path_uses_path_from_constructor() {
        StateOnDisk state = new StateOnDisk(path,store,fileSystem);
        assertEquals(path,state.path);
    }

    @Test
    public void persist_uses_fileSystem_to_create_path_on_disk() throws Exception{
        state.persist();

        assertSame(path, fileSystem.createDirectoriesCalledWith);
    }

    @Test
    public void getProcessor_returns_processor_set() {
        EchoProcessor processor = new EchoProcessor();

        state.setProcessor(processor);

        assertEquals(processor, state.getProcessor());
    }

    @Test
    public void getProcessor_returns_null_if_it_has_not_been_loaded() {
        RequestProcessor actual = state.getProcessor();
        assertEquals(null,actual);
    }

    @Test
    public void getProcessor_returns_processor_if_it_has_been_loaded() {
        EchoProcessor expected = new EchoProcessor();
        store.write(RequestProcessor.class,expected);
        state.load();
        RequestProcessor actual = state.getProcessor();
        assertEquals(expected,actual);
    }

    @Test
    public void persist_preserves_path_on_disk() throws Exception{
        assertFalse(Files.exists(path));
        StateOnDisk state1 = new StateOnDisk(path,store,fileSystem);
        EchoProcessor processor = new EchoProcessor();
        state1.setProcessor(processor);
        state1.persist();
        StateOnDisk state2 = new StateOnDisk(path,store,fileSystem);
        state2.load();
        assertEquals(processor, state2.getProcessor());
    }

    @Test
    public void delete_calls_fileSystem_to_remove_dir() throws Exception {
        StateOnDisk state = new StateOnDisk(path,io,fileSystem);

        state.delete();

        assertSame(path, fileSystem.deleteRecursiveCalledWith);
    }
}

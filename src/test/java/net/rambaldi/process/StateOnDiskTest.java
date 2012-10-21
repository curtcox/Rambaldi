package net.rambaldi.process;

import net.rambaldi.file.FakeFileSystem;
import net.rambaldi.file.FileSystem;
import net.rambaldi.file.SimpleFileSystem;
import net.rambaldi.file.SimpleRelativePath;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static net.rambaldi.file.SimpleFileSystem.fromCurrent;
import static org.junit.Assert.*;

public class StateOnDiskTest {

    FileSystem.RelativePath path = new SimpleRelativePath("dir");

    FakeObjectStore     store = new FakeObjectStore();
    FakeFileSystem fileSystem = new FakeFileSystem();
    FakeIO                 io = new FakeIO();

    StateOnDisk     state = new StateOnDisk(path,store,fileSystem);

    @Before
    public void Before() throws Exception {
        Files.deleteIfExists(fromCurrent(path));
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

        state.setRequestProcessor(processor);

        assertEquals(processor, state.getRequestProcessor());
    }

    @Test
    public void getProcessor_returns_null_if_it_has_not_been_loaded() {
        RequestProcessor actual = state.getRequestProcessor();
        assertEquals(null,actual);
    }

    @Test
    public void getProcessor_returns_processor_if_it_has_been_loaded() {
        EchoProcessor expected = new EchoProcessor();
        store.write(RequestProcessor.class,expected);
        state.load();
        RequestProcessor actual = state.getRequestProcessor();
        assertEquals(expected,actual);
    }

    @Test
    public void persist_preserves_path_on_disk() throws Exception{
        assertFalse(Files.exists(SimpleFileSystem.fromCurrent(path)));;
        StateOnDisk state1 = new StateOnDisk(path,store,fileSystem);
        EchoProcessor processor = new EchoProcessor();
        state1.setRequestProcessor(processor);
        state1.persist();
        StateOnDisk state2 = new StateOnDisk(path,store,fileSystem);
        state2.load();
        assertEquals(processor, state2.getRequestProcessor());
    }

    @Test
    public void delete_calls_fileSystem_to_remove_dir() throws Exception {
        StateOnDisk state = new StateOnDisk(path,io,fileSystem);

        state.delete();

        assertSame(path, fileSystem.deleteRecursiveCalledWith);
    }

}

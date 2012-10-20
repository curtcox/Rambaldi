package net.rambaldi.process;

import net.rambaldi.Log.Log;
import net.rambaldi.Log.FakeLog;
import net.rambaldi.file.FileSystem;
import net.rambaldi.file.SimpleFileSystem;
import net.rambaldi.file.SimpleRelativePath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TransactionProcessorsTest {

    StateOnDisk state;
    FileSystem.RelativePath path = new SimpleRelativePath("tempDir");
    IO io = new SimpleIO();
    Log log = new FakeLog();
    FileSystem fileSystem = new SimpleFileSystem(Paths.get(""));

    @Before
    public void before() throws IOException {
        fileSystem.deleteRecursive(path);
        fileSystem.createDirectories(path);
        state = new StateOnDisk(path,new FakeObjectStore(),fileSystem);
    }

    @After
    public void after() throws IOException {
        fileSystem.deleteRecursive(path);
    }

    @Test(expected=NullPointerException.class)
    public void newExternal_requires_state() throws Exception {
        state = null;
        TransactionProcessors.newExternal(state,io,log);
    }

    @Test
    public void newExternal_returns_processor() throws Exception {
        TransactionProcessor processor = TransactionProcessors.newExternal(state,io,log);
        assertNotNull(processor);
    }

}

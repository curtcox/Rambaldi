package net.rambaldi.process;

import net.rambaldi.Log.Log;
import net.rambaldi.log.FakeLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TransactionProcessorsTest {

    StateOnDisk state;
    Path path = Paths.get("tempDir");
    IO io = new SimpleIO();
    Log log = new FakeLog();
    FileSystem fileSystem = new SimpleFileSystem();

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

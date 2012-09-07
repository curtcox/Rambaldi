package net.rambaldi;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TransactionProcessorsTest {

    StateOnDisk state;

    @Before
    public void Before() {
        state = new StateOnDisk(Paths.get(""),new FakeObjectStore(),new FakeFileSystem());
    }

    @Test(expected=NullPointerException.class)
    public void newExternal_requires_state() {
        state = null;
        TransactionProcessors.newExternal(state);
    }

    @Test
    public void newExternal_returns_processor() {
        StreamServer server = TransactionProcessors.newExternal(state);
        assertNotNull(server);
    }

}

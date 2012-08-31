package net.rambaldi;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class TransactionProcessorsTest {

    OutputStream out;
    OutputStream err;
    InputStream in;
    StateOnDisk state;

    @Before
    public void Before() {
        in = new ByteArrayInputStream(new byte[0]);
    }

    @Test(expected=NullPointerException.class)
    public void newExternal_requires_in() {
        in = null;
        TransactionProcessors.newExternal(in,out, err, state);
    }

    @Test
    public void newExternal_returns_processor() {
        StreamServer server = TransactionProcessors.newExternal(in, out, err, state);
        assertNotNull(server);
    }
}

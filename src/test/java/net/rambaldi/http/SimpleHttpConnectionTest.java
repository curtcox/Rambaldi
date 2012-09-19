package net.rambaldi.http;

import net.rambaldi.process.FakeInputStream;
import net.rambaldi.process.FakeOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertTrue;

public class SimpleHttpConnectionTest {

    @Test
    public void can_create() {
        new SimpleHttpConnection(new ByteArrayInputStream(new byte[0]),new ByteArrayOutputStream());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_input() {
        new SimpleHttpConnection(null,new ByteArrayOutputStream());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_output() {
        new SimpleHttpConnection(new ByteArrayInputStream(new byte[0]),null);
    }

    @Test
    public void close() throws Exception {
        FakeInputStream in = new FakeInputStream(new byte[0]);
        FakeOutputStream out = new FakeOutputStream();

        SimpleHttpConnection connection = new SimpleHttpConnection(in,out);
        connection.close();

        assertTrue(connection.isClosed());
        assertTrue(in.closed);
        assertTrue(out.closed);
    }
}

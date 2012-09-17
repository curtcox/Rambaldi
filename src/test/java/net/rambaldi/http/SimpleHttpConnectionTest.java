package net.rambaldi.http;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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

}

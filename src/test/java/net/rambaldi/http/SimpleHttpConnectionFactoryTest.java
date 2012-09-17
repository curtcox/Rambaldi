package net.rambaldi.http;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;

public class SimpleHttpConnectionFactoryTest {

    @Test
    public void can_create() throws Exception {
        try ( AutoCloseable first = newInstance(); ){}
    }

    private SimpleHttpConnectionFactory newInstance() throws IOException {
        return new SimpleHttpConnectionFactory(4242);
    }

    @Test(expected = IOException.class)
    public void can_only_create_one_at_a_time_per_port() throws Exception {
        try (
            AutoCloseable first = newInstance();
            AutoCloseable second = newInstance();
        ){}
    }

    @Test
    public void can_create_one_after_another_on_the_same_port() throws Exception {
        try ( AutoCloseable first = newInstance(); ){}
        try ( AutoCloseable second = newInstance(); ){}
    }

}

package net.rambaldi.http;

import com.sun.net.httpserver.HttpServer;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;

public class SimpleHttpServerTest {

    ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
    HttpRequestReader reader = new HttpRequestReader(in);
    HttpEchoProcessor echo = new HttpEchoProcessor();

    @Test
    public void can_create() {
        new SimpleHttpServer(echo,0);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_processor() {
        new SimpleHttpServer(null,0);
    }

    @Test
    public void start_starts_reading_requests() {
        SimpleHttpServer server = new SimpleHttpServer(echo,0);
        server.start();
        fail();
    }
}

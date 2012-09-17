package net.rambaldi.http;

import net.rambaldi.process.Request;
import net.rambaldi.process.Timestamp;
import net.rambaldi.process.Transaction;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;

import static net.rambaldi.http.HttpRequest.Connection;
import static net.rambaldi.http.HttpRequest.Method;
import static org.junit.Assert.*;

public class HttpRequestReaderTest {

    @Test
    public void can_create() {
        new HttpRequestReader(new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_input_stream() {
        new HttpRequestReader(null);
    }

    @Test(expected = Exception.class)
    public void take_throws_exception_at_end_of_stream() {
        HttpRequestReader reader = new HttpRequestReader(new ByteArrayInputStream(new byte[0]));
        reader.take();
    }

    @Test
    public void get_root_with_close() {
        HttpRequest request = takeFrom("GET / HTTP/1.1\r\nHost: www.example.com\r\nConnection: close\r\n\r\n");
        assertEquals(Method.GET,request.method);
        assertEquals("/",request.resource);
        assertEquals("www.example.com",request.host);
        assertEquals(Connection.close,request.connection);
    }

    @Test
    public void get_root_with_Java_URL() {
        HttpRequest request = takeFrom(
            "GET / HTTP/1.1\r\nUser-Agent: Java/1.7.0\r\nHost: localhost:4242\r\nAccept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\nConnection: keep-alive\r\n\r\n"
        );
        assertEquals(Method.GET,request.method);
        assertEquals("/",request.resource);
        assertEquals("localhost:4242",request.host);
        assertEquals("Java/1.7.0",request.userAgent);
        assertEquals("text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2",request.accept.toString());
        assertEquals("localhost:4242",request.host);
        assertEquals(Connection.keep_alive,request.connection);
    }

    @Test
    public void get_named_resource_with_close() {
        HttpRequest request = takeFrom("GET /named_resource HTTP/1.1\r\nHost: named.net\r\nConnection: close\r\n\r\n");
        assertEquals(Method.GET,request.method);
        assertEquals("/named_resource",request.resource);
        assertEquals("named.net",request.host);
        assertEquals(Connection.close,request.connection);
    }

    @Test
    public void can_take_2_requests() {
        String stream =
            "GET /1 HTTP/1.1\r\nHost: host1\r\nConnection: keep-alive\r\n\r\n" +
            "GET /2 HTTP/1.1\r\nHost: host2\r\nConnection: keep-alive\r\n\r\n"
        ;
        HttpRequestReader reader = new HttpRequestReader(new ByteArrayInputStream(stream.getBytes()));
        assertEquals(HttpRequest.builder().resource("/1").host("host1").build(),reader.take());
        assertEquals(HttpRequest.builder().resource("/2").host("host2").build(),reader.take());
    }
    @Test
    public void can_take_3_requests() {
        String stream =
                "GET /1 HTTP/1.1\r\nHost: host1\r\nConnection: keep-alive\r\n\r\n" +
                "GET /2 HTTP/1.1\r\nHost: host2\r\nConnection: keep-alive\r\n\r\n" +
                "GET /3 HTTP/1.1\r\nHost: host3\r\nConnection: keep-alive\r\n\r\n"
        ;
        HttpRequestReader reader = new HttpRequestReader(new ByteArrayInputStream(stream.getBytes()));
        assertEquals(HttpRequest.builder().resource("/1").host("host1").build(),reader.take());
        assertEquals(HttpRequest.builder().resource("/2").host("host2").build(),reader.take());
        assertEquals(HttpRequest.builder().resource("/3").host("host3").build(),reader.take());
    }

    HttpRequest takeFrom(String requestString) {
        HttpRequestReader reader = new HttpRequestReader(new ByteArrayInputStream(requestString.getBytes()));
        return reader.take();
    }
}

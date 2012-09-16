package net.rambaldi.http;

import net.rambaldi.process.Request;
import net.rambaldi.process.Timestamp;
import net.rambaldi.process.Transaction;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;

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
    }

    @Test
    public void can_take_2_requests() {
        String stream =
            "GET /1 HTTP/1.1\r\nHost: host1\r\nConnection: keep-alive\r\n\r\n" +
            "GET /2 HTTP/1.1\r\nHost: host2\r\nConnection: keep-alive\r\n\r\n"
        ;
        HttpRequestReader reader = new HttpRequestReader(new ByteArrayInputStream(stream.getBytes()));
        assertEquals(HttpRequest.builder().build(),reader.take());
        assertEquals(HttpRequest.builder().build(),reader.take());
    }
    @Test
    public void can_take_3_requests() {
        String stream =
                "GET /1 HTTP/1.1\r\nHost: host1\r\nConnection: keep-alive\r\n\r\n" +
                "GET /2 HTTP/1.1\r\nHost: host2\r\nConnection: keep-alive\r\n\r\n" +
                "GET /3 HTTP/1.1\r\nHost: host3\r\nConnection: keep-alive\r\n\r\n"
        ;
        HttpRequestReader reader = new HttpRequestReader(new ByteArrayInputStream(stream.getBytes()));
        assertEquals(HttpRequest.builder().build(),reader.take());
        assertEquals(HttpRequest.builder().build(),reader.take());
        assertEquals(HttpRequest.builder().build(),reader.take());
    }

    HttpRequest takeFrom(String requestString) {
        HttpRequestReader reader = new HttpRequestReader(new ByteArrayInputStream(requestString.getBytes()));
        return reader.take();
    }
}

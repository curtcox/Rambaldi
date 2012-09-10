package net.rambaldi.http;

import net.rambaldi.process.Request;
import net.rambaldi.process.Transaction;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
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
        assertEquals(HttpRequest.Method.GET,request.method);
        assertEquals("/",request.resource);
    }

    HttpRequest takeFrom(String requestString) {
        HttpRequestReader reader = new HttpRequestReader(new ByteArrayInputStream(requestString.getBytes()));
        return reader.take();
    }
}

package net.rambaldi.http;

import net.rambaldi.process.*;
import org.junit.Test;

import java.io.*;

import static net.rambaldi.http.HttpRequest.Connection;
import static net.rambaldi.http.HttpRequest.Connection.close;
import static net.rambaldi.http.HttpRequest.Method;
import static net.rambaldi.http.HttpRequest.Method.GET;
import static net.rambaldi.http.HttpRequest.builder;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

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
        assertEquals(GET,request.method);
        assertEquals("/",request.resource.name);
        assertEquals("www.example.com",request.host);
        assertEquals(close,request.connection);
    }

    @Test
    public void get_root_with_Java_URL() {
        HttpRequest request = takeFrom(
            "GET / HTTP/1.1\r\nUser-Agent: Java/1.7.0\r\nHost: localhost:4242\r\nAccept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\nConnection: keep-alive\r\n\r\n"
        );
        assertEquals(GET,request.method);
        assertEquals("/",request.resource.name);
        assertEquals("localhost:4242",request.host);
        assertEquals("Java/1.7.0",request.userAgent);
        assertEquals("text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2",request.accept.toString());
        assertEquals("localhost:4242",request.host);
        assertEquals(Connection.keep_alive,request.connection);
    }

    @Test
    public void get_named_resource_with_close() {
        HttpRequest request = takeFrom("GET /named_resource HTTP/1.1\r\nHost: named.net\r\nConnection: close\r\n\r\n");
        assertEquals(GET,request.method);
        assertEquals("/named_resource",request.resource.name);
        assertEquals("named.net",request.host);
        assertEquals(close,request.connection);
    }

    @Test
    public void can_take_2_requests() {
        HttpRequestReader reader = new HttpRequestReader(stream(
            "GET /1 HTTP/1.1|Host: host1|Connection: keep-alive",
            "GET /2 HTTP/1.1|Host: host2|Connection: keep-alive"
        ));
        assertEquals(HttpRequest.builder().resource("/1").host("host1").build(),reader.take());
        assertEquals(HttpRequest.builder().resource("/2").host("host2").build(),reader.take());
    }

    InputStream stream(String... request) {
        StringBuilder out = new StringBuilder();
        for (String line : request) {
            out.append(line.replace("|","\r\n"));
            out.append("\r\n\r\n\r\n");
        }
        return new ByteArrayInputStream(out.toString().getBytes());
    }

    @Test
    public void can_take_3_requests() {
        HttpRequestReader reader = new HttpRequestReader(stream(
            "GET /1 HTTP/1.1|Host: host1|Connection: keep-alive",
            "GET /2 HTTP/1.1|Host: host2|Connection: keep-alive",
            "GET /3 HTTP/1.1|Host: host3|Connection: keep-alive"
        ));
        assertEquals(HttpRequest.builder().resource("/1").host("host1").build(),reader.take());
        assertEquals(HttpRequest.builder().resource("/2").host("host2").build(),reader.take());
        assertEquals(HttpRequest.builder().resource("/3").host("host3").build(),reader.take());
    }

    @Test
    public void can_take_two_connections_when_first_is_keep_alive() throws Exception {

        HttpRequest request1 = builder().resource("/first_resource").connection(Connection.keep_alive).build();
        HttpRequest request2 = builder().resource("/second_resource").connection(Connection.close).build();
        FakeInputStream input = requestFromStream(
                request1,
                request2
        );

        HttpRequestReader reader = new HttpRequestReader(input);

        assertEquals(request1, reader.take());
        assertEquals(request2, reader.take());
    }

    FakeInputStream requestFromStream(HttpRequest... requests) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (HttpRequest request : requests) {
            out.write((request.toString() + "\r\n\r\n").getBytes());
        }
        return new FakeInputStream(out.toByteArray());
    }

    HttpRequest takeFrom(String requestString) {
        HttpRequestReader reader = new HttpRequestReader(new ByteArrayInputStream(requestString.getBytes()));
        return reader.take();
    }
}

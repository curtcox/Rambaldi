package net.rambaldi.http;

import net.rambaldi.process.Timestamp;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static net.rambaldi.http.HttpRequest.Connection;
import static net.rambaldi.http.HttpRequest.Version;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HttpRequestTest {

    @Test
    public void can_create() {
        HttpRequest request = HttpRequest.builder().build();
        assertNotNull(request);
    }

    @Test(expected = NullPointerException.class)
    public void resource_must_not_be_null() {
        HttpRequest.builder().resource(null);
    }

    @Test(expected = NullPointerException.class)
    public void version_must_not_be_null() {
        HttpRequest.builder().version(null);
    }

    @Test(expected = NullPointerException.class)
    public void method_must_not_be_null() {
        HttpRequest.builder().method(null);
    }

    @Test
    public void method_line() {
        String expected = "GET /path/file.html HTTP/1.0";
        String actual = HttpRequest.builder()
                .resource("/path/file.html")
                .version(Version._1_0)
                .build().method();
        assertEquals(expected,actual);
    }

    @Test
    public void from_line() {
        String expected = "From: someuser@jmarshall.com";
        String actual = HttpRequest.builder()
                .from("someuser@jmarshall.com")
                .build().from();
        assertEquals(expected,actual);
    }

    @Test
    public void host_line() throws Exception {
        String expected = "Host: www.example.com";
        String actual = HttpRequest.builder()
                .host("www.example.com")
                .build().host();
        assertEquals(expected,actual);
    }

    @Test
    public void connection_line() throws Exception {
        String expected = "Connection: close";
        String actual = HttpRequest.builder()
                .connection(Connection.close)
                .build().connection();
        assertEquals(expected,actual);
    }

    @Test
    public void userAgent_line() {
        String expected = "User-Agent: HTTPTool/1.0";
        String actual = HttpRequest.builder()
                .userAgent("HTTPTool/1.0")
                .build().userAgent();
        assertEquals(expected,actual);
    }

    @Test
    public void toString_returns_formatted_request_1() {
        String expected = lines(
             "GET /path/file.html HTTP/1.0",
             "From: someuser@jmarshall.com",
             "User-Agent: HTTPTool/1.0");
        HttpRequest request = HttpRequest.builder()
            .resource("/path/file.html")
            .version(Version._1_0)
            .from("someuser@jmarshall.com")
            .userAgent("HTTPTool/1.0")
            .build();
        String actual = request.toString();
        assertEquals(expected,actual);
    }

    @Test
    public void toString_returns_formatted_request_2() throws Exception {
        String expected = lines(
                "GET / HTTP/1.1",
                "Host: www.example.com",
                "Connection: close"
                );
        HttpRequest request = HttpRequest.builder()
                .resource("/")
                .version(Version._1_1)
                .host("www.example.com")
                .connection(Connection.close)
                .build();
        String actual = request.toString();
        assertEquals(expected,actual);
    }


    private String lines(String... lines) {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line + "\r\n");
        }
        return builder.toString();
    }

}

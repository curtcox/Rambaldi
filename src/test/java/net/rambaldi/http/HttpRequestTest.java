package net.rambaldi.http;

import net.rambaldi.process.Timestamp;
import org.junit.Test;
import tests.acceptance.Copier;

import java.net.URI;
import java.net.URISyntaxException;

import static net.rambaldi.http.HttpRequest.Accept;
import static net.rambaldi.http.HttpRequest.Connection;
import static net.rambaldi.http.HttpRequest.Version;
import static org.junit.Assert.*;

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

    @Test(expected = NullPointerException.class)
    public void connection_must_not_be_null() {
        HttpRequest.builder().connection(null);
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
    public void accept_line() {
        String expected = "Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2";
        String actual = HttpRequest.builder()
                .accept(new Accept("text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2"))
                .build().accept();
        assertEquals(expected,actual);
    }

    @Test
    public void user_agent_line() {
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
             "User-Agent: HTTPTool/1.0",
             "Connection: keep-alive");
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

    @Test
    public void toString_returns_formatted_request_3() throws Exception {
        String expected = lines(
            "GET / HTTP/1.1",
            "User-Agent: Java/1.7.0",
            "Host: localhost:4242",
            "Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2",
            "Connection: keep-alive"
        );
        HttpRequest request = HttpRequest.builder()
                .resource("/")
                .version(Version._1_1)
                .userAgent("Java/1.7.0")
                .host("localhost:4242")
                .accept(new Accept("text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2"))
                .connection(Connection.keep_alive)
                .build();
        String actual = request.toString();
        assertEquals(expected,actual);
    }

    @Test
    public void is_serializable() throws Exception {
        HttpRequest original = HttpRequest.builder()
                .resource("/")
                .version(Version._1_1)
                .userAgent("Java/1.7.0")
                .host("localhost:4242")
                .accept(new Accept("text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2"))
                .connection(Connection.keep_alive)
                .build();
        HttpRequest copy = Copier.copy(original);
        assertEquals(original,copy);
    }

    private String lines(String... lines) {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line + "\r\n");
        }
        return builder.toString();
    }

    @Test
    public void equals_implies_equal_hosts() {
        HttpRequest.Builder builder = HttpRequest.builder();
        assertEquals(builder.host("host1").build(),builder.host("host1").build());
        assertNotEquals(builder.host("host1").build(),builder.host("host2").build());
    }

    @Test
    public void equals_implies_equal_resources() {
        HttpRequest.Builder builder = HttpRequest.builder();
        assertEquals(builder.resource("resource1").build(),builder.resource("resource1").build());
        assertNotEquals(builder.resource("resource1").build(),builder.resource("resource2").build());
    }

    void assertNotEquals(HttpRequest request1, HttpRequest request2) {
        assertFalse(request1 + " should != " + request2,request1.equals(request2));
    }
}

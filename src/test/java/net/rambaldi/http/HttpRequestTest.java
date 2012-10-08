package net.rambaldi.http;

import net.rambaldi.process.Timestamp;
import org.junit.Test;
import tests.acceptance.Copier;

import java.net.URI;
import java.net.URISyntaxException;

import static net.rambaldi.http.HttpRequest.*;
import static net.rambaldi.http.HttpRequest.Connection.keep_alive;
import static net.rambaldi.http.HttpRequest.ContentType.*;
import static net.rambaldi.http.HttpRequest.Method.POST;
import static org.junit.Assert.*;

public class HttpRequestTest {

    @Test
    public void can_create() {
        HttpRequest request = builder().build();
        assertNotNull(request);
    }

    @Test(expected = NullPointerException.class)
    public void resource_must_not_be_null() {
        builder().resource(null);
    }

    @Test(expected = NullPointerException.class)
    public void version_must_not_be_null() {
        builder().version(null);
    }

    @Test(expected = NullPointerException.class)
    public void method_must_not_be_null() {
        builder().method(null);
    }

    @Test(expected = NullPointerException.class)
    public void connection_must_not_be_null() {
        builder().connection(null);
    }

    @Test(expected = NullPointerException.class)
    public void content_must_not_be_null() {
        builder().content(null);
    }

    @Test
    public void method_line() {
        String expected = "GET /path/file.html HTTP/1.0";
        String actual = builder()
                .resource("/path/file.html")
                .version(Version._1_0)
                .build().method();
        assertEquals(expected,actual);
    }

    @Test
    public void from_line() {
        String expected = "From: someuser@jmarshall.com";
        String actual = builder()
                .from("someuser@jmarshall.com")
                .build().from();
        assertEquals(expected,actual);
    }

    @Test
    public void host_line() throws Exception {
        String expected = "Host: www.example.com";
        String actual = builder()
                .host("www.example.com")
                .build().host();
        assertEquals(expected,actual);
    }

    @Test
    public void connection_line() throws Exception {
        String expected = "Connection: close";
        String actual = builder()
                .connection(Connection.close)
                .build().connection();
        assertEquals(expected,actual);
    }

    @Test
    public void accept_line() {
        String expected = "Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2";
        String actual = builder()
                .accept(new Accept("text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2"))
                .build().accept();
        assertEquals(expected,actual);
    }

    @Test
    public void user_agent_line() {
        String expected = "User-Agent: HTTPTool/1.0";
        String actual = builder()
                .userAgent("HTTPTool/1.0")
                .build().userAgent();
        assertEquals(expected,actual);
    }

    @Test
    public void content_type_line_when_specified() {
        String expected = "Content-Type: application/x-www-form-urlencoded";
        String actual = builder()
                .contentType(UrlEncodedForm)
                .build().contentType();
        assertEquals(expected,actual);
    }

    @Test
    public void content_type_line_when_unspecified() {
        String actual = builder().build().contentType();
        assertEquals("",actual);
    }

    @Test
    public void content_length_line_when_content_specified() {
        String expected = "Content-Length: 32";
        String actual = builder().content("home=Cosby&favorite+flavor=flies")
                .build().contentLength();
        assertEquals(expected,actual);
    }

    @Test
    public void content_length_line_when_unspecified() {
        String actual = builder().build().contentLength();
        assertEquals("",actual);
    }

    @Test
    public void content_line_when_content_specified() {
        String expected = "home=Cosby&favorite+flavor=flies";
        String actual = builder().content("home=Cosby&favorite+flavor=flies")
                .build().content();
        assertEquals(expected,actual);
    }

    @Test
    public void content_line_when_unspecified() {
        String actual = builder().build().content();
        assertEquals("",actual);
    }

    @Test
    public void toString_returns_formatted_request_1() {
        String expected = lines(
             "GET /path/file.html HTTP/1.0",
             "From: someuser@jmarshall.com",
             "User-Agent: HTTPTool/1.0",
             "Connection: keep-alive");
        HttpRequest request = builder()
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
        HttpRequest request = builder()
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
        HttpRequest request = builder()
                .resource("/")
                .version(Version._1_1)
                .userAgent("Java/1.7.0")
                .host("localhost:4242")
                .accept(new Accept("text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2"))
                .connection(keep_alive)
                .build();
        String actual = request.toString();
        assertEquals(expected,actual);
    }

    @Test
    public void toString_returns_formatted_request_4() throws Exception {
        String expected = lines(
            "POST /path/script.cgi HTTP/1.0",
            "From: frog@jmarshall.com",
            "User-Agent: HTTPTool/1.0",
            "Content-Type: application/x-www-form-urlencoded",
            "Content-Length: 32",
            "Connection: keep-alive",
            "",
            "home=Cosby&favorite+flavor=flies"
        );
        HttpRequest request = builder()
                .method(POST)
                .from("frog@jmarshall.com")
                .resource("/path/script.cgi")
                .version(Version._1_0)
                .userAgent("HTTPTool/1.0")
                .content("home=Cosby&favorite+flavor=flies")
                .contentType(UrlEncodedForm)
                .build();
        String actual = request.toString();
        assertEquals(expected,actual);
    }

    @Test
    public void is_serializable() throws Exception {
        HttpRequest original = builder()
                .resource("/")
                .version(Version._1_1)
                .userAgent("Java/1.7.0")
                .host("localhost:4242")
                .accept(new Accept("text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2"))
                .connection(keep_alive)
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
        assertEquals(builder().host("host1").build(),builder().host("host1").build());
        assertNotEquals(builder().host("host1").build(),builder().host("host2").build());
    }

    @Test
    public void equals_implies_equal_resources() {
        assertEquals(builder().resource("resource1").build(),builder().resource("resource1").build());
        assertNotEquals(builder().resource("resource1").build(),builder().resource("resource2").build());
    }

    void assertNotEquals(HttpRequest request1, HttpRequest request2) {
        assertFalse(request1 + " should != " + request2,request1.equals(request2));
    }

    Builder builder() {
        return HttpRequest.builder();
    }

    @Test public void query_string_when_no_params() { assertQueryString(""); }
    @Test public void query_string_when_params_1()  { assertQueryString("k=v","k","v"); }

    private void assertQueryString(String expected, String... params) {
        String actual = builder().params(params).build().queryString;
        assertEquals(expected,actual);
    }
}

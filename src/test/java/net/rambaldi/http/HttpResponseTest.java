package net.rambaldi.http;

import net.rambaldi.process.Timestamp;
import org.junit.Test;

import static net.rambaldi.http.HttpRequest.Method;
import static net.rambaldi.http.HttpResponse.ContentType;
import static net.rambaldi.http.HttpResponse.Status;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class HttpResponseTest {

    HttpRequest request = new HttpRequest("",new Timestamp(0), Method.GET);

    @Test(expected = NullPointerException.class)
    public void builder_requires_content() {
        HttpResponse.builder().content(null);
    }

    @Test(expected = NullPointerException.class)
    public void builder_requires_content_type() {
        HttpResponse.builder().contentType(null);
    }

    @Test(expected = NullPointerException.class)
    public void builder_requires_status() {
        HttpResponse.builder().status(null);
    }

    @Test(expected = NullPointerException.class)
    public void builder_requires_date() {
        HttpResponse.builder().date(null);
    }

    @Test(expected = NullPointerException.class)
    public void builder_requires_request() {
        HttpResponse.builder().build();
    }

    @Test
    public void builder_only_requires_request() {
        HttpResponse response = HttpResponse.builder().request(request).build();
        assertNotNull(response);
        assertNotNull(response.content);
        assertNotNull(response.contentType);
        assertNotNull(response.status);
        assertNotNull(response.date);
    }

    @Test
    public void status_format_is_correct() {
        HttpResponse response = HttpResponse.builder().content("1234").request(request).build();
        assertEquals("HTTP/1.0 200 OK",response.status());
    }

    @Test
    public void content_length_format_is_correct() {
        HttpResponse response = HttpResponse.builder().content("1234").request(request).build();
        assertEquals("Content-Length: 4",response.contentLength());
    }

    @Test
    public void content_date_format_is_correct() {
        HttpResponse response = HttpResponse.builder().request(request).build();
        assertEquals("Date: Thu, 01 Jan 1970 00:00:00 GMT",response.date());
    }

    @Test
    public void content_type_format_is_correct() {
        HttpResponse response = HttpResponse.builder().contentType(ContentType.TextHtml).request(request).build();
        assertEquals("Content-Type: text/html",response.contentType());
    }

    @Test
    public void builder_uses_request() {
        HttpResponse response = HttpResponse.builder().request(request).build();
        assertSame(request,response.request);
    }

    @Test
    public void Http_1_0_200_OK_HTML() {
        String response = HttpResponse.builder()
                .request(new HttpRequest("", new Timestamp(0), Method.GET))
                .status(Status.OK)
                .date(new Timestamp(0))
                .contentType(ContentType.TextHtml)
                .content("<html></html>")
                .build()
                .toString();
        String expected = lines(
            "HTTP/1.0 200 OK",
            "Date: Thu, 01 Jan 1970 00:00:00 GMT",
            "Content-Type: text/html",
            "Content-Length: 13",
            "",
            "<html></html>"
        );
        assertEquals(expected,response);
    }

    private String lines(String... lines) {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line + "\r\n");
        }
        return builder.toString();
    }

}

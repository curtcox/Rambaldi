package net.rambaldi.http;

import net.rambaldi.process.Request;
import net.rambaldi.process.Response;
import net.rambaldi.time.Timestamp;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HttpResponseWriterTest {

    @Test
    public void can_create() {
        new HttpResponseWriter(new ByteArrayOutputStream());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_output_stream() {
        new HttpResponseWriter(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void put_requires_HttpResponse() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpResponseWriter writer = new HttpResponseWriter(out);
        writer.put(new Response("",new Request("",new Timestamp(0))));
    }

        @Test
    public void put_writes_response_string_output_to_stream() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpResponseWriter writer = new HttpResponseWriter(out);
        HttpResponse     response = newHttpResponse();

        writer.put(response);

        assertEquals(response.toString(), new String(out.toByteArray()));
    }

    private HttpResponse newHttpResponse() {
        HttpRequest request = HttpRequest.builder().build();
        return HttpResponse.builder()
                .content("")
                .request(request)
                .build();
    }
}

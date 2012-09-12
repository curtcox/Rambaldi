package net.rambaldi.http;

import net.rambaldi.process.Timestamp;
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

    @Test
    public void put_writes_response_string_output_to_stream() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpResponseWriter writer = new HttpResponseWriter(out);
        HttpRequest request = new HttpRequest("",new Timestamp(0), HttpRequest.Method.GET);
        HttpResponse response = HttpResponse.builder()
                .content("")
                .request(request)
                .build();

        writer.put(response);

        assertEquals(response.toString(), new String(out.toByteArray()));
    }
}

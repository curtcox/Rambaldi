package net.rambaldi.http;

import net.rambaldi.process.Context;
import net.rambaldi.process.FakeInputStream;
import net.rambaldi.process.FakeOutputStream;
import org.junit.Test;

import java.io.*;

import static net.rambaldi.http.HttpRequest.*;
import static org.junit.Assert.*;

public class SimpleHttpConnectionHandlerTest {

    HttpRequestProcessor processor = new HttpRequestEchoProcessor();

    @Test
    public void implements_HttpConnectionHandler() {
        assertTrue(new SimpleHttpConnectionHandler(processor) instanceof HttpConnection.Handler);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_requires_processor() {
        new SimpleHttpConnectionHandler(null);
    }

    @Test
    public void handle_returns_response_from_RequestProcessor() throws Exception {

        HttpRequest request = builder().connection(Connection.close).build();
        FakeInputStream input = requestFromStream(request);
        FakeOutputStream output = new FakeOutputStream();
        HttpConnection connection = new SimpleHttpConnection(input,output);
        final HttpResponse fromProcessor = HttpResponse.builder().request(request).build();
        HttpRequestProcessor processor = new HttpRequestProcessor() {
            @Override
            public HttpResponse process(HttpRequest request, Context context) {
                return fromProcessor;
            }
        };

        SimpleHttpConnectionHandler handler = new SimpleHttpConnectionHandler(processor);
        handler.handle(connection,null);

        String written = output.toString();

        assertEquals(fromProcessor.toString(), written);
    }

    @Test
    public void handle_closes_connection_when_request_is_not_keep_alive() throws Exception {

        FakeInputStream input = requestFromStream(builder().connection(Connection.close).build());
        FakeOutputStream output = new FakeOutputStream();
        HttpConnection connection = new SimpleHttpConnection(input,output);

        SimpleHttpConnectionHandler handler = new SimpleHttpConnectionHandler(processor);
        handler.handle(connection,null);

        assertTrue(connection.isClosed());
        assertTrue(input.closed);
        assertTrue(output.closed);
    }

    @Test
    public void handle_does_not_close_connection_when_request_is_keep_alive() throws Exception {

        FakeInputStream input = requestFromStream(builder().connection(Connection.keep_alive).build());
        FakeOutputStream output = new FakeOutputStream();
        HttpConnection connection = new SimpleHttpConnection(input,output);

        SimpleHttpConnectionHandler handler = new SimpleHttpConnectionHandler(processor);
        handler.handle(connection,null);

        assertFalse(connection.isClosed());
        assertFalse(input.closed);
        assertFalse(output.closed);
    }

    private FakeInputStream requestFromStream(HttpRequest request) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(request.toString().getBytes());
        return new FakeInputStream(out.toByteArray());
    }

}

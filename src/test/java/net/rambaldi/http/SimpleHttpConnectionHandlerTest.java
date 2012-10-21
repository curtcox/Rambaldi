package net.rambaldi.http;

import net.rambaldi.process.FakeInputStream;
import net.rambaldi.process.FakeOutputStream;
import net.rambaldi.process.SimpleContext;
import org.junit.Test;

import java.io.*;

import static net.rambaldi.http.HttpRequest.*;
import static org.junit.Assert.*;

public class SimpleHttpConnectionHandlerTest {

    HttpTransactionProcessor processor = new HttpTransactionProcessor() {
        @Override
        public HttpResponse process(HttpRequest request) throws Exception {
            return HttpResponse.builder().request(request).build();
        }
    };

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
        HttpTransactionProcessor processor = new HttpTransactionProcessor() {
            @Override
            public HttpResponse process(HttpRequest request) {
                return fromProcessor;
            }
        };

        SimpleHttpConnectionHandler handler = new SimpleHttpConnectionHandler(processor);
        handler.handle(connection);

        String written = output.toString();

        assertEquals(fromProcessor.toString(), written);
    }

    @Test
    public void handle_closes_connection_when_request_is_not_keep_alive() throws Exception {

        FakeInputStream input = requestFromStream(builder().connection(Connection.close).build());
        FakeOutputStream output = new FakeOutputStream();
        HttpConnection connection = new SimpleHttpConnection(input,output);

        SimpleHttpConnectionHandler handler = new SimpleHttpConnectionHandler(processor);
        handler.handle(connection);

        assertTrue(connection.isClosed());
        assertTrue(input.closed);
        assertTrue(output.closed);
    }

    @Test
    public void handle_reads_two_connections_when_first_is_keep_alive() throws Exception {

        HttpRequest request1 = builder().resource("/first_resource").connection(Connection.keep_alive).build();
        HttpRequest request2 = builder().resource("/second_resource").connection(Connection.close).build();
        FakeInputStream input = requestFromStream(
                request1,
                request2
        );
        FakeOutputStream output = new FakeOutputStream();
        HttpConnection connection = new SimpleHttpConnection(input,output);

        HttpRequestEchoProcessor requestProcessor = new HttpRequestEchoProcessor();
        HttpTransactionProcessor processor = new SimpleHttpTransactionProcessor(requestProcessor,new SimpleContext());
        SimpleHttpConnectionHandler handler = new SimpleHttpConnectionHandler(processor);
        handler.handle(connection);

        assertTrue(connection.isClosed());
        assertTrue(input.closed);
        assertTrue(output.closed);

        String written = output.toString();
        String response1 = requestProcessor.process(request1, null).toString();
        String response2 = requestProcessor.process(request2, null).toString();
        assertTrue(written + " does not contain" + response1,written.contains(response1));
        assertTrue(written + " does not contain" + response2,written.contains(response2));
    }

    FakeInputStream requestFromStream(HttpRequest... requests) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (HttpRequest request : requests) {
            out.write(request.toString().getBytes());
            out.write("\r\n\r\n".getBytes());
        }
        return new FakeInputStream(out.toByteArray());
    }

}

package net.rambaldi.http;

import net.rambaldi.process.Context;
import net.rambaldi.process.SimpleContext;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static net.rambaldi.http.HttpRequest.Connection.*;

public final class SimpleHttpConnectionHandler
    implements HttpConnection.Handler
{
    private final HttpTransactionProcessor processor;

    public SimpleHttpConnectionHandler() {
        this(new SimpleHttpTransactionProcessor(new HttpRequestEchoProcessor(), new SimpleContext()));
    }

    public SimpleHttpConnectionHandler(HttpTransactionProcessor processor) {
        this.processor = requireNonNull(processor);
    }

    @Override
    public void handle(HttpConnection connection) throws Exception {
        HttpRequestReader  reader = new HttpRequestReader(connection.getInputStream());
        HttpResponseWriter writer = new HttpResponseWriter(connection.getOutputStream());
        try (AutoCloseable x = connection) {
            for (HttpRequest request = processSingleRequest(reader,writer);
                 request.connection == keep_alive;
                 request = processSingleRequest(reader,writer));
        }
    }

    private HttpRequest processSingleRequest(HttpRequestReader  reader, HttpResponseWriter writer) throws Exception {
        HttpRequest request = reader.take();
        writer.put(processor.process(request));
        return request;
    }
}

package net.rambaldi.http;

import net.rambaldi.process.Context;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static net.rambaldi.http.HttpRequest.Connection.*;

public final class SimpleHttpConnectionHandler
    implements HttpConnection.Handler
{
    private final HttpRequestProcessor processor;

    public SimpleHttpConnectionHandler(HttpRequestProcessor processor) {
        this.processor = requireNonNull(processor);
    }

    @Override
    public void handle(HttpConnection connection, Context context) throws Exception {
        HttpRequestReader  reader = new HttpRequestReader(connection.getInputStream());
        HttpRequest       request = reader.take();
        HttpResponse     response = processor.process(request,context);
        HttpResponseWriter writer = new HttpResponseWriter(connection.getOutputStream());
        writer.put(response);
        if (request.connection!= keep_alive) {
            connection.close();
        }
    }
}

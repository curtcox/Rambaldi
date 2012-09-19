package net.rambaldi.http;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

import static java.util.Objects.requireNonNull;

/**
 * A very simple HTTP server.
 */
public final class SimpleHttpServer
    implements AutoCloseable
{
    private final HttpTransactionProcessor processor;
    private final Executor executor;
    private final HttpConnection.Factory connectionFactory;
    private final Callable acceptCallable;

    public SimpleHttpServer(Executor executor, HttpConnection.Factory serverSocketProvider, HttpTransactionProcessor processor) {
        this.executor = requireNonNull(executor);
        this.connectionFactory = requireNonNull(serverSocketProvider);
        this.processor = requireNonNull(processor);
        acceptCallable = acceptNewSocket();
    }

    private Callable acceptNewSocket() {
        return new Callable() {
            @Override
            public Object call() throws Exception {
               acceptSocket();
               return null;
            }
        };
    }

    public void start() {
        executor.execute(new FutureTask(acceptCallable));
    }

    private void acceptSocket() throws Exception {
        HttpConnection connection = connectionFactory.accept();
        HttpRequestReader  reader = new HttpRequestReader(connection.getInputStream());
        HttpRequest       request = reader.take();
        HttpResponse     response = processor.process(request);
        HttpResponseWriter writer = new HttpResponseWriter(connection.getOutputStream());
        writer.put(response);
    }

    @Override
    public void close() throws Exception {
        connectionFactory.close();
    }
}

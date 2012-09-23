package net.rambaldi.http;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import static java.util.Objects.requireNonNull;

/**
 * A very simple HTTP server.
 */
public final class SimpleHttpServer
    implements AutoCloseable
{
    private final HttpConnection.Handler handler;
    private final ExecutorService executor;
    private final HttpConnection.Factory connectionFactory;
    private final Callable acceptCallable;

    public SimpleHttpServer(ExecutorService executor, HttpConnection.Factory serverSocketProvider, HttpConnection.Handler handler) {
        this.executor = requireNonNull(executor);
        this.connectionFactory = requireNonNull(serverSocketProvider);
        this.handler = requireNonNull(handler);
        acceptCallable = acceptNewSocket();
    }

    private Callable acceptNewSocket() {
        return new Callable() {
            @Override
            public Object call() throws Exception {
                while (!executor.isShutdown()) {
                    handle(connectionFactory.accept());
                }
                return null;
            }
        };
    }

    public void start() {
        executor.execute(new FutureTask(acceptCallable));
    }

    private void handle(final HttpConnection connection) throws Exception {
        executor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                handler.handle(connection);
                return null;
            }
        });
    }

    @Override
    public void close() throws Exception {
        connectionFactory.close();
    }
}

package net.rambaldi.http;

import net.rambaldi.process.Context;

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
    private final HttpConnection.Handler handler;
    private final Executor executor;
    private final HttpConnection.Factory connectionFactory;
    private final Callable acceptCallable;

    public SimpleHttpServer(Executor executor, HttpConnection.Factory serverSocketProvider, HttpConnection.Handler handler) {
        this.executor = requireNonNull(executor);
        this.connectionFactory = requireNonNull(serverSocketProvider);
        this.handler = requireNonNull(handler);
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
        handler.handle(connectionFactory.accept());
    }

    @Override
    public void close() throws Exception {
        connectionFactory.close();
    }
}
